package ng.com.justjava.epayment.action;

import java.util.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.model.ReportController.*;
import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.ReportControllerPFA.Format;
import ng.com.justjava.epayment.model.ReportControllerPFA.Report;
import ng.com.justjava.epayment.model.ReportControllerPFA.Months;
import ng.com.justjava.epayment.utility.UserManager;

import org.apache.commons.collections.*;
import org.apache.cxf.transport.http.netty.server.util.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.MapFacade;
import org.openxava.util.Is;

import com.openxava.naviox.model.User;

public class ReportActionPFA extends JasperReportBaseAction implements
		IChangeControllersAction {

	Report report = null;
	Format format = null;
	int temCount = 0;
	Months toMonth = null;
	Months fromMonth = null;

	String reportName = null;

	Collection dataSource = null;

	Status status = null;
	Map corporateKeyValue = new HashMap();
	Map custodianKeyValue = new HashMap();
	Map administratorKeyValue = new HashMap();
	// Corporate corporate = new Corporate();

	Map yearFromKeyValue = new HashMap();
	Map yearToKeyValue = new HashMap();

	String getAccountName = null;
	String getAccountSort = null;

	@Override
	public String getFormat() throws Exception {
		// TODO Auto-generated method stub
		String result = "pdf";

		if (!Is.empty(format)) {
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
		// parameters.put("corporate", corporateKeyValue.get("id"));
		parameters.put("userCompany", UserManager.getPFCOfLoginUser());

		if (getAccountName != null) {
			parameters.put("companyAccount", getAccountName);
			parameters.put("companyAccountSort", getAccountSort);

		} else {
			parameters.put("companyAccount", "None");
			parameters.put("companyAccountSort", "None");
		}

		// parameters.put("rc", corporate.getRcNo());

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
		format = (Format) getView().getValue("format");
		fromMonth = (Months) getView().getValue("from");
		toMonth = (Months) getView().getValue("to");
		yearFromKeyValue = (Map) getView().getValue("fromyear");
		yearToKeyValue = (Map) getView().getValue("toyear");

		if (Is.empty(report)||Is.empty(format)||Is.empty(fromMonth)||Is.empty(toMonth)||Is.empty(yearToKeyValue)) {
			addError("Compulsory field must be selected");
			return;
		} 

		int toMonthId = (int) toMonth.ordinal() + 1;
		int fromMonthId = 0;

		System.out.println("Selected month is " + toMonth + "this is d number "
				+ report);

		int yearFromId = (int) yearFromKeyValue.get("year");
		System.out.println("The year selected value is " + yearFromId);
		int yearToId = 0;

		if (report.toString() == "RSAHolders") {
			fromMonthId = (int) fromMonth.ordinal() + 1;
			yearToKeyValue = (Map) getView().getValue("toyear");
			yearToId = (int) yearToKeyValue.get("year");
		}

		System.out.println("This is the toMonth oh " + toMonthId);
		System.out.println("This is the fromMonth oh " + fromMonthId);

		corporateKeyValue = (Map) getView().getValue("corporate");
		System.out.println("The corporate selected value is "
				+ corporateKeyValue);
		Long corporateId = 0L;
		if (corporateKeyValue != null)
			corporateId = (Long) corporateKeyValue.get("id");

		System.out.println("the corporateId is " + corporateId);
		// custodianKeyValue = (Map)getView().getValue("custodian");
		// Long custodianId = null;
		System.out.println("2 I am here ");

		PensionFundAdministrator pfa = UserManager.getPFAOfLoginUser();
		System.out.println("The pfa name is" + pfa.getName());
		System.out.println("The pfa id is " + pfa.getId());

		reportName = "rsaPFA.jrxml";
		System.out.println("=== RSAHolders === Report Action==" + dataSource
				+ " corporateId==" + corporateId + " pfa " + pfa.getId()
				+ " custodian===" + pfa.getCustodian().getId()
				+ " yearFromId==" + yearFromId + " yearFromId" + yearToId
				+ " fromMonthId===" + fromMonthId + " toMonthId==" + toMonthId);

		dataSource = RSAHolder.getAllRSAHoldersToFrom(corporateId, pfa.getId(),
				pfa.getCustodian().getId(), yearFromId, yearToId, fromMonthId,
				toMonthId);

		if (dataSource.isEmpty()) {
			addError("No data for the selected parameter");

		} else {
			System.out.println("=== RSAHolders === After Report Action== "
					+ "Report Action==" + dataSource);
			if (dataSource != null)
				System.out.println(" === RSAHolders === Report Action Size== "
						+ "dataSource size==" + dataSource.size());

			System.out.println("Test=============");

		}
		super.execute();
	}

}
