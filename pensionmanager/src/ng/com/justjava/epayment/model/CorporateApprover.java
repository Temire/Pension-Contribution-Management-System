package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.model.Approver.ApprovableTransaction;

import org.openxava.annotations.*;
import org.openxava.jpa.*;

import com.openxava.naviox.model.*;

@Entity
@View(members="transaction;level;approver")
public class CorporateApprover {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	@Required
	private ApprovableTransaction transaction;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Required
	private int level;

	@ManyToOne
	private Corporate corporate;
	
	@ManyToOne
	//@JoinColumn(insertable=false,updatable=false)
	@NoCreate
	@NoModify
	//@ReferenceView("approver")
	private  Approver approver;


	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@PostCreate
	public void postCreate(){
/*		Role role;
		ArrayList<Role> roles = new ArrayList<Role>();
		try {
			role = XPersistence.getManager().find(Role.class, "approver");
			
			roles.add(role);
			getUser().setRoles(roles);
			XPersistence.getManager().merge(getUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/


	}

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}

	public Approver getApprover() {
		return approver;
	}

	public void setApprover(Approver approver) {
		this.approver = approver;
	}

	public ApprovableTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(ApprovableTransaction transaction) {
		this.transaction = transaction;
	}


}
