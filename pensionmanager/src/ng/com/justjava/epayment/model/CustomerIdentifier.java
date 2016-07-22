package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import org.openxava.annotations.*;


@Entity
@Views({@View(members="name"),@View(name="invoice",members="name")})
@XmlAccessorType(XmlAccessType.NONE)
public class CustomerIdentifier {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;

	
	private String name;
	private String value;
	
	@OneToMany(mappedBy="identifier")
	private Collection<CollectionItem> items;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Collection<CollectionItem> getItems() {
		return items;
	}

	public void setItems(Collection<CollectionItem> items) {
		this.items = items;
	}
}
