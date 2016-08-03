package org.memorandum.controllers;

import org.census.commons.dto.admin.LogicUserDto;
import org.census.commons.dto.docs.memoDTO;
//import org.census.commons.dto.docs.recipientDeptDTO;
//import org.census.commons.dto.docs.recipientUserDTO;
import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dao.simple.docs.Defaults;
import org.census.commons.utils.mail.JMail;
import org.census.commons.utils.mail.JMailConfig;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Класс реализует контроллер-редактор приложения "Служебные записки". Действия, выполняемые данным контроллером,
 * доступны только после успешной авторизации (пользователь состоит в роли [memo_editor]).
 * @author Gusev Dmitry
 * @version 1.0
*/

public class editorController extends AbstractController
{

 // SECURED. Тип действия: создание служебки (шаг №1) - выбор получателей
 private static final String ADD_MEMO_STEP1               = "addStep1";
 // SECURED. Тип действия: создание служебки (шаг №2) - заполнение остальных полей и отправка служебки.
 private static final String ADD_MEMO_STEP2               = "addStep2";
 // SECURED. Тип действия: перенаправление служебки (шаг №2) - заполнение остальных полей и отправка служебки.     
 private static final String ADD_MEMO_STEP2FORWARD        = "addStep2forForward";
 // SECURED. Тип действия: сохранения служебки после добавления/правки
 private static final String SAVE_MEMO_ACTION             = "saveMemo";
 // SECURED. Тип действия: сохранения ответа на служебную записку, при ее переадресации
 private static final String SAVE_MEMO_FORWARD_ACTION     = "saveForwardMemo";   
 // SECURED. Тип действия: вывод результатов создания служебки
 private static final String SAVE_MEMO_RESULT             = "saveMemoRes";
 // SECURED. Тип действия: редактирование уже существующей служебки
 private static final String EDIT_MEMO_ACTION             = "editMemo";

 // SECURED. Удаление из служебки даты "Срок ответа"
 private static final String DEL_REALIZEDDATE_ACTION      = "deleteRealizedDate";
 // SECURED. Тип действия: ответ на порученную служебку
 private static final String ANSWER_APPOINTED_MEMO_ACTION = "answerAppoint";
 // SECURED. Тип действия: сохранение ответа на порученную служебку
 private static final String ANSWER_APPOINTED_MEMO_RESULT_ACTION = "answerAppointResult";

 // Страница с сообщениями об ошибке
 private static final String ERROR_PAGE                   = "/error.jsp?prefix=../";

 // SECURED. Форма со списком отделов ГУР - для выбора получателей
 private static final String DEPTS_FORM                   = "/editor/deptsList.jsp";
 // SECURED. Форма для просмотра/редактирования одной служебки
 private static final String MEMO_FORM                    = "/editor/memoForm.jsp";
 // SECURED. Форма для просмотра/редактирования одной служебки
 private static final String MEMO_FORM_FORWARD            = "/editor/memoFormForward.jsp";
 // SECURED. Страница просмотра результата добавления служебки
 private static final String SAVE_MEMO_RESULT_PAGE        = "/editor/addMemoRes.jsp";
 // SECURED. Сервлет, который обрабатывает новую служебку - регистрирует ее
 private static final String NEW_MEMO_PROCESSING_PAGE     = "/editor/memoProcessing";
 // SECURED. Страница с формой для ответа на служебку
 private static final String ANSWER_APPOINTED_MEMO_PAGE   = "/editor/appointmentAnswer.jsp";
 // SECURED. Страница с результатом сохранения ответа на порученную служебку
 private static final String ANSWER_APPOINTED_MEMO_RESULT_PAGE   = "/editor/appointmentAnswerResult.jsp";

