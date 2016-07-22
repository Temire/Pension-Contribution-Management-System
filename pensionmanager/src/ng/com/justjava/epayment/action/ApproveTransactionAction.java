package ng.com.justjava.epayment.action;

import java.util.*;
import java.util.concurrent.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;
import org.openxava.model.*;

import com.google.common.eventbus.*;

public class ApproveTransactionAction extends TabBaseAction{    // 1
 
    public void execute() throws Exception { 
    	
    	
    	Map[]  selectedKeys = getSelectedKeys();
    	
    	for(Map key : selectedKeys){
    		Transaction transaction = (Transaction) MapFacade.findEntity("Transaction", key);
			 AsyncEventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
			 eventBus.register(transaction);
			 System.out.println(" 1111111approve already commented out......... ");
			 eventBus.post(new Object());

    		//transaction.approve();
    	}

    	getTab().deselectAll();
    	getView().refresh();
    	//resetDescriptionsCache();
    }


}