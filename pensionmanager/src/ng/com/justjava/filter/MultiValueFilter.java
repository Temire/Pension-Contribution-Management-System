package ng.com.justjava.filter;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Approver.ApprovableTransaction;
import ng.com.justjava.epayment.utility.*;

import org.openxava.filters.*;

public class MultiValueFilter implements IFilter {

	public Object filter(Object o) throws FilterException {

			Corporate corporate = UserManager.getCorporateOfLoginUser();
			CorporateUser user = UserManager.getLoginCorporateUser();
			Collection<Profile> profiles = user.getMyProfiles();
			System.out.println(" The profiles  " + profiles);
			int levelReached =-1;
			if(profiles != null){
				System.out.println(" The profiles  size" + profiles.size());
				for (Profile profile : profiles) {
					if(profile.getTransaction()==ApprovableTransaction.MonthlyRemittance){
						levelReached = profile.getLevel();
					}
				}
			}
			Long corporateId = corporate==null?0:corporate.getId();
			Object[] r = null;
			if(o == null){
				r = new Object[2];

				//result.add(null);
				r[0] =  corporateId;
				r[1] =  levelReached;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 2];
				 r[0] = corporateId;
				 r[1] = levelReached;
				 for (int i = 0; i < a.length; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[3];
				 r[0] = corporateId;
				 r[1] = levelReached;
				 r[2] = o; 
				 return r;
			 }				

	}
}