 /**
  * Непосредственная обработка поступившего запроса. Этот метод обрабатывает пришедший запрос и
  * возвращает ответ. В качестве параметров метод получает стандартные HTTP-потоки -
  * запрос(request) и ответ(response).
 */
 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
 {
      logger.debug("ENTERING editorController -> processRequest().");

      // Начальная страница по умолчанию
      String destPage = ERROR_PAGE;

      acc_logger.info("editorController [" + request.getRemoteUser() + "] [" + ACTION + "]");

     // todo: temporary solution!!!
      Set<DepartmentDto> deptsTmp = employee.getDepartments();

   // Переход на экран №1 добавления служебки - выбор отделов(-а)-получателей(-я).
   // (переход на страницу с формой выбора отделов ГУР)
   if (ADD_MEMO_STEP1.equals(this.ACTION)) {

       // Если получен верный идентификатор сотрудника - работаем дальше
       if (employee != null) {
           // Идентификатор сотрудника надо передать в следующий шаг
           request.setAttribute("memberID", employee.getId());

           // Передадим данные о пользователе в следующий шаг
           request.setAttribute("member", employee);

           // Если мы не создаем, а редактируем служебку - надо получить идентификатор редактируемой служебки, а затем передать его дальше
           int memoID = -1;
           try {
               memoID = Integer.parseInt(request.getParameter("memoID"));
           }
           catch (Exception e) {
               logger.debug("Edit memo mode active!");
           }
           request.setAttribute("memoID", memoID);

           // Если мы редактируем служебку - ее идентификатор занесем в лог
           if (memoID > 0)
               acc_logger.info("editorController [" + employee.getShortRusName() + "] [Edit memo: memoID=" + memoID + "]");

           // Сформируем список отделов ГУР
           request.setAttribute("deptsList", this.getDepartmentsDao().findAllActive());

           //Для пересылки служебки требуется парметр FORWARD, если он есть, то происходят действия для перенаправления служебки
           request.setAttribute("forward", request.getParameter("forward"));

           // Если это ответ на служебку - надо также передать номер родительской служебки.
           // Перед этим надо проверить - может ли данный пользователь ответить на эту служебку -
           // на служебку может ответить пользователь, имеющий права <EDITOR> и работающий в том же отделе,
           // что и отдел-отправитель данной служебки. В противном случае пользователь не имеет права
           // редактировать данную служебку.
           int parentID = -1;
           try {
               parentID = Integer.parseInt(request.getParameter("parentID"));
           } catch (Exception e) {
               logger.debug("No parent memo ID!");
           }
           if (parentID > 0) {
               // Найдем родительскую служебку
               memoDTO memo = dH.getMemoDAO().findByID(dH, parentID);
               if (memo != null) {

                   // Если мы отвечаем на служебку - занесем ее идентификатор в лог
                   acc_logger.info("editorController [" + employee.getShortRusName() + "] [Answer on memo: memoID=" + parentID + "]");

                   // Посмотрим: имеется ли данный отдел пользователя в списке получателей, чтоб отвечать смог только отдел,
                   // который находится в списке получателей. Метод вернет не NULL если отдел имеется в списке получателей

                   // todo: temporary solution!!!
                   //recipientDeptDTO recipientDept = dH.getRecipientDeptDAO().findByMemoDeptID(dH, (deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getId() : 0), parentID);

                   //if ((employee != null) && (recipientDept != null)) {
                       //request.setAttribute("parentID", parentID);
                       // При ответе на служебку пропускаем шаг выбора подразделения получателя
                   //    destPage = "/editor/controller?action=" + ADD_MEMO_STEP2 + "&memoID=" + memoID + "&parentID="
                   //            + parentID + "&memberID=" + employee.getId() + "&depts=" + memo.getExecutorDeptID();
                   //} else request.setAttribute(Defaults.ERROR_MSG_PARAM, "ВЫ НЕ МОЖЕТЕ ОТВЕТИТЬ НА ДАННУЮ СЛУЖЕБКУ!");
               } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "ВЫ НЕ МОЖЕТЕ ОТВЕТИТЬ НА ДАННУЮ СЛУЖЕБКУ!");
           } else destPage = DEPTS_FORM;
       } else {
           logger.error("Can't find registered user [" + employee.getShortRusName() + "].");
           request.setAttribute(Defaults.ERROR_MSG_PARAM, "Can't find registered user [" + employee.getShortRusName() + "].");
       }
   }

