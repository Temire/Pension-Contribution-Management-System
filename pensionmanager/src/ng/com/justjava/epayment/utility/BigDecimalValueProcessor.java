package ng.com.justjava.epayment.utility;

import java.math.*;

import com.googlecode.jcsv.annotations.*;

public class BigDecimalValueProcessor implements ValueProcessor<BigDecimal>{

	public  BigDecimal processValue(String arg0) {
		BigDecimal value = new BigDecimal(0.00);
		try{
			value = new BigDecimal(arg0);
		}catch(Exception exc){
			System.out.println(exc.getMessage());
		}
		return value;
	}

}