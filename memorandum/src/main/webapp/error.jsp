<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<% String prefix = request.getParameter("prefix"); if (prefix == null) prefix = "";%>
<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="<%=prefix%>"/>
 <jsp:param name="title" value="ньхайю"/>
</jsp:include>
<center>
<a href="<%=prefix%>controller?action=depts">
 <font color=blue><b>[оепеирх мю цкюбмсч ярпюмхжс опхкнфемхъ]</b></font>
</a>
<p>&nbsp;</p>
<table width=80% class=border bgcolor=white>
 <tr align=center><td><font color=red><b>бн бпелъ пюанрш опхкнфемхъ бнгмхйкю ньхайю</b></font></td></tr>
 <tr align=center><td><font color=red size=+1><i><b><c:out value="${errorMsg}"/></b></i></font></td></tr>
</table>
</center>
<jsp:include page="_bottom.jsp" />