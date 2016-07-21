package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import org.apache.commons.collections.map.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

@Entity
public class ScratchCard {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	private String number;
	private String dateEntered;
	private boolean expired;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDateEntered() {
		return dateEntered;
	}

	public void setDateEntered(String dateEntered) {
		this.dateEntered = dateEntered;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	
	@Transient
	public static boolean isValid(String number){
		ScratchCard scratchCard= null;
		try {
			scratchCard= (ScratchCard) XPersistence.getManager().
					createQuery("FROM ScratchCard s WHERE s.number='"+number+"' AND expired=0").getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(scratchCard !=null)
			return true;
		
		return false;
		
	}
	
	public static void invalidate(Map key){
		try {
			Map expire = new HashedMap();
			expire.put("expired", true);
			MapFacade.setValues("ScratchCard", key, expire);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
