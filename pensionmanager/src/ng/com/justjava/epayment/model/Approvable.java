package ng.com.justjava.epayment.model;

import javax.persistence.*;

@MappedSuperclass
public class Approvable {
	private boolean enable;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
