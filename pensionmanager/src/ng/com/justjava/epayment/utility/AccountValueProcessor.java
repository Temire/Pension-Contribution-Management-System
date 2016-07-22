package ng.com.justjava.epayment.utility;

import org.openxava.jpa.*;

import ng.com.justjava.epayment.model.*;

import com.googlecode.jcsv.annotations.*;

public class AccountValueProcessor implements ValueProcessor<Account>{

	public Account processValue(String number) {
		if(number != null)
			number = number.trim();
		
		Account result = new Account();
		result.setNumber(number);

		String sql = "from Account a where a.number='"+number+"'";
		
		System.out.println("/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n "
				+ "The main account sent in ======" + number + "  and the SQL ==" + sql);		

		try {
			result = (Account) XPersistence.getManager().createQuery(sql).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
