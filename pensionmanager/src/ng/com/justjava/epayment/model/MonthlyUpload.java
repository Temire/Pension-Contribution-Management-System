package ng.com.justjava.epayment.model;

import java.io.*;
import java.math.*;
import java.rmi.*;
import java.security.*;
import java.text.*;
import java.util.*;

import javax.crypto.*;
import javax.persistence.*;

import org.apache.commons.lang3.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.etranzact.fundgate.ws.*;

import ng.com.justjava.epayment.action.*;
import ng.com.justjava.epayment.model.Payment.*;
import ng.com.justjava.epayment.model.RemitPension.Months;
import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

@Views({
		@View(members = "uploadYear;month;monthlyFigure;holders"),
		@View(name = "approve", members = "companyName;payingAccount;paymentSummary;serviceCharge;totalAmount"),
		@View(name = "viewOnly", members = "paymentSummary;serviceCharge;totalAmount") })
@Tabs({
		@Tab(properties = "month,narration,status", filter = MultiValueFilter.class, baseCondition = "${deleted}=0 AND ${corporate.id}=? AND ${levelReached}=?"),
		@Tab(name = "approve", properties = "narration,dateEntered", filter = MultiValueFilter.class, baseCondition = "${status}=1 AND ${corporate.id}=? AND ${levelReached}=?"),
		@Tab(name = "retry", properties = "month,narration,status,paymentResponseCode,paymentResponseDescription", baseCondition = "${deleted}=0 AND ${status} NOT IN (0,1,2,3)"),
		@Tab(name = "MyUpload", properties = "month,narration,status,paymentResponseCode,paymentResponseDescription", baseCondition = "${deleted}=0 AND ${corporate.id}=?", filter = LoginUserCorporateFilter.class) })
@Entity
public class MonthlyUpload {

	public enum Status {
		New, awaitingApproval, approve, reject, sent, paid, errorSending, updated;
	}

	public enum Type {
		corporate, personal;
	}

	private boolean monthlyFigure;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;

	@ManyToOne
	@DescriptionsList(descriptionProperties = "year")
	@NoCreate
	@NoModify
	private PeriodYear uploadYear;

	public PeriodYear getUploadYear() {
		return uploadYear;
	}

	public void setUploadYear(PeriodYear uploadYear) {
		this.uploadYear = uploadYear;
	}

	@ManyToOne
	@NoCreate
	@NoModify
	@DescriptionsList(depends = "companyName", condition = "${corporate.name}=?", descriptionProperties = "display")
	// @OnChange(DisplayBalanceAction.class)
	private TransitAccount payingAccount;

	@Column(columnDefinition = "tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	@Transient
	public String getNarration() {
		return month + " Pension Contribution Remittance";
	}

	private int levelReached;

	private Status status;

	@ManyToOne
	private Corporate corporate;

	@Transient
	@Stereotype("LABEL")
	public String getCompanyName() {
		return corporate.getName();
	}

	@Required
	@OnChange(OnChnageMonthlyUpload.class)
	// @ReadOnly
	private Months month;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "upload")
	@ReadOnly
	// (forViews="approve")
	@ListProperties("fullName,pencommNumber,pfa.name,voluntaryDonation,"
			+ "grossPay,pensionAmount[upload.totalAmount],upload.status,variance,remark")
	@Condition("${upload.id}=-1")
	@RowStyle(style = "row-red", property = "variance", value = "vary")

	private Collection<RSAHolder> holders;

	private Date dateEntered;
	private String enteredBy;

	// private

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Collection<PensionFundAdministrator> getSummaryList() {
		return summaryList;
	}

	public void setSummaryList(Collection<PensionFundAdministrator> summaryList) {
		this.summaryList = summaryList;
	}

	public Months getMonth() {
		return month;
	}

	public void setMonth(Months month) {
		this.month = month;
	}

	public Collection<RSAHolder> getHolders() {
		return holders;
	}

	public void setHolders(Collection<RSAHolder> holders) {
		this.holders = holders;
	}

	public Date getDateEntered() {
		return dateEntered;
	}

	public void setDateEntered(Date dateEntered) {
		this.dateEntered = dateEntered;
	}

	public String getEnteredBy() {
		return enteredBy;
	}

	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getLevelReached() {
		return levelReached;
	}

