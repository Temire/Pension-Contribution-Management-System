package ng.com.justjava.filter;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

public class RoleFilter  implements IFilter {

	public Object filter(Object o) throws FilterException {
			
		
		
		
			Corporate nonAdmin = UserManager.getCorporateOfLoginUser();
			
			String ejbQL = " FROM Profile p ";
			if(nonAdmin != null)
				ejbQL = ejbQL +"WHERE p.universal=1 OR p.corporate.id=" + nonAdmin.getId();
			
			Collection<Profile> profiles = XPersistence.getManager().createQuery(ejbQL).getResultList();
			ArrayList<String> roleNames = new ArrayList<String>();
			roleNames.add("admin");
			for(Profile profile:profiles){
				roleNames.add(profile.getRole().getName());
				System.out.println("\n\n\n\n\n\n\n name===" + profile.getRole().getName() + " with ejbQL ==" + ejbQL);
			}
			
			Object[] r = null;
			if(o == null){
				r = new Object[1];

				//result.add(null);
				r[0] =  roleNames;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 1];
				 r[0] = roleNames;
				 for (int i = 0; i < a.length; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[2];
				 r[0] = roleNames;
				 r[1] = o;
				 return r;
			 }				

	}
}
