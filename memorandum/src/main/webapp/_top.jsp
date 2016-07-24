<%@ page pageEncoding="windows-1251" contentType="text/html; charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<% String prefix = request.getParameter("prefix"); if (prefix == null) prefix = "";%>

<html>

<head>
 <meta name="keywords" content="">
 <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
 <meta http-equiv="Content-Language" content="ru">

    <!-- Данные скрипты и css необходимы для стиля таблицы rsdata-table-filter-->
    <!--
    <link rel="stylesheet" type="text/css" href="http://www.rs-class.org/css/reserved/standard.css" />
    <script type='text/javascript' src='http://www.rs-class.org/js/jquery/jquery.min.js'></script>
    <script type='text/javascript' src='http://www.rs-class.org/js/jquery/jquery.tablesorter.min.js'></script>
    -->
    <link rel="stylesheet" type="text/css" href="<%=prefix%>template/standard.css" />
    <script type='text/javascript' src='<%=prefix%>template/jquery.min.js'></script>
    <script type='text/javascript' src='<%=prefix%>template/jquery.tablesorter.min.js'></script>

 <link rel="stylesheet" type="text/css" href="<%=prefix%>template/images/style.css">

 <!-- Данная таблица стилей необходима для элемента управления "календарь" (JavaScript) -->
 <link rel="stylesheet" type="text/css" href="<%=prefix%>template/calendar/dateselector.css">
 <title>Some company name. 2015</title>
 <!-- Данные скрипты (2 шт.) необходимы для элемента управления "календарь" (JavaScript) -->
 <SCRIPT type="text/javascript" language="JavaScript1.2" src="<%=prefix%>template/calendar/popup_lib.js"></SCRIPT>
 <SCRIPT type="text/javascript" language="JavaScript1.2" src="<%=prefix%>template/calendar/dateselector.js"></SCRIPT>
    
    <script type="text/javascript" src="<%=prefix%>template/images/sdmenu.js"></script>

    <script type="text/javascript">
    var myMenu;
    window.onload = function() {
        myMenu = new SDMenu("my_menu"); myMenu.init();};

    </script><a href="<%=prefix%>template/images/sdmenu.js"></a>   
</head>

<BODY LEFTMARGIN='1' TOPMARGIN='1' rightmargin='1' bottommargin='1' background='<%=prefix%>template/images/bg_index.jpg'>

<!--style="border-collapse: collapse;"-->

<table border="5" width="100%" cellspacing="0" cellpadding="0" style="border-collapse: collapse;" bordercolor="#8cabcc">

    <tr>
        <td height="40" bgcolor="#2b64aa" class="TopTitle">
            <img border="0" width="100" height="40" align="middle" src="<%=prefix%>template/images/top_title_rs.gif">
        </td>
        <td bgcolor="#2b64aa" class="TopTitle">
            <center><b><%=request.getParameter("title").toUpperCase()%></b></center>
        </td>
    </tr>

    <tr>
        <td colspan="2" height="24" background="<%=prefix%>template/images/bg_upper-mid.gif" >

            <p style="margin-right: 30px;" align="right">
            <!-- Информация о пользователе -->
                <span class="title" title="users: <%=getServletConfig().getServletContext().getAttribute("usersCounter") %>">
                    [ПОЛЬЗОВАТЕЛЬ:
                    <%
                       if (request.getRemoteUser() == null) {
                           out.println("неавторизован");
                       } else {
                           out.println(request.getRemoteUser());
                       }
                    %>
                    ]
                </span>
                <nobr>
                <span class="title">
                    <!-- Вывод текущей даты в строке статуса под заголовком -->
                    <script language="JavaScript" type="text/javascript">
                        document.write('[' + new Date().toLocaleDateString() + "]");
                    </script>
                </span>
                </nobr>
            </p>
        </td>
    </tr>

    <tr bgcolor="#FFFFFF">
        <td valign="top" >
            <jsp:include page="/template/menu.jsp"><jsp:param name="prefix" value="<%=prefix%>"/></jsp:include>
        </td>
        <td valign="top" width="100%" bgcolor="#f8fafc">
            <br>
            <!-- скрипт для сортировки по столбцам таблицы для стиля rsdata-table-filter-->
            <script type='text/javascript'>
            $(document).ready(function()     {
            $('#myTable1').tablesorter( {sortList: [[0,0], [1,0]]} );
            } );
            </script>

