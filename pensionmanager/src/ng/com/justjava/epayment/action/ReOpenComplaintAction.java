package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.LodgeComplaint.*;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class ReOpenComplaintAction   extends ViewBaseAction{
	 
		public void execute() throws Exception {
			// TODO Auto-generated method stub
			Map key = getView().getKeyValues();
			LodgeComplaint complaint = (LodgeComplaint) MapFacade.findEntity("LodgeComplaint", key);
			Map values = new HashMap();
			
			Status status = complaint.getStatus();
			
			System.out.println(" The status here ====" + complaint.getStatus());
			
			if(status.equals(Status.open)){
				addWarning("Complaint is Still Open", null);
				return;
			}
			values.put("status", Status.open);
			values.put("complaint", getView().getValueString("complaint"));
			values.put("dateAttendedTo", Dates.createCurrent());
			
			MapFacade.setValues("LodgeComplaint", key, values);
			addMessage("Complaint Successfully Re Opened ! ", null);
	    	getView().refresh();
		}

	}