package ng.com.justjava.epayment.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

@Entity
@Views({
@View(members="rcNo;name;email;address;website;corporatePhoneNumber;uniqueIdentifier;"
		+ "Users{users} PayItems{items}"),
		@View(name="myCorporate",members="rcNo;name;email;address;website;corporatePhoneNumber;uniqueIdentifier;"
				+ "companyContribution; Users{users} PayItems{items} Funding Account{transitAccounts}"),
@View(name="receiver",members="rcNo;name;email;address;website;corporatePhoneNumber;"
		+ "Account{accounts} Users{users} PayItems{items}"),
		
@View(name="balanceEquiry",members="name;transitAccounts;accounts "),
@View(name="lookup",members="name;uniqueIdentifier")
})

@Tab(properties="name,website,email,address,corporatePhoneNumber",baseCondition="${disabled}=0 AND ${deleted}=0")

public class Corporate extends AccountOwnerDetail{
	
/*	public enum Status{
		New,Verified,Approved
	}
	*/
	@Required
	private String address;
	private String website;
	
	
	private int highestApprovalLevel;
	
	@ElementCollection
	@ListsProperties({@ListProperties("code,name"),
	@ListProperties(forViews="myCorporate",value="code,name,enable")})
	private Collection<CompanyPayItemCollection> items;
	
	
	private double companyContribution;
	private double RSAHolderContribution;
	private boolean disabled;
	 
	
	private int periodMoved;
	
	private int periodReach;
	private int yearReach;
	
	@OneToMany(mappedBy="corporate",cascade=CascadeType.ALL)
	@AsEmbedded
	@CollectionView("embeded")
	@ListProperties("terminalId,bank.name,balance")
	@SaveAction("Account.saveTransit")
	@Condition("${corporate.id}=${this.id}")
	@ReadOnly(forViews="balanceEquiry")
	private Collection<TransitAccount> transitAccounts;
	
	private String terminalId;
	

	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;
	
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setTotalNumberOfHolders(int totalNumberOfHolders) {
		this.totalNumberOfHolders = totalNumberOfHolders;
	}

	public void setAmountSummation(BigDecimal amountSummation) {
		this.amountSummation = amountSummation;
	}

	public Collection<TransitAccount> getTransitAccounts() {
		return transitAccounts;
	}

	public void setTransitAccounts(Collection<TransitAccount> transitAccounts) {
		this.transitAccounts = transitAccounts;
	}	
	
	//@DisplaySize(20)
	private String rcNo;
	//@Required
	private String corporatePhoneNumber;
	
	@Embedded
	private ProcessingFeeParameters processingFee;
	
	@ListProperties("pencommNumber,firstName,secondName,pfa.name,voluntaryDonation,pensionAmount,"
			+ "holderContribution,companyContribution,fromDateJoinedToDate")
	@OneToMany(mappedBy="corporate")
	private Collection<RSAHolder> holders;
	
	@ManyToOne
	@DescriptionsList
	@NoCreate
	@NoModify
	private SectorOfBusiness sectorOfBusiness;
	
	
	private boolean receiving;
	

	@OneToMany
	@AsEmbedded
	@ListProperties("approver.name,level,transaction")
	@SaveAction("Approve.saveApprover")
	private Collection<CorporateApprover> approvers;
	

	@OneToMany
	@AsEmbedded
	@ListProperties("name")
	@SaveAction("CustomSaveAction.saveUserGroup")

	private Collection<CorporateUserGroup> userGroups;
	
	@OneToMany(cascade=CascadeType.ALL)
	@AsEmbedded
	@ListProperties("user.givenName,userDetail")
	@SaveAction("CustomSaveAction.saveUser")
	@EditAction("CorporateUser_.editUser")
	@NewAction("CorporateUser_.newUser")
	@Condition("${enable}=1 AND ${deleted}=0 AND ${corporate.id}=${this.id}") 
	private Collection<CorporateUser> users;
	
	  
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getCorporatePhoneNumber() {
		return corporatePhoneNumber;
	}
	public void setCorporatePhoneNumber(String corporatePhoneNumber) {
		this.corporatePhoneNumber = corporatePhoneNumber;
	}
	public Collection<CorporateApprover> getApprovers() {
		return approvers;
	}
	public void setApprovers(Collection<CorporateApprover> approvers) {
		this.approvers = approvers;
	}
	public Collection<CorporateUser> getUsers() {
		return users;
	}
	public void setUsers(Collection<CorporateUser> users) {
		this.users = users;
	}
	public Collection<CorporateUserGroup> getUserGroups() {
		return userGroups;
	}
	public void setUserGroups(Collection<CorporateUserGroup> userGroups) {
		this.userGroups = userGroups;
	}
	public boolean isReceiving() {
		return receiving;
	}
	public void setReceiving(boolean receiving) {
		this.receiving = receiving;
	}
	public ProcessingFeeParameters getProcessingFee() {
		return processingFee;
	}
	public void setProcessingFee(ProcessingFeeParameters processingFee) {
		this.processingFee = processingFee;
	}
	public String getRcNo() {
		return rcNo;
	}
	public void setRcNo(String rcNo) {
		this.rcNo = rcNo;
	}

