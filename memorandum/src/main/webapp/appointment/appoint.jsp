<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/><jsp:param name="title" value="��������� ��������� �������"/>
</jsp:include>
<c:choose>
 <c:when test="${membersList == null}">������ ����������� ������ ����!</c:when>
 <c:otherwise>
  <center>
   <c:if test="${Warning != null}"><font size=+2 color='red'><b><c:out value="${Warning}"/></b></font><br><br></c:if>
   <!-- �������� � ������ ������ ����� ��� ����� ������ -->
   <table width=90% border=0 cellpadding=2 cellspacing=0 class=border><tr><td>

    <form name=calendar action='../appointment/controller?action=appointResult' method="POST">
     <input type=hidden name="memoID" value="<c:out value="${memoID}"/>">

     <!-- ��������������� �������� � ������(� ���������� �����) -->
     <table width=100% border=0>
      <tr align=center>
       <!-- ����� ����������� ��� ��������� -->
       <td valign="top">
        <center><b>�������</b></center>
        <textarea name="subject" rows="5" cols="50"><c:out value="${subject}"/></textarea>
       </td>
       <!-- ������ ����������� ������ -->
       <td rowspan=3>
        <center><b>���������� ������ <c:out value="${member.departmentCode}"/> </b></center>
        <select name = "memberID" multiple size=15 style="width:99.5%">
         <c:forEach items='${membersList}' var='member'>
          <!-- ��� �������� �� ������ �������� ���������� ID ���������� ����������(memberID), ��� -1 -->
          <option value="<c:out value="${member.personnelId}"/>" <c:if test="${memberID == member.personnelId}">selected</c:if>> <c:out value="${member.shortName}"/>
         </c:forEach>
        </select>
       </td>
      </tr>
      <tr align=center>
       <!-- ���� ������ �� ������ �������� -->
       <td  valign="top">
        <center><b>���� ���������� �������</b></center>
        <INPUT name=realizedDate readonly value="<c:out value="${realizedDate}"/>">
         <IMG onclick="popUpCalendar(this, calendar.realizedDate, 'dd/mm/yyyy');"
          height=20 hspace=3 src="../template/calendar/date_selector.gif" width=20 border=0 align="bottom" alt=calendar>
       </td>
      </tr>
      <tr>
          <td>
              <center><b>�������� ����������� �� E-MAIL ��� ���������� ���������</b><BR>
              <input type='checkbox' name='sendemail' <c:if test="${sendmail != null}">checked</c:if>></center>
          </td>
      </tr>
      <!-- ������ ��� ���������� �������� -->
      <tr align=center><td colspan=2><br><input type="submit" class='buttonStyle' value='��������'></td></tr>
     </table>
    </form>
    <center><form action='../controller?action=viewMemo&memoID=<c:out value="${memoID}"/>&boxType=inbox' method="POST">
       <input type="submit" class='buttonStyle' value='���������� �� ���������'>
    </form></center>
   </td></tr></table>
  </center>
 </c:otherwise>
</c:choose>
<jsp:include page="/_bottom.jsp"><jsp:param name="prefix" value="../"/></jsp:include>