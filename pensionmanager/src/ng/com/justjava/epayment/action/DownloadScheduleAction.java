package ng.com.justjava.epayment.action;

import java.util.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import ng.com.justjava.epayment.model.DownloadSchedule.Format;
import ng.com.justjava.epayment.model.DownloadSchedule.Months;
import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.model.PaymentLog;
import ng.com.justjava.epayment.model.PensionFundAdministrator;
import ng.com.justjava.epayment.model.RSAHolder;
import ng.com.justjava.epayment.model.TransitAccount;
import ng.com.justjava.epayment.utility.UserManager;

import org.apache.commons.collections.*;
import org.openxava.actions.*;
import org.openxava.util.*;




public class DownloadScheduleAction extends JasperReportBaseAction implements IChangeControllersAction{

	//Report report = null;
	Format format = null;
	int temCount=0;
	//Months month = null;
	//Months fromMonth = null;
	Months month = null;
	String reportName = null;
	
	Collection dataSource = null;
	
	Status status = null;
	Map corporateKeyValue = new HashMap();
	//Map custodianKeyValue = new HashMap();
	Map administratorKeyValue = new HashMap();
	//Corporate corporate = new Corporate();

	Map yearKeyValue = new HashMap();
	//Map yearToKeyValue = new HashMap();
	
	String getAccountName = null;
	String getAccountSort = null;
	
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
		Map parameters = new HashMap<String, String>();
		//parameters.put("corporate", corporateKeyValue.get("id"));
		parameters.put("userCompany", UserManager.getPFCOfLoginUser());
		
		if(getAccountName != null){
			parameters.put("companyAccount", getAccountName );
			parameters.put("companyAccountSort", getAccountSort );
			
		}else{
			parameters.put("companyAccount", "None" );
			parameters.put("companyAccountSort", "None" );
		}
		
		//parameters.put("rc", corporate.getRcNo());
		
		return parameters;
	}
	
	@Override
	public boolean inNewWindow() {

		boolean result2 = true;
		
		System.out.println("1 The datasource here ===="+dataSource);
		
		if(dataSource==null || dataSource.isEmpty()){
			result2 = false;
			System.out.println("2 The datasource here ===="+dataSource);
		}
		

		return result2;
	}
	
	@Override
	public String getForwardURI() {
		String result2 = "/xava/report.pdf?time=" + System.currentTimeMillis();
		if(dataSource==null || dataSource.isEmpty()){
			result2 = null;
	}

		return result2;
	}
	
	
	public void execute() throws Exception {
	
		// report = (Report) getView().getValue("report");
		 format  = (Format)getView().getValue("format");
		 month = (Months)getView().getValue("month");
		 
		 yearKeyValue = (Map)getView().getValue("year");

		 if (Is.empty(format)||Is.empty(month)||Is.empty(yearKeyValue)) {
				addError("Compulsory field must be selected");
				return;
			}
		 
		 int monthId = (int) month.ordinal();

									
		corporateKeyValue = (Map)getView().getValue("corporate");
		System.out.println("The corporate selected value is "+corporateKeyValue);
		
		Long corporateId = 0L;
		if(corporateKeyValue != null){
		 corporateId = (Long) corporateKeyValue.get("id");}
		
		corporateId = (corporateId==null?0:corporateId);
		
		yearKeyValue = (Map)getView().getValue("year");
		System.out.println("The corporate selected value is "+yearKeyValue);
		
		Long yearId = 0L;
		if(yearKeyValue != null){
		 yearId = (Long) yearKeyValue.get("id");}
		
		yearId = (yearId==null?0:yearId);
		

		System.out.println("the corporateId is "+ corporateId);
		//custodianKeyValue = (Map)getView().getValue("custodian");
		//Long custodianId = null;
		System.out.println("2 I am here ");
		
		 PensionFundAdministrator pfa = UserManager.getPFAOfLoginUser();
		 System.out.println("The pfa name is"+pfa.getName());
		 System.out.println("The pfa id is "+pfa.getId());
		 
		 
				//reportName ="rsaholderNew.jrxml";	
		 
				reportName ="schedule.jrxml";	
				int tempMonth = monthId-1;
				
				
					System.out.println("1 It is after the return");
					
					//dataSource = RSAHolder.getAllRSAHoldersPFA(corporateId,pfa.getId(),pfa.getCustodian().getId(),monthId);
					
					dataSource = PaymentLog.getPaymentLog2(pfa.getId(),monthId,yearId,corporateId);
					if(dataSource.isEmpty()){
						//addMessage("No data for the selected parameter");
						addError("No data for the selected parameter");	
						//inNewWindow();
						return;
					}
					
					
					
					System.out.println("Troubleshoot  The DataSource ===="+dataSource.size());
					PaymentLog getLog = null;
					if(dataSource.size()>= 1)
						getLog = (PaymentLog)CollectionUtils.get(dataSource, 0);
					
					else{
						addError("No data for the selected parameter");	
						//inNewWindow();
						return;
					}
					System.out.println("Inside the case i got it "+getLog.getUpload().getCorporate().getName());
					TransitAccount getAccount = null;
					if(getLog != null && !Is.empty(getLog.getUpload().getCorporate().getTransitAccounts()))
						getAccount = (TransitAccount)CollectionUtils.get(getLog.getUpload().getCorporate().getTransitAccounts(), 0);
					else{
						addError("No Transit Account Already Selected");	
						//inNewWindow();
						return;						
					}
					System.out.println("Inside the accoubt stuff"+getAccount.getBank().getName());
					if(getAccount.getBank().getName()==null){
						getAccountName = "null";
						getAccountSort = "null";
					}else{
						getAccountName = getAccount.getBank().getName();
						getAccountSort = getAccount.getBank().getCode();
				
					}
				
					System.out.println("=== RSAHolders === After Report Action== "
							+ "Report Action=="+ dataSource);	
					
					//}
					
					super.execute();
	}

}
