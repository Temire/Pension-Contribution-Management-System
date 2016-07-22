<%@include file="../xava/imports.jsp"%>

<%@page import="java.util.Iterator"%>
<%@page import="org.openxava.application.meta.MetaModule"%>
<%@page import="org.openxava.util.Users"%>
<%@page import="org.openxava.util.Is"%>
<%@page import="com.openxava.naviox.Modules"%>
<%@page import="com.openxava.naviox.util.NaviOXPreferences"%>

<jsp:useBean id="modules" class="com.openxava.naviox.Modules" scope="session"/>



<span id="main_navigation_left">
<nobr>
<span 
	id="main_navigation_right_bridge1"></span><span 
	id="main_navigation_right_bridge2"></span><span 
	id="main_navigation_right_content">
<%
if (Is.emptyString(NaviOXPreferences.getInstance().getAutologinUser())) {
	String userName = Users.getCurrentUserInfo().getUserDetail();
	if (userName == null) {
%>
<% String selected = "SignIn".equals(request.getParameter("module"))?"selected":""; %>

<%
	}
	else {
%>

<%
	}
} 
%>
</span>
</span>
<%
if (Is.emptyString(NaviOXPreferences.getInstance().getAutologinUser())) {
	String userName = Users.getCurrentUserInfo().getUserDetail();
	if (userName == null) {
%>

<div id="main_navigation_middle2">
	<img src="../naviox/images/p-logo.png" alt="image">
</div>
<%
	}
}
%>
<div id="main_navigation_middle" style="display:none">
<nobr>

<%
int count = 0; 
for (Iterator it= modules.getAll().iterator(); it.hasNext();) {
	
	MetaModule module = (MetaModule) it.next();
	if (module.getName().equals("SignIn")) continue; 
	if (module.getName().equals("PensionCalculator")) continue;
	
	String selected = module.getName().equals(request.getParameter("module"))?"selected":"";
%>		
	<a  href="/<%=module.getMetaApplication().getName()%>/m/<%=module.getName()%>?retainOrder=true" class="<%=selected%>">
		<%=module.getLabel(request.getLocale())%>
	</a>
	<%
	if(count == 5){
	%> 
	</br>
	<%}%>
<%
	count = count + 1;
}
%>

</nobr>
</div>
<div id="main_navigation_middle2">
	<img src="../naviox/images/p-logo.png" alt="image">
</div>
<div id="main_navigation_right">
<%
	String userName = Users.getCurrentUserInfo().getUserDetail();
	if (userName != null) {
%>

<a  href="<%=request.getContextPath()%>/naviox/signOut.jsp" class="sign-in"><xava:message key="signout"/> </a>
<br/>
<b><font size="2" color="#000" face="'Ubuntu', sans-serif";> <%=userName%></font></b>
<%
	}else{
	
%>
<a href="<%=request.getContextPath()%>/m/SignIn" class="sign-in"><xava:message key="signin"/>
</a>

<%
}
%>
</div>
