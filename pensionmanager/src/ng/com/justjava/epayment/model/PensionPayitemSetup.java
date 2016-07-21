package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.ws.soap.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;


@View(members="companyContribution;rsaHolderContribution;payItems")
public class PensionPayitemSetup {

	@Size(max=3,min=1)
	private double companyContribution;
	@Size(max=3,min=1)
	private double rsaHolderContribution;
	
	 
	
	@OneToOne
	//@AsEmbedded
	@Action("PensionPayitemSetup.add")
	private PayItem specificPayItem;
	
	@ListProperties("code,name")
	@Transient
	@Condition("${code}!=null")
	//@ReadOnly
	//@NewAction("PensionPayitemSetup.add")
	//@RemoveAction("PensionPayitemSetup.remove")
	public Collection<PayItem> getPayItems(){
		return null;
	}
	
	public double getCompanyContribution() {
		return companyContribution;
	}
	public void setCompanyContribution(double companyContribution) {
		this.companyContribution = companyContribution;
	}

	public double getRsaHolderContribution() {
		return rsaHolderContribution;
	}

	public void setRsaHolderContribution(double rsaHolderContribution) {
		this.rsaHolderContribution = rsaHolderContribution;
	}

	public PayItem getSpecificPayItem() {
		return specificPayItem;
	}

	public void setSpecificPayItem(PayItem specificPayItem) {
		this.specificPayItem = specificPayItem;
	}

}
