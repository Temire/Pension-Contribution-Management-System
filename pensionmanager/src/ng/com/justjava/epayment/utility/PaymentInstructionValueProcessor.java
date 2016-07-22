package ng.com.justjava.epayment.utility;

import org.openxava.jpa.*;

import ng.com.justjava.epayment.model.*;

import com.googlecode.jcsv.annotations.*;

public class PaymentInstructionValueProcessor implements ValueProcessor<PaymentInstruction>{

	public PaymentInstruction processValue(String arg0) {
		// TODO Auto-generated method stub
		PaymentInstruction result = null;
		String sql = "from PaymentInstruction p where p.code='"+arg0+"'";
		try {
			result = (PaymentInstruction) XPersistence.getManager().createQuery(sql).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
