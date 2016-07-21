package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

public class LoadMyCorporateAction extends ViewBaseAction {

	public void execute() throws Exception {
		try {

			if (getView().getModelName() != null && getView().getModelName().trim().equalsIgnoreCase("corporate")) {


				Corporate corporate = (Corporate) UserManager.getCorporateOfLoginUser();
				
				if(corporate==null)
					return;
				getView().setViewName("myCorporate");
				Double companyContribution = (Double) getView().getValue("companyContribution");
				

				
				Map key = new HashMap();
				key.put("id", corporate.getId());
				if(corporate.isReceiving()){
					getView().setViewName("receiver");
				}
				getView().setModelName("Corporate"); // 2
				getView().setValues(key); // 3
				getView().findObject(); // 4

				getView().setEditable("name", false);
				getView().setEditable("rccNo", false);
				getView().setHidden("processingFee", true);
				getView().setKeyEditable(false);
				if(companyContribution== null || companyContribution<=0d){
					PensionSystemSetup pensionSetup = SystemWideSetup.getSystemWideSetup().getPensionSystemSetup();
					getView().setValue("companyContribution", pensionSetup.getEmployerContributionPercent());
					System.out.println(" The companyContribution===="+companyContribution);
				}
			}			// getView().setEditable(false);
			if (getView().getModelName() != null && getView().getModelName().trim().equalsIgnoreCase("PensionFundAdministrator")) {


				PensionFundAdministrator pfa = (PensionFundAdministrator) UserManager.getPFAOfLoginUser();
				
				if(pfa==null)
					return;
				
				Map key = new HashMap();
				key.put("id", pfa.getId());

				getView().setModelName("PensionFundAdministrator"); // 2
				getView().setValues(key); // 3
				getView().findObject(); // 4

				getView().setEditable("name", false);
				getView().setEditable("rccNo", false);
				//getView().setHidden("processingFee", true);
				getView().setKeyEditable(false);
			}
			if (getView().getModelName() != null && getView().getModelName().trim().equalsIgnoreCase("PensionFundCustodian")) {


				PensionFundCustodian pfc = (PensionFundCustodian) UserManager.getPFCOfLoginUser();
				
				if(pfc==null)
					return;
				
				Map key = new HashMap();
				key.put("id", pfc.getId());

				getView().setModelName("PensionFundCustodian"); // 2
				getView().setValues(key); // 3
				getView().findObject(); // 4

				getView().setEditable("name", false);
				getView().setEditable("rccNo", false);
				//getView().setHidden("processingFee", true);
				getView().setKeyEditable(false);
			}			
		}catch (Exception ex) {
			ex.printStackTrace();
			addError("system_error");
		}
	}

}
