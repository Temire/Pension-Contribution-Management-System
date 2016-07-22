package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

public class UserRoleFilter implements IFilter {

	public Object filter(Object o) throws FilterException {


			CorporateUser user = UserManager.getLoginCorporateUser();
			ArrayList<String> roleNames = new ArrayList<String>();
			roleNames.add("admin");
			if(user != null){
				Collection<Role> roles =  user.getUser().getRoles();
				for (Role role : roles) {
					roleNames.add(role.getName());
				}
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
