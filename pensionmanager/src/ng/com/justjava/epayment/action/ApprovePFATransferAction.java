package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.PFATransfer.TransferStatus;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

public class ApprovePFATransferAction extends TabBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map[] keys = getSelectedKeys();
		
		
		
		for (Map key : keys) {
			System.out.println(" The key selected here ==" +key + " the module name here=="+
					getManager().getModuleName());
			PFATransfer transfer = (PFATransfer) MapFacade.findEntity("PFATransfer", key);
			TransferStatus status = transfer.getStatus();

			Map values = new HashMap();
			values.put("status", getNextStatus(status));
			if(getNextStatus(status) == TransferStatus.fundTransferConfirmed){
				values.put("status", TransferStatus.fundTransferConfirmed);
				addMessage("PFA Transfer Successfully Closed! ", null);
		    	getTab().deselectAll();
		    	getView().refresh();
/*				String content = holder.getFirstName() + " " +holder.getSecondName() + ", PFA Transfer To " +
						pfaTransfer.getIncomingPFA() + ", Has Been Approved";
				SystemWideSetup.sendNotification(holder.getEmail(), 
						"PFA Transfer Approved", content, content, holder.getPhoneNumber());*/
		    	MapFacade.setValues("PFATransfer", key, values);
		    	return;
			}
			if(getNextStatus(status) == TransferStatus.approved){
				PFATransfer pfaTransfer = (PFATransfer) MapFacade.findEntity("PFATransfer", key);
				pfaTransfer.setStatus(TransferStatus.approved);
				RSAHolder holder = pfaTransfer.getHolder();
				holder.setPfa(pfaTransfer.getPfa());
				XPersistence.getManager().merge(pfaTransfer);
				XPersistence.getManager().merge(holder);
				addMessage("Transfer Successfully Approved! ", null);
		    	getTab().deselectAll();
		    	getView().refresh();
				String content = holder.getFirstName() + " " +holder.getSecondName() + ", PFA Transfer To " +
						pfaTransfer.getIncomingPFA() + ", Has Been Approved";
				SystemWideSetup.sendNotification(holder.getEmail(), 
						"PFA Transfer Approved", content, content, holder.getPhoneNumber());
			}else{
				addMessage("Transfer Successfully Send For Next Approval! ", null);
		    	getTab().deselectAll();
		    	getView().refresh();
			}
			
			//values.put("status", getNextStatus(status));
			MapFacade.setValues("PFATransfer", key, values);
		}

	}
	
	private TransferStatus getNextStatus(TransferStatus status){
		switch (status) {
		case withIncomingPFA:
			return TransferStatus.incomingVerified;
		case incomingVerified:
			return TransferStatus.approved;
		case approved:
			return TransferStatus.fundTransferEffected;
		case fundTransferEffected:
			return TransferStatus.fundTransferConfirmed;			
		}
		return null;
	}

}
