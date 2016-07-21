package ng.com.justjava.epayment.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;


@Entity
public class PaymentOption {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Hidden
	private Long id;
	
	private String paymentOption;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public static Collection<PaymentOption> findAll(){
		return XPersistence.getManager().createQuery("FROM PaymentOption p ").getResultList();
	}

	public String getPaymentOption() {
		return paymentOption;
	}

	public void setPaymentOption(String paymentOption) {
		this.paymentOption = paymentOption;
	}
}
