package ng.com.justjava.epayment.model;

import javax.persistence.*;

import org.openxava.annotations.*;

@Embeddable
public class Comment {
	
	@Stereotype("MEMO")
	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
