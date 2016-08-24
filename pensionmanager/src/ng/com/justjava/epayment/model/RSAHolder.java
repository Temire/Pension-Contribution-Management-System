package ng.com.justjava.epayment.model;

import java.io.*;
import java.math.*;
import java.util.*;

import javafx.collections.*;

import javax.persistence.*;

import ng.com.justjava.epayment.model.MonthlyUpload.Status;
import ng.com.justjava.epayment.model.RemitPension.Months;
import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.filter.*;

import org.apache.commons.collections.*;
import org.apache.commons.lang3.*;
import org.hibernate.validator.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.googlecode.jcsv.*;
import com.googlecode.jcsv.annotations.*;
import com.googlecode.jcsv.annotations.internal.*;
import com.googlecode.jcsv.reader.*;
import com.googlecode.jcsv.reader.internal.*;
import com.openxava.naviox.model.*;

@Entity

/*@Tab(properties="firstName,secondName",
filter=LoginUserCorporateFilter.class,baseCondition = "select e.firstName, e.secondName FROM RSAHolder "
		+ "e WHERE e.corporate.id=?")*/
@Views({@View(members="firstName;secondName;email;phoneNumber;pfa;pencommNumber;"
		+ " Items {items}"),
		@View(name="viewOnly", members="firstName;secondName;email;phoneNumber;pencommNumber"),
		@View(name="extendedViewOnly", members="firstName;secondName;email;phoneNumber;pencommNumber; Items {items}"),
		@View(name="myRecord", members="firstName;secondName;email;phoneNumber;pencommNumber;voluntaryDonation;"
						+ " Items {items}")})

@Tabs({@Tab(properties="firstName,secondName,pencommNumber,pfa.name,voluntaryDonation,grossPay,"
		+ "pensionAmount,holderContribution,"
		+ "upload.month,upload.uploadYear.year,upload.status",filter=LoginUserCorporateFilter.class,baseCondition = "${corporate.id}=? AND ${deleted=0}"),
		
		@Tab(name="pfaView",properties="firstName,secondName,pencommNumber,pensionAmount,corporate.name,"
				+ "upload.month,upload.uploadYear.year",
		filter=LoginUserPFAFilter.class,baseCondition = "${pfa.id}=?"),
		@Tab(name="personal",properties="upload.month,upload.uploadYear.year,pensionAmount",
		filter=LoginUserFilter.class,baseCondition = "${user.name}=?"),		
		
		@Tab(name="global",properties="pencommNumber,corporate.name,pensionAmount+,pfa.name,"
				+ "pfa.custodian.name,upload.month,upload.uploadYear.year",baseCondition="${deleted}=0"),		
		
		@Tab(name="pfcView",properties="firstName,secondName,pencommNumber,pfa.name,pensionAmount,"
				+ "corporate.name,upload.month,upload.uploadYear.year",filter=LoginUserPFCFilter.class,
				baseCondition = "${pfa.custodian.id}=?")})

public class RSAHolder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id; 
	
	private String remark;
	
	@ManyToOne
	@JoinColumn(name="upload_id")
	private MonthlyUpload upload;
	
	@OneToOne
	private User user;
	
	@MapToColumn(columnName = "firstName")
	@ReadOnly(forViews="viewOnly,extendedViewOnly")
	private String firstName;
	
	@MapToColumn(columnName = "lastName")
	@ReadOnly(forViews="viewOnly,extendedViewOnly")
	private String secondName;
	
	@MapToColumn(columnName = "othername")
	@ReadOnly(forViews="viewOnly,extendedViewOnly")
	private String otherName;
	
	@MapToColumn(columnName = "email")
	@ReadOnly(forViews="viewOnly,extendedViewOnly")
	private String email;
	
	@MapToColumn(columnName = "phoneNumber")
	@ReadOnly(forViews="viewOnly,extendedViewOnly")
	private String phoneNumber;
	
	@MapToColumn(columnName = "pencommNumber")
	@ReadOnly(forViews="myRecord,viewOnly,extendedViewOnly")
	private String pencommNumber;
	
	@MapToColumn(columnName = "voluntaryDonation")
	@ReadOnly(forViews="viewOnly,extendedViewOnly")
	private BigDecimal voluntaryDonation;

	
	private int base;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;
	
	
	
	public boolean isDeleted() {
		return deleted;
	}



	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}



	public Collection<PayItemCollection> getTempItems() {
		return tempItems;
	}



	public void setTempItems(Collection<PayItemCollection> tempItems) {
		this.tempItems = tempItems;
	}



	public int getBase() {
		return base;
	}



	public void setBase(int base) {
		this.base = base;
	}

	private BigDecimal cummulatedAmount;
	
	private int monthCreated;
	private int yearCreated;
	

	
	@Transient
	public BigDecimal getFromDateJoinedToDate(){
		int move = (getCorporate()!=null)?(getCorporate().getPeriodReach() - base):0;
		
		System.out.println("About to get pension amount here==");
		
		BigDecimal pensionAmount = getPensionAmount();
		
		System.out.println(" The pension amount here==" + pensionAmount);
		
		BigDecimal firstAmount = (pensionAmount==null?new BigDecimal(0.00):pensionAmount).multiply(new BigDecimal(move));
		BigDecimal toDate = firstAmount.add(getCummulatedAmount() != null?getCummulatedAmount():new BigDecimal(0.00));
		return toDate;
	}
	
	//@Transient
