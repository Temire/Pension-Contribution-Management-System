package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.RemitPension.Months;
import ng.com.justjava.epayment.utility.*;

import org.apache.commons.collections.map.*;
import org.openxava.actions.*;
import org.openxava.util.*;

public class OnChangeViewPeriodAction extends OnChangePropertyBaseAction{

	public void execute() throws Exception {
		Months month = (Months) getView().getValue("month");
		Long corporateId = 0L;
		String pfaClause = "";
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		corporateId = corporate!=null?corporate.getId():0;
		
		if(corporate==null){
			Map corporateKey = (Map) getView().getValue("corporate");
			if(!Maps.isEmpty(corporateKey)){
				corporateId = (Long) corporateKey.get("id");
			}
			PensionFundAdministrator pfa = UserManager.getPFAOfLoginUser();
			pfaClause = " AND ${pfa.id}="+(pfa!=null?pfa.getId():0);
			
			if( pfa==null){
				Map pfaKey = (Map) getView().getValue("pfa");
				if(!Maps.isEmpty(pfaKey)){
					pfaClause = " AND ${pfa.id}="+(pfaKey.get("id"));
				}
			}
		}
		

		
		String condition = "${monthCreated}<="+ 12 + 
				" AND ${corporate.id}="+corporateId + pfaClause;
		
		System.out.println(" The full sql statement===" + condition);
		getView().getSubview("holders").getCollectionTab().setBaseCondition(condition);
		getView().getSubview("holders").refresh();
		
	}

}
