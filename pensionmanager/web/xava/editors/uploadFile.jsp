<%@ include file="../imports.jsp"%>

<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<table>
<th align='left' class="<%=style.getLabel()%>">
<xava:message key="enter_file_name"/>
</th>
<td>
<input name = "newImage" class=<%=style.getEditor()%> type="file" size='60' tabindex="1"/>
</td>
</table>
 

