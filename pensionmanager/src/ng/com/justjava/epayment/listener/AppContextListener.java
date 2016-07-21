package ng.com.justjava.epayment.listener;

import java.text.*;

import javax.servlet.*;

import org.apache.cxf.endpoint.*;
import org.apache.cxf.jaxrs.*;
import org.apache.cxf.jaxrs.lifecycle.*;
import org.quartz.*;

import ng.com.justjava.epayment.utility.*;
import ng.com.justjava.test.*;

public class AppContextListener implements ServletContextListener{

	Server server = null;
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
		System.out.println(" Destroying Here....................\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		try {
			server.stop();
			server.destroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(" Starting Here....................\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+
				" context path =="+ arg0.getServletContext().getContextPath() + " Real Path "
						+ "" + arg0.getServletContext().getRealPath(""));
		
        try {

			JAXRSServerFactoryBean  sf = new JAXRSServerFactoryBean();
			sf.setResourceClasses(TestWebService.class);
			sf.setResourceProvider(TestWebService.class, 
			    new SingletonResourceProvider(new TestWebService()));
			String url = org.openxava.util.XavaPreferences.getInstance().getDeployedUrl();
			
			System.out.println(" from the properties file=="+url);			
			//sf.setAddress(decode);
			 
			sf.setAddress(url);
			server = sf.create();
			//server.start();
			System.out.println(" After Starting.......");
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
		CronJob cronJob = new CronJob();
		try {
			cronJob.startSchedule(null);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
