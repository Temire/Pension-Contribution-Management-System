package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

/**
 * @author Javier Paniza
 */

public class MySearchAction extends ReferenceSearchAction {
		
	public void execute() throws Exception {
		super.execute();
		String condition =  "";
		//getTab().setTabName("Other"); 
		
		if(true)
			return;
		
System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n The SQL ==="
		+ condition + " The propertie name here =====" + getTab().getPropertiesNamesAsString());	

		
		Long corporateId = UserManager.getCorporateOfLoginUser()==null?0L:
			UserManager.getCorporateOfLoginUser().getId();
		if(getTab().getPropertiesNamesAsString()!=null && 
				getTab().getPropertiesNamesAsString().indexOf("terminalId") >= 0){
			 condition = "${corporate.id}=" + corporateId;
		}
		
		
System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n The SQL ==="
		+ condition + " The propertie name here =====" + getTab().getPropertiesNamesAsString());		

		getTab().setBaseCondition(condition);
	}

}
