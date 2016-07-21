package ng.com.justjava.epayment.action;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.inject.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Approver.ApprovableTransaction;

import org.apache.commons.logging.*;
import org.hibernate.validator.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.tab.*;
import org.openxava.util.*;
import org.openxava.validators.*;
import org.openxava.view.*;

public class SaveApproverAction extends CollectionElementViewBaseAction {
	
	private boolean containerSaved = false;
	
	public void execute() throws Exception {		
		Map containerKey = saveIfNotExists(getCollectionElementView().getParent());
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n The Parent Key===" + containerKey
				+ "  and the value to save here ==" + getValuesToSave());
		
		Map approverKey = (Map) getValuesToSave().get("approver");
		
		int level =(Integer) getValuesToSave().get("level");
		
		ApprovableTransaction transaction =(ApprovableTransaction) getValuesToSave().get("transaction");
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n transaction to save here ==" 
		+ getValuesToSave().get("transaction").getClass().getName());		
		
		

		
		Approver approver = (Approver) MapFacade.findEntity("Approver", approverKey);
		
		String name =(String) approver.getName();
		 
		Corporate corporate = (Corporate) MapFacade.findEntity("Corporate", containerKey);
		
		if(corporate !=null){
			String ejbQL = " from CorporateApprover c where c.corporate.id="+corporate.getId() +
					" AND c.approver.name='" + name + "' AND c.transaction="+transaction.ordinal();
			CorporateApprover corporateApprover = null;
			try {
				corporateApprover = (CorporateApprover) XPersistence.getManager().
						createQuery(ejbQL).getSingleResult();
				
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n duplicated corporateApprover===" + 
				corporateApprover.getApprover().getName());					
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n corporateApprover===" + 
			corporateApprover + "   supplied level=== " + level);
			
			if(corporateApprover != null && corporateApprover.getLevel() != level){
/*				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n corporateApprover.getLevel()===" + 
						corporateApprover.getLevel() + "   supplied level=== " + level);*/				
				
				addInfo("The_same_approver_cannot_appear_twice_on_approver_route", null);
				return;
			}
			
		}   
			
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n "
				+ "approver===" + approver.getName());		 
		
		if (XavaPreferences.getInstance().isMapFacadeAutoCommit()) {
			getView().setKeyEditable(false); // To mark as saved
		}
		saveCollectionElement(containerKey);
		commit(); // If we change this, we should run all test suite using READ COMMITED (with hsqldb 2 for example) 
		getView().setKeyEditable(false); // To mark as saved
		getCollectionElementView().setCollectionEditingRow(-1);
		getCollectionElementView().clear();
		resetDescriptionsCache();
		if (containerSaved) getView().getRoot().refresh(); 
		else getView().recalculateProperties();
		closeDialog(); 		
	}
	
	
	protected Map getValuesToSave() throws Exception {
		return getCollectionElementView().getValues();		
	}
		
	/**
	 * Saves the collection or aggregate.
	 * @param containerKey 
	 * @throws Exception
	 * @since 4.7 
	 */
	protected void saveCollectionElement(Map containerKey) throws Exception {
		if (getCollectionElementView().isEditable()) {
			// Aggregate or entity reference used as aggregate 
			boolean isEntity = isEntityReferencesCollection(); 			
			Map parentKey = new HashMap();
			MetaCollection metaCollection = getMetaCollection();
			parentKey.put(metaCollection.getMetaReference().getRole(), containerKey);
			Map values = getValuesToSave();			
			values.putAll(parentKey);
			
			try {
				MapFacade.setValues(getCollectionElementView().getModelName(), getCollectionElementView().getKeyValues(), values);
				addMessage(isEntity?"entity_modified":"aggregate_modified", getCollectionElementView().getModelName());
			}
			catch (ObjectNotFoundException ex) {
				create(values, isEntity);				
			}		
		}
		else {
			// Entity reference used in the standard way
			validateMaximum(1); 
			associateEntity(getCollectionElementView().getKeyValues());
			addMessage("entity_associated" , getCollectionElementView().getModelName(), getCollectionElementView().getParent().getModelName());
		}
	}
	
	private void create(Map values, boolean isEntity) throws CreateException {
		validateMaximum(1);
		MapFacade.create(getCollectionElementView().getModelName(), values);
		addMessage(isEntity?"entity_created_and_associated":"aggregate_created", 
			getCollectionElementView().getModelName(), 
			getCollectionElementView().getParent().getModelName());
	}

	protected void associateEntity(Map keyValues) throws ValidationException, XavaException, ObjectNotFoundException, 
	FinderException, RemoteException {		
		MapFacade.addCollectionElement(
				getCollectionElementView().getParent().getMetaModel().getName(),
				getCollectionElementView().getParent().getKeyValues(),
				getCollectionElementView().getMemberName(),  
				keyValues);		
	}

	/**
	 * @return The saved object 
	 */
	protected Map saveIfNotExists(View view) throws Exception {
		if (getView() == view) {
			if (view.isKeyEditable()) {				
				Map key = MapFacade.createNotValidatingCollections(getModelName(), view.getValues());
				addMessage("entity_created", getModelName());
				view.addValues(key);
				containerSaved=true;				
				return key;								
			}			
			else {										
				return view.getKeyValues();									
			}
		}			
		else {
			if (view.getKeyValuesWithValue().isEmpty()) {				
				Map parentKey = saveIfNotExists(view.getParent());
				Map key = MapFacade.createAggregateReturningKey( 
					view.getModelName(),
					parentKey, 0,					
					view.getValues() );
				addMessage("aggregate_created", view.getModelName());
				view.addValues(key);
				return key;										
			}
			else {			
				return view.getKeyValues();
			}
		}
	}

	public String getNextAction() throws Exception { 		
		return getErrors().contains()?null:getCollectionElementView().getNewCollectionElementAction();
	}
		
}
