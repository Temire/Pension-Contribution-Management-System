package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;
import org.openxava.model.*;

public class SetItemIdentifierLabelAction extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map itemMap = (Map)getView().getValue("item");
		
		if(itemMap == null || itemMap.isEmpty() || itemMap.get("id") == null)
			return;
		
		CollectionItem item = (CollectionItem) MapFacade.findEntity("CollectionItem", itemMap);
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n The item identifier === ==" + item.getIdentifier());
		
		
		getView().setValue("identifier", item.getIdentifier().getName());
		getView().setValue("amount", item.getExpectedAmount());
		getView().setHidden("identifier", false);
		getView().setHidden("value", false);

		System.out.println(" The item picked ==" + item.getName() + " the identifier===" + item.getIdentifier().getName());
	}

}
