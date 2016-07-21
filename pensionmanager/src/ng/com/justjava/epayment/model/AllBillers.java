package ng.com.justjava.epayment.model;

import java.util.*;

import javax.xml.bind.annotation.*;

import org.openxava.jpa.*;

@XmlRootElement(name ="BillerList")
@XmlAccessorType(XmlAccessType.NONE)
public class AllBillers {
	
    //@XmlElementWrapper(name = "billers")
    @XmlElement(name = "biller")
	public Collection<Biller> getBillers(){
		return XPersistence.getManager().createQuery("FROM Biller b").getResultList();
	}
}
    