<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<!-- Отрабатывает скрипт отсчета таймера -->
<body onLoad="DownRepeat()">
<!-- Формирование заголовка страницы-->
<jsp:include page="/_top.jsp">
 <jsp:param name="prefix" value="../"/>
 <jsp:param name="title" value="КОММЕНТАРИЙ К СОПРОВОДИТЕЛЬНОМУ ПИСЬМУ"/>
</jsp:include>
<center>

 <p align=left><b style="font-size:14pt">
    КОММЕНТАРИЙ К СОПРОВОДИТЕЛЬНОМУ ПИСЬМУ<br>ПРИ ПЕРЕАДРЕСАЦИИ СЛУЖЕБНОЙ ЗАПИСКИ № <c:out value="${memo.memoNumber}"/>
 </b></p>

 <table width="99%" border=0>
  <tr>
   <td align=left>
    <form name="depts"method="POST" action='../controller?action=viewMemo&memoID=<c:out value="${memo.id}"/>&boxType=inbox'>
        <input type="submit" value='НЕ РЕГИСТРИРОВАТЬ И ВЫЙТИ' class=buttonStyle>
    </form>
   </td>

      <!-- Непосредственно форма для формирования ответа при переадресации служебной записки -->
      <form name=calendar action='../editor/controller?action=saveForwardMemo' method='POST'>

          <td align=right>
              <input type="submit" value='ОТПРАВИТЬ ПИСЬМО АДРЕСАТАМ' class=buttonStyle>
          </td>
  </tr>
 </table>
    <br>
    <!-- Скрытые поля - список отделов-получателей служебки -->
    <c:forEach items="${memo.recipientsDepts}" var="recipientDept">
        <input type='hidden' name='recipient' value="<c:out value="${recipientDept.recipientDeptID}"/>">
    </c:forEach>
    <!-- Скрытое поле - идентификатор переадресуемой служебки -->
    <input type=hidden name=memoID value="<c:out value="${memo.id}" />">
    <!-- Табличка с рамкой вокруг элементов ввода данных -->
    <table width=99% border=0 cellpadding=2 cellspacing=0 class=border>
        <tr>
            <td>

                <!-- Табличка с элементами для ввода данных в форму -->
                <table cellspacing="2" cellpadding=3 width="100%">

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

                    <!-- Список отделов-получателей пересылаемой служебки служебки -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%">
                            <b>ПОЛУЧАТЕЛИ ПЕРЕСЫЛАЕМОЙ СЛУЖЕБКИ</b>
                        </td>
                        <td bgcolor="#f0f0f0">                            
                            <c:forEach items="${memo.recipientsDepts}" var="recipientDept"><c:out
                                    value="${recipientDept.recipientDeptCode}"/> </c:forEach><br>
                        </td>
                    </tr>

                    <!-- Содержание служебки -->
                    <tr align="left">
                        <td bgcolor="#c7d0d9" width="20%"><b>ПРИМЕЧАНИЕ</b></td>
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