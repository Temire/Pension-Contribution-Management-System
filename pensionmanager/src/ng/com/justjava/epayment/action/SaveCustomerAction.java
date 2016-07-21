package ng.com.justjava.epayment.action;

import java.util.*;

import org.apache.commons.lang.math.*;
import org.openxava.actions.*;
import org.openxava.model.*;

public class SaveCustomerAction extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
		Map values = getView().getAllValues();
		//Integer custId= RandomUtils.nextInt();
		//values.put("customerId", custId);
		MapFacade.create("Customer",values );
		
		closeDialog();
		addMessage("Customer Successfully Saved: Customer Id: " + "1234543443", null);
	}

}
