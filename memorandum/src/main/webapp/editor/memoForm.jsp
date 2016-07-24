<%@ page pageEncoding="windows-1251"  contentType="text/html; charset=windows-1251" %>
<%@ page import="org.census.commons.dto.docs.memoDTO" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<!-- Отрабатывает скрипт отсчета таймера -->
<body onLoad="DownRepeat()">
<!-- Формирование заголовка страницы в зависимости от типа действия -->
    <%! String title;%>
<c:choose>
    <c:when test="${memo.id > 0}"><% title = "РЕДАКТИРОВАНИЕ";%>
    </c:when>
    <c:otherwise><% title = "СОЗДАНИЕ";%>
    </c:otherwise>
</c:choose>

<jsp:include page="/_top.jsp">
    <jsp:param name="prefix" value="../"/>
    <jsp:param name="title" value='<%=title + " СЛУЖЕБНОЙ ЗАПИСКИ"%>'/>
</jsp:include>
<center>

    <p align=left><b style="font-size:14pt">
        <%=title + " СЛУЖЕБНОЙ ЗАПИСКИ"%><c:if test="${memo.id > 0}"> № <c:out value="${memo.memoNumber}"/></c:if>
    </b></p>

    <!-- Кнопка сохранения данных из заполенной формы. -->
    <%! String save_buttonValue
            ,
            exit_buttonValue;%>
    <c:choose>
        <c:when test="${memo.id > 0}">
            <%
                save_buttonValue = "СОХРАНИТЬ ИЗМЕНЕНИЯ И ВЫЙТИ";
                exit_buttonValue = "НЕ СОХРАНЯТЬ ИЗМЕНЕНИЯ И ВЫЙТИ";
            %>
        </c:when>
        <c:otherwise>
            <%
                save_buttonValue = "ЗАРЕГИСТРИРОВАТЬ И ВЫЙТИ";
                exit_buttonValue = "НЕ РЕГИСТРИРОВАТЬ И ВЫЙТИ";
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

            <!-- Непосредственно форма редактирования/добавления служебной записки -->
            <form name=calendar action='../editor/controller?action=saveMemo&memoID=<c:out value="${memo.id}" />'
                  method='POST' enctype="multipart/form-data" accept-charset="windows-1251">

                <td align=right>
                    <input type="submit" value='<%=save_buttonValue%>' class=buttonStyle>                    
                </td>
        </tr>
    </table>
    <br>
    <!-- Скрытые поля - список отделов-получателей служебки -->
    <c:forEach items="${memo.recipientsDepts}" var="recipientDept">
        <input type='hidden'  name='recipient' value="<c:out value="${recipientDept.recipientDeptID}"/>">
    </c:forEach>
    <!-- Скрытое поле - идентификатор редактируемой служебки (у новой он = -1) -->
    <input type=hidden name=memoID value="<c:out value="${memo.id}" />">
    <!-- Скрытое поле - идентификатор родительской служебки -->
    <input type=hidden name=parentID value="<c:out value="${memo.parentID}" />">

    <!-- Табличка с рамкой вокруг элементов ввода данных -->
    <table width=99% border=0 cellpadding=2 cellspacing=0 class=border>
        <tr>
            <td>

                <!-- Табличка с элементами для ввода данных в форму -->
                <table cellspacing="2" cellpadding=3 width="100%">

                    <!-- Номер служебки, ответом на которую является данная -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>ОТВЕТ НА СЗ № </b></td>
                        <td bgcolor="#f0f0f0">
                            <c:choose>
                                <c:when test="${memo.parentID > 0}"><c:out value="${parentMemo.memoNumber}"/></c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                    <!-- Сотрудник, подготовивший служебку -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>ПОДГОТОВИЛ(А)</b></td>
                        <td bgcolor="#f0f0f0"><c:out value="${memo.executorShortName}"/></td>
                    </tr>

                    <!-- Отдел, в котором числится сотрудник, подготовивший служебку -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>ПОДРАЗДЕЛЕНИЕ ОТПРАВИТЕЛЯ</b></td>
                        <td bgcolor="#f0f0f0"><c:out value="${memo.executorDeptCode}"/></td>
                    </tr>

                    <!-- Тема служебки -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>КАСАТЕЛЬНО</b></td>
                        <td bgcolor="#f0f0f0">
                            <!-- Если мы отвечаем на другую служебку - выведем ее тему. -->
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

                    <!-- Список отделов-получателей служебки -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%">
                            <b>ПОЛУЧАТЕЛИ</b>
                            <!-- Если мы находимся в режиме редактирования, то отобразим ссылочку для изменения списка получателей -->
                            <c:if test="${memo.id > 0}">
                                <br><a href="../editor/controller?action=addStep1&memoID=<c:out value="${memo.id}"/>">
                                <b><font color=blue>[ИЗМЕНИТЬ]</font></b></a>
                            </c:if>
                        </td>
                        <td bgcolor="#f0f0f0">
                            <c:forEach items="${memo.recipientsDepts}" var="recipientDept"><c:out
                                    value="${recipientDept.recipientDeptCode}"/> </c:forEach><br>
                        </td>
                    </tr>

                    <!-- Если мы в режиме редактирования - отобразим предупреждение -->
                    <c:if test="${memo.id > 0}">
                        <tr align=center bgcolor="#c7d0d9">
                            <td colspan=2><i><b>ВНИМАНИЕ! При изменении списка получателей текущий список теряется!</b></i>
                            </td>
                        </tr>
                    </c:if>

                    <!-- Срок ответа на данную служебку -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>СРОК ОТВЕТА</b></td>
                        <td bgcolor="#f0f0f0">
                            <INPUT name=realizedDate readonly value="<c:out value="${memo.realizedDate}"/>">
                            <IMG onclick="popUpCalendar(this, calendar.realizedDate, 'dd/mm/yyyy');"
                                 height=20 hspace=3 src="../template/calendar/date_selector.gif" width=20 border=0
                                 align="bottom" alt=calendar>
                        </td>
                    </tr>

                    <!-- Содержание служебки -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>СОДЕРЖАНИЕ</b></td>
                        <td bgcolor="#f0f0f0">
                            <!--
                              1)Если ((memo.id > 0) && (memo.parentID > 0)) - мы редактируем служебку, у которой есть родительская - выводим текст
                                 самой редактируемой служебки
                              2)Если ((memo.id > 0) && (memo.parentID < 0)) - мы редактируем служебку, у которой нет родительской - выводим текст
                                 самой редактируемой служебки
                              3)Если ((memo.id < 0) && (memo.parentID > 0)) - мы отвечаем на другую служебку (создаем новую в ответ) - выводим текст
                                 служебки, на которую отвечаем
                              4)Если ((memo.id < 0) && (memo.parentID < 0)) - мы просто создаем новую служебку - ничего не выводим, но можно вставить
                                 тег вывода текста самой служебки - он просто пуст!
                              5)При ответе или создании новой служебки, если отдел-получатель один, то выводим: Уважаемый/Уважаемая Имя Отчество нач-ка

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

                    <!-- Приложенный к служебке файл -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>ПРИЛОЖЕНИЯ</b></td>
                        <td bgcolor="#f0f0f0">
                            
                            <c:if test="${(memo.id > 0) && (memo.attachedFile != null)}">
                                <input type="hidden" name="fileExt" value="<c:out value="${memo.attachedFile}"/>">
                            </c:if>

                            <input type="file" name="file" size=90 class=input><br>
                            <!-- Если мы редактируем служебку у которой уже есть приложение, сообщим об этом -->
                            <c:if test="${(memo.id > 0) && (memo.attachedFile != null)}">
                                <i>
                                    у данной служебной записки уже есть прикрепленный файл
                                    <a href="../downloadAttach?memoID=<c:out value="${memo.id}"/>">
                                        <b>[<c:out value="${memo.id}"/>.<c:out value="${memo.attachedFile}"/>]</b>
                                    </a><br>
                                    <b>ВНИМАНИЕ! Прикрепление еще одного файла удалит предыдущий!</b>
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