<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<!-- ������������ ������ ������� ������� -->
<body onLoad="DownRepeat()">
<!-- ������������ ��������� ��������-->
<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/>
 <jsp:param name="title" value="����������� � ����������������� ������"/>
</jsp:include>
<center>

 <p align=left><b style="font-size:14pt">
    ����������� � ����������������� ������<br>��� ������������� ��������� ������� � <c:out value="${memo.memoNumber}"/>
 </b></p>

 <table width="99%" border=0>
  <tr>
   <td align=left>
    <form name="depts"method="POST" action='../controller?action=viewMemo&memoID=<c:out value="${memo.id}"/>&boxType=inbox'>
        <input type="submit" value='�� �������������� � �����' class=buttonStyle>
    </form>
   </td>

      <!-- ��������������� ����� ��� ������������ ������ ��� ������������� ��������� ������� -->
      <form name=calendar action='../editor/controller?action=saveForwardMemo' method='POST'>

          <td align=right>
              <input type="submit" value='��������� ������ ���������' class=buttonStyle>
          </td>
  </tr>
 </table>
    <br>
    <!-- ������� ���� - ������ �������-����������� �������� -->
    <c:forEach items="${memo.recipientsDepts}" var="recipientDept">
        <input type='hidden' name='recipient' value="<c:out value="${recipientDept.recipientDeptID}"/>">
    </c:forEach>
    <!-- ������� ���� - ������������� �������������� �������� -->
    <input type=hidden name=memoID value="<c:out value="${memo.id}" />">
    <!-- �������� � ������ ������ ��������� ����� ������ -->
    <table width=99% border=0 cellpadding=2 cellspacing=0 class=border>
        <tr>
            <td>

                <!-- �������� � ���������� ��� ����� ������ � ����� -->
                <table cellspacing="2" cellpadding=3 width="100%">

                    <!-- ���������, ������������� �������� -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>����������(�)</b></td>
                        <td bgcolor="#f0f0f0"><c:out value="${memo.executorShortName}"/></td>
                    </tr>

                    <!-- �����, � ������� �������� ���������, ������������� �������� -->
                    <tr align="left">                                                                             
                        <td bgcolor="#c7d0d9" width="20%"><b>������������� �����������</b></td>
                        <td bgcolor="#f0f0f0"><c:out value="${memo.executorDeptCode}"/></td>
                    </tr>

                    <!-- ������ �������-����������� ������������ �������� �������� -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%">
                            <b>���������� ������������ ��������</b>
                        </td>
                        <td bgcolor="#f0f0f0">                            
                            <c:forEach items="${memo.recipientsDepts}" var="recipientDept"><c:out
                                    value="${recipientDept.recipientDeptCode}"/> </c:forEach><br>
                        </td>
                    </tr>

                    <!-- ���������� �������� -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>����������</b></td>
                        <td bgcolor="#f0f0f0">
                            <textarea name="prim" rows="10" cols="80"></textarea>
                        </td>
                    </tr>

                </table>
            </td>
        </tr>
    </table>
    </form>
</center>
<jsp:include page="/_bottom.jsp"><jsp:param name="prefix" value="../"/></jsp:include>