package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.action.*;
import ng.com.justjava.epayment.model.RemitPension.*;

import org.openxava.annotations.*;

public class RSAHoldersViewByPFA {

	@OnChange(OnChangeViewPeriodAction.class)
	private Months month;
	
	
	@ManyToOne
	@DescriptionsList
	@NoModify
	@NoCreate
	@OnChange(OnChangeViewPeriodAction.class)
	private Corporate corporate;
	
	
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

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}
	
}
