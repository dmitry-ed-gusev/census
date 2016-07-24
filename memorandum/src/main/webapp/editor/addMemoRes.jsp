<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/>
 <jsp:param name="title" value="пегскэрюр напюанрйх яксфеамни гюохяйх"/>
</jsp:include>
<center>

<%! String boxType;%>

<!-- еЯКХ ЯННАЫЕМХЪ НА НЬХАЙЕ МЕР - БЯЕ нй! еЯКХ ФЕ ЯННАЫЕМХЕ ЕЯРЭ - БШБНДХЛ ЕЦН МЕ ЩЙПЮМ. -->
<c:choose>
 <c:when test="${error == null}">
  <c:if test="${edit != null && edit == 'edit'}">
      <font color="green"><b>бюью яксфеамюъ гюохяйю нрпедюйрхпнбюмю!</b></font><br>
      <% boxType = "outbox";%>
  </c:if>
  <c:if test="${edit == null && forward == null}">
      <font color="green"><b>бюью яксфеамюъ гюохяйю гюпецхярпхпнбюмю! мнлеп <c:out value="${memo.memoNumber}"/></b></font><br>
      <% boxType = "outbox";%>
  </c:if>
  <c:if test="${forward != null}">
      <font color="green"><b>бюью яксфеамюъ гюохяйю оепеюдпеянбюмю! мнлеп <c:out value="${memo.memoNumber}"/></b></font><br>
      <% boxType = "inbox";%>
  </c:if>
  <p><a href="../controller?action=viewMemo&memoID=<c:out value="${memoID}"/>&boxType=<%=boxType%>"><b>[опнялнрп яксфеамни гюохяйх]</b></a></p>
 </c:when>
 <c:otherwise>
  <font color="red"><b>
   опх днаюбкемхх/педюйрхпнбюмхх/оепеюдпеяюжхх яксфеайх бнгмхйкю ньхайю: <br>
   <c:out value="${error}"/>
  </b></font>
 </c:otherwise>
</c:choose>
</center>
<jsp:include page="/_bottom.jsp" />