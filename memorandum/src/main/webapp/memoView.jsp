<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ page import="org.census.commons.dto.docs.memoDTO" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="/_top.jsp"><jsp:param name="title" value="ПРОСМОТР СЛУЖЕБНОЙ ЗАПИСКИ"/></jsp:include>

<!-- Проверяем: из какого ящика был сделан вызов служебки -->
<p><b style="font-size:14pt">СЛУЖЕБНАЯ ЗАПИСКА № <c:out value="${memo.memoNumber}"/>
  <c:if test="${boxType == 'inbox'}">(Вх.)</c:if>
  <c:if test="${boxType == 'outbox'}">(Исх.)</c:if>          
</b></p>

<table width="98%" cellspacing="3" style="margin-left:3px" >

        <tr>
            <!-- Кнопка редактирования служебки. Появляется только если служебка редактируема - если она еще не отправлена. -->
            <!-- Так же, если для текущего пользователя служебка является исходящей -->
            <c:if test="${(memo.sendDate == null) && (member.departmentId == memo.executorDeptID)}">
                <td>
                    <form name="editor" action="editor/controller">
                        <input type=hidden name=action value='editMemo'>
                        <input type=hidden name=memoID value="<c:out value="${memo.id}"/>">
                        <input type="submit" value='РЕДАКТИРОВАТЬ' class=buttonSellStyle>
                    </form>
                </td>
            </c:if>

            <!-- Кнопка для ответа на служебку(создание новой служебки в ответ на данную). -->
            <!-- Появлятся только, если для пользователя служебка является входящей, а так же его подразделение входит в список получателей-->
            <!-- Вызов был сделан из папки INBOX -->
            <c:if test="${(boxType == 'inbox')}">
                <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>
                    <c:if test="${recipientDept.recipientDeptID == member.departmentId}">
                        <td>
                            <form name="answer" action="editor/controller">
                                <input type=hidden name=action value='addStep1'>
                                <input type=hidden name=parentID value="<c:out value="${memo.id}"/>">
                                <input type="submit" value='ОТВЕТИТЬ' class=buttonSellStyle>
                            </form>
                        </td>
                    </c:if>
                </c:forEach>
            </c:if>

            <!-- Кнопка для поручения служебки кому-либо -->
            <!-- Появлятся только, если для пользователя служебка является входящей, а так же его подразделение входит в список получателей-->
            <!-- Вызов был сделан из папки INBOX и у пользователя есть право поручать СЗ-->
            <% if (request.isUserInRole("memo_appointment")) {%>
            <c:if test="${(boxType == 'inbox')}">
                <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>
                    <c:if test="${recipientDept.recipientDeptID == member.departmentId}">
                        <td>
                            <form name="appoint" action="appointment/controller">
                                <input type=hidden name=action value='appoint'>
                                <input type=hidden name=memoID value="<c:out value="${memo.id}"/>">
                                <input type="submit" value='ПОРУЧИТЬ' class=buttonSellStyle>
                            </form>
                        </td>
                    </c:if>
                </c:forEach>
            </c:if>
            <%}%>

            <!-- Кнопка для пересылке служебки кому-либо -->
            <!-- Появлятся только, если для пользователя служебка является входящей, а так же его подразделение входит в список получателей-->
            <!-- Вызов был сделан из папки INBOX и у пользователя есть права начальника подразделения-->
            <% if (request.isUserInRole("memo_chief")) {%>
            <c:if test="${(boxType == 'inbox')}">
                <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>
                    <c:if test="${recipientDept.recipientDeptID == member.departmentId}">
                        <td>
                            <form name="iforward" action="editor/controller">
                                <input type=hidden name=action value='addStep1'>
                                <input type=hidden name=memoID value="<c:out value="${memo.id}"/>">
                                <input type=hidden name=forward value="forward">
                                <input type="submit" value='ПЕРЕСЛАТЬ' class=buttonSellStyle>
                            </form>
                        </td>
                    </c:if>
                </c:forEach>
            </c:if>
            <%}%>

            <!-- Кнопка для перехода на печатную форму -->
            <td>
                <form name="printMemo" action="controller">
                    <input type=hidden name=action value='printMemo'>
                    <input type=hidden name=memoID value="<c:out value="${memo.id}"/>">
                    <input type="submit" value='ПЕЧАТЬ' class=buttonSellStyle>
                </form>
            </td>

            <!-- Кнопка для отправки служебки получателю. Ему придет уведомление по эл. почте, а в поле SendDate
            служебки появится дата отправки (текущая дата на сервере).  -->
            <!-- Появлятся только, если для пользователя служебка является исходящей -->
            <% if (request.isUserInRole("memo_sender")) {%>
            <c:if test="${(member.departmentId == memo.executorDeptID)}">
                <td>
                    <form name="isend" action="sender/controller">
                        <input type=hidden name=action value='send'>
                        <input type=hidden name=memoID value="<c:out value="${memo.id}"/>">
                        <input type="submit" value='ОТПРАВИТЬ' class=buttonSellStyle>
                    </form>
                </td>
            </c:if>
            <%}%>

            <!-- Кнопка перехода в ящик отдела, служебку которго мы просматриваем boxType == 'outbox'-->
            <td>
                <form name="deptBox" action="controller">
                    <%! String nameButton2;%>
                    <c:if test="${(boxType) == 'inbox'}"><% nameButton2 = "ПЕРЕЙТИ ВО ВХОДЯЩИЕ ";%></c:if>
                    <c:if test="${(boxType) == 'outbox'}"><% nameButton2 = "ПЕРЕЙТИ В ИСХОДЯЩИЕ ";%></c:if>

                    <input type=hidden name=action value='deptBox'>
                    <input type=hidden name=boxType value=<c:out value="${boxType}"/>>
                    <input type=hidden name=deptID value="<c:out value="${member.departmentId}"/>">

                    <input type="submit" value='<%=nameButton2%> [<c:out value="${member.departmentCode}"/>]' class=buttonSellStyle>
                </form>
            </td>           
        </tr>
