package ng.com.justjava.epayment.model;

import java.io.*;

import javax.persistence.*;

import org.openxava.annotations.*;


@Entity
@Tab(baseCondition="${deleted}=0")
public class PayItem implements Serializable{
	
	@Id @Column(length=15)  
	private String code;
	
	private String name;
	
	private boolean compulsory;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCompulsory() {
		return compulsory;
	}

	public void setCompulsory(boolean compulsory) {
		this.compulsory = compulsory;
	}   

}