   // Переход на экран №2 добавления служебки - заполнение данных служебки.
   // (переход на страницу с формой добавления/редактирования служебки)
   else if (ADD_MEMO_STEP2.equals(ACTION)) {

       // Создаем объект "служебка"
       memoDTO memo = new memoDTO();

       // Если есть сведения о пользователе - работаем
       if (employee != null) {
           // В пустую служебку добавим уже имеющуюся информацию - сотрудник-исполнитель
           memo.setExecutorUserID(employee.getId());
           memo.setExecutorShortName(employee.getShortRusName());

           // todo: temporary solution!!!
           memo.setExecutorDeptID(deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getId() : 0);
           memo.setExecutorDeptCode((deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getCode() : null));

           // Также добавим в служебку идентификатор родительской служебки
           int parentID = -1;
           try {
               parentID = Integer.parseInt(request.getParameter("parentID"));
           }
           catch (Exception e) {logger.debug("Not parentID Parameter");}
           logger.debug("Recieved parentID value = " + parentID);
           
           if (parentID > 0) {
               // Найдем объект "родительская служебка"
               memoDTO parentMemo = dH.getMemoDAO().findByID(dH, parentID);
               memo.setParentID(parentID);
               //Заменим тэги <br> на пробелы, для <textarea>
               parentMemo.setText(parentMemo.getText().replaceAll("<br>", ""));
               request.setAttribute("parentMemo", parentMemo);
           }

           // В пустую служебку добавим уже имеющуюся информацию - список отделов-получателей
           String[] depts = request.getParameterValues("depts");

           // Если список отделов-получателей не пуст - работаем далее
           if (depts != null) {

               depts = dH.getRecipientDeptDAO().checkDepartment(depts);
               
               List deptList = dH.getRecipientDeptDAO().findDeptID(dH, depts);
               if (deptList.size() == 1) {
                   request.setAttribute("one_only", "one_only");
               }
               memo.setRecipientsDepts(deptList);
               // Передадим объект "служебка" на следующий этап обработки
               request.setAttribute("memo", memo);
               // Установим страницу назначения для перенаправления запроса
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

    // Переход на экран №2 добавления служебки - заполнение данных служебки.
    // (переход на страницу с формой добавления/редактирования служебки)
    else if (ADD_MEMO_STEP2FORWARD.equals(ACTION)) {

       logger.debug(ADD_MEMO_STEP2FORWARD);
       // Идентификатор СЗ которую пересылаем в другое подразделение
       int memoID = -1;
       try {
           memoID = Integer.parseInt(request.getParameter("memoID"));
       }
       catch (Exception e) {
           logger.error("Invalid memoID value!");
       }

       // Если мы получили правильный идентификатор сотрудника и СЗ - работаем дальше
       if (employee != null && memoID > 0) {
           // Найдем объект служебка, которую пересылаем и на которую делаем ответ
           memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);

           // В служебку добавим уже имеющуюся информацию - сотрудник-исполнитель
           memo.setExecutorUserID(employee.getId());
           memo.setExecutorShortName(employee.getShortRusName());

           // todo: temporary solution!!!
           memo.setExecutorDeptID(deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getId() : 0);
           memo.setExecutorDeptCode((deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getCode() : null));

           // В служебку добавим информацию - список отделов-получателей
           String[] depts = request.getParameterValues("depts");
           // Если список отделов-получателей не пуст - работаем далее
           if (depts != null) {
               List deptList = dH.getRecipientDeptDAO().findDeptID(dH, depts);
               if (deptList.size() == 1) {
                   request.setAttribute("one_only", "one_only");
               }
               memo.setRecipientsDepts(deptList);
               // Передадим объект "служебка" на следующий этап обработки
               request.setAttribute("memo", memo);
               // Установим страницу назначения для перенаправления запроса
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

   // Сохранение результатов добавления/редактирования служебки
   else if (SAVE_MEMO_ACTION.equals(ACTION)) {
       // Идентификатор измененной/добавленной служебки
       int memoID = -1;       
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("No memoID parameter!");}
       // Передаем параметр говорящий, что служебка была отредактирована, если id>0
       if (memoID > 0){request.setAttribute("edit", "edit");}
       
       destPage = NEW_MEMO_PROCESSING_PAGE;}

   // Сохранение результатов ответа на служебную записку при ее переадресации   
   else if (SAVE_MEMO_FORWARD_ACTION.equals(ACTION)) {
       // Идентификатор измененной/добавленной служебки
       int memoID = -1;
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("No memoID parameter!");}

       String email;
       // Eсли идентификатор СЗ приняли работаем
       if (memoID > 0){
           //Найдем СЗ на которую идет ответ, и которую пересылаем другим подразделениям
           memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);

           //Получим список подразделений, которым переадресуется служебка
           String[] depts = request.getParameterValues("recipient");
           List   emailsList = null;

           //Получим примечания от подразделения, которое пересылает данную служебную записку
           String prim = request.getParameter("prim");

           if (depts != null){

               //Проходимся по списку подразделений, формируем записи в таблице recipientDept (получатели СЗ),
               //а так же формируем уведомления о переадресации им служебной записки

               List deptList = dH.getRecipientDeptDAO().findDeptID(dH, depts);
               for (int u = 0; u < deptList.size(); u++){

                   //Дозаполним поля и добавляем получателей служебной записки
                   //recipientDeptDTO r = (recipientDeptDTO) deptList.get(u);
                   //r.setMemoID(memoID);
                   //r.setUpdateUserID(employee.getId());
                   try{
                       //Добавляем подразделение в список получателей служебки
                       //dH.getRecipientDeptDAO().create(dH, r);
                       
                       // В начале смотрим таблицу Mapping и получаем список его эл. адресов
                       // todo: get emails for department and send emails
                       //List recipientEmails = dH.getDeptsEmailsMappingDAO().findDeptId(dH, r.getRecipientDeptID());
                       List recipientEmails = new ArrayList();
                       // Если список не пуст, то отправляем только по этим эл. адресам
                       /*
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
                       */

                   }catch (Exception e){
                       //logger.error("Can't create recipientDept = " + r.getRecipientDeptCode());
                       //request.setAttribute(Defaults.ERROR_MSG_PARAM, "Can't create recipientDept = " + r.getRecipientDeptCode());}
               }

               // Если в списке есть хотя бы один адрес - работаем(отправляем письма по указанным адресам)
               if (emailsList != null && emailsList.size() > 0){
                   // Непосредственно отправка уведомления о переадресации служебно записки - создание объекта для работы с почтой
                   JMailConfig JMConfig = new JMailConfig();
                   // Обратный адрес системы служебок
                   JMConfig.setFrom(Defaults.MAIL_FROM);
                   // SMTP-сервер для отправки почты
                   JMConfig.setMailHost(Defaults.MAIL_HOST);

                   // Тема письма
                   // todo: temporary solution!!!
                   JMConfig.setSubject("Переадресация СЗ № " + memo.getMemoNumber() + " от " + employee.getShortRusName()
                           + " (" + (deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getCode() : null) + ")");

                   // Текст письма
                   // todo: temporary solution!!!
                   String msgText = "Вам была переадресована служебная записка №" + memo.getMemoNumber()
                           + " от " +  employee.getShortRusName() + " (" + (deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getCode() : null) + ")" + "\n";

                   msgText = msgText + "Комментарий подразделения, переадресовавший служебную записку: " + "\n";
                   msgText = msgText + prim + "\n" + "\n";

                   msgText = msgText + "Касательно: " + memo.getSubject() + "\n" + "\n";
                   msgText = msgText + "Содержание: ";
                   if (memo.getText().trim().length() > 200){
                       msgText = msgText + (memo.getText().trim().substring(0,200)).replaceAll("<br>", "") + "\n" + "\n";}
                   else{msgText = msgText + memo.getText().trim().replaceAll("<br>", "") + "\n" + "\n";}
                   
                   msgText = msgText + "Ознакомиться с ней можно по ссылке: \n";
                   msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&boxType=inbox&memoID=" + memo.getId();
                   JMConfig.setText(msgText);

                   // В цикле проходим по списку эл. адресов и отправляем письма
                   for (Object aObject : emailsList){
                       // Получение адреса
                       email = (String) aObject;
                       // Установка его в поле "адресат"
                       JMConfig.setTo(email);

                       JMail mail = new JMail(JMConfig);

                       // Отправка уведомления
                       try {
                           mail.send();
                           acc_logger.info("editorController [" + employee.getShortRusName() + "] [send to " + email + "]");
                       }
                       catch (Exception e) {logger.error("Cannot send email to [" + email + "]");}
                   }
                   //Переход на страницу результата переадресации документа
                   request.setAttribute("memoID", memoID);
                   request.setAttribute("memo", memo);
                   request.setAttribute("forward", "forward");
                   destPage = SAVE_MEMO_RESULT_PAGE;
               }
           }//else{
            //   logger.error("DEPTS list is EMPTY!"); request.setAttribute(Defaults.ERROR_MSG_PARAM, "DEPTS list is EMPTY!");
           //}
       }else{
           request.setAttribute(Defaults.ERROR_MSG_PARAM, "memoId parameter is invalid!");
       }
   }

   // Удаление даты "Срок ответа"
   else if (DEL_REALIZEDDATE_ACTION.equals(ACTION)) {
       // Идентификатор измененной служебки
       //int memoID = -1;
       try {
           memoID = Integer.parseInt(request.getParameter("memoID"));
       }
       catch (Exception e) {
           logger.error("No memoID parameter!");
       }

       // Если имеется идентификатор служебки и пользователь входит в группу Memo_chief, то продолжаем работу
       // на всякий пожарный можно добавить проверку, что чел начальник отдела подготовившего служебку(поку нету)
       if (memoID > 0 && request.isUserInRole("memo_chief")) {
           //Найдем служебку
           memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);
           //Обнулим поле "Срок ответа"
           memo.setRealizedDate("");
           
           try {
               // Непосредственно сам UPDATE, установка даты в NULL
               dH.getMemoDAO().update(dH, memo, null);
           }
           catch (Exception e) {
               logger.error("Cannot set RealizedDate IN NULL");
           }
           // После выполнения UPDATE, переход на страничку с сообщением о выполненных дествиях
           destPage = "/editor/controller?action=saveMemoRes&memoID=" + memoID + "&edit=edit";
       }
   }

   // Страничка с результатом создания/редактирования служебки
   else if (SAVE_MEMO_RESULT.equals(ACTION)) {
       // Принемаем и передаем аттрибут EDIT, если он есть, то служебка была отредактирована
       // EDIT может передавтся как в виде параметра, так и в виде аттрибута
       if (request.getParameter("edit") != null || request.getAttribute("edit") != null) {
           request.setAttribute("edit", "edit");
       }

       // Передаем на страничку результата переменную с ошибками
       request.setAttribute("error", request.getAttribute("error"));
       // Также передадим на старничку с результатом идентификатор созданной/измененной служебки
       //int memoID = -1;
       try {
           memoID = Integer.parseInt(request.getParameter("memoID"));
       }
       catch (Exception e) {
           logger.error("No memoID parameter!");
       }

       memoDTO memo = null;
       // Найдем служебку
       if (memoID > 0) {
           memo = dH.getMemoDAO().findByID(dH, memoID);
       }

       request.setAttribute("memoID", memoID);
       request.setAttribute("memo", memo);
       destPage = SAVE_MEMO_RESULT_PAGE;
   }

   // Редактирование служебки (уже существующей)
   else if (EDIT_MEMO_ACTION.equals(ACTION)) {
       //int memoID = 0;
       // Получаем иеднтификатор редактируемой служебки
       try {
           memoID = Integer.parseInt(request.getParameter("memoID"));
       }
       catch (Exception e) {
           logger.error("Invalid memoID [" + memoID + "] parameter!");
       }
       // Теперь найдем служебку, которую мы будем редактировать
       memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);
       if (memo == null) logger.error("Memo with ID = " + memoID + " doesn't exists!");
       // Идентификатор пользователя-создателя служебки (из БД "Кадры")
       long executorUserID = -1;
       if (memo != null) {
           executorUserID = memo.getExecutorUserID();
       }
       if (executorUserID < 0) logger.error("ERROR: executorUserID < 0!");
       // Идентификатор отдела пользователя-создателя служебки (из БД "Кадры")
       long executorDeptID = -1;
       if (memo != null) {
           executorDeptID = memo.getExecutorDeptID();
       }
       if (executorDeptID < 0) logger.error("ERROR: executorDeptID < 0!");

       // Редактировать служебку может или ее создатель или начальник отдела(Пользователь с правами memo_chief),
       // в котором числится создатель служебки - проверяем.
       // Также служебку нельзя редактировать, если она уже отправлена - надо проверить поле SendDate - оно должно быть пустым.
       if ((memo != null) && (employee.getId() > 0) && (executorUserID > 0) && (memo.getSendDate() == null) &&
               ((employee.getId() == executorUserID) || request.isUserInRole("memo_chief"))) {
           //Найдем объект "родительская служебка", если таковая есть
           if (memo.getParentID() > 0) {
               memoDTO parentMemo = dH.getMemoDAO().findByID(dH, memo.getParentID());
               //Заменим тэги <br> на пробелы, для <textarea>
               parentMemo.setText(parentMemo.getText().replaceAll("<br>", ""));
               request.setAttribute("parentMemo", parentMemo);
           }

           //Заменим тэги <br> на пробелы, для <textarea>
           memo.setText(memo.getText().replaceAll("<br>", ""));
           // Если мы получили список адресатов - меняем оригинальный список на полученный
           String[] depts = request.getParameterValues("depts");
           if (depts != null) memo.setRecipientsDepts(dH.getRecipientDeptDAO().findByDeptIDList(dH, depts));
           // Передаем на страницу редактирования сам объект "служебка"
           request.setAttribute("memo", memo);
           // Устанавливаем нужную страницу редактирования
           destPage = MEMO_FORM;
       } else
           request.setAttribute("errorMsg", "РЕДАКТИРОВАНИЕ СЛУЖЕБНОЙ ЗАПИСКИ НЕВОЗМОЖНО!");
   }

   // Ответ на порученную служебку. Пользователь может ответить только на то поручение, которое
   // адресовано именно ему. Если текущего пользователя нет среди получателей служебки - он ответить
   // на нее не сможет! Теперь сможет, но только в пределах подразделения!
   else if (ANSWER_APPOINTED_MEMO_ACTION.equals(ACTION)) {
       // Идентификатор порученной служебки
       //int memoID = -1;
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("editiorController: Can't recieve memoID parameter!");}

       // Идентификатор записи получателя, которому была поручена служебка
       int id        = -1;
       try {id       = Integer.parseInt(request.getParameter("id"));}
       catch (Exception e) {logger.error("Can't recieve ID parameter!");}

       // Объект "служебка"
       memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);

