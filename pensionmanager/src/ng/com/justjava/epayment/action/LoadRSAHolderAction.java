package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

public class LoadRSAHolderAction extends ViewBaseAction {

	public void execute() throws Exception {

				System.out.println("1 Entering LoadRSAHolderAction ");
				String loginName = Users.getCurrent();
				
				System.out.println("2 Entering LoadRSAHolderAction " + loginName);
				RSAHolder holder = null;
				try {
					holder = (RSAHolder) XPersistence.getManager().
							createQuery(" FROM RSAHolder h WHERE h.email='"+loginName+"'").
							getSingleResult();
					System.out.println(" The holder here has name ==" + holder.getFirstName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	
				if(holder==null)
					return;
				
				Map key = new HashMap();
				key.put("id", holder.getId());
	
				getView().setModelName("RSAHolder"); // 2
				getView().setValues(key); // 3
				getView().findObject(); // 4

				getView().setKeyEditable(false);
	}
		
}