package ng.com.justjava.epayment.action;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.model.*;

public class PersistUserAction extends OnChangePropertyBaseAction{

	public void execute() throws Exception {
		
		System.out.println("000000000\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
				+ " All the values in on change of user here ==" + getView().getAllValues());
		Map values = getView().getAllValues();
		if(!values.isEmpty()){
			System.out.println("11111111111\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
					+ " All the values in on change of user here ==" + getView().getAllValues());			
			
			MapFacade.create("User", values);
			
			System.out.println("222222222\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
					+ " All the values in on change of user here ==" + getView().getAllValues());			
		}
		// TODO Auto-generated method stub
		
	}

}
