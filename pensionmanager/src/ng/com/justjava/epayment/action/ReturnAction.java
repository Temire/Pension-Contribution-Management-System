package ng.com.justjava.epayment.action;

import org.openxava.actions.*;
import org.openxava.view.*;


public class ReturnAction extends ViewBaseAction {

	
	
	public void execute() throws Exception {
		View view = (View) getRequest().getSession().getAttribute("view");
		
		System.out.println(" The view name here=====================" + view.getViewName());
		setView(view);
	}


	public boolean hasReinitNextModule() { 
		return false;
	}


/*	public String getNextModule() {
		// TODO Auto-generated method stub
		return "ApproveTransaction";
	}*/

}
