<html>
<head>
<%@page import="org.openxava.jpa.*"%>
<%@page import="ng.com.justjava.epayment.model.*"%>
<%@page import="java.util.*"%>
<%@page import="import org.openxava.util.*"%>
<%@page import="ng.com.justjava.epayment.utility.*"%>

<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Making Payment via eTranzact</title> 
</head>
<body topmargin="0" leftmargin="0" >
<%@ page language="java"  session="true" isThreadSafe="true" %>
<%
//String transId = request.getParameter("TRANSACTION_ID");
//Generate your own unique transId per transaction.
String transId = "1234567891011";

/* String query = " FROM Corporate c";
Collection<Corporate> corporates = XPersistence.getManager().createQuery(query).getResultList();
for (Corporate corporate:corporates){
	System.out.println(" The corporate picked here===="+corporate.getName());

} */

String terminalId = request.getParameter("TERMINAL_ID");
String success = request.getParameter("SUCCESS");
String TRANSACTION_ID = request.getParameter("TRANSACTION_ID");
String amount = request.getParameter("amount"); 
String email = request.getParameter("email"); 
String CHECKSUM = request.getParameter("CHECKSUM");
String responseUrl = request.getParameter("responseUrl");
String secretKey = request.getParameter("secretKey");

if(Is.emptyString(CHECKSUM)){
	CHECKSUM = Cryptor.getCheckSum(amount, terminalId, TRANSACTION_ID, responseUrl, secretKey);
	System.out.println(" The Checksum internally calculated =="+CHECKSUM);
}



System.out.println(" terminalId = "+terminalId + "  TRANSACTION_ID ="+TRANSACTION_ID +
"  amount ="+amount + " CHECKSUM="+CHECKSUM + "  email="+email);


if (terminalId == null) terminalId = "0000000001";

if (success == null){ //or success = "" for asp
	out.println("<form method='POST' action='http://demo.etranzact.com/WebConnectPlus/caller.jsp'>");
	
		out.println("<input type=hidden name ='TERMINAL_ID' value='"+terminalId+"'>");
		out.println("<input type=hidden name ='TRANSACTION_ID' value='" + TRANSACTION_ID + "'>");
		out.println("<input type=hidden name ='AMOUNT' value='" + amount+ "'>");
		out.println("<input type=hidden name ='DESCRIPTION' value='Pension Contribution'>");
		out.println("<input type=hidden name ='EMAIL' value='"+ email +"'>");
		out.println("<input type=hidden name ='CURRENCY_CODE' value='NGN'>");
		out.println("<input type=hidden name ='CHECKSUM' value='"+CHECKSUM+"'"); //MD5 Encrypted Value'
		out.println("<input type=hidden name ='CARD_TYPE' value= '0'>");
		out.println("<input type=hidden name ='RESPONSE_URL' value='" + responseUrl + "'>");		
		out.println("<input type=hidden name ='LOGO_URL' value='https://www.cscsnigeriaplc.com/img/logo.png'>");
	out.println("</form>");
	
	out.println("<script language='javascript'>");
	out.println("var form = document.forms[0];");
	out.println("form.submit();</script>");
}else if (success.equals("0")){
	//deal with successful transaction
	out.println("Transaction Successfull");
}else	//Deal with Timeout Here, Transaction ID no more valid  
	out.println("Error while requesting for transaction authorisation, Transaction ID no more valid ");
%>
</body>
</html>