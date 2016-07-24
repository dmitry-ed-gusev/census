package memorandum.controllers;

import jlib.mail.*;
import jpersonnel.dto.SimpleEmployeeDTO;
import jpersonnel.dto.SimpleLogicUserDTO;
import memorandum.Defaults;
import memorandum.dataModel.dto.memoDTO;
import memorandum.dataModel.dto.recipientDeptDTO;
import memorandum.dataModel.dto.recipientUserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;

/**
 * ������ ����� - ����������-�������, ������� ��������� �������� �� ��������� ��������� ������� ����������
 * ������. ������ � ������� ����������� ��������� ����� memo_appointer.
 *
 */

public class appointmentController extends AbstractController {

    /**
     * ��� ��������: ��������� �������� ����������.
     */
    private static final String APPOINT_ACTION = "appoint";
    /**
     * ��� ��������: ������� �� �������� � ����������� ��������� ��������.
     */
    private static final String APPOINT_RES_ACTION = "appointResult";

    /**
     * �������� � ����������� �� ������.
     */
    private static final String ERROR_PAGE = "/error.jsp?prefix=../";
    /**
     * �������� ��� ��������� �������� ����������.
     */
    private static final String APPOINT_PAGE = "/appointment/appoint.jsp";
    /**
     * �������� � ����������� ��������� ��������.
     */
    private static final String APPOINT_RES_PAGE = "/appointment/appointResult.jsp";

