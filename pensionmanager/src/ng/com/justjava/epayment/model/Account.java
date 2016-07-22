package ng.com.justjava.epayment.model;

import java.io.*;
import java.math.*;
import java.security.*;
import java.util.*;

import javax.crypto.*;
import javax.persistence.*;
import javax.xml.bind.*;

import ng.com.justjava.epayment.Nibss.*;
import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.apache.commons.lang.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.xml.sax.*;

import com.etranzact.fundgate.ws.*;
import com.etranzact.fundgate.ws.Transaction;
import com.googlecode.jcsv.*;
import com.googlecode.jcsv.annotations.*;
import com.googlecode.jcsv.annotations.internal.*;
import com.googlecode.jcsv.reader.*;
import com.googlecode.jcsv.reader.internal.*;
import com.nibss.pitch.services.*;



@Entity
@Views({@View(members="bank;name;number"),@View(name="embeded",members="bank;name;number")})


@Tabs({@Tab(properties = "name,number,bank.name",baseCondition = "${deleted}=0"),
@Tab(name="verifiedAccount", 
rowStyles=@RowStyle(style="row-red", property="valid", value="inValid"),
properties = "name,number,bank.name,verifiedName,valid"),
@Tab(filter = LoginUserCorporateFilter.class,name="accountBalance",
properties = "name,number,bank.name,balance",baseCondition = "${owner.id}=?"),
@Tab(filter = VerifiedAccountFilter.class,name="accountVerification",
properties = "name,number,bank.name,verify,verifiedName",
baseCondition = "${id} IN ?")
})

