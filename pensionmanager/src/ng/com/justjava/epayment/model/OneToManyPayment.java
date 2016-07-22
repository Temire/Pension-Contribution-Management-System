package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
public class OneToManyPayment extends Payment{
	
	@ManyToOne
	private Account account;
	
	@OneToMany
	private Collection<PaymentInstruction> payments;




	public Collection<PaymentInstruction> getPayments() {
		return payments;
	}

	public void setPayments(Collection<PaymentInstruction> payments) {
		this.payments = payments;
	}
	
	@PreCreate
	public void setfromAcount(){
		for(PaymentInstruction instruction:payments){
			instruction.setFromAccount(account);
			instruction.setNarration(getNarration());
			instruction.setStatus(Status.New);
		}
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
}
