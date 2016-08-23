package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.openxava.annotations.*;
import org.openxava.util.*;

@Entity
@Views({@View(members="complaint;visibleToCompany;visibleToMyPFA"),
	@View(name="fullView",members="complaint;makeComment")})
@Tabs({@Tab(properties="complainantName,complainantCompany,dateLodge",baseCondition="${status}=0 AND ${deleted=0}"),
	@Tab(name="companyView",properties="complainantName,complainantCompany,dateLodge,status",
	baseCondition="${visibleToCompany}=1 AND ${complainant.corporate.id}=?",filter=LoginUserCorporateFilter.class),
	@Tab(name="pfaView",properties="complainantName,complainantCompany,dateLodge",
	baseCondition="${visibleToMyPFA}=1 AND ${complainant.pfa.id}=?",filter=LoginUserPFAFilter.class),
	
	@Tab(name="viewStatusOfMyComplaint",properties="dateLodge,complaint,status,visibleToCompany,visibleToMyPFA",
	baseCondition="${complainant.user.name}=?",filter=LoginUserFilter.class),
	
	@Tab(name="viewMyComplaint",properties="dateLodge,status,visibleToCompany,visibleToMyPFA",
			baseCondition="${complainant.user.name}=?",filter=LoginUserFilter.class)})

public class LodgeComplaint {
	         
	public enum Status{
		open,close
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;

	private Status status;
	
	@Stereotype("MEMO")
	@ReadOnly(forViews="fullView")
	private String complaint;
	

	@ManyToOne
	private PFAUser specialComplainant;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	
	@ReadOnly
	@Stereotype("MEMO")
	private String previousComment;
	
	@LabelStyle("bold-label")
	@Transient
	@Stereotype("MEMO")
	@Action("LodgeComplaint.addComment")
	private String makeComment;
	
	private Date dateLodge;
	
	private Date dateAttendedTo;
	
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getComplainantName(){
		return getComplainant().getFirstName() + " ," + getComplainant().getSecondName();
	}
	
	public String getComplainantCompany(){
		return getComplainant().getCorporate().getName();
	}
	
	@ManyToOne
	//@Required
	private RSAHolder complainant;
	
	private boolean visibleToCompany;
	
	private boolean visibleToMyPFA;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComplaint() {
		return complaint;
	}

	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}

	public RSAHolder getComplainant() {
		return complainant;
	}

	public void setComplainant(RSAHolder complainant) {
		this.complainant = complainant;
	}

	public boolean isVisibleToCompany() {
		return visibleToCompany;
	}

	public void setVisibleToCompany(boolean visibleToCompany) {
		this.visibleToCompany = visibleToCompany;
	}
	

	public Date getDateLodge() {
		return dateLodge;
	}

	public void setDateLodge(Date dateLodge) {
		this.dateLodge = dateLodge;
	}

	public Date getDateAttendedTo() {
		return dateAttendedTo;
	}

	public void setDateAttendedTo(Date dateAttendedTo) {
		this.dateAttendedTo = dateAttendedTo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isVisibleToMyPFA() {
		return visibleToMyPFA;
	}

	public void setVisibleToMyPFA(boolean visibleToMyPFA) {
		this.visibleToMyPFA = visibleToMyPFA;
	}

	public String getMakeComment() {
		return makeComment;
	}

	public void setMakeComment(String makeComment) {
		this.makeComment = makeComment;
	}

	public String getPreviousComment() {
		return previousComment;
	}

	public void setPreviousComment(String previousComment) {
		this.previousComment = previousComment;
	}

	public PFAUser getSpecialComplainant() {
		return specialComplainant;
	}

	public void setSpecialComplainant(PFAUser specialComplainant) {
		this.specialComplainant = specialComplainant;
	}
	
	@PostCreate
	public void postCreate(){
		RSAHolder holder = UserManager.getHolderProfileOfLoginUser();
		if(holder != null){
			setComplainant(holder);
		}else{
			PFAUser pfaUser = UserManager.getPFAUserProfileOfLoginUser();
			setSpecialComplainant(pfaUser);
		}
		setDateLodge(Dates.createCurrent());
		setStatus(Status.open);
	}
}
