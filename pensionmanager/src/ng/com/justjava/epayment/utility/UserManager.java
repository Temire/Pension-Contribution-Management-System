package ng.com.justjava.epayment.utility;

import java.util.*;

import org.openxava.jpa.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

import ng.com.justjava.epayment.model.*;

public class UserManager {
	final private static ThreadLocal current = new ThreadLocal(); 

	
	
	
	public static boolean loginUserIsAgent(){
		String userName = Users.getCurrent();
		userName = userName!=null?userName.toLowerCase():userName;
		String ejbQL = " FROM Agent a WHERE a.user.name='"+userName+"'";
		List<Agent> agents = XPersistence.getManager().createQuery(ejbQL).getResultList();
		
		return (agents!=null && !agents.isEmpty());
	}
	public static boolean loginUserHasRole(String roleName){
		if(Is.empty(roleName))
			return false;
		boolean response = false;
		CorporateUser loginUser = getLoginCorporateUser();
		if(loginUser == null)
			return false;
		User user = loginUser.getUser();
		for (Role role : user.getRoles()) {
			if(Is.equalAsStringIgnoreCase(roleName, role.getName()))
				return true;
		}
		return response;
	}
	
	public static Biller getBillerOfUser(String user){
		BillerUser billerUser = null;
		Biller biller = null;
		try {
			String ejbQL = " FROM BillerUser b "
					+ " WHERE b.user.name='"+ user + "'";
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n QUERY HERE"+ejbQL);			

			
			billerUser = (BillerUser)XPersistence.getManager().createQuery(ejbQL).getSingleResult();
			biller = billerUser.getBiller();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return biller;
	}
	
	public static Corporate getCorporateOfUser(String user){
		CorporateUser corporateUser = null;
		Corporate corporate = null;
		try {
			String ejbQL = " FROM CorporateUser c "
					+ " WHERE c.user.name='"+ user + "'";
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n QUERY HERE"+ejbQL);			

			ArrayList<CorporateUser> corporateUsers = (ArrayList<CorporateUser>) 
					XPersistence.getManager().createQuery(ejbQL).getResultList();
			if(!corporateUsers.isEmpty()){
				corporateUser = corporateUsers.get(0);
				corporate = corporateUser.getCorporate();
			}
			System.out.println(" The corporate I am returning here==== " + corporate);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return corporate;
	}	
	
/*	public static PensionFundAdministrator getPFCOfLoginUser(){
		PFAUser user = null;
		PensionFundAdministrator pfa = null;
		try {
			String ejbQL = " FROM PFAUser c "
					+ " WHERE c.user.name='"+ user + "'";
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n QUERY HERE"+ejbQL);			

			
			user = (PFAUser)XPersistence.getManager().createQuery(ejbQL).getSingleResult();
			pfa = user.getPfa();
			
			System.out.println(" The corporate I am returning here==== " + pfa);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pfa;
	}*/
	
	public static Corporate getCorporateOfLoginUser(){
		String userName = Users.getCurrent();
		userName = userName!=null?userName.toLowerCase():userName;
		Corporate corporate = getCorporateOfUser(userName);
		System.out.println(" Inside getCorporateOfLoginUser The corporate I am returning "
				+ " here==== " + corporate);
		
		return corporate;
	}
	
	public static Biller getBillerOfLoginUser(){
		Biller biller = null;
		String userName = Users.getCurrent();
		userName = userName!=null?userName.toLowerCase():userName;		
		Agent agent = Agent.getAgentByUserName(userName);
		if(agent!=null)
			biller = agent.getBiller();
		else
			biller = getBillerOfUser(Users.getCurrent());
		
		return biller;
	}

	
	public static RSAHolder getHolderProfileOfLoginUser(){

		ArrayList<RSAHolder> holders = null;
		RSAHolder holder =  null;
		try {
			String userName = Users.getCurrent();
			userName = userName!=null?userName.toLowerCase():userName;		
			
			String ejbQL = " FROM RSAHolder p "
					+ " WHERE p.user.name='"+ userName + "'";
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n RSAHolder "+ejbQL);		
			holders = (ArrayList<RSAHolder>)XPersistence.getManager().createQuery(ejbQL).getResultList();
			holder = (!holders.isEmpty())?holders.get(0):null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return holder;
	}	
	public static RSAHolder getHolderProfileOfLoginUser(String userName){

		ArrayList<RSAHolder> holders = null;
		RSAHolder holder =  null;
		try {
			userName = userName!=null?userName.toLowerCase():userName;		
			
			String ejbQL = " FROM RSAHolder p "
					+ " WHERE p.user.name='"+ userName + "'";
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n RSAHolder "+ejbQL);		
			holders = (ArrayList<RSAHolder>)XPersistence.getManager().createQuery(ejbQL).getResultList();
			holder = (!holders.isEmpty())?holders.get(0):null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return holder;
	}
	
	public static PFAUser getPFAUserProfileOfLoginUser(){

		PFAUser pfaUser = null;
		try {
			String userName = Users.getCurrent();
			userName = userName!=null?userName.toLowerCase():userName;		
						
			String ejbQL = " FROM PFAUser p "
					+ " WHERE p.user.name='"+ userName+ "'";
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n RSAHolder "+ejbQL);		
			pfaUser = (PFAUser)XPersistence.getManager().createQuery(ejbQL).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pfaUser;
	}		
	public static PensionFundAdministrator getPFAOfLoginUser(){
		PensionFundAdministrator pfa = null;
		PFAUser pfaUser = null;
		try {
			String userName = Users.getCurrent();
			userName = userName!=null?userName.toLowerCase():userName;		
								
			String ejbQL = " FROM PFAUser p "
					+ " WHERE p.user.name='"+ userName + "'";
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n QUERY HERE"+ejbQL);			

			
			pfaUser = (PFAUser)XPersistence.getManager().createQuery(ejbQL).getSingleResult();
			pfa = pfaUser.getPfa();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pfa;
	}	
	public static PensionFundCustodian getPFCOfLoginUser(){
		PensionFundCustodian pfc = null;
		PFCUser pfcUser = null;
		try {
			String userName = Users.getCurrent();
			userName = userName!=null?userName.toLowerCase():userName;		
									
			String ejbQL = " FROM PFCUser p "
					+ " WHERE p.user.name='"+ userName + "'";
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n QUERY HERE"+ejbQL);			

			
			pfcUser = (PFCUser)XPersistence.getManager().createQuery(ejbQL).getSingleResult();
			pfc = pfcUser.getPfc();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pfc;
	}	
	public static String getLoginUserRoleForQuery(String prefix,String propertyName){
		CorporateUser user = getLoginCorporateUser();
		if(user==null)
			return "";
		String result = "(";
		Role[] roles =  user.getUser().getRoles().toArray(new Role[0]);
		for(int i = 0; i < roles.length-1;i++ ){
			result = result +prefix+propertyName+"='"+roles[0].getName() + "' OR ";
		}
		
		//.role.name
		result = result +prefix+propertyName +"='"+roles[roles.length-1].getName() + "')";
		
		String ejbQL = " FROM Profile c WHERE c.corporate.id="+user.getCorporate().getId()+
		" AND " + result;
		
		System.out.println(" The final getLoginUserRoleForQuery ==" + result + " the full query=="+ ejbQL);
		return result;
	}
	
	public static boolean isLoginUserApprover(){
		boolean result = false;
		Corporate corporate = getCorporateOfLoginUser();
		if(corporate == null){
			return false;
		}
		
		List<Profile> profile =(List<Profile>)XPersistence.getManager().
				createQuery(" FROM Profile c WHERE c.corporate.id="+corporate.getId()+
						"AND (c.transaction=1 OR c.transaction=3) AND c.approver=1 AND " + UserManager.getLoginUserRoleForQuery("c.","role.name")).getResultList();
		if(profile !=null && profile.size() > 0)
			result=true;
		
		return result;
	}
	public static boolean approverExists(int transNumber){
		boolean result = false;
		Corporate corporate = getCorporateOfLoginUser();
		if(corporate == null){
			return false;
		}
		
		List<Profile> profile =(List<Profile>)XPersistence.getManager().
				createQuery(" FROM Profile c WHERE c.level=1 AND c.corporate.id="+corporate.getId()
						+" AND (c.transaction="+transNumber +" OR c.transaction=3)").getResultList();
		if(profile !=null && profile.size() > 0)
			result=true;
		
		return result;
	}
	
	
	public static Profile getFirstApprover(int transNumber){
		

		Profile result = null;
		User  approver = null;
		Corporate corporate = getCorporateOfLoginUser();
	
		if(corporate==null){
			return null;
		}
		try {
			result = (Profile)XPersistence.getManager()
					.createQuery(" FROM Profile c WHERE c.corporate.id="+ corporate.getId() + " AND c.level=1"
							+ " AND (c.transaction="+transNumber + " OR c.transaction=3)")
					.getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public static CorporateUser getCorporateUser(String userName){
		CorporateUser corporateUser = null;

		try {
			corporateUser = (CorporateUser)XPersistence.getManager().createQuery(" FROM CorporateUser c "
					+ " WHERE c.user.name='"+ userName + "'").getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return corporateUser;
	} 
	public static CorporateUser getLoginCorporateUser(){
		CorporateUser corporateUser = null;

		try { 
			String userName = Users.getCurrent();
			userName = userName!=null?userName.toLowerCase():userName;		
							
			corporateUser = (CorporateUser)XPersistence.getManager().createQuery(" FROM CorporateUser c "
					+ " WHERE c.user.name='"+ userName+ "'").getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return corporateUser;
	}

}
