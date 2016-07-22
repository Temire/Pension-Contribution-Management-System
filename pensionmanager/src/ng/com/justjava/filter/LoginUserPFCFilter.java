package ng.com.justjava.filter;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;

public class LoginUserPFCFilter implements IFilter {

	public Object filter(Object o) throws FilterException {

			PensionFundCustodian pfaC = UserManager.getPFCOfLoginUser();
			Long pfcId = pfaC==null?0:pfaC.getId();
			
			System.out.println(" The custodian id here =======" + pfcId);
			Object[] r = null;
			if(o == null){
				r = new Object[1];
				r[0] =  pfcId;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 1];
				 r[0] = pfcId;
				 for (int i = 0; i < a.length; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[2];
				 r[0] = pfcId;
				 r[1] = o;
				 return r;
			 }				

	}
}
