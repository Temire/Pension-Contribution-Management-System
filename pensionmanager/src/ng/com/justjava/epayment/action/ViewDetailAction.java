package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

public class ViewDetailAction extends OnSelectElementBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		//List list = getSelectedObjects();
		String name = getCollectionElementView().getModelName();
		int status = 5;
		System.out.println(" The Model Name "+getView().getModelName());
		System.out.println(" All Values == "+getView().getAllValues());
		
		PensionFundAdministrator  pfa = null;
		Long pfaId = null;
		Corporate corporate = null;
		System.out.println(" about to show modal " + name + " selected row==" + getRow());
		PensionFundCustodian pfc = UserManager.getPFCOfLoginUser();
		String lastFilter = "";
		if(Is.equalAsStringIgnoreCase(getView().getModelName(), "MonthlyUpload")){
			Long id = (Long) getView().getAllValues().get("id");
			lastFilter = " AND ${upload.id}="+id;
			status = 1;
			System.out.println(" Inside MonthlyUpload");
		}
		
		if(Is.equalAsString("PensionFundAdministrator", name)){
		
				pfa = (PensionFundAdministrator) getCollectionElementView().getCollectionObjects().get(getRow());
				pfaId = pfa!=null?pfa.getId():0;
				corporate = UserManager.getCorporateOfLoginUser();
				
		}else if(Is.equalAsString("Corporate", name)){
			pfa = UserManager.getPFAOfLoginUser();
			pfaId = pfa!=null?pfa.getId():0;
			corporate = (Corporate) getCollectionElementView().getCollectionObjects().get(getRow());
		}
		System.out.println(" pfa here " + pfa);
		
		if(pfc==null){
			
			System.out.println(" Showing the dialog as expected=====");
			showDialog();
		}else{
			pfaId = (Long) getRequest().getSession().getAttribute("pfaId");
			pfa = XPersistence.getManager().find(PensionFundAdministrator.class, pfaId);
			
			System.out.println(" This is what I'm retrieving from session=="+pfaId);
			corporate = (Corporate) getCollectionElementView().getCollectionObjects().get(getRow());
		}
		
		
		System.out.println("0 Not returning here..........................pfaId=="+pfaId);
		
		if(pfaId==null || pfaId ==0)
			return;
		
		
		System.out.println("1 Not returning here..........................");
		Map key = new HashMap();
		key.put("id", pfaId);

		System.out.println("2 Not returning here..........................");
		
		
		getView().setTitle(pfa.getName() + " Details");
		
		System.out.println("2a Not returning here..........................");
		
		
		
		getView().setModelName("PensionFundAdministrator"); // 2
		
		System.out.println("2b Not returning here..........................");
		getView().setViewName("pfaHolders");
		System.out.println("2c Not returning here..........................");
		String condition="${upload.status}=" +status+" AND ${pfa.id}=" + pfaId + " AND "
				+ "${corporate.id}="+corporate.getId() +lastFilter;
		System.out.println("See the condition Here......................." + condition);
		//String condition="${pfa.id}=3 AND ${corporate.id}=1";
		getView().setValues(key); // 3
		getView().findObject(); // 4
		
		System.out.println("2e Not returning here..........................");
		getView().getSubview("holders").getCollectionTab().setBaseCondition(condition);
		System.out.println("2f Not returning here..........................");
		getView().setKeyEditable(false);

		System.out.println("3 Not returning here..........................condition=="+condition);
		addActions("Return.close");
		
	}

}
