<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/><jsp:param name="title" value="œ≈–≈Õ¿«Õ¿◊≈Õ»≈ ¿ƒ–≈—Œ¬ EMAIL."/>
</jsp:include>
<center>
<c:choose>
 <c:when test="${emailsList == null}">
     <font color="blue"><b>—œ»—Œ  E-MAIL œ”—“</b></font>
 </c:when>
 <c:otherwise>
   <b>—œ»—Œ  E-MAIL</b>
   <table border="1" cellpadding="2" cellspacing="2" width="50%">
    <tr bgcolor="#e3eff9" align=center>
        <td>E-mail </td>
    </tr>
    <c:forEach items='${emailsList}' var='emails'>
    <tr>
        <td><a HREF='../system/controller?action=addDeptEmail&type=<c:out value="${type}"/>&deptID=<c:out value="${deptID}"/>&emailID=<c:out value="${emails.key}"/>'><c:out value="${emails.value}"/></a></td>
    </tr>
   </c:forEach>
   </table>


 </c:otherwise>
</c:choose>
</center>
<jsp:include page="/_bottom.jsp"><jsp:param name="prefix" value="../"/></jsp:include>