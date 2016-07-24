<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/>
 <jsp:param name="title" value="��������� ���������� ������ �� ���������"/>
</jsp:include>
<center>
<!-- ���� ��������� �� ������ ��� - ��� ��! ���� �� ��������� ���� - ������� ��� �� �����. -->
<c:choose>
 <c:when test="${error == null}">
  <font color="green"><b>
   ��� ����� �� ���������� ��������� ������� <c:out value="${memo.memoNumber}"/> ������� ���������!
  </b></font>
  <p><a href="../controller?action=viewMemo&memoID=<c:out value="${memo.id}"/>&boxType=outbox"><b>[�������� ��������� �������]</b></a></p>
 </c:when>
 <c:otherwise><font color="red"><b>��� ��������� ��������� ������� �������� ������: <br> <c:out value="${error}"/></b></font></c:otherwise>
</c:choose>
</center>
<jsp:include page="/_bottom.jsp"><jsp:param name="prefix" value="../"/></jsp:include>