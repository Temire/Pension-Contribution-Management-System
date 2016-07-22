package ng.com.justjava.filter;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;
import org.openxava.jpa.*;

public class VerifiedAccountFilter  implements IFilter {

	public Object filter(Object o) throws FilterException {
			
		
		
		
			Corporate nonAdmin = UserManager.getCorporateOfLoginUser();
			
			String ejbQL = " SELECT p.toAccount.id FROM PaymentInstruction p ";
			if(nonAdmin != null)
				ejbQL = ejbQL +"WHERE p.paymentBatch.owner.id=" + nonAdmin.getId();
			
			Collection<Long> ids = XPersistence.getManager().createQuery(ejbQL).getResultList();

			for(Long id : ids){
				System.out.println(" The id fetched are ===" + id);
			}
			
			
			Object[] r = null;
			if(o == null){
				r = new Object[1];

				//result.add(null);
				r[0] =  ids;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 1];
				 r[0] = ids;
				 for (int i = 0; i < a.length; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[2];
				 r[0] = ids;
				 r[1] = o;
				 return r;
			 }				

	}
}