    /**
     * ���������������� ��������� ������������ �������.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("ENTERING appointmentController -> processRequest().");

        // ��������� �������� �� ���������
        String destPage = ERROR_PAGE;
        // ������� ��� �������� "������" - ��� ����������� ����������� ������. ��������� ��������� ������ �������� �� ��������
        request.setAttribute("prefix", "../");

        acc_logger.info("appointmentController [" + request.getRemoteUser() + "] [" + ACTION + "]");

        // ��������� ��������
        if (APPOINT_ACTION.equals(ACTION)) {

            // ������������� ������ ������������
            int deptID = -1;
            if (simpleEmployee != null) deptID = simpleEmployee.getDepartmentId();

            // ������������� ���������� ��������
            int memoID = -1;
            // ������ "��������" ��� �������� ��������������
            memoDTO memo;
            // �������� �������� ������������� ���������� ��������
            try {
                memoID = Integer.parseInt(request.getParameter("memoID"));
            }
            catch (Exception e) {
                logger.error("appointmentController: Can't recieve memoID parameter!");
            }
            // �� �������������� ������ ��������
            memo = dH.getMemoDAO().findByID(dH, memoID);

            // ���� �� �������� ������������� ������ � ������ ����� �������� - ��������.
            if ((deptID > 0) && (memoID > 0) && (memo != null)) {

                // ����� ���������, ��� ������������, ���������� ��������, �������� � ��� ������, � ������� �������� ����������
                // (����������: �����, � ������� �������� ������������ ���� � ������ ����������� ������ ��������)
                recipientDeptDTO recipient = dH.getRecipientDeptDAO().findByMemoDeptID(dH, deptID, memoID);
                if (recipient != null) {
                    // ������ ����������� ������
                    request.setAttribute("membersList", dH.getConnectorSource().getSomething(dH, deptID,"findAllPersonnelWithDepartmentId"));
                    // ���������, ���������� ��������
                    request.setAttribute("member", simpleEmployee);
                    // ������������� ���������� ��������
                    request.setAttribute("memoID", memoID);
                    destPage = APPOINT_PAGE;
                    acc_logger.info("appointmentController [" + request.getRemoteUser() + "] [Enter into " + APPOINT_PAGE + "]");
                } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "�� �� ������ �������� ������ ��������� �������!");
            } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "�������� ������������� ������ ��� ��������� �������!");
        }

        // ��������� ��������� ��������
        else if (APPOINT_RES_ACTION.equals(ACTION)) {

            // �������� ������������� ���������� ��������
            int memoID = -1;
            try {
                memoID = Integer.parseInt(request.getParameter("memoID"));
            }
            catch (Exception e) {
                logger.error("appointmentController: ResAction: Can't recieve memoID parameter! PERSONNELID = " + simpleEmployee.getPersonnelId() +
                        " value: " + request.getParameter("memoID"));
            }

            // ������ "��������" ��� �������� ��������������
            memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);

            /// �������� �������������� ������������� (�� �� "�����"), ������� �������� ��������
            String[] members = request.getParameterValues("memberID");

            // ���� ������ �������������-������� �������� �������� �� ���� - �������� �����
            List MemberList = null;
            // ������ �� ������� �� ��� ��� ����������
            if (members != null) {
                MemberList = dH.getMemberDAO().findMemberID(dH, members);
            } else {
                logger.error("Members list is EMPTY!");
            }

            // ����(����) ���������� ��������
            String realizedDate = request.getParameter("realizedDate");
            // �������� ����������� ��� ����������� ��������
            String subject = request.getParameter("subject");
            // �������� ������� �������� E-mail ��� ���������� ���������
            String sendemail = request.getParameter("sendemail");

            // ������ ��� �����������, ������� �������� ��, ����������� � ����� for
            String memberShortName = "";

            // ��������������� ���������� ��������� (���� ��� ��������� �����), ���� ���, �� ������������ �������
            if (memoID > 0 && memo != null && MemberList != null && subject != null && !subject.trim().equals("") &&
                    realizedDate != null && !realizedDate.trim().equals("") && simpleEmployee != null) {

                // ���������� �� ������ ������������� - ���� �������� ��������
                for (int r = 0; r < MemberList.size(); r++) {

                    SimpleEmployeeDTO simpleEmployee2;
                    simpleEmployee2 = (SimpleEmployeeDTO) MemberList.get(r);

                    recipientUserDTO recipient = new recipientUserDTO();
                    // ����������� �����������
                    recipient.setSubject(subject);
                    // ������������� ���������� ��������
                    recipient.setMemoID(memoID);
                    // ������������� ������������, ���� �������� ��������
                    recipient.setRecipientUserID(simpleEmployee2.getPersonnelId());
                    // ������������� ������, � ������� �������� ������������, �������� �������� ��������
                    recipient.setRecipientDeptID(simpleEmployee2.getDepartmentId());
                    // ���� ���������� ��������
                    recipient.setRealizedDate(realizedDate);
                    // ������ ���������� �������� - "�� ���������"
                    recipient.setRealized(0);
                    // ������������, ��������� ������
                    recipient.setUpdateUserID(simpleEmployee.getPersonnelId());
                    // ������������, ���������� ��������
                    recipient.setAppointedUserID(simpleEmployee.getPersonnelId());
                    // ������� �������� E-mail ��� ���������� ���������, �� ��������� 0-���
                    if (sendemail != null && sendemail.equals("on")) {
                        recipient.setSendemail(1);
                    }

                    acc_logger.info("appointmentController [" + request.getRemoteUser() + "] [Appoint memo: memoID=" + recipient.getMemoID() +
                            ", appointedUserID=" + recipient.getAppointedUserID() + ", memberID=" + recipient.getRecipientUserID() + "]");

                    // ��������������� ��������� ��������
                    try {
                        dH.getRecipientUserDAO().create(dH, recipient);
                    }
                    catch (Exception e) {
                        logger.error("Can't appoint this memo!");
                        request.setAttribute(Defaults.ERROR_MSG_PARAM, "Can't appoint this memo!");
                    }

                    // ����� ��������� �������� - �������� ������������, �������� ��� ��������, email
                    // �������� �������� ��. ����� ������������ �� �� LDAP, ����� ���� ��������� �������
                    // � ��������� email, ������� ��, � ��������� �� ������
                    List recipientUserEmails = (List)dH.getConnectorSource().getSomething(dH, simpleEmployee2.getPersonnelId(),"getUserEmailByPersonnelId");
                                        
                    // ���� ������ �� ���� ���������� �� ���� ������
                    if (recipientUserEmails != null) {

                        //���������� �������� ���������� ���������
                        JMailConfig JMConfig = new JMailConfig();

                        JMConfig.setFrom(Defaults.MAIL_FROM);
                        JMConfig.setMailHost(Defaults.MAIL_HOST);
                        JMConfig.setSubject("��������� ��������� �������");
                        String msgText = "��� �������� ���������� ��������� ������� � " + memo.getMemoNumber()
                                + " �� " + memo.getExecutorDeptCode() + " ���." + memo.getExecutorShortName() + ".\n";
                        msgText = msgText + "����������: " + memo.getSubject() + "\n";
                        msgText = msgText + "���� ����������: " + realizedDate + ".\n";
                        msgText = msgText + "����������� ���������� (" + simpleEmployee.getShortName().trim() + "): " + subject + "\n";
                        msgText = msgText + "������������ �� ��������� �������� ����� �� ������: \n";
                        msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&memoID=" + memo.getId() + "&boxType=inbox";
                        JMConfig.setText(msgText);

                        //��������� �� ������ email ������������ recipient.getAppointedUserID() � �������� ������
                        for (int i = 0; i < recipientUserEmails.size(); i++){
                            SimpleLogicUserDTO logicUser = (SimpleLogicUserDTO)recipientUserEmails.get(i);

                            JMConfig.setTo(logicUser.getEmail());
                            logger.debug("MAIL for send appointment memo notify: [" + logicUser.getEmail() + "]");

                            JMail mail = new JMail(JMConfig);

                            try {
                                mail.send();
                            } catch (Exception e) {
                                logger.error("AppointmentController: Can't send notify to following email ["
                                   + logicUser.getEmail() + "]. memoId = " + memo.getId());
                            }
                        }                                                                        
                    } else logger.error("Emails list for this member is EMPTY! Can't send email message!");

                    // ������������� ���������� ��������
                    request.setAttribute("memoID", memoID);
                    // ����� ���������� ��������
                    request.setAttribute("memoNumber", memo.getMemoNumber());
                    // ������� �.�. �����������, ������� �������� ��������
                    memberShortName = memberShortName + simpleEmployee2.getShortName() + " ";
                    request.setAttribute("memberShortName", memberShortName.trim());
                    // ��� ������, � ������� �������� ������ ���������
                    request.setAttribute("memberDeptCode", simpleEmployee2.getDepartmentCode());
                    // ���� ���������� ��������� �������
                    request.setAttribute("realizedDate", realizedDate);
                    destPage = APPOINT_RES_PAGE;
                }  //End ���� �� �������������
            }
            // ���� �� ��� ���� ���������, �� ������������� ����� �� ��������� "�������� ��", � ���������� ����� �����������
            // ����� ��������� ����� ���� �� ���������
            else {

                // ������������� ������ ������������
                int deptID = -1;
                if (simpleEmployee != null) deptID = simpleEmployee.getDepartmentId();

                // ���� �� �������� ������������� ������ � ������ ����� �������� - ��������.
                if (deptID > 0 && memoID > 0 && memo != null) {

                    // ����� ���������, ��� ������������, ���������� ��������, �������� � ��� ������, � ������� �������� ����������
                    // (����������: �����, � ������� �������� ������������ ���� � ������ ����������� ������ ��������)
                    recipientDeptDTO recipient = dH.getRecipientDeptDAO().findByMemoDeptID(dH, deptID, memoID);
                    if (recipient != null) {

                        // ������ ����������� ������
                        request.setAttribute("membersList", dH.getConnectorSource().getSomething(dH, deptID,"findAllPersonnelWithDepartmentId"));
                        // ���������, ���������� ��������
                        request.setAttribute("member", simpleEmployee);
                        // ������������� ���������� ��������
                        request.setAttribute("memoID", memoID);
                        // ���� ���������� ��������� �������
                        request.setAttribute("realizedDate", realizedDate);
                        // ����������� ��� ����������� ��������
                        request.setAttribute("subject", subject);
                        // ID ���� �������� ���������� ��, ���� ���, �� ����� -1
                        request.setAttribute("memberID", -1);
                        // ������� �������� E-mail ��� ���������� ��������� - ������������� � ��������� FALSE, ���� ����
                        if (sendemail != null && sendemail.equals("on")) {
                            request.setAttribute("sendmail", "on");
                        }

                        // ������ ������������� �����
                        String Warning = "�� ��������� ��������� ����: ";

                        if (subject == null || subject.trim().equals("")) {
                            Warning = Warning + "\"" + "�������" + "\"" + ",";
                        }
                        if (realizedDate == null || realizedDate.trim().equals("")) {
                            Warning = Warning + "\"" + "���� ����������" + "\"" + ",";
                        }

                        Warning = Warning + "\"" + "��������� ������" + "\"" + ",";
                        // ������ ������ �������
                        Warning = Warning.trim().substring(0, Warning.trim().length() - 1);

                        request.setAttribute("Warning", Warning);
                        destPage = APPOINT_PAGE;
                        acc_logger.info("appointmentController [" + request.getRemoteUser() + "] [Enter into " + APPOINT_PAGE + "]");
                    } else
                        request.setAttribute(Defaults.ERROR_MSG_PARAM, "�� �� ������ �������� ������ ��������� �������!");
                } else
                    request.setAttribute(Defaults.ERROR_MSG_PARAM, "�������� ������������� ������ ��� ��������� �������!");
            }
        }

        // �� ������� �� ���� �� ��������
        else {
            request.setAttribute(Defaults.ERROR_MSG_PARAM, "�������� ��� �������� [" + ACTION + "]!");
        }

        logger.debug("ACTION: " + ACTION);
        logger.debug("DEST PAGE: " + destPage);

        // �������� ��������� ������� ������� � �������������� ������
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
        // ��������������� ���������������
        dispatcher.forward(request, response);

        logger.debug("LEAVING appointmentController -> processRequest().");
    }
}