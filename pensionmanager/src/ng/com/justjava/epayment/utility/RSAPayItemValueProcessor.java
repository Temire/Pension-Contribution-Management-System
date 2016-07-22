package ng.com.justjava.epayment.utility;

import java.math.*;

import org.openxava.jpa.*;
import org.openxava.util.*;

import ng.com.justjava.epayment.model.*;

import com.googlecode.jcsv.annotations.*;

public class RSAPayItemValueProcessor implements ValueProcessor<PayItemCollection> {

	public PayItemCollection processValue(String arg0) {
		PayItemCollection rsaPayItem = new PayItemCollection();
		
		
		System.out.println(" The sent arg0==" + arg0);
		if (arg0 != null) {
			String[] detail = arg0.split("#");
			if (detail.length == 2) {
				String payitemType = detail[0];
				CompanyPayItemCollection type = (CompanyPayItemCollection)getModel(payitemType.trim());
				
				
				if(type != null){
					rsaPayItem.setCode(type.getCode());
					rsaPayItem.setName(type.getName());
					rsaPayItem.setActive(type.isActive());
					//rsaPayItem.setPayItem(type);
				}else
					return null;
				
				BigDecimal amount = new BigDecimal(detail[1]);
				System.out.println(" The sent data payitemType =====" + payitemType + "  amount==" + amount);
				rsaPayItem.setAmount(amount);
			}
		}
		
		System.out.println(" The about to be sent rsaPayItem ==" + rsaPayItem + " amount=="+rsaPayItem.getAmount()+
				" name "+rsaPayItem.getName());
		return rsaPayItem;

	}
	//@Transient
	public CompanyPayItemCollection getModel(String code) {
			CompanyPayItemCollection returnItem = null;
			Corporate corporate = UserManager.getCorporateOfLoginUser();
			if(corporate==null || corporate.getItems()==null)
				return returnItem;
			for (CompanyPayItemCollection item : corporate.getItems()) {
				if(Is.equalAsString(code, item.getCode())){
					returnItem = item;
					break;
				}
			}

		return returnItem;
	}	
}