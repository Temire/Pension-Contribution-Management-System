package ng.com.justjava.filter;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;

public class PFCBankAccountFilter implements IFilter {

	public Object filter(Object o) throws FilterException {

			PensionFundCustodian pfc = UserManager.getPFCOfLoginUser();
			
			ArrayList<String> banks = new ArrayList<>();

			if(pfc !=null){
				for (Account account : pfc.getAccounts()) {
					banks.add(account.getName());
					System.out.println("account.getName()="+account.getName());
				}
			}

			
			
			Object[] r = null;
			if(o == null){
				r = new Object[1];
				r[0] =  banks;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 1];
				 r[0] = banks;
				 for (int i = 0; i < a.length; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[3];
				 r[0] = banks;
				 r[1] = o;
				 return r;
			 }				

	}
}