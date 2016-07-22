package ng.com.justjava.epayment.model;

import javax.persistence.*;

@Entity
public class NibbsResponseDescription {
	
	@Id @Column(length=30)  
	private String code;
	
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
