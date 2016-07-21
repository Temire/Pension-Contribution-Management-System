package ng.com.justjava.epayment.model;

import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;

public class RemitPersonalContribution {
	private BigDecimal amount;
	
	@ManyToOne
	@AsEmbedded
	@NoFrame
	private Account account;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
}
