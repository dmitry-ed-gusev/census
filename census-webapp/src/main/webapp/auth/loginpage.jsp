<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="../template/_top.jsp"><jsp:param name="title" value="Вас приветствует система Census!"/></jsp:include>

<p align="center">

<table cellpadding="10" width="40%">
 <form action="j_spring_security_check" method="post" >
 <tr align="center"><td colspan="2"><h1>Пожалуйста, представьтесь!</h1></td></tr>
 <tr>
  <td><label for="j_username">Имя пользователя</label></td>
  <td><input id="j_username" name="j_username" type="text" /></td>
 </tr>
 <tr>
  <td><label for="j_password">Пароль</label></td>
  <td><input id="j_password" name="j_password" type="password" /></td>
 </tr>
 <tr align="center"><td colspan="2" align="center"><input  type="submit" value="Войти в систему"/></td></tr>

 <%-- If there is auth error, we will out message. --%>
 <c:if test="${not empty error}">
  <tr><td colspan="2" style="border-top: 1 solid #006699"><font color="red">${error}</font></td></tr>
 </c:if>

 </form>
</table>

<jsp:include page="../template/_bottom.jsp"/>