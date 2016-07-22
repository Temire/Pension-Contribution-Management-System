package ng.com.justjava.epayment.model;

import java.io.*;
import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;

//@Embeddable
@Entity
@View(members="payItem;amount")
public class RSAPayItem  implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	@ManyToOne
	private RSAHolder rSAHolder;
	
	private BigDecimal originalAmount;

	@OneToOne
	@DescriptionsList(descriptionProperties="payItem.name")
	@NoCreate @NoModify
	private CompanyPayitem payItem;
	
	@Transient
	public String getName(){
		return (payItem!=null? payItem.getPayItem().getName():"");
	}
	
	private BigDecimal amount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public RSAHolder getrSAHolder() {
		return rSAHolder;
	}

	public void setrSAHolder(RSAHolder rSAHolder) {
		this.rSAHolder = rSAHolder;
	}

	public CompanyPayitem getPayItem() {
		return payItem;
	}

	public void setPayItem(CompanyPayitem payItem) {
		this.payItem = payItem;
	}
	
	@PreUpdate
	public void recalculateAmount(){
		//getrSAHolder().setCummulatedAmount(getrSAHolder().getContributionToDate());
		RSAHolder holder = getrSAHolder();
		int companyBase = holder.getCorporate().getPeriodReach();
		int holderBase = holder.getBase();
		if(companyBase != holderBase){
			holder.setCummulatedAmount(getrSAHolder().getFromDateJoinedToDate());
			holder.setBase(companyBase);
			XPersistence.getManager().merge(holder);
		
		}
	}
	
	public BigDecimal getOriginalAmount(){
		return originalAmount;
	}
	
	@PostLoad
	public void loadOriginalAmount(){
		originalAmount = getAmount();
	}	
}
