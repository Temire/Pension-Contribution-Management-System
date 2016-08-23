package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;
import ng.com.justjava.epayment.utility.*;

public class LoadByPencommNumberAction extends OnChangePropertyBaseAction{

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
		String getPencomm = getView().getValueString("PIN");
		
		Validation getValidate = new Validation();
		
	     if (getValidate.validatePin(getPencomm) == true){
	    	 addError("Contains special character");
			 getView().clear();
			 return;
	     }
 	
		
		RSAHolder holder = RSAHolder.findByPencommNumber(getPencomm);
		if(holder==null){
			addError("Not Yet Register: You need to Register first with PFA", null);
			getView().clear();
			return;
		}
		PensionFundAdministrator pfa = holder.getPfa();
		Map pfaMap = new HashMap<>();
		pfaMap.put("id", pfa.getId());
		
		if(holder != null){
			getView().setValue("firstName",holder.getFirstName());
			getView().setValue("pfa",pfaMap);
			getView().setValue("PIN",holder.getPencommNumber());
			getView().setValue("email",holder.getEmail());
			getView().setValue("lastName",holder.getSecondName());
			getView().setValue("phoneNumber",holder.getPhoneNumber());
		}
	}

}
