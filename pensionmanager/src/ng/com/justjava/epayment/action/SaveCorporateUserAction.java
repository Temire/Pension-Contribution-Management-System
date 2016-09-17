package ng.com.justjava.epayment.action;

import java.util.*;
import java.util.concurrent.*;

import javax.ejb.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.model.Approver.ApprovableTransaction;
import ng.com.justjava.epayment.utility.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.view.*;

import com.google.common.eventbus.*;
import com.openxava.naviox.model.*;

public class SaveCorporateUserAction extends CollectionElementViewBaseAction {

	private boolean containerSaved = false;
	@Override
	public void execute() throws Exception {
		super.executeBefore();
		// TODO Auto-generated method stub
		
		//Map corporateMap = getCollectionElementView().getParent().getAllValues();
		Map corporateMap = saveIfNotExists(getCollectionElementView().getParent());
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n 0000000The value to be set===" 
		+corporateMap);

		Corporate corporate = (Corporate) MapFacade.findEntity("Corporate", corporateMap);
		
		Collection<Role>  roles = new ArrayList<Role>();
		List<Profile> selectedObjects = (List<Profile>) getCollectionElementView().getSubview("profiles").getCollectionSelectedObjects();
		
		for(Profile profile:selectedObjects){
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n 11111111TThe value to be set===" 
					+profile.getRole().getName());
					
			roles.add(profile.getRole());
		}
		Map userValues = (Map) getCollectionElementView().getAllValues().get("user");
		String sentName = (String) userValues.get("name");
		
/*		if(!Is.emptyString(sentName) && Is.equalAsString(sentName, "admin")){
			addError("UserName admin is Reserved: Please use other name", null);
			return;
		}*/
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n 2222222TThe value to be set===" 
		+corporateMap);
		

		System.out.println(" The object size====" + roles.size());
		

		
/*		Corporate corporate = null;
		Map corporateKey = corporateMap;
		if(corporateMap.get("name") != null && (corporateMap.get("id")==null) ||!corporateKey.containsKey("id") ){
			corporateKey = MapFacade.createReturningKey("Corporate", corporateMap);
		}*/
		
/*		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n 33333333TThe value to be set===corporateKey=" 
		+corporateKey);
		
		try {
			if(corporateKey.containsKey("id"))
				corporate = (Corporate) MapFacade.findEntity("Corporate", corporateKey);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/

		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n 44444444The value to be set===");

		//Map userValues = (Map) getCollectionElementView().getAllValues().get("user");
		
		
/*		SystemWideSetup setup = SystemWideSetup.getSystemWideSetup();
		
		if(setup == null){
			addError("SystemWide Parameter Not Yet Set", null);
			return;
		}*/
		


		
		User user = null;
		String password = "password";//RandomStringUtils.randomAlphanumeric(15);
		userValues.put("password", password);

		//String  concatUserName = (String) userValues.get("email");// + corporate.getUniqueIdentifier();
		userValues.put("name", sentName);

		try {
			user = (User) MapFacade.findEntity("User", userValues);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (user == null) {
			//String userName = (String) userValues.get("name");
			//userName = userName + corporate.getUniqueIdentifier();
			
			user = (User) MapFacade.create("User", userValues);

		
		} else {
			MapFacade.setValues("User", userValues, userValues);
		}
		if(roles.size() > 0){
			user.setRoles(roles);
			XPersistence.getManager().merge(user);
		}
System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
		+ "SaveCorporate The value to be set===" + userValues);

/*SystemWideSetup.sendMail(user.getEmail(), "Notification of User Credentials ", " Your Username is "+
		sentName + " and the password is "+ 
		password + " Please ensure to change your password during first login ");	
*/



		CorporateUser corporateUser = null;
		try {
			SystemWideSetup.sendNotification(user.getEmail(), "Notification of User Credentials ", 
					" Your Username is "+
							sentName + " and the password is "+ 
							password + " Please ensure to change your password during first login ",
							" Your Username is "+
									sentName + " and the password is "+ 
									password + " Please ensure to change your password during first login ",
									user.getPhoneNumber());

			System.out.println(" The Password==" + password + " And the UserName==" + sentName);				
			corporateUser = (CorporateUser) getCollectionElementView()
					.getEntity();
		} catch (ObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		boolean corporateUserExist = (corporateUser != null && corporateUser
				.getId() != null);

		if (!corporateUserExist) {
			corporateUser = new CorporateUser();
		}else{
			corporateUser.setEnable(true);
		}
		corporateUser.setName(user.getFamilyName() + " ," + user.getGivenName());
		corporateUser.setUser(user);
		
/*		if(corporate.getId()==null){
			corporate = XPersistence.getManager().merge(corporate);
		}*/
		
		corporateUser.setCorporate(corporate);
		corporateUser.setUserGroup(null);

		System.out.println("111\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n The value to be set for corporate"
				+ " User is enable==="+ corporateUser.isEnable() + " and id==" + corporateUser.getId());		
		if(UserManager.getCorporateOfLoginUser() == null)
			corporateUser.setEnable(true);
		
		
		corporateUser = XPersistence.getManager().merge(corporateUser);
		
		//if (!corporateUserExist) {
			Transaction transaction = new Transaction();
			transaction.setCorporate(corporateUser.getCorporate());
			
			transaction.setTransRef(ApprovableTransaction.UserManagement);
			transaction.setDateEntered(Dates.createCurrent());
			transaction.setModelName(corporateUser.getClass().getName());
			transaction.setModelId(corporateUser.getId());
			transaction.setEnteredBy(Users.getCurrent());
			if(corporateUserExist)
				transaction.setDescription(corporateUser.getName() + " Record Modified");
			else
				transaction.setDescription(corporateUser.getName() + " Record Created");
			
			
			String msg= "";
			if(UserManager.getCorporateOfLoginUser() != null){
				 msg= "Gone for Approval ";
				 System.out.println(" 00000approve already commented out......... ");
				 AsyncEventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
				 eventBus.register(transaction);
				 System.out.println(" 1111111approve already commented out......... ");
				 eventBus.post(new Object());
				 System.out.println(" 22222approve already commented out......... ");
					//transaction = transaction.approve();
					System.out
							.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n "
									+ " The Batch status here ooooooo======"
									+ transaction.getStatus());
					
			}
			



		//}
 
		
		addMessage("Corporate_User_Successfully_Saved: "+msg, null);	
		if (containerSaved) getView().getRoot().refresh(); 
		else getView().recalculateProperties();
		closeDialog();
		
		return;
	}
	protected Map saveIfNotExists(View view) throws Exception {
		if (getView() == view) {
			if (view.isKeyEditable()) {				
				Map key = MapFacade.createNotValidatingCollections(getModelName(), view.getValues());
				addMessage("entity_created", getModelName());
				view.addValues(key);
				containerSaved=true;				
				return key;								
			}			
			else {										
				return view.getKeyValues();									
			}
		}			
		else {
			if (view.getKeyValuesWithValue().isEmpty()) {				
				Map parentKey = saveIfNotExists(view.getParent());
				Map key = MapFacade.createAggregateReturningKey( 
					view.getModelName(),
					parentKey, 0,					
					view.getValues() );
				addMessage("aggregate_created", view.getModelName());
				view.addValues(key);
				return key;										
			}
			else {			
				return view.getKeyValues();
			}
		}
	}

}
