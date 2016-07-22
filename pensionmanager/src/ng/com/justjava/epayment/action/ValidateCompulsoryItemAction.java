package ng.com.justjava.epayment.action;

import javax.inject.*;

import org.openxava.actions.*;
import org.openxava.controller.*;
import org.openxava.util.*;
import org.openxava.view.*;

public class ValidateCompulsoryItemAction extends OnChangePropertyBaseAction {
	@Inject @Named("xava_currentModule")
	private String currentModule;
	
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		//addWarning("Must be active", null);
		//getView().setEditable("code", false);
		System.out.println(" Sorry this payitem must be active............."+currentModule);
	}

}
