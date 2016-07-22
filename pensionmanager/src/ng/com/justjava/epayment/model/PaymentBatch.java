package ng.com.justjava.epayment.model;

import java.io.*;
import java.math.*;
import java.rmi.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;

import javax.crypto.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import javax.xml.ws.soap.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;
import ng.com.justjava.epayment.Nibss.*;
import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Payment.*;

import org.apache.commons.lang3.*;
import org.dom4j.io.*;
import org.hibernate.validator.util.privilegedactions.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.xml.sax.*;

import com.etranzact.fundgate.ws.*;
import com.google.common.eventbus.*;
import com.nibss.pitch.services.*;

@Entity
@Views({@View(members="batchSummary;name;batchNumber;payingAccount;softToken;comment;paymentInstructions"),
	@View(name="approve",members="batchSummary;name;batchNumber;payingAccount;comment;paymentInstructions"),
	@View(name="finalApproval",members="batchSummary;name;batchNumber;payingAccount;softToken;comment;paymentInstructions"),
	@View(name="funding", members="batchSummary;name;batchNumber;narration;payingAccount;switching;comment;paymentInstructions")})


@Tabs({@Tab(name="fundBulkPayment", properties = "narration,batchNumber,errorCode,owner.name,dateCreated",
filter=LoginUserCorporateFilter.class,
baseCondition = "${owner.id}=? AND ${deleted = 0}"),

@Tab(name="viewBatches", properties = "narration,batchNumber,errorCode,errorMessage,dateCreated",
filter=LoginUserCorporateFilter.class,
baseCondition = "${owner.id}=?"),

@Tab(properties = "narration,batchNumber,errorCode,owner.name,dateCreated")})

@XmlRootElement(name="PaymentRequest")
@XmlAccessorType(XmlAccessType.NONE) 
public class PaymentBatch extends Payment{
	
	public enum Switching{
		NIBSS,eTranzact
	}
	@XmlTransient
	private Date dateCreated;
	
	private BigDecimal totalAmount;
	
	@LabelFormat(LabelFormatType.NO_LABEL)
	@ReadOnly
	@Stereotype("MEMO")
	private String batchSummary;
	
	private String queryString;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	
	@Transient
	@Stereotype("MEMO")
	private String comment; 
	
	@ManyToOne
	@NoCreate
	@NoModify
	//@SearchListCondition("${transitAccountOwner.id}={this.owner.id}")
	//@Condition("${transitAccountOwner.id} = ${this.owner.id}")
	//@SearchAction("MyReference.search")
	//@ReferenceView("embeded")
	@DescriptionsList(depends="name",condition="${corporate.name}=? AND ${enable}=1",descriptionProperties="display")
	//@Condition("${corporate.name}='' AND ${enable}=1")
	private TransitAccount payingAccount;
	
	private String errorCode;  
	private String errorMessage;
	
	
	
	@DisplaySize(value=6)
	@Action("Security.generateToken")
	private String softToken;
	
	@Transient
	public String getName(){
		return (owner==null?"":owner.getName());
	}
	private Switching switching;
	
	private int alreadyPaid;
	
	
	private String referenceId;

	
	//@LabelFormat(value=LabelFormatType.NO_LABEL)
	@ReadOnly
	private long batchNumber;
	
	@ManyToOne
	private AccountOwnerDetail owner;
	
	@OneToMany(mappedBy="paymentBatch", cascade=CascadeType.ALL)
	@ReadOnly//(forViews="approve")
	@ListProperties("toAccount.name,amount+,narration,initiator")
	//@XmlElement(name="PaymentRecord",type=PaymentInstruction.class)

	private Collection<PaymentInstruction> paymentInstructions;
	

	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public long getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(long batchNumber) {
		this.batchNumber = batchNumber;
	}

	@XmlElement(name="PaymentRecord",type=PaymentInstruction.class)
	public Collection<PaymentInstruction> getPaymentInstructions() {
		return paymentInstructions;
	}

	public void setPaymentInstructions(Collection<PaymentInstruction> paymentInstructions) {
		this.paymentInstructions = paymentInstructions;
	}

	public TransitAccount getPayingAccount() {
		return payingAccount;
	}

	public void setPayingAccount(TransitAccount payingAccount) {
		this.payingAccount = payingAccount;
	}

	public AccountOwnerDetail getOwner() {
		return owner;
	}

	public void setOwner(AccountOwnerDetail owner) {
		this.owner = owner;
	}
	
	public static Long getNextbatchNumber(Long ownerId){
		Long result = 0L;
		result = (Long)XPersistence.getManager().createQuery(" SELECT MAX(p.batchNumber) FROM PaymentBatch p "
				+ " WHERE p.owner.id="+ ownerId).getSingleResult();
		result = result == null?1:result+1L;
		
		return result;
	}
	
