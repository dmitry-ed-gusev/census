<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<jsp:include page="/_top.jsp"><jsp:param name="title" value="Memorandum"/></jsp:include>
<center>   

  <p>&nbsp;</p>

    <form name="welcome" action="controller">
        <input type=hidden name=action value='deptBox'>
        <input type=hidden name=boxType value='inbox'>
        <input type=hidden name=pageNumber value=1>
        <input type="submit" class="buttonStyle" value='ВХОД В СИСТЕМУ "СЛУЖЕБНЫЕ ЗАПИСКИ"'>
    </form>

   <br><br>
   <p align="center">При некорректном отображении панели меню требуется обновить страницу нажатием F5</p>   

</center>
<jsp:include page="_bottom.jsp" />