/*	@ElementCollection
	  @CollectionTable(
		        name="RSAPayItem",
		        joinColumns=@JoinColumn(name="holder_id"))*/
	@OneToMany
	@AsEmbedded
	@ListProperties("name,amount,payItem.active")
	@JoinColumn(name = "RSAHolder_id")
	@ReadOnly(forViews="viewOnly,extendedViewOnly")
	private Collection<RSAPayItem> payItems;
	
	
	@Transient
	@MapToColumn(columnName = "payitem", type = PayItemCollection.class)
	private Object[] payitem;
	
	private BigDecimal monthlyContribution;
	
	
	
	@Transient
	public BigDecimal getPensionAmount(){

		System.out.println(" getGrossPay() to start with =="+getGrossPay());
		double employerPercentageContri = getCompanyPercentageContribution()/100;
		BigDecimal employerContri= getGrossPay().multiply(new BigDecimal(employerPercentageContri));
		//employerContri = employerContri.divide(new BigDecimal(100.00));
		System.out.println(" The Employer contribution ==="+employerContri);
		System.out.println(" Reached 2");
		double employeePercentageContri = getMyPercentageContribution()/100;
		BigDecimal employeeContri= getGrossPay().multiply(new BigDecimal(employeePercentageContri));
		System.out.println(" The Employee contribution ==="+employerContri);
		
		employeeContri = employeeContri.add(getVoluntaryDonation()==null? new BigDecimal(0.00):getVoluntaryDonation());
		System.out.println(" After adding the voluntary=="+employeeContri);
		BigDecimal result = employeeContri.add(employerContri);
		System.out.println(" The result being sent here === "+result);
		return result; 
	}
	
	public BigDecimal getGrossPay(){
		
		System.out.println(" Calculating the gross");
		BigDecimal gross = new BigDecimal(0.00);
		
		if(getItems() == null) 
			return gross;
		
		Collection<PayItemCollection> payItemss = getItems();
		
		System.out.println(" Calculating the gross payitems count " +(payItemss==null?0:payItemss.size()));
		for (PayItemCollection rsaPayItem : payItemss) {
			if(rsaPayItem.isActive()){
				System.out.println(" Troublshooting the rsaPayItem amount =="+rsaPayItem.getAmount());
				gross = gross.add((rsaPayItem.getAmount()!=null?rsaPayItem.getAmount():new BigDecimal(0.00)));
			}
		}
		double doubleGross = gross.doubleValue();
		
		System.out.println(" Troublshooting the doubleGross amount =="+doubleGross);
		if(getUpload()!=null && !getUpload().isMonthlyFigure())
			doubleGross = doubleGross/12.0;
		
		System.out.println(" Troublshooting the doubleGross amount after division by 12 =="+doubleGross);
		gross = new BigDecimal(doubleGross);
		//gross = gross.divide(new BigDecimal(12.0));
		 
		System.out.println(" After the division here====="+gross);
		gross =gross.setScale(2,RoundingMode.CEILING);
		return gross;
	}
	
	@Transient
	public BigDecimal getCompanyContribution(){

		double employerPercentageContri = getCompanyPercentageContribution()/100;
		BigDecimal employerContri= getGrossPay().multiply(new BigDecimal(employerPercentageContri));
		return employerContri;
	}
	
	@Transient
	public BigDecimal getVolutaryEmployer(){

		double employeePercentageContri = getMyPercentageContribution()/100;
		//BigDecimal employeeContri= getGrossPay().multiply(new BigDecimal(employeePercentageContri));
		//employeeContri = employeeContri.add(getVoluntaryDonation()==null? new BigDecimal(0.00):getVoluntaryDonation());
		//employeeContri.
		//employeeContri = 0.00;
		BigDecimal employeeContri = new BigDecimal(0.0);
		return employeeContri;
	}
	
	@Transient
	public BigDecimal getVolutaryEmployee(){

		double employeePercentageContri = getMyPercentageContribution()/100;
		//BigDecimal employeeContri= getGrossPay().multiply(new BigDecimal(employeePercentageContri));
		//employeeContri = employeeContri.add(getVoluntaryDonation()==null? new BigDecimal(0.00):getVoluntaryDonation());
		
		BigDecimal employeeContri = new BigDecimal(0.0);
		//return employeeContri;
		return employeeContri;
	}
	
	@Transient
	public BigDecimal getLegacyEmployer(){

		double employeePercentageContri = getMyPercentageContribution()/100;
		//BigDecimal employeeContri= getGrossPay().multiply(new BigDecimal(employeePercentageContri));
		//employeeContri = employeeContri.add(getVoluntaryDonation()==null? new BigDecimal(0.00):getVoluntaryDonation());
		//return employeeContri;
		BigDecimal employeeContri = new BigDecimal(0.0);
		return employeeContri;
	}
	
	@Transient
	public BigDecimal getLegacyEmployee(){

		double employeePercentageContri = getMyPercentageContribution()/100;
		//BigDecimal employeeContri= getGrossPay().multiply(new BigDecimal(employeePercentageContri));
		//employeeContri = employeeContri.add(getVoluntaryDonation()==null? new BigDecimal(0.00):getVoluntaryDonation());
		
		//return employeeContri;
		BigDecimal employeeContri = new BigDecimal(0.0);
		return employeeContri;
	}
	
	@Transient
	public BigDecimal getOpeningEmployeeContribution(){

		double employeePercentageContri = getMyPercentageContribution()/100;
		//BigDecimal employeeContri= getGrossPay().multiply(new BigDecimal(employeePercentageContri));
		//employeeContri = employeeContri.add(getVoluntaryDonation()==null? new BigDecimal(0.00):getVoluntaryDonation());
		
		//return employeeContri;
		BigDecimal employeeContri = new BigDecimal(0.0);
		return employeeContri;
	}
	
	@Transient
	public BigDecimal getOpeningEmployerContribution(){

		double employeePercentageContri = getMyPercentageContribution()/100;
		//BigDecimal employeeContri= getGrossPay().multiply(new BigDecimal(employeePercentageContri));
		//employeeContri = employeeContri.add(getVoluntaryDonation()==null? new BigDecimal(0.00):getVoluntaryDonation());
		
		//return employeeContri;
		BigDecimal employeeContri = new BigDecimal(0.0);
		return employeeContri;
	}
	
	
	@Transient
	public BigDecimal getHolderContribution(){

		double employeePercentageContri = getMyPercentageContribution()/100;
		BigDecimal employeeContri= getGrossPay().multiply(new BigDecimal(employeePercentageContri));
		employeeContri = employeeContri.add(getVoluntaryDonation()==null? new BigDecimal(0.00):getVoluntaryDonation());
		return employeeContri;
	}
	
	@Transient
	public double getCompanyPercentageContribution(){
		
		System.out.println(" The corporate at this stage =====" + getCorporate());
		if(getCorporate()==null)
			return 0.00;
		else{
			return getCorporate().getCompanyContribution();
		}
	}
	@Transient
	public double getMyPercentageContribution(){
		System.out.println(" The corporate at this stage =====" + getCorporate());
		if(getCorporate()==null)
			return 0.00;
		else{
			return getCorporate().getRSAHolderContribution();
		}
	}
	
	
	
	
