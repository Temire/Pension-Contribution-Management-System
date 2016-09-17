package ng.com.justjava.test;


import java.math.*;
import java.util.*;

import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.codehaus.jettison.json.*;
import org.openxava.jpa.*;

import com.openxava.naviox.impl.*;
import com.openxava.naviox.model.*;

import ng.com.justjava.epayment.model.*;
import ng.com.justjava.epayment.utility.*;
import org.openxava.util.*;

@Path(value="/pension")
public class TestWebService {
    @Context
    private HttpServletRequest httpRequest;

    @GET
    @Path(value="/add/{a}/{b}")
    @Produces(value={"text/plain"})
    public String addPlainText(@PathParam(value="a") double d, @PathParam(value="b") double d2) {
        return "" + (d + d2) + "";
    }

    @GET
    @Path(value="/add/{a}/{b}")
    @Produces(value={"text/xml"})
    public String add(@PathParam(value="a") double d, @PathParam(value="b") double d2) {
        return "<?xml version=\"1.0\"?><result>" + (d + d2) + "</result>";
    }

    @GET
    @Path(value="/sub/{a}/{b}")
    @Produces(value={"text/plain"})
    public String subPlainText(@PathParam(value="a") double d, @PathParam(value="b") double d2) {
        return "" + (d - d2) + "";
    }

    @GET
    @Path(value="/sub/{a}/{b}")
    @Produces(value={"text/xml"})
    public String sub(@PathParam(value="a") double d, @PathParam(value="b") double d2) {
        return "<?xml version=\"1.0\"?><result>" + (d - d2) + "</result>";
    }

    @GET
    @Produces(value={"text/xml"})
    @Path(value="/pfaXML/{uniqueId}")
    public Response getPFAXML(@PathParam(value="uniqueId") String string) {
        System.out.println("  The sent PFA Unique Id ===" + string);
        String string2 = "FROM PensionFundAdministrator p WHERE p.uniqueIdentifier='" + string + "'";
        PensionFundAdministrator pensionFundAdministrator = null;
        try {
            pensionFundAdministrator = (PensionFundAdministrator)XPersistence.getManager().createQuery(string2).getSingleResult();
        }
        catch (Exception var4_4) {
            var4_4.printStackTrace();
        }
        return Response.status((int)200).entity((Object)pensionFundAdministrator).build();
    }

    @GET
    @Produces(value={"application/json"})
    @Path(value="/pfaTransfer/{username}")
    public String getPFATransfer(@PathParam(value="username") String string) {
        System.out.println("  The sent username is ===" + string);
        String string2 = "FROM PFATransfer p WHERE p.holder.user.name='" + string + "'";
        JSONObject jSONObject = new JSONObject();
        PFATransfer pFATransfer = null;
        Object var5_5 = null;
        ArrayList arrayList = null;
        String string3 = "FROM LodgeComplaint l WHERE l.complainant.user.name='" + string + "'";
        Object var8_8 = null;
        Object var9_9 = null;
        ArrayList arrayList2 = null;
        try {
            arrayList = (ArrayList)XPersistence.getManager().createQuery(string2).getResultList();
            pFATransfer = !arrayList.isEmpty() ? (PFATransfer)arrayList.get(0) : null;
            arrayList2 = (ArrayList)XPersistence.getManager().createQuery(string3).getResultList();
            if (pFATransfer != null) {
                System.out.println("The pfa id is ===" + pFATransfer.getId());
                jSONObject.put("userName", (Object)string);
                jSONObject.put("status", (Object)pFATransfer.getStatus());
                jSONObject.put("dateEntered", (Object)pFATransfer.getDateEntered());
                jSONObject.put("comment", (Object)pFATransfer.getComment());
                jSONObject.put("incomingPFA", (Object)pFATransfer.getIncomingPFA());
                jSONObject.put("outgoingPFA", (Object)pFATransfer.getOutgoingPFA());
                System.out.println("The comment is " + pFATransfer.getComment());
            } else {
                System.out.println("This is the else part");
                jSONObject.put("userName", (Object)string);
                jSONObject.put("status", (Object)"No Request was made");
                jSONObject.put("dateEntered", (Object)"Empty");
                jSONObject.put("comment", (Object)"Empty");
                jSONObject.put("incomingPFA", (Object)"Empty");
                jSONObject.put("outgoingPFA", (Object)"Empty");
            }
            if (arrayList2 != null) {
                for (int i = 0; i < arrayList2.size(); ++i) {
                    LodgeComplaint lodgeComplaint = (LodgeComplaint)arrayList2.get(i);
                    jSONObject.put("dateEntered" + i, (Object)lodgeComplaint.getDateLodge());
                    jSONObject.put("complaint" + i, (Object)lodgeComplaint.getComplaint());
                    jSONObject.put("status" + i, (Object)lodgeComplaint.getStatus());
                }
                System.out.println("The size is " + arrayList2.size());
                jSONObject.put("size", arrayList2.size());
            }
        }
        catch (JSONException var11_12) {
            var11_12.printStackTrace();
        }
        return jSONObject.toString();
    }

