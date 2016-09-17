package ng.com.justjava.epayment.action;

import java.io.*;
import java.lang.reflect.*;
import java.math.*;
import java.util.*;
import java.util.concurrent.*;

import javax.inject.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.RemitPension.Months;
import ng.com.justjava.epayment.utility.*;

import org.apache.commons.fileupload.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;

import com.googlecode.jcsv.*;
import com.googlecode.jcsv.annotations.internal.*;

//import com.google.common.eventbus.*;

public class ProcessLoadedFile extends ViewBaseAction implements INavigationAction, IProcessLoadedFileAction {
	
	private List fileItems;
	@Inject
	private String newImageProperty;
	

	public void execute() throws Exception {
		Iterator i = getFileItems().iterator();
		Object result = null;
		String strResult = "";
		String modelName = getPreviousView().getModelName() ;
		
		
		Corporate corporate = UserManager.getCorporateOfLoginUser();
		Months month = (Months) getPreviousView().getValue("month");
		
		System.out.println(" The model name around here ==="+modelName);
		if(Is.equalAsStringIgnoreCase("MonthlyUpload", modelName)){
			modelName = "RSAHolder";
		}

		
		while (i.hasNext()) {
			FileItem fi = (FileItem)i.next();
			String fileName = fi.getName();		
			try {
				File file = null;
				if (!Is.emptyString(fileName)) {
					CSVStrategy strategy = new CSVStrategy(',', '"', '#', true, true);
					if(fileName.toLowerCase().contains("csv")){
						file = new File("data/"+fileName+" " + System.currentTimeMillis()+" "+
								(corporate!=null?corporate.getUniqueIdentifier():corporate)+ ".csv");
						fi.write(file);	
						FileInputStream fis = new FileInputStream(file);
						Reader csvFile = new InputStreamReader(fis);
  
						ValueProcessorProvider vpp = new ValueProcessorProvider();
						vpp.registerValueProcessor(Account.class, new AccountValueProcessor());
						vpp.registerValueProcessor(PayItemCollection.class, new RSAPayItemValueProcessor());
						vpp.registerValueProcessor(PensionFundAdministrator.class, new PFAValueProcessor());
						
						vpp.registerValueProcessor(PaymentInstruction.class,
								new PaymentInstructionValueProcessor());
						vpp.registerValueProcessor(Bank.class,	new BankValueProcessor());
						vpp.registerValueProcessor(BigDecimal.class,new BigDecimalValueProcessor());
						
						String className="ng.com.justjava.epayment.model."+modelName;
						System.out.println(" The Model name aroun here===" + className);
						Class cls = Class.forName(className);
						
						Method method = cls.getDeclaredMethod("saveUpload", Reader.class,CSVStrategy.class,
								ValueProcessorProvider.class,Object.class);
						
						//method.invoke(null, csvFile,strategy,vpp);
						result = method.invoke(cls.newInstance(), csvFile,strategy,vpp,month);
						
						strResult = String.valueOf(result);
	
						
						//vpp.registerValueProcessor(BigDecimal.class,new BigDecimalValueProcessor());


					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		

		
		
		
		if(strResult!= null && strResult.toLowerCase().indexOf("error")>=0){
			addError(strResult, null);
			//return;
		}else{
			addMessage(strResult, null);
		}
		closeDialog(); 
		//setNextMode(LIST);
	}
	
	private View findView() { 
		String property = getNewImageProperty(); 
		String []m = property.split( "\\." );   
		View result = getPreviousView(); 
		for( int i = 0; i < m.length-1; i++ ) { 
			result = result.getSubview(m[i]); 
		} 		
		return result; 
	} 
		
	public String[] getNextControllers() {				
		return PREVIOUS_CONTROLLERS;
	}

	public String getCustomView() {		
		return PREVIOUS_VIEW;
	}

	public String getNewImageProperty() {
		return newImageProperty;
	}

	public void setNewImageProperty(String string) {
		newImageProperty = string;	
	}

	public List getFileItems() {
		return fileItems;
	}

	public void setFileItems(List fileItems) {
		this.fileItems = fileItems;
	}

}