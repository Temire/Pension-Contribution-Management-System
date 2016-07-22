package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

public class InitPFAAction extends ViewBaseAction {

	public void execute() throws Exception {

				PensionFundAdministrator pfa = UserManager.getPFAOfLoginUser();
				
	
				if(pfa==null)
					return;
				
				Map key = new HashMap();
				key.put("id", pfa.getId());
	
				getView().setModelName("PensionFundAdministrator"); // 2
				getView().setValues(key); // 3
				getView().findObject(); // 4

				getView().setKeyEditable(false);
	}
		
}