    @GET
    @Produces(value={"application/json"})
    @Path(value="/viewComplaint/{username}")
    public String getComplaint(@PathParam(value="username") String string) {
        System.out.println("  The sent username is ===" + string);
        String string2 = "FROM LodgeComplaint l WHERE l.complainant.user.name='" + string + "'";
        Object var3_3 = null;
        Object var4_4 = null;
        ArrayList arrayList = null;
        JSONObject jSONObject = new JSONObject();
        try {
            arrayList = (ArrayList)XPersistence.getManager().createQuery(string2).getResultList();
            if (arrayList != null) {
                for (int i = 0; i < arrayList.size(); ++i) {
                    LodgeComplaint lodgeComplaint = (LodgeComplaint)arrayList.get(i);
                    jSONObject.put("dateEntered" + i, (Object)lodgeComplaint.getDateLodge());
                    jSONObject.put("complaint" + i, (Object)lodgeComplaint.getComplaint());
                    jSONObject.put("status" + i, (Object)lodgeComplaint.getStatus());
                }
                System.out.println("The size is " + arrayList.size());
                jSONObject.put("size", arrayList.size());
            }
        }
        catch (JSONException var7_8) {
            var7_8.printStackTrace();
        }
        return jSONObject.toString();
    }

    @GET
    @Produces(value={"text/xml"})
    @Path(value="/pfas/")
    public Response getPFAs() {
        PensionFundAdministrators pensionFundAdministrators = new PensionFundAdministrators();
        System.out.println(" REQUESTING FOR THE WHOLE pfas ....................................");
        return Response.status((int)200).entity((Object)pensionFundAdministrators).build();
    }

    @GET
    @Produces(value={"application/json"})
    @Path(value="/authorization/{userName}/{password}")
    public String authorization(@PathParam(value="userName") String string, @PathParam(value="password") String string2) {
        System.out.println(" The call to authorization returning true here......");
        PersonalPayment personalPayment = new PersonalPayment();
        personalPayment.setAmount(new BigDecimal(0.0));
        JSONObject jSONObject = new JSONObject();
        try {
            System.out.println("1 The sent userName == " + string);
            if (SignInHelper.isAuthorized((String)string, (String)string2)) {
                System.out.println("2 The sent userName == " + string);
                RSAHolder rSAHolder = UserManager.getHolderProfileOfLoginUser((String)string);
                System.out.println("3 The sent userName == " + string);
                jSONObject.put("authorized", true);
                jSONObject.put("userName", (Object)string);
                jSONObject.put("password", (Object)string2);
                jSONObject.put("fullName", (Object)rSAHolder.getFullName());
                if (rSAHolder != null) {
                    jSONObject.put("administrator", (Object)(rSAHolder.getPfa() != null ? rSAHolder.getPfa().getUniqueIdentifier() : "Un Available"));
                    System.out.println("4 The sent userName == " + string + " holder==" + (Object)rSAHolder.getPfa());
                }
                System.out.println("5 The sent userName == " + string);
            } else {
                jSONObject.put("authorized", false);
            }
            System.out.println(" Object Here ==" + (Object)jSONObject);
        }
        catch (JSONException var5_6) {
            var5_6.printStackTrace();
        }
        return jSONObject.toString();
    }

    @GET
    @Produces(value={"application/json"})
    @Path(value="/pfa/{uniqueIdentifier}")
    public String getPFA(@PathParam(value="uniqueIdentifier") String string, @PathParam(value="password") String string2) {
        System.out.println(" The call to authorization returning true here......");
        PensionFundAdministrator pensionFundAdministrator = PensionFundAdministrator.findPFAByUniqueIdentifier((String)string);
        JSONObject jSONObject = new JSONObject();
        try {
            if (pensionFundAdministrator != null) {
                jSONObject.put("name", (Object)pensionFundAdministrator.getName());
            } else {
                jSONObject.put("authorized", false);
            }
            System.out.println(" Object Here ==" + (Object)jSONObject);
        }
        catch (JSONException var5_5) {
            var5_5.printStackTrace();
        }
        return jSONObject.toString();
    }

