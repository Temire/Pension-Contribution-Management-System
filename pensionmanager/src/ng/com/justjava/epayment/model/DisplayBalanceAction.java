package ng.com.justjava.epayment.model;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.model.*;

public class DisplayBalanceAction extends OnChangePropertyBaseAction{

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map key = (Map) getView().getValue("payingAccount");
		TransitAccount payingAccount = (TransitAccount) MapFacade.findEntity("TransitAccount", key);
		addMessage("The Bank Balance on this account is " + payingAccount.getBankBalance(), null);
		
		
	}

}
