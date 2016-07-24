<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/><jsp:param name="title" value="�������������� ������� EMAIL."/>
</jsp:include>
<center>
<a href="../system/controller?action=selectDept&type=mapping_email"><b>[�������� �������]</b></a><br><br>
<c:choose>
 <c:when test="${mapping == null}"><font color="blue"><b>������ MAPPING ����</b></font></c:when>


 <c:otherwise>

     <i>[��� �������� E-mail - ������ �� E-mail]</i><br>
     <table id="myTable1" class="rsdata-table-filter" width="40%">
         <thead>
         <tr>
             <th class="midth_col">��� ������</th>
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

<!-- ��������� ������� ����������-������������� -->
<br><br>
<a href="../system/controller?action=selectDept&type=mapping_dpm"><b>[�������� �������]</b></a><br>
<c:choose>
 <c:when test="${mapping2 == null}"><font color="blue"><b>������ MAPPING ���������-������������� ����!</b></font></c:when>

 <c:otherwise>

  <i>[��� �������� mapping - ������ �� ����������]</i><br>
  <i>[��� ��������� ������� ���������� ������������� - ������ �� ������ � ���� "���������"]</i><br>
  <table border=1>
   <tr>
    <td><b>���������</b></td>
    <td><b>��� ������</b></td>
    <td><b>���������</b></td>
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