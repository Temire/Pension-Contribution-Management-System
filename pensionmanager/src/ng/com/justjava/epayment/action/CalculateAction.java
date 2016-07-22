package ng.com.justjava.epayment.action;

import java.math.*;
import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;

public class CalculateAction extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Double employeePercent = (Double) getView().getValue("employeeRate");
		Double employerPercent = (Double) getView().getValue("employerRate");
		
		
		
		SystemWideSetup setup = SystemWideSetup.getSystemWideSetup();
		PensionSystemSetup pensionSetup = setup.getPensionSystemSetup();
		
		System.out.println("Employer Contribution Can not be less than ==" + 
		pensionSetup.getEmployerContributionPercent() + " employerPercent =="+ employerPercent+"  booolean  "+
		(pensionSetup != null && (pensionSetup.getEmployerContributionPercent() < employerPercent )));
		
		if(pensionSetup != null && (pensionSetup.getEmployerContributionPercent() > employerPercent )){
			addError("Employer Contribution Can not be less than " + pensionSetup.getEmployerContributionPercent(), null);
			return;
		}
		BigDecimal voluntary = (BigDecimal) (getView().getValue("voluntaryContribution") 
									== null?new BigDecimal(0.00):getView().getValue("voluntaryContribution"));
		//List<CalculatorPayItems> items = getView().getSubview("items").getCollectionObjects();
		
		System.out.println(" employeePercent,employerPercent values===============" + employeePercent+" ,"+employerPercent);
		
		
		List<Map> itemsMap = (List<Map>) getView().getValues().get("items");
		
		
		BigDecimal gross = new BigDecimal(0.00);
		

		

		//System.out.println(" Calculating the gross payitems count " +(payItemss==null?0:payItemss.size()));
		for (Map item : itemsMap) {
			if(Boolean.valueOf((Boolean) (item.get("active")==null?false:item.get("active"))) && item.get("amount")!=null){
				gross = gross.add(((BigDecimal) item.get("amount")!=null?(BigDecimal) item.get("amount"):new BigDecimal(0.00)));
			}
		}
		double doubleGross = gross.doubleValue();
		doubleGross = doubleGross/12.0;
		gross = new BigDecimal(doubleGross);
		gross =gross.setScale(2,RoundingMode.CEILING);
		System.out.println(" Reached 1");
		double employerPercentageContri = employerPercent/100;
		BigDecimal employerContri= gross.multiply(new BigDecimal(employerPercentageContri));
		employerContri =employerContri.setScale(2,RoundingMode.CEILING);
		//employerContri = employerContri.divide(new BigDecimal(100.00));
		System.out.println(" Reached 2");
		double employeePercentageContri = employeePercent/100;
		BigDecimal employeeContri= gross.multiply(new BigDecimal(employeePercentageContri));
		employeeContri = employeeContri.add(voluntary==null? new BigDecimal(0.00):voluntary);
		employeeContri =employeeContri.setScale(2,RoundingMode.CEILING);
		
		System.out.println(" Reached 3");
		BigDecimal result = employeeContri.add(employerContri);
		result =result.setScale(2,RoundingMode.CEILING);
		System.out.println(" The result being sent here === "+result);
		


		addMessage("Employer Contribution is: N"+employerContri, null);
		addMessage("Employee Contribution is: N"+employeeContri, null);
		addMessage(" The Pension Amount Due Is: N" + result, null);
		System.out.println(" The Summation Here ===============" + result);
	}

}
