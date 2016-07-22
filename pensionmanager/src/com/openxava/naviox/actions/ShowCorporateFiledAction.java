package com.openxava.naviox.actions;

import org.openxava.actions.*;

public class ShowCorporateFiledAction extends OnChangePropertyBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Boolean showCorporate = (Boolean) getView().getValue("corporateSignIn");
		
		if(showCorporate)
			getView().setHidden("corporate", false);
		else
			getView().setHidden("corporate", true);
		
	}

}
