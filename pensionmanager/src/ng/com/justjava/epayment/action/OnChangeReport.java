package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.ReportControllerPFA.Months;
import ng.com.justjava.epayment.model.ReportControllerPFA.Report;

import org.openxava.actions.*;

public class OnChangeReport extends  OnChangePropertyBaseAction {
	public void execute() throws Exception {
				
		Report report = (Report) getView().getValue("report");
		
		if(report.ordinal() == 1)
		{
			
			getView().setHidden("toyear", true);
			getView().setHidden("from", true);
		}else{
			getView().setHidden("toyear", false);
			getView().setHidden("from", false);
		}
		//getView().refresh();
		//returnToPreviousControllers();
	}

}
