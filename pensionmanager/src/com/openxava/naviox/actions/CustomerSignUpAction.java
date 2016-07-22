package com.openxava.naviox.actions;

import org.openxava.actions.*;

public class CustomerSignUpAction extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		showDialog();
		getView().setModelName("Customer"); // 2
		//addActions("Customer.save","Customer.makePayment","Customer.cancel");
		addActions("Customer.save","Customer.cancel");
	}

}
 