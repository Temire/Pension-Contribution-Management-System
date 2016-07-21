package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.util.*;

public class InitRemitPensionAction extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		if(corporate == null)
			return;
		
		System.out.println(" The view name here= "+getView().getViewName());
		getView().setValue("name", corporate.getName());
		if(!Is.equalAsString("initiate", getView().getViewName())){
			getView().setEditable("month", false);
			getView().setEditable("periodYear", false);
		}
		getView().addActionForProperty("summary", "RemitPension.viewDetail");
	}

}
