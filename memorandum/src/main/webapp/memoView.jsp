<%@ page contentType="text/html;  charset=windows-1251" %>
<%@ page import="org.census.commons.dto.docs.memoDTO" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="/_top.jsp"><jsp:param name="title" value="�������� ��������� �������"/></jsp:include>

<!-- ���������: �� ������ ����� ��� ������ ����� �������� -->
<p><b style="font-size:14pt">��������� ������� � <c:out value="${memo.memoNumber}"/>
  <c:if test="${boxType == 'inbox'}">(��.)</c:if>
  <c:if test="${boxType == 'outbox'}">(���.)</c:if>          
</b></p>

<table width="98%" cellspacing="3" style="margin-left:3px" >

        <tr>
            <!-- ������ �������������� ��������. ���������� ������ ���� �������� ������������ - ���� ��� ��� �� ����������. -->
            <!-- ��� ��, ���� ��� �������� ������������ �������� �������� ��������� -->
            <c:if test="${(memo.sendDate == null) && (member.departmentId == memo.executorDeptID)}">
                <td>
                    <form name="editor" action="editor/controller">
                        <input type=hidden name=action value='editMemo'>
                        <input type=hidden name=memoID value="<c:out value="${memo.id}"/>">
                        <input type="submit" value='�������������' class=buttonSellStyle>
                    </form>
                </td>
            </c:if>

            <!-- ������ ��� ������ �� ��������(�������� ����� �������� � ����� �� ������). -->
            <!-- ��������� ������, ���� ��� ������������ �������� �������� ��������, � ��� �� ��� ������������� ������ � ������ �����������-->
            <!-- ����� ��� ������ �� ����� INBOX -->
            <c:if test="${(boxType == 'inbox')}">
                <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>
                    <c:if test="${recipientDept.recipientDeptID == member.departmentId}">
                        <td>
                            <form name="answer" action="editor/controller">
                                <input type=hidden name=action value='addStep1'>
                                <input type=hidden name=parentID value="<c:out value="${memo.id}"/>">
                                <input type="submit" value='��������' class=buttonSellStyle>
                            </form>
                        </td>
                    </c:if>
                </c:forEach>
            </c:if>

            <!-- ������ ��� ��������� �������� ����-���� -->
            <!-- ��������� ������, ���� ��� ������������ �������� �������� ��������, � ��� �� ��� ������������� ������ � ������ �����������-->
            <!-- ����� ��� ������ �� ����� INBOX � � ������������ ���� ����� �������� ��-->
            <% if (request.isUserInRole("memo_appointment")) {%>
            <c:if test="${(boxType == 'inbox')}">
                <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>
                    <c:if test="${recipientDept.recipientDeptID == member.departmentId}">
                        <td>
                            <form name="appoint" action="appointment/controller">
                                <input type=hidden name=action value='appoint'>
                                <input type=hidden name=memoID value="<c:out value="${memo.id}"/>">
                                <input type="submit" value='��������' class=buttonSellStyle>
                            </form>
                        </td>
                    </c:if>
                </c:forEach>
            </c:if>
            <%}%>

            <!-- ������ ��� ��������� �������� ����-���� -->
            <!-- ��������� ������, ���� ��� ������������ �������� �������� ��������, � ��� �� ��� ������������� ������ � ������ �����������-->
            <!-- ����� ��� ������ �� ����� INBOX � � ������������ ���� ����� ���������� �������������-->
            <% if (request.isUserInRole("memo_chief")) {%>
            <c:if test="${(boxType == 'inbox')}">
                <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>
                    <c:if test="${recipientDept.recipientDeptID == member.departmentId}">
                        <td>
                            <form name="iforward" action="editor/controller">
                                <input type=hidden name=action value='addStep1'>
                                <input type=hidden name=memoID value="<c:out value="${memo.id}"/>">
                                <input type=hidden name=forward value="forward">
                                <input type="submit" value='���������' class=buttonSellStyle>
                            </form>
                        </td>
                    </c:if>
                </c:forEach>
            </c:if>
            <%}%>

            <!-- ������ ��� �������� �� �������� ����� -->
            <td>
                <form name="printMemo" action="controller">
                    <input type=hidden name=action value='printMemo'>
                    <input type=hidden name=memoID value="<c:out value="${memo.id}"/>">
                    <input type="submit" value='������' class=buttonSellStyle>
                </form>
            </td>

            <!-- ������ ��� �������� �������� ����������. ��� ������ ����������� �� ��. �����, � � ���� SendDate
            �������� �������� ���� �������� (������� ���� �� �������).  -->
            <!-- ��������� ������, ���� ��� ������������ �������� �������� ��������� -->
            <% if (request.isUserInRole("memo_sender")) {%>
            <c:if test="${(member.departmentId == memo.executorDeptID)}">
                <td>
                    <form name="isend" action="sender/controller">
                        <input type=hidden name=action value='send'>
                        <input type=hidden name=memoID value="<c:out value="${memo.id}"/>">
                        <input type="submit" value='���������' class=buttonSellStyle>
                    </form>
                </td>
            </c:if>
            <%}%>

            <!-- ������ �������� � ���� ������, �������� ������� �� ������������� boxType == 'outbox'-->
            <td>
                <form name="deptBox" action="controller">
                    <%! String nameButton2;%>
                    <c:if test="${(boxType) == 'inbox'}"><% nameButton2 = "������� �� �������� ";%></c:if>
                    <c:if test="${(boxType) == 'outbox'}"><% nameButton2 = "������� � ��������� ";%></c:if>

                    <input type=hidden name=action value='deptBox'>
                    <input type=hidden name=boxType value=<c:out value="${boxType}"/>>
                    <input type=hidden name=deptID value="<c:out value="${member.departmentId}"/>">

                    <input type="submit" value='<%=nameButton2%> [<c:out value="${member.departmentCode}"/>]' class=buttonSellStyle>
                </form>
            </td>           
        </tr>
