package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.MonthlyUpload.Status;
import ng.com.justjava.epayment.model.PFATransfer.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class ApproveMonthlyUpload  extends TabBaseAction{

	private boolean reject = false;
	
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map keys = getView().getKeyValues();

		Corporate corporate = UserManager.getCorporateOfLoginUser();
		System.out.println("0  Reaching line ");
		Map payingAccountId = (Map) getView().getValue("payingAccount");
		System.out.println("1  Reaching line "+payingAccountId);
		TransitAccount account =  null ;
		if(!Is.empty(payingAccountId))
			account = (TransitAccount) MapFacade.findEntity("TransitAccount", payingAccountId);
		else{
			addError("You Need To Pick Funding Account", null);
			return;
		}
		if(corporate==null)
			return;
		MonthlyUpload upload = (MonthlyUpload) MapFacade.findEntity(
				"MonthlyUpload", keys);
		 
		if(account != null){
			upload.setPayingAccount(account);
			System.out.println(" The Transit account picked =="+ account.getDisplay());
		}
		System.out.println("4  Reaching line ");		
		if (isReject()) {
			upload.setStatus(Status.reject);
			upload.setLevelReached(-1);
			XPersistence.getManager().merge(upload);
			addMessage("Transaction Rejected", null);
	    	getTab().deselectAll();
	    	getView().refresh();
			setNextMode(LIST);
			return;
		}
		if (corporate.getHighestApprovalLevel() > upload.getLevelReached()) {
			upload.setLevelReached(upload.getLevelReached() + 1);
		} else {
			upload.setStatus(Status.approve);
			//upload.setPayingAccount(account);
			upload.setLevelReached(-1);
		}
		upload = XPersistence.getManager().merge(upload);
		
		upload.sendNotification();
		
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
