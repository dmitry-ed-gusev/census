package org.memorandum.controllers;

import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dao.simple.docs.Defaults;
import org.census.commons.dto.docs.memoDTO;
//import org.census.commons.dto.docs.recipientDeptDTO;
import org.census.commons.utils.JLibUtils;
import org.census.commons.utils.mail.JMail;
import org.census.commons.utils.mail.JMailConfig;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
  * Данный сервлет выполняет отправку уведомлений отделам-получателям служебной записки. Список отделов
  * берется из таблицы recipientsDepts (БД "memorandum"), список адресов эл. почты берется из таблицы
  * depts_emails (БД "tomcat_users"). Перед отправкой проверяется наличие пользователя, инициирующего отправку
  * в БД "Кадры" и БД "Авторизация", а также его нахождение в отделе-создателе данной служебки.
  * @author Gusev Dmitry
  * @version 1.0
 */

public class senderController extends AbstractController {

    // send or not email notification
    private static final boolean sendEmailNotify = false;
    /** Тип действия: отправка служебки и уведомления. */
    private static final String SEND_MEMO_ACTION = "send";
    // SECURED. Тип действия: пересылка существующей служебки другим подразделениям
    private static final String FORWARD_MEMO_ACTION          = "forwardMemo";
    /** Страница с сообщениями об ошибке */
    private static final String ERROR_PAGE       = "/error.jsp";
    /** Страница, отображающая результат отправки служебки адресатам. */
    private static final String SEND_MEMO_RESULT = "/sender/sendResult.jsp";

