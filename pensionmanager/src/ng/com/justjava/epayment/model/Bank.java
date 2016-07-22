package ng.com.justjava.epayment.model;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;

import com.googlecode.jcsv.*;
import com.googlecode.jcsv.annotations.*;
import com.googlecode.jcsv.annotations.internal.*;
import com.googlecode.jcsv.reader.*;
import com.googlecode.jcsv.reader.internal.*;

@Entity
@Tab(properties="code,name",baseCondition = "${deleted}=0")
public class Bank {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;

	
	@MapToColumn(columnName="name")
	private String name;
	@MapToColumn(columnName="code")
	private String code;
	
	
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public static void saveUpload(Reader csvFile,CSVStrategy strategy,ValueProcessorProvider vpp){		
		try {

			CSVReaderBuilder<Bank> builder = new CSVReaderBuilder<Bank>(csvFile);

			builder.strategy(strategy);
			CSVReader<Bank> csvReader = builder.entryParser(	new AnnotationEntryParser<Bank>(
					Bank.class, vpp)).build();
			
			List<Bank> banks = csvReader.readAll();
			for (Bank bank : banks) {
				XPersistence.getManager().merge(bank);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
