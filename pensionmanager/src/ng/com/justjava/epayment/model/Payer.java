package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
public class Payer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	private String name;
	
	@OneToMany
	private Collection<Account> payingAccounts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Collection<Account> getPayingAccounts() {
		return payingAccounts;
	}

	public void setPayingAccounts(Collection<Account> payingAccounts) {
		this.payingAccounts = payingAccounts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
