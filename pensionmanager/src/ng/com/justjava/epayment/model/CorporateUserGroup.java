package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.filter.*;

import org.openxava.annotations.*;

import com.openxava.naviox.model.*;

@View(members="name;users")
@Entity
@Tab(filter=LoginUserCorporateFilter.class,baseCondition = "${corporate.id}=?")
public class CorporateUserGroup extends Approver{
/*	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;*/
	

	@OneToMany(mappedBy="userGroup")
/*	@NoCreate
	@NoModify
	@AsEmbedded*/
	
	
/*	@JoinTable(name = "user_userGroup", 
	joinColumns = @JoinColumn(name = "name"), inverseJoinColumns = @JoinColumn(name = "id"))	
*/
	@ListProperties("user.givenName,user.familyName")
	//@NewAction("UserGroup.addUser")	
	private Collection<CorporateUser> users;
	 

	
	

/*	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}*/

/*	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}*/

	public Collection<CorporateUser> getUsers() {
		return users;
	}

	public void setUsers(Collection<CorporateUser> users) {
		this.users = users;
	}



}
