package com.openxava.naviox.impl;

import java.util.*;

import javax.servlet.http.*;

import org.openxava.jpa.*;
import org.openxava.util.*;

import com.openxava.naviox.*;
import com.openxava.naviox.model.*;

/**
 * 
 * @author Javier Paniza
 */

public class SignInHelper {
	
	public static void signIn(HttpSession session, String userName) {
		session.setAttribute("naviox.user", userName);
		session.setAttribute("xava.portal.userinfo", toUserInfo(userName));
		Modules modules = (Modules) session.getAttribute("modules");
		modules.reset();		
	}
	
	public static boolean isAuthorized(String userName, String password) {
		User user = null;
		ArrayList<User> users = (ArrayList<User>) XPersistence.getManager().
								 createQuery(" FROM User u WHERE u.name='"+userName+"'").
								 getResultList();
		if(!users.isEmpty())
			user = users.get(0);
		
		return user != null && user.isAuthorized(password);
	}	 

	private static UserInfo toUserInfo(String userName) {
		User user = User.find(userName);
		UserInfo info = new UserInfo();
		info.setId(user.getName());
		info.setGivenName(user.getGivenName());
		info.setFamilyName(user.getFamilyName());
		info.setEmail(user.getEmail());
		info.setJobTitle(user.getJobTitle());
		info.setMiddleName(user.getMiddleName());
		info.setNickName(user.getNickName());
		info.setBirthDateYear(Dates.getYear(user.getBirthDate()));
		info.setBirthDateMonth(Dates.getMonth(user.getBirthDate()));
		info.setBirthDateDay(Dates.getDay(user.getBirthDate()));
		info.setUserDetail(user.getUserDetail());
		return info;
	}

}
