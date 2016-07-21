package ng.com.justjava.epayment.model;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import org.openxava.annotations.*;

@Entity
public class SectorOfBusiness {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
