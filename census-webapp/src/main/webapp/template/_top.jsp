<%--<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:set var="user" value="<%=request.getRemoteUser()%>"/>

<html>

 <head>
  <%-- Tags cloud. --%>
  <meta name="keywords" content="">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <%-- System main style --%>
  <link rel="stylesheet" type="text/css" href="/template/css/census.css">
  <title>Census (C).</title>
 </head>

 <body LEFTMARGIN="1" TOPMARGIN="1" rightmargin="1" bottommargin="1">

  <%-- Table with left and right paddings --%>
  <table width="100%" align="center">
   <tr>
    <%-- Left padding --%>
    <td width="3%">&nbsp;</td>
    <%-- Content cell --%>
    <td width="*">


     <%-- Table with content --%>
     <table border="1" width="100%" cellspacing="0" cellpadding="0" style="border-collapse: collapse;" bordercolor="#8cabcc">
      <tr>
       <td bgcolor="#2b64aa" class="TopTitle">
        <center><b><%=request.getParameter("title")%></b></center>
       </td>
      </tr>
      <%-- Status line on the page top --%>
      <tr>
       <td height="24" background="/template/images/bg_top_status_bar.gif" >
       <%-- Table with status on the top of the page. --%>
       <table width=100%>
        <tr>
         <td align="left">
          <c:choose>
           <c:when test="${user != null}">
            <p class="title" style="margin-left: 30px;"><a href="/auth/logout.cns">Выйти из системы</a></p>
           </c:when>
           <c:otherwise>&nbsp;</c:otherwise>
          </c:choose>
         </td>
         <td align="right">
          <p class="title" style="margin-right: 30px;">
           <c:if test="${user != null}">[Пользователь: ${user}]</c:if>
           <!-- Current date (on client) on the top -->
           <script language="JavaScript" type="text/javascript">
            document.write('[' + new Date().toLocaleDateString() + "]");
           </script>
          </p>
         </td>
        </tr>
       </table>

       </td>
      </tr>

      <%-- Page content --%>
      <tr bgcolor="#FFFFFF">
       <td valign="top" width="100%" bgcolor="#f8fafc">