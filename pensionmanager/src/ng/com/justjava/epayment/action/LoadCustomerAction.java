package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;

public class LoadCustomerAction extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		String mail = getView().getValueString("email");
		Customer customer = Customer.loadCustomer(mail);
		
		System.out.println(" The customer retrieved here is "+customer);
		if(customer!=null){
			System.out.println(" Full name " + customer.getUser().getGivenName() + " " + customer.getUser().getFamilyName());
			getView().setValue("name", customer.getUser().getGivenName() + " " + customer.getUser().getFamilyName());
			getView().setEditable("name", false);
			getView().setValue("phoneNumber", customer.getUser().getPhoneNumber());
			getView().setEditable("phoneNumber", false);
		}
	}

}
