package ng.com.justjava.epayment.model;

import javax.persistence.*;

import org.openxava.annotations.*;

public class FundTransfer {
	
	public enum Destination{
		toAccount,toCard
	}
	
	@Editor("ValidValuesRadioButton")
	@Required
	private Destination endoint;
	
	@Required
	@DisplaySize(25)
	private String cardId;
	
	@Required
	@Stereotype("PASSWORD")	
	@DisplaySize(4)
	private String pin;
	
	@Required
	@DisplaySize(25)
	@Action("FundTransfer.validate")
	private String accountNumber;
	
	@Required
	private double amount;
	
	@ManyToOne
	@DescriptionsList
	@NoCreate
	@NoModify
	@Required
	private Bank bank;
	
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Destination getEndoint() {
		return endoint;
	}
	public void setEndoint(Destination endoint) {
		this.endoint = endoint;
	}
}
