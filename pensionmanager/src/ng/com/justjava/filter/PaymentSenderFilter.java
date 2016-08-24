package ng.com.justjava.filter;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;

public class PaymentSenderFilter implements IFilter {

	public Object filter(Object o) throws FilterException {

			Corporate corporate = UserManager.getCorporateOfLoginUser();
			

			String  senderName = corporate==null?" ":corporate.getUniqueIdentifier();
			
			System.out.println("corporate.getName()="+corporate.getUniqueIdentifier().trim());
			
			Object[] r = null;
			if(o == null){
				r = new Object[1];

				//result.add(null);
				r[0] =  senderName;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 1];
				 r[0] = senderName;
				 for (int i = 0; i < a.length; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[2];
				 r[0] = senderName;
				 r[1] = o;
				 return r;
			 }				

	}
}
