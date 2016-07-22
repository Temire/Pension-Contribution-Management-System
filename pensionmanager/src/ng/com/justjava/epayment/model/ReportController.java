package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.model.RemitPension.*;

import org.openxava.annotations.*;

//@View(members ="report;format;custodian;fundAdministrator;corporate;from;to; month")
@View(members ="report;format;year;month;fundAdministrator")
public class ReportController {

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
	private Months month;
	
	@Required
	@DescriptionsList(descriptionProperties="name")
	@NoCreate
	@NoModify
	@ManyToOne
	private PensionFundCustodian custodian;
	
	@Required
	@DescriptionsList(descriptionProperties="name")
	@NoCreate
	@NoModify
	@ManyToOne
	private PensionFundAdministrator fundAdministrator;
	
	//depends="fundAdministrator",condition="${fundAdministrator.} = ?",
	
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

	

	

	

	public PeriodYear getYear() {
		return year;
	}

	public void setYear(PeriodYear year) {
		this.year = year;
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
	
	
}
