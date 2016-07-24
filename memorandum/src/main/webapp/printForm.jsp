<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ page import="org.census.commons.dto.docs.memoDTO"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<c:choose>
 <c:when test="${memo == null}"><font color=red>[СЛУЖЕБКА НЕ СУЩЕСТВУЕТ!]</font></c:when>
 <c:otherwise>
  <font face="Arial">

   <table width="100%" style="font-size:11pt">
    <tr>
       <td width="75%"></td>
       <td><b><c:out value="${boss}"/></b>
           
        <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>
        <c:out value="${recipientDept.recipientDeptCode}"/>
        </c:forEach>
      </td>
    </tr>
   </table>

   <br><br><br><br><br>

   <table width=99%>
      <tr>
          <td width=8%></td>
          <td align="center"><b>Служебная записка № </b><c:out value="${memo.memoNumber}"/><b> от </b><c:out value="${memo.timeStamp}"/><br><br></td>
      </tr>
      <tr>
          <td></td>
          <td align="left"><b>Касательно:</b>&nbsp;<c:out value="${memo.subject}"/><br><br></td>
      </tr>
      <tr>
          <td></td>
          <td align="left"><%memoDTO memo = (memoDTO) request.getAttribute("memo");%><%= memo.getText()%><br><br></td>
      </tr>
      <tr>
       <c:choose>
        <c:when test="${memo.attachedFile != null}">
          <td></td>
          <td>Приложения: <br> <c:out value="${memo.id}"/>.<c:out value="${memo.attachedFile}"/><br><br></td>
        </c:when>
      </c:choose>
      </tr>



      <tr>
          <td></td>
          <td>
              <% if(memo.getText() != null && memo.getText().indexOf("С уважением,") < 0){%>

              <table width='50%'>
                  <tr>
                      <td>С уважением,</td>
                  </tr>
                  <c:if test="${memo.chiefdeptexecutor != null}">
                      <tr>
                          <td><c:out value="${memo.chiefdeptexecutor.post}"/></td>
                      </tr>
                      <tr>
                          <td><c:out value="${memo.chiefdeptexecutor.shortName}"/></td>
                      </tr>
                  </c:if>
              </table>
              <%} %>
                                   
          </td>
      </tr>
      <tr>
          <td></td>
          <td align="left"><font size=-2>Подготовил(а): <c:out value="${memo.executorShortName}"/>&nbsp;<c:out value="${memo.executorDeptCode}"/></font></td>
      </tr>
   </table>
 </c:otherwise>
</c:choose>