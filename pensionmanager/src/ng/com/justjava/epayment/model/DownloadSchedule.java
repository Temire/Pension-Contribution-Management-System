package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.action.*;
import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.model.ReportController.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.annotations.*;

@View(members ="format;year;month;corporate")
//@View(name="rsaholder2",members ="report;format;year,frommonth;year,tomonth;corporate")
public class DownloadSchedule {
	
	PensionFundAdministrator pfa = UserManager.getPFAOfLoginUser();

	public enum Format {
		PDF, Excel, RTF
	};
    
	
	public enum Months { 
		January,February,March,April,May,June,July,August,September,October,November,December;
	   
	   };
	   
	
	@Required
	private Format format;

	@Required
	@OnChange(OnChangeReport.class)
	//@ReadOnly
	private Report report;
	
	
	@Required
	private Months month;
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
	private PeriodYear year;
	
	
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

	public PensionFundAdministrator getPfa() {
		return pfa;
	}

	public void setPfa(PensionFundAdministrator pfa) {
		this.pfa = pfa;
	}

	public Months getMonth() {
		return month;
	}

	public void setMonth(Months month) {
		this.month = month;
	}

	public PeriodYear getYear() {
		return year;
	}

	public void setYear(PeriodYear year) {
		this.year = year;
	}

	

	

	
}