</table>

<br><br>

<!-- ������� � ����� ��������� ��� ��������� -->
<table cellspacing="3" width="99%">
 <!-- ���� ������ �������� �������� ������� �� ������ - ������� �� ��� ������ -->
 <c:if test="${parentMemoNumber != null}">
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>����� �� �� � </b></td>
  <td bgcolor="#f0f0f0">
   <a href="controller?action=viewMemo&memoID=<c:out value="${memo.parentID}"/>&boxType=outbox">
    <font color=blue><b>[<c:out value="${parentMemoNumber}" />]</b></font>
   </a>
  </td>
 </tr>
 </c:if>

 <!-- �����, � ������� �������� ���������, ������������� �������� -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>������������� �����������</b></td>
  <td bgcolor="#f0f0f0"><c:out value="${memo.executorDeptCode}"/></td>
 </tr>

 <!-- ���������, ������������� �������� -->
  <tr align="left">
   <td bgcolor="#c7d0d9" width="20%"><b>����������(�)</b></td>
   <td bgcolor="#f0f0f0"><c:out value="${memo.executorShortName}"/></td>
  </tr>

 <!-- ���� �������� �������� -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>���� ��������</b></td>
  <td bgcolor="#f0f0f0"><c:out value="${memo.timeStamp}"/></td>
 </tr>

 <!-- ���� �������� �������� -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>���� ��������</b></td>
  <td bgcolor="#f0f0f0">
   <c:choose>
    <c:when test="${memo.sendDate != null}">
        <c:out value="${memo.sendDate}"/>
        <c:if test="${memo.sendShortName != null}"> [<c:out value="${memo.sendShortName}"/>]</c:if>
    </c:when>
    <c:otherwise><b><font color='red'>* ��������� ������� �� ���������� *</font></b></c:otherwise>
   </c:choose>
  </td>
 </tr>

 <!-- ���� ������ �� �������� -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>���� ������</b></td>
  <td bgcolor="#f0f0f0">
   <c:choose>
    <c:when test="${memo.realizedDate != null}">
        <!-- ���� ����� ��� � ������� ����, � ������� ����������� ��� ������ ��� ���������, -->
        <!-- �� � ������ ������������ ���������� ����������� ������� ���� "���� ������" -->
        <!--editor/controller?action=deleteRealizedDate&memoID= -->
        <% if (request.isUserInRole("memo_chief")) {%>
            <c:if test="${member.departmentId == memo.executorDeptID}">
               <a href=# onclick='if(confirm("������� ���� ���� ������")==1){window.location="editor/controller?action=deleteRealizedDate&memoID=<c:out value="${memo.id}"/>";}'><B><c:out value="${memo.realizedDate}"/></B></a>
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

 <!-- ���� �������� -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>����������</b></td>
  <td bgcolor="#f0f0f0"><c:out value="${memo.subject}"/></td>
 </tr>

  <!-- ������ �������-����������� �������� -->
  <tr align="left">
   <td bgcolor="#c7d0d9" width="20%"><b>����������</b></td>
   <td bgcolor="#f0f0f0">
    <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>
     <c:out value="${recipientDept.recipientDeptCode}"/>
    </c:forEach>
   </td>
  </tr>

  <!-- ������ �������-����������� �������� ������� �������� �� ���, � ������ �� ��������-->
  <tr align="left">
   <td bgcolor="#c7d0d9" width="20%"><b>��������</b></td>
   <td bgcolor="#f0f0f0">
     <!-- ���� ������� ������� � ����������, �� ����� ������ �������������,
             � ������ �� ��������, ������� �������� ������� �� ������ ��������-->
     <c:choose>
      <c:when test="${memo.memoChild != null && memo.recipientsDepts != null}">
        <c:forEach items='${memo.recipientsDepts}' var='recipientDept'>             
             <c:if test="${recipientDept.realized == 1}">
                 <c:forEach items='${memo.memoChild}' var='memoChild'>
                     <c:if test="${recipientDept.recipientDeptCode == memoChild.executorDeptCode}">
                          <a title="�� <c:out value="${memoChild.memoNumber}"/>" href="controller?action=viewMemo&memoID=<c:out value="${memoChild.id}"/>"><c:out value="${memoChild.executorDeptCode}"/></a>
                     </c:if>
                 </c:forEach>
             </c:if>
        </c:forEach>
      </c:when>
      <c:otherwise>-</c:otherwise>
     </c:choose>
   </td>
  </tr>

  <!-- ���������� �������� -->
  <tr align="left">
   <td bgcolor="#c7d0d9" width="20%"><b>����������</b></td>
   <td bgcolor="#f0f0f0"><%memoDTO memo = (memoDTO) request.getAttribute("memo");%><%= memo.getText()%></td>
  </tr>

  <!-- ����������� � �������� ���� -->
  <tr align="left">
    <td bgcolor="#c7d0d9" width="20%"><b>����������</b></td>
    <td bgcolor="#f0f0f0">
     <c:choose>
      <c:when test="${memo.attachedFile != null}">
       <a href="downloadAttach?memoID=<c:out value="${memo.id}"/>">
        <b>������������� ����: [<c:out value="${memo.id}"/>.<c:out value="${memo.attachedFile}"/>]</b>
       </a>
      </c:when>
      <c:otherwise>-</c:otherwise>
     </c:choose>
    </td>
  </tr>

  <!-- ������ �����������, ������� �������� ������ ��������. ����� ����������� ����������� � ���� ������. -->
 <tr align="left">
  <td bgcolor="#c7d0d9" width="20%"><b>��������</b></td>
  <td bgcolor="#f0f0f0">
   <c:choose>
    <c:when test="${memo.recipientsUsers != null}">
     <c:forEach items="${memo.recipientsUsers}" var="recipient">
      <%! String bgcolor;%>
      <c:choose>
       <c:when test="${recipient.realized == 1}"><%bgcolor="81FEB9";%></c:when>
       <c:otherwise><%bgcolor="FFB8B3";%></c:otherwise>
      </c:choose>

      <!-- ��������� ���������:-->
      <!-- recipient.recipientDeptID == deptID : ��������� ������� �������������, ����� ���� "��������" -->
      <!-- recipient.recipientDeptID == member.departmentId : ��������� ������������� � �������� ��������� ���. ������������-->
      <!-- ViewType != null : ��� ���������, �� ���� ��������������, ���������� �� ������ Search-->
      <!-- boxType == 'outbox': ��� ���������, �� ���� �������������� -->
      <c:choose>
      <c:when test="${recipient.recipientDeptID == deptID || recipient.recipientDeptID == member.departmentId || ViewType != null || boxType == 'outbox'}">
      <table width=80% border=0 cellpadding=2 cellspacing=0 class=border><tr><td bgcolor="<%=bgcolor%>">
          <table>
           <tr>
            <td>
             <b>���������:</b> <c:out value="${recipient.recipientShortName}"/>
             <b>���� ����������:</b> <c:out value="${recipient.realizedDate}"/>
            </td>
           </tr>
           <tr><td><b>�������:</b> <c:out value="${recipient.appointedUserName}"/></td></tr>
           <tr><td><b>����� ���������:</b> <c:out value="${recipient.subject}"/></td></tr>

           <!-- ���� ��������� ��������� - ������� ����� ������ -->
           <c:if test="${recipient.realized == 1}"><tr><td><b>����� ������:</b> <c:out value="${recipient.answer}"/></td></tr></c:if>

           <tr>
            <td>
             <b>������ ����������:</b>
             <c:choose>
              <c:when test="${recipient.realized == 1}">���������</c:when>
              <c:otherwise>�� ���������</c:otherwise>
             </c:choose>
            </td>
           </tr>

           <!-- ������ ��� ������ �� ���������� �������� -->
           <tr><td>
            <form action="editor/controller">
             <input type=hidden   name="action"          value=answerAppoint>
             <input type=hidden   name="memoID"          value="<c:out value="${memo.id}"/>">
             <input type=hidden   name="id" value="<c:out value="${recipient.id}"/>">
             <c:choose>
              <c:when test="${recipient.realized != 1 && recipient.recipientDeptID == member.departmentId}">
                  <input type="submit" class='buttonColor' name="saveMemo"        value='�������� �� ���������'>
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