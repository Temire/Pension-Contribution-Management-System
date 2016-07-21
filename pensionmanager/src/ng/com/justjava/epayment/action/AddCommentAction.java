package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

public class AddCommentAction extends ViewBaseAction{

	public void execute() throws Exception {
		String comment = getView().getValueString("makeComment");
		
		LodgeComplaint complain = (LodgeComplaint) getView().getEntity();
		RSAHolder holder = complain.getComplainant();
		
		System.out.println(" The Complaint Here entered on ==============" + complain.getDateLodge());
		User user = User.find(Users.getCurrent());
		comment = comment + "("+user.getGivenName() + " " + user.getFamilyName()+"---" + Dates.createCurrent()+")";
		
		String complaint = getView().getValueString("complaint");
		complaint = (complaint==null?"":complaint+"\n Comments\n======================"
				+ "================================\n") +comment;
		String content = holder.getFirstName() + " " +holder.getSecondName() + ", The Complain You Lodge Has Been Commented " +
				"On By " + user.getGivenName() + " " + user.getFamilyName();
		SystemWideSetup.sendNotification(holder.getEmail(), 
				"Lodge Complain", content, content, holder.getPhoneNumber());
		
		getView().setValue("complaint", complaint);
		getView().setValue("makeComment", "");
	}

}
