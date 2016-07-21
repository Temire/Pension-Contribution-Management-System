package ng.com.justjava.epayment.Nibss;

import javax.xml.bind.annotation.*;


@XmlType(propOrder={"vendorName", "accountName","accountNumber","sortCode","status","errorReason","hashValue"})
public class NibssVendor {
	private String vendorName;
	private String accountName;
	private String accountNumber;
	private String sortCode;
	private String status;
	private String errorReason;
	private String hashValue;
	
	
	@XmlElement(name="VendorName")
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
	@XmlElement(name="AccountName")
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	@XmlElement(name="AccountNumber")
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	@XmlElement(name="SortCode")
	public String getSortCode() {
		return sortCode;
	}
	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}
	
	@XmlElement(name="Status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@XmlElement(name="ErrorReason")
	public String getErrorReason() {
		return errorReason;
	}
	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}
	
	@XmlElement(name="HashValue")
	public String getHashValue() {
		return hashValue;
	}
	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}
}
