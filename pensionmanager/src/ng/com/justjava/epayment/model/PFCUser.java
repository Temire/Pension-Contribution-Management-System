package ng.com.justjava.epayment.model;

import javax.persistence.*;

import org.openxava.annotations.*;

import com.openxava.naviox.model.*;

@Entity
@View(members="user")
public class PFCUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;

	@ManyToOne//(cascade=CascadeType.ALL)
	private PensionFundCustodian pfc;
	
	@OneToOne(cascade=CascadeType.ALL)
	@ReferenceView("approver")
	@AsEmbedded
	private User user;
	
	public Long getId() {
		return id; 
	}

	public void setId(Long id) {
		this.id = id;
	}



	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@PreCreate
	public void preCreate(){
/*		List<Role> billerUser = XPersistence.getManager().createQuery("FROM Role r where r.name='billerAdmin'").getResultList();
		if(billerUser==null || billerUser.isEmpty()){
			Role billerAdmin = new Role();
			billerAdmin.setName("billerAdmin");  
			XPersistence.getManager().merge(billerAdmin);
			List<Module> biller = XPersistence.getManager().createQuery("FROM Module m WHERE m.name='MyBiller'").getResultList();
			billerAdmin.setModules(biller);
			billerAdmin = XPersistence.getManager().merge(billerAdmin);
			billerUser.add(billerAdmin);
		}
		getUser().setRoles(billerUser);*/
	}


	public PensionFundCustodian getPfc() {
		return pfc;
	}

	public void setPfc(PensionFundCustodian pfc) {
		this.pfc = pfc;
	}
}