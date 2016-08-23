package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.apache.commons.lang3.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.Is;

import com.etranzact.fundgate.ws.*;

@Views({ @View(members = "holders") })
@Tabs({
		@Tab(properties = "upload.corporate.name,amount,holderSize,numberofHolders,upload.month,upload.uploadYear.year", filter = PFABankAccountFilter.class, baseCondition = "${beneficiaryAccountName}=?"
				+ " AND ${beneficiaryAccountNumber} in ? AND ${upload.status}=5", defaultOrder = "${upload.month} desc"),
		@Tab(name = "pfcTab", properties = "beneficiary.name,beneficiary.account.bank.name,upload.corporate.name,amount,numberofHolders,upload.month,upload.uploadYear.year", filter = PFCBankAccountFilter.class, baseCondition = "${beneficiaryAccountName} IN ? AND ${upload.status}=5", defaultOrder = "${upload.month} desc"),

		@Tab(name = "corporateTab", properties = "beneficiary.name,beneficiary.account.bank.name,amount,numberofHolders,upload.month,upload.uploadYear.year", filter = PaymentSenderFilter.class, baseCondition = "${senderName}=? AND ${upload.status}=5", defaultOrder = "${upload.month} desc") })
@Entity
public class PaymentLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;

	public String getTerminalID() {
		return terminalID; 
	}

	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}

	public String getBeneficiaryAccountNumber() {
		return beneficiaryAccountNumber;
	}

	public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
		this.beneficiaryAccountNumber = beneficiaryAccountNumber;
	}

	public String getOtherReference() {
		return otherReference;
	}

	public void setOtherReference(String otherReference) {
		this.otherReference = otherReference;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ListProperties("firstName,secondName,pencommNumber,pensionAmount")
	@ViewAction("")
	@ReadOnly
	@ListAction("Print.generateExcel")
	public Collection<RSAHolder> getHolders() {
		String ejbQL = "";
		ejbQL = " FROM RSAHolder h WHERE h.pfa.name='"
				+ getPensionFundAdministrator() + "' AND h.upload.id="
				+ getUpload().getId();
		Collection<RSAHolder> holders = null;
		try {
			System.out.println("0 The ejbQL == " + ejbQL);
			holders = XPersistence.getManager().createQuery(ejbQL)
					.getResultList();

			System.out.println("1 The ejbQL == " + ejbQL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return holders;

	}
	
	public int getHolderSize(){
		return 2;
	}

	public int getNumberofHolders() {
		String ejbQL = "";
		ejbQL = " FROM RSAHolder h WHERE h.pfa.name='"
				+ getPensionFundAdministrator() + "' AND h.upload.id="
				+ getUpload().getId();

		Collection<RSAHolder> holders = null;
		try {
			System.out.println("0 The ejbQL == " + ejbQL);
			holders = XPersistence.getManager().createQuery(ejbQL)
					.getResultList();

			System.out.println("1 The ejbQL == " + ejbQL + " with size ==="+holders.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return holders.size();
	}

	public String getBeneficiaryAccountName() {
		return beneficiaryAccountName;
	}

	public void setBeneficiaryAccountName(String beneficiaryAccountName) {
		this.beneficiaryAccountName = beneficiaryAccountName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public MonthlyUpload getUpload() {
		return upload;
	}

	public void setUpload(MonthlyUpload upload) {
		this.upload = upload;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Transient
	public String getPensionFundAdministrator() {
		String ejbQL = "FROM PensionFundAdministrator p WHERE p.account.name='"
				+ beneficiaryAccountName + "'";
		PensionFundAdministrator pfa = null;
		try {
			pfa = (PensionFundAdministrator) XPersistence.getManager()
					.createQuery(ejbQL).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (pfa == null ? "" : pfa.getName());
	}

	public boolean updateStatus() {
		  boolean result = false;
		  FundRequest request = new FundRequest();

		  request.setAction("BQ");

		  System.out.println(" 0 updateStatus");
		  Corporate corporate = (Corporate) getUpload().getCorporate();

		  TransitAccount payingAccount = getUpload().getPayingAccount();

		  System.out.println(" 1 updateStatus");
		  String terminalId = payingAccount.getTerminalId();

		  System.out.println(" 2 updateStatus");
		  String pin = payingAccount.getPin();

		  String uniqueId = corporate.getUniqueIdentifier();
		  request.setTerminalId(terminalId);
		  System.out.println(" 3 updateStatus");
		  com.etranzact.fundgate.ws.Transaction trans = new com.etranzact.fundgate.ws.Transaction();

		  System.out.println(" 4 updateStatus");
		  // "ZhXy4geRgnpqVOH/7V2beg=="
		  trans.setPin(pin);
		  // trans.setPin("ZhXy4geRgnpqVOH/7V2beg==");

		  //trans.setToken("N");
		  System.out.println(" 5 updateStatus");
		  // trans.setReference(this.getReferenceId());
		  try {
		   trans.setReference(RandomStringUtils.randomAlphanumeric(18)
		     .toLowerCase());
		   System.out.println(" 6 updateStatus reference====" + trans.getReference());
		  } catch (Exception e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		  trans.setCompanyId(uniqueId);
		  //trans.setUniqueTransId(getUniqueId());
		  trans.setUniqueTransId("16_ycewszl53e");
		  trans.setSenderName(corporate.getName());
		  trans.setEndPoint("B");
		  
		  BulkItems bi = new BulkItems();
		        
		        BulkItem bit = new BulkItem();
		        bit.setUniqueId(getUniqueId());  
		        bi.getBulkItem().add(bit);
		  trans.setBulkItems(bi);
		  request.setTransaction(trans);
		  boolean succeed = false;
		  try {
		   System.out.println("uniqueId =="+ trans.getUniqueTransId());
		   FundResponse response = WebserviceUtil.getPort().process(request);
		   
		   
		   System.out.println(" The response.getBulkItems().getBulkItem()=="+
		   response.getBulkItems().getBulkItem() +" and size is "+ (response.getBulkItems().getBulkItem()==null?
		     0 : response.getBulkItems().getBulkItem().size()));
		   
		   
		   for (BulkItem item : response.getBulkItems().getBulkItem()) {
		    if(item.getAmount()>0.00){
		     succeed = true;
		     break;
		    }
		    System.out.println(" Each Item ==="+ item.getAmount());
		   }
		   System.out.println("Status Result Code = " + response.getError());
		   System.out.println("Status Result Message = "
		     + response.getMessage());
		   System.out
		      .println("Status Result Ref = " + response.getReference());
		   System.out.println("Status Result OtherRef = "
		     + response.getOtherReference());
		   System.out 
		     .println("Status Result Amount = " + response.getAmount());
		   System.out.println("Status Result Company = "
		     + response.getCompanyId());
		   System.out
		     .println("Status Result Action = " + response.getAction());

		   setReference(response.getReference());
		   setOtherReference(response.getOtherReference());
		   if ("0".equalsIgnoreCase(response.getError())) {
		    // this.setResponseCode(response.getError());
		    result = true;
		   }

		  } catch (Exception e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		  return result;
		 }

	public static PaymentLog getPaymentLogByUniqueId(String uniqueId) {
		String ql = "FROM PaymentLog p WHERE p.uniqueId='" + uniqueId + "'";
		PaymentLog log = (PaymentLog) XPersistence.getManager().createQuery(ql)
				.getSingleResult();
		return log;
	}

	public static Collection<PaymentLog> getPaymentLog() {
		System.out.println("===== INside geyPayment Method ======");
		String ql = "FROM PaymentLog p";
		Collection<PaymentLog> log = null;
		try {
			log = XPersistence.getManager().createQuery(ql).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("==== Has Executed=====");
		return log;

	}
	
	public static Collection<PaymentLog> getPaymentLog2(Long pfaId,int monthId, Long year, Long payeeId) {
		
		System.out.println("I am inside the method");
		
		
		String concat=" ";
		if(payeeId != null|| pfaId != null){
			concat = "WHERE ";
		}
		
		if(pfaId !=0 ){
			if(Is.equalAsStringIgnoreCase(" WHERE ", concat)){
				concat = concat + " p.beneficiary.id="+pfaId;
			}else{
				concat = concat + " AND " + "p.beneficiary.id="+pfaId;
			}
		}
		
		
		if(year !=0 ){
			if(Is.equalAsStringIgnoreCase(" WHERE ", concat)){
				concat = concat + " p.upload.uploadYear_year="+year;
			}else{
				concat = concat + " AND " + "p.upload.uploadYear_year="+year;
			}
		}
		
		if(monthId !=0 ){
			if(Is.equalAsStringIgnoreCase(" WHERE ", concat)){
				concat = concat + " p.upload.month="+monthId;
			}else{
				concat = concat + " AND " + "p.upload.month="+monthId;
			}
		}
		
		
		if(payeeId !=0 ){
			if(Is.equalAsStringIgnoreCase(" WHERE ", concat)){
				concat = concat + " p.payee.id="+payeeId;
			}else{
				concat = concat + " AND " + "p.payee.id="+payeeId;
			}
		}
		
		System.out.println("===== INside geyPayment Method ======" );
		String ql = "FROM PaymentLog p ";	
		
		System.out.println(" The concat string here ==="+ concat +
				" AND the index of where =="+concat.indexOf("WHERE"));
		
		if(concat.indexOf("WHERE")>=0){
			concat = concat + " AND p.responseCode='0' AND p.upload.status=5";
		}else{
			concat = concat + " WHERE p.responseCode='0'  AND p.upload.status=5";
		}
		
		ql = ql + concat;
		System.out.println(" The full query string here ==="+ ql +
				" AND the index of where =="+concat.indexOf("WHERE"));
		
		Collection<PaymentLog> log = null;
		try {
			log = XPersistence.getManager().createQuery(ql).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("==== Has Executed=====");
		return log;


	}
	
	public static Collection<PaymentLog> getPaymentLogPFA(Long pfaId, Long payeeId, Date fromDate, Date toDate) {
		System.out.println("I am inside the method");
		
		java.sql.Date dateFrom = new java.sql.Date(fromDate.getTime());
		java.sql.Date dateTo = new java.sql.Date(toDate.getTime());
		System.out.println("The New From Date is "+dateFrom);
		System.out.println("The New To Date is "+dateTo);
		
		String concat=" ";
		if(payeeId != 0|| pfaId != null|| fromDate!=null||toDate!=null){
			concat = "WHERE p.responseCode = '0'";
		}
		

		if(pfaId != null ){
			if(Is.equalAsStringIgnoreCase(" WHERE p.responseCode = '0' AND ", concat)){
				concat = concat + " p.beneficiary.id="+(pfaId);
			}else{
				concat = concat + " AND " + "p.beneficiary.id="+(pfaId);
			}
		}
		
		if(payeeId !=0 ){
			if(Is.equalAsStringIgnoreCase(" WHERE p.responseCode = '0' AND ", concat)){
				concat = concat + " p.payee.id="+payeeId;
			}else{
				concat = concat + " AND " + "p.payee.id="+payeeId;
			}
		}
		
		if(dateTo !=null){
			if(dateTo.before(dateFrom)){
				concat = concat + " AND " + "p.date BETWEEN '"+dateTo+"' AND '"+dateFrom+"'";
				
			}else{
				concat = concat + " AND " + "p.date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'";
			}
		
		
		}
		
		System.out.println("===== INside geyPayment Method ======");
		String ql = "FROM PaymentLog p ";		
		ql = ql + concat;
		System.out.println(" The full query string here ==="+ ql );
		
		Collection<PaymentLog> log = null;
		try {
			log = XPersistence.getManager().createQuery(ql).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("==== Has Executed=====");
		return log;


	}

	
	public static Collection<PaymentLog> getPaymentLogCorporate(long payeeId, Long pfaId, Date fromDate, Date toDate) {
		System.out.println("I am inside the method");
		
		java.sql.Date dateFrom = new java.sql.Date(fromDate.getTime());
		java.sql.Date dateTo = new java.sql.Date(toDate.getTime());
		System.out.println("The New From Date is "+dateFrom);
		System.out.println("The New To Date is "+dateTo);
		
		String concat=" ";
		if(payeeId != 0|| pfaId != null|| fromDate!=null||toDate!=null){
			concat = "WHERE p.responseCode = '0' AND ";
		}
		
		if(payeeId !=0 ){
			if(Is.equalAsStringIgnoreCase(" WHERE p.responseCode = '0' AND ", concat)){
				concat = concat + " p.payee.id="+payeeId;
			}else{
				concat = concat + " AND " + "p.payee.id="+payeeId;
			}
		}
		
		
		if(pfaId != null ){
			if(Is.equalAsStringIgnoreCase(" WHERE p.responseCode = '0' AND ", concat)){
				concat = concat + " p.beneficiary.id="+(pfaId);
			}else{
				concat = concat + " AND " + "p.beneficiary.id="+(pfaId);
			}
		}
	
		if(dateTo !=null){
			if(dateTo.before(dateFrom)){
				concat = concat + " AND " + "p.date BETWEEN '"+dateTo+"' AND '"+dateFrom+"'";
				
			}else{
				concat = concat + " AND " + "p.date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'";
			}
		
		
		}
		
		System.out.println("===== INside geyPayment Method ======");
		String ql = "FROM PaymentLog p ";		
		ql = ql + concat;
		System.out.println(" The full query string here ==="+ ql );
		
		Collection<PaymentLog> log = null;
		try {
			log = XPersistence.getManager().createQuery(ql).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("==== Has Executed=====");
		return log;

	}	


	public PensionFundAdministrator getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(PensionFundAdministrator beneficiary) {
		this.beneficiary = beneficiary;
	}

	public Corporate getPayee() {
		return payee;
	}

	public void setPayee(Corporate payee) {
		this.payee = payee;
	}

	private String terminalID;
	private String beneficiaryAccountNumber;
	private String beneficiaryAccountName;

	@ManyToOne
	private PensionFundAdministrator beneficiary;

	@ManyToOne
	private Corporate payee;

	private String otherReference;
	private String reference;
	private double amount;
	private String responseCode;
	private String responseDescription;
	private Date date;
	private String narration;
	private String senderName;
	private String uniqueId;
	@ManyToOne
	private MonthlyUpload upload;
	
	public static Collection<PaymentLog> getPaymentLogPFC(Long pfcId, Long payeeId, Long pfaId,	Date fromDate, Date toDate) {
		System.out.println("I am inside the method");
		
		java.sql.Date dateFrom = new java.sql.Date(fromDate.getTime());
		java.sql.Date dateTo = new java.sql.Date(toDate.getTime());
		System.out.println("The New From Date is "+dateFrom);
		System.out.println("The New To Date is "+dateTo);
		
		String concat=" ";
		if(payeeId != 0|| pfaId != null|| fromDate!=null||toDate!=null){
			concat = "WHERE p.responseCode = '0' AND ";
		}
		
		if(payeeId !=0 ){
			if(Is.equalAsStringIgnoreCase(" WHERE p.responseCode = '0' AND ", concat)){
				concat = concat + " p.payee.id="+payeeId;
			}else{
				concat = concat + " AND " + "p.payee.id="+payeeId;
			}
		}

		
		if(pfcId != null ){
			if(Is.equalAsStringIgnoreCase(" WHERE p.responseCode = '0' AND ", concat)){
				concat = concat + " p.beneficiary.custodian.id="+(pfcId);
			}else{
				concat = concat + " AND " + "p.beneficiary.custodian.id="+(pfcId);
			}
		}		
		
		
		if(pfaId != null ){
			if(Is.equalAsStringIgnoreCase(" WHERE p.responseCode = '0' AND ", concat)){
				concat = concat + " p.beneficiary.id="+(pfaId);
			}else{
				concat = concat + " AND " + "p.beneficiary.id="+(pfaId);
			}
		}
		
		if(dateTo !=null){
			if(dateTo.before(dateFrom)){
				concat = concat + " AND " + "p.date BETWEEN '"+dateTo+"' AND '"+dateFrom+"'";
				
			}else{
				concat = concat + " AND " + "p.date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'";
			}
		
		
		}
		
		System.out.println("===== INside geyPayment Method ======");
		String ql = "FROM PaymentLog p ";
		//String ql = "FROM PaymentLog p WHERE p.payee.id ='" + payeeId + "'";
		
		
		ql = ql + concat;
		System.out.println(" The full query string here ==="+ ql );
		
		Collection<PaymentLog> log = null;
		try {
			log = XPersistence.getManager().createQuery(ql).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("==== Has Executed=====");
		return log;
	}
	
	
	public static Collection<PaymentLog> getPaymentLogPencomm(Long pfcId, Long payeeId, Long pfaId,	Date fromDate, Date toDate) {
		System.out.println("I am inside the method");
		
		java.sql.Date dateFrom = new java.sql.Date(fromDate.getTime());
		java.sql.Date dateTo = new java.sql.Date(toDate.getTime());
		System.out.println("The New From Date is "+dateFrom);
		System.out.println("The New To Date is "+dateTo);
		
		String concat=" ";
		if(payeeId != 0|| pfaId != null|| fromDate!=null||toDate!=null||pfcId!=null){
			concat = "WHERE p.responseCode = '0' AND ";
		}
		
		if(payeeId !=0 ){
			if(Is.equalAsStringIgnoreCase(" WHERE p.responseCode = '0' AND ", concat)){
				concat = concat + " p.payee.id="+payeeId;
			}else{
				concat = concat + " AND " + "p.payee.id="+payeeId;
			}
		}		
		
		System.out.println(" The pfcId ====="+pfcId);
		
		if(pfcId != null ){
			if(Is.equalAsStringIgnoreCase(" WHERE p.responseCode = '0' AND ", concat)){
				concat = concat + " p.beneficiary.custodian.id="+(pfcId);
			}else{
				concat = concat + " AND " + "p.beneficiary.custodian.id="+(pfcId);
			}
		}		
		
		
		if(pfaId != null ){
			if(Is.equalAsStringIgnoreCase(" WHERE p.responseCode = '0' AND ", concat)){
				concat = concat + " p.beneficiary.id="+(pfaId);
			}else{
				concat = concat + " AND " + "p.beneficiary.id="+(pfaId);
			}
		}
		
		System.out.println(" The concat ====="+concat);
		
		if(dateTo !=null && dateFrom !=null){
			String cojoin = "AND";
			if(Is.equalAsStringIgnoreCase(concat, "WHERE p.responseCode = '0' AND ")){
				cojoin = " ";
			}
			if(dateTo.before(dateFrom)){
				concat = concat + cojoin + "p.date BETWEEN '"+dateTo+"' AND '"+dateFrom+"'";
				
			}else{
				concat = concat + cojoin + "p.date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'";
			}
		
		
		}
		
		System.out.println("===== INside geyPayment Method ======");
		String ql = "FROM PaymentLog p ";
		ql = ql + concat;
		System.out.println(" The full query string here ==="+ ql );
		
		Collection<PaymentLog> log = null;
		try {
			log = XPersistence.getManager().createQuery(ql).getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("==== Has Executed=====");
		return log;
	}


}