       // Объект "сотрудник, которому поручена данная служебка", по идентификатору
       //recipientUserDTO recipient = null;
       //if (id > 0) {
       //    recipient = dH.getRecipientUserDAO().findByID(dH, id);
       //}

       // Проверим: является ли текущий пользователь(memberDTO member) сотрудником подразделения, к которому относится
       // чел. кому шеф поручил служебку. Сделано чтоб внутри подразделения сотрудники могли выполнять СЗ друг за друга.
       // Если все проверки завершились удачно - текущий пользователь может ответить на поручение (служебка ему поручена)
       // todo: temporary solution!!!
       //if ((recipient != null) && ((deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getId() : 0) == recipient.getRecipientDeptID())) {

           // Передаем объект "служебка"
           request.setAttribute("memo", memo);
           // Передаем объект "пользователь-получатель"
           //request.setAttribute("recipient", recipient);
           // Страница с формой для ответа на порученную служебку
           destPage = ANSWER_APPOINTED_MEMO_PAGE;
       } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "Вы не можете ответить на данное поручение!");
   }

   // Сохранение ответа на порученную служебку. Так же запрос может прийти при ответе на служебку,в этом случае
   // требуется автоматом челу проставить что он выполнил поручение (если такое есть) и перейти на страницу
   // которая отображает результат добавления новой служебки
   else if (ANSWER_APPOINTED_MEMO_RESULT_ACTION.equals(ACTION))
    {
     // Идентификатор новой созданной служебки, если он есть значит требуестя автоматом поставить челу отметку
     // о выполнении поручения
     int newMemoID        = -1;
     try {newMemoID       = Integer.parseInt(request.getParameter("newMemoID"));}
     catch (Exception e) {logger.debug("Can't recieve newMemoID parameter!");}

     // Идентификатор порученной служебки
     int memoID        = -1;
     try {memoID       = Integer.parseInt(request.getParameter("memoID"));}
     catch (Exception e) {logger.error("editiorController: Can't recieve memoID parameter! newMemoID = " + newMemoID);}

     // Идентификатор записи получателя, которому была поручена служебка
     int id        = -1;
     try {id       = Integer.parseInt(request.getParameter("id"));}
     catch (Exception e) {logger.debug("Can't recieve ID parameter! memoid = " + memoID);}

     // Идентификатор получателя, которому была поручена служебка
     // параметр приходит при ответе на служебку для автоотметки о выполнении поручения   
     int recipientUserID        = -1;
     try {recipientUserID       = Integer.parseInt(request.getParameter("recipientUserID"));}
     catch (Exception e) {logger.debug("Can't recieve recipientUserID parameter! memoid = " + memoID);}
        
     // Объект "служебка"
     memoDTO memo      = dH.getMemoDAO().findByID(dH, memoID);

     // Объект "сотрудник, которому была поручена данная служебка"
     //recipientUserDTO recipient = null;
     if (id > 0 && memoID > 0){
         //Найдем поручение по идентификатору, точно знаем какое будет
         //recipient = dH.getRecipientUserDAO().findByID(dH, id);
     }else if(recipientUserID > 0 && memoID > 0){
         //Найдем первое не выполненное поручение
         //recipient = dH.getRecipientUserDAO().findByMemoDeptID(dH, recipientUserID, memoID, 0);
     }

     // Текст ответа на порученную служебку
     String answer = request.getParameter("answer");

     // Если все проверки завершились удачно - текущий пользователь может ответить на поручение,
     // тогда сохраняем текст его ответа, устанавливаем флажок "выполнено" и отправляем уведомление о выполнении
     // начальнику подразделения и тому, кто поручил данную служебку
     if ((answer != null) && (!answer.trim().equals("")) /*&& (recipient != null)*/) {

         // Создаем объект "пользователь, которому поручена служебка" и заносим в него данные
         //recipientUserDTO recipientUpdate = new recipientUserDTO();
         //recipientUpdate.setId(recipient.getId());
         //recipientUpdate.setRealized(1);
         // Добавим к тексту ответа ФИО пользователя, для контроля при ответе другим сотрудником отдела
         //recipientUpdate.setAnswer(answer + "(исп. " + employee.getShortRusName() + ")");
         // Непосредственно сохранение результата ответа на служебку
         try {
             //dH.getRecipientUserDAO().update(dH, recipientUpdate);
         }
         catch (Exception e) {
             logger.error("Can't save appoint memo answer result!");
         }
         
         // Отправка почтового уведомления (начальнику подразделения или поручителю), если
         // при создании поручения был указан признак получения уведомления по E-mail
         if (/*recipient.getSendemail() == 1*/ false) {
             // Найдем чела поручившего служебку и получаем основной эл. адрес пользователя из БД LDAP
             // у поручившего может буть несколько логинов с основными email, получим их, и пройдемся по списку
             // todo: get emails for notifying
             //List appointedUserEmails = (List)dH.getConnectorSource().getSomething(dH, recipient.getAppointedUserID(), "getUserEmailByPersonnelId");
             List appointedUserEmails = new ArrayList();

             if (appointedUserEmails != null) {

                 //Заполнение основных параметров сообщения
                 JMailConfig JMConfig = new JMailConfig();

                 JMConfig.setFrom(Defaults.MAIL_FROM);
                 JMConfig.setMailHost(Defaults.MAIL_HOST);
                 JMConfig.setSubject("Выполнение СЛУЖЕБНОЙ ЗАПИСКИ");
                 String msgText = "Служебная записка № " + memo.getMemoNumber() + " выполнена: " + employee.getShortRusName() + ".\n";
                 msgText = msgText + "КОММЕНТАРИЙ: " + answer + "\n";
                 msgText = msgText + "Ознакомиться со служебной запиской можно по ссылке: \n";
                 msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&memoID=" + memo.getId() + "&boxType=inbox&deptID=" + memo.getExecutorDeptID();

                 // если пришел запрос при создании новой СЗ (newMemoID >0) и требуется автоматом ответить на поручение,
                 // а так же, отправить уведомление шефу по e-mail, то меняем текст сообщения
                 if (newMemoID > 0) {
                     msgText = "Подготовлен ответ на служебную записку № " + memo.getMemoNumber() + "\n";
                     msgText = msgText + "Касательно: " + memo.getSubject() + "\n";
                     msgText = msgText + "Выполнил: " + employee.getShortRusName() + "\n";
                     msgText = msgText + "Ознакомиться можно по ссылке: \n";

                     // todo: temporary solution!!!
                     msgText = msgText + "http://gur.rs-head.spb.ru/jdbc/memorandum/controller?action=viewMemo&memoID=" + newMemoID + "&boxType=outbox&deptID=" +
                             (deptsTmp != null && deptsTmp.size() > 0 ? deptsTmp.iterator().next().getId() : 0);
                 }

                 JMConfig.setText(msgText);

                 //Пройдемся по списку email пользователя recipient.getAppointedUserID() и отправим письмо
                 for (int i = 0; i < appointedUserEmails.size(); i++){
                     LogicUserDto logicUser = (LogicUserDto)appointedUserEmails.get(i);

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
         // Объект "служебка"
         request.setAttribute("memo", memo);
         // Страничка с результатом сохранения ответа на порученную служебку
         destPage = ANSWER_APPOINTED_MEMO_RESULT_PAGE;

     } else request.setAttribute(Defaults.ERROR_MSG_PARAM, "Вы не можете ответить на данное поручение!");

        // Проверим, если запрос пришел при создании служебки, то перенаправляем на страничку успешной регистрации новой СЗ
        if (newMemoID > 0) {
            request.setAttribute(Defaults.ERROR_MSG_PARAM, "");
            memoDTO memonew = null;
            // Найдем служебку
            if (memoID > 0) {
                memonew = dH.getMemoDAO().findByID(dH, newMemoID);
            }

            request.setAttribute("memoID", newMemoID);
            request.setAttribute("memo", memonew);
            destPage = SAVE_MEMO_RESULT_PAGE;
        }
    }

   // Если не подошел ни один из типов действий, реализуемых контроллером
   else request.setAttribute(Defaults.ERROR_MSG_PARAM, "НЕВЕРНЫЙ ТИП ДЕЙСТВИЯ: [" + ACTION + "]!");

   // Отладочный вывод
   logger.debug("ACTION: " + ACTION); logger.debug("DEST PAGE: " + destPage);

   // Получаем диспетчер данного запроса и перенаправляем запрос
   RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
   // Непосредственно перенаправление
   dispatcher.forward(request, response);

   logger.debug("LEAVING editorController -> processRequest().");
  }

}