package com.openxava.naviox.model;

import java.io.*;
import java.math.*;
import java.security.*;
import java.util.*;

import javax.naming.*;
import javax.naming.directory.*;
import javax.persistence.*;
import javax.xml.bind.annotation.*;

import org.apache.commons.collections.*;
import org.apache.commons.logging.*;
import org.openxava.annotations.*;
import org.openxava.application.meta.*;
import org.openxava.calculators.*;
import org.openxava.controller.meta.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

/**
 *
 * @author Javier Paniza
 */

@Entity
@Table(name="OXUSERS")
@Views({@View(members=
"name;" +
"password;" +
"repeatPassword;" +

"personalData [" +
"	givenName;" +
"	familyName;" +
"	email;"+ 
"	phoneNumber;"	+ 
"	address;" +
"	jobTitle;" +
"]; roles"
),@View(name="full",members=
"name;password; " +
"personalData [" +
"	givenName;" +
"	familyName;" +
"	email;"+ 
"	phoneNumber;"	+ 
"	address;" +
"	jobTitle" +
"];"
),@View(name="fullWithoutPassword",members=
"name;" +
"personalData [" +
"	givenName;" +
"	familyName;" +
"	email;"+ 
"	phoneNumber;"	+ 
"	address;" +
"	jobTitle;" +
"];"
), @View(name="approver" , members=
"name;" +
"personalData [" +
"	givenName;" +
"	familyName;" +
"	email;"+ 
"	phoneNumber;"	+ 
"	address;" +
"	jobTitle;" +
"];"
),@View(name="withoutName" , members=
"name;" +
"personalData [" +
"	givenName;" +
"	familyName;" +
"	email;"+ 
"	phoneNumber;"	+ 
"	address;" +
"	jobTitle;" +
"];"
),
@View(name="customer" , members=
"name;" +
"personalData [" +
"	givenName;" +
"	familyName;" +
"	email;"+ 
"	phoneNumber;"	+ 
"	address;" +
"	jobTitle;" +
"];"
)})
@Tab(properties="name,givenName,familyName,phoneNumber,email",baseCondition = "${deleted}=0")
@XmlAccessorType(XmlAccessType.NONE)
public class User implements java.io.Serializable { 

	private final static String PROPERTIES_FILE = "naviox.properties";  
	private static Log log = LogFactory.getLog(User.class);
	private static Properties properties;
     
	@Column(columnDefinition="tinyint(1) default 0")
	@Hidden
	private boolean deleted;
	
	public boolean isDeleted(){
		return deleted;
	}
	
	public void setDeleted(boolean deleted){
		this.deleted=deleted;
	}
	
	/** middleName=nickName,
	 * residencyNumber;firstName;lastName;middleName;gender;email;"
				+ "phoneNumber;dateOfBirth;address;localGovernmentArea"
	 * 
	 * 
	 * 
	 */	
	
	private boolean firstLogin;       
	private int loginAttempt;  
    
	
	@Required
	@XmlElement(name="PhoneNumber")
	private String phoneNumber;
	
	private String address;
	
	public static User find(String name) {
		//return XPersistence.getManager().find(User.class, name);
		
		User user = null;
		
		try {
			ArrayList<User> users = (ArrayList<User>) XPersistence.getManager().
					createQuery("FROM User a where a.deleted = 0 AND a.name = '"+name+"'").getResultList();
			if(users != null && users.size()>0)
				user = users.get(0);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}            
  
	@Id @Column(length=60)  
	@XmlElement(name="UserName")
	@Stereotype("EMAIL")
	private String name;   
   
	@Column(length=41) @DisplaySize(30)
	@Stereotype("PASSWORD")

	private String password; 

	@Transient
	@Column(length=41) @DisplaySize(30)
	@Stereotype("PASSWORD")
	private String repeatPassword;

	@org.hibernate.annotations.Type(type="org.hibernate.type.YesNoType")
	@DefaultValueCalculator(TrueCalculator.class)
	private boolean active = true;

	@org.hibernate.annotations.Type(type="org.hibernate.type.YesNoType")
	private boolean authenticateWithLDAP;

	@Column(length=60,unique=true) @Stereotype("EMAIL")
	@Required
	@XmlElement(name="email")
	private String email;

	@Column(length=30)
	@Required
	@XmlElement(name="GivenName")
	private String givenName;

	@Required
	@Column(length=30)
	@XmlElement(name="FamilyName")
	private String familyName;

	@Column(length=30)
	private String jobTitle;

	@Column(length=30)
	private String middleName;

	@Column(length=30)
	private String nickName;

	private Date birthDate;

	@ManyToMany
	@NewAction("CustomSaveAction.addRole")
	private Collection<Role> roles;

	@ReadOnly
	public Collection<Module> getModules() {
		if (roles == null) return Collections.EMPTY_LIST;
		Collection<Module> modules = new ArrayList<Module>();
		for (Role role: roles) {
			modules.addAll(role.getModules());
		}
		return modules;
	}

	private void encryptPassword() {
		password = encrypt(password);
	}

	public boolean isAuthorized(String password) {
		if (!isActive()) return false;
		return passwordMatches(password);
	}

	public boolean passwordMatches(String password) {
		if (isAuthenticateWithLDAP()) return isValidLoginWithLDAP(password);
		return encrypt(password).equals(this.password);
	}

	private boolean isValidLoginWithLDAP(String password) {
        Hashtable<String, String> props = new Hashtable<String, String>();
        String securityPrincipal = getProperties().getProperty("ldapDomain", "").trim() + "\\" + this.name;
        String ldapURL = "ldap://" + getProperties().getProperty("ldapHost", "").trim() +
                ":" + getProperties().getProperty("ldapPort", "").trim() +
                "/" + getProperties().getProperty("ldapDN", "").trim();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, ldapURL);
        props.put(Context.SECURITY_AUTHENTICATION, "simple");
        props.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        props.put(Context.SECURITY_CREDENTIALS, password);
        try {
            DirContext ctx = new InitialDirContext(props);
            ctx.close();
            return true;
        } catch (NamingException ex) {
            log.error(XavaResources.getString("ldap_authentication_error"), ex);
        }
        return false;
    }

