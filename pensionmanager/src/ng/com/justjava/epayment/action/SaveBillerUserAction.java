package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

public class SaveBillerUserAction extends SaveElementInCollectionAction {
 
	@Override 
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map userValues = (Map) getCollectionElementView().getAllValues().get("user");
		
		String modelName = getView().getModelName();

		
		String roleName = "pfaAdmin";
		String roleQuery = "FROM Role r where r.name='pfaAdmin'";
		String moduleQuery = "FROM Module m WHERE m.name='MyPensionFundAdministrator' ";
		if(Is.equalAsString("PensionFundCustodian", modelName)){
			roleName = "pfcAdmin";
			roleQuery = "FROM Role r where r.name='pfcAdmin'";
			moduleQuery = "FROM Module m WHERE m.name='MyPensionFundCustodian' ";
		}
		String name = (String) userValues.get("name");
		
		if(!Is.emptyString(name) && Is.equalAsString(name, "admin")){
			addError("UserName admin is Reserved: Please use other name", null);
			return;
		}
		getCollectionElementView().putObject("name", name);
		System.out.println("userValues =====" + userValues + " and the name ===" + name + " the one put==" + getCollectionElementView().getObject("name"));
		super.execute();
		
		System.out.println("11111111Before getCollectionElementView().getAllValues() =====" + getCollectionElementView().getAllValues()
				+ " and the picked name ==" + name + "the one put===" + getView().getObject("name"));
		Map userKey = new HashMap();
		userKey.put("name", name);
		User user = (User) MapFacade.findEntity("User", userKey) ;
		
		List<Role> pfaUser = XPersistence.getManager().createQuery(roleQuery).getResultList();
		if(pfaUser==null || pfaUser.isEmpty()){
			Role pfaAdmin = new Role();
			pfaAdmin.setName(roleName);  
			//XPersistence.getManager().merge(billerAdmin);
			List<Module> biller = XPersistence.getManager().createQuery(moduleQuery).getResultList();
			pfaAdmin.setModules(biller);
			pfaAdmin = XPersistence.getManager().merge(pfaAdmin);
			pfaUser.add(pfaAdmin);
		}
		String password = "password";//RandomStringUtils.randomAlphanumeric(15); 
		user.setPassword(password);
		user.setRoles(pfaUser);		
		XPersistence.getManager().merge(user);
		SystemWideSetup.sendNotification(user.getEmail(), "Notification of User Credentials ", 
				" Your Username is "+
						name + " and the password is "+ 
						password + " Please ensure to change your password during first login ",
						" Your Username is "+
								name + " and the password is "+ 
								password + " Please ensure to change your password during first login ",
								user.getPhoneNumber());	
		
		System.out.println(" After Users email is =====" + user.getEmail()); 
		
	} 

}
