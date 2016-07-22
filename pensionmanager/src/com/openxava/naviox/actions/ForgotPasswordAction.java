/**
 * 
 */
package com.openxava.naviox.actions;

import ng.com.justjava.epayment.model.*;

import org.apache.commons.lang.math.*;
import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

/**
 * @author user
 *
 */
public class ForgotPasswordAction extends ViewBaseAction{

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		System.out.println(" The generated values here==="+getView().getValues());
		System.out.println(" The User value here==="+getView().getValue("user"));
		String userName = getView().getValueString("user");
		if(Is.emptyString(userName)){
			addWarning("Enter Your Username ", null);
			return;
		}
		User user = User.find(userName);
		if(user==null){
			addError("Username not found", null);
			return;
		}
		
		String password = RandomStringUtils.randomAlphanumeric(15);
		user.setPassword(password);
		user.setFirstLogin(false);
		
		System.out.println(" The New password =="+ password);
		XPersistence.getManager().merge(user);
		SystemWideSetup.sendMail( user.getEmail(), "User Credentials", " Your New Password Generated is: " + password);	
		addMessage("Your New Password Has been forwarded to your Mail ", null);
	}

}
