package ng.com.justjava.epayment.action;

import java.util.*;
import java.util.concurrent.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.hibernate.util.*;
import org.joda.time.*;

import com.google.common.eventbus.*;

public class ApproveSingleTransactionAction extends ViewBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub

		Long key = (Long) getView().getAllValues().get("id");
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n The value sent===" + getView().getAllValues());
		Map payingAcct = (Map) getView().getAllValues().get("payingAccount");

		
		PaymentBatch batch = XPersistence.getManager().find(PaymentBatch.class, key);
		TransitAccount debitAccount=batch.getPayingAccount();
		
		if(payingAcct.get("id")!=null){
			debitAccount = (TransitAccount) MapFacade.findEntity("TransitAccount", payingAcct);
			batch.setPayingAccount(debitAccount);
			XPersistence.getManager().merge(batch);
		}		
		if(UserManager.loginUserHasRole("funder") && debitAccount==null){
			addError("As funder, You Need to Attach Debit Account", null);
			return;
		}
		
		Long transId = (Long) getView().getObject("transId");

		Boolean fina = (Boolean) getView().getObject("final");
		
		
		
		if (fina) {
			if(debitAccount == null){
				addError(" Debit Account Yet To be Attached", null);
				return;
			}
			String token = (String) getView().getObject("token");
			String softToken = (String) getView().getValue("softToken");
			if (Is.empty(softToken)) {
				addError("Soft Token Is Required", null);
				return;
			}
			DateTime dT = (DateTime) getView().getObject("fiveMinutes");
			DateTime presentTime = new DateTime(Dates.withTime(Dates
					.createCurrent()));

			if (!Is.equalAsStringIgnoreCase(token, softToken)) {
				addError("Incorrect Soft Token", null);
				return;
			}
			if (dT.getMillis() < presentTime.getMillis()) {
				addError("Token Has Expired", null);
				return;
			}

			System.out
					.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n The sent object to this place ==="
							+ transId);

		}

		Transaction transaction = XPersistence.getManager().find(
				Transaction.class, transId);
		
		 AsyncEventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
		 eventBus.register(transaction);
		 System.out.println(" 1111111approve already commented out......... ");
		 eventBus.post(new Object());

		//transaction.approve();
		closeDialog();
		setNextMode(LIST);
	}

}