	public void preCreate(){
		if(getProcessingFee() == null){
			setProcessingFee(SystemWideSetup.getSystemWideSetup().getProcessingFee());
		}
	}
	public SectorOfBusiness getSectorOfBusiness() {
		return sectorOfBusiness;
	}
	public void setSectorOfBusiness(SectorOfBusiness sectorOfBusiness) {
		this.sectorOfBusiness = sectorOfBusiness;
	}
	@Transient
	public String getMasterKey() {
		// TODO Auto-generated method stub
		String masterKey = SystemWideSetup.getSystemWideSetup().getMasterKey();
		return masterKey;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	

	@Transient
	public List<CompanyPayitem> getPayItems(){
		List<CompanyPayitem> payItems = XPersistence.getManager().
				createQuery(" FROM CompanyPayitem c WHERE c.company.id=" + getId()).getResultList();
		return payItems;
	}

	public double getCompanyContribution() {
		return companyContribution;
	}

	public void setCompanyContribution(double companyContribution) {
		this.companyContribution = companyContribution;
	}

	public double getRSAHolderContribution() {
		return RSAHolderContribution;
	}

	public void setRSAHolderContribution(double rSAHolderContribution) {
		RSAHolderContribution = rSAHolderContribution;
	}

	public int getPeriodMoved() {
		return periodMoved;
	}

	public void setPeriodMoved(int periodMoved) {
		this.periodMoved = periodMoved;
	}

	public Collection<CompanyPayItemCollection> getItems() {
		return items;
	}

	public void setItems(Collection<CompanyPayItemCollection> items) {
		this.items = items;
	}
	
	public CompanyPayItemCollection findCompanyPayItemCollection(String code){
		for (CompanyPayItemCollection item : getItems()) {
			
			System.out.println(" The CompanyPayItemCollection =="+item.getCode() + " and active of it "
					+ item.isActive());
			
			if(Is.equalAsStringIgnoreCase(code, item.getCode())){
				return item;
				
			}
				
		}
		return null;
	}
	
	@PostUpdate
	public void postUpdate(){
		if(getItems()!=null){
			for (CompanyPayItemCollection item : getItems()) {
				if(item.isCompulsory())
					item.setActive(true);
			}
		
		}
	}
	
	@PostCreate
	public void postCreate(){
		String payitemQuery = "FROM PayItem p ";

		List<PayItem> payitems = XPersistence.getManager().createQuery(payitemQuery).getResultList();
		Collection<CompanyPayItemCollection> items = new ArrayList<CompanyPayItemCollection>();
		for (PayItem payItem : payitems) {
			CompanyPayItemCollection companyPayItemCollection = new CompanyPayItemCollection();
			companyPayItemCollection.setCompulsory(payItem.isCompulsory());
			companyPayItemCollection.setCode(payItem.getCode());
			if(payItem.isCompulsory()){
				companyPayItemCollection.setActive(true);
			}
			companyPayItemCollection.setName(payItem.getName());
			items.add(companyPayItemCollection);
		}
		setItems(items);
		PensionSystemSetup pensionSetup = SystemWideSetup.getSystemWideSetup().getPensionSystemSetup();
		
		if(pensionSetup!= null && pensionSetup.getEmployerContributionPercent()>0d){
			setCompanyContribution(pensionSetup.getEmployerContributionPercent());
		}
		if(pensionSetup!= null && pensionSetup.getEmployeeContributionPercent()>0d){
			setRSAHolderContribution(pensionSetup.getEmployeeContributionPercent());
		}		
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
	
	public int getPeriodReach() {
		return periodReach;
	}

	public void setPeriodReach(int periodReach) {
		this.periodReach = periodReach;
	}

	public int getYearReach() {
		return yearReach;
	}

	public void setYearReach(int yearReach) {
		this.yearReach = yearReach;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public int getHighestApprovalLevel() {
		return highestApprovalLevel;
	}

	public void setHighestApprovalLevel(int highestApprovalLevel) {
		this.highestApprovalLevel = highestApprovalLevel;
	}

	@Transient
	private int totalNumberOfHolders;

	@Transient
	private BigDecimal amountSummation = new BigDecimal(0.00);
	

	
}
