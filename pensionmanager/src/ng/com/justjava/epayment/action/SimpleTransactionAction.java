package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.joda.time.*;
import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class SimpleTransactionAction extends SearchByViewKeyAction { // 1

	public void execute() throws Exception {

		Map key = (Map) getView().getKeyValues();
		Transaction trans = (Transaction) MapFacade.findEntity("Transaction", key);
		
		CorporateUser corporateUser = UserManager.getLoginCorporateUser();
		if(corporateUser == null){
			addError("User not allowed to view transaction", null);
			return;
		}
		// Object payItem =
		// (Object)XPersistence.getManager().find(Class.forName(trans.getRequestClassName()),
		// trans.getRequestId());

		Map mainkey = new HashMap();
		mainkey.put("id", trans.getModelId());

		String modelName = trans.getModelName().replaceAll(
				"ng.com.justjava.epayment.model.", "");
		
		showDialog();
		getView().setModelName(modelName); // 2
		getView().setValues(mainkey); // 3
   
		getView().setEditable("batchNumber", false);
		getView().findObject(); // 4
		addActions("Return.return");
	}
}