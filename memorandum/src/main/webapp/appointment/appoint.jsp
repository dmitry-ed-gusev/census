<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/><jsp:param name="title" value="ПОРУЧЕНИЕ СЛУЖЕБНОЙ ЗАПИСКИ"/>
</jsp:include>
<c:choose>
 <c:when test="${membersList == null}">СПИСОК СОТРУДНИКОВ ОТДЕЛА ПУСТ!</c:when>
 <c:otherwise>
  <center>
   <c:if test="${Warning != null}"><font size=+2 color='red'><b><c:out value="${Warning}"/></b></font><br><br></c:if>
   <!-- Табличка с рамкой вокруг формы для ввода данных -->
   <table width=90% border=0 cellpadding=2 cellspacing=0 class=border><tr><td>

    <form name=calendar action='../appointment/controller?action=appointResult' method="POST">
     <input type=hidden name="memoID" value="<c:out value="${memoID}"/>">

     <!-- Непосредственно табличка с формой(с элементами ввода) -->
     <table width=100% border=0>
      <tr align=center>
       <!-- Текст комментария для поручения -->
       <td valign="top">
        <center><b>ЗАДАНИЕ</b></center>
        <textarea name="subject" rows="5" cols="50"><c:out value="${subject}"/></textarea>
       </td>
       <!-- Список сотрудников отдела -->
       <td rowspan=3>
        <center><b>СОТРУДНИКИ ОТДЕЛА <c:out value="${member.departmentCode}"/> </b></center>
        <select name = "memberID" multiple size=15 style="width:99.5%">
         <c:forEach items='${membersList}' var='member'>
          <!-- При возврате на данную страницу передается ID выбранного сотрудника(memberID), или -1 -->
          <option value="<c:out value="${member.personnelId}"/>" <c:if test="${memberID == member.personnelId}">selected</c:if>> <c:out value="${member.shortName}"/>
         </c:forEach>
        </select>
       </td>
      </tr>
      <tr align=center>
       <!-- Срок ответа на данную служебку -->
       <td  valign="top">
        <center><b>СРОК ИСПОЛНЕНИЯ ЗАДАНИЯ</b></center>
        <INPUT name=realizedDate readonly value="<c:out value="${realizedDate}"/>">
         <IMG onclick="popUpCalendar(this, calendar.realizedDate, 'dd/mm/yyyy');"
          height=20 hspace=3 src="../template/calendar/date_selector.gif" width=20 border=0 align="bottom" alt=calendar>
       </td>
      </tr>
      <tr>
          <td>
              <center><b>ПОЛУЧИТЬ УВЕДОМЛЕНИЕ ПО E-MAIL ПРИ ВЫПОЛНЕНИИ ПОРУЧЕНИЯ</b><BR>
              <input type='checkbox' name='sendemail' <c:if test="${sendmail != null}">checked</c:if>></center>
          </td>
      </tr>
      <!-- Кнопка для выполнения действия -->
      <tr align=center><td colspan=2><br><input type="submit" class='buttonStyle' value='ПОРУЧИТЬ'></td></tr>
     </table>
    </form>
    <center><form action='../controller?action=viewMemo&memoID=<c:out value="${memoID}"/>&boxType=inbox' method="POST">
       <input type="submit" class='buttonStyle' value='ОТКАЗАТЬСЯ ОТ ПОРУЧЕНИЯ'>
    </form></center>
   </td></tr></table>
  </center>
 </c:otherwise>
</c:choose>
<jsp:include page="/_bottom.jsp"><jsp:param name="prefix" value="../"/></jsp:include>