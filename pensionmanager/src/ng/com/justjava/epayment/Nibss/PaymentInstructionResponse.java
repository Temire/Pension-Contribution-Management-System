package ng.com.justjava.epayment.Nibss;

import javax.xml.bind.annotation.*;




@XmlType(propOrder={"beneficiary", "amount","accountNumber" ,"sortCode" ,"narration","status","errorReason"})
public class PaymentInstructionResponse {
	@XmlElement(name="Amount")
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	@XmlElement(name="Narration")
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
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
	@XmlElement(name="Beneficiary")
	public String getBeneficiary() {
		return beneficiary;
	}
	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}

	private  String beneficiary;
	private  String amount;	
	private  String accountNumber;
	private  String sortCode;
	private  String narration;
	private  String status;
	private  String errorReason;
	

}