public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	private boolean verify;
	
	private BigDecimal balance;
	
	private String verifiedName;
	
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
	public String getValid(){
		return (Is.equalAsString(StringUtils.trim(getName()), StringUtils.trim(getVerifiedName())))?"valid":"inValid";
	}
	
	@MapToColumn(columnName="accountname")
	private String name;
	
	@MapToColumn(columnName="accountnumber")
	private String number;
	
	@Transient
	@MapToColumn(columnName="ownerName")
	private String ownerName;
	
	@Transient
	@MapToColumn(columnName="email")
	private String email;
	
	@Transient
	@MapToColumn(columnName="uniqueIdentifier")
	private String uniqueIdentifier;
	
	@ManyToOne
	@NoCreate
	@NoModify
	@DescriptionsList
	@MapToColumn(columnName="bank",type=Bank.class)
	private Bank bank;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="owner_id")
	private AccountOwnerDetail owner;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}
	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}
	
	public static void saveUpload(Reader csvFile,CSVStrategy strategy,ValueProcessorProvider vpp){		
		try {
			
/*			if(true){
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
						+ "############################################### Here we come ");
				return;
			}*/

			CSVReaderBuilder<Account> builder = new CSVReaderBuilder<Account>(csvFile);

			builder.strategy(strategy);
			CSVReader<Account> csvReader = builder.entryParser(	new AnnotationEntryParser<Account>(
					Account.class, vpp)).build();
			
			List<Account> accounts = csvReader.readAll();
			for (Account account : accounts) {
				AccountOwnerDetail owner = new AccountOwnerDetail();
				owner.setEmail(account.getEmail());
				owner.setName(account.getOwnerName());
				owner.setUniqueIdentifier(account.getEmail() + String.valueOf(System.currentTimeMillis()));
				account.setOwner(owner);
				XPersistence.getManager().merge(account);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public AccountOwnerDetail getOwner() {
		return owner;
	}
	public void setOwner(AccountOwnerDetail owner) {
		this.owner = owner;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getNibssFetchedAccountName(){
		PitchControlCenterWithXML_Service service = new PitchControlCenterWithXML_Service();
		PitchControlCenterWithXML nibss = service.getPitchControlCenterWithXMLPort();
		
		
		String response = nibss.uploadNewVendors(getAVSRequest());
		
		System.out.println(" The AVS Response from NIBSS====" + response);
		
		NibssAccountVerificationResponse mashalledResponse = unmashal(response);
		
		if(Is.equalAsString("01", StringUtils.trim(mashalledResponse.getHeader().getStatus()))){
			response = nibss.getVendorUpdateStatus(getStatusRequest());
			
			System.out.println(" Making Status Request Afterward====" + response);
			mashalledResponse = unmashal(response);
			if(mashalledResponse != null){
				if(mashalledResponse.getVendor() != null)
					return mashalledResponse.getVendor().getErrorReason();
			}
		}
		

		return null;
	}
	
	@Transient
	private String getStatusRequest(){
		String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<StatusRequest>"
					+ "<Header>"
						+ "<ScheduleId>" + getNumber() + "_"+getId()+ "</ScheduleId>"
						+ "<ClientId>"+ ""+ "</ClientId>"
					+ "</Header>"
					+ "<HashValue></HashValue>"
				+ "</StatusRequest>";
		//SystemWideSetup.getSystemWideSetup().getMasterKey()
		return request;
	}
	
	@Transient
	private NibssAccountVerificationResponse unmashal(String xmlString){
		NibssAccountVerificationResponse response = null;
		try {
			
			JAXBContext jcUM = JAXBContext.newInstance(NibssAccountVerificationResponse.class);
			Unmarshaller unmarshaller = jcUM.createUnmarshaller();
			response= (NibssAccountVerificationResponse) unmarshaller.unmarshal(new InputSource(new StringReader(xmlString)));	
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}	
	public void updateVerifiedName(){
		String mainGateway = XavaPreferences.getInstance().getXavaProperty("mainGateway", "NIBSS");
		if(Is.equalAsStringIgnoreCase(mainGateway, "NIBSS")){
			nibssOption();
		}else{
			eTranzactOption();
		}

	}
	
	
	private void eTranzactOption(){
		
		String terminalId = "";
		String pin = "";
		String account = getNumber();
		//Map bankMap = getBank()(Map) getView().getValue("bank");
		

		//String bankCode =(String) bank.get("code");
		//double amount = (Double)getView().getValue("amount");
		

		try {
			Bank bank = getBank();
			//pin = Cryptor.encrypt(pin, "KEd4gDNSDdMBxCGliZaC8w==");
					
			System.out.println(" terminalId=="+terminalId + " pin=="+pin+" account=="+account
							+ " bank code=="+bank.getCode() + " amount=="+0.00);

			FundRequest request = getFundRequest(terminalId, pin, account, bank.getCode() ,0.00);
			
			FundResponse response = WebserviceUtil.getPort().process(request);
			
            System.out.println("Result Code = "+response.getError());
            System.out.println("Result Message = "+response.getMessage());
            System.out.println("Result Ref = "+response.getReference());
            System.out.println("Result OtherRef = "+response.getOtherReference());
            setVerifiedName(response.getMessage());
            setVerify(true);
            XPersistence.getManager().merge(this);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void nibssOption() {
		if(getNibssFetchedAccountName() != null){
			setVerifiedName(getNibssFetchedAccountName());
			setVerify(true);
			XPersistence.getManager().merge(this);
			XPersistence.commit();
		}
	}
	private String getAVSRequest(){
		String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<VendorUploadRequest>"
					+ "<Header>"
						+ "<ScheduleId>" + getNumber() + "_"+getId()+ "</ScheduleId>"
						+ "<ClientId>"+ ""+ "</ClientId>"
					+ "</Header>"
					+ "<Vendor>"
						+ "<VendorNumber>" + getName() + "</VendorNumber>"
						+ "<AccountName>"+ getName()+"</AccountName>"
						+ "<AccountNumber>" + getNumber() + "</AccountNumber>"
						+ "<SortCode>"+ getBank().getCode()+"</SortCode>"
					+ "</Vendor>"
					+ "<HashValue></HashValue>"
				+ "</VendorUploadRequest>";
		//SystemWideSetup.getSystemWideSetup().getMasterKey()
		return request;
	}
	private FundRequest getFundRequest(String terminalId, String pin,String account,String bankCode,double amount){

        FundRequest request = new FundRequest();

        try {
			request.setAction("AQ");
			//request.setTerminalId("7000000001");
			request.setTerminalId(terminalId);
			Transaction t = new Transaction();
			//t.setPin("ZhXy4geRgnpqVOH/7V2beg==");
			t.setPin(pin);
			//t.setDestination("0025840828");
			t.setDestination(account);
			//t.setReference("100dcxssrg5588p6");
			t.setReference(Cryptor.generateKey());
			t.setEndPoint("A");
			//t.setBankCode("063");
			t.setBankCode(bankCode);
			//t.setAmount(0.0);
			t.setAmount(amount);
			request.setTransaction(t);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return request;
	}
	
/*	public double getFetchedBalance(){
        
		if(!(this instanceof TransitAccount)){
			return 0.00;
		}
		TransitAccount acct =(TransitAccount)this; 
		
		TransitAccountOwner transitOwner = (TransitAccountOwner)owner;
		
        FundGate port = WebserviceUtil.getPort();

        FundRequest request = new FundRequest();
        request.setAction("BE");

        Transaction t = new Transaction();
        String plainPin = acct.getPin();
        String masterKey = transitOwner.getMasterKey();
        String terminalId = acct.getTerminalId();
        String pin =  "";
        request.setTerminalId(terminalId);//20000000054
        try {
			pin = Cryptor.encrypt(plainPin, masterKey);
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidAlgorithmParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalBlockSizeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BadPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        t.setPin(pin);//0012
        try {
			t.setReference(Cryptor.generateKey());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        request.setTransaction(t);
        FundResponse result = port.process(request);
        String message = result.getMessage();
        
        BigDecimal bd = new BigDecimal(message);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();        
        

	}*/
	public String getVerifiedName() {
		return verifiedName;
	}
	public void setVerifiedName(String verifiedName) {
		this.verifiedName = verifiedName;
	}
	public boolean isVerify() {
		return verify;
	}
	public void setVerify(boolean verify) {
		this.verify = verify;
	}
}
