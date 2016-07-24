<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="/_top.jsp">
    <jsp:param name="prefix" value="../"/>
    <jsp:param name="title" value="оНХЯЙ ОЕПЯНМЮКЮ ОН ЙПХРЕПХЪЛ"/>
</jsp:include>

<center>

<table width=90% border=0 cellpadding=2 cellspacing=0 class=border>
    <tr>
        <td>
            <table cellspacing="2" cellpadding=3 width="100%">
                <form name=calendar action="../system/controller?action=searchMember" method="POST" >
                    <tr>
                        <td bgcolor="#c7d0d9" width="30%"><b>хдемрхтхйюрнп</b></td>
                        <td bgcolor="#f0f0f0"><INPUT type="text" size="20" name=id value=""></td>
                    </tr>
                    <tr>
                        <td bgcolor="#c7d0d9" width="30%"><b>тюлхкхъ</b></td>
                        <td bgcolor="#f0f0f0"><INPUT type="text" size="20" name=family value=""></td>
                    </tr>
                    <tr>
                        <td bgcolor="#c7d0d9" width="30%"><b>хлъ / нрверярбн</b></td>
                        <td bgcolor="#f0f0f0"><INPUT type="text" size="20" name=initials value=""></td>
                    </tr>
                    <tr>
                        <td bgcolor="#c7d0d9" width="30%"><b>днкфмнярэ</b></td>
                        <td bgcolor="#f0f0f0"><INPUT type="text" size="20" name=post value=""></td>
                    </tr>
                    <tr>
                        <td bgcolor="#c7d0d9" width="30%"><b>рхо ондпюгдекемхъ</b></td>

                        <td bgcolor="#f0f0f0">
                            цсп<input type="radio" name="departmentType" value="0" checked="checked">
                            бяе<input type="radio" name="departmentType" value="1">
                        </td>

                    </tr>
                    <tr>
                        <td bgcolor="#c7d0d9" width="30%"><b>ондпюгдекемхе</b></td>
                        <td bgcolor="#f0f0f0">
                            <select name="departmentId">
                                <option value="0"></option>
                                <c:forEach items='${departmentList}' var='department'>
                                <option value="<c:out value="${department.id}"/>">
                                        <c:out value="${department.departmentCode}"/>
                                    </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#c7d0d9" width="30%"><b>дюрю опхмърхъ б пецхярп (МЮВХМЮЕРЯЪ НР)</b></td>
                        <td bgcolor="#f0f0f0">
                            <INPUT name=hiredDate value="">
                            <IMG onclick="popUpCalendar(this, calendar.hiredDate, 'dd/mm/yyyy');" height=20 hspace=3
                                 src="../template/calendar/date_selector.gif" width=20 border=0 align="bottom">
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#c7d0d9" width="30%"><b>дюрю сбнкэмемхъ (МЮВХМЮЕРЯЪ НР)</b></td>
                        <td bgcolor="#f0f0f0">
                            <INPUT name=firedDate value="" readonly>
                            <IMG onclick="popUpCalendar(this, calendar.firedDate, 'dd/mm/yyyy');" height=20 hspace=3
                                 src="../template/calendar/date_selector.gif" width=20 border=0 align="bottom">
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#c7d0d9" width="30%"><b>опхгмюй сбнкэмемхъ</b></td>
                        <td bgcolor="#f0f0f0">                            
                            рнкэйн юйрхбмше<input type="radio" name="isFired" value="0" checked="checked">
                            бяе<input type="radio" name="isFired" value="1">
                        </td>
                    </tr>
                    <tr>
                        <td colspan=2 bgcolor="#f0f0f0" align=center>
                            <input type=submit class='buttonActiveStyle' name=submit value="хяйюрэ!"></td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
<br>
<br>

<c:if test="${simpleEmployees != null}">
     <%! int size = 0;%>
     <%List d = (List)request.getAttribute("simpleEmployees"); size = d.size(); %>
     <b>йнкхвеярбн мюидемшу янрпсдмхйнб: <%=size%></b>
     <br>
     <br>
     <TABLE cellPadding=0 width="90%" border=1>
         <TR>
             <td><b>ID</b></td>
             <td><b>тхн</b></td>
             <td><b>днкфмнярэ</b></td>
             <td><b>ондпюгдекемхе</b></td>
             <td><b>пюанвюъ цпсоою</b></td>
             <td><b>дюрю опхмърхъ</b></td>
             <td><b>дюрю сбнкэмемхъ</b></td>
         </TR>
         
         <c:forEach items='${simpleEmployees}' var='simpleEmployee'>
             <TR>
                 <td><c:out value="${simpleEmployee.personnelId}"/></td>
                 <td><c:out value="${simpleEmployee.family}"/> <c:out value="${simpleEmployee.fullInitials}"/></td>
                 <td><c:out value="${simpleEmployee.post}"/></td>
                 <td><c:out value="${simpleEmployee.departmentCode}"/></td>
                 <td title='<c:out value="${simpleEmployee.workGroupName}"/>'><c:out value="${simpleEmployee.workGroupCode}"/></td>
                 <td><c:out value="${simpleEmployee.hiredDate}"/></td>
                 <td><c:out value="${simpleEmployee.firedDate}"/></td>           
             </TR>
         </c:forEach>

       </TABLE>
</c:if>

<jsp:include page="/_bottom.jsp">
    <jsp:param name="prefix" value="../"/>
</jsp:include>