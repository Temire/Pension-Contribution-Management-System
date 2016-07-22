package ng.com.justjava.epayment.model;

import java.math.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import ng.com.justjava.filter.*;

import org.openxava.annotations.*;

@Entity
@View(members="name;identifier;expectedAmount")
@XmlAccessorType(XmlAccessType.NONE)
/*@Tabs({@Tab(filter=LoginUserCorporateFilter.class,baseCondition = "${enable}=1 AND ${corporate.id}=?"),
	@Tab(name="allUsers",properties="user.givenName,user.familyName,corporate.name")})*/

//@Tab(filter=CollectionItemFilter.class,baseCondition = "${name} IN ?")
public class CollectionItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	
	private String name;

	@ManyToOne
	@Embedded
	private Account account;
	
	private BigDecimal expectedAmount;
	
	@ManyToOne
	private Biller biller;
	
	
	
	@ManyToOne
	@AsEmbedded
	private CustomerIdentifier identifier;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name = "ExpectedAmount")
	public BigDecimal getExpectedAmount() {
		return expectedAmount;
	}

	public void setExpectedAmount(BigDecimal expectedAmount) {
		this.expectedAmount = expectedAmount;
	}

	@XmlElement(name = "CustomerIdentifier")
	public CustomerIdentifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(CustomerIdentifier identifier) {
		this.identifier = identifier;
	}

	public Biller getBiller() {
		return biller;
	}

	public void setBiller(Biller biller) {
		this.biller = biller;
	}

	@XmlElement(name = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "Account")
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}
