package ng.com.justjava.epayment.model;

import javax.persistence.*;

import ng.com.justjava.filter.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;

import com.openxava.naviox.model.*;

@Entity
//@View(members="name")
@Tab(filter=LoginUserCorporateFilter.class,baseCondition = "${corporate.id}=? AND ${deleted}=0")
public class Approver extends Approvable {
	
	public enum ApprovableTransaction{
		MonthlyRemittance,DebitAccount,UserManagement
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	@ManyToOne
	private Corporate corporate;
	
	private String name;

	
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
/*
	public User getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(User userProfile) {
		this.userProfile = userProfile;
	}*/
	
/*	@Transient
	public Corporate getCorporate(){
		CorporateUser corporateApprover =  null;
		try {
			corporateApprover = (CorporateUser)XPersistence.getManager()
												.createQuery(" from CorporateApprover c where c.approver.id="+getId())
												.getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Corporate corporate =(corporateApprover !=null?corporateApprover.getCorporate():null); 
		return corporate;
	}*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}


}
