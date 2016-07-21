package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import org.openxava.annotations.*;
import org.openxava.util.*;


@Entity
@View(members="rccNo;name,sidRequired;uniqueIdentifier;email;Users {users} Billing Items{items} Agents {agents} ")
//@XmlRootElement(name ="Biller")
@XmlAccessorType(XmlAccessType.NONE)
public class Biller extends AccountOwnerDetail{


	@DisplaySize(15)
	private String rccNo;
	
	private Date registeredDate;
	private String registerBy;
	
	private boolean sidRequired;
	
	@OneToMany(mappedBy="biller")
	@AsEmbedded
	@ListProperties(value="name,expectedAmount,identifier.name")
	private Collection<CollectionItem> items;
	 
	@OneToMany(mappedBy="biller",cascade=CascadeType.ALL)
	@AsEmbedded
	@SaveAction("CustomSaveAction.saveBillerUser")
	@ListProperties(value="user.givenName,user.email")
	private Collection<BillerUser> users;
	
	@ManyToOne
	@DescriptionsList
	private SectorOfBusiness sectorOfBusiness;
	
	@OneToMany(mappedBy="biller",cascade=CascadeType.ALL)
	@SaveAction("Agent_.saveBillerAgent")
	@EditAction("Agent_.editBillerAgent")
	@NewAction("Agent_.newBillerAgent")
	private Collection<Agent> agents;
	

	
	@XmlElement(name = "RCCNO")
	public String getRccNo() {
		return rccNo;
	}

	public void setRccNo(String rccNo) {
		this.rccNo = rccNo;
	}

	@XmlElement(name="BusinessSector")
	public SectorOfBusiness getSectorOfBusiness() {
		return sectorOfBusiness;
	}

	public void setSectorOfBusiness(SectorOfBusiness sectorOfBusiness) {
		this.sectorOfBusiness = sectorOfBusiness;
	}


	@XmlElement(name = "Agents")
	public Collection<Agent> getAgents() {
		return agents;
	}

	public void setAgents(Collection<Agent> agents) {
		this.agents = agents;
	}

	public Collection<BillerUser> getUsers() {
		return users;
	}

	public void setUsers(Collection<BillerUser> users) {
		this.users = users;
	}

	@XmlElement(name = "RegisteredDate")
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

	@XmlElement(name = "CollectionItems")
	public Collection<CollectionItem> getItems() {
		return items;
	}

	public void setItems(Collection<CollectionItem> items) {
		this.items = items;
	}

	@XmlElement(name = "SIDRequired")
	public boolean isSidRequired() {
		return sidRequired;
	}

	public void setSidRequired(boolean sidRequired) {
		this.sidRequired = sidRequired;
	}	
}
