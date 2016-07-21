package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class SaveCorporateAction extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		String uniqueIdentifier = getView().getValueString("uniqueIdentifier");
		
		System.out.println(" Entering SaveCorporateAction.........");
		
		Map allValues = getView().getAllValues();
		
		List<Map> items =  (List<Map>) allValues.get("items");
		
		List<Map> newItems =  new ArrayList<Map>();
		System.out.println("1 The Values in All ==="+ allValues + " items length" + items.size());
		for (Map map : items) {
			if(!Is.emptyString((String) map.get("code"))){
				newItems.add(map);
			}
			System.out.println(" item here == " + map);
		}
		

		allValues.put("items", newItems);
		System.out.println("2 The Values in All ==="+ allValues + " items length" + items.size());
		//addMessage("OKAY!!!", null);

		
		if(!Is.emptyString(uniqueIdentifier)){
			ArrayList<Corporate> corporate = (ArrayList<Corporate>) XPersistence.getManager().
					createQuery(" FROM Corporate c WHERE c.uniqueIdentifier='"+uniqueIdentifier+"'").getResultList();
			if(corporate != null && corporate.size()>0){
				
				
				System.out.println(" The allValues==" + allValues);
				
				if(allValues.containsKey("id") && allValues.get("id")!=null){
					MapFacade.setValues("Corporate", allValues, allValues);
				}else{
					addError("Corporate with uniqueIdentifier "+uniqueIdentifier + " Already Exist", null);
					return;
					//allValues.put("id", corporate.get(0).getId());
					//MapFacade.setValues("Corporate", allValues, allValues);
				}
					
			}else{
				System.out.println(" The full values =====" + allValues);
				MapFacade.create("Corporate", allValues);
			}
			addMessage("Corporate Successfully Saved");
		}else{
			addError("UniqueIdentifier Is Required", null);
			return;
		}
		
	}

}
