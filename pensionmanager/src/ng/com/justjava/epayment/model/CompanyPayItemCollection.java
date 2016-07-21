package ng.com.justjava.epayment.model;
import javax.persistence.*;

import ng.com.justjava.epayment.action.*;

import org.openxava.annotations.*;
import org.openxava.util.*;


@Embeddable
public class CompanyPayItemCollection {
	
	private String code;
	private String name;
	
	//@OnChange(ValidateCompulsoryItemAction.class)
	private boolean active;
	
	private boolean compulsory;
	
	
	public boolean isEnable(){
		if(Is.equalAsStringIgnoreCase(code,"TRAN") || Is.equalAsStringIgnoreCase(code,"BAS")||
				Is.equalAsStringIgnoreCase(code, "HOUS") ){
			return true;
		}
		return active;
	}
	
	public boolean isActive() {
		return active;
	}

	
	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCompulsory() {
		return compulsory;
	}

	public void setCompulsory(boolean compulsory) {
		this.compulsory = compulsory;
	}

	public String getRemark() {
		if(isCompulsory()){
			return "Compulsory PayItem, deactivating it has not effect";
		}
		return "";
	}

}
