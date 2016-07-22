package ng.com.justjava.epayment.model;

import java.io.*;
import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import org.openxava.annotations.*;

@Entity
@XmlAccessorType(XmlAccessType.NONE)

/*@TableGenerator(schema="chamspay", table = "ID_TABLE", name = "AccountOwnerDetailIdTable", 
allocationSize = 100000, initialValue = 0, pkColumnName = "pk", 
valueColumnName = "value", pkColumnValue = "AccountOwnerDetail")*/


@Inheritance(strategy = InheritanceType.JOINED)
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class AccountOwnerDetail implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	@Required
	private String name;
	
	@Required
	private String email;
	
	@Required
	@Column(unique=true)
	private String uniqueIdentifier;
	
	@OneToMany(mappedBy="owner")
	@AsEmbedded
	@CollectionView("embeded")
	@ListProperties("bank.name,number,name,balance")
	private Collection<Account> accounts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name = "Name")	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "UniqueIdentifier")	
	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	public Collection<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Collection<Account> accounts) {
		this.accounts = accounts;
	}


}
