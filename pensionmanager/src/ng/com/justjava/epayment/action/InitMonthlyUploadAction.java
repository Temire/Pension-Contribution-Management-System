package ng.com.justjava.epayment.action;


import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.RemitPension.Months;
import ng.com.justjava.epayment.utility.*;

import org.apache.commons.collections.map.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

public class InitMonthlyUploadAction extends ViewBaseAction{

	public void execute() throws Exception {
		
		//PeriodYear year = new PeriodYear();
		//year.setYear(2016);
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		Map year = new HashedMap();

		
		
		//year = (Map)getView().getValue("year");
		//Long yearId = (Long) year.get("id");
		
		int dateYear = Dates.getYear(Dates.createCurrent());
		
		PeriodYear periodYear = PeriodYear.findPeriodYearByYear(dateYear);
		
		//PeriodYear periodYear = XPersistence.getManager().find(periodYear,periodYear);
		
		year.put("uploadYear", (periodYear!=null?periodYear.getYear():2016));
		
		System.out.println("monthly uplaod and dateyear is "+dateYear + " and corporate =="+corporate);

		Months result = (Months)XPersistence.getManager().
				createQuery(" SELECT MAX(m.month) FROM MonthlyUpload m where m.corporate.id="+corporate.getId()).getSingleResult();

		System.out.println(" The Result of the query==============" + result);
		
		Months latestMonth = (result==null?Months.January :result);
		
		addMessage(latestMonth +" "+periodYear.getYear()+ " Is The Most Recent Upload", null);
		getView().setValue("uploadYear", year);
		getView().setValue("month", (result==null?Months.January:result.getNext()));
		Months month = (Months) getView().getValue("month");
		//PeriodYear year1 = (PeriodYear) getView().getValue("year");
		
		getView().putObject("currentMonth", month);
		getView().putObject("uploadYear", year);

		String condition = "${upload.month}=" +(month!=null?month.ordinal():-1)
				+" AND ${corporate.id}="+(corporate!=null?corporate.getId():0) +" AND ${upload.status}=0";
		
		System.out.println(" The condition =="+condition);
		getView().getSubview("holders").getCollectionTab().setBaseCondition(condition);
		// TODO Auto-generated method stub
		
	}

}
