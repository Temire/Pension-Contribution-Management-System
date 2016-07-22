package ng.com.justjava.epayment.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.xml.ws.soap.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.hibernate.envers.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

@Entity
@Views({@View(members="name;month;periodYear;paymentSummary;comment;softToken"),
		@View(name="initiate",members="name;month;periodYear;paymentSummary;comment"),
		@View(name="approve",members="name;month;periodYear;payingAccount;"
				+ "paymentSummary;comment"),
		@View(name="remit",members="name;month;periodYear;payingAccount,pin;paymentSummary;comment;softToken")})
@Tabs({@Tab(properties="dateEntered,month,myMonthlyContribution,pensionAdministrator"),
	   @Tab(name="approve", properties="dateEntered,month,myMonthlyContribution,pensionAdministrator",
			   filter=LoginUserCorporateFilter.class,baseCondition="${status}=0 AND ${corporate.id}=?"),
	   @Tab(name="remit", properties="dateEntered,month,myMonthlyContribution,pensionAdministrator",
			   filter=LoginUserCorporateFilter.class,baseCondition="${status}=1 AND ${corporate.id}=?")})
public class RemitPension {
	
	public enum Status{
		awaitingApproval,approve,reject,remit;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	public String getName(){
		return "";
	}
	@ManyToOne
	@NoCreate
	@NoModify
	@DescriptionsList(depends="name",condition="${corporate.name}=? AND ${enable}=1",descriptionProperties="display")
	private TransitAccount payingAccount;
	
	
	private Status status;
	
	
	@Transient
	private String pin;
	
	@DescriptionsList(descriptionProperties="year")
	@ManyToOne
	@NoCreate @NoModify
	//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private PeriodYear periodYear;
	
	private int year;
	
	private Date dateEntered;
	
	private String enteredBy;
	
	@ManyToOne
	private Corporate corporate;
/*	public enum Months {
		January,February,March,April,May,June,July,August,September,October,November,December
	}*/
	
	public enum Months { January,February,March,April,May,June,
		July,August,September,October,November,December;
	   public Months getNext() {
	     return this.ordinal() < Months.values().length - 1
	         ? Months.values()[this.ordinal() + 1]
	         : null;
	   }
	   public Months getMonth(int ordinal) {
		   for (Months month : Months.values()) {
			   if(month.ordinal() == ordinal){
				   return month;
			   }
			
		   }
		     return Months.January;
		   }
	 }
	
	public String getSummary(){
		return "Summary";
	}
	private Months month;
	
	@DescriptionsList
	@ManyToOne
	@NoCreate @NoModify
	private Bank payingBank;
	
	private String accountName;
	private String accountNumber;
	
	@Stereotype("MEMO")
	private String comment;
	
	@Column(length=6)
	//@Action("generateToken")
	private String softToken;

	public String getPensionAdministrator(){
		return "IBTC";
		
	}
	
	@Transient
	public BigDecimal getMyMonthlyContribution(){
		String userName = Users.getCurrent();
		String ejbQL = " FROM RSAHolder r WHERE r.email='" + userName +"'";
		RSAHolder rsaHolder =null;
		try {
			rsaHolder = (RSAHolder) XPersistence.getManager().createQuery(ejbQL).getSingleResult();
			
			System.out.println(" The sent rsaHolder contri==" + rsaHolder.getPensionAmount());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsaHolder!=null?rsaHolder.getPensionAmount():new BigDecimal(0.00);
	}
	@ListProperties("firstName,secondName,pencommNumber,pfa.name,voluntaryDonation,"
		+ "pensionAmount,grossPay,myPercentageContribution,companyPercentageContribution")
	@Transient
	public Collection<RSAHolder> getHolders(){
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		Collection<RSAHolder> holders = XPersistence.getManager().
				createQuery("FROM RSAHolder r WHERE r.corporate.id="+corporate.getId()).getResultList();
		return holders;
	}
	@ListProperties("name")
	@Transient
	@ReadOnly
	@CollectionView("rsaCompanyHolders")
	@RowAction("RowAction.showDetail")
	@NoModify
	//@EditOnly
	@ViewAction("")
	public Collection<PensionFundAdministrator> getPaymentSummary(){
		Corporate corporate = UserManager.getCorporateOfLoginUser(); 
		String query = " FROM PensionFundAdministrator p "
				+ "INNER JOIN p.holders h WHERE h.corporate.id="+corporate.getId();
		Collection<Object[]> pfas = null;
		Collection<PensionFundAdministrator> list = new ArrayList<PensionFundAdministrator>();

		try {
			pfas = XPersistence.getManager().
					createQuery(query).getResultList();
			for (Object[] object : pfas) {
				PensionFundAdministrator pfa = (PensionFundAdministrator)object[0];
				
				RSAHolder holder = (RSAHolder)object[1];
				pfa.addAmountSummation(holder.getPensionAmount());
				pfa.addCompanyHolders(holder);
				if(!list.contains(pfa))
					list.add(pfa);
				

				System.out.println(" The pensionFundAdministrator name=="+pfa.getName() + " the holder too here is "+
						holder.getFirstName());
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Months getMonth() {
		return month;
	}

	public void setMonth(Months month) {
		this.month = month;
	}

	public Bank getPayingBank() {
		return payingBank;
	}

	public void setPayingBank(Bank payingBank) {
		this.payingBank = payingBank;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSoftToken() {
		return softToken;
	}

	public void setSoftToken(String softToken) {
		this.softToken = softToken;
	}
	public Date getDateEntered() {
		return dateEntered;
	}
	public void setDateEntered(Date dateEntered) {
		this.dateEntered = dateEntered;
	}
	public String getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	public Corporate getCorporate() {
		return corporate;
	}
	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}
	
	//@PreCreate
	public void setMyCorporate(){
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		int move = (corporate!=null? corporate.getPeriodReach():0) + 1;
		System.out.println(" 1 setMyCorporate...");
		corporate.setPeriodReach(move);
		corporate.setPeriodReach(getMonth().ordinal()+1);
		System.out.println(" 2 setMyCorporate...");
		corporate.setYearReach(getYear());
		//corporate = XPersistence.getManager().merge(corporate);
		
		System.out.println(" 3 setMyCorporate...");
		//setCorporate(corporate);
/*		System.out.println(" 4 setMyCorporate...");
		setYear(getPeriodYear()!=null?getPeriodYear().getYear():0);
		System.out.println(" 5 setMyCorporate...");
		setDateEntered(Dates.createCurrent());
		System.out.println(" 6 setMyCorporate...");*/
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	public PeriodYear getPeriodYear() {
		return periodYear;
	}

	public void setPeriodYear(PeriodYear periodYear) {
		this.periodYear = periodYear;
	}
	
	@PostCreate
	public void postCreate(){
		Collection<RSAHolder> holders = getHolders();
		String msg  = null;
/*		for (RSAHolder rsaHolder : holders) {
			msg = " Your Pension Contribution for the Month of "+ getMonth() + " Year "+getYear()+" Has Been Remited";
			SystemWideSetup.sendNotification(rsaHolder.getEmail(), "Pension Remittance", msg, msg, rsaHolder.getPhoneNumber());
		}*/
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public TransitAccount getPayingAccount() {
		return payingAccount;
	}

	public void setPayingAccount(TransitAccount payingAccount) {
		this.payingAccount = payingAccount;
	}
}
