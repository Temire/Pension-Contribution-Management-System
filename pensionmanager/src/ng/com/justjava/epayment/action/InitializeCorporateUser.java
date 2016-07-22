package ng.com.justjava.epayment.action;

import java.util.*;

import ng.com.justjava.epayment.model.*;

import org.apache.commons.collections.*;
import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.model.*;

import com.openxava.naviox.model.*;

public class InitializeCorporateUser extends  EditElementInCollectionAction {

	public void execute() throws Exception {
		// TODO Auto-generated method stub
		getCollectionElementView().setViewName("forEdit");
		super.execute();
		try {
			
			CorporateUser corporateUser =(CorporateUser) MapFacade.findEntity("CorporateUser", getCollectionElementView().getKeyValues());
			String condition="${universal}=1 OR ${corporate.id}="+corporateUser.getCorporate().getId();
			
			
			System.out.println(" The condition I am testing here ==========" + condition);
			
			
			getCollectionElementView().getSubview("profiles").getCollectionTab().setBaseCondition(condition);
			getCollectionElementView().getSubview("profiles").refreshCollections();
			List<Profile> profiles = getCollectionElementView().getSubview("profiles").getCollectionObjects();
			
			
			List<Role> roles = new ArrayList<Role>();
			//List<Role> intersection = new ArrayList<Role>();
			for(Profile eachProfile:profiles){
				roles.add(eachProfile.getRole());
			}
			Collection<Role> intersection = CollectionUtils.intersection(corporateUser.getUser().getRoles(), roles);
			Map[] keys =  new HashMap[intersection.size()];
			
			int count = 0;
			for (Profile profile:profiles) {
				if(intersection.contains(profile.getRole())){
					Map profileKey = new HashMap();
					profileKey.put("id", profile.getId());
					keys[count]=profileKey;
					count = count + 1;
				}

			}
			
			
			getCollectionElementView().getSubview("profiles").getCollectionTab().setAllSelectedKeys(keys);
			resetDescriptionsCache();
/*			System.out.println(" Here we are at last element size " + keys.length + " while the whole object is "+
			getCollectionElementView().getAllValues());*/
						
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//List<Profile> selectedObjects = (List<Profile>) 

	}

}
