<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="/_top.jsp">
    <jsp:param name="prefix" value="../"/>
    <jsp:param name="title" value="Список специалистов Регистра."/>
</jsp:include>

<center>

    <p>&nbsp;</p>
    <c:choose>
        <c:when test="${membersList == null}">СПИСОК СПЕЦИАЛИСТОВ ПУСТ!</c:when>
        <c:otherwise>

            <!-- Таблица со списком специалистов ГУР. -->
            <TABLE cellpadding=2 cellspacing=2 width="100%" border=0>
                <c:forEach items='${membersList}' var='member'>
                    <tr bgcolor="#f0f0f0">
                    <TD width='20%'><a
                            href="../system/controller?action=addDeptsPersonnelMapping&deptID=<c:out value="${deptID}"/>&memberID=<c:out value="${member.personnelId}"/>">
                        <c:out value="${member.shortName}"/></a>
                    </TD>
                    <TD><c:out value="${member.departmentCode}"/>&nbsp;&nbsp;<c:out
                            value="${member.departmentName}"/></TD>
                    </tr>
                </c:forEach>
            </TABLE>

        </c:otherwise>
    </c:choose>
</center>
<jsp:include page="/_bottom.jsp">
    <jsp:param name="prefix" value="../"/>
</jsp:include>