package ng.com.justjava.epayment.model;

import java.util.*;

import javax.xml.bind.annotation.*;

import org.openxava.jpa.*;


@XmlRootElement(name ="PensionFundAdministrators")
public class PensionFundAdministrators {
    @XmlElement(name = "PFA")
	public Collection<PensionFundAdministrator> getPensionFundAdministrators(){
		return XPersistence.getManager().createQuery("FROM PensionFundAdministrators p").
				getResultList();
	}
}
