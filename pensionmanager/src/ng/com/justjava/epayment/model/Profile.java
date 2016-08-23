package ng.com.justjava.epayment.model;

import javax.persistence.*;

import ng.com.justjava.epayment.action.*;
import ng.com.justjava.epayment.model.Approver.ApprovableTransaction;
import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.codehaus.groovy.tools.shell.commands.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;

import com.openxava.naviox.model.*;

@Entity
@View(members="approver;level;transaction;universal;Role [role]") 
@Tab(filter=LoginUserCorporateFilter.class, baseCondition="(${corporate.id} IN ? AND ${deleted}=0) OR (${corporate} IS NULL AND ${deleted}=0)",properties=
"role.name,corporate.name,approver,transaction,universal") 

public class Profile {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	@OnChange(SetApprovalOnProfileAction.class)
	private boolean approver;
	
	@OnChange(PreventDuplicateAction.class)
	private ApprovableTransaction transaction;
	
	private boolean universal;
	
	//@OnChange(PreventDuplicateAction.class)
	private int level;
	
	@ManyToOne 
	private Corporate corporate;

	@OneToOne(cascade=CascadeType.ALL)
	@AsEmbedded
	@Required
	@NoFrame
	private Role role;
	
	

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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public ApprovableTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(ApprovableTransaction transaction) {
		this.transaction = transaction;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isApprover() {
		return approver;
	}

	public void setApprover(boolean approver) {
		this.approver = approver;
	}	

	public void preCreate(){
		setCorporate(UserManager.getCorporateOfLoginUser());
	}

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}

	public boolean isUniversal() {
		return universal;
	}

	public void setUniversal(boolean universal) {
		this.universal = universal;
	}
}