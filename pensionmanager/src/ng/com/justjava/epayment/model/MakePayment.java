package ng.com.justjava.epayment.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import ng.com.justjava.epayment.action.*;
import ng.com.justjava.filter.*;

import org.apache.commons.lang.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

@Entity
@Views({@View(members="paymentOption;collectionBiller;name;email;phoneNumber;Customer [customerName;customerEmail]; "
		+ "biller;item;amount;SID;Customer Id [#identifier,value];meansOfPayment;"
		+ "Payment Method[# means,meansValue,bank]; previousPayment"),
		@View(name="agent",members="collectionBiller;name;email;phoneNumber;"
				+ "Customer [customerName,customerEmail]; "
				+ "biller;item;amount;SID;"
				+ "Customer Id [#identifier,value];meansOfPayment;"
				+ "Payment Method[# means,meansValue,bank]; previousPayment"),
				@View(name="payBill",members="biller;item;amount;"
						+ "Customer Id [#identifier,value];meansOfPayment;"
						+ "Payment Method[# means,meansValue,bank]; collectionBiller; email;SID;name;phoneNumber;")})

@Tab(filter = LoginUserBillerFilter.class, 
properties = "name,amount,meansOfPayment,meansValue,biller.name,item.name,identifier,value", 
baseCondition = "${biller.id} = ? AND ${deleted=0}")

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="Collection")
@XmlType(propOrder={"customerId", "amount", "collectionItem" ,"identifier" ,"value","SID","collectionBiller"})
public class MakePayment {
	
	public enum MeansOfPayment{
		cash,accountTransfer,debitCard,scratchCard
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	private boolean paid;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	@Transient
	@Editor("opt")
	private PaymentOption paymentOption;
	
	private String customerId;
	
	private String customerName;
	private String customerEmail;
	
	private String reference;
	private String otherReference;
	
	private String madeBy;
	
	private String madeFor;
	
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getMadeFor() {
		return madeFor;
	}

	public void setMadeFor(String madeFor) {
		this.madeFor = madeFor;
	}

	public String getMadeBy() {
		return madeBy;
	}

	public void setMadeBy(String madeBy) {
		this.madeBy = madeBy;
	}

	@ReadOnly(notForViews="payBill")
	private String name;
	
	@ReadOnly(notForViews="payBill")
	@OnChange(forViews="payBill",value=LoadCustomerAction.class)
	private String email;
	
	@ReadOnly(notForViews="payBill")
	private String phoneNumber;
	
	@DisplaySize(20)
	private String SID;
	
	@Editor("ValidValuesRadioButton")
	@OnChange(SetMeansOfPaymentAction.class)
	private MeansOfPayment meansOfPayment;
	
	@Editor("BoldLabel")
	@LabelFormat(LabelFormatType.NO_LABEL)
	private String means;
	
	@LabelFormat(LabelFormatType.NO_LABEL)
	@DisplaySize(15)
	private String meansValue;
	
	@ManyToOne
	@DescriptionsList
	@NoCreate
	@NoModify
	private Bank bank;
	
	

	
	@XmlElement(name="Biller")
	public String getCollectionBiller(){
		return (getBiller()==null?"LASG":getBiller().getName());
	}
	
	
	
	@DescriptionsList
	@ManyToOne
	@NoCreate
	@NoModify
	@OnChange(ShowSIDAction.class)
	private Biller biller;
	
	@DescriptionsList(depends="collectionBiller",condition="${biller.name}=?")
	@ManyToOne
	@NoCreate
	@NoModify
	@OnChange(SetItemIdentifierLabelAction.class)
	private CollectionItem item;
	
	
	@Editor("BoldLabel")
	@LabelFormat(LabelFormatType.NO_LABEL)
	private String identifier;
	
	@LabelFormat(LabelFormatType.NO_LABEL)
	@DisplaySize(15)
	private String value;
	@ReadOnly
	private BigDecimal amount;
	
	
	@Transient
	@ReadOnly
	@ListAction("MakePayment.repay")
	@ListProperties("biller.name,item.name,identifier,value,amount")
	public Collection<MakePayment> getPreviousPayment(){
		List<MakePayment> payments = XPersistence.getManager().createQuery("FROM MakePayment m WHERE m.madeBy='" + Users.getCurrent()+"'")
										.getResultList();
		return payments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Biller getBiller() {
		return biller;
	}

	public void setBiller(Biller biller) {
		this.biller = biller;
	}

	public CollectionItem getItem() {
		return item;
	}

	public void setItem(CollectionItem item) {
		this.item = item;
	}
	
	@Transient
	@XmlElement(name="CollectionItem")
	public String getCollectionItem(){
		return "item";
	}

	@XmlElement(name="Identifier")
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@XmlElement(name="Value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlElement(name="Amount")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public MeansOfPayment getMeansOfPayment() {
		return meansOfPayment;
	}

	public void setMeansOfPayment(MeansOfPayment meansOfPayment) {
		this.meansOfPayment = meansOfPayment;
	}

	public String getMeans() {
		return means;
	}

	public void setMeans(String means) {
		this.means = means;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMeansValue() {
		return meansValue;
	}

	public void setMeansValue(String meansValue) {
		this.meansValue = meansValue;
	}

	@XmlElement(name="StateIdentificationNumber")
	public String getSID() {
		return SID;
	}

	public void setSID(String sID) {
		SID = sID;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getOtherReference() {
		return otherReference;
	}

	public void setOtherReference(String otherReference) {
		this.otherReference = otherReference;
	}
	
	@PreCreate
	public void preCreate(){
		setReference(RandomStringUtils.randomAlphanumeric(15));
	}

	@XmlElement(name="CustomerId")
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public PaymentOption getPaymentOption() {
		return paymentOption;
	}

	public void setPaymentOption(PaymentOption paymentOption) {
		this.paymentOption = paymentOption;
	}
}
