package ng.com.justjava.epayment.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;


@Entity
public class PensionCalculator {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id; 
	
	
	private double employerRate;
	
	@ReadOnly
	private double employeeRate;
	
	private BigDecimal voluntaryContribution;
	
	@ElementCollection
	@ListProperties("name,amount,active")
	@CollectionTable(name="CalculatorItems",joinColumns=@JoinColumn(name="calculator_id"))
	private Collection<CalculatorPayItems> items;



	public double getEmployerRate() {
		return employerRate;
	}


	public void setEmployerRate(double employerRate) {
		this.employerRate = employerRate;
	}


	public double getEmployeeRate() {
		return employeeRate;
	}


	public void setEmployeeRate(double employeeRate) {
		this.employeeRate = employeeRate;
	}


	public Collection<CalculatorPayItems> getItems() {
		return items;
	}


	public void setItems(Collection<CalculatorPayItems> items) {
		this.items = items;
	}
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public BigDecimal getVoluntaryContribution() {
		return voluntaryContribution;
	}


	public void setVoluntaryContribution(BigDecimal voluntaryContribution) {
		this.voluntaryContribution = voluntaryContribution;
	}
}
