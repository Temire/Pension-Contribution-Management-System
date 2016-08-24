package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.filter.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;

import com.openxava.naviox.model.*;

//@Table(uniqueConstraints={@UniqueConstraint(columnNames={"level"})})
@Entity
//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Views({@View(members="user;profiles"), @View(name="approver", members="user;profiles"),
	@View(name="fullUser",members="enable,user"), 
	@View(name="forNew",members="user;profiles"),
	
	@View(name="approver", members="userDetail;user;profiles"),
	@View(name="forEdit", members="userDetail;user;profiles")})
 
@Tabs({@Tab(filter=LoginUserCorporateFilter.class,baseCondition = "${corporate.id}=? AND ${deleted}=0"),
	@Tab(name="allUsers",properties="user.givenName,user.familyName,corporate.name")})

public class CorporateUser extends Approver{
/*	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;*/
 	

	@LabelFormat(LabelFormatType.NO_LABEL)
	public String getUserDetail(){
		String detail = getUser()!=null?getUser().getUserDetail():"";
		return detail;
	}

	@OneToOne(cascade=CascadeType.MERGE)
	@JoinColumn(unique=true)
	@AsEmbedded
	  @ReferenceViews({@ReferenceView(forViews="DEFAULT", value="approver" ),
		    @ReferenceView(forViews="approver", value="approver" ),
		    @ReferenceView(forViews="fullUser", value="full"),
		    @ReferenceView(forViews="forEdit", value="withoutName"),
		    @ReferenceView(forViews="forNew", value="fullWithoutPassword")
		  })
	private User user;
	
	
	@ManyToOne
	@JoinColumn(nullable = true)
	private CorporateUserGroup userGroup;
	

	@ListProperties("role.name,universal,approver,transaction")
	@Transient
	@Condition("${universal}=2")
	@ReadOnly
	public Collection<Profile> getProfiles(){
		return null;
	}
	

	@Transient
	public Collection<Profile> getMyProfiles(){
		User user = getUser();
		Collection<Role> roles = user.getRoles();
		Collection<Profile> profiles = new ArrayList<Profile>();
		for (Role role : roles) {
			Profile profile = null;
			ArrayList<Profile> tempVariable = (ArrayList<Profile>) XPersistence.getManager().createQuery
					(" FROM Profile p WHERE p.role.name='"+role.getName()+"'").getResultList();
			if(!tempVariable.isEmpty()){
				profile = tempVariable.get(0);
				profiles.add(profile);
			}
		}
		return profiles;
	}

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}	
	
	
	@PostCreate
	public void postCreate(){
		setName(getUser().getName());		
	}

	public CorporateUserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(CorporateUserGroup userGroup) {
		this.userGroup = userGroup;
	}
	
	public boolean approve(){
		setEnable(true);
		XPersistence.getManager().merge(this);
		return true;
	}

}
