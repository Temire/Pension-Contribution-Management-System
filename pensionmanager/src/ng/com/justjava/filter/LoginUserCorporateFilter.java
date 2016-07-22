package ng.com.justjava.filter;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;
import org.openxava.util.*;

public class LoginUserCorporateFilter  implements IFilter {

	public Object filter(Object o) throws FilterException {

			Corporate corporate = UserManager.getCorporateOfLoginUser();
			Long corporateId = corporate==null?0:corporate.getId();

			Object[] r = null;
			if(o == null){
				r = new Object[1];

				//result.add(null);
				r[0] =  corporateId;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 1];
				 r[0] = corporateId;
				 for (int i = 0; i < a.length; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[2];
				 r[0] = corporateId;
				 r[1] = o;
				 return r;
			 }				

	}
}
