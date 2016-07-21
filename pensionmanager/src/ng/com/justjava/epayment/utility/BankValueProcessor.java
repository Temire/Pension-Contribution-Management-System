package ng.com.justjava.epayment.utility;

import org.openxava.jpa.*;

import ng.com.justjava.epayment.model.*;

import com.googlecode.jcsv.annotations.*;

public class BankValueProcessor implements ValueProcessor<Bank> {

	public Bank processValue(String code) {
		// TODO Auto-generated method stub
		Bank result = null;
		String sql = "from Bank b where b.code='"+code+"'";
		try {
			result = (Bank) XPersistence.getManager().createQuery(sql).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
