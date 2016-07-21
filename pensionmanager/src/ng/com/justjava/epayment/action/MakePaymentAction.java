package ng.com.justjava.epayment.action;

import java.util.*;

import javax.ejb.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.MakePayment.MeansOfPayment;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

public class MakePaymentAction extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n Make Payment Action called........");
		Map billerMap = (Map)getView().getAllValues().get("biller");
		
		Biller biller = UserManager.getBillerOfLoginUser();
		if((billerMap ==null || billerMap.isEmpty()) && biller ==null  ){
			addError("Biller is Required Field", null);
			return;
		}
		if(biller == null)
			biller = (Biller) MapFacade.findEntity("Biller", billerMap);
		
		if(biller.isSidRequired()){
			if(Is.emptyString(getView().getValueString("SID"))){
				notifyIdentityManager();
				//return;
			}
				
		}
		System.out.println("0000000ScratchCard Picked=======");
		MeansOfPayment means = (MeansOfPayment) getView().getValue("meansOfPayment");
		
		System.out.println("111111ScratchCard Picked=======");
		

		
		if(means==MeansOfPayment.scratchCard){
			String scratchCardNumber=getView().getValueString("meansValue");
			if(!ScratchCard.isValid(scratchCardNumber)){
				addError("Invalid Scratch Card", null);
				return;
			}

		}
		

		Map allValues = getView().getAllValues();
		
		System.out.println("allValues=====" + allValues);
		
		allValues.put("madeBy", Users.getCurrent());
		allValues.put("madeFor", Users.getCurrent());
		Map key= MapFacade.createReturningKey("MakePayment", allValues);
		System.out.println(" The biller here ==" + biller.getName() + " The Key Just Created ==" + key);
		switch (means) {
		case accountTransfer:
			//fundTransfer();
			break;
		case cash:
			payCash(key);
			break;
		case scratchCard:

			processScratchCard(key);
			break;			
		}
		addMessage("Payment Successfully Made", null);
	}

	private void notifyIdentityManager() {
		// TODO Auto-generated method stub
		
	}

	private void processScratchCard(Map key) {
		// TODO Auto-generated method stub
		Map paid = new HashMap();
		paid.put("paid", true);
		try {
			MapFacade.setValues("MakePayment", key, paid);
			ScratchCard.invalidate(key);
		} catch (ObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void payCash(Map key) {
		// TODO Auto-generated method stub
		Map paid = new HashMap();
		paid.put("paid", true);
		try {
			MapFacade.setValues("MakePayment", key, paid);
		} catch (ObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
