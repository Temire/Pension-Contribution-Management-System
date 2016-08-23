package ng.com.justjava.epayment.model;

import javax.persistence.*;

import ng.com.justjava.epayment.action.*;

import org.openxava.annotations.*;
@View(members="PIN;pfa;email;firstName;lastName;phoneNumber")
public class RegisterPersonalContribution {
	
	
	@LabelFormat(LabelFormatType.NO_LABEL)
	@OnChange(LoadByPencommNumberAction.class)
	@Required
	private String PIN;
	
	
	@LabelFormat(LabelFormatType.NO_LABEL)
	@ReadOnly
	private String email;
	
	@LabelFormat(LabelFormatType.NO_LABEL)
	private String firstName;
	
	@LabelFormat(LabelFormatType.NO_LABEL)
	private String lastName;
	
	@LabelFormat(LabelFormatType.NO_LABEL)
	private String phoneNumber;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public PensionFundAdministrator getPfa() {
		return pfa;
	}

	public void setPfa(PensionFundAdministrator pfa) {
		this.pfa = pfa;
	}

	

	public String getPIN() {
		return PIN;
	}

	public void setPIN(String pIN) {
		PIN = pIN;
	}



	@ManyToOne
	@DescriptionsList
	@NoCreate @NoModify
	@ReadOnly
	private PensionFundAdministrator pfa;
	
}
