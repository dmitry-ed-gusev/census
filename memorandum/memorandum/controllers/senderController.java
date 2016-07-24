package memorandum.controllers;

import memorandum.Defaults;
import memorandum.dataModel.dto.deptsEmailsMappingDTO;
import memorandum.dataModel.dto.memoDTO;
import memorandum.dataModel.dto.recipientDeptDTO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.*;

import jlib.mail.*;
import jlib.utils.JLibUtils;

/**
  * ������ ������� ��������� �������� ����������� �������-����������� ���������� �������. ������ �������
  * ������� �� ������� recipientsDepts (�� "memorandum"), ������ ������� ��. ����� ������� �� �������
  * depts_emails (�� "tomcat_users"). ����� ��������� ����������� ������� ������������, ������������� ��������
  * � �� "�����" � �� "�����������", � ����� ��� ���������� � ������-��������� ������ ��������.
  * @author Gusev Dmitry
  * @version 1.0
 */

public class senderController extends AbstractController
 {
  /** ��� ��������: �������� �������� � �����������. */
  private static final String SEND_MEMO_ACTION = "send";

  // SECURED. ��� ��������: ��������� ������������ �������� ������ ��������������
  private static final String FORWARD_MEMO_ACTION          = "forwardMemo";

  /** �������� � ����������� �� ������ */
  private static final String ERROR_PAGE       = "/error.jsp";
  /** ��������, ������������ ��������� �������� �������� ���������. */
  private static final String SEND_MEMO_RESULT = "/sender/sendResult.jsp";

  /**
   * ���������������� ��������� ������������ �������.
  */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException
   {
    // ���������/��������� �������� ����� ������ ������������, �������� � ���� "memo_sender"(��� �����������
    // ����������� ���������� - ���������� �������� �������� ��� ���������� ������) �
    // ��������� � ������-���������/���������� �������� (��� ���� ��������� �������)

    logger.debug("ENTERING senderController -> processRequest().");
    // ��������� �������� �� ���������
    String destPage = ERROR_PAGE;

    acc_logger.info("senderController [" + request.getRemoteUser() + "] [" + ACTION + "]");

    // ���������������� �������� �������� ����������
    if (SEND_MEMO_ACTION.equals(ACTION)){

        // ������� ������������� ������������ ��������
        int memoID = -1;
        try {memoID = Integer.parseInt(request.getParameter("memoID"));}
        catch (Exception e) {
            logger.error("Invalid memoID [" + memoID + "]!");
        }
        // ������ �������� �� ����������� ��������������
        memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);
        // ���� �������� ����� - ��������. ����� ����� ����������� �����-���� �������� �������� �������������
        // ������������ � �� "�����������" � "�����" � ���������� ������������ � ������-��������� ��������
        if ((memo != null) && (simpleEmployee != null) && (simpleEmployee.getDepartmentId() == memo.getExecutorDeptID())) {

            acc_logger.info("senderController [" + request.getRemoteUser() + "] [Sending memo: memoID=" + memo.getId() +
                    ", memoNumber=" + memo.getMemoNumber() + "]");

            logger.debug("All parameters are OK! Continue sending memo.");
            // ������ ���������� �������� ������ ���������-�������
            List recipients = dH.getRecipientDeptDAO().findAllByMemo(dH, memoID, Defaults.STATUS_UNDELETED);
            // ���� ������ ����������� �� ���� - ��������
            if ((recipients != null) && (recipients.size() > 0)) {
                // ��������� ������ email ������� ��� �������� �����������
                List emailsList = null;
                List recipientEmails;
                String email;

                // � ����� �������� �� ������ �������-����������� ��������
                for (Object aObject : recipients) {
                    // �������� ���� ������ �����-����������
                    recipientDeptDTO recipientDept = (recipientDeptDTO) aObject;
                    // � ������ ������� ������� Mapping � �������� ������ ��� ��. �������
                    recipientEmails = dH.getDeptsEmailsMappingDAO().findDeptId(dH, recipientDept.getRecipientDeptID());
                    // ���� ������ �� ����, �� ���������� ������ �� ���� ��. ������� ��� ������� ������
                    if (recipientEmails != null) {

                        for (int r = 0; r < recipientEmails.size(); r++) {
                            deptsEmailsMappingDTO deptsEmailsMapping;
                            deptsEmailsMapping = (deptsEmailsMappingDTO) recipientEmails.get(r);
                            if (deptsEmailsMapping.getEmail() != null && !deptsEmailsMapping.getEmail().trim().equals("")
                                    && !deptsEmailsMapping.getEmail().trim().equals("-")) {
                                if (emailsList == null) {emailsList = new ArrayList();}
                                emailsList.add(deptsEmailsMapping.getEmail());
                            }
                        }
                    }
                }

                // ���� � ������ ���� ���� �� ���� ����� - ��������(���������� ������ �� ��������� �������)
                if (emailsList != null && emailsList.size() > 0) {

                    // ��������������� �������� ���������� ������� - �������� ������� ��� ������ � ������
                    JMailConfig JMConfig = new JMailConfig();

                    // �������� ����� ������� ��������
                    JMConfig.setFrom(Defaults.MAIL_FROM);
                    // SMTP-������ ��� �������� �����
                    JMConfig.setMailHost(Defaults.MAIL_HOST);

                    // ���� ������     24.10.2007
                    JMConfig.setSubject("�� � " + memo.getMemoNumber() + " �� " + memo.getExecutorDeptCode() + ". " + memo.getSubject());
                    // ����� ������
                    String msgText = "��������� ������� �" + memo.getMemoNumber() + " �� " + memo.getExecutorDeptCode() + "\n";
                    msgText = msgText + "����������: " + memo.getSubject() + "\n";
                    msgText = msgText + "����������: ";
                    if (memo.getText().trim().length() > 200) {
                        msgText = msgText + memo.getText().trim().substring(0, 200) + "\n";
                    } else {
                        msgText = msgText + memo.getText().trim() + "\n";
                    }
                    msgText = msgText + "������������ � ��� ����� �� ������: \n";
                    msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&boxType=inbox&memoID=" + memo.getId();
                    JMConfig.setText(msgText);

                    String error = null;
                    // � ����� �������� �� ������ ��. ������� � ���������� ������
                    for (Object aObject : emailsList) {
                        // ��������� ������
                        email = (String) aObject;
                        logger.debug("email: " + email);
                        // ��������� ��� � ���� "�������"
                        JMConfig.setTo(email);

                        JMail mail = new JMail(JMConfig);

                        // �������� �����������
                        try {
                            mail.send();
                        }
                        catch (Exception e) {
                            error = "Cannot send email to [" + email + "]";
                            logger.error("Cannot send email to [" + email + "]");
                        }
                    }

                    // --- ��������� � ������ �������� ���� �������� � ��� �������� (���� SendDate � SendUserId) ---
                    // ������ �� ���������� ���� �������� ���� ����������
                    if (memo.getSendDate() == null) {

                        logger.debug("Setting SendDate value.");
                        // � ������ "��������" ������� ���������� ���� - ���� �������� � ������������� ������������
                        memo.setSendDate(JLibUtils.dateToPattern(new Date(), Defaults.DATE_PATTERN, null));
                        memo.setSendUserId(simpleEmployee.getPersonnelId());

                        // ��������� ��������� ��������� SendDate � ����� ��������
                        try {
                            dH.getMemoDAO().update(dH, memo, null);
                        }
                        catch (Exception e) {
                            logger.error("Cannot set SendDate value [memoID = " + memo.getId() +
                                    "; SendDate = " + memo.getSendDate() + "]");
                        }
                    }
                    // --- ��������� ����� "���������" � ���������� ������������ �������� - �.�. ---
                    // � ������-����������, ������� ������ �������� ������ �������� � ����� (����
                    // ������ �������� �������� ������� �� ������); ������ ������ �������� ����� � ��
                    // ��������� ������ - ����� � ��������� ������� ����� ��� ������

                    // ���� ������ �������� - ����� �� ������, �� ��������, �� ���� �� ������ �������� �������� � ������ "������� ������".
                    if (memo.getParentID() > 0) {
                        // ������ ���� ����� � ������ ����������� ������ ��������
                        recipientDeptDTO recipientDept = dH.getRecipientDeptDAO().findByMemoDeptID(dH, simpleEmployee.getDepartmentId(), memo.getParentID());
                        // ���� �� ���� � ������ ����������� ������������ �������� � �������� ���� "���/�����" ����� "�� ���������" - ��������
                        if ((recipientDept != null) && (recipientDept.getRealized() == 0)) {
                            // ������� ����� ������ �����-���������� ��� ���������� ������
                            recipientDeptDTO recipientUpdate = new recipientDeptDTO();
                            recipientUpdate.setId(recipientDept.getId());
                            recipientUpdate.setRealized(1);
                            // ��������������� ���������� ������
                            try {
                                dH.getRecipientDeptDAO().update(dH, recipientUpdate);
                            }
                            catch (Exception e) {
                                logger.error("Cannot update record in recipientsDepts table!");
                            }
                        }
                    }

                    // �������� ������(���� ��� ����) �� �������� ���������� ��������
                    request.setAttribute("error", error);
                    // �������� �� �������� ���������� ������������� ��������
                    request.setAttribute("memoID", memo.getId());
                    // �������� �� �������� ���������� ����� ��������
                    request.setAttribute("memoNumber", memo.getMemoNumber());
                    // �������� �� �������� ������ � ���� ��� ������� �����
                    request.setAttribute("member", simpleEmployee);
                    // ������� �� ��������� � ���������� � ���������� ��������
                    destPage = SEND_MEMO_RESULT;
                } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "��� ������� ��� �������� ��������� �������!");
            } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "������ ����������� ������ ��������� ������� ����!");
        } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "��������� ������� �� ������� ��� � ��� ��� ���� �� " +
                "�������� ������ ��������� �������!");
    }

    // ��������� �������� (��� ������������), � ����������� ������������� �� e-mail
    else if (FORWARD_MEMO_ACTION.equals(ACTION))
    {
       //������� ������������� ��������
       int memoID = -1;
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("No memoID parameter!");}

       // ������ ������ ��������, ������� �� ����� ����������
       memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);
       if (memo == null) logger.error("Memo with ID = " + memoID + " doesn't exists!");

       //�������� �� ����� ������ ������������� �����������
       List deptList;
       String[] depts = request.getParameterValues("depts");

       //������������ �������� ��� ��� � �������
       if (memo != null && simpleEmployee != null && depts != null) {
                     
           //������ ��� ������-����������
           deptList = dH.getRecipientDeptDAO().findDeptID(dH, depts);

           //���� ��� ��������� ���� ����� �� ������
           if (deptList != null)
           for (Object aObject : deptList){
              recipientDeptDTO recipientDept = (recipientDeptDTO) aObject;
              //������� � ������ ������������� �������� � ������������� ������������
              recipientDept.setMemoID(memoID);
              recipientDept.setUpdateUserID(simpleEmployee.getPersonnelId());

              // ��������� ������ email ������� ��� �������� �����������
              List   emailsList = null;
              List<deptsEmailsMappingDTO>recipientEmails;
              String email;

              // � ������ ������� ������� Mapping � �������� ������ ��� ��. �������
              recipientEmails = dH.getDeptsEmailsMappingDAO().findDeptId(dH, recipientDept.getRecipientDeptID());
              // ���� ������ �� ����, �� ���������� ������ �� ���� ��. ������� ��� ������� ������
              if (recipientEmails != null) {
                  for (int r = 0; r < recipientEmails.size(); r++) {

                      if (recipientEmails.get(r).getEmail() != null
                              && !recipientEmails.get(r).getEmail().trim().equals("")
                              && !recipientEmails.get(r).getEmail().trim().equals("-")){
                          if(emailsList == null){emailsList = new ArrayList();}
                          emailsList.add(recipientEmails.get(r).getEmail());
                      }                                                                             
                  }
              }

              //���� ���� ������ ������� ��� ��������, �� ��������� ��������� � ������� ������ � ������� �����������
              if(emailsList != null && emailsList.size() > 0){
                  // ��������������� �������� ���������� ������� - �������� ������� ��� ������ � ������
                  JMailConfig JMConfig = new JMailConfig();
                  // �������� ����� ������� ��������
                  JMConfig.setFrom(Defaults.MAIL_FROM);
                  // SMTP-������ ��� �������� �����
                  JMConfig.setMailHost(Defaults.MAIL_HOST);
                  // ���� ������
                  JMConfig.setSubject("��� ���� �������������� �� � " + memo.getMemoNumber()
                          + " �� " + simpleEmployee.getDepartmentCode() + " ������������� (" + simpleEmployee.getShortName() + "). ");
                  // ����� ������
                  //String msgText = "��������� ������� �" + memo.getMemoNumber() + " �� " + memo.getExecutorDeptCode() + "\n";
                  String msgText = "��� ���� �������������� �� � " + memo.getMemoNumber()
                      + " �� " + simpleEmployee.getDepartmentCode() + " ������������� (" + simpleEmployee.getShortName() + "). " + "\n";
                  msgText = msgText + "����������: " + memo.getSubject() + "\n";
                  
                  msgText = msgText + "\n" + "���.�������������: " + deptList.size();
                  msgText = msgText + "\n" + "�������������: " + recipientDept.getRecipientDeptCode()
                           + " e-mails: " + emailsList.size() + "\n";

                  msgText = msgText + "����������: ";
                  if (memo.getText().trim().length() > 200) {
                      msgText = msgText + memo.getText().trim().substring(0, 200) + "\n";
                  } else {
                      msgText = msgText + memo.getText().trim() + "\n";
                  }

                  msgText = msgText + "������������ � ��� ����� �� ������: \n";
                  msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&boxType=inbox&memoID=" + memo.getId();

                  msgText = msgText.replaceAll("\n<br>", "\n");
                  JMConfig.setText(msgText);


                  // � ����� �������� �� ������ ��. ������� � ���������� ������
                  for (Object aObject2 : emailsList) {
                      // ��������� ������
                      email = (String) aObject2;
                      // ��������� ��� � ���� "�������"
                      JMConfig.setTo(email);

                      JMail mail = new JMail(JMConfig);

                      // �������� �����������
                      try {
                          mail.send();
                      }catch (Exception e) {logger.error("Cannot send email to [" + email + "]");}
                  }
              }
           }
           // �������� �� �������� ���������� ������������� ��������
           request.setAttribute("memoID", memo.getId());
           // �������� �� �������� ���������� ����� ��������
           request.setAttribute("memoNumber", memo.getMemoNumber());
           // �������� �� �������� ������ � ���� ��� ������� �����
           request.setAttribute("member", simpleEmployee);
           // ������� �� ��������� � � ��������� � ���������� ��������
           destPage = SEND_MEMO_RESULT;
       }                   
    }

    logger.debug("ACTION: " + ACTION); logger.debug("DEST PAGE: " + destPage);

    // �������� ��������� ������� ������� � �������������� ������
    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
    // ��������������� ���������������
    dispatcher.forward(request, response);

    logger.debug("LEAVING senderController -> processRequest().");
   }
}
