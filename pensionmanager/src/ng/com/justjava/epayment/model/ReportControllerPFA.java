package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.action.*;
import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.model.ReportController.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.annotations.*;

@View(members ="report;format;fromyear,from;toyear,to;corporate")
//@View(name="rsaholder2",members ="report;format;year,frommonth;year,tomonth;corporate")
public class ReportControllerPFA {
	
	PensionFundAdministrator pfa = UserManager.getPFAOfLoginUser();

	public enum Format {
		PDF, Excel, RTF
	};
    
	/*public enum Report {
		PFACompany,RSAHolders, PFAInstitutions
	};*/
	
	public enum Report {
		RSAHolders
		//Schedule
	};
	
	public enum Months { 
		January,February,March,April,May,June,July,August,September,October,November,December;
	   
	   };
	   
	@Required
	@DescriptionsList(descriptionProperties="year")
	@NoCreate
	@NoModify
	@ManyToOne
	private PeriodYear fromyear;
	
	@Required
	private Format format;

	@Required
	@OnChange(OnChangeReport.class)
	//@ReadOnly
	private Report report;
	
	@Required
	private Months to;
	
	@Required
	private Months from;
	//depends="fundAdministrator",condition="${fundAdministrator.} = ?",
	@Required
	@DescriptionsList(descriptionProperties="name")
	@NoCreate
	@NoModify
	@ManyToOne
	private Corporate corporate;
	
	@Required
	@DescriptionsList(descriptionProperties="year")
	@NoCreate
	@NoModify
	@ManyToOne
	private PeriodYear toyear;
	
	
	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	
	

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}

	

	


	public Months getTo() {
		return to;
	}

	public void setTo(Months to) {
		this.to = to;
	}

	public Months getFrom() {
		return from;
	}

	public void setFrom(Months from) {
		this.from = from;
	}

	public PensionFundAdministrator getPfa() {
		return pfa;
	}

	public void setPfa(PensionFundAdministrator pfa) {
		this.pfa = pfa;
	}

	public PeriodYear getFromyear() {
		return fromyear;
	}

	public void setFromyear(PeriodYear fromyear) {
		this.fromyear = fromyear;
	}

	public PeriodYear getToyear() {
		return toyear;
	}

	public void setToyear(PeriodYear toyear) {
		this.toyear = toyear;
	}

	

	
}