package ng.com.justjava.epayment.utility;

import java.math.*;

import com.googlecode.jcsv.annotations.*;

public class CSVToRSAHolder {
	
	@MapToColumn(columnName = "firstName")
	private String firstName;
	
	@MapToColumn(columnName = "secondName")
	private String secondName;
	
	@MapToColumn(columnName = "email")
	private String email;
	
	@MapToColumn(columnName = "phoneNumber")
	private String phoneNumber;
	
	@MapToColumn(columnName = "pencommNumber")
	private String pencommNumber;
	
	@MapToColumn(columnName = "voluntaryDonation")
	private BigDecimal voluntaryDonation;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPencommNumber() {
		return pencommNumber;
	}
	public void setPencommNumber(String pencommNumber) {
		this.pencommNumber = pencommNumber;
	}
	public BigDecimal getVoluntaryDonation() {
		return voluntaryDonation;
	}
	public void setVoluntaryDonation(BigDecimal voluntaryDonation) {
		this.voluntaryDonation = voluntaryDonation;
	}
}
