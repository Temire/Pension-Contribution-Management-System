package com.openxava.naviox.impl;

import java.util.*;

import org.openxava.application.meta.*;
import org.openxava.controller.*;
import org.openxava.controller.meta.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;


/**
 * Actions refiner, to remove the unauthorized actions.
 * 
 * @author Javier Paniza
 */
public class ActionsRefiner {
	
	private static ActionsRefiner instance;

	public void refine(MetaModule metaModule, Collection metaActions) {
		String currentUser = Users.getCurrent();
		if (currentUser == null) return;
		User user = User.find(currentUser);
		Collection<MetaAction> excludedActions =  user.getExcludedMetaActionsForMetaModule(metaModule);	
		for (MetaAction action: excludedActions) {		
			metaActions.remove(action);
		}
	}

	public static void init() {
		if (instance == null) instance = new ActionsRefiner();
		ModuleManager.setRefiner(instance);
	}
	
}
