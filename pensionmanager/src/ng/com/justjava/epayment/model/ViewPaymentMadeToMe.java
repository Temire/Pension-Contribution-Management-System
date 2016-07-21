package ng.com.justjava.epayment.model;

import javax.persistence.*;

import org.openxava.annotations.*;

public class ViewPaymentMadeToMe {
	private String name;
	
	@ManyToOne
	@DescriptionsList(depends="name", condition="${owner.id} = ?")
	private Account account;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	} 

}
