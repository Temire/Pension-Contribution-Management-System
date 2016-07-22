package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.MonthlyUpload.Status;
import ng.com.justjava.epayment.model.PFATransfer.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

public class ApproveMonthlyUpload  extends TabBaseAction{

	private boolean reject = false;
	
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map keys = getView().getKeyValues();

		Corporate corporate = UserManager.getCorporateOfLoginUser();
		
		Map payingAccountId = (Map) getView().getValue("payingAccount");
		TransitAccount account = (TransitAccount) MapFacade.findEntity("TransitAccount", payingAccountId);
		

		if(corporate==null)
			return;
		

		MonthlyUpload upload = (MonthlyUpload) MapFacade.findEntity(
				"MonthlyUpload", keys);
		
		if(account != null){
			upload.setPayingAccount(account);
			System.out.println(" The Transit account picked =="+ account.getDisplay());
		}
		
		if (isReject()) {
			upload.setStatus(Status.reject);
			upload.setLevelReached(-1);
			XPersistence.getManager().merge(upload);
			addMessage("Transaction Rejected", null);
			return;
		}
		if (corporate.getHighestApprovalLevel() > upload.getLevelReached()) {
			upload.setLevelReached(upload.getLevelReached() + 1);
		} else {
			upload.setStatus(Status.approve);
			upload.setPayingAccount(account);
			upload.setLevelReached(-1);
		}
		XPersistence.getManager().merge(upload);

    	getTab().deselectAll();
    	getView().refresh();
		addMessage("Transaction Successfully Approve", null);
		setNextMode(LIST);

	}

	public boolean isReject() {
		return reject;
	}

	public void setReject(boolean reject) {
		this.reject = reject;
	}
	
}
