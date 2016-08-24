package ng.com.justjava.epayment.utility;

import java.text.*;
import java.util.*;

import javax.persistence.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.MonthlyUpload.Status;
import ng.com.justjava.security.*;

import org.openxava.jpa.*;
import org.quartz.*;
import org.quartz.impl.*;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

public class CronJob {

	Scheduler scheduler = null;
	//JobDetail jobC = null;
	//CronTrigger cronTriggerC = null;
    public void startSchedule(String[] args) throws ParseException,SchedulerException {

/*		System.setProperty( "javax.net.ssl.trustStore", "C:/Java/jre7/lib/security/cacerts" );
		System.setProperty( "javax.net.ssl.trustStorePassword", "changeit" );
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		*/
    	
        scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobA = newJob(SendForPayment.class)
            .withIdentity("cronJob", "sendForPayment") 
            .build();
   
        JobDetail jobB = newJob(UpdatePaymentStatus.class)
                .withIdentity("cronJobB", "updatePaymentStatus") 
                .build();  
        
        JobDetail jobC = newJob(VerifyAccount.class)
                .withIdentity("cronJobC", "verifyAccount") 
                .build();
        
        JobDetail jobD = newJob(MailNotification.class)
                .withIdentity("cronJobD", "mailNotification") 
                .build();        
        
        String startDateStr = "2013-09-27 00:00:00.0";
        String endDateStr = "2019-09-31 00:00:00.0";

  
        
        
        Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(startDateStr);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(endDateStr);
        
      System.out.println(" Here is calling the startDateStr==" + startDate);
        
        System.out.println(" Here is calling the endDateStr==" + endDate);
        CronTrigger cronTriggerA = newTrigger()
          .withIdentity("triggerA", "sendForPayment")
          .startAt(startDate)
          .withSchedule(CronScheduleBuilder.cronSchedule("0/45 * * * * ?").withMisfireHandlingInstructionDoNothing())
          .endAt(endDate)
          .build();
        
        
        
        
        CronTrigger cronTriggerB = newTrigger()
                .withIdentity("triggerB", "updatePaymentStatus")
                .startAt(startDate)
                .withSchedule(CronScheduleBuilder.cronSchedule("0/60 * * * * ?").withMisfireHandlingInstructionDoNothing())
                .endAt(endDate)
                .build();    
        
        CronTrigger cronTriggerC = newTrigger()
                .withIdentity("triggerC", "verifyAccount")
                .startAt(startDate)
                .withSchedule(CronScheduleBuilder.cronSchedule("0/45 * * * * ?").withMisfireHandlingInstructionDoNothing())
                .endAt(endDate)
                .build();
        
        CronTrigger cronTriggerD = newTrigger()
                .withIdentity("triggerD", "mailNotification")
                .startAt(startDate)
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?").withMisfireHandlingInstructionDoNothing())
                .endAt(endDate)
                .build();        

        scheduler.scheduleJob(jobA, cronTriggerA);
        scheduler.scheduleJob(jobB, cronTriggerB);
        scheduler.scheduleJob(jobC, cronTriggerC);
        scheduler.scheduleJob(jobD, cronTriggerD);
        scheduler.start();
        
       
    }    

    public static class UpdatePaymentStatus implements Job {
    	public void execute(JobExecutionContext context) throws JobExecutionException {
    		
    		
    		System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
    				+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n Running Payment Status ");
    		
        	String sql = "FROM PaymentLog p where p.responseCode='N'";
        	List<PaymentLog> logs = XPersistence.getManager().createQuery(sql).
        			setMaxResults(500).getResultList();   
        	System.out.println(" UpdatePaymentStatus able to load total of  "+logs.size());
        	for (PaymentLog log : logs) {
        		log.setResponseCode(log.updateStatus()?"0":"Q");
        		XPersistence.getManager().merge(log);
        		
			}
        	XPersistence.commit();
        	XPersistence.getManager().flush();
    	}
    }
    
    public static class SendForPayment implements Job {
        //@Override
        public void execute(JobExecutionContext context) throws JobExecutionException {

/*            FundGateImplService service = new FundGateImplService();
            FundGate port = service.getFundGateImplPort();
            FundRequest request = new FundRequest();
*/
        	XPersistence.getManager().flush();
        	System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n Running Payment for Payment");
        	String sql = "FROM MonthlyUpload m where m.status=2";
        	List<MonthlyUpload> uploads = XPersistence.getManager().createQuery(sql).getResultList();
        	
        	System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
        			+ " Running Payment for Payment List Size"
        			+ uploads.size());
        	for(MonthlyUpload upload:uploads){
        		try {
        			boolean succeed = upload.pay();
					if(!succeed){
						upload.setStatus(Status.errorSending);
						XPersistence.getManager().merge(upload);
					}
					XPersistence.commit();
					XPersistence.getManager().flush();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	XPersistence.getManager().flush();
        }        
    }
    
    
     
    public static class VerifyAccount implements Job {
    	public void execute(JobExecutionContext context) throws JobExecutionException {
    		
    		
 /*   		System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n Running Verification ");
    		
    		
        	String sql = "from Account a where a.verify=0";
        	List<Account> accounts = XPersistence.getManager().createQuery(sql).
        			setMaxResults(20).getResultList();   
        	for (Account account : accounts) {
        		account.updateVerifiedName();
			}
        	XPersistence.getManager().flush();
        	
        	String ql = "from TransitAccount t";
        	List<TransitAccount> transitAccounts = XPersistence.getManager().createQuery(ql).
        			setMaxResults(20).getResultList();   
        	for (TransitAccount transitAccount : transitAccounts) {
        		transitAccount.updateCurrentBalance();
			}
        	XPersistence.getManager().flush();
*/         	
    	}
   } 
    public static class MailNotification implements Job {
    	public void execute(JobExecutionContext context) throws JobExecutionException {
    		
    		
    		System.out.println(" \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n Running Verification ");
    		
    		
        	String sql = "from MailManager m where m.sent=0";
        	List<MailManager> mails = XPersistence.getManager().createQuery(sql).
        			setMaxResults(20).getResultList();   
        	for (MailManager mail : mails) {
        		mail.reSend();
			}
    	}
    }    

}
