package ng.com.justjava.epayment.model;

import java.io.*;
import java.math.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.apache.commons.lang.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.etranzact.fundgate.ws.*;
import com.google.common.eventbus.*;
import com.googlecode.jcsv.*;
import com.googlecode.jcsv.annotations.*;
import com.googlecode.jcsv.annotations.internal.*;
import com.googlecode.jcsv.reader.*;
import com.googlecode.jcsv.reader.internal.*;

@Entity
@Tabs({ @Tab(filter = LoginUserCorporateFilter.class, 
properties = "paymentBatch.dateCreated,narration,accountDetail,amount+,"
		+ "paymentStatus", 
baseCondition = "${paymentBatch.owner.id} = ? AND ${deleted=0}"),

@Tab(name="paymentToMe", filter = PaymentToMeFilter.class, 
properties = "paymentBatch.batchNumber,paymentBatch.dateCreated,narration,fromAccount.name,fromAccount.number,amount+,"
		+ "switchResponse,sourceReference,paymentStatus", 
baseCondition = "${toAccount.owner.id} = ?") })

@Views({ @View(members = "batchNumber;narration;payingAmount;beneficiary") })
//@XmlRootElement(name="PaymentRecord")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder={"beneficiary", "amount", "accountNumber" ,"sortCode" ,"id","fundGateStatus","fundGateMessage"})
public class PaymentInstruction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	
	private Long id;

	@XmlTransient
	private String uuid;
	
	
	private String remark;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Transient
	@XmlElement(name="Beneficiary")
	public String getBeneficiary() {
		return (toAccount!=null?toAccount.getName():"NOT SET");
	}	
	@MapToColumn(columnName = "mount")
	private BigDecimal amount;
	@XmlElement(name="Amount")
	public BigDecimal getAmount() {
		return amount;
	}
	
	public String getAccountDetail(){
		return getToAccount().getName() + " ("+getToAccount().getBank().getName()+")";
	}
	
	
	@Transient
	@XmlElement(name="AccountNumber")
	public String getAccountNumber() {
		return (toAccount!=null?toAccount.getNumber():"NOT SET");
	}	
	
	@Transient
	@XmlElement(name="SortCode")
	public String getSortCode() {
		if(getToAccount() != null)
			return StringUtils.trim(getToAccount().getBank().getCode());
		return "NOT SET";
	}	
	
	@Transient
	public String getSwitchResponse(){
		return (getPaymentBatch()!=null?getPaymentBatch().getErrorMessage():"");
	}
	public String getNarration() {
		return narration;
	}	
	
	
	
	public String getPaymentStatus(){
		String status = getFundGateMessage();
		
		switch (paymentBatch.getStatus()) {
		case Approve:
			status="Already Approve";
			break;
		case AwaitingApproval: 
			status="Awaiting Approval";
			break;
		case Sent:
			status="Sent for Payment";
			break;			

		}
		return status;
	}
	
	@MapToColumn(columnName="beneficiaryId")
	@XmlTransient
	private String beneficiaryId;
	
	@MapToColumn(columnName="sourceReference")
	@XmlTransient
	private String sourceReference;
	
	@Transient
	@MapToColumn(columnName="accountname")
	@XmlTransient
	private String accountname;
	
	@Transient
	@MapToColumn(columnName="accountnumber")
	@XmlTransient
	private String accountnumber;	
	

	@Transient
	@MapToColumn(columnName="ownerName")
	@XmlTransient
	private String ownerName;	
	
	@Transient
	@MapToColumn(columnName="bank",type=Bank.class)
	@XmlTransient
	private Bank bank;		
	
	@Transient
	@MapToColumn(columnName="email")
	@XmlTransient
	private String email;		

	@XmlTransient
	private String fundGateMessage;
	

	private String fundGateStatus;

	@ManyToOne
	@XmlTransient
	private PaymentBatch paymentBatch;



	@ManyToOne
	//@MapToColumn(columnName = "toAccount")
	@XmlTransient
	private Account toAccount;

	@ManyToOne
	@XmlTransient
	private Account fromAccount;

	@XmlTransient
	private Status status;

	@MapToColumn(columnName = "narration")
	private String narration;

	@XmlTransient
	private String initiator;

	@XmlElement(name="Narration")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getToAccount() {
		return toAccount;
	}

	public void setToAccount(Account toAccount) {
		this.toAccount = toAccount;
	}

	public Account getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}



	public void setNarration(String narration) {
		this.narration = narration;
	}



	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	@Transient
	public long getBatchNumber() {
		return paymentBatch.getBatchNumber();
	}



	
	public BigDecimal getPayingAmount() {
		return getAmount();
	}

	public static String saveUpload(Reader csvFile, CSVStrategy strategy,
			ValueProcessorProvider vpp, Object extraParam){	
		String response = "Successfully Uploaded";
		try {
			CSVReaderBuilder<PaymentInstruction> builder = new CSVReaderBuilder<PaymentInstruction>(
					csvFile);

			PaymentBatch batch = new PaymentBatch();

			if (UserManager.getCorporateOfLoginUser() == null) {
				response = "Error:Invalid User for Payment Instruction Upload";
				return response;
			}
			if (!UserManager.approverExists(0)) {
				response = "Error:Approver Route Not Yet Setup ";
				return response;
			}
			if (UserManager.isLoginUserApprover()) {
				response = "Error:Uploader cannot be Approver";
				return response;
			} else {
				String nextApprover = "";
				if (UserManager.getFirstApprover(1) != null) {
					nextApprover = UserManager.getFirstApprover(1).getRole().getName();
				}
				response = "Transaction Sent To: " + nextApprover
						+ " for Approver";
			}
			batch.setBatchNumber(PaymentBatch.getNextbatchNumber(UserManager
					.getCorporateOfLoginUser().getId()));
			// Collection<PaymentInstruction> paymentInstructions = new
			// ArrayList<PaymentInstruction>();
			builder.strategy(strategy);
			CSVReader<PaymentInstruction> csvReader = builder.entryParser(
					new AnnotationEntryParser<PaymentInstruction>(
							PaymentInstruction.class, vpp)).build();
			List<PaymentInstruction> paymentInstructions = csvReader.readAll();
			ArrayList<PaymentInstruction> instructions = new ArrayList<PaymentInstruction>();
			
			batch.setOwner(UserManager.getCorporateOfLoginUser());
			batch.setDateCreated(Dates.createCurrent());
			batch.setStatus(Status.New);
			batch = XPersistence.getManager().merge(batch);
			for (PaymentInstruction paymentInstruction : paymentInstructions) {
				String sql = " FROM Account a WHERE a.number='"+paymentInstruction.getAccountnumber()+"'";
				Account account = null;
				ArrayList<Account> accounts = (ArrayList<Account>) XPersistence.getManager().createQuery(sql).getResultList();
				if(accounts!=null && accounts.size()>0){
					account = accounts.get(0);
				}else{
					account = new Account();
					account.setBank(paymentInstruction.getBank());
					account.setName(paymentInstruction.getAccountname());
					account.setNumber(paymentInstruction.getAccountnumber());
					account.setEmail(paymentInstruction.getEmail());
					AccountOwnerDetail owner = new AccountOwnerDetail();
					owner.setEmail(account.getEmail());
					owner.setName(paymentInstruction.getOwnerName());
					owner.setUniqueIdentifier(paymentInstruction.getBeneficiaryId());
					account.setOwner(owner);
					account = XPersistence.getManager().merge(account);
				}

				
				paymentInstruction.setToAccount(account);
				paymentInstruction.setInitiator(Users.getCurrent());
				paymentInstruction.setStatus(Status.New);
				paymentInstruction.setPaymentBatch(batch);
				instructions.add(paymentInstruction);
				
				batch.setPaymentInstructions(instructions);
				batch = XPersistence.getManager().merge(batch);
				ng.com.justjava.epayment.model.Transaction transaction = 
						new ng.com.justjava.epayment.model.Transaction();

				Corporate corporate = (Corporate)batch.getOwner();
				transaction.setCorporate(corporate);
				transaction.setDateEntered(Dates.createCurrent());
				transaction.setModelName(batch.getClass().getName());
				transaction.setModelId(batch.getId());
				transaction.setEnteredBy(Users.getCurrent());
				transaction.setDescription("Approver Required for Payment Batch "+ batch.getBatchNumber());
				
				batch.setStatus(Status.AwaitingApproval);
				XPersistence.getManager().merge(batch);
				System.out.println("Before \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n "
						+ " The Batch status here ======" + transaction.getStatus());		
				
				//transaction = transaction.approve();
				 AsyncEventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
				 eventBus.register(transaction);
				 System.out.println(" 1111111approve already commented out......... ");
				 eventBus.post(new Object());

				System.out.println(" After \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n "
						+ " The Batch status here ======" + transaction.getStatus());				
				
			}
			//batch.setPaymentInstructions(instructions);

			//System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n Here is after saving the payment batch");
			//XPersistence.getManager().merge(batch);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}

	public PaymentBatch getPaymentBatch() {
		return paymentBatch;
	}

	public void setPaymentBatch(PaymentBatch paymentBatch) {
		this.paymentBatch = paymentBatch;
	}

	@Transient
	public BulkItem getBulkItem() {

		BulkItem item = new BulkItem();
		item.setBeneficiaryName(getToAccount().getName());
		item.setAccountId(getToAccount().getNumber());
		item.setAmount(getAmount().doubleValue());
		item.setBankCode(StringUtils.trim(getToAccount().getBank().getCode()));
		item.setNarration(getId() + "_" +  getNarration());
		// item.setBankCode("033");
		item.setUniqueId(getUuid());
		// item.setUniqueId(String.valueOf(System.currentTimeMillis()
		// +this.getId()));
		return item;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@PreCreate
	public void preCreate() {
		String uniqueId = getId() + "_" + RandomStringUtils.randomAlphanumeric(18).toLowerCase();
		setUuid(uniqueId);
	}

	public synchronized static PaymentInstruction getPaymentInstructionByRef(
			String uniqueId) {
		String sql = "from PaymentInstruction p where p.id=" + Long.valueOf(uniqueId);

		System.out
				.println(" The SQL Here ================================================================"
						+ sql);

		PaymentInstruction paymentInstruction = null;
		try {
			paymentInstruction = (PaymentInstruction) XPersistence.getManager()
					.createQuery(sql).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paymentInstruction;
	}

	@XmlElement(name="ErrorReason")
	public String getFundGateMessage() {
		return fundGateMessage;
	}
	
	public void setFundGateMessage(String fundGateMessage) {
		this.fundGateMessage = fundGateMessage;
	}

	@XmlElement(name="Status")
	public String getFundGateStatus() {
		return fundGateStatus;
	}

	public void setFundGateStatus(String fundGateStatus) {
		this.fundGateStatus = fundGateStatus;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getBeneficiaryId() {
		return beneficiaryId;
	}

	public void setBeneficiaryId(String beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public String getSourceReference() {
		return sourceReference;
	}

	public void setSourceReference(String sourceReference) {
		this.sourceReference = sourceReference;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public static Collection getPaymentInstruction(Long corporate, Long accountId, Date start, Date end, Status status) {
		Collection<PaymentInstruction> instructions = new ArrayList<PaymentInstruction>();
		String sql = " FROM PaymentInstruction p WHERE p.paymentBatch.owner.id = "+corporate+" AND p.paymentBatch.status = "+status.ordinal()+" "
				+ "AND p.toAccount.id = "+accountId+" AND p.paymentBatch.dateCreated BETWEEN :start AND :end";
		
		System.out.println("########### Inside getPaymentInstruction the sql sent is: "+sql);
		
		instructions = (Collection<PaymentInstruction>)XPersistence
											.getManager().createQuery(sql)
												.setParameter("start", start, TemporalType.DATE).setParameter("end", end, TemporalType.DATE).getResultList();
		System.out.println("############## Start date is "+start+" , End date is "+end+" status is "+status.ordinal()+"\n"
				+ "  instructions size==" + instructions.size());
		
		return instructions;
	}
	

}
