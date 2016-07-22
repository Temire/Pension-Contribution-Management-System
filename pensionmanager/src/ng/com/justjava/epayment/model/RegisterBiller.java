package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.annotations.*;
import org.openxava.util.*;

@Entity
@View(members="biller")
@Tab(properties="biller.name,registeredDate,registerBy",baseCondition = " ${deleted}=0")
public class RegisterBiller {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;

	@OneToOne
	@AsEmbedded
	private Biller biller;
	
	private Date registeredDate;
	private String registerBy;
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;
	
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public String getRegisterBy() {
		return registerBy;
	}

	public void setRegisterBy(String registerBy) {
		this.registerBy = registerBy;
	}
	
	@PreCreate
	public void preCreate(){
		setRegisterBy(Users.getCurrent());
		setRegisteredDate(Dates.createCurrent());
	}

	public Biller getBiller() {
		return biller;
	}

	public void setBiller(Biller biller) {
		this.biller = biller;
	}
}
