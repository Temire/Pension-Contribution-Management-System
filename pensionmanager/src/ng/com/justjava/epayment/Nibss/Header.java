package ng.com.justjava.epayment.Nibss;

import javax.xml.bind.annotation.*;

@XmlType(propOrder={"fileName", "scheduleId", "debitSortCode" ,"debitAccountNumber" ,"clientId","status"})
public class Header {
	private String fileName;
	private String scheduleId;
	private String debitSortCode;
	private String debitAccountNumber;
	private String clientId;

	
	private String status;

	@XmlElement(name="Status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@XmlElement(name="FileName")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@XmlElement(name="ScheduleId")
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	@XmlElement(name="DebitSortCode")
	public String getDebitSortCode() {
		return debitSortCode;
	}
	public void setDebitSortCode(String debitSortCode) {
		this.debitSortCode = debitSortCode;
	}
	
	@XmlElement(name="DebitAccountNumber")
	public String getDebitAccountNumber() {
		return debitAccountNumber;
	}
	public void setDebitAccountNumber(String debitAccountNumber) {
		this.debitAccountNumber = debitAccountNumber;
	}
	
	@XmlElement(name="ClientId")
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
