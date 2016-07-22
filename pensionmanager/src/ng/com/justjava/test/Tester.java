package ng.com.justjava.test;

import java.util.*;

import ng.com.justjava.epayment.utility.*;

import org.apache.commons.collections.*;
import org.apache.commons.lang.math.*;
import org.apache.commons.lang3.*;
import org.apache.cxf.endpoint.*;
import org.apache.cxf.jaxrs.*;
import org.apache.cxf.jaxrs.lifecycle.*;

//import com.nbiss.main.*;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			try {
				
/*				System.out.println("Key: " + Cryptor.generateKey());
				String pageName = "menu";
				String jsp = pageName + "\".jsp\"";
				System.out.println(" The Long ==" + RandomUtils.nextInt() + " The JSP==" + jsp);
				*/
	/*			ArrayList<String> str = new ArrayList<>();
				str.add("One");
				str.add("Two");
				str.add("Three");
				System.out.println(CollectionUtils.get(str, 0));*/
				
				System.out.println(getCheckSum("10.00", "0000000001", 
						"1090987878888", "http://localhost:8080/pensionmanager/testing.jsp", 
						"DEMO_KEY"));
				
/*				
		            JAXRSServerFactoryBean  sf = new JAXRSServerFactoryBean();
		            sf.setResourceClasses(TestWebService.class);
		            sf.setResourceProvider(TestWebService.class, 
		                new SingletonResourceProvider(new TestWebService()));
		            sf.setAddress("http://localhost:9999/calcrest/");
		            Server server = sf.create();*/
		            
				/*NiBss nibss = NiBss.getInstance("");*/
				//String response= nibss.uploadPaymentSchedule("");
				
				
/*				SmsSender sender = new SmsSender();
				sender.sendSMS("1222222", "2347062023181", "Just Testing");*/
				
				//System.out.println("  The Response Here===" + RandomStringUtils.randomAlphanumeric(15));
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getCheckSum(String amount, String terminalId, String transactionId, String responseUrl, String secretKey) { 
        String checkSum = ""; 
        String combination = amount + terminalId + transactionId + responseUrl + secretKey; 
        try { 
            java.security.MessageDigest mdEnc = java.security.MessageDigest.getInstance("MD5"); // Encryption algorithm 
            mdEnc.update(combination.getBytes(), 0, combination.length()); 
            checkSum = new java.math.BigInteger(1, mdEnc.digest()).toString(16); 
   System.out.println("CHECKSUM is "+checkSum); 
        } catch (Exception e) { 
        } 
        return checkSum; 
    } 	

}