    @GET
    @Produces(value={"text/plain"})
    @Path(value="/postpfa/{username}/{outgoingPfa}/{incomingPfa}/{comment}")
    public String transferPfa(@PathParam(value="username") String string, @PathParam(value="outgoingPfa") String string2, @PathParam(value="incomingPfa") String string3, @PathParam(value="comment") String string4) {
        System.out.println(" The call to post webservices starts here......");
        PensionFundAdministrator pensionFundAdministrator = PensionFundAdministrator.findPFAByUniqueIdentifier((String)string3);
        PensionFundAdministrator pensionFundAdministrator2 = PensionFundAdministrator.findPFAByUniqueIdentifier((String)string2);
        RSAHolder rSAHolder = UserManager.getHolderProfileOfLoginUser((String)string);
        try {
            PFATransfer pFATransfer = new PFATransfer();
            pFATransfer.setHolder(rSAHolder);
            pFATransfer.setPfa(pensionFundAdministrator);
            pFATransfer.setDateEntered(Dates.createCurrent());
            pFATransfer.setOutgoing(pensionFundAdministrator2);
            pFATransfer.setStatus(PFATransfer.TransferStatus.withIncomingPFA);
            pFATransfer.setComment(string4);
            XPersistence.getManager().merge((Object)pFATransfer);
            XPersistence.commit();
            pFATransfer.getClass();
        }
        catch (Exception var8_9) {
            var8_9.printStackTrace();
        }
        return "OK";
    }

    @GET
    @Produces(value={"text/plain"})
    @Path(value="/postregister/{pencomm}/{pfa}/{email}/{firstName}/{lastName}/{phoneno}")
    public String postRegister(@PathParam(value="email") String string, @PathParam(value="pencomm") String string2, @PathParam(value="pfa") String string3, @PathParam(value="firstName") String string4, @PathParam(value="lastName") String string5, @PathParam(value="phoneno") String string6) {
        System.out.println(" The call to post webservices starts here......");
        PensionFundAdministrator pensionFundAdministrator = PensionFundAdministrator.findPFAByUniqueIdentifier((String)string3);
        System.out.println(" The administrator here ===" + (Object)pensionFundAdministrator);
        User user = new User();
        RSAHolder rSAHolder = new RSAHolder();
        try {
            user.setEmail(string);
            user.setName(string);
            user.setFamilyName(string5);
            user.setGivenName(string4);
            user.setPhoneNumber(string6);
            String string7 = "FROM Role r where r.name='RSAHolder'";
            List list = XPersistence.getManager().createQuery(string7).getResultList();
            if (list == null || list.isEmpty()) {
                Role role = new Role();
                role.setName("RSAHolder");
                role = (Role)XPersistence.getManager().merge((Object)role);
                list.add(role);
            }
            user.setRoles((Collection)list);
            user = (User)XPersistence.getManager().merge((Object)user);
            rSAHolder.setPencommNumber(string2);
            rSAHolder.setPfa(pensionFundAdministrator);
            rSAHolder.setFirstName(string4);
            rSAHolder.setSecondName(string5);
            rSAHolder.setUser(user);
            XPersistence.getManager().merge((Object)rSAHolder);
            XPersistence.commit();
        }
        catch (Exception var10_11) {
            var10_11.printStackTrace();
        }
        return "OK";
    }

    @GET
    @Produces(value={"text/plain"})
    @Path(value="/postcomplaint/{username}/{comment}")
    public String makeComplaint(@PathParam(value="username") String string, @PathParam(value="comment") String string2) {
        System.out.println(" The call to post webservices starts here......");
        RSAHolder rSAHolder = UserManager.getHolderProfileOfLoginUser((String)string);
        try {
            LodgeComplaint lodgeComplaint = new LodgeComplaint();
            lodgeComplaint.setComplainant(rSAHolder);
            lodgeComplaint.setVisibleToCompany(true);
            lodgeComplaint.setVisibleToMyPFA(true);
            lodgeComplaint.setComplaint(string2);
            lodgeComplaint.setDateLodge(Dates.createCurrent());
            lodgeComplaint.setStatus(LodgeComplaint.Status.open);
            XPersistence.getManager().merge((Object)lodgeComplaint);
            XPersistence.commit();
        }
        catch (Exception var4_5) {
            var4_5.printStackTrace();
        }
        return "OK";
    }

    @POST
    @Produces(value={"text/plain"})
    @Path(value="/makePayment")
    public String paymentFile() {
        return "OK";
    }
}
