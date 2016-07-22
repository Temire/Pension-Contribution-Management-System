package ng.com.justjava.epayment.action;

import org.openxava.actions.*;

public class CloseAction extends ViewBaseAction {

	
	
	public void execute() throws Exception {
		closeDialog();
		//setNextMode(LIST);
	}


	public boolean hasReinitNextModule() { 
		return false;
	}


/*	public String getNextModule() {
		// TODO Auto-generated method stub
		return "ApproveTransaction";
	}*/

}
