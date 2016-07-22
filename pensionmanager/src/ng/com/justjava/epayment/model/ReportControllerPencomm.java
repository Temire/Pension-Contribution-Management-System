package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.action.*;
import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.model.RemitPension.*;
import ng.com.justjava.epayment.model.ReportControllerPencomm.Months;

import org.openxava.annotations.*;

@View(members ="report;format;uploadYear;month;custodian;fundAdministrator;corporate")
public class ReportControllerPencomm {

	public enum Format {
		PDF, Excel, RTF
	};
    
	public enum Report {
		RSAHolders
	};
	
	public enum Months { 
		All,January,February,March,April,May,June,July,August,September,October,November,December;
	   
	   };
	
	@Required
	private Format format;

	@Required
	private Report report;
	
	@Required
	@DescriptionsList(descriptionProperties="year")
	@NoCreate
	@NoModify
	@ManyToOne
	private PeriodYear uploadYear;
	
	@Required
	private Months month;
	
	@Required
	@DescriptionsList(descriptionProperties="name")
	@NoCreate
	@NoModify
	@ManyToOne
	private PensionFundCustodian custodian;
	
	@Required
	@DescriptionsList(depends="custodian",condition="${custodian.id} = ?",descriptionProperties="name")
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

	public PensionFundCustodian getCustodian() {
		return custodian;
	}

	public void setCustodian(PensionFundCustodian custodian) {
		this.custodian = custodian;
	}

	public Months getMonth() {
		return month;
	}

	public void setMonth(Months month) {
		this.month = month;
	}

	public PeriodYear getUploadYear() {
		return uploadYear;
	}

	public void setUploadYear(PeriodYear uploadYear) {
		this.uploadYear = uploadYear;
	}

	
	
	
}
