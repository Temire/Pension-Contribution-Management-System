package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.model.*;

public class SaveInvestorAction extends SaveAction{
	public void execute() throws Exception {	
		Map values = getView().getAllValues();

		System.out.println(" The values sent 1===" + values);
		if(!values.containsKey("id") || values.get("id")==null){
			Corporate fundManagerObj = UserManager.getCorporateOfLoginUser();
			Map fundManager = new HashMap<String, Long>();
			fundManager.put("id", fundManagerObj.getId());
			values.put("fundManager", fundManager);
			MapFacade.createReturningKey("Investor", values);
		}else
			MapFacade.setValues("Investor", values,values);
		
		addMessage("Investor Successfully Saved", null);
		System.out.println(" The values sent 2===" + values);
		
		//values.put(fundManager, value)
	}
}
