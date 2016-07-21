package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

public class PensionPayitemSetupInitAction extends ViewBaseAction  {

	public void execute() throws Exception {
		
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		if(corporate==null)
			return;
		

		double companyPercent = corporate.getCompanyContribution();
		double rsaPercent = corporate.getRSAHolderContribution();
		
		PensionSystemSetup setup = SystemWideSetup.getSystemWideSetup().getPensionSystemSetup();	
		double setupEmployerPercent = setup.getEmployerContributionPercent();
		double setupEmployeePercent = setup.getEmployerContributionPercent();
		
		List<CompanyPayitem> payItems = corporate.getPayItems();
		Map[] keys =  new HashMap[payItems.size()];
		getView().setValue("companyContribution", (companyPercent<setupEmployerPercent?setupEmployerPercent:companyPercent));
		getView().setValue("rsaHolderContribution", (rsaPercent<setupEmployeePercent?setupEmployeePercent:rsaPercent));
		
		int count = 0;
		for (CompanyPayitem payItem : payItems) {
			Map payItemKey = new HashMap();
			if(payItem.isActive()){
				payItemKey.put("code", payItem.getPayItem().getCode());
				System.out.println(" payItemKey =="+ payItem.getId());
				keys[count]=payItemKey;
				count = count + 1;
			}
		}
		getView().getSubview("payItems").getCollectionTab().setAllSelectedKeys(keys);
		resetDescriptionsCache();

		// TODO Auto-generated method stub

	}

}
