package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.FundTransfer.Destination;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.model.*;

import com.etranzact.fundgate.ws.*;
import com.etranzact.fundgate.ws.Transaction;

public class TransferToAccountAction extends ViewBaseAction{
	
	
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
		Destination endpointEnum = (Destination)getView().getValue("endpoint");
		String endpoint = "";
		if(endpointEnum == Destination.toAccount){
			endpoint = "A";
		}else if (endpointEnum == Destination.toCard){
			endpoint = "C";
		}
		
/*		if(endpoint == null){
			addError("", ids);
			return;
		}*/
		String terminalId = getView().getValueString("cardId");
		String pin = getView().getValueString("pin");
		String account = getView().getValueString("accountNumber");
		Map bankMap = (Map) getView().getValue("bank");
		

		//String bankCode =(String) bank.get("code");
		double amount = (Double)getView().getValue("amount");
		

		try {
			Bank bank = (Bank) MapFacade.findEntity("Bank", bankMap);
			pin = Cryptor.encrypt(pin, "KEd4gDNSDdMBxCGliZaC8w==");
					
			System.out.println(" terminalId=="+terminalId + " pin=="+pin+" account=="+account
							+ " bank code=="+bank.getCode() + " amount=="+amount);

			FundRequest request = getFundRequest(endpoint,terminalId, pin, account, bank.getCode() ,amount);
			
			FundResponse response = WebserviceUtil.getPort().process(request);
			
            System.out.println("Result Code = "+response.getError());
            System.out.println("Result Message = "+response.getMessage());
            System.out.println("Result Ref = "+response.getReference());
            System.out.println("Result OtherRef = "+response.getOtherReference());
            
            addMessage(response.getMessage(), null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private FundRequest getFundRequest(String endpoint,String terminalId, String pin,String account,String bankCode,double amount){

        FundRequest request = new FundRequest();

        try {
			request.setAction("FT");
			//request.setTerminalId("7000000001");
			request.setTerminalId(terminalId);
			Transaction t = new Transaction();
			//t.setPin("ZhXy4geRgnpqVOH/7V2beg==");
			t.setPin(pin);
			//t.setDestination("0025840828");
			t.setDestination(account);
			//t.setReference("100dcxssrg5588p6");
			t.setReference(Cryptor.generateKey());
			t.setEndPoint(endpoint);
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

}
