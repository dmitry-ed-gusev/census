package memorandum.controllers;


import jpersonnel.JPersonnelConsts;
import jpersonnel.dto.SimpleLogicUserDTO;
import memorandum.Defaults;
import memorandum.dataModel.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jlib.mail.*;

/**
 * ����� ��������� ����������-�������� ���������� "��������� �������". ��������, ����������� ������ ������������,
 * �������� ������ ����� �������� ����������� (������������ ������� � ���� [memo_editor]).
 * @author Gusev Dmitry
 * @version 1.0
*/

public class editorController extends AbstractController
{

 // SECURED. ��� ��������: �������� �������� (��� �1) - ����� �����������
 private static final String ADD_MEMO_STEP1               = "addStep1";
 // SECURED. ��� ��������: �������� �������� (��� �2) - ���������� ��������� ����� � �������� ��������.
 private static final String ADD_MEMO_STEP2               = "addStep2";
 // SECURED. ��� ��������: ��������������� �������� (��� �2) - ���������� ��������� ����� � �������� ��������.     
 private static final String ADD_MEMO_STEP2FORWARD        = "addStep2forForward";
 // SECURED. ��� ��������: ���������� �������� ����� ����������/������
 private static final String SAVE_MEMO_ACTION             = "saveMemo";
 // SECURED. ��� ��������: ���������� ������ �� ��������� �������, ��� �� �������������
 private static final String SAVE_MEMO_FORWARD_ACTION     = "saveForwardMemo";   
 // SECURED. ��� ��������: ����� ����������� �������� ��������
 private static final String SAVE_MEMO_RESULT             = "saveMemoRes";
 // SECURED. ��� ��������: �������������� ��� ������������ ��������
 private static final String EDIT_MEMO_ACTION             = "editMemo";

 // SECURED. �������� �� �������� ���� "���� ������"
 private static final String DEL_REALIZEDDATE_ACTION      = "deleteRealizedDate";
 // SECURED. ��� ��������: ����� �� ���������� ��������
 private static final String ANSWER_APPOINTED_MEMO_ACTION = "answerAppoint";
 // SECURED. ��� ��������: ���������� ������ �� ���������� ��������
 private static final String ANSWER_APPOINTED_MEMO_RESULT_ACTION = "answerAppointResult";

 // �������� � ����������� �� ������
 private static final String ERROR_PAGE                   = "/error.jsp?prefix=../";

 // SECURED. ����� �� ������� ������� ��� - ��� ������ �����������
 private static final String DEPTS_FORM                   = "/editor/deptsList.jsp";
 // SECURED. ����� ��� ���������/�������������� ����� ��������
 private static final String MEMO_FORM                    = "/editor/memoForm.jsp";
 // SECURED. ����� ��� ���������/�������������� ����� ��������
 private static final String MEMO_FORM_FORWARD            = "/editor/memoFormForward.jsp";
 // SECURED. �������� ��������� ���������� ���������� ��������
 private static final String SAVE_MEMO_RESULT_PAGE        = "/editor/addMemoRes.jsp";
 // SECURED. �������, ������� ������������ ����� �������� - ������������ ��
 private static final String NEW_MEMO_PROCESSING_PAGE     = "/editor/memoProcessing";
 // SECURED. �������� � ������ ��� ������ �� ��������
 private static final String ANSWER_APPOINTED_MEMO_PAGE   = "/editor/appointmentAnswer.jsp";
 // SECURED. �������� � ����������� ���������� ������ �� ���������� ��������
 private static final String ANSWER_APPOINTED_MEMO_RESULT_PAGE   = "/editor/appointmentAnswerResult.jsp";

 /**
  * ���������������� ��������� ������������ �������. ���� ����� ������������ ��������� ������ �
  * ���������� �����. � �������� ���������� ����� �������� ����������� HTTP-������ -
  * ������(request) � �����(response).
 */
 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
   logger.debug("ENTERING editorController -> processRequest().");

   // ��������� �������� �� ���������
   String destPage = ERROR_PAGE;

   acc_logger.info("editorController [" + request.getRemoteUser() + "] [" + ACTION + "]");

