package ng.com.justjava.test;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.openxava.jpa.*;
import org.openxava.util.*;

import ng.com.justjava.epayment.model.*;
 
/**
 * @author Crunchify.com
 * 
 */

public class CrunchifyJavaMailExample {
 
	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;
	//static SystemWideSetup setup;
 
	public static void main(String args[]) throws AddressException, MessagingException {
		//generateAndSendEmail();
		System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
	}
	
	private static void setupMailServerProperties(){
		System.out.println("\n 1st ===> setup Mail Server Properties..");
		
		//setup = SystemWideSetup.getSystemWideSetup();
		//EmailParameter mailParam = setup.getEmailParameter();
		XavaPreferences.getInstance().getDeployedUrl();
		
		
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", XavaPreferences.getInstance().getSMTPPort());
		mailServerProperties.put("mail.smtp.auth", XavaPreferences.getInstance().getSMTPAuth());
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		
		mailServerProperties.put("mail.smtp.ssl.trust", "smtp.office365.com");
		System.out.println("Mail Server Properties have been setup successfully..");
		
		//mailServerProperties.put("mail.smtp.auth", "true");
		// mailServerProperties.put("mail.smtp.starttls.enable", "true");
		
	
	}
 
	private static void setMailSession(String toAddress, String copyAddress, String content,String subject)
			throws AddressException, MessagingException{
		System.out.println("\n\n 2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		Address[] addresses = {(Address)new  InternetAddress(XavaPreferences.getInstance().getSMTPUserID())};
		String superAdminMail=XavaPreferences.getInstance().getXavaProperty("adminMail",
				"ekeh.chinwendu@cscsnigeriaplc.com");
		generateMailMessage.addFrom(addresses);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
		generateMailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(copyAddress));
		generateMailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(superAdminMail));
		generateMailMessage.setSubject(subject);
		String emailBody = content;
		generateMailMessage.setContent(emailBody, "text/html");
		System.out.println("Mail Session has been created successfully..");
	}
	
	private static boolean getSessionAndSendMail()  throws AddressException, MessagingException {
		boolean response = false;
		try {
			
			System.out.println("\n\n 3rd ===> Get Session and Send mail");
			Transport transport = getMailSession.getTransport("smtp");
			
			// Enter your correct gmail UserID and Password (XXXApp Shah@gmail.com)
			transport.connect(XavaPreferences.getInstance().getSMTPHost(), XavaPreferences.getInstance().getSMTPUserID(),
					XavaPreferences.getInstance().getSMTPUserPassword());
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();
			response = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(" Mail Related Issues=========");
			e.printStackTrace();
		}
		return response;
	}
	public static void generateAndSendEmail(String toAddress, String copyAddress, String content,String subject)
			throws AddressException, MessagingException {
		MailManager mailing = new MailManager();
		mailing.setContent(content);
		mailing.setSubject(subject);
		mailing.setToAddress(toAddress);
		//mailing = XPersistence.getManager().merge(mailing);
		setupMailServerProperties();
		setMailSession(toAddress, copyAddress, content, subject);
		boolean sent = getSessionAndSendMail();
		if(sent){
			mailing.setSent(true);
			//XPersistence.getManager().merge(mailing);
		}
		//XPersistence.commit();
	}
}