<%@ page contentType="text/html; charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%
 // �������� ������� ��� ������ ����� ����� �������������� URL'���
 String prefix = request.getParameter("prefix"); if (prefix == null) prefix = "";
%>
<!-- ���� -->    

    <img border="0" src="<%=prefix%>template/images/bg_topmenubar1.gif" height="35">    

    <div id="my_menu" class="sdmenu">

        <!-- top left static menu -->
        <a href=""><b class="sdmenu3">Static menu item #1</b></a>
        <a href=""><b class="sdmenu3">Static menu item #2</b></a>
        <a href=""><b class="sdmenu3">Static menu item #3</b></a>
        <a href=""><b class="sdmenu3">Static menu item #4</b></a>
        
        <%--<div class="collapsed">--%>
            <%--<span>���� ��� ���������</span>--%>
            <%--<a href="http://gur.rs-head.spb.ru">���� ��� ���������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/survey/mainwin.html">������ ����������� ������������ ����������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/prom/index.html">������������ ������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/mkub/ubo.htm">������ ���������� ������������� � ������� (���)</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/onti/index.html">����</a>                                          --%>
            <%--<a href="http://gur.rs-head.spb.ru/win/skepnd/skepnd.htm">������ �������������, ����������, �������� � ����������� ������������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/quality/index.html">������ ��������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/kancler/index.htm">����� ����������������� ����������� ����������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/fin/index.htm">���������� ������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/jurists/index.htm">����������� ������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/004/index.html">������������� ������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/classify/mechan.htm">����� ������������� ������������ � ������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/021/index.htm">����� ���������� ���������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/kadry/index.htm">����� ������</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/008/index.htm">�����������-��������������� �����</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/014/index.htm">����� ��������� � �������</a>--%>
        <%--</div>--%>
       
        <hr style="border-bottom: 1px solid #8ce0ff; width:200px">

        <a href="<%=prefix%>controller?action=deptBox&boxType=inbox&pageNumber=1"><b  class="sdmenu3">�������� �������������</b></a>
        <a href="<%=prefix%>doc/instr.doc"><b  class="sdmenu3">����������</b></a>

        <% if (request.isUserInRole("memo_superuser")) {%>
             <a href="<%=prefix%>controller?action=depts"><b  class="sdmenu3">������ �������</b></a>
        <%}%>

        <a href="<%=prefix%>editor/controller?action=addStep1"><b  class="sdmenu3">������� ����� ��</b></a>

        <c:if test="${boxType != null && boxType != ''}">
             <div class="collapsed">
                <span><font color="#1F639B">������</font></span>
                <c:if test="${boxType == 'inbox'}">
                    <a href="<%=prefix%>controller?action=notMake&type=personnel" style="color:#1F639B;">������������� � ���� ��������� <c:out value="${member.shortName}"/></a>
                    <a href="<%=prefix%>controller?action=notMake" style="color:#1F639B;">������������� � ���� ��������� �� �� ������</a>
                    <a href="<%=prefix%>controller?action=deptBox&deptID=<c:out value="${member.departmentId}"/>&boxType=inbox&boxSubType=inbox_no_answer" style="color:#1F639B;">��� ��������� ������� ��������� ������</a>
                    <a href="<%=prefix%>controller?action=deptBox&deptID=<c:out value="${member.departmentId}"/>&boxType=inbox&boxSubType=inbox_no_answer_date" style="color:#1F639B;">������� ��� ������ � ������������� ����</a>
                </c:if>
                <c:if test="${boxType == 'outbox'}">
                    <a href="<%=prefix%>controller?action=deptBox&deptID=<c:out value="${member.departmentId}"/>&boxType=outbox&boxSubType=outbox_no_answer" style="color:#1F639B;">�������, �� ������� �� ��������</a>                 
                </c:if>
            </div>
        </c:if>

        <a href="<%=prefix%>controller?action=search"><b  class="sdmenu3">�����</b></a>                

        <% if (request.isUserInRole("memo_system")) {%>
            <a href="<%=prefix%>system/controller?action=Mapping"><b  class="sdmenu3">SYSTEM</b></a>

            <div class="collapsed">
                <span><font color="#1F639B"><b>������� �� �� �����</b></font></span>
                    <a href="<%=prefix%>system/controller?action=searchMember&newQuery=yes" style="color:#1F639B;">����� ��������� �� ���������</a>
            </div>
        <%}%>

    </div>

