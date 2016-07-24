<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<jsp:include page="/_top.jsp">
    <jsp:param name="prefix" value="../"/>
    <jsp:param name="title" value="����� �� ���������� ��������� �������"/>
</jsp:include>

<script language="JavaScript" type="text/javascript">
    function anonsCounter() {
        maxlimit = 200;
        if (document.calendar.answer.value.length > maxlimit) {
            document.calendar.answer.value = document.calendar.answer.value.substring(0, maxlimit);
            alert('�� ����� 200 ��������!');
        }
    }
</script>


<center>
    <!-- �������� � ������ ������ ����� ��� ����� ������ -->
    <table width=95% border=0 cellpadding=2 cellspacing=0 class=border>
        <tr>
            <td>

                <!-- ��������������� �������� � ������(� ���������� �����) -->
                <table width=100% border=0 cellpadding=2 cellspacing=2>

                    <tr align=center>
                        <td colspan=2 bgcolor="#c7d0d9">
                            <b>����� �� ���������� ��������� ������� � <c:out value="${memo.memoNumber}"/></b>
                        </td>
                    </tr>

                    <tr>
                        <td>&nbsp;</td>
                    </tr>

                    <tr>
                        <td align=left bgcolor="#c7d0d9"><b>����� ���������:</b></td>
                        <td bgcolor="#f0f0f0"><c:out value="${recipient.subject}"/></td>
                    </tr>

                    <tr>
                        <td align=left bgcolor="#c7d0d9"><b>���� ���������� ���������:</b></td>
                        <td bgcolor="#f0f0f0"><c:out value="${recipient.realizedDate}"/></td>
                    </tr>

                    <tr>
                        <td align=left bgcolor="#c7d0d9"><b>���������, �������� �������� ������ ��������� �������:</b>
                        </td>
                        <td bgcolor="#f0f0f0"><c:out value="${recipient.recipientShortName}"/></td>
                    </tr>

                    <tr>
                        <td>&nbsp;</td>
                    </tr>

                    <form name="calendar" action='../editor/controller?action=answerAppointResult' method="POST">
                        <input type=hidden name="memoID" value="<c:out value="${memo.id}"/>">
                        <input type=hidden name="id" value="<c:out value="${recipient.id}"/>">
                        <!-- ����� ������ �� ��������� -->
                        <tr>
                            <td align="center" valign="top" colspan=2>
                                <b>����� �� ��������� ��������� ������� (�� ����� 200 ��������)</b></td>
                        </tr>
                        <tr>
                            <td align="center" colspan=2>
                                <textarea name="answer" rows="4" cols="70" onChange="anonsCounter();"
                                          onKeyDown="anonsCounter();"
                                          onKeyUp="anonsCounter();" onKeyPress="anonsCounter();"
                                          onFocus="anonsCounter();" onBlur="anonsCounter();"
                                          onMouseOver="anonsCounter();" onMouseOut="anonsCounter();"></textarea>
                            </td>
                        </tr>
                        <!-- ������ ��� ���������� �������� -->
                        <tr align=center>
                            <td colspan=2><input type="submit" class='buttonStyle' value='�������� �� ���������'></td>
                        </tr>
                    </form>
                </table>
            </td>
        </tr>
    </table>
</center>
<jsp:include page="/_bottom.jsp">
    <jsp:param name="prefix" value="../"/>
</jsp:include>