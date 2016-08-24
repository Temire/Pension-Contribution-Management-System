package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

import com.openxava.naviox.model.*;

public class RegisterAction extends ViewBaseAction {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub

		
		Map allValues = getView().getAllValues();
		System.out.println(" All Values ==" + allValues);
		RSAHolder holder = RSAHolder.findByPencommNumber(getView().getValueString("PIN"));
		
		System.out.println(" The all values =="+ allValues);
		
		
		if(holder!=null){
			addError("RSA Holder Already Registered in the System");
			return;
		}
		
		PensionFundAdministrator pfa = (PensionFundAdministrator) 
				MapFacade.findEntity("PensionFundAdministrator", (Map)allValues.get("pfa"));
		
		holder = new RSAHolder();
		holder.setFirstName(getView().getValueString("firstName"));
		holder.setPfa(pfa);
		holder.setPencommNumber(getView().getValueString("PIN"));
		holder.setEmail(getView().getValueString("email"));
		holder.setSecondName(getView().getValueString("lastName"));
		holder.setPhoneNumber(getView().getValueString("phoneNumber"));
		
		
		System.out.println("1 The loaded holder==" + holder.getFirstName());

		System.out.println("2 The loaded holder==" + holder.getSecondName());
		holder.setCorporate(null);

		holder.setItems(null);
		System.out.println(" The Pension amount here in upload ==="
				+ holder.getPensionAmount());


		String roleQuery = "FROM Role r where r.name='RSAHolder'";
		String moduleQuery = "FROM Module m WHERE m.name='ViewMyContribution' ";

		List<Role> rsaHolderRole = XPersistence.getManager()
				.createQuery(roleQuery).getResultList();
		if (rsaHolderRole == null || rsaHolderRole.isEmpty()) {
			Role rsaHolder = new Role();
			rsaHolder.setName("RSAHolder");
			// XPersistence.getManager().merge(billerAdmin);
			List<Module> viewMyContribution = XPersistence.getManager()
					.createQuery(moduleQuery).getResultList();
			rsaHolder.setModules(viewMyContribution);
			rsaHolder = XPersistence.getManager().merge(rsaHolder);
			rsaHolderRole.add(rsaHolder);
		}

		System.out.println(" ABout To attach user to RSAHolder ");

		User innerUser = User.find(holder.getEmail());
		String password = null;
		if (innerUser == null) {
			innerUser = new User();
			innerUser.setRoles(rsaHolderRole);
			innerUser.setName(holder.getEmail());
			innerUser.setEmail(holder.getEmail());
			innerUser.setFamilyName(holder.getSecondName());
			innerUser.setGivenName(holder.getFirstName());
			innerUser.setPhoneNumber(holder.getPhoneNumber());
			password = RandomStringUtils.randomAlphanumeric(10);
			innerUser.setPassword(password);
			innerUser = XPersistence.getManager().merge(innerUser);
		}

		System.out.println(" There is attachment of user and others here");

		holder.setUser(innerUser);
		System.out
				.println("1 The Pension amount here in upload second time ==="
						+ holder.getPensionAmount());
		holder = XPersistence.getManager().merge(holder);
		XPersistence.getManager().merge(holder);	
		
		SystemWideSetup.sendMail( innerUser.getEmail(), "User Credentials", 
				" Your New Password Generated is: "+ password + " And User Name is "+ innerUser.getName());	
		//addMessage("Your New Password Has been forwarded to your Mail ", null);		
		closeDialog();
		
		addMessage(
				"Personal Pension Contribution Created Successfully. Credentials Sent to your mail");
	}

}
