package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

import org.openxava.actions.*;
import org.openxava.util.*;

public class ViewPFADetailAction extends OnSelectElementBaseAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		//List list = getSelectedObjects();
		PensionFundAdministrator  pfa = (PensionFundAdministrator) 
				getCollectionElementView().getCollectionObjects().get(getRow());

		
		showDialog();
		
		
		
		if(pfa==null)
			return;
		
		Map key = new HashMap();
		key.put("id", pfa.getId());
		
		
		getRequest().getSession().setAttribute("pfaId", pfa.getId());
		
		
		getView().setModelName("PensionFundAdministrator"); // 2
		getView().setViewName("pfaRecordView");
		getView().setValues(key); // 3
		getView().findObject(); // 4
		getView().getSubview("companies").addListAction("RowAction2.confirm");
		getView().setKeyEditable(false);
		
		getRequest().getSession().setAttribute("pfaId", pfa.getId());
		
	}

}
