package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.action.*;
import ng.com.justjava.epayment.model.RemitPension.*;

import org.openxava.annotations.*;
@View(members="administrators")
@Entity
public class PFCViewByPensionAdministrator {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	
	@ManyToOne
	@DescriptionsList(descriptionProperties="year")
	@NoCreate @NoModify
	private PeriodYear uploadYear;
	
	@Required
	@OnChange(OnChangeMonthlyView.class)
	//@ReadOnly
	private Months month;


	public Months getMonth() {
		return month;
	}

	public void setMonth(Months month) {
		this.month = month;
	}
	

	public Collection<PensionFundAdministrator> getAdministrators() {
		return administrators;
	}

	public void setAdministrators(Collection<PensionFundAdministrator> administrators) {
		this.administrators = administrators;
	}
	
	
public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}











	public PeriodYear getUploadYear() {
		return uploadYear;
	}

	public void setUploadYear(PeriodYear uploadYear) {
		this.uploadYear = uploadYear;
	}


	@ReadOnly
	@OneToMany(mappedBy="pfcView")
	@ListProperties("name")
	@ViewAction("")
	@RowAction("RowAction2.showDetail")
	@Condition("${custodian.id}=0")
	private Collection<PensionFundAdministrator> administrators;
	
}
