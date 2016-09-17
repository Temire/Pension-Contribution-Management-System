package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.MonthlyUpload.Status;
import ng.com.justjava.epayment.model.RemitPension.Months;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class SendMonthlyUploadForApprovalAction extends ViewBaseAction{

	public void execute() throws Exception {
		System.out.println(" The id of this entity here is this =="+
					((MonthlyUpload)getView().getEntity()).getId()+
					" all values =="+getView().getAllValues());
		
		
		Map uploadYear = (Map) getView().getValue("uploadYear");
		if(uploadYear.isEmpty()){
			addError("Year is Required ", null);
			return;
		}
		Months month = (Months) getView().getValue("month");
		Boolean monthlyFigure = (Boolean) getView().getValue("monthlyFigure");
		System.out.println(" upload Year = "+uploadYear);
		
		PeriodYear periodYear=(PeriodYear) MapFacade.findEntity("PeriodYear", uploadYear) ;
		
		
		//Map year = (Map) getView().getValue("month");
		
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		if(corporate==null)
			return;
		
		if(corporate.getHighestApprovalLevel() <= 0){
			addError("Approval Route Not Yet Setup", null);
			return;
		}
		
		if(corporate.getTransitAccounts() ==null ||  corporate.getTransitAccounts().size()<= 0){
			addError("Paying Account Not Yet Setup", null);
			return;
		}		
		System.out.println(" The Highest approval level here ===" + corporate.getHighestApprovalLevel());
		String sql = " FROM MonthlyUpload m WHERE m.month="+month.ordinal() +
				" AND m.corporate.id="+corporate.getId() +" AND m.status=0 AND m.deleted=0";
		
		Collection<MonthlyUpload> uploads = null;
		try {
			uploads = XPersistence.getManager().createQuery(sql).getResultList();
			for (MonthlyUpload monthlyUpload : uploads) {
				monthlyUpload.setStatus(Status.awaitingApproval);
				//monthlyUpload.setDateEntered(Dates.createCurrent());
				monthlyUpload.setMonthlyFigure(monthlyFigure);
				monthlyUpload.setUploadYear(periodYear);
				monthlyUpload.setLevelReached(monthlyUpload.getLevelReached()+1);
				monthlyUpload = XPersistence.getManager().merge(monthlyUpload);
				monthlyUpload.sendNotification();

			}
			XPersistence.commit();
			addMessage((!uploads.isEmpty()?String.valueOf(uploads.size()):" ")+"  " + month + " Upload Sent For Approval", null);
			getView().refresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		
	}
	
}
