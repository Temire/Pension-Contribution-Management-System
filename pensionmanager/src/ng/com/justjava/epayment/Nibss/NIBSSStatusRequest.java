package ng.com.justjava.epayment.Nibss;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="StatusRequest")
@XmlType(propOrder={"header", "hashValue"})
public class NIBSSStatusRequest {
	
	
	private Header header;


	private String hashValue;
	
	@XmlElement(name="Header")
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	@XmlElement(name="HashValue")
	public String getHashValue() {
		return hashValue;
	}

	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}
}
