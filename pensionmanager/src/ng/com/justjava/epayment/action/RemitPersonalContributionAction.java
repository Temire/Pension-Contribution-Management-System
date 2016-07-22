package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.MonthlyUpload.Status;
import ng.com.justjava.epayment.model.MonthlyUpload.Type;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class RemitPersonalContributionAction extends ViewBaseAction{

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		MonthlyUpload upload = new MonthlyUpload();
		upload.setStatus(Status.approve);
		upload.setCorporate(null);
		upload.setDateEntered(Dates.createCurrent());
		upload.setEnteredBy(Users.getCurrent());
		
		
		Map bankKey = (Map) getView().getValue("bank");
		Bank bank = (Bank) MapFacade.findEntity("Bank", bankKey);
		
		String number = getView().getValueString("accountNumber");
		//String name = getView().getValueString("name");
		TransitAccount account = new TransitAccount();
		account.setBank(bank);
		account.setTerminalId(number);
		upload.setCorporate(UserManager.getCorporateOfLoginUser());
		upload.setPayingAccount(account);
		upload.setType(Type.personal);
		RSAHolder holder = UserManager.getHolderProfileOfLoginUser();
		ArrayList<RSAHolder> holders = new ArrayList<>();
		holders.add(holder);
		upload.setHolders(holders);
		XPersistence.getManager().merge(upload);
	}

}
