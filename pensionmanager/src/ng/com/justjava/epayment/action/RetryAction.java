package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.MonthlyUpload.Status;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

public class RetryAction extends TabBaseAction{

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map key = (Map) getTab().getTableModel().getObjectAt(getRow());
		MonthlyUpload upload = (MonthlyUpload) MapFacade.findEntity("MonthlyUpload", key);
		
		String reference = upload.getPaymentOtherReference();
		String[] referenceSplit = reference.split("_");
		
		if(referenceSplit.length>1){
			int add = Integer.valueOf(referenceSplit[1]) + 1;
			reference = referenceSplit[0] + add;
		}else
			reference = reference +"_"+1;
			
		upload.setPaymentOtherReference(reference);
		upload.setStatus(Status.approve);
		XPersistence.getManager().merge(upload);
		XPersistence.getManager().flush();
		addMessage(upload.getNarration() + " For Month "+upload.getMonth() + " Is Being Retried! " , null);
		
	}

}
