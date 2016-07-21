package ng.com.justjava.filter;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;

public class LoginUserPFAFilter implements IFilter {

	public Object filter(Object o) throws FilterException {

			PensionFundAdministrator pfa = UserManager.getPFAOfLoginUser();
			Long pfaId = pfa==null?0:pfa.getId();

			Object[] r = null;
			if(o == null){
				r = new Object[1];
				r[0] =  pfaId;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 1];
				 r[0] = pfaId;
				 for (int i = 0; i < a.length; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[2];
				 r[0] = pfaId;
				 r[1] = o;
				 return r;
			 }				

	}
}
