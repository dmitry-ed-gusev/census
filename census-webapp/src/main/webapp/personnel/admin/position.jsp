<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>

 <head>
  <title>Positions Manager</title>
 </head>

 <body>
  <h2>Positions Manager</h2>
  <form:form method="post" action="addPosition.cns">
   <table>
    <tr>
     <td><form:label path="name">Position name</form:label></td>
     <td><form:input path="name" /></td>
    </tr>
    <tr>
     <td><form:label path="weight">Position weight</form:label></td>
     <td><form:input path="weight" /></td>
    </tr>
    <tr>
     <td><form:label path="comment">Comment</form:label></td>
     <td><form:input path="comment" /></td>
    </tr>
    <tr><td colspan="2"><input type="submit" value="Add Contact"/></td></tr>
   </table>
  </form:form>
 </body>

</html>