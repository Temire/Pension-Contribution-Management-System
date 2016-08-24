package ng.com.justjava.epayment.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
	public boolean validatePin(String pin){
		boolean returnAnswer = false;
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
	    Matcher m = p.matcher(pin);
	    returnAnswer = m.find();
	    return returnAnswer;
	}
	

}
