package ng.com.justjava.epayment.action;

import java.util.*;

import javax.ejb.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Payment.Status;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

public class BatchSaveAction extends ViewBaseAction {
	
private boolean resetAfter = true;
private boolean refreshAfter = true; 

public void execute() throws Exception {		
	try {
		Map values = null;			
		if (getView().isKeyEditable()) {
			// Create			
			if (isResetAfter() || !isRefreshAfter()) { 
				MapFacade.create(getModelName(), getValuesToSave());
				addMessage("entity_created", getModelName());
			}
			else {								
				Map keyValues = MapFacade.createReturningKey(getModelName(), getValuesToSave());					
				addMessage("entity_created", getModelName());
				getView().clear(); 
				values = MapFacade.getValues(getModelName(), keyValues, getView().getMembersNamesWithHidden());
			}
		}
		else {
			// Modify
			String viewName = getView().getViewName();
			if("funding".equalsIgnoreCase(viewName.trim())){
/*				Transaction transaction = new Transaction();
				PaymentBatch batch = (PaymentBatch)getView().getEntity();
				Corporate corporate = (Corporate)batch.getOwner();
				transaction.setCorporate(corporate);
				transaction.setDateEntered(Dates.createCurrent());
				transaction.setModelName(batch.getClass().getName());
				transaction.setModelId(batch.getId());
				transaction.setEnteredBy(Users.getCurrent());
				transaction.setDescription(getView().getValueString("shortDescription"));
				
				batch.setStatus(Status.AwaitingApproval);
				XPersistence.getManager().merge(batch);
				transaction = transaction.approve();
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n "
						+ " The Batch status here ======" + transaction.getStatus());*/
				//return;
			}

			Map keyValues = getView().getKeyValues();				
			MapFacade.setValues(getModelName(), keyValues, getValuesToSave());
			addMessage("entity_modified", getModelName());
			if (!isResetAfter() && isRefreshAfter()) {
				getView().clear(); 
				values = MapFacade.getValues(getModelName(), keyValues, getView().getMembersNamesWithHidden());
			}
		}
		
		if (isResetAfter()) {
			getView().setKeyEditable(true);
			commit(); // If we change this, we should run all test suite using READ COMMITED (with hsqldb 2 for example)
			getView().reset();				
		}
		else {				
			getView().setKeyEditable(false);				
			if (isRefreshAfter()) getView().setValues(values); 
		}			
		resetDescriptionsCache();
	}
	catch (ValidationException ex) {			
		addErrors(ex.getErrors());
	}
	catch (ObjectNotFoundException ex) {			
		addError("no_modify_no_exists");
	}
	catch (DuplicateKeyException ex) {
		addError("no_create_exists");
	}
	
	addMessage("Save Successful", null);
}

protected Map getValuesToSave() throws Exception {	
	return getView().getValues();		
}

/**
 * If <tt>true</tt> reset the form after save, else refresh the
 * form from database displayed the recently saved data. <p>
 * 
 * The default value is <tt>true</tt>.
 */
public boolean isResetAfter() {
	return resetAfter;
}

/**
 * If <tt>true</tt> reset the form after save, else refresh the
 * form from database displayed the recently saved data. <p>
 * 
 * The default value is <tt>true</tt>.
 */
public void setResetAfter(boolean b) {
	resetAfter = b;
}

/**
 * If <tt>false</tt> after save does not refresh the
 * form from database. <p>
 * 
 * It only has effect if <tt>resetAfter</tt> is <tt>false</tt>.
 * 
 * The default value is <tt>true</tt>.
 * 
 * @since 4.8
 */	
public boolean isRefreshAfter() {
	return refreshAfter;
}

/**
 * If <tt>false</tt> after save does not refresh the
 * form from database. <p>
 * 
 * It only has effect if <tt>resetAfter</tt> is <tt>false</tt>.
 * 
 * The default value is <tt>true</tt>.
 * 
 * @since 4.8
 */		
public void setRefreshAfter(boolean refreshAfter) {
	this.refreshAfter = refreshAfter;
}

}