</table>

<br><br>

<!-- Таблица с одной служебкой для просмотра -->
<table cellspacing="3" width="99%">
 <!-- Если данная служебка является ответом на другую - выводим на нее ссылку -->
 <c:if test="${parentMemoNumber != null}">
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>ОТВЕТ НА СЗ № </b></td>
  <td bgcolor="#f0f0f0">
   <a href="controller?action=viewMemo&memoID=<c:out value="${memo.parentID}"/>&boxType=outbox">
    <font color=blue><b>[<c:out value="${parentMemoNumber}" />]</b></font>
   </a>
  </td>
 </tr>
 </c:if>

 <!-- Отдел, в котором числится сотрудник, подготовивший служебку -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>ПОДРАЗДЕЛЕНИЕ ОТПРАВИТЕЛЯ</b></td>
  <td bgcolor="#f0f0f0"><c:out value="${memo.executorDeptCode}"/></td>
 </tr>

 <!-- Сотрудник, подготовивший служебку -->
  <tr align="left">
   <td bgcolor="#c7d0d9" width="20%"><b>ПОДГОТОВИЛ(А)</b></td>
   <td bgcolor="#f0f0f0"><c:out value="${memo.executorShortName}"/></td>
  </tr>

 <!-- Дата создания служебки -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>ДАТА СОЗДАНИЯ</b></td>
  <td bgcolor="#f0f0f0"><c:out value="${memo.timeStamp}"/></td>
 </tr>

 <!-- Дата отправки служебки -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>ДАТА ОТПРАВКИ</b></td>
  <td bgcolor="#f0f0f0">
   <c:choose>
    <c:when test="${memo.sendDate != null}">
        <c:out value="${memo.sendDate}"/>
        <c:if test="${memo.sendShortName != null}"> [<c:out value="${memo.sendShortName}"/>]</c:if>
    </c:when>
    <c:otherwise><b><font color='red'>* СЛУЖЕБНАЯ ЗАПИСКА НЕ ОТПРАВЛЕНА *</font></b></c:otherwise>
   </c:choose>
  </td>
 </tr>

 <!-- Срок ответа на служебку -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>СРОК ОТВЕТА</b></td>
  <td bgcolor="#f0f0f0">
   <c:choose>
    <c:when test="${memo.realizedDate != null}">
        <!-- если зашел чел с правами шефа, и записка принадлежит его отделу как исходящая, -->
        <!-- то у такого пользователя появляется возможность удалить дату "Срок ответа" -->
        <!--editor/controller?action=deleteRealizedDate&memoID= -->
        <% if (request.isUserInRole("memo_chief")) {%>
            <c:if test="${member.departmentId == memo.executorDeptID}">
               <a href=# onclick='if(confirm("Удалить дату СРОК ОТВЕТА")==1){window.location="editor/controller?action=deleteRealizedDate&memoID=<c:out value="${memo.id}"/>";}'><B><c:out value="${memo.realizedDate}"/></B></a>
            </c:if>
            <c:if test="${member.departmentId != memo.executorDeptID}">
               <c:out value="${memo.realizedDate}"/>               
            </c:if>
        <%} else {%><c:out value="${memo.realizedDate}"/><%}%>        
    </c:when>
    <c:otherwise>-</c:otherwise>
   </c:choose>
  </td>
 </tr>

 <!-- Тема служебки -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>КАСАТЕЛЬНО</b></td>
  <td bgcolor="#f0f0f0"><c:out value="${memo.subject}"/></td>
 </tr>

  <!-- Список отделов-получателей служебки -->
  <tr align="left">
   <td bgcolor="#c7d0d9" width="20%"><b>ПОЛУЧАТЕЛИ</b></td>
   <td bgcolor="#f0f0f0">
    <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>
     <c:out value="${recipientDept.recipientDeptCode}"/>
    </c:forEach>
   </td>
  </tr>

  <!-- Список отделов-получателей служебки которые ответили на нее, и ссылки на служебки-->
  <tr align="left">
   <td bgcolor="#c7d0d9" width="20%"><b>ОТВЕТИЛИ</b></td>
   <td bgcolor="#f0f0f0">
     <!-- если иммется отметка о выполнении, то вывод данное подразделение,
             с сылкой на служебку, которая является ответом на данную служебку-->
     <c:choose>
      <c:when test="${memo.memoChild != null && memo.recipientsDepts != null}">
        <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>             
             <c:if test="${recipientDept.realized == 1}">
                 <c:forEach items='${memo.memoChild}' var='memoChild'>
                     <c:if test="${recipientDept.recipientDeptCode == memoChild.executorDeptCode}">
                          <a title="СЗ <c:out value="${memoChild.memoNumber}"/>" href="controller?action=viewMemo&memoID=<c:out value="${memoChild.id}"/>"><c:out value="${memoChild.executorDeptCode}"/></a>
                     </c:if>
                 </c:forEach>
             </c:if>
        </c:forEach>
      </c:when>
      <c:otherwise>-</c:otherwise>
     </c:choose>
   </td>
  </tr>

  <!-- Содержание служебки -->
  <tr align="left">
   <td bgcolor="#c7d0d9" width="20%"><b>СОДЕРЖАНИЕ</b></td>
   <td bgcolor="#f0f0f0"><%memoDTO memo = (memoDTO) request.getAttribute("memo");%><%= memo.getText()%></td>
  </tr>

  <!-- Приложенный к служебке файл -->
  <tr align="left">
    <td bgcolor="#c7d0d9" width="20%"><b>ПРИЛОЖЕНИЯ</b></td>
    <td bgcolor="#f0f0f0">
     <c:choose>
      <c:when test="${memo.attachedFile != null}">
       <a href="downloadAttach?memoID=<c:out value="${memo.id}"/>">
        <b>ПРИКРЕПЛЕННЫЙ ФАЙЛ: [<c:out value="${memo.id}"/>.<c:out value="${memo.attachedFile}"/>]</b>
       </a>
      </c:when>
      <c:otherwise>-</c:otherwise>
     </c:choose>
    </td>
  </tr>

  <!-- Список сотрудников, которым поручена данная служебка. Также указываются комментарий и срок ответа. -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>ПОРУЧЕНО</b></td>
  <td bgcolor="#f0f0f0">
   <c:choose>
    <c:when test="${memo.recipientsUsers != null}">
     <c:forEach items="${memo.recipientsUsers}" var="recipient">
      <%! String bgcolor;%>
      <c:choose>
       <c:when test="${recipient.realized == 1}"><%bgcolor="81FEB9";%></c:when>
       <c:otherwise><%bgcolor="FFB8B3";%></c:otherwise>
      </c:choose>

      <!-- Видимость поручений:-->
      <!-- recipient.recipientDeptID == deptID : поручения данного подразделения, через ящик "входящие" -->
      <!-- recipient.recipientDeptID == member.departmentId : поручения подразделения к которому относится тек. пользователь-->
      <!-- ViewType != null : все поручения, во всех подразделениях, вызывается из метода Search-->
      <!-- boxType == 'outbox': все поручения, во всех подразделениях -->
      <c:choose>
      <c:when test="${recipient.recipientDeptID == deptID || recipient.recipientDeptID == member.departmentId || ViewType != null || boxType == 'outbox'}">
      <table width=80% border=0 cellpadding=2 cellspacing=0 class=border><tr><td bgcolor="<%=bgcolor%>">
          <table>
           <tr>
            <td>
             <b>Сотрудник:</b> <c:out value="${recipient.recipientShortName}"/>
             <b>Срок исполнения:</b> <c:out value="${recipient.realizedDate}"/>
            </td>
           </tr>
           <tr><td><b>Поручил:</b> <c:out value="${recipient.appointedUserName}"/></td></tr>
           <tr><td><b>Текст поручения:</b> <c:out value="${recipient.subject}"/></td></tr>

           <!-- Если поручение выполнено - выведем текст ответа -->
           <c:if test="${recipient.realized == 1}"><tr><td><b>Текст ответа:</b> <c:out value="${recipient.answer}"/></td></tr></c:if>

           <tr>
            <td>
             <b>СТАТУС ИСПОЛНЕНИЯ:</b>
             <c:choose>
              <c:when test="${recipient.realized == 1}">ВЫПОЛНЕНО</c:when>
              <c:otherwise>НЕ ВЫПОЛНЕНО</c:otherwise>
             </c:choose>
            </td>
           </tr>

           <!-- Кнопка для ответа на порученную служебку -->
           <tr><td>
            <form action="editor/controller">
             <input type=hidden   name="action"          value=answerAppoint>
             <input type=hidden   name="memoID"          value="<c:out value="${memo.id}"/>">
             <input type=hidden   name="id" value="<c:out value="${recipient.id}"/>">
             <c:choose>
              <c:when test="${recipient.realized != 1 && recipient.recipientDeptID == member.departmentId}">
                  <input type="submit" class='buttonColor' name="saveMemo"        value='ОТВЕТИТЬ НА ПОРУЧЕНИЕ'>
              </c:when>
             </c:choose>
            </form>
           </td></tr>
          </table>
      </td></tr></table><br>
      </c:when>
      </c:choose>
     </c:forEach>
    </c:when>
    <c:otherwise>-</c:otherwise>
   </c:choose>
  </td>
 </tr>

</table>
</form>
<jsp:include page="_bottom.jsp" />