package ng.com.justjava.epayment.model;

import java.rmi.*;
import java.text.*;
import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.apache.commons.lang3.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;

import com.etranzact.fundgate.ws.*;

@Views({@View(members="holders")})

@Tabs({@Tab(properties="senderName,amount,narration,numberofHolders,upload.month,upload.uploadYear.year",
			filter=PFABankAccountFilter.class,
			baseCondition = "${beneficiaryAccountName}=?"
		+ " AND ${beneficiaryAccountNumber}=? AND ${upload.status}=5"
		,defaultOrder = "${upload.month} desc"),
		
	   @Tab(name="corporateTab",properties="pensionFundAdministrator,amount,narration,numberofHolders,upload.month,upload.uploadYear.year",
		filter=PaymentSenderFilter.class,
		baseCondition = "${senderName}=? AND ${upload.status}=5"
	,defaultOrder = "${upload.month} desc")})

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
	public Collection<RSAHolder> getHolders(){
		return getUpload().getHolders();
	}
	

	public int getNumberofHolders(){
		return getUpload().getHolders().size();
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
	public String getPensionFundAdministrator(){
		String ejbQL = "FROM PensionFundAdministrator p WHERE p.account.name='"+beneficiaryAccountName+"'";
		PensionFundAdministrator pfa =  null;
		try {
			pfa = (PensionFundAdministrator) XPersistence.getManager().createQuery(ejbQL).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (pfa==null?"":pfa.getName());
	}
	public boolean updateStatus(){
		boolean result = false;
        FundRequest request = new FundRequest();

        request.setAction("BQ");
        
        Corporate corporate = (Corporate) getUpload().getCorporate();
        
        TransitAccount payingAccount = getUpload().getPayingAccount();
        
        String terminalId = payingAccount.getTerminalId();
        
        String pin = payingAccount.getPin();
        
        String uniqueId = corporate.getUniqueIdentifier();
        request.setTerminalId(terminalId);
        com.etranzact.fundgate.ws.Transaction trans = new com.etranzact.fundgate.ws.Transaction();
        
        
        //"ZhXy4geRgnpqVOH/7V2beg=="
        trans.setPin(pin);  
        //trans.setPin("ZhXy4geRgnpqVOH/7V2beg==");
        
        trans.setToken("N");
        //trans.setReference(this.getReferenceId());
        try {
        	trans.setReference(RandomStringUtils.randomAlphanumeric(18).toLowerCase());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        trans.setCompanyId(uniqueId);
        trans.setSenderName(corporate.getName());	
        trans.setEndPoint("I");
		trans.setBulkItems(null);
        request.setTransaction(trans);
        try {
			FundResponse response = WebserviceUtil.getPort().process(request);
            System.out.println("Status Result Code = "+response.getError());
            System.out.println("Status Result Message = "+response.getMessage());
            System.out.println("Status Result Ref = "+response.getReference());
            System.out.println("Status Result OtherRef = "+response.getOtherReference());
            System.out.println("Status Result Amount = "+response.getAmount());
            System.out.println("Status Result Company = "+response.getCompanyId());
            System.out.println("Status Result Action = "+response.getAction());
            
            setReference(response.getReference());
            setOtherReference(response.getOtherReference());
            if("0".equalsIgnoreCase(response.getError())){
            	//this.setResponseCode(response.getError());
            	result = true;
            }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static PaymentLog getPaymentLogByUniqueId(String uniqueId){
		String ql = "FROM PaymentLog p WHERE p.uniqueId='"+uniqueId+"'";
		PaymentLog log = (PaymentLog) XPersistence.getManager().createQuery(ql).getSingleResult();
		return log;
	}
	
	public static Collection <PaymentLog> getPaymentLog(){
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
	
	
	private String terminalID;
	private String beneficiaryAccountNumber;
	private String beneficiaryAccountName;
	
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
	
}