   // ������� �� ����� �1 ���������� �������� - ����� �������(-�)-�����������(-�).
   // (������� �� �������� � ������ ������ ������� ���)
   if (ADD_MEMO_STEP1.equals(this.ACTION)) {

       // ���� ������� ������ ������������� ���������� - �������� ������
       if (simpleEmployee != null) {
           // ������������� ���������� ���� �������� � ��������� ���
           request.setAttribute("memberID", simpleEmployee.getPersonnelId());

           // ��������� ������ � ������������ � ��������� ���
           request.setAttribute("member", simpleEmployee);

           // ���� �� �� �������, � ����������� �������� - ���� �������� ������������� ������������� ��������, � ����� �������� ��� ������
           int memoID = -1;
           try {
               memoID = Integer.parseInt(request.getParameter("memoID"));
           }
           catch (Exception e) {
               logger.debug("Edit memo mode active!");
           }
           request.setAttribute("memoID", memoID);

           // ���� �� ����������� �������� - �� ������������� ������� � ���
           if (memoID > 0)
               acc_logger.info("editorController [" + simpleEmployee.getShortName() + "] [Edit memo: memoID=" + memoID + "]");

           // ���������� ������ ������� ���
           request.setAttribute("deptsList", dH.getConnectorSource().getSomething(dH, JPersonnelConsts.DeptType.HO, "getDepartmentsList"));

           //��� ��������� �������� ��������� ������� FORWARD, ���� �� ����, �� ���������� �������� ��� ��������������� ��������
           request.setAttribute("forward", request.getParameter("forward"));

           // ���� ��� ����� �� �������� - ���� ����� �������� ����� ������������ ��������.
           // ����� ���� ���� ��������� - ����� �� ������ ������������ �������� �� ��� �������� -
           // �� �������� ����� �������� ������������, ������� ����� <EDITOR> � ���������� � ��� �� ������,
           // ��� � �����-����������� ������ ��������. � ��������� ������ ������������ �� ����� �����
           // ������������� ������ ��������.
           int parentID = -1;
           try {
               parentID = Integer.parseInt(request.getParameter("parentID"));
           } catch (Exception e) {
               logger.debug("No parent memo ID!");
           }
           if (parentID > 0) {
               // ������ ������������ ��������
               memoDTO memo = dH.getMemoDAO().findByID(dH, parentID);
               if (memo != null) {

                   // ���� �� �������� �� �������� - ������� �� ������������� � ���
                   acc_logger.info("editorController [" + simpleEmployee.getShortName() + "] [Answer on memo: memoID=" + parentID + "]");

                   // ���������: ������� �� ������ ����� ������������ � ������ �����������, ���� �������� ���� ������ �����,
                   // ������� ��������� � ������ �����������. ����� ������ �� NULL ���� ����� ������� � ������ �����������
                   recipientDeptDTO recipientDept = dH.getRecipientDeptDAO().findByMemoDeptID(dH, simpleEmployee.getDepartmentId(), parentID);

                   if ((simpleEmployee != null) && (recipientDept != null)) {
                       //request.setAttribute("parentID", parentID);
                       // ��� ������ �� �������� ���������� ��� ������ ������������� ����������
                       destPage = "/editor/controller?action=" + ADD_MEMO_STEP2 + "&memoID=" + memoID + "&parentID="
                               + parentID + "&memberID=" + simpleEmployee.getPersonnelId() + "&depts=" + memo.getExecutorDeptID();
                   } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "�� �� ������ �������� �� ������ ��������!");
               } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "�� �� ������ �������� �� ������ ��������!");
           } else destPage = DEPTS_FORM;
       } else {
           logger.error("Can't find registered user [" + simpleEmployee.getShortName() + "].");
           request.setAttribute(Defaults.ERROR_MSG_PARAM, "Can't find registered user [" + simpleEmployee.getShortName() + "].");
       }
   }

   // ������� �� ����� �2 ���������� �������� - ���������� ������ ��������.
   // (������� �� �������� � ������ ����������/�������������� ��������)
   else if (ADD_MEMO_STEP2.equals(ACTION)) {

       // ������� ������ "��������"
       memoDTO memo = new memoDTO();

       // ���� ���� �������� � ������������ - ��������
       if (simpleEmployee != null) {
           // � ������ �������� ������� ��� ��������� ���������� - ���������-�����������
           memo.setExecutorUserID(simpleEmployee.getPersonnelId());
           memo.setExecutorShortName(simpleEmployee.getShortName());
           memo.setExecutorDeptID(simpleEmployee.getDepartmentId());
           memo.setExecutorDeptCode(simpleEmployee.getDepartmentCode());
           // ����� ������� � �������� ������������� ������������ ��������
           int parentID = -1;
           try {
               parentID = Integer.parseInt(request.getParameter("parentID"));
           }
           catch (Exception e) {logger.debug("Not parentID Parameter");}
           logger.debug("Recieved parentID value = " + parentID);
           
           if (parentID > 0) {
               // ������ ������ "������������ ��������"
               memoDTO parentMemo = dH.getMemoDAO().findByID(dH, parentID);
               memo.setParentID(parentID);
               //������� ���� <br> �� �������, ��� <textarea>
               parentMemo.setText(parentMemo.getText().replaceAll("<br>", ""));
               request.setAttribute("parentMemo", parentMemo);
           }

           // � ������ �������� ������� ��� ��������� ���������� - ������ �������-�����������
           String[] depts = request.getParameterValues("depts");

           // ���� ������ �������-����������� �� ���� - �������� �����
           if (depts != null) {

               depts = dH.getRecipientDeptDAO().checkDepartment(depts);
               
               List deptList = dH.getRecipientDeptDAO().findDeptID(dH, depts);
               if (deptList.size() == 1) {
                   request.setAttribute("one_only", "one_only");
               }
               memo.setRecipientsDepts(deptList);
               // ��������� ������ "��������" �� ��������� ���� ���������
               request.setAttribute("memo", memo);
               // ��������� �������� ���������� ��� ��������������� �������
               destPage = MEMO_FORM;
           } else {
               logger.error("DEPTS list is EMPTY!");
               request.setAttribute(Defaults.ERROR_MSG_PARAM, "DEPTS list is EMPTY!");
           }

       } else {
           logger.error("memberID parameter is invalid!");
           request.setAttribute(Defaults.ERROR_MSG_PARAM, "memberID parameter is invalid!");
       }
   }

    // ������� �� ����� �2 ���������� �������� - ���������� ������ ��������.
    // (������� �� �������� � ������ ����������/�������������� ��������)
    else if (ADD_MEMO_STEP2FORWARD.equals(ACTION)) {

       logger.debug(ADD_MEMO_STEP2FORWARD);
       // ������������� �� ������� ���������� � ������ �������������
       int memoID = -1;
       try {
           memoID = Integer.parseInt(request.getParameter("memoID"));
       }
       catch (Exception e) {
           logger.error("Invalid memoID value!");
       }

       // ���� �� �������� ���������� ������������� ���������� � �� - �������� ������
       if (simpleEmployee != null && memoID > 0) {
           // ������ ������ ��������, ������� ���������� � �� ������� ������ �����
           memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);

           // � �������� ������� ��� ��������� ���������� - ���������-�����������
           memo.setExecutorUserID(simpleEmployee.getPersonnelId());
           memo.setExecutorShortName(simpleEmployee.getShortName());
           memo.setExecutorDeptID(simpleEmployee.getDepartmentId());
           memo.setExecutorDeptCode(simpleEmployee.getDepartmentCode());

           // � �������� ������� ���������� - ������ �������-�����������
           String[] depts = request.getParameterValues("depts");
           // ���� ������ �������-����������� �� ���� - �������� �����
           if (depts != null) {
               List deptList = dH.getRecipientDeptDAO().findDeptID(dH, depts);
               if (deptList.size() == 1) {
                   request.setAttribute("one_only", "one_only");
               }
               memo.setRecipientsDepts(deptList);
               // ��������� ������ "��������" �� ��������� ���� ���������
               request.setAttribute("memo", memo);
               // ��������� �������� ���������� ��� ��������������� �������
               destPage = MEMO_FORM_FORWARD;
           } else {
               logger.error("DEPTS list is EMPTY!");
               request.setAttribute(Defaults.ERROR_MSG_PARAM, "DEPTS list is EMPTY!");
           }

       } else {
           logger.error("memberID or memoId parameter is invalid!");
           request.setAttribute(Defaults.ERROR_MSG_PARAM, "memberID parameter is invalid!");
       }
   }

   // ���������� ����������� ����������/�������������� ��������
   else if (SAVE_MEMO_ACTION.equals(ACTION)) {
       // ������������� ����������/����������� ��������
       int memoID = -1;       
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("No memoID parameter!");}
       // �������� �������� ���������, ��� �������� ���� ���������������, ���� id>0
       if (memoID > 0){request.setAttribute("edit", "edit");}
       
       destPage = NEW_MEMO_PROCESSING_PAGE;}

   // ���������� ����������� ������ �� ��������� ������� ��� �� �������������   
   else if (SAVE_MEMO_FORWARD_ACTION.equals(ACTION)) {
       // ������������� ����������/����������� ��������
       int memoID = -1;
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("No memoID parameter!");}

       String email;
       // E��� ������������� �� ������� ��������
       if (memoID > 0){
           //������ �� �� ������� ���� �����, � ������� ���������� ������ ��������������
           memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);

           //������� ������ �������������, ������� �������������� ��������
           String[] depts = request.getParameterValues("recipient");
           List   emailsList = null;

           //������� ���������� �� �������������, ������� ���������� ������ ��������� �������
           String prim = request.getParameter("prim");

           if (depts != null){

               //���������� �� ������ �������������, ��������� ������ � ������� recipientDept (���������� ��),
               //� ��� �� ��������� ����������� � ������������� �� ��������� �������

               List deptList = dH.getRecipientDeptDAO().findDeptID(dH, depts);
               for (int u = 0; u < deptList.size(); u++){

                   //���������� ���� � ��������� ����������� ��������� �������
                   recipientDeptDTO r = (recipientDeptDTO) deptList.get(u);
                   r.setMemoID(memoID);
                   r.setUpdateUserID(simpleEmployee.getPersonnelId());
                   try{
                       //��������� ������������� � ������ ����������� ��������
                       dH.getRecipientDeptDAO().create(dH, r);
                       
                       // � ������ ������� ������� Mapping � �������� ������ ��� ��. �������
                       List recipientEmails = dH.getDeptsEmailsMappingDAO().findDeptId(dH, r.getRecipientDeptID());
                       // ���� ������ �� ����, �� ���������� ������ �� ���� ��. �������
                       if (recipientEmails != null) {
                           for (int i = 0; i < recipientEmails.size(); i++) {
                               deptsEmailsMappingDTO deptsEmailsMapping;
                               deptsEmailsMapping = (deptsEmailsMappingDTO) recipientEmails.get(i);
                               if (deptsEmailsMapping.getEmail() != null
                                       && !deptsEmailsMapping.getEmail().trim().equals("")){

                                   if(emailsList == null){emailsList = new ArrayList();}
                                   emailsList.add(deptsEmailsMapping.getEmail());
                               }
                           }
                       }

                   }catch (Exception e){
                       logger.error("Can't create recipientDept = " + r.getRecipientDeptCode());
                       request.setAttribute(Defaults.ERROR_MSG_PARAM, "Can't create recipientDept = " + r.getRecipientDeptCode());}
               }

               // ���� � ������ ���� ���� �� ���� ����� - ��������(���������� ������ �� ��������� �������)
               if (emailsList != null && emailsList.size() > 0){
                   // ��������������� �������� ����������� � ������������� �������� ������� - �������� ������� ��� ������ � ������
                   JMailConfig JMConfig = new JMailConfig();
                   // �������� ����� ������� ��������
                   JMConfig.setFrom(Defaults.MAIL_FROM);
                   // SMTP-������ ��� �������� �����
                   JMConfig.setMailHost(Defaults.MAIL_HOST);
                   // ���� ������
                   JMConfig.setSubject("������������� �� � " + memo.getMemoNumber() + " �� " + simpleEmployee.getShortName()
                           + " (" + simpleEmployee.getDepartmentCode() + ")");

                   // ����� ������
                   String msgText = "��� ���� �������������� ��������� ������� �" + memo.getMemoNumber()
                           + " �� " +  simpleEmployee.getShortName() + " (" + simpleEmployee.getDepartmentCode() + ")" + "\n";

                   msgText = msgText + "����������� �������������, ���������������� ��������� �������: " + "\n";
                   msgText = msgText + prim + "\n" + "\n";

                   msgText = msgText + "����������: " + memo.getSubject() + "\n" + "\n";
                   msgText = msgText + "����������: ";
                   if (memo.getText().trim().length() > 200){
                       msgText = msgText + (memo.getText().trim().substring(0,200)).replaceAll("<br>", "") + "\n" + "\n";}
                   else{msgText = msgText + memo.getText().trim().replaceAll("<br>", "") + "\n" + "\n";}
                   
                   msgText = msgText + "������������ � ��� ����� �� ������: \n";
                   msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&boxType=inbox&memoID=" + memo.getId();
                   JMConfig.setText(msgText);

                   // � ����� �������� �� ������ ��. ������� � ���������� ������
                   for (Object aObject : emailsList){
                       // ��������� ������
                       email = (String) aObject;
                       // ��������� ��� � ���� "�������"
                       JMConfig.setTo(email);

                       JMail mail = new JMail(JMConfig);

                       // �������� �����������
                       try {
                           mail.send();
                           acc_logger.info("editorController [" + simpleEmployee.getShortName() + "] [send to " + email + "]");
                       }
                       catch (Exception e) {logger.error("Cannot send email to [" + email + "]");}
                   }
                   //������� �� �������� ���������� ������������� ���������
                   request.setAttribute("memoID", memoID);
                   request.setAttribute("memo", memo);
                   request.setAttribute("forward", "forward");
                   destPage = SAVE_MEMO_RESULT_PAGE;
               }
           }else{
               logger.error("DEPTS list is EMPTY!"); request.setAttribute(Defaults.ERROR_MSG_PARAM, "DEPTS list is EMPTY!");
           }                 
       }else{
           request.setAttribute(Defaults.ERROR_MSG_PARAM, "memoId parameter is invalid!");
       }
   }

   // �������� ���� "���� ������"
   else if (DEL_REALIZEDDATE_ACTION.equals(ACTION)) {
       // ������������� ���������� ��������
       int memoID = -1;
       try {
           memoID = Integer.parseInt(request.getParameter("memoID"));
       }
       catch (Exception e) {
           logger.error("No memoID parameter!");
       }

       // ���� ������� ������������� �������� � ������������ ������ � ������ Memo_chief, �� ���������� ������
       // �� ������ �������� ����� �������� ��������, ��� ��� ��������� ������ �������������� ��������(���� ����)
       if (memoID > 0 && request.isUserInRole("memo_chief")) {
           //������ ��������
           memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);
           //������� ���� "���� ������"
           memo.setRealizedDate("");
           
           try {
               // ��������������� ��� UPDATE, ��������� ���� � NULL
               dH.getMemoDAO().update(dH, memo, null);
           }
           catch (Exception e) {
               logger.error("Cannot set RealizedDate IN NULL");
           }
           // ����� ���������� UPDATE, ������� �� ��������� � ���������� � ����������� ��������
           destPage = "/editor/controller?action=saveMemoRes&memoID=" + memoID + "&edit=edit";
       }
   }

   // ��������� � ����������� ��������/�������������� ��������
   else if (SAVE_MEMO_RESULT.equals(ACTION)) {
       // ��������� � �������� �������� EDIT, ���� �� ����, �� �������� ���� ���������������
       // EDIT ����� ���������� ��� � ���� ���������, ��� � � ���� ���������
       if (request.getParameter("edit") != null || request.getAttribute("edit") != null) {
           request.setAttribute("edit", "edit");
       }

       // �������� �� ��������� ���������� ���������� � ��������
       request.setAttribute("error", request.getAttribute("error"));
       // ����� ��������� �� ��������� � ����������� ������������� ���������/���������� ��������
       int memoID = -1;
       try {
           memoID = Integer.parseInt(request.getParameter("memoID"));
       }
       catch (Exception e) {
           logger.error("No memoID parameter!");
       }

       memoDTO memo = null;
       // ������ ��������
       if (memoID > 0) {
           memo = dH.getMemoDAO().findByID(dH, memoID);
       }

       request.setAttribute("memoID", memoID);
       request.setAttribute("memo", memo);
       destPage = SAVE_MEMO_RESULT_PAGE;
   }

   // �������������� �������� (��� ������������)
   else if (EDIT_MEMO_ACTION.equals(ACTION)) {
       int memoID = 0;
       // �������� ������������� ������������� ��������
       try {
           memoID = Integer.parseInt(request.getParameter("memoID"));
       }
       catch (Exception e) {
           logger.error("Invalid memoID [" + memoID + "] parameter!");
       }
       // ������ ������ ��������, ������� �� ����� �������������
       memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);
       if (memo == null) logger.error("Memo with ID = " + memoID + " doesn't exists!");
       // ������������� ������������-��������� �������� (�� �� "�����")
       int executorUserID = -1;
       if (memo != null) {
           executorUserID = memo.getExecutorUserID();
       }
       if (executorUserID < 0) logger.error("ERROR: executorUserID < 0!");
       // ������������� ������ ������������-��������� �������� (�� �� "�����")
       int executorDeptID = -1;
       if (memo != null) {
           executorDeptID = memo.getExecutorDeptID();
       }
       if (executorDeptID < 0) logger.error("ERROR: executorDeptID < 0!");

       // ������������� �������� ����� ��� �� ��������� ��� ��������� ������(������������ � ������� memo_chief),
       // � ������� �������� ��������� �������� - ���������.
       // ����� �������� ������ �������������, ���� ��� ��� ���������� - ���� ��������� ���� SendDate - ��� ������ ���� ������.
       if ((memo != null) && (simpleEmployee.getPersonnelId() > 0) && (executorUserID > 0) && (memo.getSendDate() == null) &&
               ((simpleEmployee.getPersonnelId() == executorUserID) || request.isUserInRole("memo_chief"))) {
           //������ ������ "������������ ��������", ���� ������� ����
           if (memo.getParentID() > 0) {
               memoDTO parentMemo = dH.getMemoDAO().findByID(dH, memo.getParentID());
               //������� ���� <br> �� �������, ��� <textarea>
               parentMemo.setText(parentMemo.getText().replaceAll("<br>", ""));
               request.setAttribute("parentMemo", parentMemo);
           }

           //������� ���� <br> �� �������, ��� <textarea>
           memo.setText(memo.getText().replaceAll("<br>", ""));
           // ���� �� �������� ������ ��������� - ������ ������������ ������ �� ����������
           String[] depts = request.getParameterValues("depts");
           if (depts != null) memo.setRecipientsDepts(dH.getRecipientDeptDAO().findByDeptIDList(dH, depts));
           // �������� �� �������� �������������� ��� ������ "��������"
           request.setAttribute("memo", memo);
           // ������������� ������ �������� ��������������
           destPage = MEMO_FORM;
       } else
           request.setAttribute("errorMsg", "�������������� ��������� ������� ����������!");
   }

   // ����� �� ���������� ��������. ������������ ����� �������� ������ �� �� ���������, �������
   // ���������� ������ ���. ���� �������� ������������ ��� ����� ����������� �������� - �� ��������
   // �� ��� �� ������! ������ ������, �� ������ � �������� �������������!
   else if (ANSWER_APPOINTED_MEMO_ACTION.equals(ACTION)) {
       // ������������� ���������� ��������
       int memoID = -1;
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("editiorController: Can't recieve memoID parameter!");}

       // ������������� ������ ����������, �������� ���� �������� ��������
       int id        = -1;
       try {id       = Integer.parseInt(request.getParameter("id"));}
       catch (Exception e) {logger.error("Can't recieve ID parameter!");}

       // ������ "��������"
       memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);

       // ������ "���������, �������� �������� ������ ��������", �� ��������������
       recipientUserDTO recipient = null;
       if (id > 0) {
           recipient = dH.getRecipientUserDAO().findByID(dH, id);
       }

       // ��������: �������� �� ������� ������������(memberDTO member) ����������� �������������, � �������� ���������
       // ���. ���� ��� ������� ��������. ������� ���� ������ ������������� ���������� ����� ��������� �� ���� �� �����.
       // ���� ��� �������� ����������� ������ - ������� ������������ ����� �������� �� ��������� (�������� ��� ��������)
       if ((recipient != null) && (simpleEmployee.getDepartmentId() == recipient.getRecipientDeptID())) {

           // �������� ������ "��������"
           request.setAttribute("memo", memo);
           // �������� ������ "������������-����������"
           request.setAttribute("recipient", recipient);
           // �������� � ������ ��� ������ �� ���������� ��������
           destPage = ANSWER_APPOINTED_MEMO_PAGE;
       } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "�� �� ������ �������� �� ������ ���������!");
   }

   // ���������� ������ �� ���������� ��������. ��� �� ������ ����� ������ ��� ������ �� ��������,� ���� ������
   // ��������� ��������� ���� ���������� ��� �� �������� ��������� (���� ����� ����) � ������� �� ��������
   // ������� ���������� ��������� ���������� ����� ��������
   else if (ANSWER_APPOINTED_MEMO_RESULT_ACTION.equals(ACTION))
    {
     // ������������� ����� ��������� ��������, ���� �� ���� ������ ��������� ��������� ��������� ���� �������
     // � ���������� ���������
     int newMemoID        = -1;
     try {newMemoID       = Integer.parseInt(request.getParameter("newMemoID"));}
     catch (Exception e) {logger.debug("Can't recieve newMemoID parameter!");}

     // ������������� ���������� ��������
     int memoID        = -1;
     try {memoID       = Integer.parseInt(request.getParameter("memoID"));}
     catch (Exception e) {logger.error("editiorController: Can't recieve memoID parameter! newMemoID = " + newMemoID);}

     // ������������� ������ ����������, �������� ���� �������� ��������
     int id        = -1;
     try {id       = Integer.parseInt(request.getParameter("id"));}
     catch (Exception e) {logger.debug("Can't recieve ID parameter! memoid = " + memoID);}

     // ������������� ����������, �������� ���� �������� ��������
     // �������� �������� ��� ������ �� �������� ��� ����������� � ���������� ���������   
     int recipientUserID        = -1;
     try {recipientUserID       = Integer.parseInt(request.getParameter("recipientUserID"));}
     catch (Exception e) {logger.debug("Can't recieve recipientUserID parameter! memoid = " + memoID);}
        
     // ������ "��������"
     memoDTO memo      = dH.getMemoDAO().findByID(dH, memoID);

     // ������ "���������, �������� ���� �������� ������ ��������"
     recipientUserDTO recipient = null;
     if (id > 0 && memoID > 0){
         //������ ��������� �� ��������������, ����� ����� ����� �����
         recipient = dH.getRecipientUserDAO().findByID(dH, id);
     }else if(recipientUserID > 0 && memoID > 0){
         //������ ������ �� ����������� ���������
         recipient = dH.getRecipientUserDAO().findByMemoDeptID(dH, recipientUserID, memoID, 0);
     }

     // ����� ������ �� ���������� ��������
     String answer = request.getParameter("answer");

     // ���� ��� �������� ����������� ������ - ������� ������������ ����� �������� �� ���������,
     // ����� ��������� ����� ��� ������, ������������� ������ "���������" � ���������� ����������� � ����������
     // ���������� ������������� � ����, ��� ������� ������ ��������
     if ((answer != null) && (!answer.trim().equals("")) && (recipient != null)) {

         // ������� ������ "������������, �������� �������� ��������" � ������� � ���� ������
         recipientUserDTO recipientUpdate = new recipientUserDTO();
         recipientUpdate.setId(recipient.getId());
         recipientUpdate.setRealized(1);
         // ������� � ������ ������ ��� ������������, ��� �������� ��� ������ ������ ����������� ������
         recipientUpdate.setAnswer(answer + "(���. " + simpleEmployee.getShortName() + ")");
         // ��������������� ���������� ���������� ������ �� ��������
         try {
             dH.getRecipientUserDAO().update(dH, recipientUpdate);
         }
         catch (Exception e) {
             logger.error("Can't save appoint memo answer result!");
         }
         
         // �������� ��������� ����������� (���������� ������������� ��� ����������), ����
         // ��� �������� ��������� ��� ������ ������� ��������� ����������� �� E-mail
         if (recipient.getSendemail() == 1) {
             // ������ ���� ����������� �������� � �������� �������� ��. ����� ������������ �� �� LDAP
             // � ����������� ����� ���� ��������� ������� � ��������� email, ������� ��, � ��������� �� ������
             List appointedUserEmails = (List)dH.getConnectorSource().getSomething(dH, recipient.getAppointedUserID(), "getUserEmailByPersonnelId");

             if (appointedUserEmails != null) {

                 //���������� �������� ���������� ���������
                 JMailConfig JMConfig = new JMailConfig();

                 JMConfig.setFrom(Defaults.MAIL_FROM);
                 JMConfig.setMailHost(Defaults.MAIL_HOST);
                 JMConfig.setSubject("���������� ��������� �������");
                 String msgText = "��������� ������� � " + memo.getMemoNumber() + " ���������: " + simpleEmployee.getShortName() + ".\n";
                 msgText = msgText + "�����������: " + answer + "\n";
                 msgText = msgText + "������������ �� ��������� �������� ����� �� ������: \n";
                 msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&memoID=" + memo.getId() + "&boxType=inbox&deptID=" + memo.getExecutorDeptID();

                 // ���� ������ ������ ��� �������� ����� �� (newMemoID >0) � ��������� ��������� �������� �� ���������,
                 // � ��� ��, ��������� ����������� ���� �� e-mail, �� ������ ����� ���������
                 if (newMemoID > 0) {
                     msgText = "����������� ����� �� ��������� ������� � " + memo.getMemoNumber() + "\n";
                     msgText = msgText + "����������: " + memo.getSubject() + "\n";
                     msgText = msgText + "��������: " + simpleEmployee.getShortName() + "\n";
                     msgText = msgText + "������������ ����� �� ������: \n";
                     msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&memoID=" + newMemoID + "&boxType=outbox&deptID=" + simpleEmployee.getDepartmentId();
                 }

                 JMConfig.setText(msgText);

                 //��������� �� ������ email ������������ recipient.getAppointedUserID() � �������� ������
                 for (int i = 0; i < appointedUserEmails.size(); i++){
                     SimpleLogicUserDTO logicUser = (SimpleLogicUserDTO)appointedUserEmails.get(i);

                     JMConfig.setTo(logicUser.getEmail());
                     logger.debug("MAIL for send appointment memo notify: [" + logicUser.getEmail() + "]");

                     JMail mail = new JMail(JMConfig);

                     try {
                         mail.send();
                     } catch (Exception e) {
                         logger.error("editorController: Can't send notify to following email ["
                                 + logicUser.getEmail() + "]. memoId = " + memo.getId());
                     }
                 }
             }
         }
         // ������ "��������"
         request.setAttribute("memo", memo);
         // ��������� � ����������� ���������� ������ �� ���������� ��������
         destPage = ANSWER_APPOINTED_MEMO_RESULT_PAGE;

     } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "�� �� ������ �������� �� ������ ���������!");

        // ��������, ���� ������ ������ ��� �������� ��������, �� �������������� �� ��������� �������� ����������� ����� ��
        if (newMemoID > 0) {
            request.setAttribute(Defaults.ERROR_MSG_PARAM, "");
            memoDTO memonew = null;
            // ������ ��������
            if (memoID > 0) {
                memonew = dH.getMemoDAO().findByID(dH, newMemoID);
            }

            request.setAttribute("memoID", newMemoID);
            request.setAttribute("memo", memonew);
            destPage = SAVE_MEMO_RESULT_PAGE;
        }
    }

   // ���� �� ������� �� ���� �� ����� ��������, ����������� ������������
   else request.setAttribute(Defaults.ERROR_MSG_PARAM, "�������� ��� ��������: [" + ACTION + "]!");

   // ���������� �����
   logger.debug("ACTION: " + ACTION); logger.debug("DEST PAGE: " + destPage);

   // �������� ��������� ������� ������� � �������������� ������
   RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
   // ��������������� ���������������
   dispatcher.forward(request, response);

   logger.debug("LEAVING editorController -> processRequest().");
  }

}