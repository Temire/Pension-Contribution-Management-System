package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import ng.com.justjava.epayment.action.*;

import org.apache.commons.lang.math.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;

import com.openxava.naviox.model.*;


@Entity
@View(members="stateIdentification;user")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="Customer")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	private String customerId;
	
	@OnChange(LoadIdentityManagerAction.class)
	@DisplaySize(15)
	@XmlElement(name="SateIdentificationNumber")
	private String stateIdentification;
	
	@OneToOne(cascade=CascadeType.ALL)
	@AsEmbedded
	@ReferenceView("customer")
	
	@XmlElement(name="User")
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
		Role customerRole = Role.findRole("customer");
		if(customerRole==null)
			return;
		
		ArrayList<Role> roles = new ArrayList<Role>();
		roles.add(customerRole);
		getUser().setRoles(roles);
		XPersistence.getManager().merge(getUser());
	}
	public String getStateIdentification() {
		return stateIdentification;
	}
	public void setStateIdentification(String stateIdentification) {
		this.stateIdentification = stateIdentification;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public static Customer loadCustomer(String mail){
		Customer customer = null;
		try {
			customer = (Customer) XPersistence.getManager().createQuery("FROM Customer c WHERE c.user.name='"
					+ mail +"'").getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customer;
	}
}
