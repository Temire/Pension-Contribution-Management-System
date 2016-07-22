package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.test.*;
import oneapi.client.impl.*;
import oneapi.config.*;
import oneapi.config.Configuration;
import oneapi.model.*;

import org.joda.time.*;
import org.kamranzafar.otp.*;
import org.openxava.actions.*;
import org.openxava.util.*;

public class GenerateTokenAction  extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		String otpKey = "12345678";
		String token = OTP.generate("" + otpKey, "" + System.currentTimeMillis(), 6, "totp");
		DateTime dateTime = new DateTime(Dates.withTime(Dates.createCurrent()));
		DateTime fiveMinutes = dateTime.plusMinutes(5);
		getView().putObject("fiveMinutes", fiveMinutes);
		getView().putObject("token", token);

		CorporateUser corporateUser = UserManager.getLoginCorporateUser();
		if(corporateUser == null){
			addError("User not allowed to view transaction", null);
			return;
		}
		
		String medium = XavaPreferences.getInstance().getXavaProperty("notification", "EMAIL");
		
		if(medium.toLowerCase().contains("sms")){
			
			String smsUserName = XavaPreferences.getInstance().getXavaProperty("smsUserName", "justjava1");
			String smsPassword = XavaPreferences.getInstance().getXavaProperty("smsPassword", "changeme1A");
			Configuration configuration = new Configuration (smsUserName, smsPassword);
			
			configuration.setApiUrl("http://oneapi.infobip.com");
			SMSClient smsClient = new SMSClient(configuration);
			
			String phone = corporateUser.getUser().getPhoneNumber();
			if(phone.startsWith("0")){
				phone = "+234"+phone.substring(1);
			}else
				phone = phone.startsWith("+")?phone:"+"+phone;
			
			
			SMSRequest smsRequest = new SMSRequest("+2347062023181",
					" Use Token "+ token + " To Complete The Transaction",phone);
			
			// Store request id because we can later query for the delivery status with it:
			SendMessageResult sendMessageResult = smsClient.getSMSMessagingClient().sendSMS(smsRequest);
		
		}
		
		SystemWideSetup.sendMail( corporateUser.getUser().getEmail(), 
				 "Token", " Use  Token "+ token + " to complete the transaction");	
		addMessage("Token Sucessfully Generated. Check your mail", null);
	}

}
