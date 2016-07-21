package ng.com.justjava.epayment.model;

import java.util.*;

import ng.com.justjava.epayment.utility.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;


@View(members="percentage;investors;comment")
public class SendForPayment {

	@Stereotype("MEMO")
	//@NoFrame
	private String comment;
	
	private double percentage;
	
	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	@ListProperties("name,payItem1,payItem2,payItem3,payItem3,summation,netPayment")
	@ReadOnly
	//@NoFrame
	public Collection<Investor> getInvestors() {
		Long id = UserManager.getCorporateOfLoginUser().getId();
		List<Investor> investors = XPersistence.getManager().createQuery("FROM Investor i "
				+ "WHERE i.fundManager.id=" + id).getResultList();
		return investors;
	}

}
