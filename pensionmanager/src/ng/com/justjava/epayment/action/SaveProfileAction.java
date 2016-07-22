package ng.com.justjava.epayment.action;

import java.util.*;

import javax.ejb.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Approver.ApprovableTransaction;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

import com.openxava.naviox.model.*;

public class SaveProfileAction extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub

		Profile profileEntity = null;
		Map profile =  getView().getAllValues();
		
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		if(corporate==null)
			return;
		try {
			if(corporate!=null){
				Map corporateMap = new HashMap();
				corporateMap.put("id", corporate.getId());
				profile.put("corporate", corporateMap);
			
			}
			//profile = (Profile)getView().getEntity();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(profile.get("id") == null){	
			Role role = null;
			try {
				role = (Role) MapFacade.findEntity("Role",(Map)profile.get("role"));
			} catch (ObjectNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(role == null)
				MapFacade.create("Role",(Map)profile.get("role"));
			
			profileEntity = (Profile) MapFacade.create("Profile",profile);
			
		}else{
			MapFacade.setValues("Role",(Map)profile.get("role"), (Map)profile.get("role"));
			MapFacade.setValues("Profile",getView().getAllValues(), profile);
			profileEntity = (Profile) MapFacade.findEntity("Profile", profile);
		}
		if(profileEntity.getTransaction()==ApprovableTransaction.MonthlyRemittance && profileEntity.isApprover()){
			if(corporate.getHighestApprovalLevel()<profileEntity.getLevel()){
				corporate.setHighestApprovalLevel(profileEntity.getLevel());
				corporate = XPersistence.getManager().merge(corporate);
			}
		}
		addMessage("Profile Successfully Saved", null);
	}

}
