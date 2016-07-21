package ng.com.justjava.epayment.action;

import java.util.*;

import org.openxava.actions.*;

public class LoadIdentityManagerAction extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		System.out.println(" The task around here====" + getView().getAllValues());
		String sid = getView().getValueString("stateIdentification");
		if(sid == null)
			return;
		
		Map loadedUser = loadUserFromIdentityManager(Integer.valueOf(sid));
		if(loadedUser.isEmpty()){
			addWarning("State Identification Number Not Yet Available", null);
		}else{
			getView().setValue("user", loadedUser);
			
			//getView().setEditable(name, editable);
			getView().getSubview("user").setEditable("phoneNumber",false);
			getView().getSubview("user").setEditable("email",false);
			getView().getSubview("user").setEditable("givenName",false);
			getView().getSubview("user").setEditable("address",false);
			getView().getSubview("user").setEditable("familyName",false);
		
		}
		//getView().refresh();
	}
	
	private Map loadUserFromIdentityManager(int caseSwitch){
		
		Map user = new HashMap();
		//int caseSwitch = 100;
		switch (caseSwitch) {
		case 100:
			user.put("phoneNumber", "0898883883");
			user.put("address", "Gbagada Lagos");
			user.put("givenName", "Moshood");
			user.put("familyName", "Banks");
			user.put("email", "mbanks@chams.com");			
			break;
			
		case 200:
			user.put("phoneNumber", "07062023181");
			user.put("address", "Ilupeju Lagos");
			user.put("givenName", "Akinyemi");
			user.put("familyName", "Akinrinde");
			user.put("email", "akinrinde@justjava.com.ng");			
			break;
		case 300:
			user.put("phoneNumber", "909939939");
			user.put("address", "Bodija");
			user.put("givenName", "Akinkunmi");
			user.put("familyName", "Aderinto");
			user.put("email", "aderinto@gmail.ng");			
			break;			
		default:
			break;
		}


		
		return user;//(Map) new HashMap().put("user", user);
	}
}
