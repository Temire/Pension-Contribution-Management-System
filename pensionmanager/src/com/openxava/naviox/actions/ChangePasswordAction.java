package com.openxava.naviox.actions;

import javax.persistence.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.security.SecurityManager;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

/**
 * 
 * @author Javier Paniza
 */

public class ChangePasswordAction extends ViewBaseAction {

	public void execute() throws Exception {
		
		
		System.out.println(" The login user here ======="+Users.getCurrent());
		System.out.println("1 Get here ...");
		User user = (User)getRequest().getSession().getAttribute("user");//User.find(Users.getCurrent());
		System.out.println(" The login user here password ======="+user.getPassword());
		String currentPassword = getView().getValueString("currentPassword");
		
		System.out.println("2 Get here ...");
		if (!user.passwordMatches(currentPassword)) {
			addError("passwords_not_match");
			return;
		}
		
		System.out.println("3 Get here ...");
		String newPassword = getView().getValueString("newPassword");
		if (Is.equal(currentPassword, newPassword)) {
			addError("you_need_to_change_your_password");
			return;
		}		
		
		System.out.println("4 Get here ...");
		 
		String repeatNewPassword = getView().getValueString("repeatNewPassword");
		if (!Is.equal(newPassword, repeatNewPassword)) {
			addError("passwords_not_match");
			return;
		}
		
		System.out.println("5 Get here ...");
		SecurityManager passwordComplexity = new SecurityManager();
		if(!passwordComplexity.validate(newPassword)){
			for (String message : passwordComplexity.getMessages()) {
				addError(message, null);
			}
			return;
		}
		user.setPassword(newPassword);
		user.setFirstLogin(true);
		XPersistence.getManager().merge(user);
		addMessage("password_changed"); 
		getView().clear();
	}

}
