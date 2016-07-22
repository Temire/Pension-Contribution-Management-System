package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

import com.openxava.naviox.model.*;

public class InitializeTransactionAction extends TabBaseAction{

	public void execute() throws Exception {
		CorporateUser loginUser = UserManager.getLoginCorporateUser();
		
		String condition = "";

		
		if(loginUser != null){
			System.out.println(" The previous condition is ==" + getTab().getBaseCondition() + 
					"  and the name==" + getTab().getTabName());
			String inRole = "(";
			ArrayList<String> roleNames = new ArrayList<String>();
				Collection<Role> roles =  loginUser.getUser().getRoles();
				int count = roles.size();
				for (Role role : roles) {
					inRole = inRole + "'"+role.getName()+"'" + (count>1?",":"");
					count = count - 1;
				}
				inRole = inRole + ")";
			//${approvedBy} IN ? AND ${transRef}=1
			condition = "${approvedBy} IN " + inRole + " AND ${transRef}=1 AND ${corporate.id}="+loginUser.getCorporate().getId();
			getTab().setBaseCondition(condition);
			System.out.println(" The new condition is ==" + getTab().getBaseCondition());
		}
		
		

		
	}

}
