package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

public class LoadSystemWideSetupAction  extends ViewBaseAction {

	public void execute() throws Exception {
		try {

			if (getView().getModelName() != null && getView().getModelName().trim().equalsIgnoreCase("SystemWideSetup")) {


				SystemWideSetup setup = SystemWideSetup.getSystemWideSetup();
				
				if(setup==null)
					return;
				
				Map key = new HashMap();
				key.put("id", setup.getId());

				getView().setModelName("SystemWideSetup"); // 2
				getView().setValues(key); // 3
				getView().findObject(); // 4

				getView().setKeyEditable(false);
			}			// getView().setEditable(false);
		}catch (Exception ex) {
			ex.printStackTrace();
			addError("system_error");
		}
	}

}
