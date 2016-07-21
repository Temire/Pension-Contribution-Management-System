package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

public class InitComplaintAction extends ViewBaseAction{

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		if(corporate==null){
			getView().setHidden("visibleToCompany", true);
		}
	}

}