	public void setLevelReached(int levelReached) {
		this.levelReached = levelReached;
	}

	/*
	 * public PeriodYear getUploadYear() { return uploadYear; } public void
	 * setUploadYear(PeriodYear uploadYear) { this.uploadYear = uploadYear; }
	 */
	@ListProperties("name,totalNumberOfHolders,amountSummation")
	@Transient
	@ReadOnly
	@CollectionView("rsaCompanyHolders")
	@RowAction("RowAction.showDetail")
	@NoModify
	@ViewAction("")
	public Collection<PensionFundAdministrator> getPaymentSummary() {

		Corporate corporate = getCorporate();
		if (corporate == null)
			corporate = UserManager.getCorporateOfLoginUser();

		String query = " FROM PensionFundAdministrator p "
				+ "INNER JOIN p.holders h WHERE h.corporate.id="
				+ corporate.getId() + " AND h.upload.id=" + getId();
		Collection<Object[]> pfas = null;
		Collection<PensionFundAdministrator> list = new ArrayList<PensionFundAdministrator>();

		try {
			pfas = XPersistence.getManager().createQuery(query).getResultList();
			for (Object[] object : pfas) {
				PensionFundAdministrator pfa = (PensionFundAdministrator) object[0];

				RSAHolder holder = (RSAHolder) object[1];
				pfa.addAmountSummation(holder.getPensionAmount());
				// totalAmount = totalAmount.add(holder.getPensionAmount());
				pfa.addCompanyHolders(holder);
				if (!list.contains(pfa)) {
					list.add(pfa);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		summaryList = list;
		return list;
	}

	/*
	 * @Transient private BigDecimal totalAmount=new BigDecimal(0.00);
	 */

	@Transient
	@Stereotype("LABEL")
	public BigDecimal getServiceCharge() {
		String charges = XavaPreferences.getInstance().getXavaProperty(
				"charge", "120.00");
		return new BigDecimal(charges);
	}

	@Transient
	@Stereotype("LABEL")
	public BigDecimal getTotalAmount() {
		if (summaryList == null)
			summaryList = getPaymentSummary();
		BigDecimal result = new BigDecimal(0.00);
		for (PensionFundAdministrator pfa : summaryList) {
			result = result.add(pfa.getAmountSummation());
		}
		result = result.add(getServiceCharge());
		return result;
	}

	@Transient
	private Collection<PensionFundAdministrator> summaryList = null;

	public TransitAccount getPayingAccount() {
		return payingAccount;
	}

	public void setPayingAccount(TransitAccount payingAccount) {
		this.payingAccount = payingAccount;
	}

	public boolean pay() throws RemoteException {

		boolean result = false;
		FundResponse response = null;
		FundRequest request = getBulkFundRequest();
		try {
			
			response = WebserviceUtil.getPort().process(request);
			this.setPaymentResponseCode(response.getError());
			this.setPaymentResponseDescription(response.getMessage());
			this.setPaymentReference(response.getReference());
			this.setPaymentOtherReference(response.getOtherReference());
			result = response.getError() != null&& "0".equalsIgnoreCase(response.getError().trim());
			System.out.println(" The result from eTRanzact =="+result);
			if (result) {
				result = true;
				setStatus(Status.paid);
				XPersistence.getManager().merge(this);

			}

			// XPersistence.commit();
			System.out.println("After the call pay");

		} catch (Exception e) {
			// TODO Auto-generated catch block

			System.out.println(" The exception here at eTranzact call ======"
					+ e);
			e.printStackTrace();
		}
		if (result) {
			List<BulkItem> items = request.getTransaction().getBulkItems()
					.getBulkItem();
			for (BulkItem item : items) {
				PaymentLog log = new PaymentLog();
				log.setAmount(item.getAmount());
				log.setBeneficiaryAccountName(item.getBeneficiaryName());
				log.setBeneficiaryAccountNumber(item.getAccountId());
				log.setDate(Dates.createCurrent());
				log.setSenderName(getCorporate().getUniqueIdentifier());

				log.setTerminalID(getPayingAccount() != null ? getPayingAccount().getTerminalId() : "NOT AVAILABLE");
				log.setUniqueId(item.getUniqueId());
				log.setOtherReference(response.getOtherReference());
				log.setReference(response == null ? "NULL RESPONSE" : response
						.getReference());
				log.setResponseDescription(response == null ? "NULL RESPONSE"
						: response.getMessage());
				log.setResponseCode("N");
				log.setNarration(item.getNarration());
				log.setPayee(getCorporate());
				log.setBeneficiary(PensionFundAdministrator
						.findPFAByAccountNumber(item.getAccountId()));
				log.setUpload(this);
				XPersistence.getManager().merge(log);
				System.out.println("After merge PaymentLog for "
						+ log.getUniqueId() + "...........................");
				//sendNotification(log);

			}
		}
		XPersistence.commit();

		if (response != null) {

			System.out.println("Pay Result Code = " + response.getError());
			System.out.println("Pay Result Message = " + response.getMessage());
			System.out.println("Pay Result Ref = " + response.getReference());
			System.out.println("Pay Result OtherRef = "
					+ response.getOtherReference());
			System.out.println("Pay Result Amount = " + response.getAmount());
			System.out.println("Pay Result TotalFailed = "
					+ response.getTotalFailed());
			System.out.println("Pay Result TotalSuccess = "
					+ response.getTotalSuccess());
			System.out.println("Pay Result Company = "
					+ response.getCompanyId());
			System.out.println("Pay Result Action = " + response.getAction());

		}
		return result;
	}


	public FundRequest getBulkFundRequest() {
		// List<PaymentInstruction> payItems =
		// XPersistence.getManager().createQuery(arg0)

		System.out.println("1 Entering getBulkFundRequest beginning");
		FundRequest request = null;
		request = new FundRequest();
		request.setAction("BT");
		String terminalId = getPayingAccount() != null ? getPayingAccount()
				.getTerminalId() : " ";

		String pin = getPayingAccount() != null ? getPayingAccount().getPin()
				: " ";

		// String uniqueId = corporate.getUniqueIdentifier();
		request.setTerminalId(terminalId);

		com.etranzact.fundgate.ws.Transaction trans = new com.etranzact.fundgate.ws.Transaction();

		trans.setPin(pin);
		// trans.setPin("kghxqwveJ3eSQJip/cmaMQ==");
		// trans.setPin("ZhXy4geRgnpqVOH/7V2beg==");

		// trans.setToken("N");

		trans.setReference(getPaymentOtherReference());

		trans.setSenderName(corporate.getUniqueIdentifier());

		trans.setCompanyId(corporate.getUniqueIdentifier());
		trans.setEndPoint("A");

		System.out.println("Am in here");

		// trans.setSenderName("eTranzact");

		// BulkItems bulkItems = new BulkItems();

		Collection<PensionFundAdministrator> pfas = getPaymentSummary();
		BulkItems bulkItems = new BulkItems();
		// List<PaymentInstruction> payItems = (List<PaymentInstruction>)
		// getPaymentInstructions();
		DecimalFormat format = new DecimalFormat("#.##");
		double bulkAmount = 0.00;
		for (PensionFundAdministrator payItem : pfas) {
			double localAmount = 0.00;
			BulkItem item = new BulkItem();

			Account account = payItem.getAccount();

			if (account != null) {
				localAmount = Double.valueOf(format.format(payItem
						.getAmountSummation().doubleValue()));
				bulkAmount = bulkAmount + localAmount;
				String bankCode = (account != null ? account.getBank()
						.getCode() : "NOT AVAILABLE");
				String accountName = (account != null ? account.getName()
						: "NOT AVAILABLE");
				String accountNumber = (account != null ? account.getNumber()
						: "NOT AVAILABLE");
				System.out.println(" Amount for " + payItem.getName() + " is "
						+ localAmount + " the destination account code=="
						+ bankCode + " and the account number =="
						+ accountNumber);
				item.setBeneficiaryName(accountName);
				item.setAccountId(accountNumber);
				item.setAmount(localAmount);
				item.setBankCode(StringUtils.trim(bankCode));
				item.setNarration(payItem.getId() + "_"
						+ corporate.getUniqueIdentifier() + "_" + month);
				item.setUniqueId(payItem.getId()
						+ "_"
						+ RandomStringUtils.randomAlphanumeric(10)
								.toLowerCase());
				bulkItems.getBulkItem().add(item);

			}
		}

		// if(bankBalance < bulkAmount)
		bulkAmount = Double.valueOf(format.format(bulkAmount));
		System.out.println(" Bulk Amount ==" + bulkAmount);

		trans.setAmount(bulkAmount);// bulk amount
		trans.setBulkItems(bulkItems);

		// trans.setb
		request.setTransaction(trans);
		return request;
	}

	private Type type;

	@PostCreate
	@PostPersist
	@PostUpdate
	public void setTheType() {
		if (getType() == null) {
			setType(Type.corporate);
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getPaymentResponseCode() {
		return paymentResponseCode;
	}

	public void setPaymentResponseCode(String paymentResponseCode) {
		this.paymentResponseCode = paymentResponseCode;
	}

	public String getPaymentResponseDescription() {
		return paymentResponseDescription;
	}

	public void setPaymentResponseDescription(String paymentResponseDescription) {
		this.paymentResponseDescription = paymentResponseDescription;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public String getPaymentOtherReference() {
		return paymentOtherReference;
	}

	public void setPaymentOtherReference(String paymentOtherReference) {
		this.paymentOtherReference = paymentOtherReference;
	}

	public boolean isMonthlyFigure() {
		return monthlyFigure;
	}

	public void setMonthlyFigure(boolean monthlyFigure) {
		this.monthlyFigure = monthlyFigure;
	}

	public boolean updateStatuseTranzact() {
		FundResponse response = null;
		boolean result = false;
		try {
			System.out
					.println(" About to call a webservice here for bulk query=====");

			response = WebserviceUtil.getPort().process(
					getBulkFundRequest("BQ"));

			System.out.println("Status Result Code = " + response.getError());
			System.out.println("Status Result Message = "
					+ response.getMessage());
			System.out
					.println("Status Result Ref = " + response.getReference());
			System.out.println("Status Result OtherRef = "
					+ response.getOtherReference());
			System.out
					.println("Status Result Amount = " + response.getAmount());
			System.out.println("Status Result TotalFailed = "
					+ response.getTotalFailed());
			System.out.println("Status Result TotalSuccess = "
					+ response.getTotalSuccess());
			System.out.println("Status Result Company = "
					+ response.getCompanyId());
			System.out
					.println("Status Result Action = " + response.getAction());

			if (response.getBulkItems() == null)
				return false;

			for (BulkItem item : response.getBulkItems().getBulkItem()) {

				System.out
						.println(" The Unique id Here ========================================="
								+ item.getUniqueId());
				String id = item.getUniqueId() != null ? item.getUniqueId()
						.split("_")[0] : "0";

				System.out
						.println(" The id here ========================================="
								+ id);

				PaymentLog log = PaymentLog.getPaymentLogByUniqueId(item
						.getUniqueId());
				if (log != null) {
					log.setResponseDescription(item.getMessage());
					log.setResponseCode(item.getStatus());
					log.setOtherReference(getPaymentOtherReference());
					log.setReference(getPaymentReference());
					XPersistence.getManager().merge(log);
				}
			}
			// this.setErrorCode(response.getError());
			// this.setErrorMessage(response.getMessage());
			result = true;
			XPersistence.getManager().merge(this);
			XPersistence.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	@Transient
	public FundRequest getBulkFundRequest(String action) {
		// List<PaymentInstruction> payItems =
		// XPersistence.getManager().createQuery(arg0)
		FundRequest request = new FundRequest();

		request.setAction(action);

		Corporate corporate = (Corporate) this.getCorporate();

		String terminalId = payingAccount.getTerminalId();

		String pin = payingAccount.getPin();

		String uniqueId = corporate.getUniqueIdentifier();
		request.setTerminalId(terminalId);
		com.etranzact.fundgate.ws.Transaction trans = new com.etranzact.fundgate.ws.Transaction();

		// "ZhXy4geRgnpqVOH/7V2beg=="
		trans.setPin(pin);
		// trans.setPin("ZhXy4geRgnpqVOH/7V2beg==");

		trans.setToken("N");
		// trans.setReference(this.getReferenceId());
		try {
			// trans.setReference(Cryptor.generateKey());
			// trans.setReference("y41A1ggg0CE5ddddde");//+StringUtils.trim(RandomStringUtils.randomAlphanumeric(3)));
			trans.setReference(RandomStringUtils.randomAlphanumeric(18)
					.toLowerCase());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out
				.println("/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n The parameters are ===="
						+ "terminalId=="
						+ terminalId
						+ " and pin=="
						+ pin
						+ "  and  uniqueId=="
						+ uniqueId
						+ "  the reference==="
						+ trans.getReference() + " the action here ==" + action);

		// trans.setCompanyId(getOwner().getName());
		// "00000000000000000018"
		trans.setCompanyId(uniqueId);
		trans.setSenderName(getCorporate().getName());
		// trans.setSenderName("eTranzact");
		if ("BQ".equalsIgnoreCase(action.trim())) {
			trans.setEndPoint("I");
			// trans.setReference(getQueryString());
		}

		BulkItems bulkItems = new BulkItems();
		Collection<PensionFundAdministrator> payItems = (List<PensionFundAdministrator>) getPaymentSummary();
		DecimalFormat format = new DecimalFormat("#.##");
		double bulkAmount = 0.00;
		for (PensionFundAdministrator payItem : payItems) {
			double localAmount = 0.00;
			BulkItem item = new BulkItem();

			Account account = payItem.getAccount();

			if (account != null) {
				localAmount = Double.valueOf(format.format(payItem
						.getAmountSummation().doubleValue()));
				bulkAmount = bulkAmount + localAmount;
				String bankCode = (account != null ? account.getBank()
						.getCode() : "NOT AVAILABLE");
				String accountName = (account != null ? account.getName()
						: "NOT AVAILABLE");
				String accountNumber = (account != null ? account.getNumber()
						: "NOT AVAILABLE");
				System.out.println(" Amount for " + payItem.getName() + " is "
						+ localAmount + " the destination account code=="
						+ bankCode + " and the account number =="
						+ accountNumber);
				item.setBeneficiaryName(accountName);
				item.setAccountId(accountNumber);
				item.setAmount(localAmount);
				item.setBankCode(StringUtils.trim(bankCode));
				item.setNarration(payItem.getId() + "_"
						+ corporate.getUniqueIdentifier() + "_" + month);
				// item.setUniqueId(payItem.getId() + "_" +
				// RandomStringUtils.randomAlphanumeric(10).toLowerCase());
				bulkItems.getBulkItem().add(item);

			}
		}

		// if(bankBalance < bulkAmount)
		bulkAmount = Double.valueOf(format.format(bulkAmount));
		System.out.println(" Bulk Amount ==" + bulkAmount);

		trans.setAmount(bulkAmount);// bulk amount
		trans.setBulkItems(bulkItems);

		// trans.setb
		request.setTransaction(trans);

		return request;
	}
	public Profile getAwaitingApprovalProfile(){
		Profile profile = null;
		
		System.out.println(" Inside getAwaitingApprovalProfile =="+Status.awaitingApproval);
		if(getStatus() != Status.awaitingApproval)
			return null;
		try { 
			System.out.println(" SQL ==="+" FROM Profile p WHERE p.transaction=0 AND p.level="+getLevelReached());
			profile = (Profile) XPersistence.getManager().
					createQuery("FROM Profile p WHERE p.transaction=0 AND p.level="+getLevelReached()).
					getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return profile;
	}
	private String paymentResponseCode;
	private String paymentResponseDescription;
	private String paymentReference;
	private String paymentOtherReference;

	public void sendNotification() {
		
		System.out.println(" Inside Notification of send for approval....");
		
		if(getAwaitingApprovalProfile() ==null)
			return;
		
		Collection<CorporateUser> users = getAwaitingApprovalProfile().getUsers();
		
		System.out.println(" Inside Notification of send for approval....");
		for (CorporateUser corporateUser : users) {
			MailManager notification = new MailManager();
			notification.setContent("Dear "+corporateUser.getUser().getGivenName()+
									" Pension Contribution for Month "+getMonth()+
									" Is Awaiting Your Approval");
			notification.setPhoneNumber(corporateUser.getUser().getPhoneNumber());
			notification.setSubject("Pension Transaction Awaiting Approval");
			notification.setToAddress(corporateUser.getUser().getEmail());
			
			System.out.println(" The notification is hereby persisted..");
			XPersistence.getManager().merge(notification);
		}
		
		// TODO Auto-generated method stub
		
	}
}
