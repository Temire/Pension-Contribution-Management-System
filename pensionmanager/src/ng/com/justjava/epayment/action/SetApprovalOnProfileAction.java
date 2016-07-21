package ng.com.justjava.epayment.action;

import org.openxava.actions.*;

public class SetApprovalOnProfileAction extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub

		boolean approver = false;
		if(getView() != null && getView().getAllValues() !=null)
				approver = Boolean.parseBoolean(String.valueOf(getView().getAllValues().get("approver")));
				
				
		if(approver){
			getView().setHidden("level", false);
			getView().setHidden("transaction", false);
		} else {
			getView().setHidden("level", true);
			getView().setHidden("transaction", true);
		}
		
	}

}
