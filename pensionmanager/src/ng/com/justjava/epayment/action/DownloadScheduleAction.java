package ng.com.justjava.epayment.action;

import java.util.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import ng.com.justjava.epayment.model.DownloadSchedule.Format;
import ng.com.justjava.epayment.model.DownloadSchedule.Months;
import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.model.PensionFundAdministrator;
import ng.com.justjava.epayment.model.RSAHolder;
import ng.com.justjava.epayment.model.TransitAccount;
import ng.com.justjava.epayment.utility.UserManager;

import org.apache.commons.collections.*;
import org.openxava.actions.*;




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
		if(dataSource.isEmpty()){
			//addError("No data for the selected parameter");	
			result2 = false;
		}
		
		return result2;
	}
	
	@Override
	public String getForwardURI() {	
		String result2 = "/xava/report.pdf?time="+System.currentTimeMillis();
		if(dataSource.isEmpty()){
			//addError("No data for the selected parameter");	
			result2 = null;
		}
		
		return result2;
	}
	
	public void execute() throws Exception {
	
		// report = (Report) getView().getValue("report");
		 format  = (Format)getView().getValue("format");
		 month = (Months)getView().getValue("month");
		 //toMonth = (Months)getView().getValue("to");
		 
		 int monthId = (int) month.ordinal() + 1;
		// int fromMonthId = 0;
		 
		 //System.out.println("Selected month is "+toMonth+"this is d number "+report);
		 
		 yearKeyValue = (Map)getView().getValue("year");
		
		int yearId = (int) yearKeyValue.get("year");
		
							
		corporateKeyValue = (Map)getView().getValue("corporate");
		System.out.println("The corporate selected value is "+corporateKeyValue);
		Long corporateId = 0L;
		if(corporateKeyValue != null)
		 corporateId = (Long) corporateKeyValue.get("id");
		
		System.out.println("the corporateId is "+ corporateId);
		//custodianKeyValue = (Map)getView().getValue("custodian");
		//Long custodianId = null;
		System.out.println("2 I am here ");
		
		 PensionFundAdministrator pfa = UserManager.getPFAOfLoginUser();
		 System.out.println("The pfa name is"+pfa.getName());
		 System.out.println("The pfa id is "+pfa.getId());
		 
		 
				reportName ="rsaholderNew.jrxml";	

				int tempMonth = monthId-1;
				
				
					System.out.println("1 It is after the return");
					
					dataSource = RSAHolder.getAllRSAHoldersPFA(corporateId,pfa.getId(),pfa.getCustodian().getId(),monthId);
					if(dataSource.isEmpty()){
						//addMessage("No data for the selected parameter");
						addError("No data for the selected parameter");	
						//inNewWindow();
						return;
					}
					
					
					System.out.println("Troubleshoot  The DataSource ===="+dataSource);
					
					RSAHolder getRSA = (RSAHolder)CollectionUtils.get(dataSource, 0);
					
					
					System.out.println("Inside the case i got it"+getRSA.getCorporate().getName());
					TransitAccount getAccount = (TransitAccount)CollectionUtils.get(getRSA.getCorporate().getTransitAccounts(), 0);
					
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
