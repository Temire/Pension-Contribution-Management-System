package ng.com.justjava.epayment.action;

import java.util.*;

import javax.ejb.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.validators.*;

public class SendForApprovalAction extends ViewBaseAction{
	private boolean resetAfter = false;
	private boolean refreshAfter = true; 
    
	public void execute() throws Exception {		
		try {
			Map values = null;		
			System.out.println(" 1 Created");
			if (getView().isKeyEditable()) {
				// Create			
				System.out.println(" 2 Created");
				if (isResetAfter() || !isRefreshAfter()) { 
					System.out.println(" 3  Created");
					MapFacade.create(getModelName(), getValuesToSave());
					addMessage("entity_created", getModelName());
				}
				else {	
					System.out.println(" 4  Created getModelName()="+getModelName()+ " getValuesToSave()=="+getValuesToSave());
					Map keyValues = MapFacade.createReturningKey(getModelName(), getValuesToSave());
					System.out.println(" 5  Created");
					addMessage("entity_created", getModelName());
					getView().clear(); 
					values = MapFacade.getValues(getModelName(), keyValues, getView().getMembersNamesWithHidden());
				}
			}
			else {
				// Modify				
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
	}
	
	protected Map getValuesToSave() throws Exception {	
		Map values = getView().getValues();
		values.put("status", RemitPension.Status.awaitingApproval);
		return values;		
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
