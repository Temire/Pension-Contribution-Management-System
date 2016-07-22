package ng.com.justjava.epayment.model;

import java.lang.reflect.*;
import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.action.*;
import ng.com.justjava.epayment.model.Approver.ApprovableTransaction;
import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.apache.commons.beanutils.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.google.common.eventbus.*;
import com.openxava.naviox.model.*;
@Entity

@Tabs({@Tab(filter=UserRoleFilter.class,rowStyles=@RowStyle(style="row-green", property="finalApprover", value="Y"),
properties="description,enteredBy,dateEntered,levelReach,finalApprover",
baseCondition="${approvedBy} IN ?"),
@Tab(name="approvePayment", filter=UserRoleFilter.class,rowStyles=@RowStyle(style="row-green", property="finalApprover", value="Y"),
properties="description,enteredBy,dateEntered,levelReach,finalApprover",
baseCondition="${approvedBy} IN ? AND ${transRef}=1"),
@Tab(name="approveUser",filter=UserRoleFilter.class,rowStyles=@RowStyle(style="row-green", property="finalApprover", value="Y"),
properties="description,enteredBy,dateEntered,levelReach,finalApprover",
baseCondition="${approvedBy} IN ? AND ${transRef}=0"),
@Tab(name="approveDebitAccount", filter=UserRoleFilter.class,rowStyles=@RowStyle(style="row-green", property="finalApprover", value="Y"),
properties="description,enteredBy,dateEntered,levelReach,finalApprover",
baseCondition="${approvedBy} IN ? AND ${transRef}=2")})

@View(members="status,description,levelReach,transRef")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private long id;

	public enum Status{
		approved,rejected,awaitingApprover
	}
	
	private Status status;
	private String description;
	private String modelName;
	private Long modelId;
	private int levelReach;
	
	private String comment;
	
	@Required
	private ApprovableTransaction transRef;
	
	@ManyToOne
	private Corporate corporate;
	
	private String enteredBy;
	private Date dateEntered;
	private String approvedBy;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getEnteredBy() {
		return enteredBy;
	}

	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}

	public Date getDateEntered() {
		return dateEntered;
	}

	public void setDateEntered(Date dateEntered) {
		this.dateEntered = dateEntered;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public int getLevelReach() {
		return levelReach;
	}

	public void setLevelReach(int levelReach) {
		this.levelReach = levelReach;
	}

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}
	
	public void reject(String rejectReason){
		setStatus(Status.rejected);
		setApprovedBy("reject");
		XPersistence.getManager().merge(this);
		notifyRejection(getEnteredBy(),rejectReason);
		
	}
	
	
	@Subscribe
	public Transaction approve(Object obj){
		//boolean result = false;
		Profile profile = null;
		
		
		System.out.println(" Approval Routine Called Here and Here");
		//int approveableTrans=-1;
		try {
			profile = getNextApprover();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(" The error while retrieving approval===" + e.getMessage());
		}
		if(profile != null){
			String name = profile.getRole().getName();
			sendMail(name);
			this.setApprovedBy(name);
			
			this.setLevelReach(getLevelReach() + 1);
			this.setStatus(Status.awaitingApprover);
		}else{
			this.setApprovedBy(null);
			this.setStatus(Status.approved);
			
			try {
				try {
					MethodUtils.invokeMethod(XPersistence.getManager().find(Class.forName(this.getModelName()), 
							this.modelId), "approve", null);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ " saving the trans" + this.getApprovedBy());
		XPersistence.getManager().merge(this);
		XPersistence.commit();
		return this;
	}

	private void sendMail(String name) {
		// TODO Auto-generated method stub
		String ejbQL = " FROM User u WHERE '"+name+ "' MEMBER OF u.roles";
		
		
		System.out.println(" The query doing the job here==" + ejbQL);
		
		
		Collection<User> users = XPersistence.getManager().createQuery(ejbQL).getResultList();
		
		for (User user : users) { 
			SystemWideSetup.sendMail(user.getEmail(),"Awaiting Your Attention for Approval ", this.getTransRef().toString() + " Is Awaiting Your Approval " );
		}
		
	}
	private void notifyRejection(String name, String rejectReason) {
		// TODO Auto-generated method stub
		String ejbQL = " FROM User u WHERE u.name='" + name +"'";
		
		
		System.out.println(" The query doing the job here==" + ejbQL);
		String body = " Transaction " + getDescription() + " Has been rejected by " + Users.getCurrentUserInfo().getGivenName() + " Reason: " + rejectReason;
		
		Collection<User> users = XPersistence.getManager().createQuery(ejbQL).getResultList();
		
		for (User user : users) { 
			SystemWideSetup.sendMail(user.getEmail(),getDescription() + " Rejected ", body );
		}
		
	}
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ApprovableTransaction getTransRef() {
		return transRef;
	}

	public void setTransRef(ApprovableTransaction transRef) {
		this.transRef = transRef;
	}
	
	@Transient
	public String getFinalApprover(){
		return (getNextApprover()==null?"Y":"N");
	}
	
	@Transient
	public Profile getNextApprover(){
		String ejbQL = " from Profile p where p.approver=1 AND p.corporate.id="
				+getCorporate().getId() + " AND p.level="+(getLevelReach() + 1) + 
				" AND (p.transaction="+getTransRef().ordinal() + " OR p.transaction=3)";
		Profile profile = null;
		try {
			profile = (Profile)XPersistence.
					getManager().createQuery(ejbQL).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n The next corporate approver ==="
		+ (profile==null?null:profile.getRole().getName()) + "  and the ejbQL ====" + ejbQL );		
		
		return profile;

	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
