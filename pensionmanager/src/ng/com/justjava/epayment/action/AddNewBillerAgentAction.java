package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

public class AddNewBillerAgentAction  extends CreateNewElementInCollectionAction{
	public void execute() throws Exception {
		super.execute();
		
		System.out.println(" The Parent Value =====" + getCollectionElementView().getParent().getAllValues());
		
		String condition="${biller.id}=0";
		Map biller = getCollectionElementView().getParent().getAllValues();
		if(biller !=null){
			condition = "${biller.id}="+((Long)biller.get("id"));
		}
		
		System.out.println(" The condition afterall=====" + condition);
		
		getCollectionElementView().getSubview("attachCollectionItem").getCollectionTab().setBaseCondition(condition);
		//getCollectionElementView().getSubview("profiles").refreshCollections();
		getCollectionElementView().getSubview("attachCollectionItem").collectionDeselectAll();
	}
}
