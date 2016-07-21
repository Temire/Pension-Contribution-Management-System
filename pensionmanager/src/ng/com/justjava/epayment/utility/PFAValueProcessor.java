package ng.com.justjava.epayment.utility;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.jpa.*;

import com.googlecode.jcsv.annotations.*;

public class PFAValueProcessor implements ValueProcessor<PensionFundAdministrator> {

	public PensionFundAdministrator processValue(String uniqueIdentifier) {
		// TODO Auto-generated method stub
		PensionFundAdministrator result = null;
		ArrayList<PensionFundAdministrator> results = null;
		String sql = "from PensionFundAdministrator p where p.uniqueIdentifier='"+uniqueIdentifier+"'";
		try {
			System.out.println(" The SQL Here ================"+sql);
			results = (ArrayList<PensionFundAdministrator>) XPersistence.getManager().
																					createQuery(sql).getResultList();
			if(results!=null && results.size()>0){
				result = results.get(0);
			}else{
				sql = "from PensionFundAdministrator p where p.uniqueIdentifier='default_pfa'";
				results = (ArrayList<PensionFundAdministrator>) XPersistence.getManager().
						createQuery(sql).getResultList();
				if(results!=null && results.size()>0){
					result = results.get(0);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}