<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/>
 <jsp:param name="title" value="СПРАВОЧНИК ПОДРАЗДЕЛЕНИЙ ГУР"/>
</jsp:include>
<c:choose>
 <c:when test="${deptsList == null}">СПИСОК ОТДЕЛОВ ГУР ПУСТ!</c:when>
 <c:otherwise>

  <script type="text/javascript">
   function asdf() {
       var objSel = document.nd_ch.depts;
       alert("ssdsdsdsd = ");}
    </script>


  <%! String action;%>
  <c:choose>
      <c:when test="${memoID > 0}"><%action="../editor/controller?action=editMemo";%></c:when>
      <c:otherwise><%action = "../editor/controller?action=addStep2";%></c:otherwise>
  </c:choose>
  <c:if test="${forward != null}"><%action = "../editor/controller?action=addStep2forForward";%></c:if>

     <form id="nd_ch" name='nd_ch' action='<%=action%>' method="POST">
         
         <input type=hidden name="memoID" value="<c:out value="${memoID}"/>">
         <input type=hidden name="parentID" value="<c:out value="${parentID}"/>">
         <input type=hidden name="memberID" value="<c:out value="${memberID}"/>">

         <table width="99%">
             <tr>
                 <td align="right" >
                     <input type="submit" value='ЗАВЕРШЕНИЕ ВЫБОРА ПОДРАЗДЕЛЕНИЙ' class=buttonStyle>                    
                 </td>
             </tr>
         </table>

         <br><br>

         <p><b><i>* Для выбора нескольких подразделений, используйте клавишу Ctrl</i></b></p>
         <table id="myTable1" class="rsdata-table-filter" width="99%" style="margin-left:3px">
             <TR>
                 <TD style="background-color:#f2f2f2;"><center><b>ПОДРАЗДЕЛЕНИЕ / АДРЕСАТ</b></center></TD>
             </TR>
             <tr>
                 <td>
                     <select name="depts" multiple size=25 style="width:99.5%">
                         <c:forEach items='${deptsList}' var='dept'>
                             <!-- Выводим подразделения в списке, кроме подразделения тек. пользователя, чтоб не отправлять самим себе -->
                             <c:if test="${member.departmentId != dept.id}">
                                <option value="<c:out value="${dept.id}"/>"><c:out value="${dept.departmentName}"/>
                             </c:if>
                             <!-- Если пользователь входит в группу memo_chief, то выводиться и его подразделение -->
                             <% if (request.isUserInRole("memo_chief")) {%>
                             <c:if test="${member.departmentId == dept.id}">
                                <option value="<c:out value="${dept.id}"/>"><c:out value="${dept.departmentName}"/>
                             </c:if>
                             <%}%>
                         </c:forEach>
                     </select>
                 </td>
             </tr>
         </table>
     </form>
 </c:otherwise>
</c:choose>
<jsp:include page="/_bottom.jsp" ><jsp:param name="prefix" value="../"/></jsp:include>