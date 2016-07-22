package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

public class AddNewPensionPayItem extends CreateNewElementInCollectionAction{
	public void execute() throws Exception {
		//getCollectionElementView().setViewName("forNew");
		super.execute();
		
/*		String condition="${universal}=1";
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		if(corporate !=null){
			condition = condition + " OR ${corporate.id}="+corporate.getId();
		}
				
		getCollectionElementView().getSubview("profiles").getCollectionTab().setBaseCondition(condition);
		//getCollectionElementView().getSubview("profiles").refreshCollections();
		getCollectionElementView().getSubview("profiles").collectionDeselectAll();*/
	}
}
