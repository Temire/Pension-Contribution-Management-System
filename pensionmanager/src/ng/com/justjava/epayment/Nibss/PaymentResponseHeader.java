package ng.com.justjava.epayment.Nibss;

import javax.xml.bind.annotation.*;


@XmlType(propOrder={"scheduleId", "clientId","debitSortCode" ,"debitAccountNumber" ,"status"})
public class PaymentResponseHeader {
	private String scheduleId;
	private String clientId;
	private String debitSortCode;
	private String debitAccountNumber;
	private String status;
	
	
	@XmlElement(name="ScheduleId")
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	@XmlElement(name="ClientId")
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
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
	
	@XmlElement(name="Status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}