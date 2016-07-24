<%@ page contentType="text/html; charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%
 // Получаем префикс для записи перед всеми относительными URL'ами
 String prefix = request.getParameter("prefix"); if (prefix == null) prefix = "";
%>
<!-- МЕНЮ -->    

    <img border="0" src="<%=prefix%>template/images/bg_topmenubar1.gif" height="35">    

    <div id="my_menu" class="sdmenu">

        <!-- top left static menu -->
        <a href=""><b class="sdmenu3">Static menu item #1</b></a>
        <a href=""><b class="sdmenu3">Static menu item #2</b></a>
        <a href=""><b class="sdmenu3">Static menu item #3</b></a>
        <a href=""><b class="sdmenu3">Static menu item #4</b></a>
        
        <%--<div class="collapsed">--%>
            <%--<span>Сайт для персонала</span>--%>
            <%--<a href="http://gur.rs-head.spb.ru">Сайт для персонала</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/survey/mainwin.html">Служба организации технического наблюдения</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/prom/index.html">Промышленная служба</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/mkub/ubo.htm">Служба управления безопасностью и охраной (УБО)</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/onti/index.html">ОНТИ</a>                                          --%>
            <%--<a href="http://gur.rs-head.spb.ru/win/skepnd/skepnd.htm">Служба классификации, экспертизы, проектов и нормативной деятельности</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/quality/index.html">Служба качества</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/kancler/index.htm">Отдел документационного обеспечения управления</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/fin/index.htm">Финансовая служба</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/jurists/index.htm">Юридическая служба</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/004/index.html">Международная служба</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/classify/mechan.htm">Отдел механического оборудования и систем</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/021/index.htm">Отдел подготовки персонала</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/kadry/index.htm">Отдел кадров</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/008/index.htm">Издательско-полиграфический отдел</a>--%>
            <%--<a href="http://gur.rs-head.spb.ru/win/014/index.htm">Отдел протокола и рекламы</a>--%>
        <%--</div>--%>
       
        <hr style="border-bottom: 1px solid #8ce0ff; width:200px">

        <a href="<%=prefix%>controller?action=deptBox&boxType=inbox&pageNumber=1"><b  class="sdmenu3">Входящие подразделения</b></a>
        <a href="<%=prefix%>doc/instr.doc"><b  class="sdmenu3">Инструкция</b></a>

        <% if (request.isUserInRole("memo_superuser")) {%>
             <a href="<%=prefix%>controller?action=depts"><b  class="sdmenu3">Список отделов</b></a>
        <%}%>

        <a href="<%=prefix%>editor/controller?action=addStep1"><b  class="sdmenu3">Создать новую СЗ</b></a>

        <c:if test="${boxType != null && boxType != ''}">
             <div class="collapsed">
                <span><font color="#1F639B">Сервис</font></span>
                <c:if test="${boxType == 'inbox'}">
                    <a href="<%=prefix%>controller?action=notMake&type=personnel" style="color:#1F639B;">Невыполненные в срок поручения <c:out value="${member.shortName}"/></a>
                    <a href="<%=prefix%>controller?action=notMake" style="color:#1F639B;">Невыполненные в срок поручения по СЗ отдела</a>
                    <a href="<%=prefix%>controller?action=deptBox&deptID=<c:out value="${member.departmentId}"/>&boxType=inbox&boxSubType=inbox_no_answer" style="color:#1F639B;">Все служебные записки требующие ответа</a>
                    <a href="<%=prefix%>controller?action=deptBox&deptID=<c:out value="${member.departmentId}"/>&boxType=inbox&boxSubType=inbox_no_answer_date" style="color:#1F639B;">Записки без ответа в установленный срок</a>
                </c:if>
                <c:if test="${boxType == 'outbox'}">
                    <a href="<%=prefix%>controller?action=deptBox&deptID=<c:out value="${member.departmentId}"/>&boxType=outbox&boxSubType=outbox_no_answer" style="color:#1F639B;">Записки, на которые не ответили</a>                 
                </c:if>
            </div>
        </c:if>

        <a href="<%=prefix%>controller?action=search"><b  class="sdmenu3">ПОИСК</b></a>                

        <% if (request.isUserInRole("memo_system")) {%>
            <a href="<%=prefix%>system/controller?action=Mapping"><b  class="sdmenu3">SYSTEM</b></a>

            <div class="collapsed">
                <span><font color="#1F639B"><b>Выборки из БД Кадры</b></font></span>
                    <a href="<%=prefix%>system/controller?action=searchMember&newQuery=yes" style="color:#1F639B;">Поиск персонала по критериям</a>
            </div>
        <%}%>

    </div>

