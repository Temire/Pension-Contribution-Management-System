package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

public class AddRoleAction  extends GoAddElementsToCollectionAction {

	@Override
	public void execute() throws Exception {
		super.execute();
		// TODO Auto-generated method stub
		getTab().setBaseCondition("${name}<>'admin' OR ${name}<>'corporateAdmin'");
	}
	
	public String getNextController(){
		return "AddRole";
	}
}