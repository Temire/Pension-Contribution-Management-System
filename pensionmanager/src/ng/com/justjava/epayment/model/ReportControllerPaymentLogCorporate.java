package ng.com.justjava.epayment.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

import org.openxava.annotations.*;


@View(members ="report;format;fromDate;toDate;fundAdministrator")
public class ReportControllerPaymentLogCorporate {

	public enum Format {
		PDF, Excel, RTF
	};
    
	public enum Report {
		PaymentLog
	};
	
	
	@Required
	private Format format;

	@Required
	private Report report;
	

	@Required
	@DisplaySize(10)
	@Column(length=10)
	private Date fromDate;
	
	
	@Required
	@DisplaySize(10)
	@Column(length=10)
	private Date toDate;

	
	@DescriptionsList(descriptionProperties="name")
	@NoCreate
	@NoModify
	@ManyToOne
	private PensionFundAdministrator fundAdministrator;

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







	public PensionFundAdministrator getFundAdministrator() {
		return fundAdministrator;
	}



	public void setFundAdministrator(PensionFundAdministrator fundAdministrator) {
		this.fundAdministrator = fundAdministrator;
	}



	public Date getFromDate() {
		return fromDate;
	}



	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}



	public Date getToDate() {
		return toDate;
	}



	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}



}