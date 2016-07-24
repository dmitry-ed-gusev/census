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
 * Данный класс - контроллер-сервлет, который реализует действия по поручению служебной записки сотруднику
 * отдела. Доступ к данному контроллеру ограничен ролью memo_appointer.
 *
 */

public class appointmentController extends AbstractController {

    /**
     * Тип действия: поручение служебки сотруднику.
     */
    private static final String APPOINT_ACTION = "appoint";
    /**
     * Тип действия: переход на страницу с результатом поручения служебки.
     */
    private static final String APPOINT_RES_ACTION = "appointResult";

    /**
     * Страница с сообщениями об ошибке.
     */
    private static final String ERROR_PAGE = "/error.jsp?prefix=../";
    /**
     * Страница для поручения служебки сотруднику.
     */
    private static final String APPOINT_PAGE = "/appointment/appoint.jsp";
    /**
     * Страница с результатом поручения служебки.
     */
    private static final String APPOINT_RES_PAGE = "/appointment/appointResult.jsp";

    /**
     * Непосредственная обработка поступившего запроса.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("ENTERING appointmentController -> processRequest().");

        // Начальная страница по умолчанию
        String destPage = ERROR_PAGE;
        // Префикс для страницы "ошибка" - для правильного отображения стилей. Остальным страницам данный параметр не помешает
        request.setAttribute("prefix", "../");

        acc_logger.info("appointmentController [" + request.getRemoteUser() + "] [" + ACTION + "]");

        // Поручение служебки
        if (APPOINT_ACTION.equals(ACTION)) {

            // Идентификатор отдела пользователя
            int deptID = -1;
            if (simpleEmployee != null) deptID = simpleEmployee.getDepartmentId();

            // Идентификатор поручаемой служебки
            int memoID = -1;
            // Объект "служебка" для проверки идентификатора
            memoDTO memo;
            // Пытаемся получить идентификатор поручаемой служебки
            try {
                memoID = Integer.parseInt(request.getParameter("memoID"));
            }
            catch (Exception e) {
                logger.error("appointmentController: Can't recieve memoID parameter!");
            }
            // По идентификатору найдем служебку
            memo = dH.getMemoDAO().findByID(dH, memoID);

            // Если мы получили идентификатор отдела и верный номер служебки - работаем.
            if ((deptID > 0) && (memoID > 0) && (memo != null)) {

                // Также проверяем, что пользователь, поручающий служебку, работает в том отделе, в который служебка адресована
                // (необходимо: отдел, в котором числится пользователь есть в списке получателей данной служебки)
                recipientDeptDTO recipient = dH.getRecipientDeptDAO().findByMemoDeptID(dH, deptID, memoID);
                if (recipient != null) {
                    // Список сотрудников отдела
                    request.setAttribute("membersList", dH.getConnectorSource().getSomething(dH, deptID,"findAllPersonnelWithDepartmentId"));
                    // Сотрудник, поручающий служебку
                    request.setAttribute("member", simpleEmployee);
                    // Идентификатор поручаемой служебки
                    request.setAttribute("memoID", memoID);
                    destPage = APPOINT_PAGE;
                    acc_logger.info("appointmentController [" + request.getRemoteUser() + "] [Enter into " + APPOINT_PAGE + "]");
                } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "Вы не можете поручить данную служебную записку!");
            } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "Неверный идентификатор отдела или служебной записки!");
        }

        // Результат поручения служебки
        else if (APPOINT_RES_ACTION.equals(ACTION)) {

            // Получаем идентификатор поручаемой служебки
            int memoID = -1;
            try {
                memoID = Integer.parseInt(request.getParameter("memoID"));
            }
            catch (Exception e) {
                logger.error("appointmentController: ResAction: Can't recieve memoID parameter! PERSONNELID = " + simpleEmployee.getPersonnelId() +
                        " value: " + request.getParameter("memoID"));
            }

            // Объект "служебка" для проверки идентификатора
            memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);

            /// Получаем идентификаторы пользователей (из БД "Кадры"), которым поручена служебка
            String[] members = request.getParameterValues("memberID");

            // Если список пользователей-которым поручена служебка не пуст - работаем далее
            List MemberList = null;
            // Найдем по каждому из них всю информацию
            if (members != null) {
                MemberList = dH.getMemberDAO().findMemberID(dH, members);
            } else {
                logger.error("Members list is EMPTY!");
            }

            // Срок(дата) исполнения служебки
            String realizedDate = request.getParameter("realizedDate");
            // Получаем комментарий для исполнителя служебки
            String subject = request.getParameter("subject");
            // Получаем признак отправки E-mail при выполнении поручения
            String sendemail = request.getParameter("sendemail");

            // Список ФИО сотрудников, которым поручены СЗ, формируется в цикле for
            String memberShortName = "";

            // Непосредственно выполнение поручения (если все параметры верны), если нет, то возвращаемся обратно
            if (memoID > 0 && memo != null && MemberList != null && subject != null && !subject.trim().equals("") &&
                    realizedDate != null && !realizedDate.trim().equals("") && simpleEmployee != null) {

                // Проходимся по списку пользователей - кому поручена служебка
                for (int r = 0; r < MemberList.size(); r++) {

                    SimpleEmployeeDTO simpleEmployee2;
                    simpleEmployee2 = (SimpleEmployeeDTO) MemberList.get(r);

                    recipientUserDTO recipient = new recipientUserDTO();
                    // Комментарий исполнителю
                    recipient.setSubject(subject);
                    // Идентификатор поручаемой служебки
                    recipient.setMemoID(memoID);
                    // Идентификатор пользователя, кому поручена служебка
                    recipient.setRecipientUserID(simpleEmployee2.getPersonnelId());
                    // Идентификатор отдела, в котором работает пользователь, которому поручили служебку
                    recipient.setRecipientDeptID(simpleEmployee2.getDepartmentId());
                    // Срок исполнения служебки
                    recipient.setRealizedDate(realizedDate);
                    // Статус выполнения служебки - "не выполнено"
                    recipient.setRealized(0);
                    // Пользователь, создавший запись
                    recipient.setUpdateUserID(simpleEmployee.getPersonnelId());
                    // Пользователь, поручивший служебку
                    recipient.setAppointedUserID(simpleEmployee.getPersonnelId());
                    // Признак отправки E-mail при выполнении поручения, по умолчанию 0-нет
                    if (sendemail != null && sendemail.equals("on")) {
                        recipient.setSendemail(1);
                    }

                    acc_logger.info("appointmentController [" + request.getRemoteUser() + "] [Appoint memo: memoID=" + recipient.getMemoID() +
                            ", appointedUserID=" + recipient.getAppointedUserID() + ", memberID=" + recipient.getRecipientUserID() + "]");

                    // Непосредственно поручение служебки
                    try {
                        dH.getRecipientUserDAO().create(dH, recipient);
                    }
                    catch (Exception e) {
                        logger.error("Can't appoint this memo!");
                        request.setAttribute(Defaults.ERROR_MSG_PARAM, "Can't appoint this memo!");
                    }

                    // После поручения служебки - направим пользователю, которому она поручена, email
                    // Получаем основной эл. адрес пользователя из БД LDAP, может быть несколько логинов
                    // с основными email, получим их, и пройдемся по списку
                    List recipientUserEmails = (List)dH.getConnectorSource().getSomething(dH, simpleEmployee2.getPersonnelId(),"getUserEmailByPersonnelId");
                                        
                    // Если список не пуст отправляем на него письмо
                    if (recipientUserEmails != null) {

                        //Заполнение основных параметров сообщения
                        JMailConfig JMConfig = new JMailConfig();

                        JMConfig.setFrom(Defaults.MAIL_FROM);
                        JMConfig.setMailHost(Defaults.MAIL_HOST);
                        JMConfig.setSubject("Поручение СЛУЖЕБНЫЕ ЗАПИСКИ");
                        String msgText = "Вам поручено исполнение служебной записки № " + memo.getMemoNumber()
                                + " от " + memo.getExecutorDeptCode() + " исп." + memo.getExecutorShortName() + ".\n";
                        msgText = msgText + "Касательно: " + memo.getSubject() + "\n";
                        msgText = msgText + "СРОК ИСПОЛНЕНИЯ: " + realizedDate + ".\n";
                        msgText = msgText + "КОММЕНТАРИЙ ПОРУЧИТЕЛЯ (" + simpleEmployee.getShortName().trim() + "): " + subject + "\n";
                        msgText = msgText + "Ознакомиться со служебной запиской можно по ссылке: \n";
                        msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&memoID=" + memo.getId() + "&boxType=inbox";
                        JMConfig.setText(msgText);

                        //Пройдемся по списку email пользователя recipient.getAppointedUserID() и отправим письмо
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

                    // Идентификатор порученной служебки
                    request.setAttribute("memoID", memoID);
                    // Номер порученной служебки
                    request.setAttribute("memoNumber", memo.getMemoNumber());
                    // Фамилия И.О. сотрудников, которым служебка поручена
                    memberShortName = memberShortName + simpleEmployee2.getShortName() + " ";
                    request.setAttribute("memberShortName", memberShortName.trim());
                    // Код отдела, в котором работает данный сотрудник
                    request.setAttribute("memberDeptCode", simpleEmployee2.getDepartmentCode());
                    // Срок исполнения служебной записки
                    request.setAttribute("realizedDate", realizedDate);
                    destPage = APPOINT_RES_PAGE;
                }  //End цикл по пользователям
            }
            // Если не все поля заполнены, то перебрасываем снова на страничку "Поручени СЗ", с введенными ранее параметрами
            // Также указываем какие поля не заполнены
            else {

                // Идентификатор отдела пользователя
                int deptID = -1;
                if (simpleEmployee != null) deptID = simpleEmployee.getDepartmentId();

                // Если мы получили идентификатор отдела и верный номер служебки - работаем.
                if (deptID > 0 && memoID > 0 && memo != null) {

                    // Также проверяем, что пользователь, поручающий служебку, работает в том отделе, в который служебка адресована
                    // (необходимо: отдел, в котором числится пользователь есть в списке получателей данной служебки)
                    recipientDeptDTO recipient = dH.getRecipientDeptDAO().findByMemoDeptID(dH, deptID, memoID);
                    if (recipient != null) {

                        // Список сотрудников отдела
                        request.setAttribute("membersList", dH.getConnectorSource().getSomething(dH, deptID,"findAllPersonnelWithDepartmentId"));
                        // Сотрудник, поручающий служебку
                        request.setAttribute("member", simpleEmployee);
                        // Идентификатор поручаемой служебки
                        request.setAttribute("memoID", memoID);
                        // Срок исполнения служебной записки
                        request.setAttribute("realizedDate", realizedDate);
                        // Комментарий для исполнителя служебки
                        request.setAttribute("subject", subject);
                        // ID чела которому поручается СЗ, если нет, то равно -1
                        request.setAttribute("memberID", -1);
                        // Признак отправки E-mail при выполнении поручения - устанавливаем в положение FALSE, если надо
                        if (sendemail != null && sendemail.equals("on")) {
                            request.setAttribute("sendmail", "on");
                        }

                        // Список незаполненных полей
                        String Warning = "НЕ ЗАПОЛНЕНЫ СЛЕДУЮЩИЕ ПОЛЯ: ";

                        if (subject == null || subject.trim().equals("")) {
                            Warning = Warning + "\"" + "ЗАДАНИЕ" + "\"" + ",";
                        }
                        if (realizedDate == null || realizedDate.trim().equals("")) {
                            Warning = Warning + "\"" + "СРОК ИСПОЛНЕНИЯ" + "\"" + ",";
                        }

                        Warning = Warning + "\"" + "СОТРУДНИК ОТДЕЛА" + "\"" + ",";
                        // Уберем лишнюю запятую
                        Warning = Warning.trim().substring(0, Warning.trim().length() - 1);

                        request.setAttribute("Warning", Warning);
                        destPage = APPOINT_PAGE;
                        acc_logger.info("appointmentController [" + request.getRemoteUser() + "] [Enter into " + APPOINT_PAGE + "]");
                    } else
                        request.setAttribute(Defaults.ERROR_MSG_PARAM, "Вы не можете поручить данную служебную записку!");
                } else
                    request.setAttribute(Defaults.ERROR_MSG_PARAM, "Неверный идентификатор отдела или служебной записки!");
            }
        }

        // Не подошло ни одно из действий
        else {
            request.setAttribute(Defaults.ERROR_MSG_PARAM, "Неверный тип действия [" + ACTION + "]!");
        }

        logger.debug("ACTION: " + ACTION);
        logger.debug("DEST PAGE: " + destPage);

        // Получаем диспетчер данного запроса и перенаправляем запрос
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
        // Непосредственно перенаправление
        dispatcher.forward(request, response);

        logger.debug("LEAVING appointmentController -> processRequest().");
    }
}