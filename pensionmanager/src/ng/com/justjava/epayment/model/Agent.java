package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;

import com.openxava.naviox.model.*;

@Entity
@View(members="photo;uniqueCode;type;user;attachCollectionItem")
@Tab(properties="name,type,biller.name",baseCondition = "${deleted}=0")
@XmlAccessorType(XmlAccessType.NONE)
public class Agent {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;

	public enum AgentType{
		directPurchase,commission;
	}
	@OneToOne
	@AsEmbedded
	@ReferenceView("approver")
	private User user;
	
	
	@ListProperties("name,expectedAmount,identifier.name")
	@Transient
	@Condition("${biller.id}=0")
	@ReadOnly
	public Collection<CollectionItem> getAttachCollectionItem(){
		return null;
	}
	
	
	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(name="agents_collectionItems", joinColumns={@JoinColumn(name="agent_id")}, 
	inverseJoinColumns={@JoinColumn(name="colectionItem_id")})
	private Collection<CollectionItem> colletionItems;
	
	@Editor("ValidValuesRadioButton")
	@Required
	private AgentType type; 
	
	@ManyToOne
	private Biller biller;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	private String uniqueCode;

	@Stereotype("PHOTO")
	private byte[] photo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name="uniqueCode")
	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Biller getBiller() {
		return biller;
	}

	public void setBiller(Biller biller) {
		this.biller = biller;
	}

	public AgentType getType() {
		return type;
	}

	public void setType(AgentType type) {
		this.type = type;
	}
	
	@XmlElement(name="Name")
	public String getName(){
		if(getUser()== null)
			return "";
		return getUser().getGivenName() + " " + getUser().getFamilyName();
	}

	public Collection<CollectionItem> getColletionItems() {
		return colletionItems;
	}

	public void setColletionItems(Collection<CollectionItem> colletionItems) {
		this.colletionItems = colletionItems;
	}
	
	public static Agent getAgentByUserName(String name){
		String ejbQL = " FROM Agent a WHERE a.user.name='" + name +"'";
		Agent agent = null;
		try {
			agent = (Agent) XPersistence.getManager().createQuery(ejbQL).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return agent;
	}
	public void addCollectionItem(Collection<CollectionItem> items){
			colletionItems = new ArrayList<CollectionItem>();
		if(items!=null)
			colletionItems.addAll(items);
		
		//XPersistence.getManager().merge(this);
	}
}
