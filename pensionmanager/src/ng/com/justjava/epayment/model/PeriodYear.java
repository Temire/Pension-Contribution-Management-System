package ng.com.justjava.epayment.model;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;

@Entity
public class PeriodYear {
	@Id
	private int year;


	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public static PeriodYear findPeriodYearByYear(int year){
		return XPersistence.getManager().find(PeriodYear.class, year);
	}
}
