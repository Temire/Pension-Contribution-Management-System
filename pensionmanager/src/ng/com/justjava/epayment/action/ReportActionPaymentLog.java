package ng.com.justjava.epayment.action;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openxava.actions.IChangeControllersAction;
import org.openxava.actions.JasperReportBaseAction;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ng.com.justjava.epayment.model.Corporate;
import ng.com.justjava.epayment.model.RSAHolder;
import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.model.PaymentLog;
import ng.com.justjava.epayment.model.ReportControllerPaymentLog.*;
import ng.com.justjava.epayment.utility.UserManager;

public class ReportActionPaymentLog extends JasperReportBaseAction implements IChangeControllersAction{

	Report report = null;
	Format format = null;
//	Date fromDate = null;
//	Date toDate = null;
//	Months toMonth = null;
	
	String reportName = null;
	
	Collection dataSource = null;
	
	Status status = null;
	Map corporateKeyValue = new HashMap();
	//Map custodianKeyValue = new HashMap();
	//Map administratorKeyValue = new HashMap();
	Map monthKeyValue = new HashMap();
	Corporate corporate = new Corporate();
	
	Map yearKeyValue = new HashMap();
	
	@Override
	public String getFormat() throws Exception {
	
		// TODO Auto-generated method stub
		String result = "pdf";
		
		switch (format) {
		case Excel:
			result = "excel";
			break;
		case PDF:
			result = "pdf";
			break;
		default:
			break;
		}
		return result;
	}


	
	public String[] getNextControllers() throws Exception {
		// TODO Auto-generated method stub
		return PREVIOUS_CONTROLLERS;
	}

	@Override
	protected JRDataSource getDataSource() throws Exception {
		// TODO Auto-generated method stub
		return new JRBeanCollectionDataSource(dataSource);
	}

	@Override
	protected String getJRXML() throws Exception {
		return reportName;
	}



	@Override
	protected Map getParameters() throws Exception {
		// TODO Auto-generated method stub
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		//RSAHolder holder = new RSAHolder();
		Map parameters = new HashMap<String, String>();
		parameters.put("corporate", "The Corporate");
		//parameters.put("rc", corporate.getRcNo());
		parameters.put("userCompany", corporate.getName());
		//parameters.put("month", "March");
		return parameters;
	}
	
	public void execute() throws Exception {
		System.out.println("====== Executing here=====");
		
		 report = (Report) getView().getValue("report");
		 format  = (Format)getView().getValue("format");
//		 fromDate  = (Date)getView().getValue("from");
//		 toDate  = (Date)getView().getValue("to");
//		 
//		 toMonth = (Months)getView().getValue("month");
//		 int toMonthId = (int) toMonth.ordinal();
		 
		 //toMonth.toString();
		 
		 Corporate corporate = UserManager.getCorporateOfLoginUser(); 
		
				reportName ="payment_log_report.jrxml";
				System.out.println("=== PaymentLog === Report Action=="
						+ dataSource);		
				
					dataSource = PaymentLog.getPaymentLog();
					
					//corporateId = corporate.getId();
				
						

				System.out.println("=== Payment Log === After Report Action== "
						+ "Report Action=="+ dataSource);
				if(dataSource!= null)
					System.out.println(" === Payment Log === Report Action Size== "
							+ "dataSource size=="+ dataSource.size());
	
	
		
		System.out.println("Test=============");
		/*if(dataSource == null){
			addMessage("No data for the selected parameter");
			return;
			}*/
		super.execute();
	}
	
}