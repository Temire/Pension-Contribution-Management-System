package ng.com.justjava.epayment.utility;

import com.etranzact.fundgate.ws.*;



public class WebserviceUtil {
	public static FundGate getPort() throws java.rmi.RemoteException{
		FundGateImplService service = new FundGateImplService();
        FundGate port = service.getFundGateImplPort();
		return port;
	}
}
