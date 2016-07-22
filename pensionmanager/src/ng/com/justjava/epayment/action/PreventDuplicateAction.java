package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.Approver.ApprovableTransaction;
import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;

public class PreventDuplicateAction  extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		if(corporate==null){
			return;
		}
		ApprovableTransaction trans = (ApprovableTransaction) getView().getValue("transaction");
		
		if(trans == ApprovableTransaction.MonthlyRemittance){
			addMessage("The Last "+trans + " Approval Level Is "+corporate.getHighestApprovalLevel(), null);
		}
		
		Integer level = getView().getValueInt("level");
		if(trans== null || level == null)
			return;
		
		String sql = " FROM Profile p WHERE (p.transaction="+trans.ordinal() + " OR p.transaction=3) "
				+ "AND p.level="+level + " AND p.corporate.id="+corporate.getId();
		List profiles= XPersistence.getManager().createQuery(sql).getResultList();
		if(profiles!=null && !profiles.isEmpty()){
			addError("Profile for "+trans.name() + " At Level "+level +" Already Defined", null);
			getView().reset();
			return;
		}
		
	}

}