package ng.com.justjava.epayment.action;

import java.math.*;
import java.util.*;
import java.util.concurrent.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Approver.*;
import ng.com.justjava.epayment.model.Payment.*;
import ng.com.justjava.epayment.model.ProcessingFeeParameters.*;
import ng.com.justjava.epayment.utility.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

import com.google.common.eventbus.*;

public class SendInvestorPayment extends ViewBaseAction{

	public void execute() throws Exception {
		System.out.println(" Here I am and then ");
		Map[] keys = getView().getSubview("investors").getCollectionTab().getSelectedKeys();
		List<Investor> selectedInvestors = getView().getSubview("investors").getCollectionSelectedObjects();
		System.out.println(" The supplied key length====" + keys.length + 
				" the list size ==" + selectedInvestors.size()); 
		PaymentBatch batch = new PaymentBatch();
		
		batch.setBatchNumber(PaymentBatch.getNextbatchNumber(UserManager
				.getCorporateOfLoginUser().getId()));

		

		batch.setOwner(UserManager.getCorporateOfLoginUser());
		batch.setDateCreated(Dates.createCurrent());
		batch.setStatus(Status.New);
		batch.setQueryString(RandomStringUtils.randomAlphanumeric(18).toLowerCase());
		batch.setNarration(getView().getValueString("comment"));
		
		batch = XPersistence.getManager().merge(batch);
		BigDecimal amount = new BigDecimal(0.00);
		ArrayList<PaymentInstruction> instructions = new ArrayList<PaymentInstruction>();
		for (Investor investor : selectedInvestors) {
			PaymentInstruction paymentInstruction = new PaymentInstruction();
			String sql = " FROM Account a WHERE a.number='"
					+ investor.getAccount().getNumber() + "'";
			Account account = null;
			ArrayList<Account> accounts = (ArrayList<Account>) XPersistence
					.getManager().createQuery(sql).getResultList();
			if (accounts != null && accounts.size() > 0) {
				account = accounts.get(0);
				account.setName(investor.getAccount().getName());
			} else {
				account = new Account();
				account.setBank(investor.getAccount().getBank());
				account.setName(investor.getAccount().getName());
				account.setNumber(investor.getAccount().getNumber() );
				account.setEmail(investor.getEmail());
				AccountOwnerDetail owner = new AccountOwnerDetail();
				owner.setEmail(account.getEmail());
				owner.setName(investor.getName());
				owner.setUniqueIdentifier(investor.getName());
				account.setOwner(owner);
				
			}
			account = XPersistence.getManager().merge(account);
			
			paymentInstruction.setRemark("Investor Payment");
			paymentInstruction.setAccountname(investor.getName());
			paymentInstruction.setAccountnumber(investor.getAccount().getNumber());
			
			BigDecimal beneficiaryAmount = investor.getNetPayment();
			amount = amount.add(beneficiaryAmount);
			paymentInstruction.setAmount(beneficiaryAmount);
			paymentInstruction.setBank(investor.getAccount().getBank());
			paymentInstruction.setBeneficiaryId(investor.getName());
			paymentInstruction.setEmail(investor.getEmail());
			//paymentInstruction.setNarration(paymentUpload.getNarration());
			paymentInstruction.setOwnerName(investor.getName());
			//paymentInstruction.setSourceReference(paymentUpload.getSourceReference());
			paymentInstruction.setToAccount(account);
			paymentInstruction.setInitiator(Users.getCurrent());
			paymentInstruction.setStatus(Status.New);
			
			
			String uuid = RandomStringUtils.randomAlphanumeric(18).toLowerCase();
			paymentInstruction.setUuid(uuid);
			
			paymentInstruction.setPaymentBatch(batch);
			instructions.add(paymentInstruction);
			
			System.out.println(" The selected investor name====" + investor.getName()); 
		}
		batch.setPaymentInstructions(instructions);
		batch.setPaymentInstructions(instructions);
		
		System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n After adding processing fee ");
		batch.setTotalAmount(amount);
		batch.setBatchSummary("Summary");
		batch = XPersistence.getManager().merge(batch);
		System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n After saving batch ");
		ng.com.justjava.epayment.model.Transaction transaction = new ng.com.justjava.epayment.model.Transaction();

		transaction.setCorporate(UserManager.getCorporateOfLoginUser());
		
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
		
		

		//getTab().deselectAll();
		//resetDescriptionsCache();
		addMessage("Operation Successfull", null);
		
		// TODO Auto-generated method stub
	}

}
