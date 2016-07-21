package ng.com.justjava.epayment.model;

import javax.persistence.ManyToOne;

import org.openxava.annotations.*;


@View(members ="report;format;month")
public class ReportControllerPaymentLog {

	public enum Format {
		PDF, Excel, RTF
	};
    
	public enum Report {
		PaymentLog
	};
	
//	public enum Months { 
//		All,January,February,March,April,May,June,July,August,September,October,November,December;
//	   
//	   };
	
	@Required
	private Format format;

	@Required
	private Report report;
	
/*	@Required*/
//	private Months month;

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



//	public Months getMonth() {
//		return month;
//	}
//
//
//
//	public void setMonth(Months month) {
//		this.month = month;
//	}



	public Corporate getCorporate() {
		return corporate;
	}



	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}



	public PeriodYear getYear() {
		return year;
	}



	public void setYear(PeriodYear year) {
		this.year = year;
	}	
}