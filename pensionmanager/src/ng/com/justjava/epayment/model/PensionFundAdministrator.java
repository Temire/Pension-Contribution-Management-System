package ng.com.justjava.epayment.model;

import java.math.*;
import java.util.*;
import java.util.prefs.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import ng.com.justjava.epayment.model.RemitPension.*;
import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

@SuppressWarnings("serial")
@Entity
@Views({@View(members="rcNo;name;email;uniqueIdentifier;custodian;account;Users {users}"),
	@View(name="pfaRecordView" , members="companies"),
	@View(name="rsaCompanyHolders" , members="companyHolders"),
	@View(name="pfaHolders" , members="holders")})
@Tabs({@Tab(properties="rcNo,name",baseCondition = "${deleted}=0"),
	   @Tab(name="custodianView", properties="name",filter=LoginUserPFCFilter.class,baseCondition = "${custodian.id}=?")})
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name ="Administrator")
public class PensionFundAdministrator extends AccountOwnerDetail{


	@DisplaySize(15)
	private String rcNo;

	
	public Months getCurrentMonth(){
		Months result = Months.January;
		String month = "0";
		try {
			
			System.out.println("1 The month saved===="+
					Users.getCurrentPreferences().get("month", String.valueOf(7)));
			
			month = Users.getCurrentPreferences().get("month", String.valueOf(7));
			result = result.getMonth(Integer.valueOf(month));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public String getCurrentYear(){
		String year = "2016";
		try {
			//month = Users.getCurrentPreferences().get("month", String.valueOf(2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return year;
	}	
/*	@XmlElement(name = "Code")
	public String getCode(){
		return getUniqueIdentifier();
	}

	@XmlElement(name = "Name")
	public String getName(){
		return super.getName();
	}
*/	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	@ManyToOne
	private PFCViewByPensionAdministrator pfcView;

	@Transient
	private MonthlyUpload upload;

	@ReadOnly
	@ViewAction("")
	@OneToMany(mappedBy="pfa")
	@ListProperties("pencommNumber,corporate.name,firstName,secondName,pensionAmount,upload.month,upload.uploadYear.year")
	@Condition("${pfa.id}=0 AND ${corporate.id}=0 AND upload.status=5")
	private Collection<RSAHolder> holders;



	
	public int getFullHoldersNumber(){
		String month =String.valueOf(Dates.getMonth(Dates.createCurrent()));
		try {
			System.out.println("After The Integer Value Here====="+ 
					Users.getCurrentPreferences().get("month", String.valueOf(3)+
					" while the current =="+ Users.getCurrentPreferences().get("month", String.valueOf(3))));
			month = Users.getCurrentPreferences().get("month", month);
			System.out.println("Inside getFullHoldersNumber month ====="+ month);
			
		} catch (BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String ejbQL = "FROM RSAHolder r WHERE r.upload.month="+month + " AND r.pfa.id="+getId();
		Collection<RSAHolder> holders = XPersistence.getManager().
				createQuery(ejbQL).getResultList();
		return (holders!=null?holders.size():0);
	}
	
	
	public boolean isDeleted() {
		return deleted;
	}


	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	public void setCompanyHolders(Collection<RSAHolder> companyHolders) {
		this.companyHolders = companyHolders;
	}


	public void setTotalNumberOfHolders(int totalNumberOfHolders) {
		this.totalNumberOfHolders = totalNumberOfHolders;
	}


	public void setAmountSummation(BigDecimal amountSummation) {
		this.amountSummation = amountSummation;
	}


	@ListProperties("name")
	@Transient
	@ReadOnly
	@ViewAction("")
	@RowAction("RowAction.showDetail")

	public Collection<Corporate> getCompanies(){
		
		System.out.println("Before The Integer Value Here=====");
		///String monthMap = Users.getInnerMap();
		String month =String.valueOf(Dates.getMonth(Dates.createCurrent()));
		try {
			System.out.println("After The Integer Value Here====="+ 
					Users.getCurrentPreferences().get("month", String.valueOf(3)+
					" while the current =="+ Users.getCurrentPreferences().get("month", String.valueOf(3))));
			month = Users.getCurrentPreferences().get("month", month);
			System.out.println("After The Integer Value Here====="+ month);
			
		} catch (BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String query = " FROM Corporate c "
				+ "INNER JOIN c.holders h WHERE "
				+ "h.pfa.id="+getId() +" AND h.upload.status=5 " +" ORDER BY h.upload.id" ;//+ " AND " + "h.upload.month="+month;

		System.out.println(" Surprising query here ==="+query);
				
				
		Collection<Object[]> copmanies = null;
		Collection<Corporate> list = new ArrayList<Corporate>();
		try {
			copmanies = XPersistence.getManager().createQuery(query).getResultList();
			for (Object[] object : copmanies) {
				Corporate corporate = (Corporate)object[0];
				RSAHolder holder = (RSAHolder)object[1];
				corporate.addAmountSummation(holder.getPensionAmount());
				corporate.incrementTotalNumberOfHolders();
				if(!list.contains(corporate))
					list.add(corporate);
				System.out.println(" The pensionFundAdministrator name=="+corporate.getName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}



	@ManyToOne
	@DescriptionsList
	@NoCreate
	@NoModify
	@Required
	private PensionFundCustodian custodian;

	@ManyToOne
	@NoCreate
	@NoModify
	@DescriptionsList(depends="custodian",condition="${owner.id}=?")
	private Account account;
	
	
	private Date registeredDate;
	private String registerBy;




	@OneToMany(mappedBy="pfa",cascade=CascadeType.ALL)
	@AsEmbedded
	@SaveAction("CustomSaveAction.savePFAUser")
	@ListProperties(value="user.givenName,user.email")
	private Collection<PFAUser> users;


	public String getRcNo() {
		return rcNo;
	}

	public void setRcNo(String rcNo) {
		this.rcNo = rcNo;
	}



	public Collection<PFAUser> getUsers() {
		return users;
	}

	public void setUsers(Collection<PFAUser> users) {
		this.users = users;

	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public String getRegisterBy() {
		return registerBy;
	}

	public void setRegisterBy(String registerBy) {
		this.registerBy = registerBy;
	}

	@PreCreate
	public void preCreate(){
		setRegisterBy(Users.getCurrent());
		setRegisteredDate(Dates.createCurrent());
	}


	public static PensionFundAdministrator findPFAByUniqueIdentifier(String uniqueIdentifier){
		PensionFundAdministrator pensionFundAdministrator = null;
		try {
			pensionFundAdministrator = (PensionFundAdministrator) XPersistence.getManager().createQuery
					("FROM PensionFundAdministrator p WHERE p.uniqueIdentifier='"+uniqueIdentifier+"'").getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pensionFundAdministrator;
	}
	public static PensionFundAdministrator findPFAByAccountNumber(String accountNumber){
		PensionFundAdministrator pensionFundAdministrator = null;
		try {
			pensionFundAdministrator = (PensionFundAdministrator) XPersistence.getManager().createQuery
					("FROM PensionFundAdministrator p WHERE p.account.number='"+accountNumber+"'").getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pensionFundAdministrator;
	}
/*	public Account getPensionAccount() {
		return pensionAccount;
	}

	public void setPensionAccount(Account pensionAccount) {
		this.pensionAccount = pensionAccount;
	}*/

	public PensionFundCustodian getCustodian() {
		return custodian;
	}

	public void setCustodian(PensionFundCustodian custodian) {
		this.custodian = custodian;
	}

	public Collection<RSAHolder> getHolders() {
		return holders;
	}

	public void setHolders(Collection<RSAHolder> holders) {
		this.holders = holders;
	}

	public BigDecimal getAmountSummation() {
		return amountSummation;
	}

	public void addAmountSummation(BigDecimal amountSummation) {
		this.amountSummation = this.amountSummation.add(amountSummation);
	}
	public int getTotalNumberOfHolders() {
		return totalNumberOfHolders;
	}

	public void incrementTotalNumberOfHolders() {
		this.totalNumberOfHolders = this.totalNumberOfHolders + 1;
	}
	public Collection<RSAHolder> getCompanyHolders() {
		return companyHolders;
	}

	public void addCompanyHolders(RSAHolder companyHolder) {
		this.companyHolders.add(companyHolder);

		System.out.println( " Just Added another one and size now===" + companyHolders.size());
		incrementTotalNumberOfHolders();
	}

	@Transient
	public PFAUser getMyAdmin(){
		List<PFAUser> users = XPersistence.getManager().createQuery
							("From PFAUser p WHERE p.pfa.id="+getId()).getResultList();
		PFAUser adminUser = null;
		userLoop:
		for (PFAUser pfaUser : users) {
			for (Role role : pfaUser.getUser().getRoles()) {
				if(Is.equalAsString("pfaAdmin", role.getName())){
					adminUser = pfaUser;
					break userLoop;
				}
			}
		}
		return adminUser;
	}



	public MonthlyUpload getUpload() {
		return upload;
	}

	public void setUpload(MonthlyUpload upload) {
		this.upload = upload;
	}



	public PFCViewByPensionAdministrator getPfcView() {
		return pfcView;
	}


	public void setPfcView(PFCViewByPensionAdministrator pfcView) {
		this.pfcView = pfcView;
	}

	@Transient
	public BigDecimal getTotalAmount(){

		String month =String.valueOf(Dates.getMonth(Dates.createCurrent()));
		
		System.out.println(" Entering getTotalAmount ");
		try {
			System.out.println("After The Integer Value Here====="+ 
					Users.getCurrentPreferences().get("month", String.valueOf(3)+
					" while the current =="+ Users.getCurrentPreferences().get("month", String.valueOf(3))));
			month = Users.getCurrentPreferences().get("month", month);
			System.out.println("Inside getTotalAmount month ====="+ month);
			
		} catch (BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String ejbQL = " SELECT SUM(r.pensionAmount) "
				+ "FROM RSAHolder r WHERE r.upload.month="+month + " AND r.pfa.id="+getId() +
				" GROUP BY r.pfa.id";
		Double amount = 0.00d;
		try {
			amount = (Double) XPersistence.getManager().createQuery(ejbQL).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" The double ==="+ amount);
		return new BigDecimal(amount);
	}


	public Account getAccount() {
		return account;
	}


	public void setAccount(Account account) {
		this.account = account;
	}




	@Transient
	@ListProperties("firstName,secondName,pencommNumber,pensionAmount,fromDateJoinedToDate,latestMonthPaid")
	private Collection<RSAHolder> companyHolders = new ArrayList<RSAHolder>();

	@Transient
	private int totalNumberOfHolders;
	@Transient
	private BigDecimal amountSummation = new BigDecimal(0.00);
}