package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;

public class PensionPayitemSetupAction extends ViewBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		if(corporate==null)
			return;
		
		Double employerContribution = (Double) getView().getValue("companyContribution");
		Double employeeContribution = (Double) getView().getValue("rsaHolderContribution");
		corporate.setCompanyContribution(employerContribution);
		corporate.setRSAHolderContribution(employeeContribution);
		corporate = XPersistence.getManager().merge(corporate);
		List<PayItem> allObjects = (List<PayItem>) getView().
				getSubview("payItems").getCollectionObjects();
		
		List<PayItem> selectedObjects = (List<PayItem>) getView().
				getSubview("payItems").getCollectionSelectedObjects();
		
		
		for(PayItem payItem:allObjects){
			CompanyPayitem companyPayitem = new CompanyPayitem();
			companyPayitem.setCompany(corporate!=null?corporate:null);
			companyPayitem.setPayItem(payItem);
			CompanyPayitem duplicate = companyPayitem.find();
			if(duplicate!=null)
				companyPayitem = duplicate;
			
			if(selectedObjects.contains(payItem))
				companyPayitem.setActive(true);
			else
				companyPayitem.setActive(false);
			
			
			
/*			if(duplicate != null && selectedObjects.contains(payItem))
				XPersistence.getManager().merge(duplicate);
			*/
/*			if(duplicate != null && !selectedObjects.contains(payItem)){
				duplicate.setActive(false);
				//XPersistence.getManager().merge(duplicate);
			
			}if(duplicate == null && selectedObjects.contains(payItem)){
				companyPayitem.setActive(true);
				//XPersistence.getManager().merge(companyPayitem);
			}*/
			XPersistence.getManager().merge(companyPayitem);
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n 11111111TThe value to be set===" 
					+payItem.getName() + "  the corporate ==" + (corporate!=null?corporate.getName():"null"));
		}
		addMessage("Pension Payitem Successfully Saved!", null);
		
	}

}
