package ng.com.justjava.epayment.action;

import java.math.*;
import java.util.*;

import javax.ejb.*;

import ng.com.justjava.epayment.model.*;

import org.apache.commons.collections.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.validators.*;

public class SaveRSAHolderAction extends ViewBaseAction {
	
private boolean resetAfter = false;
private boolean refreshAfter = false; 

public void execute() throws Exception {	
	System.out.println(" The SaveRSAHolderAction Here");
	String email = getView().getValueString("email");
	BigDecimal voluntary = (BigDecimal) getView().getValue("voluntaryDonation");
	 
	System.out.println(" The values are =="+getView().getValues());
	try {
		RSAHolder holder = null;
		Collection<RSAHolder> holders = XPersistence.getManager().
								createQuery("FROM RSAHolder r WHERE r.email='"+email+"' ORDER BY r.upload.id desc")
								.getResultList();
		if(!holders.isEmpty()){
			System.out.println(" 0 Super The SaveRSAHolderAction Here");
			holder = (RSAHolder) CollectionUtils.get(holders, 0);
			
			System.out.println(" 1 Super The SaveRSAHolderAction Here voluntary==="+voluntary);
			holder.setVoluntaryDonation(voluntary);
			XPersistence.getManager().merge(holder);
			XPersistence.commit();
			System.out.println(" After committing The upload id ="+holder.getUpload().getId());
		}
		addMessage("Successfully Saved", null);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		addError("Error Saving RSAHolder", null);
	}
	System.out.println(" After Super The SaveRSAHolderAction Here");
	//System.out.println(" rsa holder to save map == "+getValuesToSave());
}

protected Map getValuesToSave() throws Exception {	
	Map values = getView().getValues();
	values.put("dirty", true);
	return values;		
}

/**
 * If <tt>true</tt> reset the form after save, else refresh the
 * form from database displayed the recently saved data. <p>
 * 
 * The default value is <tt>true</tt>.
 */
public boolean isResetAfter() {
	return resetAfter;
}

/**
 * If <tt>true</tt> reset the form after save, else refresh the
 * form from database displayed the recently saved data. <p>
 * 
 * The default value is <tt>true</tt>.
 */
public void setResetAfter(boolean b) {
	resetAfter = b;
}

/**
 * If <tt>false</tt> after save does not refresh the
 * form from database. <p>
 * 
 * It only has effect if <tt>resetAfter</tt> is <tt>false</tt>.
 * 
 * The default value is <tt>true</tt>.
 * 
 * @since 4.8
 */	
public boolean isRefreshAfter() {
	return refreshAfter;
}

/**
 * If <tt>false</tt> after save does not refresh the
 * form from database. <p>
 * 
 * It only has effect if <tt>resetAfter</tt> is <tt>false</tt>.
 * 
 * The default value is <tt>true</tt>.
 * 
 * @since 4.8
 */		
public void setRefreshAfter(boolean refreshAfter) {
	this.refreshAfter = refreshAfter;
}

}
