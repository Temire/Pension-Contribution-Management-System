package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;


@Entity
@Views({@View(members="rcNo;name;email;uniqueIdentifier;Users {users}"
		+ " Contribution Account{accounts}"),
	@View(name="administrators", members="administrators")})

@Tabs({@Tab(properties="name",baseCondition = "${deleted}=0"),
	   @Tab(name="pencommView",properties="name")})

public class PensionFundCustodian extends AccountOwnerDetail{


	@DisplaySize(15)
	private String rcNo;
	
	private Date registeredDate;
	private String registerBy;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	
	@ReadOnly
	@OneToMany(mappedBy="custodian")
	@ListProperties("name,fullHoldersNumber")
	//@CollectionView("rsaCompanyHolders")
	@ViewAction("")
	@RowAction("RowAction2.showDetail")
	private Collection<PensionFundAdministrator> administrators;
	
	@OneToOne
	@AsEmbedded
	@Required
	private Account pensionAccount;
	 
	@OneToMany(mappedBy="pfc",cascade=CascadeType.ALL)
	@AsEmbedded
	@SaveAction("CustomSaveAction.savePFCUser")
	@ListProperties(value="user.givenName,user.email")
	private Collection<PFCUser> users;


	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getRcNo() {
		return rcNo;
	}

	public void setRcNo(String rcNo) {
		this.rcNo = rcNo;
	}

	

	public Collection<PFCUser> getUsers() {
		return users;
	}

	public void setUsers(Collection<PFCUser> users) {
		this.users = users;
		
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

	
	public static PensionFundAdministrator findPFAByUniqueIdentifier(String uniqueIdentifier){
		PensionFundAdministrator pensionFundAdministrator = null;
		try {
			pensionFundAdministrator = (PensionFundAdministrator) XPersistence.getManager().createQuery
					("FROM PensionFundAdministrator p WHERE p.uniqueIdentifier='"+uniqueIdentifier+"'").getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pensionFundAdministrator;
	}

/*	public Account getPensionAccount() {
		return pensionAccount;
	}

	public void setPensionAccount(Account pensionAccount) {
		this.pensionAccount = pensionAccount;
	}
*/
	public Collection<PensionFundAdministrator> getAdministrators() {
		return administrators;
	}

	public void setAdministrators(Collection<PensionFundAdministrator> administrators) {
		this.administrators = administrators;
	}

	public Account getPensionAccount() {
		return pensionAccount;
	}

	public void setPensionAccount(Account pensionAccount) {
		this.pensionAccount = pensionAccount;
	}

	
}