<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/>
 <jsp:param name="title" value="пегскэрюр онпсвемхъ яксфеамни гюохяйх"/>
</jsp:include>
<center>
<!-- еЯКХ ЯННАЫЕМХЪ НА НЬХАЙЕ МЕР - БЯЕ нй! еЯКХ ФЕ ЯННАЫЕМХЕ ЕЯРЭ - БШБНДХЛ ЕЦН МЕ ЩЙПЮМ. -->
<c:choose>
 <c:when test="${error == null}">
  <font color="green"><b>
   яксфеамюъ гюохяйю ╧ <c:out value="${memoNumber}" /> онпсвемю якедсчыхл янрпсдмхйюл нрдекю <c:out value="${memberDeptCode}" />: <c:out value="${memberShortName}"/>, ян япнйнл хяонкмемхъ <c:out value="${realizedDate}"/> !
  </b></font>
  <p><a href="../controller?action=viewMemo&memoID=<c:out value="${memoID}"/>&boxType=outbox"><b>[опнялнрп яксфеамни гюохяйх]</b></a></p>
 </c:when>
 <c:otherwise><font color="red"><b>опх онпсвемхх яксфеамни гюохяйх бнгмхйкю ньхайю: <br> <c:out value="${error}"/></b></font></c:otherwise>
</c:choose>
</center>
<jsp:include page="/_bottom.jsp"><jsp:param name="prefix" value="../"/></jsp:include>