package ng.com.justjava.epayment.model;

import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

public class AddUserToGroup extends GoAddElementsToCollectionAction {
	public void execute() throws Exception {
		super.execute();  // It executes the standard logic, that shows a dialog
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		if(corporate != null){
			getTab().setBaseCondition("${corporate.id} = " + corporate.getId());
		}
		
	}
}
