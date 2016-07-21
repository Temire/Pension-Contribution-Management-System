<%@ include file="../xava/imports.jsp"%>

<style type="text/css">
#apDiv1 {
	position:absolute;
	width:830px;
	height:249px;
	z-index:1;
	top: 173px;
	left: 227px;
}
</style>

<%
String app = request.getParameter("application");
%>
</br>
</br></br>
<table width="331" height="85" border="0" align="center">
    <tr>
	
	
	<td width="169">
		<div id="sign_in_box">
			
			<img src="../naviox/images/login.png" alt="image" align="center">
			</br>
			<div style="text-align:center; font-family: 'Ubuntu', sans-serif; font-size:12px; font-weight: bold;">
				Login To Your Account
			</div></br>
			<jsp:include page='<%="../xava/module.jsp?application=" + app + "&module=SignIn"%>'/>
			<xava:action action="SignIn.forgotPassword"/>?
			</br>
			</br>
			<xava:action action="SignIn.registerPersonalPensionContribution"/>
		</div>


	</td>

    	<td width="169">
    	<a href="/pensionmanager/m/PensionCalculator?retainOrder=true">
    		<div id="penCalc">
    				<img src="../naviox/images/pen-calc.png" alt="image" align="center">
    		</div>	
    		
    	</a>
	</td>
    </tr>

 <tr>
<td align="center">
<div id="welcome">
  <jsp:include page='welcome.jsp'/>
 </div>
 </td></tr>
</table>