package com.openxava.naviox.actions;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

/**
 * 
 * @author Javier Paniza
 */
public class CallFolderMethodAction extends CollectionBaseAction { 
	
	private String method;
	
	public void execute() throws Exception {
		Folder folder = (Folder) getCollectionElementView().getRoot().getEntity();
		XPersistence.getManager().refresh(folder);
		XObjects.execute(folder, method, int.class, getRow());
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
