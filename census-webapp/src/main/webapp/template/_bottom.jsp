<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.util.Calendar" %>

       </td>
      </tr>

      <%-- Status line on the page bottom. --%>
      <tr>
       <td background="/template/images/bg_bottom_status_bar.gif" height="20">
        <span class="title" style="margin-left: 10px;">
         <b>Система Census<span lang="en"> © </span><%= new GregorianCalendar().get(Calendar.YEAR)%></b>
        </span>
       </td>
      </tr>

    </table>

   <%-- End of main table with left and right paddings. --%>
   </td>
   <%-- Right padding --%>
   <td width="3%">&nbsp;</td>
  </tr>
 </table>

 </body>

</html>