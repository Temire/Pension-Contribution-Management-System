package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;

public class InitCalculatorAction extends ViewBaseAction {

	public void execute() throws Exception {
		//System.out.println("");
		Collection<PayItem> originalItems = XPersistence.getManager().
				createQuery("FROM PayItem p").getResultList();
		
		List<CalculatorPayItems> items = new ArrayList<CalculatorPayItems>();
		for (PayItem originalItem : originalItems) {
			CalculatorPayItems payItemCollection = new CalculatorPayItems();
			payItemCollection.setName(originalItem.getName());
			if(originalItem.isCompulsory()){
				payItemCollection.setActive(true);
			}		
			items.add(payItemCollection);
		}
		SystemWideSetup setup = SystemWideSetup.getSystemWideSetup();
		
		PensionCalculator calculator = new PensionCalculator();
		calculator.setId(1L);
		calculator.setItems(items);
		calculator.setEmployeeRate(setup.getPensionSystemSetup().getEmployeeContributionPercent());
		calculator.setEmployerRate(setup.getPensionSystemSetup().getEmployerContributionPercent());
		calculator = XPersistence.getManager().merge(calculator);
		getView().setModel(calculator);

		//getView().recalculateProperties();
		
	}
		
}