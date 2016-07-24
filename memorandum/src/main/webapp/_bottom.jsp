<%@ page contentType="text/html; charset=windows-1251" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.util.Calendar" %>

<%
 // Получаем префикс для записи перед всеми относительными URL'ами
 String prefix = request.getParameter("prefix"); if (prefix == null) prefix = "";
 // Получаем год
 int year = new GregorianCalendar().get(Calendar.YEAR);
%>

        </td>
    </tr>
    <tr>
        <td colspan="2" background="<%=prefix%>template/images/bg_down-mid.gif" height="20">

            <span class="title" style="margin-left: 10px;">
                <b>Company name<span lang="en"> © </span><%=year%></b>
            </span>        

        </td>
    </tr>
</table>

</body>
</html>