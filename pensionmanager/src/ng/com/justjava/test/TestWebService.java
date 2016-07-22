package ng.com.justjava.test;


import java.math.*;

import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.codehaus.jettison.json.*;
import org.openxava.jpa.*;

import com.openxava.naviox.impl.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;

@Path("/pension")
public class TestWebService {
 
	@Context
	private HttpServletRequest httpRequest;
	
	
    @GET
    @Path("/add/{a}/{b}")
    @Produces(MediaType.TEXT_PLAIN)
    public String addPlainText(@PathParam("a") double a, @PathParam("b") double b) {
        return (a + b) + "";
    }
     
    @GET
    @Path("/add/{a}/{b}")
    @Produces(MediaType.TEXT_XML)
    public String add(@PathParam("a") double a, @PathParam("b") double b) {
        return "<?xml version=\"1.0\"?>" + "<result>" +  (a + b) + "</result>";
    }
     
    @GET
    @Path("/sub/{a}/{b}")
    @Produces(MediaType.TEXT_PLAIN)
    public String subPlainText(@PathParam("a") double a, @PathParam("b") double b) {
        return (a - b) + "";
    }
     
    @GET
    @Path("/sub/{a}/{b}")
    @Produces(MediaType.TEXT_XML)
    public String sub(@PathParam("a") double a, @PathParam("b") double b) {
        return "<?xml version=\"1.0\"?>" + "<result>" +  (a - b) + "</result>";
    }
    
    
   @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/pfaXML/{uniqueId}")
    public Response getPFAXML(@PathParam("uniqueId") String uniqueId){
    	
    	
    	
    	
    	System.out.println("  The sent PFA Unique Id ===" + uniqueId);
    	
    	String ejbQL = "FROM PensionFundAdministrator p WHERE p.uniqueIdentifier='"+uniqueId+"'";
    	PensionFundAdministrator pfa = null;
		try {
			pfa = (PensionFundAdministrator) XPersistence.getManager().createQuery(ejbQL).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return Response.status(200).entity(pfa).build();
    } 
    
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/pfas/")
    public Response getPFAs(){ 
    	
    	PensionFundAdministrators pfas = new PensionFundAdministrators();
    	System.out.println(" REQUESTING FOR THE WHOLE pfas ....................................");
    	return Response.status(200).entity(pfas).build();
    }
    
   
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/authorization/{userName}/{password}")
    public String authorization(@PathParam("userName") String userName, 
    		@PathParam("password") String password){ 
    	System.out.println(" The call to authorization returning true here......");
    	PersonalPayment personalPayment = new PersonalPayment();
    	personalPayment.setAmount(new BigDecimal(0.00));
    	JSONObject json = new JSONObject();
    	try {
    		System.out.println("1 The sent userName == "+userName);
    		if(SignInHelper.isAuthorized(userName, password)){
    			System.out.println("2 The sent userName == "+userName);
    			RSAHolder holder = UserManager.getHolderProfileOfLoginUser(userName);
    			System.out.println("3 The sent userName == "+userName);
    			json.put("authorized", true);
    			json.put("userName", userName);
    			json.put("password", password);
    			json.put("fullName", holder.getFullName());
    			
    			
    			if(holder !=null){
    				json.put("administrator",(holder.getPfa()!=null? holder.getPfa().getUniqueIdentifier():"Un Available"));
    				System.out.println("4 The sent userName == "+userName + " holder=="+holder.getPfa());
    			}
    				System.out.println("5 The sent userName == "+userName);
    		}else{
    			json.put("authorized", false);
    		}
	    	System.out.println(" Object Here =="+json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

    	return json.toString();
    	//return !SignInHelper.isAuthorized(userName, password);
    	 
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/pfa/{uniqueIdentifier}")
    public String getPFA(@PathParam("uniqueIdentifier") String uniqueIdentifier, 
    		@PathParam("password") String password){ 
    	System.out.println(" The call to authorization returning true here......");
    	PensionFundAdministrator administrator = PensionFundAdministrator.
    														findPFAByUniqueIdentifier(uniqueIdentifier);
    	JSONObject json = new JSONObject();
    	try {
    		if(administrator != null){
    			json.put("name", administrator.getName());
    			//json.put("", userName);
    			//json.put("password", password);
    		}else{
    			json.put("authorized", false);
    		}
	    	System.out.println(" Object Here =="+json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

    	return json.toString();
    	//return !SignInHelper.isAuthorized(userName, password);
    	 
    }        
 /*  
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/makePayment")
    public String paymentFile(MakePayment payment){

    	System.out.println(" payment Sent===" + payment.getCollectionItem());
    	return "OK";
    }
    
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/payment")
    public String paymentFile(byte[] content){
    	
    	String response = "OK";
    	
    	
    	System.out.println(" The Token exchanged Here==== " + httpRequest.getHeader("token"));
    	
    	String encryptCorporate =  httpRequest.getHeader("corporate");
    	String key = XavaPreferences.getInstance().getXavaProperty("symmetryKey", "Pct9GF1sbINbHxE7CKslwQ==");
    	String decryptCorporate =  null;
    	
    	try {
			decryptCorporate = Cryptor.decrypt(encryptCorporate, key);
			
			if(!Is.equalAsStringIgnoreCase("ChamsPayroll", decryptCorporate))
				return "UNKNOWN SITE";
			else
				return "OK";
		} catch (KeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidAlgorithmParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalBlockSizeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BadPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	try {
    		File file = new File("paymentupload"+System.currentTimeMillis()+".csv");

    		java.nio.file.Files.write(file.toPath(), content, java.nio.file.StandardOpenOption.CREATE);
    		
    		CSVStrategy strategy = new CSVStrategy(',', '"', '#', true, true);
    		
    		System.out.println("000000 It is finally fired..................................");
			if(file.getName().toLowerCase().contains("csv")){
				//file = new File("data/paymentupload.csv");
				
				FileInputStream fis = new FileInputStream(file);
				Reader csvFile = new InputStreamReader(fis);

				System.out.println("1111111 It is finally fired..................................");
				
				
				ValueProcessorProvider vpp = new ValueProcessorProvider();
				vpp.registerValueProcessor(Account.class, new AccountValueProcessor());
				vpp.registerValueProcessor(PaymentInstruction.class,
						new PaymentInstructionValueProcessor());
				vpp.registerValueProcessor(Bank.class,	new BankValueProcessor());
				vpp.registerValueProcessor(BigDecimal.class,new BigDecimalValueProcessor());
				
				String className="ng.com.justjava.epayment.model.PaymentUpload";
				System.out.println(" The Model name aroun here===" + className);
				
				System.out.println("22222222 It is finally fired..................................");
				Class cls = Class.forName(className);
				
				Method method = cls.getDeclaredMethod("saveUpload", Reader.class,CSVStrategy.class,
						ValueProcessorProvider.class);
				System.out.println("3333333 It is finally fired..................................");
				//method.invoke(null, csvFile,strategy,vpp);
				Object result = method.invoke(null, csvFile,strategy,vpp);
				
				response = String.valueOf(result);

				
				//vpp.registerValueProcessor(BigDecimal.class,new BigDecimalValueProcessor());


			}    		
			//FileOutputStream out = new FileOutputStream(file);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return response;
    }
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/customer/{customerId}")
    public Response getCustomer(@PathParam("customerId") String customerId){
    	Customer customer = new Customer();
    	customer.setCustomerId("111111");
    	customer.setStateIdentification("222222");
    	User user = new User();
    	user.setName("kazeem");
    	user.setPassword("password");
    	user.setEmail("akinrinde@justjava.com.ng");
    	user.setGivenName("kazeem");
    	user.setFamilyName("Akinrinde");
    	customer.setUser(user);
 
    	System.out.println(" REQUESTING FOR THE Customer ....................................");
    	return Response.status(200).entity(customer).build();
    }
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/collection/{collectionRef}")
    public Response getCollection(@PathParam("collectionRef") String collectionRef){
    	MakePayment payment = new MakePayment();
    	payment.setAmount(new BigDecimal(0.00));
    	payment.setIdentifier("TIN");
    	payment.setValue("120920922");
    	System.out.println(" REQUESTING FOR THE Payment ....................................");
    	return Response.status(200).entity(payment).build();
    } 
    
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/avs/{number}/{name}/{bankCode}")
    public String getAccountName(@PathParam("number") String number,
    		@PathParam("name") String name,@PathParam("bankCode") String bankCode){


		try {
	    	Account account = new Account();
	    	Bank bank = new Bank();
	    	bank.setCode(bankCode);
	    	account.setId(System.currentTimeMillis());
	    	account.setName(name);
	    	account.setNumber(number);
	    	account.setBank(bank);

	    	String accountName = account.getNibssFetchedAccountName();
	    	System.out.println(" Account Verification Result ===" + accountName);
	    	if(accountName !=null)
	    		return accountName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return "Unable to Verify the Account";
    }    
*/    
}