/*	@Transient
	@ElementCollection
	@ListProperties("payItem.name,amount")
	public Collection<RSAPayItem> getCompanyPayItems(){
		
		System.out.println(" Calling getPayItems here ==");
		List<RSAPayItem> items = new ArrayList<RSAPayItem>();
		Collection<PayItem> payitems = XPersistence.getManager().createQuery("FROM PayItem c").getResultList();
		for (PayItem payitem : payitems) {
			RSAPayItem rsaItem1 = new RSAPayItem();
			//rsaItem1.setCompanyPayitem(companyPayitem);
			items.add(rsaItem1);
		}
		System.out.println(" The size of this list is==" + items.size());
		return items;
	}*/
	
	@ManyToOne
	@DescriptionsList
	@NoModify
	@NoCreate
	@MapToColumn(columnName = "pfa")
	@Required
	private PensionFundAdministrator pfa;
	
	@ManyToOne
	@ReadOnly(forViews="viewOnly,extendedViewOnly")
	private Corporate corporate;
	

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

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

	public String getPencommNumber() {
		return pencommNumber;
	}

	public void setPencommNumber(String pencommNumber) {
		this.pencommNumber = pencommNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getVoluntaryDonation() {
		return voluntaryDonation;
	}

	public void setVoluntaryDonation(BigDecimal voluntaryDonation) {
		this.voluntaryDonation = voluntaryDonation;
	}

	public Corporate getCorporate() {
		return corporate;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}

	public PensionFundAdministrator getPfa() {
		return pfa;
	}

	public void setPfa(PensionFundAdministrator pfa) {
		this.pfa = pfa;
	}

	public Collection<RSAPayItem> getPayItems() {
		return payItems;
	}

	public void setPayItems(Collection<RSAPayItem> payItems) {
		this.payItems = payItems;
	}
	
	@PreCreate
	public void preCreate(){
		

		setCorporate(UserManager.getCorporateOfLoginUser());

		String roleQuery = "FROM Role r where r.name='RSAHolder'";
		String moduleQuery = "FROM Module m WHERE m.name='ViewMyContribution' ";

		List<Role> rsaHolderRole = XPersistence.getManager().createQuery(roleQuery).getResultList();
		if(rsaHolderRole==null || rsaHolderRole.isEmpty()){
			Role rsaHolder = new Role();
			rsaHolder.setName("RSAHolder");  
			//XPersistence.getManager().merge(billerAdmin);
			List<Module> viewMyContribution = XPersistence.getManager().createQuery(moduleQuery).getResultList();
			rsaHolder.setModules(viewMyContribution);
			rsaHolder = XPersistence.getManager().merge(rsaHolder);
			rsaHolderRole.add(rsaHolder);
		}
		
		System.out.println(" ABout To attach user to RSAHolder ");
		if(user == null)
			user  = new User();
		
		user.setRoles(rsaHolderRole);
		user.setName(getEmail());
		user.setEmail(getEmail());
		user.setFamilyName(getSecondName());
		user.setGivenName(getFirstName());
		user.setPhoneNumber(getPhoneNumber());
		user.setPassword("password");
		
		System.out.println(" There is attachment of user and others here");
		
		user = XPersistence.getManager().merge(user);
		setUser(user);
	}
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
/*	@ListProperties("upload.month,pensionAmount,pfa.name")
	@ReadOnly
	@ViewAction("")
	@Transient
	public Collection<RSAHolder> getPaymentRecords(){
		Collection<RSAHolder> records = XPersistence.getManager().
				createQuery("FROM RSAHolder r WHERE r.id"+getId()).getResultList();
		return records;
	}*/
	
	@Transient
	public Months getLatestMonthPaid(){
		List<RemitPension> records = (List<RemitPension>) XPersistence.getManager().
				createQuery("FROM RemitPension r ORDER BY r.id desc").setMaxResults(1).getResultList();
		RemitPension pension = null;
		if(records!=null && !records.isEmpty()){
			if(records.get(0).getMonth().ordinal()+1 > getMonthCreated())
				return records.get(0).getMonth();
		}
		return null;
	}
	public BigDecimal getCummulatedAmount() {
		return cummulatedAmount;
	}

	public void setCummulatedAmount(BigDecimal cummulatedAmount) {
		this.cummulatedAmount = cummulatedAmount;
	}

	public String saveUpload(Reader csvFile,CSVStrategy strategy,ValueProcessorProvider vpp, Object extra){		
		String response = "Successfully Uploaded";
		try {
			
			Corporate corporate = UserManager.getCorporateOfLoginUser();
			MonthlyUpload localUpload = new MonthlyUpload();
			
			
			
			
			//Months result = (Months)XPersistence.getManager().
					//createQuery(" SELECT MAX(m.month) FROM MonthlyUpload m where m.corporate.id="+corporate.getId()).getSingleResult();
			
/*			String query = "FROM MonthlyUpload m WHERE m.month="+((Months) extra).ordinal()+"and m.corporate.id="+corporate.getId();
			System.out.println(" The Query HEre ==="+ query);
			List uploads = XPersistence.getManager().createQuery(query).getResultList();
			if(!uploads.isEmpty()){
				response = "Error: Data for " +((Months) extra)+ " Already Uploaded ";
				return response;
			}*/
			//Corporate corporate = UserManager.getCorporateOfLoginUser();	
			localUpload.setDateEntered(Dates.createCurrent());
			localUpload.setCorporate(corporate);
			localUpload.setMonth((Months) extra);
			localUpload.setUploadYear(PeriodYear.findPeriodYearByYear(2016));
			//localUpload.setUploadYear((PeriodYear)extra);

			localUpload.setMonthlyFigure(true);
			localUpload.setStatus(Status.New);
			String reference = RandomStringUtils.randomAlphanumeric(18).toLowerCase();
			localUpload.setPaymentOtherReference(reference);
			localUpload = XPersistence.getManager().merge(localUpload);
			
			System.out.println(" The saveUpload called and fired.......................... extra parameter=="+extra);

			CSVReaderBuilder<RSAHolder> builder = new CSVReaderBuilder<RSAHolder>(csvFile);

			builder.strategy(strategy);
			CSVReader<RSAHolder> csvReader = builder.entryParser
					(new AnnotationEntryParser<RSAHolder>(RSAHolder.class, vpp)).build();
			
			List<RSAHolder> holders = csvReader.readAll();
			
			System.out.println( " The total number of holders here===="+ holders.size());
			
			for (RSAHolder holder : holders) {
				
				
				if(holder.getPfa() == null){
					return "Error: PFA not Set or Not Known To The System for RSA Holder " + holder.getFullName();
				}
				System.out.println("1 The loaded holder==" + holder.getFirstName());
				ArrayList<PayItemCollection> innerItems = new ArrayList<PayItemCollection>();
				System.out.println("2 The loaded holder==" + holder.getSecondName());
				holder.setCorporate(corporate);
				if(holder.getPayitem()!=null){
					System.out.println(" The Size of Payitem for this is " + holder.getPayitem().length);
					Object[] objects = holder.getPayitem();

					for (Object object : objects) {
						PayItemCollection pay = (PayItemCollection)object;
						if(pay == null){
							continue;
						}
						System.out.println(" Is my pay a null? "+(null==pay));
						CompanyPayItemCollection companyPayItem = corporate.findCompanyPayItemCollection(pay.getCode());
						if(companyPayItem != null){
							pay.setCompulsory(companyPayItem.isCompulsory());
							if(companyPayItem.isCompulsory()){
								pay.setActive(true);
							}
							System.out.println(" The companyPayItem active == "+ companyPayItem.isActive() +
									" While pay own = "+ pay.isActive());
							
							pay.setActive(companyPayItem.isActive());
							innerItems.add(pay);
							System.out.println(" The pay here == "+ pay + " and its amount ===" + pay.getAmount());
						}
						
						
						

					}
				
				}
				holder.setItems(innerItems);
				System.out.println(" The Pension amount here in upload ==="+holder.getPensionAmount());

				holder.setUpload(localUpload);
				
				System.out.println("2 The loaded holder==" + holder.getEmail());
				//holder.setMonthlyContribution(getPensionAmount());
				//setCorporate(UserManager.getCorporateOfLoginUser());

				String roleQuery = "FROM Role r where r.name='RSAHolder'";
				String moduleQuery = "FROM Module m WHERE m.name='ViewMyContribution' ";

				List<Role> rsaHolderRole = XPersistence.getManager().createQuery(roleQuery).getResultList();
				if(rsaHolderRole==null || rsaHolderRole.isEmpty()){
					Role rsaHolder = new Role();
					rsaHolder.setName("RSAHolder");  
					//XPersistence.getManager().merge(billerAdmin);
					List<Module> viewMyContribution = XPersistence.getManager().createQuery(moduleQuery).getResultList();
					rsaHolder.setModules(viewMyContribution);
					rsaHolder = XPersistence.getManager().merge(rsaHolder);
					rsaHolderRole.add(rsaHolder);
				}
				
				System.out.println(" ABout To attach user to RSAHolder ");
				
				User innerUser  = User.find(holder.getEmail());
				if(innerUser == null){
					innerUser  =  new User();
					innerUser.setRoles(rsaHolderRole);
					innerUser.setName(holder.getEmail());
					innerUser.setEmail(holder.getEmail());
					innerUser.setFamilyName(holder.getSecondName());
					innerUser.setGivenName(holder.getFirstName());
					innerUser.setPhoneNumber(holder.getPhoneNumber());
					innerUser.setPassword("password");
					try {

						innerUser = XPersistence.getManager().merge(innerUser);
					} catch (Exception e) {
						
						 if (e instanceof InvalidStateException) {
								InvalidValue [] invalidValues = ((InvalidStateException) e).getInvalidValues();
								for (int i=0; i<invalidValues.length; i++) {
									System.out.println("invalid_state   "+
											invalidValues[i].getPropertyName()+"  "+ 
											invalidValues[i].getBeanClass().getSimpleName() +"   " +
											invalidValues[i].getMessage()+ "    "+
											invalidValues[i].getValue());			
								}
						// TODO Auto-generated catch block
								e.printStackTrace();
						 }
					}					
					
				}
	
				
				System.out.println(" There is attachment of user and others here");
				
				
				holder.setUser(innerUser);
				System.out.println("1 The Pension amount here in upload second time ==="+holder.getPensionAmount());
				holder = XPersistence.getManager().merge(holder);
				holder.setMonthlyContribution(holder.getPensionAmount());
				holder = holder.updateRemark();
				XPersistence.getManager().merge(holder);
				System.out.println("2 The getMonthlyContribution amount here in upload second time ==="+holder.getMonthlyContribution());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
		
	}



	public Object[] getPayitem() {
		return payitem;
	}



	public void setPayitem(Object[] payitem) {
		if(payitem!=null)
			System.out.println(" Setting this here===" + payitem.length);
		
		this.payitem = payitem;
	}
	@ElementCollection
	@OrderColumn(name="code")
	  @CollectionTable(
		        name="RSAItems",
		        joinColumns=@JoinColumn(name="holder_id"))
	@ListProperties("name,amount,originalAmount")
	@ReadOnly//(forViews="myRecord,viewOnly,extendedViewOnly")
	@RemoveAction("")
	private Collection<PayItemCollection> items;

	public Collection<PayItemCollection> getItems() {
		return items;
	}

	public void setItems(Collection<PayItemCollection> items) {
		this.items = items;
	}


	
	@PostCreate
	public void postCreate(){
		
		System.out.println(" postCreate now fired......................");
		BigDecimal pension = getPensionAmount();
		setMonthlyContribution(pension);

		System.out.println(" Pension variable==="+ pension);
		
		Corporate corporate = UserManager.getCorporateOfLoginUser();
/*		String payitemQuery = "FROM CompanyPayItemCollection p ";

		List<CompanyPayItemCollection> payitems = XPersistence.getManager().createQuery(payitemQuery).getResultList();
		Collection<PayItemCollection> items = new ArrayList<PayItemCollection>();*/
		for (CompanyPayItemCollection payItem : corporate.getItems()) {
			PayItemCollection payItemCollection = new PayItemCollection();
			payItemCollection.setCode(payItem.getCode());
			payItemCollection.setName(payItem.getName());
			payItemCollection.setActive(payItem.isActive());
			items.add(payItemCollection);
		}
		setMonthCreated(Dates.getMonth(Dates.createCurrent()));
		setYearCreated(Dates.getYear(Dates.createCurrent()));
		setItems(items);
		setDirty(false);
		
		
/*		String msg = getFirstName() + ", Created on the platform with User Name "+getUser().getName() + 
				" And First Login Password of " + getUser().getPassword();
		SystemWideSetup.sendNotification(getEmail(), "User Creation", msg, msg, getPhoneNumber());*/
	}
	
/*	@Transient
	private Collection<PayItemCollection> payitems;*/
	
	@Transient
	private Collection<PayItemCollection> tempItems;
/*	@PostLoad
	public void postLoad(){

		setMonthlyContribution(getPensionAmount());
		System.out.println(" Pulling out the the kept map of===" + Users.getInnerMap() + " getItems()=="+getItems());
		tempItems = getItems();
		SystemWideSetup.sendNotification("akinrinde@justjava.com.ng", "Test SMS", "Testing Testing", 
				"Testing SMS Body", "08171038704");
		try {
			for (PayItemCollection payItemCollection : getItems()) {
				payItemCollection.setOriginalAmount(payItemCollection.getAmount());
				//payItemCollection.runPeriodicAmount();
				System.out.println("Loading items........................................." 
				+ payItemCollection.getName()+" its periodAmount==="+payItemCollection.getPeriodAmount()
						+ " the parent name ===" + payItemCollection.getHolder().getFirstName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
*/	
/*	public boolean isDirty(){
		for (PayItemCollection payItemCollection : items) {
			if(!payItemCollection.getAmount().equals(payItemCollection.getOriginalAmount()))
				return true;
		}
		return false;
	}*/
	
	@PreUpdate
	public void preUpdate(){
		System.out.println(" The preupdate runs");

		setMonthlyContribution(getPensionAmount());
		
		int companyBase = getCorporate().getPeriodReach();
		int holderBase = getBase();
		
		//System.out.println("0 The companyBase ==" + companyBase +"  holderBase=="+holderBase);
		
		if(companyBase != holderBase){
			
			//System.out.println("1 The companyBase ==" + companyBase +"  holderBase=="+holderBase + getItems());
			
			//setCummulatedAmount(getFromDateJoinedToDate());
			setBase(companyBase);
			if(getItems()!=null){
/*				for (PayItemCollection payItemCollection : tempItems) {
					System.out.println(" My own copy="+ payItemCollection.getName()
							+ " payItemCollection.getAmount() =="+payItemCollection.getAmount() +
							" payItemCollection.getOriginalAmount()=="+payItemCollection.getOriginalAmount() +
							" payItemCollection.getChangeLog() =="+payItemCollection.getChangeLog());
					
				}*/
				int count = 0;
				for (PayItemCollection payItemCollection : getItems()) {

					if(payItemCollection.getAmount() != null && payItemCollection.getOriginalAmount() !=null ){
						
						System.out.println(" Iterating payItemCollection condition " + 
						!payItemCollection.getAmount().equals(payItemCollection.getOriginalAmount())+ " payItemCollection.getAmount() =="+payItemCollection.getAmount() +
						" payItemCollection.getOriginalAmount()=="+payItemCollection.getOriginalAmount() 
						+ " my internal copy "+((PayItemCollection)CollectionUtils.get(tempItems, count)).getChangeLog());
						
						if(!payItemCollection.getAmount().equals(payItemCollection.getOriginalAmount())){
							String changeLog = ((PayItemCollection) CollectionUtils.get(tempItems, count)).getChangeLog();
							String concat = String.valueOf(getCorporate().getYearReach()) + String.valueOf(getCorporate().getPeriodReach());
							concat = concat+"$"+payItemCollection.getOriginalAmount() + "$"+payItemCollection.getAmount();
							
							System.out.println(" The concat and changelog before setting==" + concat + " , "+changeLog);
			 				changeLog = (changeLog==null?concat : changeLog+"#"+concat);
			 				System.out.println(" The concat and changelog after setting==" + concat + " , "+changeLog);
			 				payItemCollection.setChangeLog(changeLog);
/*							PayItemChangeLog log = new PayItemChangeLog();
							log.setRsaHolderId(getId());
							log.setItemCode(payItemCollection.getCode());
							log.setMonthChanged(getCorporate().getPeriodReach());
							log.setYearChanged(getCorporate().getYearReach());
							log.setMonthChanged(getCorporate().getPeriodReach());
							log.setPreviousAmount(payItemCollection.getOriginalAmount());
							log.setNewAmount(payItemCollection.getAmount());
							System.out.println("2 Itearting payItemCollection " + payItemCollection.getName());
							XPersistence.getManager().merge(log);
							System.out.println(" The original ====="+payItemCollection.getOriginalAmount() + 
							" new amount==="+payItemCollection.getAmount() + " corporate=="+getCorporate());
						*/
						}
					
					}
					count = count + 1;
				}
			
			}
			//XPersistence.getManager().merge(holder);
		
		}		
		setDirty(false);
	}
	
	public boolean isDirty() {
		return dirty;
	}


  
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public String getOtherName() {
		return otherName;
	}



	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public int getMonthCreated() {
		return monthCreated;
	}



	public void setMonthCreated(int monthCreated) {
		this.monthCreated = monthCreated;
	}
	private boolean dirty;
	
	
	
public static Collection<RSAHolder> getAllRSAHoldersCorporate(Long corporateId,Long administratorId, int monthId){
		
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		//MonthlyUpload upload = null;
		//upload.getMonth();
		
		String concat="";
		if(corporateId!= null || administratorId!=null  || monthId != 0){
			concat = " WHERE r.upload.status IN (5) ";
		}
		if(corporateId !=null){
			if(Is.equalAsStringIgnoreCase("WHERE", concat)){
				concat = concat + "r.corporate.id="+corporateId;
			}else{
				concat = concat + " AND " + "r.corporate.id="+corporateId;
			}
		}
		if(administratorId !=null){
			if(Is.equalAsStringIgnoreCase("WHERE", concat)){
				concat = concat + "r.pfa.id="+administratorId;
			}else{
				concat = concat + " AND " + "r.pfa.id="+administratorId;
			}
		}	
		
		if(monthId !=0){
			if(Is.equalAsStringIgnoreCase("WHERE", concat)){
				concat = concat + "r.upload.month ="+(monthId-1);
			}else{
				concat = concat + " AND " + "r.upload.month="+(monthId-1);
			} 
		}
		
		String query = " FROM RSAHolder r ";
		
		query = query + concat + " ORDER BY r.pfa.custodian, r.pfa";
		
		System.out.println("1 The full query string here ==="+ query );
		
		Collection<RSAHolder> holders = XPersistence.getManager().createQuery(query).getResultList();
		
		for (RSAHolder holder : holders) {
			System.out.println("Checking here for what happened "+holder.getUpload().getMonth());
			holder.getPfa().getRcNo();
			
			//if
			//System.out.println("Checking2 here for what happened "+holder.getPfa());
			
						
			System.out.println("Checking2 here for what happened "+holder.getPfa().getCustodian().getPensionAccount().getNumber());
			//holder.get
			//holder
			//System.out.println("Checking4 here for what happened "+holder.getCorporate().getAccounts().iterator().next().getBank().getName());
			
			//System.out.println("Checking4 here for what happened "+holder.ge);
			//itr.hasNext()? itr.next() : null;
			//System.out.println("Checking2 here for what happened "+holder.;
			//System.out.println("Checking the account for what happened"+holder.getUpload().getPayingAccount().getPin());
			
		}
		return holders;
	}
	
	
	
public static Collection<RSAHolder> getAllRSAHolders(Long corporateId,Long administratorId,Long custodianId, int monthId){
	
	Corporate corporate = UserManager.getCorporateOfLoginUser();
	//MonthlyUpload upload = null;
	//upload.getMonth();
	String concat=" WHERE r.upload.status IN (5) ";
	//String concat="";
	if(corporateId!= null || administratorId!=null || custodianId!=null || monthId != 0){
		concat = "WHERE r.upload.status IN (5)";
	}
	if(corporateId !=null){
		if(Is.equalAsStringIgnoreCase("WHERE", concat)){
			concat = concat + "r.corporate.id="+corporateId;
		}else{
			concat = concat + " AND " + "r.corporate.id="+corporateId;
		}
	}
	if(administratorId !=null){
		if(Is.equalAsStringIgnoreCase("WHERE", concat)){
			concat = concat + "r.pfa.id="+administratorId;
		}else{
			concat = concat + " AND " + "r.pfa.id="+administratorId;
		}
	}	
	//concat = concat + "r.";s
	if(custodianId !=null){
		if(Is.equalAsStringIgnoreCase("WHERE", concat)){
			concat = concat + "r.pfa.custodian.id="+custodianId;
		}else{
			concat = concat + " AND " + "r.pfa.custodian.id="+custodianId;
		} 
	}
	if(monthId !=0){
		if(Is.equalAsStringIgnoreCase("WHERE", concat)){
			concat = concat + "r.upload.month ="+(monthId-1);
		}else{
			concat = concat + " AND " + "r.upload.month="+(monthId-1);
		} 
	}
	
	String query = " FROM RSAHolder r ";
	System.out.println(" The full query string here ==="+ query + concat);
	query = query + concat + " ORDER BY r.pfa.custodian, r.pfa, r.corporate";
	
	Collection<RSAHolder> holders = XPersistence.getManager().createQuery(query).getResultList();
	
	for (RSAHolder holder : holders) {
		System.out.println("Checking here for what happened "+holder.getUpload().getMonth());
		holder.getPfa().getRcNo();
		
		//if
		//System.out.println("Checking2 here for what happened "+holder.getPfa());
		
					
		System.out.println("Checking2 here for what happened "+holder.getPfa().getCustodian().getPensionAccount().getNumber());
		//holder.get
		//holder
		//System.out.println("Checking4 here for what happened "+holder.getCorporate().getAccounts().iterator().next().getBank().getName());
		
		//System.out.println("Checking4 here for what happened "+holder.ge);
		//itr.hasNext()? itr.next() : null;
		//System.out.println("Checking2 here for what happened "+holder.;
		//System.out.println("Checking the account for what happened"+holder.getUpload().getPayingAccount().getPin());
		
	}
	return holders;
}
	
	
public static Collection<RSAHolder> getAllRSAHoldersToFrom(Long corporateId,Long administratorId,Long custodianId,int yearFrom,int yearTo,int fromMonthId, int monthId){
		
		//Corporate corporate = UserManager.getCorporateOfLoginUser();
		//MonthlyUpload upload = null;
		//upload.getMonth();
		
		String concat=" WHERE r.upload.status IN (5) ";
		if(corporateId !=null){
			concat = concat + " AND " + "r.corporate.id="+corporateId;
		}
		if(administratorId !=null){
			concat = concat + " AND " + "r.pfa.id="+administratorId;
		}	
		//concat = concat + "r.";
		if(custodianId !=null){
			concat = concat + " AND " + "r.pfa.custodian.id="+custodianId;

		}
		
		
		
		if(monthId !=0){
			if(monthId<fromMonthId){
				concat = concat + " AND " + "r.upload.month BETWEEN "+(monthId-1)+" AND "+(fromMonthId-1);
				
			}else{
				concat = concat + " AND " + "r.upload.month BETWEEN "+(fromMonthId-1)+" AND "+(monthId-1);
			}
		
		
		
		}
			
		
		String query = " FROM RSAHolder r ";
		
		
		query = query + concat + " ORDER BY r.pfa.custodian, r.pfa, r.corporate";
		System.out.println(" The full query around here======"+query);
		
		Collection<RSAHolder> holders = XPersistence.getManager().createQuery(query).getResultList();
		
/*		for (RSAHolder holder : holders) {
			System.out.println("Checking here for what happened "+holder.getUpload().getMonth());
			holder.getPfa().getRcNo();
			holder.getFirstName();
			holder.getUpload().getMonth();
			holder.getUpload().getUploadYear().getYear();
			//if
			//System.out.println("Checking2 here for what happened "+holder.getPfa());
			
			System.out.println(" The logging record ==="+holder.getPfa() + " for holder=="+holder.getFullName());
						
			System.out.println("Checking2 here for what happened "+holder.getPfa().
					getCustodian().getPensionAccount() + " for holder =="+holder.getFullName());
			//holder.get
			//holder
			//System.out.println("Checking4 here for what happened "+CollectionUtils.get(holder.getCorporate().getTransitAccounts(), 0));
			
			//Object getonj = CollectionUtils.get(holder.getCorporate().getTransitAccounts(), 0);
			
			//System.out.println("Checking4 here for what happened "+holder.ge);
			//itr.hasNext()? itr.next() : null;
			//System.out.println("Checking2 here for what happened "+holder.;
			//System.out.println("Checking the account for what happened"+holder.getUpload().getPayingAccount().getPin());
			
		}*/
		return holders;
	}
	
	
	// Report Query
		public static Collection<RSAHolder> getAllRSAHoldersPFA(Long corporateId,Long administratorId,Long custodianId, int monthId){
			
			Corporate corporate = UserManager.getCorporateOfLoginUser();
			//MonthlyUpload upload = null;
			//upload.getMonth();
			String concat=" WHERE r.upload.status IN (5) ";
			if(corporateId !=null){
				concat = concat + " AND " + "r.corporate.id="+corporateId;
			}
			if(administratorId !=null){
				concat = concat + " AND " + "r.pfa.id="+administratorId;
			}	
			//concat = concat + "r.";
			if(custodianId !=null){
				concat = concat + " AND " + "r.pfa.custodian.id="+custodianId;

			}
			if(monthId != 0){
				concat = concat + " AND " + "r.upload.month="+(monthId-1);
			}
			
			
			
			String query = " FROM RSAHolder r ";
			//System.out.println(" The full query string here ==="+ query + concat);
			query = query + concat + " ORDER BY r.pfa.custodian, r.pfa, r.corporate";
			
			System.out.println("2 The full query string here ==="+ query );
			
			Collection<RSAHolder> holders = XPersistence.getManager().createQuery(query).getResultList();
			
			return holders;
		}
	

	// Report Query
	public static Collection<RSAHolder> getCompaniesForReport(Long corporateId,int monthId){
		
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		//MonthlyUpload upload = null;
		//upload.getMonth();
		
		String concat="";
		if(corporateId!= null || monthId != 0){
			concat = " WHERE ";
		}
		if(corporateId !=null){
			if(Is.equalAsStringIgnoreCase("WHERE", concat)){
				concat = concat + "r.corporate.id="+corporateId;
			}else{
				concat = concat + " AND " + "r.corporate.id="+corporateId;
			}
		}
		
		if(monthId !=0){
			if(Is.equalAsStringIgnoreCase("WHERE", concat)){
				concat = concat + "r.upload.month ="+(monthId);
			}else{
				concat = concat + " AND " + "r.upload.month="+(monthId);
			} 
		}
		
		
		
		String query = " FROM RSAHolder r ";
		System.out.println(" The full query string here ==="+ query + concat);
		query = query + concat + " ORDER BY r.pfa.custodian,r.pfa, r.corporate";
		Collection<RSAHolder> holders = XPersistence.getManager().createQuery(query).getResultList();
		
		System.out.println("3 The full query string here ==="+ query );
		for (RSAHolder holder : holders) {
			System.out.println("Checking here for what happened "+holder.getUpload().getMonth());
			
		}
		return holders;
	}

	public int getYearCreated() {
		return yearCreated;
	}



	public void setYearCreated(int yearCreated) {
		this.yearCreated = yearCreated;
	}



	public BigDecimal getMonthlyContribution() {
		return monthlyContribution;
	}



	public void setMonthlyContribution(BigDecimal monthlyContribution) {
		this.monthlyContribution = monthlyContribution;
	}



	public MonthlyUpload getUpload() {
		return upload;
	}



	public void setUpload(MonthlyUpload upload) {
		this.upload = upload;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public RSAHolder updateRemark(){
		RSAHolder previousRecord = null;
		
		try {
			ArrayList<RSAHolder> previousHolders  =(ArrayList<RSAHolder>) XPersistence.getManager().
					createQuery("FROM RSAHolder r WHERE r.pencommNumber='"+getPencommNumber()
							+"' AND r.upload.month="+(getUpload().getMonth().ordinal()-1)).getResultList();
			if(previousHolders!=null && !previousHolders.isEmpty()){
				previousRecord = previousHolders.get(0);
			}
			if(previousRecord == null){
				setRemark("New Holder");
			}else{			
				if(previousRecord.getGrossPay().doubleValue() != getGrossPay().doubleValue() ){
					
					setRemark("Gross was "+previousRecord.getGrossPay() +" but "+getGrossPay()+" this month");
				}
				if(!Is.equalAsStringIgnoreCase(previousRecord.getPfa().getName(),getPfa().getName())){
					setRemark("Last Contribution was made to "+previousRecord.getPfa().getName() +", but going to "+
							getPfa().getName()+" this month");
				}
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this;
	}

	
	@Transient
	public String getVariance(){
		String variance = "same";
		if(!Is.empty(getRemark()))
			variance="vary";
		return variance;
	}
	@Transient
	public String getFullName(){
		return getFirstName() + ", "+getSecondName();
	}
	
	public static RSAHolder findByPencommNumber(String pencommNumber){
		ArrayList<RSAHolder> holders = null;
		try {
			System.out.println(" About to retrieve RSAHolder.......\n");
			holders = (ArrayList<RSAHolder>) XPersistence.getManager().createQuery("FROM RSAHolder r "
					+ "WHERE r.pencommNumber='" +pencommNumber	+ "'").getResultList();
			if(holders !=null && holders.size()>0){
				
				return holders.get(0);
			}
			//System.out.println(" The holder retrieved ============="+holder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
