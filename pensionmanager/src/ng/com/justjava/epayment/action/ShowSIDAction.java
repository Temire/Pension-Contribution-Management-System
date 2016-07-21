package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;
import org.openxava.model.*;

public class ShowSIDAction extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		getView().setHidden("SID", true);
		Map billerMap = (Map)getView().getAllValues().get("biller");
		if(billerMap !=null && !billerMap.isEmpty() && billerMap.get("id") != null  ){
			Biller biller = (Biller) MapFacade.findEntity("Biller", billerMap);
			getView().setValue("collectionBiller", biller.getName());
			if(biller.isSidRequired())
				getView().setHidden("SID", false);
			else
				getView().setHidden("SID", true);
		}
	}

}