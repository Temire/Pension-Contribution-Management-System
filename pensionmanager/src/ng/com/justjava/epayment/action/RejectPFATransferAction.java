package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.PFATransfer.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class RejectPFATransferAction extends TabBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map[] keys = getSelectedKeys();
		for (Map key : keys) {
			System.out.println(" The key selected here ==" +key + " the module name here=="+
					getManager().getModuleName());
			if(Is.equalAsStringIgnoreCase("ApproveIncomingTransfer", getManager().getModuleName())){
				Map values = new HashMap();
				values.put("status", TransferStatus.rejected);
				MapFacade.setValues("PFATransfer", key, values);
			}
			if(Is.equalAsStringIgnoreCase("ApproveOutgoingTransfer", getManager().getModuleName())){
				Map values = new HashMap();
				
				PFATransfer pfaTransfer = (PFATransfer) MapFacade.findEntity("PFATransfer", key);
				pfaTransfer.setStatus(TransferStatus.rejected);
				XPersistence.getManager().merge(pfaTransfer);
/*				RSAHolder holder = pfaTransfer.getHolder();
				holder.setPfa(pfaTransfer.getPfa());
				
				XPersistence.getManager().merge(holder);*/
			}
		}
		addMessage("Transfer Rejected !", null);
    	getTab().deselectAll();
    	getView().refresh();
	}

}
