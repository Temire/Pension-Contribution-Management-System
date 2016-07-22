package ng.com.justjava.epayment.model;

import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;

public class RemitContribution {
	private BigDecimal amount;
	
	@ManyToOne
	@DescriptionsList
	@NoCreate
	@NoModify
	private Bank bank;
	
	private String accountNumber;
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
}
