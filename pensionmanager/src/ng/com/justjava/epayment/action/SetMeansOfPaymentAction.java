package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.MakePayment.MeansOfPayment;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class SetMeansOfPaymentAction extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		MeansOfPayment means = (MeansOfPayment)getView().getValue("meansOfPayment");
		switch (means) {
		case accountTransfer:
			getView().setValue("means", Strings.fix("Account Number", 20, Align.LEFT));
			getView().setHidden("bank", false);
			break;
		case cash:
			getView().setValue("means", Strings.fix("Amount", 20, Align.LEFT));
			getView().setHidden("bank", true);
			break;
		case scratchCard:
			getView().setValue("means", Strings.fix("Scratch Card Number", 20, Align.LEFT));
			getView().setHidden("bank", true);
			break;			

		}
		getView().setHidden("meansValue", false);
		getView().setHidden("means", false);
	}

}