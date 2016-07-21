package ng.com.justjava.epayment.Nibss;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="VendorUploadResponse")
@XmlType(propOrder={"header", "vendor","hashValue"})
public class NibssAccountVerificationResponse {
	private Header header;
	private NibssVendor vendor;
	private String hashValue;
	
	@XmlElement(name="Header")
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	
	@XmlElement(name="Vendor")
	public NibssVendor getVendor() {
		return vendor;
	}
	public void setVendor(NibssVendor vendor) {
		this.vendor = vendor;
	}
	
	@XmlElement(name="HashValue")
	public String getHashValue() {
		return hashValue;
	}
	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}
}
