package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.model.ReportController.*;

import org.openxava.annotations.*;

@View(members ="report;format;year;month;fundAdministrator;corporate")

public class ReportControllerPFC {

	public enum Format {
		PDF, Excel, RTF
	};
    
	public enum Report {
		RSAHolders
	};
	
	@Required
	@DescriptionsList(descriptionProperties="year")
	@NoCreate
	@NoModify
	@ManyToOne
	private PeriodYear year;
	
	public enum Months { 
		All,January,February,March,April,May,June,July,August,September,October,November,December;
	   
	   };
	
	   
	public PeriodYear getYear() {
		return year;
	}

	public void setYear(PeriodYear year) {
		this.year = year;
	}

	@Required
	private Format format;

	@Required
	private Report report;
	
	@Required
	private Months month;
	
	@Required
	@DescriptionsList(descriptionProperties="name")
	@NoCreate
	@NoModify
	@ManyToOne
	private PensionFundAdministrator fundAdministrator;
	
	//depends="fundAdministrator",condition="${fundAdministrator.} = ?",
	@Required
	@DescriptionsList(descriptionProperties="name")
	@NoCreate
	@NoModify
	@ManyToOne
	private Corporate corporate;
	
	private Date from;
	
	private Date to;
	
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

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}


	public PensionFundAdministrator getFundAdministrator() {
		return fundAdministrator;
	}

	public void setFundAdministrator(PensionFundAdministrator fundAdministrator) {
		this.fundAdministrator = fundAdministrator;
	}

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}

	public Months getMonth() {
		return month;
	}

	public void setMonth(Months month) {
		this.month = month;
	}

	
}