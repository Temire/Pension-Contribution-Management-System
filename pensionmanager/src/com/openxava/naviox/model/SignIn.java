package com.openxava.naviox.model;

import javax.persistence.*;

import org.openxava.annotations.*;

import com.openxava.naviox.actions.*;

/**
 * 
 * @author Javier Paniza 
 */

public class SignIn {
	
/*	@OnChange(ShowCorporateFiledAction.class)
	@LabelFormat(LabelFormatType.SMALL)
	private boolean corporateSignIn;*/
	
/*	@Column(length=30) @LabelFormat(LabelFormatType.NO_LABEL)
	private String corporate;
	*/

	@Column(length=50) @LabelFormat(LabelFormatType.NO_LABEL)
	private String user;

	@Column(length=50) @Stereotype("PASSWORD")
	@LabelFormat(LabelFormatType.NO_LABEL)
	private String password;
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

/*	public String getCorporate() {
		return corporate;
	}

	public void setCorporate(String corporate) {
		this.corporate = corporate;
	}*/

/*	public boolean isCorporateSignIn() {
		return corporateSignIn;
	}

	public void setCorporateSignIn(boolean corporateSignIn) {
		this.corporateSignIn = corporateSignIn;
	}*/

}
