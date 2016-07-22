package ng.com.justjava.epayment.model;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.action.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;


@Entity
public class CompanyPayitem implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	@OnChange(ValidateCompulsoryItemAction.class)
	private boolean active;
	
	@OneToOne
	private PayItem payItem;
	
	@OneToOne
	private Corporate company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Corporate getCompany() {
		return company;
	}

	public void setCompany(Corporate company) {
		this.company = company;
	}

	public PayItem getPayItem() {
		return payItem;
	}

	public void setPayItem(PayItem payItem) {
		this.payItem = payItem;
	}
	
	public CompanyPayitem find(){
		
		CompanyPayitem companyPayitem = null;
		String  ejbQL = "FROM CompanyPayitem c WHERE c.payItem.code='"+ getPayItem().getCode() + "' "
				+ " AND c.company.id="+getCompany().getId();
		List<CompanyPayitem> companyPayItems = XPersistence.getManager().createQuery(ejbQL).getResultList();
		if(companyPayItems != null && companyPayItems.size()>0){
			companyPayitem = companyPayItems.get(0);
		}
		return companyPayitem;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
