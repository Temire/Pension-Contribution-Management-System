package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Payment.Status;
import ng.com.justjava.epayment.utility.*;

import org.joda.time.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class RejectSingleTransactionAction extends ViewBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub

		Long key = (Long) getView().getAllValues().get("id");
		
		String rejectReason = getView().getValueString("comment");
		if(Is.empty(rejectReason)){
			addError("Comment is Compulsory When Transaction is Rejected !", null);
			return;
		}
		System.out.println("\n\n\n\n\n\n\n\n\n\n The value sent===" + getView().getAllValues());

		
		PaymentBatch batch = XPersistence.getManager().find(PaymentBatch.class, key);
		batch.setStatus(Status.Reject);
		XPersistence.getManager().merge(batch);
		
		Long transId = (Long) getView().getObject("transId");

		
		Transaction transaction = XPersistence.getManager().find(Transaction.class, transId);
		transaction.setComment(rejectReason);
		transaction.reject(rejectReason);;
		closeDialog();
		setNextMode(LIST);
	}

}