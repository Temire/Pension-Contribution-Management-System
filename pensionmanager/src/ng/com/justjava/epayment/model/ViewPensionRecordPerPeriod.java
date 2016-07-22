package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import ng.com.justjava.epayment.action.*;
import ng.com.justjava.epayment.model.RemitPension.Months;

public class ViewPensionRecordPerPeriod {

	@ManyToOne
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties="year")
	private PeriodYear year;
	
	@OnChange(OnChangeViewPeriodAction.class)
	private Months month;
	
	
	@OneToMany
	@ListProperties("firstName,secondName,pensionAmount")
	@Condition("${id} IS NULL")
	@ReadOnly
	private Collection<RSAHolder> holders;
	

	public Months getMonth() {
		return month;
	}

	public void setMonth(Months month) {
		this.month = month;
	}

	public Collection<RSAHolder> getHolders() {
		return holders;
	}

	public void setHolders(Collection<RSAHolder> holders) {
		this.holders = holders;
	}

	public PeriodYear getYear() {
		return year;
	}

	public void setYear(PeriodYear year) {
		this.year = year;
	}
	
}
