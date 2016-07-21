package ng.com.justjava.epayment.action;

import java.io.*;
import java.security.*;
import java.util.*;

import javax.crypto.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Payment.*;
import ng.com.justjava.epayment.model.RemitPension.Months;
import ng.com.justjava.epayment.utility.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;

import com.etranzact.fundgate.ws.*;

public class RemitPensionAction extends SaveAction{
	public void execute() throws Exception {
		if(true){
			Collection<PensionFundAdministrator> pfas = getView().getSubview("paymentSummary").getCollectionObjects();
			for (PensionFundAdministrator pensionFundAdministrator : pfas) {
				System.out.println(" The Role Played Here===="+pensionFundAdministrator.getName()
						+ " Amount =="+pensionFundAdministrator.getAmountSummation());
			}
			return;
		}
		super.execute();
		FundResponse response = WebserviceUtil.getPort().process(getBulkFundRequest());
		
        System.out.println("Pay Result Code = "+response.getError());
        System.out.println("Pay Result Message = "+response.getMessage());
        System.out.println("Pay Result Ref = "+response.getReference());
        System.out.println("Pay Result OtherRef = "+response.getOtherReference());
        System.out.println("Pay Result Amount = "+response.getAmount());
        System.out.println("Pay Result TotalFailed = "+response.getTotalFailed());
        System.out.println("Pay Result TotalSuccess = "+response.getTotalSuccess());
        System.out.println("Pay Result Company = "+response.getCompanyId());
        System.out.println("Pay Result Action = "+response.getAction());	
        
  /*      if("0".equalsIgnoreCase(response.getError())){
        	this.setStatus(Status.Sent);
        	this.setQueryString(response.getOtherReference());
        }else{
        	this.setStatus(Status.ErrorSendingPayment);
        	this.setErrorCode(response.getError());
        	this.setErrorMessage(response.getMessage());
        }*/
	}
	
	public FundRequest getBulkFundRequest(){
		//List<PaymentInstruction> payItems = XPersistence.getManager().createQuery(arg0)
        FundRequest request = new FundRequest();

        request.setAction("BT");
        String viewPin = getView().getValueString("pin");
        
        Corporate corporate = (Corporate) UserManager.getCorporateOfLoginUser();
        
        String terminalId = corporate.getTerminalId();
        
        String pin = "";
		try {
			pin = Cryptor.encrypt(viewPin, corporate.getMasterKey());
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
        trans.setSenderName(corporate.getName());	
        //trans.setSenderName("eTranzact");
		
		BulkItems bulkItems = new BulkItems();
		Months month = (Months) getView().getValue("month");
		Collection<PensionFundAdministrator> pfas = getView().getSubview("paymentSummary").getCollectionObjects();
		
		//List<PaymentInstruction> payItems = (List<PaymentInstruction>) getPaymentInstructions();
		for(PensionFundAdministrator payItem:pfas){
			bulkAmount = bulkAmount + payItem.getAmountSummation().doubleValue(); 
			BulkItem item = new BulkItem();
			item.setBeneficiaryName(payItem.getName());
			item.setAccountId(payItem.getAccount().getNumber());
			item.setAmount(payItem.getAmountSummation().doubleValue());
			item.setBankCode(StringUtils.trim(payItem.getAccount().getBank().getCode()));
			item.setNarration(payItem.getId() + "_" +  
			"Pension Contribution for Month"+month+ " From "+corporate.getName());
			// item.setBankCode("033");
			item.setUniqueId(payItem.getId() + "_" + RandomStringUtils.randomAlphanumeric(18).toLowerCase());
			bulkItems.getBulkItem().add(item);            
		}
		trans.setAmount(bulkAmount);//bulk amount
		//trans.setBulkItems(bulkItems);
        request.setTransaction(trans);
		return request;
	}

}
