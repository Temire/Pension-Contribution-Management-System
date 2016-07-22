package ng.com.justjava.epayment.model;

import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@Embeddable
@View(members="deductionType,accountToDebit;bank;accountNumber;accountName;amount;naration")
public class ProcessingFeeParameters {
	public enum DeductionMethod{
		lumpSum,perTransaction
	}
	
	public enum AccountToDebit{
		beneficiaryAccount,debitAccount
	}
	
	private AccountToDebit accountToDebit;
	
	
	@ManyToOne
	@DescriptionsList
	private Bank bank;
	
	
	//@Required
	private DeductionMethod deductionType;
	
	//@Required
	private String accountNumber;
	//@Required
	private String accountName;
	
	//@Required
	private BigDecimal amount;
	
	//@Required
	private String naration;
	
	public DeductionMethod getDeductionType() {
		return deductionType;
	}
	public void setDeductionType(DeductionMethod deductionType) {
		this.deductionType = deductionType;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getNaration() {
		return naration;
	}
	public void setNaration(String naration) {
		this.naration = naration;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public AccountToDebit getAccountToDebit() {
		return accountToDebit;
	}
	public void setAccountToDebit(AccountToDebit accountToDebit) {
		this.accountToDebit = accountToDebit;
	}

}
