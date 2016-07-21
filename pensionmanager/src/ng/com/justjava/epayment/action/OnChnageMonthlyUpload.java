package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.RemitPension.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

public class OnChnageMonthlyUpload extends OnChangePropertyBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Months month = (Months) getView().getValue("month");

		System.out.println(" The View name inside here==="+getView().getModelName()
				+ " the title root title ==="+getView().getRoot());
		
		Months currentMonth = (Months) getView().getObject("currentMonth");
		
/*		if((currentMonth!=Months.January) && (month.ordinal() > currentMonth.ordinal()) ){
			addError("Upload Month Cannot Be Jumped", null);
			addError(currentMonth + " Is Still Outstanding ", null);
			getView().setValue("month", currentMonth);
			return;
		}*/
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		String condition = "${upload.month}=" +(month!=null?month.ordinal():0)
				+" AND ${corporate.id}="+(corporate!=null?corporate.getId():0) +" AND ${upload.status}=0";
		//condition = "${corporate.id}=3";
		System.out.println(" The condition =="+condition);
		getView().getSubview("holders").getCollectionTab().setBaseCondition(condition);
		getView().getSubview("holders").refresh();
	}

}