  /**
   * Непосредственная обработка поступившего запроса.
  */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException
   {
    // Отправить/переслать служебку могут только пользователи, входящие в роль "memo_sender"(это проверяется
    // контейнером приложений - контроллер отправки объявлен как защищенный ресурс) и
    // состоящие в отделе-создателе/получателе служебки (это надо проверить вручную)

    logger.debug("ENTERING senderController -> processRequest().");
    // Начальная страница по умолчанию
    String destPage = ERROR_PAGE;

    acc_logger.info("senderController [" + request.getRemoteUser() + "] [" + ACTION + "]");

       Set<DepartmentDto> deptsTmp = employee.getDepartments();

    // Непосредственная отправка служебки адрессатам
    if (SEND_MEMO_ACTION.equals(ACTION)){

        // Получим идентификатор отправляемой служебки
        int memoID = -1;
        try {memoID = Integer.parseInt(request.getParameter("memoID"));}
        catch (Exception e) {
            logger.error("Invalid memoID [" + memoID + "]!");
        }
        // Найдем служебку по полученному идентификатору
        memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);
        // Если служебку нашли - работаем. Также перед выполнением каких-либо действий проверим существование
        // пользователя в БД "Авторизация" и "Кадры" и нахождение пользователя в отделе-создателе служебки
        if ((memo != null) && (employee != null) && ((deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getId() : 0) == memo.getExecutorDeptID())) {

            acc_logger.info("senderController [" + request.getRemoteUser() + "] [Sending memo: memoID=" + memo.getId() +
                    ", memoNumber=" + memo.getMemoNumber() + "]");

            logger.debug("All parameters are OK! Continue sending memo.");
            // Теперь необходимо получить список адресатов-отделов
            List recipients = dH.getRecipientDeptDAO().findAllByMemo(dH, memoID, Defaults.STATUS_UNDELETED);

            // Если список получателей не пуст - работаем
            if ((recipients != null) && (recipients.size() > 0)) {

                // Формируем список email адресов для рассылки уведомления
                List emailsList = null;
                List recipientEmails;
                String email;

                if (sendEmailNotify) { // if we will send email notify - generates emails list
                    // В цикле проходим по списку отделов-получателей служебки
                    for (Object aObject : recipients) {
                        // Получаем один объект отдел-получатель
                        //recipientDeptDTO recipientDept = (recipientDeptDTO) aObject;

                        // В начале смотрим таблицу Mapping и получаем список его эл. адресов
                        // todo: get department emails list and send email notifications
                        //recipientEmails = dH.getDeptsEmailsMappingDAO().findDeptId(dH, recipientDept.getRecipientDeptID());
                        recipientEmails = new ArrayList();

                        // Если список не пуст, то отправляем только по этим эл. адресам для данного отдела
                        if (recipientEmails != null) {
                            /*
                            for (int r = 0; r < recipientEmails.size(); r++) {
                                deptsEmailsMappingDTO deptsEmailsMapping;
                                deptsEmailsMapping = (deptsEmailsMappingDTO) recipientEmails.get(r);
                                if (deptsEmailsMapping.getEmail() != null && !deptsEmailsMapping.getEmail().trim().equals("")
                                        && !deptsEmailsMapping.getEmail().trim().equals("-")) {
                                    if (emailsList == null) {
                                        emailsList = new ArrayList();
                                    }
                                    emailsList.add(deptsEmailsMapping.getEmail());
                                }
                            }
                            */
                        }
                    } // end of FOR (recipients processing)

                }

                // Если в списке есть хотя бы один адрес - работаем(отправляем письма по указанным адресам)
                // (or if we will not send email notify)
                if ((sendEmailNotify && emailsList != null && emailsList.size() > 0) || !sendEmailNotify) {

                    String error = null;

                    if (sendEmailNotify) {
                        // Непосредственно отправка служебкной записки - создание объекта для работы с почтой
                        JMailConfig JMConfig = new JMailConfig();

                        // Обратный адрес системы служебок
                        JMConfig.setFrom(Defaults.MAIL_FROM);
                        // SMTP-сервер для отправки почты
                        JMConfig.setMailHost(Defaults.MAIL_HOST);

                        // Тема письма     24.10.2007
                        JMConfig.setSubject("СЗ № " + memo.getMemoNumber() + " от " + memo.getExecutorDeptCode() + ". " + memo.getSubject());
                        // Текст письма
                        String msgText = "Служебная записка №" + memo.getMemoNumber() + " от " + memo.getExecutorDeptCode() + "\n";
                        msgText = msgText + "Касательно: " + memo.getSubject() + "\n";
                        msgText = msgText + "Содержание: ";
                        if (memo.getText().trim().length() > 200) {
                            msgText = msgText + memo.getText().trim().substring(0, 200) + "\n";
                        } else {
                            msgText = msgText + memo.getText().trim() + "\n";
                        }
                        msgText = msgText + "Ознакомиться с ней можно по ссылке: \n";
                        msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&boxType=inbox&memoID=" + memo.getId();
                        JMConfig.setText(msgText);

                        // В цикле проходим по списку эл. адресов и отправляем письма
                        for (Object aObject : emailsList) {
                            // Получение адреса
                            email = (String) aObject;
                            logger.debug("email: " + email);
                            // Установка его в поле "адресат"
                            JMConfig.setTo(email);

                            JMail mail = new JMail(JMConfig);

                            // Отправка уведомления
                            try {
                                mail.send();
                            } catch (Exception e) {
                                error = "Cannot send email to [" + email + "]";
                                logger.error("Cannot send email to [" + email + "]");
                            }
                        }
                    } // end [if (sendEmailNotify)] condition

                    // --- Установка у данной служебки даты отправки и кто отправил (поле SendDate и SendUserId) ---
                    // Данные НЕ ИЗМЕНЯЮСТЯ если служебка была отправлена
                    if (memo.getSendDate() == null) {

                        logger.debug("Setting SendDate value.");
                        // В объект "служебка" добавим изменяемые поля - дата отправки и идентификатор пользователя
                        memo.setSendDate(JLibUtils.dateToPattern(new Date(), Defaults.DATE_PATTERN, null));
                        memo.setSendUserId(employee.getId());

                        // Выполняем установку параметра SendDate у нашей служебки
                        try {
                            dH.getMemoDAO().update(dH, memo, null);
                        }
                        catch (Exception e) {
                            logger.error("Cannot set SendDate value [memoID = " + memo.getId() +
                                    "; SendDate = " + memo.getSendDate() + "]");
                        }
                    }
                    // --- Установка флага "выполнено" у получателя родительской служебки - т.е. ---
                    // у отдела-получателя, который сейчас отправил данную служебку в ответ (если
                    // данная служебка является ответом на другую); причем данная служебка может и не
                    // требовать ответа - тогда в установке данного флага нет смысла

                    // Если данная служебка - ответ на другую, то проверим, не была ли первая служебка прислана с флагом "требует ответа".
                    if (memo.getParentID() > 0) {
                        // Найдем свой отдел в списке получателей данной служебки
                        //recipientDeptDTO recipientDept = dH.getRecipientDeptDAO().findByMemoDeptID(dH, (deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getId() : 0), memo.getParentID());
                        // Если мы есть в списке получателей родительской служебки и значение поля "вып/невып" равно "не выполнено" - работаем
                        /*
                        if ((recipientDept != null) && (recipientDept.getRealized() == 0)) {
                            // Создаем новый объект отдел-получатель для обновления записи
                            recipientDeptDTO recipientUpdate = new recipientDeptDTO();
                            recipientUpdate.setId(recipientDept.getId());
                            recipientUpdate.setRealized(1);
                            // Непосредственно обновление записи
                            try {
                                dH.getRecipientDeptDAO().update(dH, recipientUpdate);
                            }
                            catch (Exception e) {
                                logger.error("Cannot update record in recipientsDepts table!");
                            }
                        }
                        */
                    }

                    // Передаем ошибки(если они были) на страницу результата отправки
                    request.setAttribute("error", error);
                    // Передаем на страницу результата идентификатор служебки
                    request.setAttribute("memoID", memo.getId());
                    // Передаем на страницу результата номер служебки
                    request.setAttribute("memoNumber", memo.getMemoNumber());
                    // Передаем на страницу данные о челе под которым сидим
                    request.setAttribute("member", employee);
                    // Переход на страничку с сообщением о результате отправки
                    destPage = SEND_MEMO_RESULT;
                } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "НЕТ АДРЕСОВ ДЛЯ ОТПРАВКИ СЛУЖЕБНОЙ ЗАПИСКИ!");
            } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "СПИСОК ПОЛУЧАТЕЛЕЙ ДАННОЙ СЛУЖЕБНОЙ ЗАПИСКИ ПУСТ!");
        } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "СЛУЖЕБНАЯ ЗАПИСКА НЕ НАЙДЕНА ИЛИ У ВАС НЕТ ПРАВ НА " +
                "ОТПРАВКУ ДАННОЙ СЛУЖЕБНОЙ ЗАПИСКИ!");
    }

    // Пересылка служебки (уже существующей), с оповещением подразделений по e-mail
    else if (FORWARD_MEMO_ACTION.equals(ACTION))
    {
       //Получим идентификатор служебки
       int memoID = -1;
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("No memoID parameter!");}

       // Теперь найдем служебку, которую мы будем пересылать
       memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);
       if (memo == null) logger.error("Memo with ID = " + memoID + " doesn't exists!");

       //Получаем из формы список подразделений получателей
       List deptList;
       String[] depts = request.getParameterValues("depts");

       //Окончательно проверим что все в порядке
       if (memo != null && employee != null && depts != null) {
                     
           //Найдем все отделы-получатели
           deptList = dH.getRecipientDeptDAO().findDeptID(dH, depts);

           //если все нормально идем далее по списку
           if (deptList != null)
           for (Object aObject : deptList){
              ///recipientDeptDTO recipientDept = (recipientDeptDTO) aObject;
              //Добавим в объект идентификатор служебки и идентификатор отправиытеля
              //recipientDept.setMemoID(memoID);
              //recipientDept.setUpdateUserID(employee.getId());

               // todo: get department emails list and send email notifications

              // Формируем список email адресов для рассылки уведомления
              List   emailsList = null;
               String email;

               /*
              List<deptsEmailsMappingDTO>recipientEmails;


              // В начале смотрим таблицу Mapping и получаем список его эл. адресов

              //recipientEmails = dH.getDeptsEmailsMappingDAO().findDeptId(dH, recipientDept.getRecipientDeptID());
               recipientEmails = new ArrayList<>();

              // Если список не пуст, то отправляем только по этим эл. адресам для данного отдела
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
*/

              //Если есть список адресов для отправки, то оправляем сообщение и создаем запись в таблице получателей
              if(emailsList != null && emailsList.size() > 0){
                  // Непосредственно отправка служебкной записки - создание объекта для работы с почтой
                  JMailConfig JMConfig = new JMailConfig();
                  // Обратный адрес системы служебок
                  JMConfig.setFrom(Defaults.MAIL_FROM);
                  // SMTP-сервер для отправки почты
                  JMConfig.setMailHost(Defaults.MAIL_HOST);
                  // Тема письма
                  JMConfig.setSubject("Вам была переадресована СЗ № " + memo.getMemoNumber()
                          + " от " + (deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getCode() : null) + " подразделения (" + employee.getShortRusName() + "). ");
                  // Текст письма
                  //String msgText = "Служебная записка №" + memo.getMemoNumber() + " от " + memo.getExecutorDeptCode() + "\n";
                  String msgText = "Вам была переадресована СЗ № " + memo.getMemoNumber()
                      + " от " + (deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getCode() : null) + " подразделения (" + employee.getShortRusName() + "). " + "\n";
                  msgText = msgText + "Касательно: " + memo.getSubject() + "\n";
                  
                  msgText = msgText + "\n" + "кол.подразделений: " + deptList.size();
                  //msgText = msgText + "\n" + "подразделение: " + recipientDept.getRecipientDeptCode()
                  //         + " e-mails: " + emailsList.size() + "\n";

                  msgText = msgText + "Содержание: ";
                  if (memo.getText().trim().length() > 200) {
                      msgText = msgText + memo.getText().trim().substring(0, 200) + "\n";
                  } else {
                      msgText = msgText + memo.getText().trim() + "\n";
                  }

                  msgText = msgText + "Ознакомиться с ней можно по ссылке: \n";
                  msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&boxType=inbox&memoID=" + memo.getId();

                  msgText = msgText.replaceAll("\n<br>", "\n");
                  JMConfig.setText(msgText);


                  // В цикле проходим по списку эл. адресов и отправляем письма
                  for (Object aObject2 : emailsList) {
                      // Получение адреса
                      email = (String) aObject2;
                      // Установка его в поле "адресат"
                      JMConfig.setTo(email);

                      JMail mail = new JMail(JMConfig);

                      // Отправка уведомления
                      try {
                          mail.send();
                      }catch (Exception e) {logger.error("Cannot send email to [" + email + "]");}
                  }
              }
           }
           // Передаем на страницу результата идентификатор служебки
           request.setAttribute("memoID", memo.getId());
           // Передаем на страницу результата номер служебки
           request.setAttribute("memoNumber", memo.getMemoNumber());
           // Передаем на страницу данные о челе под которым сидим
           request.setAttribute("member", employee);
           // Переход на страничку с с ообщением о результате отправки
           destPage = SEND_MEMO_RESULT;
       }                   
    }

    logger.debug("ACTION: " + ACTION); logger.debug("DEST PAGE: " + destPage);

    // Получаем диспетчер данного запроса и перенаправляем запрос
    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
    // Непосредственно перенаправление
    dispatcher.forward(request, response);

    logger.debug("LEAVING senderController -> processRequest().");
   }
}