	private String encrypt(String source) {
		if (!isEncryptPassword()) return source;
		try {
	      MessageDigest md = MessageDigest.getInstance("SHA");
	      byte[] bytes = source.getBytes();
	      md.update(bytes);
	      byte[] encrypted= md.digest();
	      if (isStorePasswordAsHex()) {
	    	  return new BigInteger(encrypted).toString(16);
	      }
	      else {
	    	  return new String(encrypted);
	      }
		}
		catch (Exception ex) {
			log.error(XavaResources.getString("encrypting_password_problem"), ex);
			throw new RuntimeException(XavaResources.getString("encrypting_password_problem"), ex);
		}
	}

	private static boolean isEncryptPassword() {
		return "true".equalsIgnoreCase(getProperties().getProperty(
				"encryptPassword", "true").trim());
	}

	private static boolean isStorePasswordAsHex() {
		return "true".equalsIgnoreCase(getProperties().getProperty(
				"storePasswordAsHex", "true").trim());
	}

	// We don't use NaviOXProperties here to not create a compilation dependency from naviox jar from User.java
	private static Properties getProperties() {
		if (properties == null) {
			PropertiesReader reader = new PropertiesReader(
					User.class, PROPERTIES_FILE);
			try {
				properties = reader.get();
			} catch (IOException ex) {
				log.error(XavaResources.getString("properties_file_error",
						PROPERTIES_FILE), ex);
				properties = new Properties();
			}
		}
		return properties;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return "********";
	}

	public void setPassword(String password) {
		if (getPassword().equals(password)) return;
		this.password = password;
		encryptPassword();
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}


	public Collection<Module> getNotHiddenModules() {
		Collection<Module> result = new ArrayList<Module>();
		for (Module module: getModules()) {
			if (!module.isHidden()) {
				result.add(module);
			}
		}
		return result;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public boolean isAuthenticateWithLDAP() {
		return authenticateWithLDAP;
	}

	public void setAuthenticateWithLDAP(boolean authenticateWithLDAP) {
		this.authenticateWithLDAP = authenticateWithLDAP;
	}

	public String getRepeatPassword() {
		return getPassword();
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Collection<MetaAction> getExcludedMetaActionsForMetaModule(MetaModule metaModule) {
		Collection<MetaAction> result = null;
		for (Role role: roles) {
			ModuleRights rights = role.getModulesRightsForMetaModule(metaModule);
			if (rights == null) continue;
			if (result == null) result = rights.getExcludedMetaActions();
			else result = CollectionUtils.intersection(result, rights.getExcludedMetaActions());
		}
		return result==null?Collections.EMPTY_LIST:result;
	}

	public boolean isFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	public int getLoginAttempt() {
		return loginAttempt;
	}

	public void setLoginAttempt(int loginAttempt) {
		this.loginAttempt = loginAttempt;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Transient
	public String getUserDetail(){
		String fullName = (!Is.empty(getGivenName())?getGivenName() + " ":"") + 
				(!Is.empty(getFamilyName())?getFamilyName():getName());

		String detail = fullName;
		String concat = "";
		for(Role role : roles){
			concat = concat + role.getName() + " \n ";
		}
		if(!Is.empty(concat))
			detail = detail + " (" + concat + ")";
		return detail;
	}

}
