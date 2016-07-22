package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

import com.openxava.naviox.model.*;

public class SaveUserGroupAction extends CollectionElementViewBaseAction{

	@Override
	public void execute() throws Exception {
		super.executeBefore();
		// TODO Auto-generated method stub
		Corporate corporate = (Corporate)getParentView().getEntity();
		
		System.out.println("0 \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ "  Here are all the values ====="
		+getCollectionElementView().getAllValues() + "  and the corporate ====" + corporate);
		Map userGroupMap = getCollectionElementView().getAllValues();
		CorporateUserGroup userGroup = (CorporateUserGroup) getCollectionElementView().getEntity();
		
		
		userGroup.setCorporate(corporate);
		XPersistence.getManager().merge(userGroup);
		
		System.out.println("0 \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ "  The Entities probed ====="
		+ MapFacade.getKeyValues("CorporateUserGroup", userGroup));		
		
	//XPersistence
		addMessage("User_Group_Successfully_Saved", null);
		closeDialog();
		return;
	}

}
