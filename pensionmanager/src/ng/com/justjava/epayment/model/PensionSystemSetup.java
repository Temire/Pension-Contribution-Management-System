package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@Embeddable
public class PensionSystemSetup {
	
	private double employeeContributionPercent;
	private double employerContributionPercent;	
	
	@OneToMany
	private Collection<PayItem> payItems;
	
	public Collection<PayItem> getPayItems() {
		return payItems;
	}
	public void setPayItems(Collection<PayItem> payItems) {
		this.payItems = payItems;
	}
	public double getEmployeeContributionPercent() {
		return employeeContributionPercent;
	}
	public void setEmployeeContributionPercent(double employeeContributionPercent) {
		this.employeeContributionPercent = employeeContributionPercent;
	}
	public double getEmployerContributionPercent() {
		return employerContributionPercent;
	}
	public void setEmployerContributionPercent(double employerContributionPercent) {
		this.employerContributionPercent = employerContributionPercent;
	}

}
