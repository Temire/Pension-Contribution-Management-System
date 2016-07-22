package ng.com.justjava.epayment.action;

import java.util.*;
import java.util.concurrent.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Approver.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

import com.google.common.eventbus.*;
import com.openxava.naviox.model.*;

public class SaveTransitAccountAction extends CollectionElementViewBaseAction {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub

		if (UserManager.getCorporateOfLoginUser() == null) {
			addError(
					"Login User Not Allowed To Enter Transit Account For Corporate",
					null);
			return;
		}

		System.out
				.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n 0000000The value to be set==="
						+ getCollectionElementView().getParent().getAllValues());

		Map corporateMap = getCollectionElementView().getParent()
				.getAllValues();

		Corporate corporate = null;
		Map corporateKey = corporateMap;
		if (!corporateMap.containsKey("id")) {
			corporateKey = MapFacade.createReturningKey("Corporate",
					corporateMap);
		}

		corporate = (Corporate) MapFacade.findEntity("Corporate", corporateKey);

		// owner = XPersistence.getManager().merge(owner);

		// Map keyValues = MapFacade.createReturningKey("TransitAccountOwner",
		// getParentView().getAllValues());
		TransitAccount transit = null;// (TransitAccount)getCollectionElementView().getEntity();
		System.out.println("000000000000000");
		try {
			System.out.println("111111111111111111111");
			transit = (TransitAccount) MapFacade.findEntity("TransitAccount",
					getCollectionElementView().getAllValues());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (transit == null) {
			System.out.println("222222222222222222222");
			transit = (TransitAccount) MapFacade.create("TransitAccount",
					getCollectionElementView().getAllValues());

		} else {
			MapFacade.setValues("TransitAccount", getCollectionElementView()
					.getAllValues(), getCollectionElementView().getAllValues());
		}

		/*
		 * boolean transitExist = transit!=null && transit.getId()!=null;
		 * 
		 * System.out.println(
		 * " The transit account values here \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n===="
		 * + getCollectionElementView().getAllValues());
		 * 
		 * 
		 * 
		 * 
		 * transit = (TransitAccount) MapFacade.create("TransitAccount",
		 * getCollectionElementView().getAllValues());
		 */

		System.out.println("33333333333333333333333");
		transit.setCorporate(corporate);
		transit = XPersistence.getManager().merge(transit);

		/*
		 * ArrayList<TransitAccount> transits = new ArrayList<TransitAccount>();
		 * transits.add(transit); if(owner.getTransitAccounts() !=null){
		 * transits.addAll(owner.getTransitAccounts()); }
		 * owner.setTransitAccounts(transits);
		 * 
		 * owner = XPersistence.getManager().merge(owner);
		 */

		Transaction transaction = new Transaction();
		transaction.setTransRef(ApprovableTransaction.DebitAccount);
		transaction.setCorporate(corporate);
		transaction.setDateEntered(Dates.createCurrent());
		transaction.setModelName(transit.getClass().getName());
		transaction.setModelId(transit.getId());
		transaction.setEnteredBy(Users.getCurrent());

		if (transit.getId() != null)
			transaction.setDescription(" Debit Account with Number"
					+ transit.getTerminalId() + " Record Modified");
		else
			transaction.setDescription(" Debit Account with Number"
					+ transit.getTerminalId() + " Record Created");

		// transaction = transaction.approve();

		AsyncEventBus eventBus = new AsyncEventBus(
				Executors.newCachedThreadPool());
		eventBus.register(transaction);
		System.out.println(" 1111111approve already commented out......... ");
		eventBus.post(new Object());

		System.out.println(" 22222222approve already commented out......... ");
		addMessage("Operation Successful", null);
		closeDialog();
		// XPersistence

	}

}
