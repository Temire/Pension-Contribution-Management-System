package ng.com.justjava.epayment.action;

import java.util.*;

import org.openxava.actions.*;

public class GlobalSearchAction extends SearchByViewKeyAction {
	private boolean isApproveable() { // To see if this entity has a deleted
										// property
		return getView().getMetaModel().containsMetaProperty("enable");
	}

	protected Map getValuesFromView() // Gets the values displayed in this view
			throws Exception // These values are used as keys for the search
	{
		if (!isApproveable()) { // If not deletable we run the standard logic
			return super.getValuesFromView();
		}
		Map values = super.getValuesFromView();
		//values.put("enable", false); // We populate deleted property with false
		return values;
	}

	protected Map getMemberNames() // The members to be read from the entity
			throws Exception {
		if (!isApproveable()) { // If not deletable we run the standard logic
			return super.getMemberNames();
		}
		Map members = super.getMemberNames();
		members.put("enable", null); // We want to get the deleted property
										// from entity,
		return members;
	}

	protected void setValuesToView(Map values) // Assigns the values from the
			throws Exception // entity to the view
	{
		if (isApproveable() && // If it has an deleted property and
				(Boolean) values.get("enable")) { // it is true
			throw new Exception("Object Not Found"); // The same exception OpenXava
		} // throws when the object is not found
		else {
			super.setValuesToView(values); // Otherwise we run the standard
											// logic
		}
	}
}