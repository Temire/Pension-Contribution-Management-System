package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

public class RefreshRSAHolderAction extends ViewBaseAction implements IChainAction{

	public void execute() throws Exception {
		
		
		Map keys = getView().getKeyValues();
		if(keys.isEmpty() || keys.get("id")==null)
			return;
		
		RSAHolder holder = (RSAHolder) MapFacade.findEntity("RSAHolder", keys);
		
		Collection<PayItemCollection> items = getItems(holder.getCorporate());
		
		
		holder.setItems(items);
		XPersistence.getManager().merge(holder);
		//XPersistence.commit();
		addMessage("RSAHolder Successfully Refreshed", null);
		getView().recalculateProperties();
		getView().refreshCollections();
		getView().refresh();
		System.out.println(" The refresh is done here.................." + holder.getFirstName());
	}

	public String getNextAction() throws Exception {
		return getEnvironment().getValue("XAVA_SEARCH_ACTION");	
	}
	
	private Collection<PayItemCollection> getItems(Corporate corporate){
		List<CompanyPayItemCollection> payitems = (List<CompanyPayItemCollection>) corporate.getItems();
		Collection<PayItemCollection> items = new ArrayList<PayItemCollection>();
		for (CompanyPayItemCollection payItem : payitems) {
			System.out.println(" The Items ===" + payItem.getName());
			PayItemCollection payItemCollection = new PayItemCollection();
			payItemCollection.setCode(payItem.getCode());
			payItemCollection.setName(payItem.getName());
			payItemCollection.setActive(payItem.isActive());
			items.add(payItemCollection);
		}
		return items;
	}

}
