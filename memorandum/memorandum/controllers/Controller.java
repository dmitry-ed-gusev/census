package memorandum.controllers;

import jpersonnel.JPersonnelConsts;
import jpersonnel.dto.SimpleDepartmentDTO;
import memorandum.Defaults;
import memorandum.dataModel.dto.memoDTO;
import memorandum.dataModel.dto.recipientDeptDTO;
import jlib.utils.JLibUtils;
import static jlib.utils.JLibUtils.DatePeriod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * ����� ��������� �������� ���������� ���������� "��������� �������". ��������, ����������� ������ ������������
 * �������� ���������� - �������������.
*/

public class Controller extends AbstractController
{

 // ��� ��������: �������� ����� ��������
 private static final String MEMO_VIEW_ACTION      = "viewMemo";
 // ��� ��������: ����� ������ ������� ��� ��� ������ ����� ��������
 private static final String DEPTS_ACTION          = "depts";
 // ��� ��������: ����������� ����� ��������� �������
 private static final String DEPT_BOX_ACTION       = "deptBox";
 // ��� ��������: ����� �� �� ����������� ��������� ������ �������������
 private static final String DEPT_NOTMAKE_ACTION   = "notMake";
 // ��� ��������: ������� �� ����� ������ �������� �� ���������
 private static final String MEMO_SEARCH_ACTION    = "search";
 // ��� ��������: ��������������� ����� ��������
 private static final String MEMO_SEARCH_DO_ACTION = "searchDo";
 // ��� ��������: ����� ��� ������ ��������
 private static final String MEMO_PRINT_ACTION     = "printMemo";

 // ��������(�����) �����������
 private static final String CONTROLLER_PAGE          = "/controller";
 // �������� � ����������� �� ������
 private static final String ERROR_PAGE               = "/error.jsp";
 // �������� ��������� ����� ��������
 private static final String MEMO_VIEW_PAGE           = "/memoView.jsp";
 // �������� �� ������� ������� ��� ��� ������ ����� ��������
 private static final String DEPTS_PAGE               = "/depts.jsp";
 // �������� � ������ ��������� ������� ������� ������
 private static final String DEPT_BOX_PAGE            = "/deptBox.jsp";
 // �������� � ������ ������ ��������
 private static final String MEMO_SEARCH_PAGE         = "/search.jsp";
 // �������� � ������ ��� ������ ��������
 private static final String MEMO_PRINT_PAGE          = "/printForm.jsp";

 /**
  * ���������������� ��������� ������������ �������.
  * @param request HttpServletRequest http-������
  * @param response HttpServletResponse http-�����
  * @throws ServletException ������ � ������ ��������
  * @throws IOException ������ �����/������
 */
 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
   logger.debug("ENTERING controller -> processRequest().");
      
   // �� ��������� �������� � �������
   String destPage      = ERROR_PAGE;

   // �������� ��� ��������� ������������ � JSP
   request.setAttribute("member", simpleEmployee);

   // �������� �� ������� ���� ������� ���
   if(DEPTS_ACTION.equals(ACTION)){
        //���� ������������ � ������� memo_superuser, �� �� ����� ������������� �� ������ �������������
        if (request.isUserInRole("memo_superuser")){
            request.setAttribute("deptsList", dH.getConnectorSource().getSomething(dH, JPersonnelConsts.DeptType.HO, "getDepartmentsList"));
            destPage = DEPTS_PAGE;
        }
   }

   // ���� � ���� �� ���������� ������� ������ ("��������"/"���������")
   else if (DEPT_BOX_ACTION.equals(ACTION)) {

       // ���������� ��� �������� ����������� ������ ��������
       List memosList;

       // ������� ������������� ������, ��� �������� ���������� ����
       int deptID = -1;
       try {deptID = Integer.parseInt(request.getParameter("deptID"));}
       catch (Exception e) {logger.debug("Invalid deptID parameter!");}

       //�������� ������������� �������� ������ ������ ��� ����� � �������,
       //������� ��������� ����� ��. ���� ������������� ������������
       if(deptID < 0){
           deptID = simpleEmployee.getDepartmentId();
           logger.debug("deptId simpleEmployee = " + deptID);
       }

       //�������� ���������� ������ ������������� ����� ������ ������������ � ������� memo_superuser
       if (simpleEmployee.getDepartmentId() != deptID && !request.isUserInRole("memo_superuser")){
           deptID = simpleEmployee.getDepartmentId();
       }

       // ���� ������������� ������ � ������� - ��������� ��� ������
       if (deptID > 0){
           SimpleDepartmentDTO dept = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, deptID, "getSimpleDepartmentByID");
           request.setAttribute("deptCode", dept.getDepartmentCode());
       }
       // ������� ��� ����� (��������/���������). ���� ��� ����� �� ������ - ������ �� ��������� �������� ���� "���������"
       String boxType = request.getParameter("boxType");
       // ������� ����� �������� ��� �����������. ���� ����� �� ������ - ���������� ������ ��������
       int pageNumber = 1;
       try {pageNumber = Integer.parseInt(request.getParameter("pageNumber"));}
       catch (Exception e) {logger.debug("NOT pageNumber parametr");}
       
