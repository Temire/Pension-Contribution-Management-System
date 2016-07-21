<%@page import="java.util.Iterator"%>
<%@page import="ng.com.justjava.epayment.model.PaymentOption"%>

	<form id="myForm" name="myForm"
		action="http://example.com/examplePage.do" method="POST">
		<input type=hidden name="val1" id="val1" value="value1" /> <input
			type=hidden name="val2" id="val2" value="value2" /> <input type=hidden
			name="val3" id="val3" value="value3" /> <input type=hidden
			name="submit" id="submit" value="Continue"/>
	</form>

<%
String propertyKey = request.getParameter("propertyKey"); // Id of the key property of the reference (1)
Object value = request.getAttribute(propertyKey + ".value"); // You can use propertyKey + ".value" (2)
if (value == null) value = 0;

for (PaymentOption paymentOption : PaymentOption.findAll()) {
 
 String checked = value.equals(paymentOption.getId())?"checked='checked'":"";
%>
<span style="font-weight: bold; Payment Option #<%=paymentOption.getPaymentOption()%>; vertical-align: bottom">
 <input name="<%=propertyKey%>" value="<%=paymentOption.getPaymentOption()%>" type="radio" <%=checked%>
 onchange="f('text')"/> <!-- (3) -->
 <%=paymentOption.getPaymentOption()%>
</span>
<%
}  
%>
