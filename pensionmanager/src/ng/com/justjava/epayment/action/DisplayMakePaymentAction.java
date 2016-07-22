package ng.com.justjava.epayment.action;

import org.openxava.actions.*;

public class DisplayMakePaymentAction extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		showDialog();
		getView().setModelName("MakePayment");
	}

}
