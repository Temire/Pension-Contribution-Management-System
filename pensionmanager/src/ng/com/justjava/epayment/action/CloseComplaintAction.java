package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.LodgeComplaint.Status;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;


public class CloseComplaintAction  extends ViewBaseAction{
 
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map key = getView().getKeyValues();
		LodgeComplaint complaint = (LodgeComplaint) MapFacade.findEntity("LodgeComplaint", key);
		
		complaint.setStatus(Status.close);
		
		complaint.setComplaint(getView().getValueString("complaint"));
		complaint.setDateAttendedTo(Dates.createCurrent());
		XPersistence.getManager().merge(complaint);
		RSAHolder holder = complaint.getComplainant();
		
		User user = User.find(Users.getCurrent());
		
		String content = holder.getFirstName() + " " +holder.getSecondName() + ", The Complain You Lodge Has Been Closed " +
				"By " + user.getGivenName() + " " + user.getFamilyName();
		SystemWideSetup.sendNotification(holder.getEmail(), 
				"Lodge Complain", content, content, holder.getPhoneNumber());
		addMessage("Complaint Successfully Closed ! ", null);
    	getView().refresh();
	}

}
