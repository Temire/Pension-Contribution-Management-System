package ng.com.justjava.epayment.action;

import java.util.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.model.ReportControllerPFC.*;
import ng.com.justjava.epayment.utility.UserManager;

import org.openxava.actions.*;
import org.openxava.model.MapFacade;
import org.openxava.util.Is;



public class ReportActionPFC extends JasperReportBaseAction implements IChangeControllersAction{

	Report report = null;
	Format format = null;
	Date fromDate = null;
	Date toDate = null;
	Months toMonth = null;
	
	String reportName = null;
	
	Collection dataSource = null;
	
	Status status = null;
	Map corporateKeyValue = new HashMap();
	Map custodianKeyValue = new HashMap();
	Map administratorKeyValue = new HashMap();
	//Corporate corporate = new Corporate();

	
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
		
		PensionFundCustodian custodian = UserManager.getPFCOfLoginUser();
		Map parameters = new HashMap<String, String>();
		//parameters.put("corporate", corporateKeyValue.get("id"));
		parameters.put("userCompany",custodian.getName() );
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
	
		 report = (Report) getView().getValue("report");
		 format  = (Format)getView().getValue("format");
		 toMonth = (Months)getView().getValue("month");
		 
		 
		 if (Is.empty(report)||Is.empty(format)||Is.empty(toMonth)) {
				addError("Compulsory field must be selected");
				return;
			} 
		 
		 int toMonthId = (int) toMonth.ordinal();
		 
		 
		 

		 
		corporateKeyValue = (Map)getView().getValue("corporate");
		Long corporateId = 0L;		 
		if ( corporateKeyValue != null){
			 corporateId = (Long)corporateKeyValue.get("id");
		}
		
		System.out.println("1 I am here ");
		
		

		System.out.println("2 I am here ");
		
		administratorKeyValue = (Map)getView().getValue("fundAdministrator");
		Long pfaId = 0L;		 
		if ( administratorKeyValue != null){
			 pfaId = (Long)administratorKeyValue.get("id");
		}
		
		
		System.out.println("3 I am here ");	
		
		PensionFundCustodian custodian = UserManager.getPFCOfLoginUser();
		
		 System.out.println("ReportName:=="+report+"format Selected===="+format+"FromDate==="+fromDate
				 +"ToDate==="+toDate+"corporate==="+corporateKeyValue);
		 
		 

		switch(report){
		
		
			case RSAHolders:
				reportName ="rsaholderPFC.jrxml";
				System.out.println("=== RSAHolders === Report Action=="
						+ dataSource);				
				dataSource = RSAHolder.getAllRSAHolders(corporateId,pfaId,custodian.getId(),toMonthId);
						

				System.out.println("=== RSAHolders === After Report Action== "
						+ "Report Action=="+ dataSource);
				if(dataSource.isEmpty()){
					addError("No data for the selected parameter");	
					break;
					
				}
			
			
			
			}
	
		
		System.out.println("Test=============");
		
		super.execute();
	}

}
