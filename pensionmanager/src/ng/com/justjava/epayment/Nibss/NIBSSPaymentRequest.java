package ng.com.justjava.epayment.Nibss;

import java.util.*;

import javax.xml.bind.annotation.*;

import ng.com.justjava.epayment.model.*;

@XmlRootElement(name="PaymentRequest")
@XmlAccessorType(XmlAccessType.PROPERTY)

public class NIBSSPaymentRequest {

	private Header header;
	

	private Collection<PaymentInstruction> paymentRecord;
	
	@XmlElement(name="Header")
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	@XmlElement(name="PaymentRecord")
	public Collection<PaymentInstruction> getPaymentRecord() {
		return paymentRecord;
	}

	public void setPaymentRecord(Collection<PaymentInstruction> paymentRecord) {
		this.paymentRecord = paymentRecord;
	}
}
