package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.RemitPension.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class OnChangeMonthlyView extends OnChangePropertyBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Months month = (Months) getView().getValue("month");
		Map uploadYearMap =(Map) getView().getValue("uploadYear");
		Months preferenceMonth = Months.January;
		preferenceMonth = (month==null?preferenceMonth.getMonth(Dates.getMonth(Dates.createCurrent())-1):month);
		
		Users.getCurrentPreferences().put("month", String.valueOf(preferenceMonth.ordinal()));

		getView().getSubview("administrators").refresh();		
	}
}