	@Transient
	public FundRequest getBulkFundRequest(String action){
		//List<PaymentInstruction> payItems = XPersistence.getManager().createQuery(arg0)
        FundRequest request = new FundRequest();

        request.setAction(action);
        
        Corporate corporate = (Corporate) this.getOwner();
        
        String terminalId = payingAccount.getTerminalId();
        
        String pin = "";
		try {
			pin = Cryptor.encrypt(payingAccount.getPin(), corporate.getMasterKey());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        String uniqueId = corporate.getUniqueIdentifier();
        

        //"20000000054"
        request.setTerminalId(terminalId);
        com.etranzact.fundgate.ws.Transaction trans = new com.etranzact.fundgate.ws.Transaction();
        
        
        //"ZhXy4geRgnpqVOH/7V2beg=="
        trans.setPin(pin);  
        //trans.setPin("ZhXy4geRgnpqVOH/7V2beg==");
        
        trans.setToken("N");
        //trans.setReference(this.getReferenceId());
        try {
			//trans.setReference(Cryptor.generateKey());
        	//trans.setReference("y41A1ggg0CE5ddddde");//+StringUtils.trim(RandomStringUtils.randomAlphanumeric(3)));
        	trans.setReference(RandomStringUtils.randomAlphanumeric(18).toLowerCase());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n The parameters are ===="
        		+ "terminalId=="+terminalId + " and pin=="+ pin + "  and  uniqueId=="+ uniqueId 
        		+ "  the reference===" + trans.getReference());        
        
        double bulkAmount = 0.00;
        
        //trans.setCompanyId(getOwner().getName());
        //"00000000000000000018"
        trans.setCompanyId(uniqueId);
        trans.setSenderName(getOwner().getName());	
        //trans.setSenderName("eTranzact");
        if("BQ".equalsIgnoreCase(action.trim())){
        	trans.setEndPoint("I");
        	//trans.setReference(getQueryString()); 
        }
        int count = 0;

        List<PaymentInstruction> payItems = (List<PaymentInstruction>) getPaymentInstructions();
		Collection<PaymentInstruction> instr = payItems;
		BulkItem [] bulkItems = new BulkItem[instr.size()-1];
		
		
		for(PaymentInstruction payItem:payItems){
			bulkAmount = bulkAmount + payItem.getBulkItem().getAmount(); 
			bulkItems[count] = payItem.getBulkItem();            
		}
		trans.setAmount(bulkAmount);//bulk amount
		//trans.setBulkItems(bulkItems);
        request.setTransaction(trans);
		return request;
	}
	
	public void approve(){
		setStatus(Status.Approve);
		XPersistence.getManager().merge(this);
		XPersistence.commit();
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ "Approving Approving Approving Approving");
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	
	@Transient
	public Long totalInstructions(){
		String sql = "SELECT COUNT (p) FROM PaymentInstruction p where p.paymentBatch.id="+getId();
		Long result = (Long)XPersistence.getManager().createQuery(sql).getSingleResult();
		//int result = getPaymentInstructions()!=null?getPaymentInstructions().size():0;
		return result;
	}
	
	
	public void preCreate(){
		String uniqueId = "";
		setStatus(Status.New);
		try {
			uniqueId = Cryptor.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setReferenceId( uniqueId);
	}

	public int getAlreadyPaid() {
		return alreadyPaid;
	}

	public void setAlreadyPaid(int alreadyPaid) {
		this.alreadyPaid = alreadyPaid;
	}
	
	public boolean pay(Switching switching){
		
		
		boolean result = false;
		if(switching==null)
			switching =Switching.NIBSS;
		switch (switching) {
		case eTranzact:
			FundResponse response = payeTranzact();
			if("0".equalsIgnoreCase(response.getError())){
				result = true;
			}
			break;
		case NIBSS:
			result = payNIBSS();
			break;
		default:
			result  = payNIBSS();
			
			break;
		}
		return result;
	}
	
	public NIBSSStatusRequest nibssStatusRequest(){
		NIBSSStatusRequest request = new NIBSSStatusRequest();
		Header header = new Header();
		//header.setClientId(SystemWideSetup.getSystemWideSetup().getMasterKey());
		header.setScheduleId(getOwner().getUniqueIdentifier()+"_"+ getBatchNumber());
		request.setHeader(header);
		request.setHashValue(" ");
		return request; 
	}	
	
	public NIBSSPaymentRequest nibssRequest(){
		NIBSSPaymentRequest request = new NIBSSPaymentRequest();
		Header header = new Header();
		header.setFileName(getOwner().getName()+"_"+ getBatchNumber());
		header.setScheduleId(getOwner().getUniqueIdentifier()+"_"+ getBatchNumber());
		header.setDebitSortCode(StringUtils.trim(getPayingAccount().getBank().getCode()));
		header.setDebitAccountNumber(getPayingAccount().getTerminalId());
		//header.setDebitAccountNumber("0123456789");
		request.setPaymentRecord(getPaymentInstructions());
		//header.setClientId(SystemWideSetup.getSystemWideSetup().getMasterKey());

		request.setHeader(header);
		return request;
	}
	
	private String readFile( String file) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    return stringBuilder.toString();
	}	
	
	public boolean payNIBSS(){
		System.out.println(" \n\n\n\n\n\n\n\\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n Now Going to NIBSS");
		try {
			JAXBContext jcM = JAXBContext.newInstance(NIBSSPaymentRequest.class);
	        Marshaller marshaller = jcM.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        DocumentResult result = new DocumentResult();
	        marshaller.marshal(nibssRequest(), result);
	        String res=result.getDocument().asXML();
	        
	        res = res.replace("</PaymentRequest>", "<HashValue></HashValue></PaymentRequest>");

/*				SystemWideSetup setup = SystemWideSetup.getSystemWideSetup();
				String url = "https://196.6.103.58:9093/PitchControlCenter/PitchControlCenterWithXML?wsdl";
				if(setup !=null && setup.getNibssURL() !=null)
					url = setup.getNibssURL();*/
				PitchControlCenterWithXML_Service service = new PitchControlCenterWithXML_Service();
				PitchControlCenterWithXML nibss = service.getPitchControlCenterWithXMLPort();
				
				System.out.println("0000\n\n\n\n\n\n\n\\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n The from me ==\n" + res);
				
				String response = nibss.uploadPaymentSchedule(res);
				NIBSSPaymentResponse paymentRequestResponse= unmashalNIBSSPaymentResponse(response);
				
				if(paymentRequestResponse.getHeader()!=null ){
					setErrorCode(paymentRequestResponse.getHeader().getStatus());
				}
				if(paymentRequestResponse.getHeader()!=null && Is.equalAsStringIgnoreCase(paymentRequestResponse.getHeader().getStatus(),"16")){
					setErrorCode(paymentRequestResponse.getHeader().getStatus());
					setStatus(Status.Sent);
				}
				XPersistence.getManager().merge(this);
				//String response = nibss.uploadNewVendors(testRequest);
				
				System.out.println("11111\n\n\n\n\n\n\n\\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n The main request ==\n" + res
						
						+ "  and the immediate response==" + response);		

		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public FundResponse payeTranzact(){

		FundResponse response= null;
		try {
			response = WebserviceUtil.getPort().process(getBulkFundRequest("BT"));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        System.out.println("Pay Result Code = "+response.getError());
        System.out.println("Pay Result Message = "+response.getMessage());
        System.out.println("Pay Result Ref = "+response.getReference());
        System.out.println("Pay Result OtherRef = "+response.getOtherReference());
        System.out.println("Pay Result Amount = "+response.getAmount());
        System.out.println("Pay Result TotalFailed = "+response.getTotalFailed());
        System.out.println("Pay Result TotalSuccess = "+response.getTotalSuccess());
        System.out.println("Pay Result Company = "+response.getCompanyId());
        System.out.println("Pay Result Action = "+response.getAction());	
        
        if("0".equalsIgnoreCase(response.getError())){
        	this.setStatus(Status.Sent);
        	this.setQueryString(response.getOtherReference());
        }else{
        	this.setStatus(Status.ErrorSendingPayment);
        	this.setErrorCode(response.getError());
        	this.setErrorMessage(response.getMessage());
        }
		XPersistence.commit();
		XPersistence.getManager().merge(this);
		return response;
	}
	
	public boolean updateStatus(Switching switching){
		boolean result = false;
		if(switching==null)
			switching =Switching.NIBSS;
		
		switch (switching) {
		case eTranzact:
			updateStatuseTranzact();
			result = true;
			break;
			
		case NIBSS:
			updateStatusNIBSS();
			result = true;
			break;
		default:
			break;
		}
		return result;
		
	}
	@Transient
	private NIBSSPaymentResponse unmashalNIBSSPaymentResponse(String xmlString){
		NIBSSPaymentResponse response = null;
		try {
			
			JAXBContext jcUM = JAXBContext.newInstance(NIBSSPaymentResponse.class);
			Unmarshaller unmarshaller = jcUM.createUnmarshaller();
			response= (NIBSSPaymentResponse) unmarshaller.unmarshal(new InputSource(new StringReader(xmlString)));			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	
	private void updateStatusNIBSS() {
		try {

			JAXBContext jcStatus = JAXBContext.newInstance(NIBSSStatusRequest.class);
			JAXBContext jcResponse = JAXBContext.newInstance(NIBSSPaymentResponse.class);
			Marshaller marshallerStatus = jcStatus.createMarshaller();
			Unmarshaller unmarshaller = jcResponse.createUnmarshaller();
			
			DocumentResult requestDOC = new DocumentResult();
	
			marshallerStatus.marshal(nibssStatusRequest(), requestDOC);
			String statusReq = requestDOC.getDocument().asXML();
			
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n "
					+ "statusReq sent ===" +statusReq + "\n\n\n\n\n\n ");			
			
			SystemWideSetup setup = SystemWideSetup.getSystemWideSetup();
/*			String url = "https://196.6.103.58:9093/PitchControlCenter/PitchControlCenterWithXML?wsdl";
			if(setup !=null && setup.getNibssURL() !=null)
				url = setup.getNibssURL();*/

/*				SystemWideSetup setup = SystemWideSetup.getSystemWideSetup();
				String url = "https://196.6.103.58:9093/PitchControlCenter/PitchControlCenterWithXML?wsdl";
				if(setup !=null && setup.getNibssURL() !=null)
					url = setup.getNibssURL();*/
				PitchControlCenterWithXML_Service service = new PitchControlCenterWithXML_Service();
				PitchControlCenterWithXML nibss = service.getPitchControlCenterWithXMLPort();
				
			String response = nibss.getPaymentScheduleStatus(statusReq);
			//String testStatus = readFile("C:/Personal/StatusRequest.xml" );
			
			//String response = nibss.getVendorUploadStatus(testStatus);
			
			
			System.out.println(response + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n "
					+ "statusReq sent ===" +statusReq + "\n\n\n\n\n\n and the response got===" + response);
			
			NIBSSPaymentResponse respon= (NIBSSPaymentResponse) 
					unmarshaller.unmarshal(new InputSource(new StringReader(response)));
			respon.processStatus();
			
			//System.out.println(response + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n The response status after marshalling===" +testStatus );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	private void updateStatuseTranzact() {
		FundResponse response = null;
		try {
			System.out.println(" About to call a webservice here =====");
			
			
			
			response = WebserviceUtil.getPort().process(getBulkFundRequest("BQ"));
			
			
            System.out.println("Status Result Code = "+response.getError());
            System.out.println("Status Result Message = "+response.getMessage());
            System.out.println("Status Result Ref = "+response.getReference());
            System.out.println("Status Result OtherRef = "+response.getOtherReference());
            System.out.println("Status Result Amount = "+response.getAmount());
            System.out.println("Status Result TotalFailed = "+response.getTotalFailed());
            System.out.println("Status Result TotalSuccess = "+response.getTotalSuccess());
            System.out.println("Status Result Company = "+response.getCompanyId());
            System.out.println("Status Result Action = "+response.getAction());
            

			for (BulkItem item : response.getBulkItems().getBulkItem()) {
				
				System.out.println(" The Unique id Here =========================================" + item.getUniqueId());
				String id = item.getUniqueId() != null?item.getUniqueId().split("_")[0]:"0";
				
				
				System.out.println(" The id here =========================================" + id);
				
				PaymentInstruction paymentInstruction = PaymentInstruction.
																			getPaymentInstructionByRef(id);
				if(paymentInstruction != null){
					paymentInstruction.setFundGateMessage(item.getMessage());
					paymentInstruction.setFundGateStatus(item.getStatus());
					XPersistence.getManager().merge(paymentInstruction);
				}
			}
			int totalPaid = response.getTotalSuccess() + getAlreadyPaid();
			this.setAlreadyPaid(totalPaid);
			if(totalPaid == this.totalInstructions()){
				this.setStatus(Status.Paid);
			}else{
				this.setStatus(Status.Partial);
			}
			//this.setErrorCode(response.getError());
			//this.setErrorMessage(response.getMessage());
			XPersistence.getManager().merge(this);
			XPersistence.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
	}

	@PostCreate
	public void postCreate(){
		ng.com.justjava.epayment.model.Transaction transaction = 
				new ng.com.justjava.epayment.model.Transaction();

		Corporate corporate = (Corporate)this.getOwner();
		transaction.setCorporate(corporate);
		transaction.setDateEntered(Dates.createCurrent());
		transaction.setModelName(getClass().getName());
		transaction.setModelId(getId());
		transaction.setEnteredBy(Users.getCurrent());
		transaction.setDescription("Approver Required for Payment Batch "+ getBatchNumber());
		
		setStatus(Status.AwaitingApproval);
		XPersistence.getManager().merge(this);
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

	public Switching getSwitching() {
		return switching;
	}

	public void setSwitching(Switching switching) {
		this.switching = switching;
	}

	public String getSoftToken() {
		return softToken;
	}

	public void setSoftToken(String softToken) {
		this.softToken = softToken;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getBatchSummary() {
		return batchSummary;
	}

	public void setBatchSummary(String batchSummary) {
		this.batchSummary = batchSummary;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
