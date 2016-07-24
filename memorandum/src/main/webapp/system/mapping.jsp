<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/><jsp:param name="title" value="ПЕРЕНАЗНАЧЕНИЕ АДРЕСОВ EMAIL."/>
</jsp:include>
<center>
<a href="../system/controller?action=selectDept&type=mapping_email"><b>[ДОБАВИТЬ МАППИНГ]</b></a><br><br>
<c:choose>
 <c:when test="${mapping == null}"><font color="blue"><b>СПИСОК MAPPING ПУСТ</b></font></c:when>


 <c:otherwise>

     <i>[Для удаления E-mail - нажать на E-mail]</i><br>
     <table id="myTable1" class="rsdata-table-filter" width="40%">
         <thead>
         <tr>
             <th class="midth_col">Код отдела</th>
             <th class="midth_col">E-mail</th>
         </tr>
         </thead>
         <tbody>
         <c:forEach items='${mapping}' var='map'>
             <tr>
                 <td><a href='../system/controller?action=deleteMapping&ID=<c:out value="${map.id}"/>'><c:out
                         value="${map.deptname}"/></a></td>
                 <td><a href='../system/controller?action=deleteMapping&ID=<c:out value="${map.id}"/>'><c:out
                         value="${map.email}"/></a></td>
             </tr>
         </c:forEach>
         </tbody>
     </table>
 </c:otherwise>
</c:choose>

<!-- Добавляем маппинг Сотрудники-Подразделения -->
<br><br>
<a href="../system/controller?action=selectDept&type=mapping_dpm"><b>[ДОБАВИТЬ МАППИНГ]</b></a><br>
<c:choose>
 <c:when test="${mapping2 == null}"><font color="blue"><b>СПИСОК MAPPING СОТРУДНИК-ПОДРАЗДЕЛЕНИЕ ПУСТ!</b></font></c:when>

 <c:otherwise>

  <i>[Для удаления mapping - нажать на Сотруднике]</i><br>
  <i>[Для изменения статуса начальника падразделения - нажать на ссылку в поле "Начальник"]</i><br>
  <table border=1>
   <tr>
    <td><b>Сотрудник</b></td>
    <td><b>Код отдела</b></td>
    <td><b>Начальник</b></td>
   </tr>
   <c:forEach items='${mapping2}' var='map2'>
    <tr>
     <td><a href='../system/controller?action=deleteMappingDPM&ID=<c:out value="${map2.id}"/>'><c:out value="${map2.shortName}"/></a></td>
     <td><c:out value="${map2.deptCode}"/></td>
     <td><a href='../system/controller?action=updateMappingDPM&ID=<c:out value="${map2.id}"/>'><c:out value="${map2.isChief}"/></a></td>     
    </tr>
   </c:forEach>
  </table>

 </c:otherwise>
</c:choose>

</center>
<jsp:include page="/_bottom.jsp"><jsp:param name="prefix" value="../"/></jsp:include>