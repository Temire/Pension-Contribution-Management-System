package com.openxava.naviox.actions;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

import com.openxava.naviox.model.*;

/**
 * 
 * @author Javier Paniza
 */

public class AddModulesRightsToRoleAction extends AddElementsToCollectionAction {
	
	
	protected void associateEntity(Map keyValues) throws ValidationException, XavaException, ObjectNotFoundException, FinderException, RemoteException {		
		Module module = (Module) MapFacade.findEntity("Module", keyValues);
		
		Map roleKey = getCollectionElementView().getParent().getKeyValues();
		Role role = null;
		String ejbQL = " FROM Role r where r.name='"+ roleKey.get("name") + "'";
		Collection<Role> roles = XPersistence.getManager().createQuery(ejbQL).getResultList();
		if(roles == null || roles.size()<=0){
			try {
				role = (Role) MapFacade.create("Role", getCollectionElementView().getParent().getAllValues());
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CreateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(role==null)
			role = (Role) MapFacade.findEntity("Role", getCollectionElementView().getParent().getKeyValues());
		ModuleRights rights = new ModuleRights();
		rights.setModule(module);
		rights.setRole(role);
		XPersistence.getManager().persist(rights);
	}


}
