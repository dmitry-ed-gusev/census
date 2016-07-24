<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<% String prefix = (String)request.getAttribute("prefix"); if (prefix == null) prefix = "";%>
<jsp:include page="/_top.jsp">
 <jsp:param name="title" value="оепевмх яксфеамшу гюохянй он ондпюгдекемхъл цсп"/>
 <jsp:param name="prefix" value="<%=prefix%>"/>
</jsp:include>

<center>
<c:choose>
 <c:when test="${deptsList == null}">яохянй нрдекнб цсп осяр!</c:when>
 <c:otherwise>
     <table id="myTable1" class="rsdata-table-filter">

            <tr><td style="background-color:#f2f2f2;"><b>ондпюгдекемхе</b></td></tr>


        <c:forEach items='${deptsList}' var='dept'>
            <tr align=left>               

                <td>
                    <c:if test="${action == 'mapping_email'}"><a
                        href="<%=prefix%>system/controller?action=deptBox&deptID=<c:out value="${dept.id}"/>">
                    </c:if>
                    <c:if test="${action == 'mapping_dpm'}"><a
                        href="<%=prefix%>system/controller?action=deptBox_dpm&deptID=<c:out value="${dept.id}"/>">
                    </c:if>
                    <c:if test="${action == null}"><a
                        href="controller?action=deptBox&deptID=<c:out value="${dept.id}"/>&boxType=inbox&pageNumber=1">
                    </c:if>
                    <c:out value="${dept.departmentName}"/></a>
                </td>

            </tr>
        </c:forEach>


     </table>

</center>
 </c:otherwise>
</c:choose>
<jsp:include page="/_bottom.jsp" />