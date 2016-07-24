<%@ page pageEncoding="windows-1251" contentType="text/html; charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="/_top.jsp">
 <jsp:param name="title" value="ПОИСК СЛУЖЕБНОЙ ЗАПИСКИ"/>
</jsp:include>
<center>

<c:if test="${(memosCount == null) || (memosCount == 0)}">
<!-- Табличка с рамкой вокруг формы поиска -->
<table width=90% border=0 cellpadding=2 cellspacing=0 class=border><tr><td>

 <!-- Непосредственно форма поиска -->
 <table cellspacing="2" cellpadding=3 width="100%">
 <form name=calendar action="controller?action=searchDo" method="POST">

  <tr>
   <td bgcolor="#c7d0d9" width="25%"><b>НОМЕР СЛУЖЕБНОЙ ЗАПИСКИ</b></td>
   <td bgcolor="#f0f0f0"><input type="text" name="memoNumber" value="" size="5"/></td>
  </tr>

  <tr>
   <td bgcolor="#c7d0d9" width="25%"><b>ОТДЕЛ ОТПРАВИТЕЛЬ СЛУЖЕБНОЙ ЗАПИСКИ</b></td>
   <td bgcolor="#f0f0f0">
       <select name="executorDeptCode">
           <option value="0"> </option>
           <c:forEach items='${departmentList}' var='department'>
               <option value="<c:out value="${department.id}"/>"><c:out value="${department.departmentCode}"/>
           </c:forEach>
       </select>
   </td>
  </tr>

  <tr>
   <td bgcolor="#c7d0d9" width="25%"><b>ОТДЕЛ ПОЛУЧАТЕЛЬ СЛУЖЕБНОЙ ЗАПИСКИ</b></td>
   <td bgcolor="#f0f0f0">
       <select name="recipientDeptCode">
           <option value="0"> </option>
           <c:forEach items='${departmentList}' var='department'>
               <option value="<c:out value="${department.id}"/>"><c:out value="${department.departmentCode}"/>
           </c:forEach>
       </select>
   </td>
  </tr>

  <tr>
   <td bgcolor="#c7d0d9" width="25%"><b>ПОДГОТОВИЛ СЛУЖЕБНУЮ ЗАПИСКУ</b></td>
   <td bgcolor="#f0f0f0">
       <select name="executorUserId">
           <option value="0"> </option>
           <c:forEach items='${memberList}' var='simpleEmployee'>
               <option value="<c:out value="${simpleEmployee.personnelId}"/>"><c:out value="${simpleEmployee.shortName}"/>
                   <c:if test="${simpleEmployee.personnelId > 0}">
                   - <c:out value="${simpleEmployee.departmentCode}"/></option>
                   </c:if>

           </c:forEach>
       </select>
   </td>
  </tr>

  <tr>
   <td bgcolor="#c7d0d9" width="25%"><b>КАСАТЕЛЬНО</b></td>
   <td bgcolor="#f0f0f0">
    <input type="text" name="subject" value="" size="50" />    
   </td>
  </tr>

  <tr>
   <td bgcolor="#c7d0d9" width="30%"><b>ИНТЕРВАЛ ДАТЫ СОЗДАНИЯ</b></td>
   <td bgcolor="#f0f0f0">

    <table width=100%>
     <tr align=left>
      <td><b>начало интервала:</b></td>
      <td>
       <INPUT name=date_start value="<c:out value="${date_start_default}"/>">
       <IMG onclick="popUpCalendar(this, calendar.date_start, 'dd/mm/yyyy');"
        height=20 hspace=3 src="template/calendar/date_selector.gif" width=20 border=0 align="bottom">
      </td>
     </tr>
     <tr align=left>
      <td><b>конец интервала:</b></td>
      <td>
       <INPUT name=date_end value="<c:out value="${date_end_default}"/>">
       <IMG onclick="popUpCalendar(this, calendar.date_end, 'dd/mm/yyyy');"
        height=20 hspace=3 src="template/calendar/date_selector.gif" width=20 border=0 align="bottom">
      </td>
     </tr>
    </table>

   </td>
  </tr>

  <tr><td colspan=2 align=center>
      <input type=submit class='buttonActiveStyle' name=submit value="ИСКАТЬ!"></td>
      </tr>

 </form>
 </table>

</td></tr></table>
</c:if>

<!--
 Если мы нашли какие-либо служебки - выводим табличку с ними. О типе выводимой инфы делаем вывод по параметру memosCount:
  - memosCount == null    - первый вход на страницу поиска
  - memosCount == 0       - ничего не найдено по запросу
  - memosCount == <число> - найденное по запросу кол-во служебок
-->
<c:choose>
 <c:when test="${memosCount == 0}"><p align=center><b> <font color=red>[ПО ВАШЕМУ ЗАПРОСУ НИЧЕГО НЕ НАЙДЕНО!]</font></b></p></c:when>
 <c:when test="${memosCount == null}"></c:when>
 <c:otherwise>
  <% String prefix = "outbox";%>
     <c:if test="${memosCount == 50}">
         <p align=center><b>[ПО ВАШЕМУ ЗАПРОСУ НАЙДЕНО БОЛЕЕ 50 СЛУЖЕБНЫХ ЗАПИСОК. УТОЧНИТЕ УСЛОВИЯ ВЫБОРКИ]</b></p>
     </c:if>
     <c:if test="${memosCount < 50}">
         <p align=center><b>[ПО ВАШЕМУ ЗАПРОСУ НАЙДЕНО <c:out value="${memosCount}"/> СЛУЖЕБНЫХ ЗАПИСОК.]</b></p>
     </c:if>

  <table border="0" cellpadding="2" cellspacing="2" width="100%">
   <TR bgcolor="#e3eff9" align="center">
    <TD class=border_blue width="6%"><B>№ СЗ</B></TD>
    <TD class=border_blue><B>ДАТА</B></TD>
    <td class=border_blue><b>ОТПРАВИТЕЛЬ</b></td>
    <TD class=border_blue><B>ПОЛУЧАТЕЛИ</B></TD>
    <TD class=border_blue><B>ОТПРАВЛЕНО</B></TD>
    <TD class=border_blue width="50%"><B>КАСАТЕЛЬНО</B></TD>
   </TR>
   <c:forEach items='${memoList}' var='memo'>
    <tr bgcolor="#f0f0f0">
     <td><b>[<c:out value="${memo.memoNumber}" />]</b></td>
     <td><c:out value="${memo.timeStamp}"/></td>
     <td><c:out value="${memo.executorDeptCode}"/></td>
     <!-- проверка inbox/outbox сделана для того, чтоб после поиска, при входе в служебку появлялись доп. функции (кнопки) -->
     <td>
      <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>
         <c:if test="${(member.departmentCode == recipientDept.recipientDeptCode)}"><% prefix = "inbox";%></c:if>
       <c:out value="${recipientDept.recipientDeptCode}"/>
      </c:forEach>
     </td>
     <TD align=center><c:out value="${memo.sendDate}"/></TD>
     <td><a href="controller?action=viewMemo&memoID=<c:out value="${memo.id}" />&boxType=<%=prefix%>&ViewType=all"><b><c:out value="${memo.subject}"/></b></a></td>
    </tr>
   </c:forEach>
  </table>
 </c:otherwise>
</c:choose>

</center>
<jsp:include page="_bottom.jsp" />