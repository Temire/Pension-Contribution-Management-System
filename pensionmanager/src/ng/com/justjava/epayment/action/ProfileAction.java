package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

public class ProfileAction extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
/*		String msg = UserManager.getLoginUserRoleForQuery("c.", "role.name");
		String msg2 = UserManager.getLoginUserRoleForQuery("", "${approvedBy}");
		addInfo(msg, null);
		addInfo(msg2, null);*/
		if(UserManager.getCorporateOfLoginUser() != null)
			getView().setHidden("universal", true);
		//getView().setHidden("transaction", true);
	}

}
