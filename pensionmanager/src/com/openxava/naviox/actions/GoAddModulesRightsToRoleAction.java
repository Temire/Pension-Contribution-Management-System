package com.openxava.naviox.actions;

import org.openxava.actions.*;

/**
 * 
 * @author Javier Paniza
 */
public class GoAddModulesRightsToRoleAction extends GoAddElementsToCollectionAction {
	
	public void execute() throws Exception {
		super.execute();
		getTab().setModelName("Module");
		setNextController("AddToModulesRights");		
	}

}
