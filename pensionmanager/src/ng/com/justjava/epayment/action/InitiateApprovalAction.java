package ng.com.justjava.epayment.action;

import java.math.*;
import java.util.*;
import java.util.concurrent.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Approver.ApprovableTransaction;
import ng.com.justjava.epayment.model.ProcessingFeeParameters.AccountToDebit;
import ng.com.justjava.epayment.model.ProcessingFeeParameters.DeductionMethod;
import ng.com.justjava.epayment.model.Payment.*;
import ng.com.justjava.epayment.utility.*;

import org.apache.commons.lang.math.*;
import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

import com.google.common.eventbus.*;

public class InitiateApprovalAction extends TabBaseAction { // 1

	public void execute() throws Exception {

		Map[] selectedKeys = getSelectedKeys();
		if(selectedKeys.length<=0){
			addMessage("No Record Selected", null);
			return;
		}
		
		
		PaymentBatch batch = new PaymentBatch();
		
		
		String response = "";
		if (UserManager.getCorporateOfLoginUser() == null) {
			addError("Invalid User for Payment Upload", null);
			return;
		}
		if (!UserManager.approverExists(1)) {
			addError("Approver Route Not Yet Setup ", null);
			return;
		}
		if (UserManager.isLoginUserApprover()) {
			addError("Uploader cannot be Approver", null);
			return;
		} else {
			String nextApprover = "";
			if (UserManager.getFirstApprover(1) != null) {
				nextApprover = UserManager.getFirstApprover(1).getRole().getName();
			}
			response = "Transaction Sent To: " + nextApprover + " for Approver";
		}
		
		Corporate corporate = UserManager.getCorporateOfLoginUser() ;
		ArrayList<PaymentInstruction> instructions = new ArrayList<PaymentInstruction>();
		batch.setBatchNumber(PaymentBatch.getNextbatchNumber(UserManager
				.getCorporateOfLoginUser().getId()));

		

		batch.setOwner(corporate);
		batch.setDateCreated(Dates.createCurrent());
		batch.setStatus(Status.New);
		batch.setQueryString(RandomStringUtils.randomAlphanumeric(18).toLowerCase());
		PaymentUpload payUpload = (PaymentUpload) MapFacade.findEntity("PaymentUpload", selectedKeys[0]);
		batch.setNarration(payUpload.getNarration());
		
		batch = XPersistence.getManager().merge(batch);
		

		
		
		ProcessingFeeParameters processingFee=corporate.getProcessingFee()!=null?corporate.getProcessingFee():
				SystemWideSetup.getSystemWideSetup().getProcessingFee();
		
		if(processingFee==null){
			addError("Processing Fee Not Yet Setup", null);
			return;
		}
		//ProcessingFeeParameters processingFee=SystemWideSetup.getSystemWideSetup().getProcessingFee();
		
		boolean beneficiary = processingFee.getAccountToDebit()==AccountToDebit.beneficiaryAccount;
		boolean lumpSum = processingFee.getDeductionType() == DeductionMethod.lumpSum;
		
		
		PaymentUpload upload = new PaymentUpload();
		upload.setAccountname(processingFee.getAccountName());
		upload.setAccountnumber(processingFee.getAccountNumber());
		upload.setBank(processingFee.getBank());
		upload.setNarration(processingFee.getNaration());
		upload.setBeneficiaryId("1");
		upload.setEmail("info@chamsplc.com.ng");
		upload.setOwnerName("Chams");
		BigDecimal amount = new BigDecimal(0.00);
		int transSize = selectedKeys.length;
		
/*		System.out.println("0000 The amount from processing fee====" + amount + " and the size=="+transSize);*/
		if(lumpSum){
			upload.setAmount(String.valueOf(processingFee.getAmount()));
		}else{
			BigDecimal perTransAmount = processingFee.getAmount().multiply(new BigDecimal(transSize));
			upload.setAmount(String.valueOf(perTransAmount));
			
		}
		amount = amount.add(new BigDecimal(upload.getAmount()));
		upload=XPersistence.getManager().merge(upload);
		Map feeKey = new HashMap();
		feeKey.put("id", upload.getId());

		ArrayList<Map> keyMaps = new ArrayList<Map>();
		for (Map map : selectedKeys) {
			keyMaps.add(map);
		}
		keyMaps.add(feeKey);
	
		for (Map key : keyMaps) {
			System.out.println(" The keys as being treated ===" + key);
			PaymentUpload paymentUpload = (PaymentUpload) MapFacade.findEntity(
					"PaymentUpload", key);
			
			if ("Valid".equalsIgnoreCase(paymentUpload.getValid())) {
				PaymentInstruction paymentInstruction = new PaymentInstruction();
				String sql = " FROM Account a WHERE a.number='"
						+ paymentUpload.getAccountnumber() + "'";
				Account account = null;
				ArrayList<Account> accounts = (ArrayList<Account>) XPersistence
						.getManager().createQuery(sql).getResultList();
				if (accounts != null && accounts.size() > 0) {
					account = accounts.get(0);
					account.setName(paymentUpload.getAccountname());
				} else {
					account = new Account();
					account.setBank(paymentUpload.getBank());
					account.setName(paymentUpload.getAccountname());
					account.setNumber(paymentUpload.getAccountnumber());
					account.setEmail(paymentUpload.getEmail());
					AccountOwnerDetail owner = new AccountOwnerDetail();
					owner.setEmail(account.getEmail());
					owner.setName(paymentUpload.getOwnerName());
					owner.setUniqueIdentifier(paymentUpload.getBeneficiaryId());
					account.setOwner(owner);
					
				}
				account = XPersistence.getManager().merge(account);
				
				paymentInstruction.setRemark(processingFee.getDeductionType()==DeductionMethod.perTransaction 
						?"(Processing Fee Per Transaction " + processingFee.getAmount()+")":"");
				paymentInstruction.setAccountname(paymentUpload
						.getAccountname());
				paymentInstruction.setAccountnumber(paymentUpload
						.getAccountnumber());
				
				BigDecimal beneficiaryAmount = new BigDecimal(paymentUpload.getAmount());
				if(beneficiary && !lumpSum){
					beneficiaryAmount = beneficiaryAmount.subtract(processingFee.getAmount());
				}
				paymentInstruction.setAmount(beneficiaryAmount);
				amount = amount.add(paymentInstruction.getAmount());
				paymentInstruction.setBank(paymentUpload.getBank());
				paymentInstruction.setBeneficiaryId(paymentUpload
						.getBeneficiaryId());
				paymentInstruction.setEmail(paymentUpload.getEmail());
				paymentInstruction.setNarration(paymentUpload.getNarration());
				paymentInstruction.setOwnerName(paymentUpload.getOwnerName());
				paymentInstruction.setSourceReference(paymentUpload
						.getSourceReference());
				paymentInstruction.setToAccount(account);
				paymentInstruction.setInitiator(Users.getCurrent());
				paymentInstruction.setStatus(Status.New);
				
				
				String uuid = RandomStringUtils.randomAlphanumeric(18).toLowerCase();
				paymentInstruction.setUuid(uuid);
				
				paymentInstruction.setPaymentBatch(batch);
				instructions.add(paymentInstruction);
				Map treated = new HashMap();
				treated.put("treated", true);
				MapFacade.setValues("PaymentUpload", key, treated);
			}
		}
		
/*		
		PaymentInstruction fee = new PaymentInstruction();
		fee.setAccountname(processingFee.getAccountName());
		fee.setAccountnumber(processingFee.getAccountNumber());
		

		fee.setEmail(corporate.getEmail());
		fee.setBank(processingFee.getBank());

		System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n About adding processing fee email==" + fee.getEmail() +
				" accountname====" + fee.getAccountname() + "  directly from processingFee.getAccountName()==" + processingFee.getAccountName());
		
		instructions.add(fee);*/
		batch.setPaymentInstructions(instructions);
		
		System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n After adding processing fee ");
		batch.setTotalAmount(amount);
		batch.setBatchSummary(" The total Amount is " + batch.getTotalAmount() + " Inclusive of "+
				(processingFee.getDeductionType()==DeductionMethod.perTransaction?processingFee.getAmount()+" Per Transaction of "
		+ (instructions.size()-1) + " Transaction(s)":processingFee.getAmount() + "Convenience Fee Lump Sum "));
		batch = XPersistence.getManager().merge(batch);
		System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n After saving batch ");
		ng.com.justjava.epayment.model.Transaction transaction = new ng.com.justjava.epayment.model.Transaction();

		transaction.setCorporate(corporate);
		
		//transaction.setTransRef(ApprovableTransaction.PaymentInstruction);
		transaction.setDateEntered(Dates.createCurrent());
		transaction.setModelName(batch.getClass().getName());
		transaction.setModelId(batch.getId());
		transaction.setEnteredBy(Users.getCurrent());
		transaction.setDescription("Approver Required for Payment Batch "
				+ batch.getBatchNumber() + ":  " + batch.getNarration());

		batch.setStatus(Status.AwaitingApproval);
		XPersistence.getManager().merge(batch);

		//transaction = transaction.approve();
		 AsyncEventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
		 eventBus.register(transaction);
		 System.out.println(" 1111111approve already commented out......... ");
		 eventBus.post(new Object());

		System.out
				.println(" After \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n "
						+ " The Batch status here ======"
						+ transaction.getStatus());
		
		

		getTab().deselectAll();
		resetDescriptionsCache();
		addMessage("Operation Successfull", null);
	}

}