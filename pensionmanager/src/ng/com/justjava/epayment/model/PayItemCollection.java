package ng.com.justjava.epayment.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.utility.*;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.openxava.naviox.actions.*;

@Embeddable
public class PayItemCollection {
	
	@ReadOnly
	private String code;
	

	
	@Parent
	private RSAHolder holder;
	
	private boolean compulsory;
	
	private String name;
	private BigDecimal amount;
	private boolean active;
	
	private String changeLog;
	@Transient
	@ReadOnly
	private BigDecimal originalAmount;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@PostLoad
	public void postLoad(){
		System.out.println(" Loading.......................................");
	}
	public BigDecimal getOriginalAmount() {
		return originalAmount;
	}
	public void setOriginalAmount(BigDecimal originalAmount) {
		this.originalAmount = originalAmount;
	}
	
	@Transient
	public BigDecimal getPeriodAmount(){

		BigDecimal result = getOriginalAmount();
		String period = Dates.getYear(Dates.createCurrent())+"12";
		if(getChangeLog() == null)
			return result;
		String[] array = getChangeLog().split("#");
		System.out.println(" Original String === "+getChangeLog() + " The lenth of the arrary here " 
		+(array!=null?array.length:0));
		List<String> list =  Arrays.asList(array);
		for (String string : list) { 
			System.out.println(" The STring here ==" + string);
			String[] record = string.split("\\$");
			if(Long.valueOf(period).doubleValue()>= Long.valueOf(record[0]).doubleValue()){
				result = new BigDecimal(record[1]);
				break;
			}
			System.out.println(" the records length ==" +record.length);
		}
		
		return result;
	}
	public RSAHolder getHolder() {
		return holder;
	}
	public void setHolder(RSAHolder holder) {
		this.holder = holder;
	}
	public String getChangeLog() {
		return changeLog;
	}
	public void setChangeLog(String changeLog) {
		this.changeLog = changeLog;
	}
	
	public BigDecimal getPeriodicAmount(){
		BigDecimal result = getOriginalAmount();
		String period = "201512";
		if(getChangeLog() == null)
			return result;
		String[] array = getChangeLog().split("#");
		System.out.println(" Original String === "+getChangeLog() + " The lenth of the arrary here " 
		+(array!=null?array.length:0));
		List<String> list =  Arrays.asList(array);
		for (String string : list) { 
			System.out.println(" The STring here ==" + string);
			String[] record = string.split("\\$");
			if(Long.valueOf(period).doubleValue()>= Long.valueOf(record[0]).doubleValue()){
				result = new BigDecimal(record[1]);
				break;
			}
			System.out.println(" the records length ==" +record.length);
		}
		
		return result;
	}
	public boolean isCompulsory() {
		return compulsory;
	}
	public void setCompulsory(boolean compulsory) {
		this.compulsory = compulsory;
	}
}
