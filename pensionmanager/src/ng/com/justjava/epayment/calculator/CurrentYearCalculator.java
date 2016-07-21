package ng.com.justjava.epayment.calculator;

import ng.com.justjava.epayment.model.*;

import org.openxava.calculators.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

public class CurrentYearCalculator implements ICalculator {

	public Object calculate() throws Exception {
		// TODO Auto-generated method stub
		PeriodYear year = null;
		
		System.out.println(" OpenXava Supplied Date==="+Dates.getYear(Dates.createCurrent()));
		if(true)
			return Dates.getYear(Dates.createCurrent());
		
		try {
			year = (PeriodYear) XPersistence.getManager().
					createQuery("FROM PeriodYear p WHERE p.year="+Dates.getYear(Dates.createCurrent()))
					.getSingleResult();
			
			System.out.println( " The Year Retrieved Here ===="+year + " Exact=="+(year!=null?year.getYear():0));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return year;
	}

}
