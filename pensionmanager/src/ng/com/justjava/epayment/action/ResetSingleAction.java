package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

import com.openxava.naviox.model.*;

public class ResetSingleAction extends TabBaseAction {

	private int row;
	
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map key = (Map) getTab().getTableModel().getObjectAt(row);
		String pswd = RandomStringUtils.randomAlphanumeric(15);
		CorporateUser corporateUser = (CorporateUser) MapFacade.findEntity("CorporateUser", key);
		
		User user = corporateUser.getUser();
		user.setPassword(pswd);
		XPersistence.getManager().merge(user);
		
		SystemWideSetup setup = SystemWideSetup.getSystemWideSetup();
		
		if(setup == null){
			addError("SystemWide Parameter Not Yet Set", null);
			return;
		}
		SystemWideSetup.sendMail(corporateUser.getUser().getEmail(), "Password", pswd);
		
		addMessage("Password Reset Successfully", null);
		
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

}
