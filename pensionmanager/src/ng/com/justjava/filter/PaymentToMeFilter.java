package ng.com.justjava.filter;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;

public class PaymentToMeFilter implements IFilter {

	public Object filter(Object o) throws FilterException {
			
			CorporateUser admin = UserManager.getLoginCorporateUser();
			Long filter = -1L;
			if(admin != null)
				filter = admin.getId();
			Object[] r = null;
			if(o == null){
				r = new Object[1];

				//result.add(null);
				r[0] =  filter;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 1];
				 r[0] = filter;
				 for (int i = 0; i < a.length; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[2];
				 r[0] = filter;
				 r[1] = o;
				 return r;
			 }


	}
}