       // ���� ������������� ��� ������� - ��������� �� �������� �����,
       // ���� �� ��� - ��������� ����� � ������ �������

       // ������� ������ �����, ���� ������� ����
       String boxSubType = request.getParameter("boxSubType");

       if (deptID > 0) {
           // ����� ������� ��������
           request.setAttribute("pageNumber", pageNumber);
           // � ����������� �� ���� ����� ���������� ������ ������ ��������
           if ((boxType != null) && (boxType.trim().equals(Defaults.OUTBOX))) {
               // ��������� �������� �� ����� "���������"
               if ((boxSubType != null) && (boxSubType.trim().equals(Defaults.OUTBOX_NO_ANSWER))) {
                   logger.debug("subType for outbox: NO_ANSWER.");
                   memosList = dH.getMemoDAO().box(dH, Defaults.OUTBOX, Defaults.OUTBOX_NO_ANSWER, deptID, Defaults.STATUS_UNDELETED, pageNumber);
               } else {
                   logger.debug("subType for outbox: ALL.");
                   memosList = dH.getMemoDAO().box(dH, Defaults.OUTBOX, Defaults.OUTBOX_ALL, deptID, Defaults.STATUS_UNDELETED, pageNumber);
               }
               // ��� ����� - "���������"
               request.setAttribute("boxType", Defaults.OUTBOX);
               // ��������������� ������� �� ����� "���������"
               request.setAttribute("memosList", memosList);
               // ����� ���-�� ������� � ����� "���������"
               int outboxCount = dH.getMemoDAO().boxCount(dH, Defaults.OUTBOX, deptID, Defaults.STATUS_UNDELETED);
               logger.debug(outboxCount);
               int outboxPagesCount;
               if (outboxCount <= Defaults.PAGE_SIZE) {
                   outboxPagesCount = 1;
               } else {
                   outboxPagesCount = outboxCount / Defaults.PAGE_SIZE;
                   if (outboxCount % Defaults.PAGE_SIZE > 0) outboxPagesCount++;
               }
               request.setAttribute("outboxPagesCount", outboxPagesCount);
               logger.debug(outboxPagesCount);
               // ������ ��������� �������
               int[] pages = new int[outboxPagesCount];
               for (int i = 0; i < pages.length; i++) pages[i] = i + 1;
               request.setAttribute("pages", pages);
           } else {
               // ��� ����� - "��������"
               request.setAttribute("boxType", Defaults.INBOX);

               if ((boxSubType != null) && (boxSubType.trim().equals(Defaults.INBOX_NO_ANSWER))) {
                   logger.debug("subType for inbox: NO_ANSWER.");
                   memosList = dH.getMemoDAO().box(dH, Defaults.INBOX, Defaults.INBOX_NO_ANSWER, deptID, Defaults.STATUS_UNDELETED, pageNumber);
               } else if ((boxSubType != null) && (boxSubType.trim().equals(Defaults.INBOX_NO_ANSWER_DATE))) {
                   logger.debug("subType for inbox: NO_ANSWER_ALL.");
                   memosList = dH.getMemoDAO().box(dH, Defaults.INBOX, Defaults.INBOX_NO_ANSWER_DATE, deptID, Defaults.STATUS_UNDELETED, pageNumber);
               } else {
                   logger.debug("subType for inbox: ALL.");
                   memosList = dH.getMemoDAO().box(dH, Defaults.INBOX, Defaults.INBOX_ALL, deptID, Defaults.STATUS_UNDELETED, pageNumber);
               }
               // ��������������� ������� �� ����� "��������"
               request.setAttribute("memosList", memosList);
               // ����� ���-�� ������� � ����� "��������"
               int inboxCount = dH.getMemoDAO().boxCount(dH, Defaults.INBOX, deptID, Defaults.STATUS_UNDELETED);
               logger.debug(inboxCount);
               int inboxPagesCount;
               if (inboxCount <= Defaults.PAGE_SIZE) {
                   inboxPagesCount = 1;
               } else {
                   inboxPagesCount = inboxCount / Defaults.PAGE_SIZE;
                   if (inboxCount % Defaults.PAGE_SIZE > 0) inboxPagesCount++;
               }
               request.setAttribute("inboxPagesCount", inboxPagesCount);
               logger.debug(inboxPagesCount);
               // ������ ��������� �������
               int[] pages = new int[inboxPagesCount];
               for (int i = 0; i < pages.length; i++) pages[i] = i + 1;
               request.setAttribute("pages", pages);
           }

           // ��������� ����� ������������� ������, ���� �������� �� ����������
           request.setAttribute("deptID", deptID);
           // ��������� ������ �����
           request.setAttribute("boxSubType", boxSubType);

           destPage = DEPT_BOX_PAGE;
       } else destPage = CONTROLLER_PAGE + DEPTS_PAGE;
   }

   // �������� ����� ��������
   else if (MEMO_VIEW_ACTION.equals(ACTION)) {
       
       // ��������� �������� ViewType, ��� ���� ���� ������� ����������� ��������� ���� ������������ ��������,
       // ����� �������������� �� Search
       request.setAttribute("ViewType", request.getParameter("ViewType"));
       //��������� ��� ����� �� �������� ������ ����� ��������� ��������
       request.setAttribute("boxType", request.getParameter("boxType"));
       // ��������� ����� ������������� ������, ��� ���� ���� �������� ���� �������� (����������� ������ ������� �������������)
       request.setAttribute("deptID", request.getParameter("deptID"));

       // �������� ����� �������� ��� ���������
       int memoID = -1;
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("Invalid memoID [" + memoID + "]!");}

       // ���� ���� �������� � ����� ��������������� ���������� - ������� ��
       memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);

       if (memo != null) {
           // ���� ������ �������� - ����� �� ������, ������� ����� ������������ ��������
           memoDTO parentMemo = null;
           if (memo.getParentID() > 0) parentMemo = dH.getMemoDAO().findByID(dH, memo.getParentID());
           if (parentMemo != null) {
               request.setAttribute("parentMemoNumber", parentMemo.getMemoNumber());
           }
           // ������ ������������� ���������� �� ��� ��������, ���� ������ ����
           List recipientDept = dH.getRecipientDeptDAO().findDeptsRealizedMemo(dH, memoID, 1);
           // �������� ������ ���������� � JSP
           request.setAttribute("recipientDept", recipientDept);
           request.setAttribute("memo", memo);
           destPage = MEMO_VIEW_PAGE;
       } else {
           request.setAttribute(Defaults.ERROR_MSG_PARAM, "��������� ������� �� ����������!");
       }
   }

   // ������� �� ����� ��� ������ �������� �� ���������
   else if (MEMO_SEARCH_ACTION.equals(ACTION)) {

       // ����������� ���� ����� ��������� � ������ � ������ �������            
       String date_end_default = JLibUtils.dateToPattern(new Date(), Defaults.DATE_PATTERN, null);
       // ������ ����� � ������� ����� - ������ ������� ��������� ������ (������ ���������)
       Date dat = JLibUtils.dateToPeriod(new Date(), -Defaults.SEARCH_INTERVAL, DatePeriod.MONTH);
       // ����������� ���� ������ ��������� � ������ � ������ �������
       String date_start_default = JLibUtils.dateToPattern(dat, Defaults.DATE_PATTERN,null);
       // �������� ������ ������� ��������� ������ �� �������� ������
       request.setAttribute("date_start_default", date_start_default);
       // �������� ������� ������� ��������� ������ �� �������� ������
       request.setAttribute("date_end_default", date_end_default);
       //C����� ����������� ��������
       request.setAttribute("memberList", dH.getConnectorSource().getSomething(dH,0, "findAllPersonnelWithDepartmentId"));
       //������ �������������
       request.setAttribute("departmentList", dH.getConnectorSource().getSomething(dH, JPersonnelConsts.DeptType.HO, "getDepartmentsList"));

       destPage = MEMO_SEARCH_PAGE;
   }
   // ��������������� �����
   else if (MEMO_SEARCH_DO_ACTION.equals(ACTION)) {
       // ����������� ���� ����� ��������� � ������ � ������ �������
       String date_end_default = JLibUtils.dateToPattern(new Date(), Defaults.DATE_PATTERN,null);
       // ������ ����� � ������� ����� - ������ ������� ��������� ������ (������ ���������)
       Date dat = JLibUtils.dateToPeriod(new Date(), -Defaults.SEARCH_INTERVAL, DatePeriod.MONTH);
       // ����������� ���� ������ ��������� � ������ � ������ �������
       String date_start_default = JLibUtils.dateToPattern(dat, Defaults.DATE_PATTERN,null);
       // �������� ������ ������� ��������� ������ �� �������� ������
       request.setAttribute("date_start_default", date_start_default);
       // �������� ������� ������� ��������� ������ �� �������� ������
       request.setAttribute("date_end_default", date_end_default);
       //C����� ����������� ��������
       request.setAttribute("memberList", dH.getConnectorSource().getSomething(dH,0, "findAllPersonnelWithDepartmentId"));
       //������ �������������
       request.setAttribute("departmentList", dH.getConnectorSource().getSomething(dH, JPersonnelConsts.DeptType.HO,"getDepartmentsList"));

       // ������ ��������� ��������
       List memoList;
       // ���-�� ��������� ��������
       int memosCount = 0;

       // ����� �������� ��� ������
       int memoNumber = 0;
       try {memoNumber = Integer.parseInt(request.getParameter("memoNumber"));}
       catch (Exception e) {logger.debug("Invalid memoNumber parameter!");}
       // ���� �������� ��� ������
       String subject = request.getParameter("subject");
       // ���� - ������ ��������� ������� �������� ��������
       String date_start = request.getParameter("date_start");
       // ���� - ����� ��������� ������� �������� ��������
       String date_end = request.getParameter("date_end");
       // �����-����������� ��������
       int executorDeptId = -1;
       try {executorDeptId = Integer.parseInt(request.getParameter("executorDeptCode"));}
       catch (Exception e) {logger.debug("NOT executorDeptCode parameter!");}

       // �����-���������� ��������
       int recipientDeptId = -1;
       try {recipientDeptId = Integer.parseInt(request.getParameter("recipientDeptCode"));}
       catch (Exception e) {logger.debug("NOT recipientDeptCode parameter!");}

       // ����������� �������
       int executorUserId = -1;
       try {executorUserId = Integer.parseInt(request.getParameter("executorUserId"));}
       catch (Exception e) {logger.debug("NOT executorUserId parameter!");}

       // ���������������� ���������� ������ ��������
       memoList = dH.getMemoDAO().find(dH, memoNumber, subject, date_start, date_end, executorDeptId,
               recipientDeptId, executorUserId);

       // ��������� ��������� ��� �������� ����������
       request.setAttribute("memoList", memoList);
       if (memoList != null) memosCount = memoList.size();
       request.setAttribute("memosCount", memosCount);
       destPage = MEMO_SEARCH_PAGE;
   }
   // �������� ����� ��������
   else if (MEMO_PRINT_ACTION.equals(ACTION)) {

       // �������� �������� ������������� �������� ��� ������
       int memoID = 0;
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("Invalid memoID parameter [" + memoID + "] !");}
       // ���� �� �������� ������ �������� - ��������
       if (memoID > 0) {
           // ��������� � ����� ������ ������ "��������"
           memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);
           // ���������: ���������� - ������������� ��� ������
           if (memo.getRecipientsDepts().size() > 1) {
               request.setAttribute("boss", "����������� �������: ");
           } else {
               recipientDeptDTO recipientDept = (recipientDeptDTO) memo.getRecipientsDepts().get(0);

               if (recipientDept.getRecipientDeptName().lastIndexOf("������") > -1) {
                   request.setAttribute("boss", "���������� ������� ");
               } else {
                   request.setAttribute("boss", "���������� ������ ");
               }
           }
           request.setAttribute("memo", memo);

           destPage = MEMO_PRINT_PAGE;
       } else destPage = CONTROLLER_PAGE + "?action=depts";
   }
   else if (DEPT_NOTMAKE_ACTION.equals(ACTION)) {

       int personnelId = 0;
       //�������� ��� ������
       String type = request.getParameter("type");
       //���� personnel - ������� ������������� � ���� ��������� ������������, ����� ����� �������������
       if(type != null && type.equals("personnel")){
           personnelId = simpleEmployee.getPersonnelId();
       }

       // ��������� ��� ������
       request.setAttribute("deptCode", simpleEmployee.getDepartmentCode());
       // ����� �������� �� ������� ��� ������, ������������ ��� ���������� ����������� �������������
       request.setAttribute("memosList", dH.getRecipientUserDAO().NotMake(dH, simpleEmployee.getDepartmentId(), personnelId));
       // ��������� ������������� ������, ���� �������� �� ����������
       request.setAttribute("deptID", simpleEmployee.getDepartmentId());
       // ��������� ������ �����
       request.setAttribute("boxType", Defaults.INBOX);

       destPage = DEPT_BOX_PAGE;
   }
   
   logger.debug("ACTION: " + ACTION); logger.debug("DEST PAGE: " + destPage);
   // �������� ��������� ������� ������� � �������������� ������
   RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
   // ��������������� ���������������
   dispatcher.forward(request, response);

   logger.debug("LEAVING controller -> processRequest().");
  }

}
