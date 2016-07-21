package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.util.*;

public class InitPFCAction extends ViewBaseAction {

	public void execute() throws Exception {

				
				PensionFundCustodian pfc = UserManager.getPFCOfLoginUser();
				
	
				if(pfc==null)
					return;
				
			System.out.println(" Model Name ====" + getView().getModelName());	
			String pfcName = pfc!=null?pfc.getName():"";
			
			if(Is.equalAsStringIgnoreCase("PeriodicViewOfRSAHoldersByPFC",getView().getModelName())){
				getView().setValue("name", pfcName);
				return;
			}
			
				Map key = new HashMap();
				key.put("id", pfc.getId());
	System.out.println(" The sent pfc id == " + pfc.getId());
	
				getView().setModelName("PensionFundCustodian"); // 2
				getView().setValues(key); // 3
				getView().findObject(); // 4
				System.out.println(" Loading PFC here view name==" + getView().getViewName());
				getView().setKeyEditable(false);
				//getRequest().getSession().setAttribute("view", getView());
	}
		
}