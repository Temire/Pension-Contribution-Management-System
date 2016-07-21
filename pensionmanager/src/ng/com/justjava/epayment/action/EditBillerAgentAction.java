package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.apache.commons.collections.*;
import org.openxava.actions.*;
import org.openxava.model.*;

import com.openxava.naviox.model.*;

public class EditBillerAgentAction  extends  EditElementInCollectionAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		super.execute();
		try {
			
			Agent agent =(Agent) MapFacade.findEntity("Agent", 
					getCollectionElementView().getKeyValues());
			String condition="${biller.id}="+agent.getBiller().getId();
			 
			System.out.println(" The condition I am testing here ==========" + condition);
			
			
			getCollectionElementView().getSubview("attachCollectionItem").getCollectionTab().setBaseCondition(condition);
			getCollectionElementView().getSubview("attachCollectionItem").refreshCollections();
			List<CollectionItem> items = getCollectionElementView().getSubview("attachCollectionItem").getCollectionObjects();
			


			Collection<CollectionItem> intersection = CollectionUtils.intersection(agent.getColletionItems(), items);
			Map[] keys =  new HashMap[intersection.size()];
			
			int count = 0;
			for (CollectionItem item:items) {
				if(intersection.contains(item)){
					Map profileKey = new HashMap();
					profileKey.put("id", item.getId());
					keys[count]=profileKey;
					count = count + 1;
				}

			}
			
			
			getCollectionElementView().getSubview("attachCollectionItem").getCollectionTab().setAllSelectedKeys(keys);
			resetDescriptionsCache();
			System.out.println(" Here we are at last element size " + keys.length + " while the whole object is "+
			getCollectionElementView().getAllValues());
						
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//List<Profile> selectedObjects = (List<Profile>) 

	}

}
