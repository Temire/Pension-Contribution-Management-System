package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;
//ng.com.justjava.epayment.action.SavePayBillAction

public class SavePayBillAction extends SaveAction{
	public void execute() throws Exception {
		
		//SID;name;email;phoneNumber;
		Map allValues = getView().getAllValues();
		
		String phoneNumber = getView().getValueString("phoneNumber");
		String name = getView().getValueString("name");
		String email = getView().getValueString("email");
		String SID = getView().getValueString("SID");
		if(Is.emptyString(phoneNumber,name,email)){
			addError("Phone Number, Name And EMail Are All Required", null);
			return;
		}
		allValues.put("madeBy", email);
		allValues.put("madeFor", email);
		String password = RandomStringUtils.randomAlphanumeric(15);
		User user = new User();
		Customer customer = Customer.loadCustomer(email);
		if(customer == null){
			customer = new Customer();
			user.setName(email);
			String[] names = Strings.toArray(name, " ");
			
			System.out.println(" The length ==" + names.length + " first "+ names[0] 
					+ "  second==" + names[1]);
			if(names.length >= 1){
				user.setGivenName(names[0]);
				user.setFamilyName(names[1]);
			}else{
				user.setGivenName(name);
				user.setFamilyName(name);
			}
			user.setPhoneNumber(phoneNumber);
			user.setEmail(email);
			user.setPassword(password);
			
			
			List<Role> billerCustomer = XPersistence.getManager().createQuery("FROM Role r where r.name='customer'").getResultList();
			if(billerCustomer==null || billerCustomer.isEmpty()){
				Role customerRole = new Role();
				customerRole.setName("customer");  
				//XPersistence.getManager().merge(billerAdmin);
				List<Module> makePayment = XPersistence.getManager().createQuery("FROM Module m "
						+ "WHERE m.name='MakePayment'").getResultList();
				customerRole.setModules(makePayment);
				customerRole = XPersistence.getManager().merge(customerRole);
				billerCustomer.add(customerRole);
			}
			
			
			
			
			
			user.setRoles(billerCustomer);
			user = XPersistence.getManager().merge(user);
			
			customer.setUser(user);
			XPersistence.getManager().merge(customer);	
			
			SystemWideSetup.sendMail(user.getEmail(), "Notification of User Credentials ",
					" Your Username is "+
					email + " and the password is "+ 
					password);	
			
		}

		MapFacade.create("MakePayment", allValues);
		addMessage("Bill Payment Saved And Your Login Credential Sent To Your Mail", null);
		addMessage("You can Login with Sent Credential to View Your Previous Payment History", null);
		//customer.setCustomerId(customerId);
	
		
		
		
	}
}
