package ng.com.justjava.epayment.model;

import java.math.*;

import org.openxava.annotations.*;

public class PersonalPayment {
	
	@Action("PersonalPayment.pay")
	@Required
	private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


}
