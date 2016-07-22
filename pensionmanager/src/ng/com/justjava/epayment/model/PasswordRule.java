package ng.com.justjava.epayment.model;

import javax.persistence.*;

import org.openxava.annotations.*;

@Embeddable
@View(members="minimumLength;maximumLength;numberOfRepeatCharacter;numberOfRuleMustMet;"
		+ "digitRequired;nonAlphaNumericRequired;atLeastOneUpperCase;atLeastOneLowerCase;"
		+ "allowAlphabeticalSequence")
public class PasswordRule {
	private int minimumLength;
	private int maximumLength;
	private int numberOfRepeatCharacter;
	private int numberOfRuleMustMet;
	public int getMinimumLength() {
		return minimumLength;
	}
	public void setMinimumLength(int minimumLength) {
		this.minimumLength = minimumLength;
	}
	public int getMaximumLength() {
		return maximumLength;
	}
	public void setMaximumLength(int maximumLength) {
		this.maximumLength = maximumLength;
	}
	public int getNumberOfRepeatCharacter() {
		return numberOfRepeatCharacter;
	}
	public void setNumberOfRepeatCharacter(int numberOfRepeatCharacter) {
		this.numberOfRepeatCharacter = numberOfRepeatCharacter;
	}
	public boolean isDigitRequired() {
		return digitRequired;
	}
	public void setDigitRequired(boolean digitRequired) {
		this.digitRequired = digitRequired;
	}
	public boolean isNonAlphaNumericRequired() {
		return nonAlphaNumericRequired;
	}
	public void setNonAlphaNumericRequired(boolean nonAlphaNumericRequired) {
		this.nonAlphaNumericRequired = nonAlphaNumericRequired;
	}
	public boolean isAtLeastOneUpperCase() {
		return atLeastOneUpperCase;
	}
	public void setAtLeastOneUpperCase(boolean atLeastOneUpperCase) {
		this.atLeastOneUpperCase = atLeastOneUpperCase;
	}
	public boolean isAtLeastOneLowerCase() {
		return atLeastOneLowerCase;
	}
	public void setAtLeastOneLowerCase(boolean atLeastOneLowerCase) {
		this.atLeastOneLowerCase = atLeastOneLowerCase;
	}
	public boolean isAllowAlphabeticalSequence() {
		return allowAlphabeticalSequence;
	}
	public void setAllowAlphabeticalSequence(boolean allowAlphabeticalSequence) {
		this.allowAlphabeticalSequence = allowAlphabeticalSequence;
	}
	public int getNumberOfRuleMustMet() {
		return numberOfRuleMustMet;
	}
	public void setNumberOfRuleMustMet(int numberOfRuleMustMet) {
		this.numberOfRuleMustMet = numberOfRuleMustMet;
	}
	private boolean digitRequired;
	private boolean nonAlphaNumericRequired;
	private boolean atLeastOneUpperCase;
	private boolean atLeastOneLowerCase;
	private boolean allowAlphabeticalSequence;
}
