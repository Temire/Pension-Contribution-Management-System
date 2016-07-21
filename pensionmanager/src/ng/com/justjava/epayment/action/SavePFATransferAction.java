package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;



public class SavePFATransferAction extends SaveAction {
	
	public void execute() throws Exception {	
		super.execute();
		Map values = getView().getValues();
		Map pfaId = (Map) values.get("pfa"); 
		PensionFundAdministrator administrator = (PensionFundAdministrator) 
				MapFacade.findEntity("PensionFundAdministrator", pfaId);
		RSAHolder holder = UserManager.getHolderProfileOfLoginUser();
		
		
		System.out.println(" The value of map here===" + values + " administrator here=="+
				administrator.getName());
		
		String content = holder.getFirstName() + " " +holder.getSecondName() + " Initiated PFA Transfer From " +
				holder.getPfa().getName() + ", Awaiting Your Approval";
		if(administrator.getMyAdmin() !=null)
			SystemWideSetup.sendNotification(administrator.getMyAdmin().getUser().getEmail(), "PFA Transfer Initiated", content, content, holder.getPhoneNumber());
		
		//addMessage("Request to Change Your PFA To " ,null);
		addMessage("Request Has Been Successfully Forwarded to " + administrator.getName() +" for Approver", null);
	}
}
