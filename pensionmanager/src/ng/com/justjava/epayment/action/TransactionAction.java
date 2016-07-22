package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.joda.time.*;
import org.kamranzafar.otp.*;
import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class TransactionAction extends SearchByViewKeyAction { // 1

	public void execute() throws Exception {

		Map key = (Map) getView().getKeyValues();
		Transaction trans = (Transaction) MapFacade.findEntity("Transaction",
				key);
		
		CorporateUser corporateUser = UserManager.getLoginCorporateUser();
		if(corporateUser == null){
			addError("User not allowed to view transaction", null);
			return;
		}
		// Object payItem =
		// (Object)XPersistence.getManager().find(Class.forName(trans.getRequestClassName()),
		// trans.getRequestId());

		Map mainkey = new HashMap();
		mainkey.put("id", trans.getModelId());

		String modelName = trans.getModelName().replaceAll(
				"ng.com.justjava.epayment.model.", "");
		// showNewView();
		// getView().setViewName("approve");
		
		showDialog();
		getView().setModelName(modelName); // 2
		getView().setValues(mainkey); // 3
   
		getView().setEditable("batchNumber", false);
		
		if(!UserManager.loginUserHasRole("funder")){
			getView().setHidden("payingAccount", true);
		}
		
		
		getView().putObject("final", false);
		
		DateTime dateTime = new DateTime(Dates.withTime(Dates.createCurrent()));
		DateTime fiveMinutes = dateTime.plusMinutes(5);
		try {

/*			String otpKey = "12345678";
			String token = OTP.generate("" + otpKey, "" + System.currentTimeMillis(), 6, "totp");
			
			getView().putObject("fiveMinutes", fiveMinutes);
			getView().putObject("token", token);*/
			System.out.println("Sending the mail right now!!!");
			SystemWideSetup setup = SystemWideSetup.getSystemWideSetup();
			if(setup == null){
				addError("System Wide Not Setup Yet", null);
				return;
			}
/*			SystemWideSetup.sendMail(corporateUser.getUser().getEmail(), 
					 "Token", " Use  "+ token + " to complete the transaction");*/	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getView().putObject("transId", trans.getId());

		
	
		getView().setHidden("softToken", true);
		getView().findObject(); // 4
		// getView().addDetailAction("Approve.approve");
		//getView().setEditable(false);
		if ("Y".equalsIgnoreCase(trans.getFinalApprover())) {
			getView().putObject("final", true);
			getView().setHidden("softToken", false);
			// Emails.
			addInfo(
					"This is final approval and will remit the payment instruction!",null);
			addInfo("You Need Token To Complete This Transaction!",null);
		}
		// setNextMode(LIST);
		setControllers("Return");
		// addInfo("detail","'" + payItem.toString() + "'");

	}
}