package ng.com.justjava.filter;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;

import com.openxava.naviox.model.*;

public class TransactionFilter implements IFilter {

	public Object filter(Object o) throws FilterException {


			CorporateUser user = UserManager.getLoginCorporateUser();
			Long corporateId = user!=null?user.getCorporate().getId():0L;
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
				r[1] = corporateId;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 2];
				 r[0] = roleNames;
				 r[1] = corporateId;
				 for (int i = 1; i < a.length + 1; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[2];
				 r[0] = roleNames;
				 r[1] = corporateId;
				 r[2] = o;
				 return r;
			 }			

	}
}
