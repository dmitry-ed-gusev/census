<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/>
 <jsp:param name="title" value="пегскэрюр нропюбйх яксфеамни гюохяйх"/>
</jsp:include>
<center>
<!-- еЯКХ ЯННАЫЕМХЪ НА НЬХАЙЕ МЕР - БЯЕ нй! еЯКХ ФЕ ЯННАЫЕМХЕ ЕЯРЭ - БШБНДХЛ ЕЦН МЕ ЩЙПЮМ. -->
<c:choose>
 <c:when test="${error == null}">                          
  <font color="green"><b>бюью яксфеамюъ гюохяйю ╧ <c:out value="${memoNumber}" /> сяоеьмн нропюбкемю!</b></font>
  <p><a href="../controller?action=deptBox&deptID=<c:out value="${member.departmentId}"/>&boxType=outbox"><b>[оепеирх б ъыхй нрдекю <c:out value="${member.departmentCode}"/>]</b></a></p>
 </c:when>
 <c:otherwise>
  <font color="red"><b>
   опх нропюбйе яксфеамни гюохяйх бнгмхйкю ньхайю: <br>
   <c:out value="${error}"/>
  </b></font>
 </c:otherwise>
</c:choose>
</center>
<jsp:include page="/_bottom.jsp" />