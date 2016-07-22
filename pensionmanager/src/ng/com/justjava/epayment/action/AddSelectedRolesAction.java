package ng.com.justjava.epayment.action;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.hibernate.*;
import org.hibernate.ObjectNotFoundException;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

import com.openxava.naviox.model.*;

public class AddSelectedRolesAction extends AddElementsToCollectionAction {

	public void execute() throws Exception {
		
		System.out.println("I am here ..................");
		
		super.execute();
		getView().refresh();
	}

	protected void associateEntity(Map keyValues) // The method called to
													// associate
			throws ValidationException, // each entity to the main one, in this
										// case to
			XavaException, ObjectNotFoundException,// associate each order to
													// the invoice
			FinderException, RemoteException {
		

		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n keyValues==="+keyValues
				+ "  and the parent values are ===" + getCollectionElementView().getParent().getAllValues());
		try {
			Map values = getCollectionElementView().getParent().getAllValues();
			User user = null;
			try {
				String sql = " FROM User u WHERE u.name='" + values.get("name") + "'";
				Collection<User> users = XPersistence.getManager().createQuery(sql).getResultList();
				if(users ==null || users.size()<= 0){
					MapFacade.create("User", values);
				}
				
				//user = (User) MapFacade.findEntity("User", values);
				//MapFacade.setValues("User", values,values);
			} catch (ObjectNotFoundException e) {
				// TODO Auto-generated catch block
				//MapFacade.create("User", values);
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.associateEntity(keyValues); // It executes the standard logic (1)

	}
}
