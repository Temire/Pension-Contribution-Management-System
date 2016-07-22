package ng.com.justjava.epayment.action;

import java.util.*;

import javax.ejb.*;

import ng.com.justjava.epayment.model.*;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.validators.*;

public class SaveRSAHolderAction extends SaveAction {
	
private boolean resetAfter = false;
private boolean refreshAfter = false; 

public void execute() throws Exception {	
	super.execute();
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
