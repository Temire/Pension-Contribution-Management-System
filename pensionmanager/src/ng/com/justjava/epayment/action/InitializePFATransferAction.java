package ng.com.justjava.epayment.action;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;

public class InitializePFATransferAction  extends ViewBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		RSAHolder holder = UserManager.getHolderProfileOfLoginUser();
		

		if(holder==null)
			return;
		
		getView().setValue("currentPFA", holder.getPfa().getName());
		addMessage(holder.getFirstName()+" (RSA Id-"+holder.getPencommNumber()+")", null);
		
		String name = holder.getCorporate()==null?"Personal Pension Contribution ":holder.getCorporate().getName();
		
		addMessage("Company Name: " + name,null);
		//addMessage("Current PFA: "+holder.getPfa().getName(), null);
	}

}
