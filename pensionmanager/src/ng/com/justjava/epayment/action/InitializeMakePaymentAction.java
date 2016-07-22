package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

public class InitializeMakePaymentAction extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub

		getView().setHidden("identifier", true);
		getView().setHidden("value", true);
		getView().setHidden("means", true);
		getView().setHidden("meansValue", true);
		getView().setHidden("SID", true);
		getView().setHidden("bank", true);
		getView().setHidden("collectionBiller", true);
		
		if(!UserManager.loginUserIsAgent()){
			getView().setHidden("customerName", true);
			getView().setHidden("customerEmail", true);	
		}else{
			getView().setHidden("collectionBiller", false);
			getView().setHidden("biller", true);
			Agent agent = Agent.getAgentByUserName(Users.getCurrent());
			getView().setValue("collectionBiller", agent.getBiller().getName());
		}

		String ejbQL = " FROM User u WHERE u.name='"+Users.getCurrent()+"'";
		List<User> users = XPersistence.getManager().createQuery(ejbQL).getResultList();
		if(users !=null && users.size()>0){
			User user = users.get(0);
			getView().setValue("name", user.getGivenName() + " , " +user.getFamilyName());
			getView().setValue("phoneNumber", user.getPhoneNumber());
			getView().setValue("email", user.getEmail());
		}
		

	}

}
