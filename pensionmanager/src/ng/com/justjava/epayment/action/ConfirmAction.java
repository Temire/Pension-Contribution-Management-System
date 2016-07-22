package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

public class ConfirmAction extends OnSelectElementBaseAction {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Corporate  corporate = (Corporate) 
				getCollectionElementView().getCollectionObjects().get(getRow());
		PensionFundAdministrator administrator = (PensionFundAdministrator) getView().getEntity();
		PensionFundCustodian custodian = UserManager.getPFCOfLoginUser();
		if(corporate==null)
			return;
		
		ConfirmEntry entry = new ConfirmEntry();
		String month = Users.getCurrentPreferences().get("month", String.valueOf(2));
		
		String alreadyConfirmed = "FROM ConfirmEntry c WHERE c.corporate.id="+corporate.getId()+
				" AND c.pfa.id="+(administrator!=null?administrator.getId():0)
				+" AND c.custodian.id="+custodian.getId() +" AND c.month='"+month+"'";
		
		Collection<ConfirmEntry> entries = XPersistence.getManager().createQuery(alreadyConfirmed).getResultList();
		if(!entries.isEmpty()){
			addError("Entry Already Confirmed", null);
			return;
		}
		entry.setCorporate(corporate);
		entry.setPfa(administrator);
		entry.setCustodian(custodian);
		entry.setMonth(month);
		
		XPersistence.getManager().merge(entry);
		
		//entry.se
		addMessage(" Confirmation Successful !!!", null);
		
	}

}
