package ng.com.justjava.epayment.model;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
public class ConfirmEntry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id; 
	
	
	@ManyToOne
	private Corporate corporate;
	
	private String month;

	@ManyToOne
	private PeriodYear setyear;
	
	@ManyToOne
	private PensionFundCustodian custodian;
	
	@ManyToOne
	private PensionFundAdministrator pfa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}

	
	

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public PensionFundCustodian getCustodian() {
		return custodian;
	}

	public void setCustodian(PensionFundCustodian custodian) {
		this.custodian = custodian;
	}

	public PeriodYear getSetyear() {
		return setyear;
	}

	public void setSetyear(PeriodYear setyear) {
		this.setyear = setyear;
	}

	public PensionFundAdministrator getPfa() {
		return pfa;
	}

	public void setPfa(PensionFundAdministrator pfa) {
		this.pfa = pfa;
	}
	
	
	
}
