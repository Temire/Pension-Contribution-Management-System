package ng.com.justjava.epayment.model;

import java.io.*;
import java.math.*;
import java.rmi.*;
import java.security.*;

import javax.crypto.*;
import javax.persistence.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.apache.commons.lang.math.*;
import org.apache.commons.lang3.*;
import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.etranzact.fundgate.ws.*;
import com.etranzact.fundgate.ws.Transaction;

@Entity
@Views({@View(members="bank;terminalId;pin"),@View(name="embeded",members="bank;terminalId"),
	@View(name="balanceEquiry",members="terminalId;display,bankBalance ")})
@Tab(filter=LoginUserCorporateFilter.class,properties="terminalId,display,bankBalance",baseCondition = "${corporate.id}=? AND ${deleted}=0")

public class TransitAccount extends Approvable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	@DisplaySize(15)
	private String terminalId;
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	//@ReadOnly(forViews="balance")
	private BigDecimal balance;
	
	@Transient
	public String getDisplay(){
		return (getBank()!=null?getBank().getName() + "(" + getTerminalId() + ")":getTerminalId());
	}
	
	@DescriptionsList
	@ManyToOne
	@NoCreate
	private Bank bank;
	
	@Column(length=4)
	@DisplaySize(4)
	@Stereotype("PASSWORD")
	private String pin;
	
	@ManyToOne
	@JoinColumn(name = "accountOwnerId")
	private Corporate corporate;
	
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;
	
		
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	

	
	public void updateCurrentBalance(){
		boolean eTranzact = Is.equalAsStringIgnoreCase(XavaPreferences.getInstance().getXavaProperty("mainGateway", "eTranzact"), "eTranzact");
		System.out.println(" The result of the test here ===" + eTranzact);
		setBalance(eTranzact?new BigDecimal(getBalanceeTranzact()):new BigDecimal(0.00));
		System.out.println(" The result of the updated balance ===" + getBalance());
		XPersistence.getManager().merge(this);
		XPersistence.commit();
	}
	public boolean approve(){
		setEnable(true);
		XPersistence.getManager().merge(this);
		return true; 
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public Corporate getCorporate() {
		return corporate;
	}
	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}
	
	public double getBalanceeTranzact(){
        
		System.out.println("0000000000000000000The result from the eTranzact here =====");
		if(getPin()==null)
			return 0.00;
		
        FundGate port = null;
		try {
			port = WebserviceUtil.getPort();
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

        FundRequest request = new FundRequest();
        request.setAction("BE");

        System.out.println("1111111111111111The result from the eTranzact here =====");
        Transaction t = new Transaction();
        String plainPin = getPin();
        String masterKey = XavaPreferences.getInstance().getXavaProperty("masterKey", "R90aaowC0PrB2zILxzV1uw==");
        String terminalId = getTerminalId();
        String pin =  "";
        request.setTerminalId(terminalId);//20000000054
        try {
			pin = Cryptor.encrypt(plainPin, masterKey);
			System.out.println("22222222222222222222The result from the eTranzact here =====pin==" + pin);
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
        FundResponse result = null;
		result = port.process(request);
        String message = result.getMessage();
        
        System.out.println(" The result from the eTranzact here =====" + message + " for Card Number=" + terminalId);
        
        BigDecimal bd = new BigDecimal(message);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();        

	}
	
	@PostUpdate
	public void encryptPin() {
		System.out.println("encryptPin");
			if(getCorporate()==null || getPin() == null)
				return;
			
			System.out.println("***** My Inner pin "+getPin());
			String encPin = "XXXXX";
			try {
				if(getPin() !=null && getPin().length()<=4){
					encPin = Cryptor.encrypt(getPin(), corporate.getMasterKey());
				}else
					encPin = getPin();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			System.out.println("THe pin I am setting eventually is =="+encPin);
			setPin(encPin);
			
			//setPin(Cryptor.encrypt(getPin(), corporate.getMasterKey()));

	}
	
	@PostCreate
	public void decryptPin(){
		System.out.println("encryptPin");
		if(getCorporate()==null || getPin() == null)
			return;
		
		System.out.println("***** My Inner pin "+getPin());
		String encPin = "XXXXX";
		try {
			encPin = Cryptor.encrypt(getPin(), corporate.getMasterKey());
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		System.out.println("&&&&& My Inner pin after encryption=="
		+encPin);
		setPin(encPin);
	}

	public double getBankBalance() {


		//TransitAccountOwner transitOwner = (TransitAccountOwner) owner;

		if(getPin() == null)
			return 0.00;
		
		FundRequest request = new FundRequest();
		request.setAction("BE");

		Transaction t = new Transaction();
		String pin = getPin();
		String terminalId = getTerminalId();
		request.setTerminalId(terminalId);// 20000000054
		t.setPin(pin);// 0012
		String reference = RandomStringUtils.randomAlphanumeric(18).toLowerCase();
		System.out.println(" Reference=="+reference + " and  pin =="+pin);
		t.setReference(reference);

		request.setTransaction(t);
		FundResponse result = null;
		try {
			result = WebserviceUtil.getPort().process(request);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result == null)
			return 0.00;
		
		String message = result.getMessage();
		System.out.println(" The returned message from the eTranzact webservice=="+message+
				" for the reference id =="+reference);
		if(!NumberUtils.isNumber(message))
			return 0.00;
		
		BigDecimal bd = new BigDecimal(message);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();

	}
	
}
