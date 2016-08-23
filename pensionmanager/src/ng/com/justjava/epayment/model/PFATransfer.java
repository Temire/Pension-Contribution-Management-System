package ng.com.justjava.epayment.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.openxava.annotations.*;
import org.openxava.util.*;

@Entity

@View(members="currentPFA;pfa;comment")
@Tabs({@Tab(properties="holder.firstName,holder.secondName,comment,dateEntered,pfa.name,outgoing.name,status"),
	
	@Tab(name="approveIncoming",properties="holderName,comment,dateEntered,outgoingPFA",
	filter=TransferIncomingFilter.class,baseCondition="${status}=1 AND ${pfa.id}=?"),
	
	@Tab(name="verifyIncoming",properties="holderName,comment,dateEntered,outgoingPFA",
	filter=TransferIncomingFilter.class,baseCondition="${status}=1 AND ${pfa.id}=?"),	
	
	@Tab(name="fundTransferEffected",properties="holderName,comment,dateEntered,status",
	filter=TransferOutgoingingFilter.class,baseCondition="${status}=2 "
			+ " AND ${outgoing.id}=? "),
	
	@Tab(name="approveOutgoing",properties="holderName,comment,dateEntered,incomingPFA",
	filter=TransferOutgoingingFilter.class,baseCondition="${status}=0 AND ${outgoing.id}=?"),
	
	@Tab(name="statusofMyTransfer",properties="comment,dateEntered,incomingPFA,outgoingPFA,status",
	filter=LoginUserFilter.class,baseCondition=" ${holder.user.name}=?"),
	
	@Tab(name="fundTransferConfirmed",properties="holder.firstName,holder.secondName,outgoingPFA,"
			+ "incomingPFA,status",
	filter=TransferOutgoingingFilter.class,baseCondition="${status}=3 AND ${pfa.id}=?")})

public class PFATransfer {


	public enum TransferStatus{
		withIncomingPFA,incomingVerified,approved,fundTransferEffected,fundTransferConfirmed,rejected
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	public String getCurrentPFA(){
		return "";
	}
	
	private TransferStatus status;
	
	@ManyToOne
	@JoinColumn(name = "RSAHolder_id")
	private RSAHolder holder;
	
	private Date dateEntered;
	
	private Date dateApproved;
	

	
	
	@DescriptionsList(depends="currentPFA",condition="${name} != ?",descriptionProperties="name")
	@NoCreate
	@NoModify
	@ManyToOne
	@Required
	private PensionFundAdministrator pfa;

	@ManyToOne
	private PensionFundAdministrator outgoing;
	
	private BigDecimal amountToDate;
	
	@Transient
	public String getHolderName(){
		return getHolder().getFirstName() + ", " + getHolder().getSecondName();
	}
	@Transient
	public String getIncomingPFA(){
		return getPfa().getName();
	}
	
	public String getOutgoingPFA(){
		return getOutgoing().getName();
	}	
	
	public BigDecimal getContributionToDate(){
		return getHolder().getFromDateJoinedToDate();
	}
	
	@Stereotype("MEMO")
	private String comment;

	public PensionFundAdministrator getPfa() {
		return pfa;
	}

	public void setPfa(PensionFundAdministrator pfa) {
		this.pfa = pfa;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RSAHolder getHolder() {
		return holder;
	}

	public void setHolder(RSAHolder holder) {
		this.holder = holder;
	}

	public Date getDateEntered() {
		return dateEntered;
	}

	public void setDateEntered(Date dateEntered) {
		this.dateEntered = dateEntered;
	}

	public Date getDateApproved() {
		return dateApproved;
	}

	public void setDateApproved(Date dateApproved) {
		this.dateApproved = dateApproved;
	}
	
	@PreCreate
	public void preCreate(){
		setHolder(UserManager.getHolderProfileOfLoginUser());
		setOutgoing(getHolder().getPfa());
		setAmountToDate(getHolder().getFromDateJoinedToDate());
	}

	@PostCreate
	public void postCreate(){
		
		setDateEntered(Dates.createCurrent());
		setStatus(TransferStatus.withIncomingPFA);
		
	}

	public TransferStatus getStatus() {
		return status;
	}

	public void setStatus(TransferStatus status) {
		this.status = status;
	}
	public PensionFundAdministrator getOutgoing() {
		return outgoing;
	}
	public void setOutgoing(PensionFundAdministrator outgoing) {
		this.outgoing = outgoing;
	}
	public BigDecimal getAmountToDate() {
		return amountToDate;
	}
	public void setAmountToDate(BigDecimal amountToDate) {
		this.amountToDate = amountToDate;
	}
	
}
