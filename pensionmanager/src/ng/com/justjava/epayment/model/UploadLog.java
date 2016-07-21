package ng.com.justjava.epayment.model;

import java.sql.*;
import java.text.*;
import java.util.Date;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
public class UploadLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	private String remark;
	
	private Date dateTimeLog;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDateTimeLog() {
		return dateTimeLog;
	}

	public void setDateTimeLog(Date dateTimeLog) {
		this.dateTimeLog = dateTimeLog;
	}
	
	@PreCreate
	public void preCreate(){
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd 'T' HH:mm:ss.SSSZ");
		try {
			setDateTimeLog(format.parse(String.valueOf(new Date())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
