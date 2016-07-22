package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

import com.openxava.naviox.model.*;

public class DisableCorporateAction extends TabBaseAction{

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		Map[] selectedKeys = getTab().getSelectedKeys();

		for(Map key : selectedKeys){
			Corporate corporate = (Corporate) MapFacade.findEntity("Corporate", key);
			corporate.setDisabled(true);
			XPersistence.getManager().merge(corporate);
		}
		
		getTab().deselectAll();
		addMessage("Corporate disabled successfully", null);
	}

}
