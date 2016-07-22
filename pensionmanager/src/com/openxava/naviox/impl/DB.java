package com.openxava.naviox.impl;

import java.util.*;

import javax.persistence.*;

import org.apache.commons.logging.*;
import org.hibernate.cfg.*;
import org.hibernate.dialect.*;
import org.hibernate.ejb.*;
import org.openxava.application.meta.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import com.openxava.naviox.model.*;

/**
 * 
 * @author Javier Paniza
 */
public class DB {
	
	private static Log log = LogFactory.getLog(DB.class);
	
	
	public static void init() {
		try {			
			createModules();			
		}
		catch (PersistenceException ex) {
			ex.printStackTrace();
			log.info(XavaResources.getString("xavapro_first_time_execution"));
			XPersistence.rollback();
			createDB();
			createModules();
			populateJustCreatedDB();
		}
	}
	
	private static void createModules() { 
		Collection<MetaModule> inApp = MetaModuleFactory.createAll();
		Collection<Module> inDB = Module.findAll();
		try {
			for (Iterator<MetaModule> it = inApp.iterator(); it.hasNext(); ) {
				MetaModule metaModule = it.next();
				Module module = Module.findByMetaModule(metaModule);
				if (module == null) {
					module = Module.createFromMetaModule(metaModule);
				}
				inDB.remove(module);
			}
			for (Module module: inDB) {
				XPersistence.getManager().remove(module); 
			}	
			XPersistence.commit();
		}
		catch (Exception ex) { 
			log.warn(XavaResources.getString("updating_modules_database_problem"), ex);
			XPersistence.rollback();
		}
	}
	
	private static void createDB() {
		log.info(XavaResources.getString("creating_xavapro_tables")); 
		Ejb3Configuration ejb3cfg = new Ejb3Configuration();
		ejb3cfg.addAnnotatedClass(User.class);
		ejb3cfg.addAnnotatedClass(Role.class);
		ejb3cfg.addAnnotatedClass(Module.class);
		ejb3cfg.addAnnotatedClass(Folder.class);
		ejb3cfg.addAnnotatedClass(ModuleRights.class);
		Configuration cfg = ejb3cfg.getHibernateConfiguration();
		Properties props = new Properties();
		Map<String, Object> factoryProperties = XPersistence.getManager().getEntityManagerFactory().getProperties();
		String dialect = (String) factoryProperties.get("hibernate.dialect");
    	props.put("hibernate.dialect", dialect);
    	String schema = (String) factoryProperties.get("hibernate.default_schema"); 
		String [] scripts = cfg.generateSchemaCreationScript(Dialect.getDialect(props));
		for (String script: scripts) {
			if (!Is.emptyString(schema)) {
				script = script.replaceAll(" OX", " " + schema + ".OX");
				script = script.replaceAll(" FK", " " + schema + ".FK");
			}
			log.info(XavaResources.getString("executing") + ": " + script);
			Query query = XPersistence.getManager().createNativeQuery(script);
			query.executeUpdate();
		}
	}
	
	private static void populateJustCreatedDB() {
		log.info(XavaResources.getString("creating_default_user_roles_configuration"));
		Collection<Role> roles = new ArrayList<Role>();
		Role adminRole = new Role();
		adminRole.setName("admin");
		roles.add(adminRole);
		
		Role userRole = new Role();		
		userRole.setName("user");
		roles.add(userRole);
		
		User admin = new User();		
		admin.setName("admin");
		admin.setPassword("admin");		
		admin.setRoles(roles);
		
		Folder adminFolder = new Folder();
		adminFolder.setName("Admin"); 
		
		Collection<Module> adminModules = new ArrayList<Module>();
		Collection<Module> userModules = new ArrayList<Module>();		
		for (Module module: Module.findAll()) {
			if (module.getName().equals("User") || 
				module.getName().equals("Role") || 
				module.getName().equals("Module") ||
				module.getName().equals("Folder") ||
				module.getName().equals("ModuleRights"))  
			{
				adminModules.add(module);
				module.setFolder(adminFolder);
				if (module.getName().equals("ModuleRights")) {
					module.setHidden(true);					
				}
			}
			else {
				userModules.add(module);
				if (module.getName().equals("ChangePassword")) {
					module.setFolder(adminFolder);
				}
			}
		}
		adminRole.setModules(adminModules);
		userRole.setModules(userModules);
		
		
		XPersistence.getManager().persist(adminRole);
		XPersistence.getManager().persist(userRole);
		XPersistence.getManager().persist(admin);		
		XPersistence.getManager().persist(adminFolder); 
		XPersistence.commit();
	}
			
}
