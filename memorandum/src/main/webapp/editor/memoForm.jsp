<%@ page pageEncoding="windows-1251"  contentType="text/html; charset=windows-1251" %>
<%@ page import="org.census.commons.dto.docs.memoDTO" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<!-- ������������ ������ ������� ������� -->
<body onLoad="DownRepeat()">
<!-- ������������ ��������� �������� � ����������� �� ���� �������� -->
    <%! String title;%>
<c:choose>
    <c:when test="${memo.id > 0}"><% title = "��������������";%>
    </c:when>
    <c:otherwise><% title = "��������";%>
    </c:otherwise>
</c:choose>

<jsp:include page="/_top.jsp">
    <jsp:param name="prefix" value="../"/>
    <jsp:param name="title" value='<%=title + " ��������� �������"%>'/>
</jsp:include>
<center>

    <p align=left><b style="font-size:14pt">
        <%=title + " ��������� �������"%><c:if test="${memo.id > 0}"> � <c:out value="${memo.memoNumber}"/></c:if>
    </b></p>

    <!-- ������ ���������� ������ �� ���������� �����. -->
    <%! String save_buttonValue
            ,
            exit_buttonValue;%>
    <c:choose>
        <c:when test="${memo.id > 0}">
            <%
                save_buttonValue = "��������� ��������� � �����";
                exit_buttonValue = "�� ��������� ��������� � �����";
            %>
        </c:when>
        <c:otherwise>
            <%
                save_buttonValue = "���������������� � �����";
                exit_buttonValue = "�� �������������� � �����";
            %>
        </c:otherwise>
    </c:choose>

    <table width="99%">
        <tr>
            <td align=left>
                <form name="depts" method="POST"
                        <c:choose>
                            <c:when test="${memo.id > 0}">action='../controller?action=viewMemo&memoID=<c:out value="${memo.id}"/>&boxType=outbox'
                            </c:when>
                            <c:otherwise>action='../controller?action=deptBox&boxType=inbox&pageNumber=1'</c:otherwise>
                        </c:choose>>
                    <input type="submit" value='<%=exit_buttonValue%>' class=buttonStyle>
                </form>
            </td>

            <!-- ��������������� ����� ��������������/���������� ��������� ������� -->
            <form name=calendar action='../editor/controller?action=saveMemo&memoID=<c:out value="${memo.id}" />'
                  method='POST' enctype="multipart/form-data" accept-charset="windows-1251">

                <td align=right>
                    <input type="submit" value='<%=save_buttonValue%>' class=buttonStyle>                    
                </td>
        </tr>
    </table>
    <br>
    <!-- ������� ���� - ������ �������-����������� �������� -->
    <c:forEach items="${memo.recipientsDepts}" var="recipientDept">
        <input type='hidden'  name='recipient' value="<c:out value="${recipientDept.recipientDeptID}"/>">
    </c:forEach>
    <!-- ������� ���� - ������������� ������������� �������� (� ����� �� = -1) -->
    <input type=hidden name=memoID value="<c:out value="${memo.id}" />">
    <!-- ������� ���� - ������������� ������������ �������� -->
    <input type=hidden name=parentID value="<c:out value="${memo.parentID}" />">

    <!-- �������� � ������ ������ ��������� ����� ������ -->
    <table width=99% border=0 cellpadding=2 cellspacing=0 class=border>
        <tr>
            <td>

                <!-- �������� � ���������� ��� ����� ������ � ����� -->
                <table cellspacing="2" cellpadding=3 width="100%">

                    <!-- ����� ��������, ������� �� ������� �������� ������ -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>����� �� �� � </b></td>
                        <td bgcolor="#f0f0f0">
                            <c:choose>
                                <c:when test="${memo.parentID > 0}"><c:out value="${parentMemo.memoNumber}"/></c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

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

                    <!-- ���� �������� -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>����������</b></td>
                        <td bgcolor="#f0f0f0">
                            <!-- ���� �� �������� �� ������ �������� - ������� �� ����. -->
                            <c:choose>
                                <c:when test="${memo.parentID > 0}">
                                    <input type="text" name="subject" size="100" class=input
                                           value="RE: <c:out value="${parentMemo.subject}"/>">
                                </c:when>
                                <c:otherwise>
                                    <input type="text" name="subject" size="100" class=input
                                           value="<c:out value="${memo.subject}"/>">
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                    <!-- ������ �������-����������� �������� -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%">
                            <b>����������</b>
                            <!-- ���� �� ��������� � ������ ��������������, �� ��������� �������� ��� ��������� ������ ����������� -->
                            <c:if test="${memo.id > 0}">
                                <br><a href="../editor/controller?action=addStep1&memoID=<c:out value="${memo.id}"/>">
                                <b><font color=blue>[��������]</font></b></a>
                            </c:if>
                        </td>
                        <td bgcolor="#f0f0f0">
                            <c:forEach items="${memo.recipientsDepts}" var="recipientDept"><c:out
                                    value="${recipientDept.recipientDeptCode}"/> </c:forEach><br>
                        </td>
                    </tr>

                    <!-- ���� �� � ������ �������������� - ��������� �������������� -->
                    <c:if test="${memo.id > 0}">
                        <tr align=center bgcolor="#c7d0d9">
                            <td colspan=2><i><b>��������! ��� ��������� ������ ����������� ������� ������ ��������!</b></i>
                            </td>
                        </tr>
                    </c:if>

                    <!-- ���� ������ �� ������ �������� -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>���� ������</b></td>
                        <td bgcolor="#f0f0f0">
                            <INPUT name=realizedDate readonly value="<c:out value="${memo.realizedDate}"/>">
                            <IMG onclick="popUpCalendar(this, calendar.realizedDate, 'dd/mm/yyyy');"
                                 height=20 hspace=3 src="../template/calendar/date_selector.gif" width=20 border=0
                                 align="bottom" alt=calendar>
                        </td>
                    </tr>

                    <!-- ���������� �������� -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>����������</b></td>
                        <td bgcolor="#f0f0f0">
                            <!--
                              1)���� ((memo.id > 0) && (memo.parentID > 0)) - �� ����������� ��������, � ������� ���� ������������ - ������� �����
                                 ����� ������������� ��������
                              2)���� ((memo.id > 0) && (memo.parentID < 0)) - �� ����������� ��������, � ������� ��� ������������ - ������� �����
                                 ����� ������������� ��������
                              3)���� ((memo.id < 0) && (memo.parentID > 0)) - �� �������� �� ������ �������� (������� ����� � �����) - ������� �����
                                 ��������, �� ������� ��������
                              4)���� ((memo.id < 0) && (memo.parentID < 0)) - �� ������ ������� ����� �������� - ������ �� �������, �� ����� ��������
                                 ��� ������ ������ ����� �������� - �� ������ ����!
                              5)��� ������ ��� �������� ����� ��������, ���� �����-���������� ����, �� �������: ���������/��������� ��� �������� ���-��

                            -->
                            <c:choose>
                                <c:when test="${(memo.parentID > 0) && (memo.id < 0)}">
                                    <% memoDTO parentMemo = (memoDTO) request.getAttribute("parentMemo"); %>
                                    <textarea name="text" rows="10" cols="80"><c:if
                                            test="${one_only != null}"><c:forEach items="${memo.recipientsDepts}"
                                                                                  var="recipientDept"><c:out
                                            value="${recipientDept.recipientChief}"/></c:forEach></c:if><%
                                        if (parentMemo != null)
                                            out.println("\n" + "\n" + "** " + parentMemo.getText() + " **");
                                    %></textarea>
                                </c:when>
                                <c:otherwise>
                                    <textarea name="text" rows="10" cols="80"><c:if
                                            test="${one_only != null}"><c:forEach items="${memo.recipientsDepts}"
                                                                                  var="recipientDept"><c:out
                                            value="${recipientDept.recipientChief}"/></c:forEach></c:if><c:out
                                            value="${memo.text}"/></textarea>
                                </c:otherwise>
                            </c:choose>


                        </td>
                    </tr>

                    <!-- ����������� � �������� ���� -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>����������</b></td>
                        <td bgcolor="#f0f0f0">
                            
                            <c:if test="${(memo.id > 0) && (memo.attachedFile != null)}">
                                <input type="hidden" name="fileExt" value="<c:out value="${memo.attachedFile}"/>">
                            </c:if>

                            <input type="file" name="file" size=90 class=input><br>
                            <!-- ���� �� ����������� �������� � ������� ��� ���� ����������, ������� �� ���� -->
                            <c:if test="${(memo.id > 0) && (memo.attachedFile != null)}">
                                <i>
                                    � ������ ��������� ������� ��� ���� ������������� ����
                                    <a href="../downloadAttach?memoID=<c:out value="${memo.id}"/>">
                                        <b>[<c:out value="${memo.id}"/>.<c:out value="${memo.attachedFile}"/>]</b>
                                    </a><br>
                                    <b>��������! ������������ ��� ������ ����� ������ ����������!</b>
                                </i>
                            </c:if>
                        </td>
                    </tr>

                </table>
            </td>
        </tr>
    </table>
    </form>
</center>
<jsp:include page="/_bottom.jsp">
    <jsp:param name="prefix" value="../"/>
</jsp:include>