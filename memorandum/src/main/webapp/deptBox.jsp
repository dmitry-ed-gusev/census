<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="/_top.jsp">
    <jsp:param name="title"
               value='<%="Служебные записки. Подразделение [" + request.getAttribute("deptCode") + "]."%>'/>
</jsp:include>

<!-- Цвета: 81FEB9 - зеленый, FFB8B3 - красный. request.getAttribute("deptCode") + -->

<!-- Табличка с заголовком для выбора типа ящика: "входящие"/"исходящие" bgcolor="#f0f0f0"-->
<table>
    <tr>
        <td>
            <form name="inbox" action="controller">
                <input type=hidden name=action value='deptBox'>
                <input type=hidden name=boxType value='inbox'>
                <input type=hidden name=deptID value='<c:out value="${deptID}"/>'>
                <input type=hidden name=pageNumber value=1>
                <input type="submit" value='ВХОДЯЩИЕ СЛУЖЕБНЫЕ ЗАПИСКИ'
                        <c:if test="${boxType == 'inbox'}">class=buttonActiveStyle</c:if>
                        <c:if test="${boxType == 'outbox'}">class=buttonStyle</c:if>
                        >
            </form>
        </td>
        <td>
            <form name="inbox" action="controller">
                <input type=hidden name=action value='deptBox'>
                <input type=hidden name=deptID value='<c:out value="${deptID}"/>'>
                <input type=hidden name=boxType value='outbox'>
                <input type=hidden name=pageNumber value=1>
                <input type="submit" value='ИСХОДЯЩИЕ СЛУЖЕБНЫЕ ЗАПИСКИ'
                       <c:if test="${boxType == 'outbox'}">class=buttonActiveStyle</c:if>
                       <c:if test="${boxType == 'inbox'}">class=buttonStyle</c:if>
                        >
            </form>

        </td>
    </tr>
    <tr>
        <td colspan=2><i>[ящик синего цвета - активный]</i></td>
    </tr>
</table>

<!-- Вывод списка страничек для навигации по ящику -->
<!-- Навигация отсутствует для отчетов "кто не ответил" и "кому не ответили" -->

<c:if test="${memosList != null && boxSubType == null}">
    <table width=100%>
        <tr>
            <td align=left>
                <b>СТРАНИЦЫ:</b>&nbsp;&nbsp;
                <c:forEach items="${pages}" var="page">

                    <a href='controller?action=deptBox&deptID=<c:out value="${deptID}"/>&pageNumber=<c:out value="${page}"/>&boxType=<c:out value="${boxType}"/>'>
                        <c:if test="${page == pageNumber}"><font color="red"><b></c:if><c:out value="${page}"/><c:if test="${page == pageNumber}"></b></font></c:if>
                    </a>
                </c:forEach>
            </td>
        </tr>
    </table>
</c:if>

<br><br>

<!-- Непосредственно вывод таблицы со списком служебок в ящике -->
<c:choose>
    <c:when test="${memosList == null}">СПИСОК СЛУЖЕБНЫХ ЗАПИСОК ПУСТ!</c:when>
    <c:otherwise>
        <table id="myTable1" width="99%" class="rsdata-table-filter" style="margin-left:7px;">

                <tr>
                    <td  style="background-color:#f2f2f2;"><b>№&nbsp;СЗ</b></td>
                    <td style="background-color:#f2f2f2;"><b>ДАТА</b></td>
                    <!-- Если показываем "входящие", то выводится поле "отдел-отправитель", если
                    же показываем "исходящие", то выводится поле "ФИО исполнителя". Также в исходящих
                    показываем поле - когда отправлена служебка(если отправлена)-->
                    <c:choose>
                        <c:when test="${boxType == 'inbox'}">
                            <td style="background-color:#f2f2f2;"><b>ОТПРАВИТЕЛЬ</b></td>
                        </c:when>
                        <c:otherwise>
                            <td style="background-color:#f2f2f2;"><b>ИСПОЛНИТЕЛЬ</b></td>
                        </c:otherwise>
                    </c:choose>
                    <td style="background-color:#f2f2f2;" width="20%"><b>ПОЛУЧАТЕЛИ</b></td>
                    <td style="background-color:#f2f2f2;"><b>ОТПРАВЛЕНО</b></td>
                    <c:if test="${boxType == 'inbox'}">
                        <td style="background-color:#f2f2f2;"><b>СРОК ОТВЕТА</b></td>
                    </c:if>
                    <td style="background-color:#f2f2f2;"><b>КАСАТЕЛЬНО</b></td>
                </tr>


                 <!-- Непосредственно цикл, в котром мы выводим строчки таблицы со служебками -->
                 <c:forEach items='${memosList}' var='memo'>

                     <% String color = "white";%>

                     <c:if test="${boxType == 'inbox'}">
                         <c:forEach items="${memo.recipientsDepts}" var="recipientDept">
                             <c:if test="${(recipientDept.recipientDeptID == deptID) && (memo.realizedDate != null)}">
                                 <c:choose>
                                     <c:when test="${(recipientDept.realized != 1)&&(memo.overdue == 1)}">
                                         <%color = "#FFB8B3";%>
                                     </c:when>
                                 </c:choose>
                             </c:if>
                         </c:forEach>
                     </c:if>

                     <tr align=center>
                         <td style="background-color:<%=color%>;" >
                             <a href="controller?action=viewMemo&memoID=<c:out value="${memo.id}" />&boxType=<c:out value="${boxType}" />&deptID=<c:out value="${deptID}" />"><b>[<c:out
                                     value="${memo.memoNumber}"/>]</b></a></td>
                         <td style="background-color:<%=color%>;"><c:out value="${memo.timeStamp}"/></td>
                         <c:choose>
                             <c:when test="${boxType == 'inbox'}">
                                 <td style="background-color:<%=color%>;"><c:out value="${memo.executorDeptCode}"/></td>
                             </c:when>
                             <c:otherwise>
                                 <td style="background-color:<%=color%>;"><c:out value="${memo.executorShortName}"/></td>
                             </c:otherwise>
                         </c:choose>

                         <td style="background-color:<%=color%>;">
                             <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>
                                 <c:out value="${recipientDept.recipientDeptCode}"/>
                             </c:forEach>
                         </td>

                         <TD style="background-color:<%=color%>;" align=center><c:out value="${memo.sendDate}"/></TD>

                         <c:if test="${boxType == 'inbox'}">
                             <TD style="background-color:<%=color%>;" align=center>
                                 <c:choose>
                                     <c:when test="${memo.realizedDate != null}">
                                         <B><c:out value="${memo.realizedDate}"/></B>
                                     </c:when>
                                     <c:otherwise>-</c:otherwise>
                                 </c:choose>
                             </TD>
                         </c:if>

                         <td style="background-color:<%=color%>;" align=left>
                             <a href="controller?action=viewMemo&memoID=<c:out value="${memo.id}" />&boxType=<c:out value="${boxType}" />&deptID=<c:out value="${deptID}" />"><b><c:out
                                     value="${memo.subject}"/></b></a>
                         </td>

                     </tr>
                 </c:forEach>

        </table>
    </c:otherwise>
</c:choose>
<br><br>
<jsp:include page="_bottom.jsp"/>