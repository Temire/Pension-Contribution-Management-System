package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.utility.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.controller.*;
import org.openxava.util.*;

public class PaymentAction extends ViewBaseAction implements IForwardAction {
	
	String terminalId =null;
	String TRANSACTION_ID = null;
	String amount = null;
	String CHECKSUM = null;
	String email = null;
	String responseUrl = null;
	String secretKey = null;	
	
	
	public java.lang.String getForwardURI(){
		return "http://localhost:8080/pensionmanager/testing.jsp?TERMINAL_ID="+terminalId+"&TRANSACTION_ID="
				+TRANSACTION_ID+"&amount="+amount+"&CHECKSUM="+CHECKSUM+"&email="+email+"&responseUrl="+responseUrl
				+"&secretKey="+secretKey;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
/*		System.out.println(" Inside PaymentAction holder profile " +
		UserManager.getHolderProfileOfLoginUser()
		.getPfa().getAccount().getNumber());*/
		
		terminalId = "0000000001";
		responseUrl = "http://localhost:8080/pensionmanager/testing.jsp";
		secretKey = "DEMO_KEY";
		TRANSACTION_ID = RandomStringUtils.randomAlphanumeric(12);
		amount = getView().getValueString("amount"); 
		CHECKSUM = Cryptor.getCheckSum(amount, terminalId, TRANSACTION_ID, 
				responseUrl, secretKey); 
		email = "akinrinde@justjava.com.ng"; 
		
	}

	@Override
	public void setErrors(Messages errors) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Messages getErrors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMessages(Messages messages) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Messages getMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEnvironment(Environment environment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean inNewWindow() {
		// TODO Auto-generated method stub
		return true;
	}
}
