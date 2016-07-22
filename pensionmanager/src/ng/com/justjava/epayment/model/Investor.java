package ng.com.justjava.epayment.model;

import java.io.*;
import java.math.*;
import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.googlecode.jcsv.*;
import com.googlecode.jcsv.annotations.*;
import com.googlecode.jcsv.annotations.internal.*;
import com.googlecode.jcsv.reader.*;
import com.googlecode.jcsv.reader.internal.*;

@Entity
@Views({@View(members="fundManagerName;name;payItem1;payItem2;payItem3;payItem4;account"),
	@View(name="change",members="fundManager;name;netPayment;account")})
@Tabs({@Tab(filter = LoginUserCorporateFilter.class, 
properties = "name,payItem1,payItem2,payItem3,summation",
baseCondition = "${fundManager.id} = ? AND ${deleted=0}"),
@Tab(name="change",filter = LoginUserCorporateFilter.class, 
properties = "name,fundManager.name,account.number,account.bank.name",
baseCondition = "${fundManager.id} = ?")})
public class Investor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	private String email;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	@ManyToOne
	@DescriptionsList//(depends="fundManagerName",condition="${owner.name}=?")
	private Account account;
	
	public BigDecimal getSummation(){
		BigDecimal sum = payItem1.add(payItem2).add(payItem3).add(payItem4);
		return sum;
	}
	private BigDecimal netPayment;
	
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getFundManagerName(){
		//String name = UserManager.getCorporateOfLoginUser().getName();
		//System.out.println(" The sent name here ==== " + name);
		return "S Fund Manager";
	}
	 
	@ManyToOne
	@ReferenceView("lookup")
	private Corporate fundManager;
	
	@MapToColumn(columnName="name")
	private String name;
	
	@MapToColumn(columnName="payItem1")
	private BigDecimal payItem1;
	
	@MapToColumn(columnName="payItem2")
	private BigDecimal payItem2;
	
	@MapToColumn(columnName="payItem3")
	private BigDecimal payItem3;
	
	@MapToColumn(columnName="payItem4")
	private BigDecimal payItem4;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPayItem1() {
		return payItem1;
	}

	public void setPayItem1(BigDecimal payItem1) {
		this.payItem1 = payItem1;
	}

	public BigDecimal getPayItem2() {
		return payItem2;
	}

	public void setPayItem2(BigDecimal payItem2) {
		this.payItem2 = payItem2;
	}

	public BigDecimal getPayItem3() {
		return payItem3;
	}

	public void setPayItem3(BigDecimal payItem3) {
		this.payItem3 = payItem3;
	}

	public BigDecimal getPayItem4() {
		return payItem4;
	}

	public void setPayItem4(BigDecimal payItem4) {
		this.payItem4 = payItem4;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Corporate getFundManager() {
		return fundManager;
	}

	public void setFundManager(Corporate fundManager) {
		this.fundManager = fundManager;
	}

	public static void saveUpload(Reader csvFile,CSVStrategy strategy,ValueProcessorProvider vpp, Object extraParam){		
		try {

			CSVReaderBuilder<Investor> builder = new CSVReaderBuilder<Investor>(csvFile);

			builder.strategy(strategy);
			CSVReader<Investor> csvReader = builder.entryParser(	new AnnotationEntryParser<Investor>(
					Investor.class, vpp)).build();
			
			System.out.println(" About to load all the investors 1");
			
			List<Investor> investors = csvReader.readAll();
			Corporate corporate = UserManager.getCorporateOfLoginUser();
			
			System.out.println(" The corporate of the login user ===next time " + investors);	
			for (Investor investor : investors) {

				System.out.println(" The corporate of the login user ===next time " + investor.getName());	

				if(corporate !=null){
					investor.setFundManager(corporate);
				}
				XPersistence.getManager().merge(investor);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public BigDecimal getNetPayment() {
		return netPayment;
	}

	public void setNetPayment(BigDecimal netPayment) {
		this.netPayment = netPayment;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
