package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

public class SaveBillerAgentAction extends SaveElementInCollectionAction {
	 
		@Override 
		public void execute() throws Exception {
			// TODO Auto-generated method stub
			Map userValues = (Map) getCollectionElementView().getAllValues().get("user");
			String name = (String) userValues.get("name");
			
			if(!Is.emptyString(name) && Is.equalAsString(name, "admin")){
				addError("UserName admin is Reserved: Please use other name", null);
				return;
			}
			List<CollectionItem> selectedObjects = (List<CollectionItem>) getCollectionElementView().
					getSubview("attachCollectionItem").getCollectionSelectedObjects();

			super.execute();
			Map userKey = new HashMap();
			userKey.put("name", name);
			User user = (User) MapFacade.findEntity("User", userKey) ;
			
			List<Role> billerUser = XPersistence.getManager().createQuery("FROM Role r where r.name='agent'").getResultList();
			if(billerUser==null || billerUser.isEmpty()){
				Role billerAdmin = new Role();
				billerAdmin.setName("agent");  
				//XPersistence.getManager().merge(billerAdmin);
				List<Module> biller = XPersistence.getManager().createQuery("FROM Module m "
						+ "WHERE m.name='MakePayment'").getResultList();
				billerAdmin.setModules(biller);
				billerAdmin = XPersistence.getManager().merge(billerAdmin);
				billerUser.add(billerAdmin);
			}
			String password = RandomStringUtils.randomAlphanumeric(15); 
			user.setPassword(password);
			user.setRoles(billerUser);		
			XPersistence.getManager().merge(user);
			SystemWideSetup.sendMail(user.getEmail(), "Notification of User Credentials ", " Your Username is "+user.getName() + " and the password is "+ 
					password + " Please ensure to change your password during first login ");			
			System.out.println(" After Users email is =====" + user.getEmail()); 
			

			Agent agent = Agent.getAgentByUserName(name);
			if(agent == null)
				return;
			agent.addCollectionItem(selectedObjects);
			
		} 

	}