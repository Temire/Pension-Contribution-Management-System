<%@ page language="java"  session="true" isThreadSafe="true" %>
<%
//String transId = request.getParameter("TRANSACTION_ID");
//Generate your own unique transId per transaction.
String transId = "1234567891011";

String terminalId = null;//request.getParameter("TERMINAL_ID");
String success = null;//request.getParameter("SUCCESS");
if (terminalId == null) terminalId = "0000000001";

if (success == null){ //or success = "" for asp
	out.println("<form method='POST' action='http://demo.etranzact.com/WebConnectPlus/caller.jsp'>");
		out.println("<input type=hidden name = 'TERMINAL_ID' value='"+terminalId+"'>");
		out.println("<input type=hidden name = 'TRANSACTION_ID' value='"+transId+"'>");
		out.println("<input type=hidden name = 'AMOUNT' value='10.00'>");
		out.println("<input type=hidden name = 'DESCRIPTION' value='Ticket Payment'>");
		out.println("<input type=hidden name = 'EMAIL' value='xyz@yahoo.com'>");
		out.println("<input type=hidden name = 'CURRENCY_CODE' value='NGN'>");
		out.println("<input type=hidden name = 'CHECKSUM' value='bbdHSSLldlsl133laddf'"); //MD5 Encrypted Value'
		out.println("<input type=hidden name = 'RESPONSE_URL' value='http://www.mywebsite.com/eTranzact/paymentClient.jsp'>");
		out.println("<input type=hidden name = 'LOGO_URL' value='http://www.mywebsite.com/images/someimages.jpg'>");
		out.println("<input type=submit value='Submit'>");
	out.println("</form>");
/* 	out.println("<script language='javascript'>");
	out.println("var form = document.forms[0];");
	out.println("form.submit();</script>"); */
}else if (success.equals("0")){
	//deal with successful transaction
	out.println("Transaction Successfull");
}else	//Deal with Timeout Here, Transaction ID no more valid  
	out.println("Error while requesting for transaction authorisation, Transaction ID no more valid ");
%>