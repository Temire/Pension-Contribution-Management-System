package ng.com.justjava.epayment.model;

import java.io.*;
import java.util.*;

import javax.persistence.*;

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
@View(members="Account Detail [accountname;bank;accountnumber];amount;narration;beneficiaryId;email;ownerName")
@Tab(filter = PaymentInstructionFilter.class, 
rowStyles=@RowStyle(style="row-red", property="valid", value="inValid"),
properties = "narration,beneficiaryId,accountname,accountnumber,amount,email,ownerName,bank.name,sourceReference,valid", 
baseCondition = "${treated}=0 AND ${uploader} = ? AND ${deleted}=0")
public class PaymentUpload {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	private String uploader;
	
	private boolean treated;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	
	@MapToColumn(columnName="narration")
	private String narration;
	
	
	@MapToColumn(columnName="amount")
	private String amount;
	
	@MapToColumn(columnName="beneficiaryId")
	private String beneficiaryId;
	
	@MapToColumn(columnName="accountname")
	private String accountname;
	
	@MapToColumn(columnName="accountnumber")
	private String accountnumber;
	
	@MapToColumn(columnName="email")
	private String email;
	
	@MapToColumn(columnName="ownerName")
	private String ownerName;
	
	@MapToColumn(columnName="bank",type=Bank.class)
	@ManyToOne
	@DescriptionsList
	private Bank bank;
	
	@MapToColumn(columnName="sourceReference")
	private String sourceReference;
	

	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBeneficiaryId() {
		return beneficiaryId;
	}

	public void setBeneficiaryId(String beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public String getSourceReference() {
		return sourceReference;
	}

	public void setSourceReference(String sourceReference) {
		this.sourceReference = sourceReference;
	}
	
	public static String saveUpload(Reader csvFile, CSVStrategy strategy,ValueProcessorProvider vpp, Object extraParam){	
		
		String result = "File Loaded Successfully";

		try {
			CSVReaderBuilder<PaymentUpload> builder = new CSVReaderBuilder<PaymentUpload>(csvFile);
			builder.strategy(strategy);
			CSVReader<PaymentUpload> csvReader = builder.entryParser(
					new AnnotationEntryParser<PaymentUpload>(
							PaymentUpload.class, vpp)).build();
			List<PaymentUpload> paymentUploads = csvReader.readAll();
			for(PaymentUpload upload:paymentUploads){
				upload.setAccountname(Strings.change(upload.getAccountname(), "\"", ""));
				upload.setOwnerName(Strings.change(upload.getOwnerName(), "\"", ""));
				if(Users.getCurrent() == null)
					upload.setUploader("thirdPaty"); 
				else
					upload.setUploader(Users.getCurrent());
				
				XPersistence.getManager().merge(upload);
			}
			
			System.out.println(" Now Committing the transaction=====");
			
			XPersistence.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "Error Loading File";
		}
		return result;
		
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public boolean isTreated() {
		return treated;
	}

	public void setTreated(boolean treated) {
		this.treated = treated;
	}
	
	public String getValid(){
		 String valid  =  !Is.emptyString(narration,amount,accountname,email,accountnumber)?"Valid":"inValid";
		return valid;
	}
	
	
	@PreCreate
	public void preCreate(){
		setUploader(Users.getCurrent());
	}
}
