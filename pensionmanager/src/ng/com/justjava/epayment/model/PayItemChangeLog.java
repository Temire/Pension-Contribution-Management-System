package ng.com.justjava.epayment.model;

import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
public class PayItemChangeLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	private Long rsaHolderId;
	private String itemCode;
	private BigDecimal previousAmount;
	private BigDecimal newAmount;
	private int monthChanged;
	private int yearChanged;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRsaHolderId() {
		return rsaHolderId;
	}

	public void setRsaHolderId(Long rsaHolderId) {
		this.rsaHolderId = rsaHolderId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public BigDecimal getPreviousAmount() {
		return previousAmount;
	}

	public void setPreviousAmount(BigDecimal previousAmount) {
		this.previousAmount = previousAmount;
	}

	public BigDecimal getNewAmount() {
		return newAmount;
	}

	public void setNewAmount(BigDecimal newAmount) {
		this.newAmount = newAmount;
	}

	public int getMonthChanged() {
		return monthChanged;
	}

	public void setMonthChanged(int monthChanged) {
		this.monthChanged = monthChanged;
	}

	public int getYearChanged() {
		return yearChanged;
	}

	public void setYearChanged(int yearChanged) {
		this.yearChanged = yearChanged;
	}
}
