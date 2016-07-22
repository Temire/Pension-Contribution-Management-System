package ng.com.justjava.filter;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

public class CollectionItemFilter   implements IFilter {

	public Object filter(Object o) throws FilterException {
			
		
		
		
			Agent agent = Agent.getAgentByUserName(Users.getCurrent());
			

			ArrayList<String> items = new ArrayList<String>();
			if(agent !=null ){
				for (CollectionItem item : agent.getColletionItems()) {
					items.add(item.getName());
				}
			}else{
				String ejbQL = " FROM CollectionItem c ";
				Collection<CollectionItem> allItems = XPersistence.getManager().createQuery(ejbQL).getResultList();
				for (CollectionItem item : allItems) {
					items.add(item.getName());
				}
			}
				

			
			Object[] r = null;
			if(o == null){
				r = new Object[1];

				//result.add(null);
				r[0] =  items;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 1];
				 r[0] = items;
				 for (int i = 0; i < a.length; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[2];
				 r[0] = items;
				 r[1] = o;
				 return r;
			 }				

	}
}
