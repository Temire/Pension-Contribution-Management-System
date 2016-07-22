package com.openxava.naviox.actions;

import org.openxava.actions.*;

public class RegisterAction extends ViewBaseAction {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		showDialog();
		getView().setTitle("Personal Pension Contribution");
		getView().setModelName("RegisterPersonalContribution"); 
		addActions("Register.register");
		
	}

}
