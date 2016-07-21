package ng.com.justjava.security;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import edu.vt.middleware.password.*;

public class SecurityManager {
	private List<String> messages;
	
	public boolean validate(String password){
		// password must be between 8 and 16 chars long
		
		SystemWideSetup setup = SystemWideSetup.getSystemWideSetup();
		PasswordRule rule = setup.getPasswordRule();
		LengthRule lengthRule = new LengthRule(rule.getMinimumLength(),rule.getMaximumLength());

		// don't allow whitespace
		WhitespaceRule whitespaceRule = new WhitespaceRule();

		// control allowed characters
		CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
		// require at least 1 digit in passwords
		if(rule.isDigitRequired())
			charRule.getRules().add(new DigitCharacterRule(1));
		// require at least 1 non-alphanumeric char
		if(rule.isNonAlphaNumericRequired())
			charRule.getRules().add(new NonAlphanumericCharacterRule(1));
		// require at least 1 upper case char
		if(rule.isAtLeastOneUpperCase())
			charRule.getRules().add(new UppercaseCharacterRule(1));
		// require at least 1 lower case char
		if(rule.isAtLeastOneLowerCase())
			charRule.getRules().add(new LowercaseCharacterRule(1));
		// require at least 3 of the previous rules be met
		charRule.setNumberOfCharacteristics(rule.getNumberOfRuleMustMet());

		// don't allow alphabetical sequences
		AlphabeticalSequenceRule alphaSeqRule = new AlphabeticalSequenceRule();

		// don't allow numerical sequences of length 3
		//NumericalSequenceRule numSeqRule = new NumericalSequenceRule(3);

		// don't allow qwerty sequences
		//QwertySequenceRule qwertySeqRule = new QwertySequenceRule();

		// don't allow 4 repeat characters
		
		RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(rule.getNumberOfRepeatCharacter()+1);

		// group all rules together in a List
		List<Rule> ruleList = new ArrayList<Rule>();
		ruleList.add(lengthRule);
		ruleList.add(whitespaceRule);
		ruleList.add(charRule);
		ruleList.add(alphaSeqRule);
		//ruleList.add(numSeqRule);
		//ruleList.add(qwertySeqRule);
		ruleList.add(repeatRule);

		PasswordValidator validator = new PasswordValidator(ruleList);
		PasswordData passwordData = new PasswordData(new Password(password));

		RuleResult result = validator.validate(passwordData);
		if (result.isValid()) {
		  System.out.println("Valid password");
		} else {
		  System.out.println("Invalid password:");
		  messages = validator.getMessages(result);
		  for (String msg : validator.getMessages(result)) {
		    System.out.println(msg);
		  }
		}
		return result.isValid();
	}
	public List<String> getMessages(){
		return messages;
	}
}
