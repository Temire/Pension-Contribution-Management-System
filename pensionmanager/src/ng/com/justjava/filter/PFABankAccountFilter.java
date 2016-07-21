package ng.com.justjava.filter;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;

public class PFABankAccountFilter implements IFilter {

	public Object filter(Object o) throws FilterException {

			PensionFundAdministrator pfa = UserManager.getPFAOfLoginUser();
			

			String  pfaAccountName = pfa==null?" ":pfa.getAccount().getName();
			String  pfaAccountNumber = pfa==null?" ":pfa.getAccount().getNumber();

			
			System.out.println("pfaAccountName="+pfaAccountName + " pfaAccountNumber="+pfaAccountNumber);
			Object[] r = null;
			if(o == null){
				r = new Object[2];
				r[0] =  pfaAccountName;
				r[1] =  pfaAccountNumber;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 2];
				 r[0] = pfaAccountName;
				 r[1] = pfaAccountNumber;
				 for (int i = 0; i < a.length; i++) {
					 r[i+2]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[3];
				 r[0] = pfaAccountName;
				 r[1] = pfaAccountNumber;
				 r[2] = o;
				 return r;
			 }				

	}
}
