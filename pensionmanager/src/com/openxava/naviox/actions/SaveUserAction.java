package com.openxava.naviox.actions;

import org.openxava.actions.*;
import org.openxava.util.*;

/**
 * 
 * @author Javier Paniza
 */
public class SaveUserAction extends SaveAction {
	
	public void execute() throws Exception {
		String password = getView().getValueString("password"); 
		String repeatPassword = getView().getValueString("repeatPassword");
		if (!Is.equal(password, repeatPassword)) {
			addError("passwords_not_match", "password");
			return;
		}
		super.execute();
	}

}
