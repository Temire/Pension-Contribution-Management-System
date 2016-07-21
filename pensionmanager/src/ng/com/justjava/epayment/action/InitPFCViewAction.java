package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.apache.commons.collections.map.*;
import org.openxava.actions.*;
import org.openxava.util.*;

public class InitPFCViewAction extends ViewBaseAction {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		int month = Dates.createCurrent().getMonth();
		Users.getCurrentPreferences().put("month", String.valueOf(month));

		PensionFundCustodian pfc = UserManager.getPFCOfLoginUser();
		String condition = "${custodian.id}="+(pfc!=null?pfc.getId():0);
		
		System.out.println(" The Investigating Pension Custodian ==="+condition +
				" the month here =="+Users.getCurrentPreferences().get("month", String.valueOf(2))
				+ "  month actually=="+month);
		getView().getSubview("administrators").getCollectionTab().setBaseCondition(condition);;
		getView().getSubview("administrators").refresh();
		
	}

}
