package com.openxava.naviox.actions;

import org.openxava.actions.*;
import org.openxava.util.*;

import com.openxava.naviox.*;
import com.openxava.naviox.impl.*;
import com.openxava.naviox.model.*;

/**
 * 
 * @author Javier Paniza 
 */
public class SignInAction extends ViewBaseAction implements IForwardAction {
	
	private String forwardURI = null;

	public void execute() throws Exception {		
		String userName = getView().getValueString("user");
		String password = getView().getValueString("password");
		if (Is.emptyString(userName, password)) {
			addError("unauthorized_user"); 
			return;
		}		
		if (!SignInHelper.isAuthorized(userName, password)) {
			addError("unauthorized_user"); 
			return;									
		}
		SignInHelper.signIn(getRequest().getSession(), userName);
		
		
		
		
		Users.getCurrentPreferences().put("month",String.valueOf(Dates.createCurrent().getMonth()));
		
		
		System.out.println("0 The month saved===="+
				Users.getCurrentPreferences().get("month", String.valueOf(7)));

		
		getView().reset();
		String originalURI = getRequest().getParameter("originalURI");
		if (originalURI == null) {
			forwardURI = "/";
		}
		else {
			int idx = originalURI.indexOf("/", 1);			
			if (!originalURI.endsWith("/SignIn") && idx > 0 && idx < originalURI.length()) {
				forwardURI = originalURI.substring(idx);
			}
			else {
				forwardURI = "/";
			}
		}
		
	}
	
	public String getForwardURI() {
		return forwardURI;
	}

	public boolean inNewWindow() {
		return false;
	}

}