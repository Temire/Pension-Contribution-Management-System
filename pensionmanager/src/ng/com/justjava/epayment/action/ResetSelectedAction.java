package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

import com.openxava.naviox.model.*;

public class ResetSelectedAction extends TabBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map[] selectedKeys = getTab().getSelectedKeys();
		SystemWideSetup setup = SystemWideSetup.getSystemWideSetup();
		if(setup == null){
			addError("SystemWide Parameter Not Yet Set", null);
			return;
		}
		for(Map key : selectedKeys){
			String password = RandomStringUtils.randomAlphanumeric(15);
			CorporateUser corporateUser = (CorporateUser) MapFacade.findEntity("CorporateUser", key);
			User user = corporateUser.getUser();
			user.setPassword(password);
			XPersistence.getManager().merge(user);
			
			
			SystemWideSetup.sendMail(corporateUser.getUser().getEmail(), "Password", password);
		}
		
		getTab().deselectAll();
		addMessage("Password Reset Successfully", null);
	}

}
