package ng.com.justjava.epayment.model;

import java.math.*;

import javax.persistence.*;

@Embeddable
public class CalculatorPayItems  {
	

	private String name;
	private BigDecimal amount;
	private boolean active;
	


	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
