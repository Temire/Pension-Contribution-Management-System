package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.RemitPension.*;

import org.openxava.actions.*;
import org.openxava.model.*;

public class ApproveRemitPensionAction extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map keyValue = getView().getKeyValues();
		Map values = getView().getAllValues();
		values.put("status", RemitPension.Status.approve);
		MapFacade.setValues("RemitPension", keyValue, values);
		setNextMode(LIST);
		addMessage("Pension Remittance Has Been Forwarded for Final Remittance", null);
		
	}

}
