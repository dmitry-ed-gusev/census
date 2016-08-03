package org.memorandum.controllers;

import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dao.simple.docs.Defaults;
import org.census.commons.dto.docs.memoDTO;
//import org.census.commons.dto.docs.recipientDeptDTO;
import org.census.commons.utils.JLibUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.census.commons.utils.JLibUtils.DatePeriod;

/**
 * Класс реализует основной контроллер приложения "Служебные записки". Действия, реализуемые данным контроллером
 * являются публичными - незащищенными.
*/

// todo: class contains temporary solution with getting department id/code from employee!!!

public class Controller extends AbstractController
{

 // Тип действия: просмотр одной служебки
 private static final String MEMO_VIEW_ACTION      = "viewMemo";
 // Тип действия: вывод списка отделов ГУР для выбора ящика служебок
 private static final String DEPTS_ACTION          = "depts";
 // Тип действия: отображение ящика служебных записок
 private static final String DEPT_BOX_ACTION       = "deptBox";
 // Тип действия: отчет по не выполненным служебкам внутри подразделения
 private static final String DEPT_NOTMAKE_ACTION   = "notMake";
 // Тип действия: переход на форму поиска служебки по атрибутам
 private static final String MEMO_SEARCH_ACTION    = "search";
 // Тип действия: непосредственно поиск служебки
 private static final String MEMO_SEARCH_DO_ACTION = "searchDo";
 // Тип действия: форма для печати служебки
 private static final String MEMO_PRINT_ACTION     = "printMemo";

 // Страница(адрес) контроллера
 private static final String CONTROLLER_PAGE          = "/controller";
 // Страница с сообщениями об ошибке
 private static final String ERROR_PAGE               = "/error.jsp";
 // Страница просмотра одной служебки
 private static final String MEMO_VIEW_PAGE           = "/memoView.jsp";
 // Страница со списком отделов ГУР для выбора ящика служебок
 private static final String DEPTS_PAGE               = "/depts.jsp";
 // Страница с ящиком служебных записок данного отдела
 private static final String DEPT_BOX_PAGE            = "/deptBox.jsp";
 // Страница с формой поиска служебки
 private static final String MEMO_SEARCH_PAGE         = "/search.jsp";
 // Страница с формой для печати служебки
 private static final String MEMO_PRINT_PAGE          = "/printForm.jsp";

 /**
  * Непосредственная обработка поступившего запроса.
  * @param request HttpServletRequest http-запрос
  * @param response HttpServletResponse http-ответ
  * @throws ServletException ошибка в работе сервлета
  * @throws IOException ошибка ввода/вывода
 */
 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
   logger.debug("ENTERING controller -> processRequest().");
      
   // По умолчанию страница с ошибкой
   String destPage      = ERROR_PAGE;

   // Передаем все аттрибуты пользователя в JSP
   request.setAttribute("member", employee);

      Set<DepartmentDto> depts = employee.getDepartments();

   // Страница со списком всех отделов ГУР
   if(DEPTS_ACTION.equals(ACTION)){
        //Если пользователь с правами memo_superuser, то он может просматривать СЗ других подразделений
        if (request.isUserInRole("memo_superuser")){
            request.setAttribute("deptsList", this.getDepartmentsDao().findAllActive());
            destPage = DEPTS_PAGE;
        }
   }

   // Вход в ящик со служебками данного отдела ("входящие"/"исходящие")
   else if (DEPT_BOX_ACTION.equals(ACTION)) {

       // Переменная для хранения полученного списка служебок
       List memosList;

       // Получим идентификатор отдела, для которого отображаем ящик
       long deptID = -1;
       try {deptID = Integer.parseInt(request.getParameter("deptID"));}
       catch (Exception e) {logger.debug("Invalid deptID parameter!");}

       //Параметр подразделения приходит пустым только при входе в систему,
       //поэтому открываем сразу вх. ящик подразделения пользователя
       if(deptID < 0){
           deptID = (depts != null && depts.size() > 0 ? depts.iterator().next().getId() : 0);
           logger.debug("deptId employee = " + deptID);
       }

       //Просмотр документов других подразделений имеют только пользователи с правами memo_superuser
       if ((depts != null && depts.size() > 0 ? depts.iterator().next().getId() : 0) != deptID && !request.isUserInRole("memo_superuser")){
           deptID = (depts != null && depts.size() > 0 ? depts.iterator().next().getId() : 0);
       }

       // Если идентификатор отдела в порядке - передадим код отдела
       if (deptID > 0){
           DepartmentDto dept = this.getDepartmentsDao().findById(deptID);
           request.setAttribute("deptCode", dept.getCode());
       }
       // Получим тип ящика (входящие/исходящие). Если тип ящика не указан - ящиком по умолчанию является ящик "исходящие"
       String boxType = request.getParameter("boxType");
       // Получим номер страницы для отображения. Если номер не указан - отображаем первую страницу
       int pageNumber = 1;
       try {pageNumber = Integer.parseInt(request.getParameter("pageNumber"));}
       catch (Exception e) {logger.debug("NOT pageNumber parametr");}
       
       // Если идентификатор нас устроил - переходим на страницу ящика,
       // если же нет - переходим опять к списку отделов

       // Получим подтип ящика, если таковой есть
       String boxSubType = request.getParameter("boxSubType");

       if (deptID > 0) {
           // Номер текущей страницы
           request.setAttribute("pageNumber", pageNumber);
           // В зависимости от типа ящика отображаем разные списки служебок
           if ((boxType != null) && (boxType.trim().equals(Defaults.OUTBOX))) {
               // Получение служебок из ящика "исходящие"
               if ((boxSubType != null) && (boxSubType.trim().equals(Defaults.OUTBOX_NO_ANSWER))) {
                   logger.debug("subType for outbox: NO_ANSWER.");
                   memosList = dH.getMemoDAO().box(dH, Defaults.OUTBOX, Defaults.OUTBOX_NO_ANSWER, deptID, Defaults.STATUS_UNDELETED, pageNumber);
               } else {
                   logger.debug("subType for outbox: ALL.");
                   memosList = dH.getMemoDAO().box(dH, Defaults.OUTBOX, Defaults.OUTBOX_ALL, deptID, Defaults.STATUS_UNDELETED, pageNumber);
               }
               // Тип ящика - "исходящие"
               request.setAttribute("boxType", Defaults.OUTBOX);
               // Непосредственно записки из ящика "исходящие"
               request.setAttribute("memosList", memosList);
               // Общее кол-во страниц в ящике "исходящие"
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
               // Список выводимых страниц
               int[] pages = new int[outboxPagesCount];
               for (int i = 0; i < pages.length; i++) pages[i] = i + 1;
               request.setAttribute("pages", pages);
           } else {
               // Тип ящика - "входящие"
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
               // Непосредственно записки из ящика "входящие"
               request.setAttribute("memosList", memosList);
               // Общее кол-во страниц в ящике "входящие"
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
               // Список выводимых страниц
               int[] pages = new int[inboxPagesCount];
               for (int i = 0; i < pages.length; i++) pages[i] = i + 1;
               request.setAttribute("pages", pages);
           }

           // Передадим также идентификатор отдела, ящик которого мы отображаем
           request.setAttribute("deptID", deptID);
           // Передадим подтип ящика
           request.setAttribute("boxSubType", boxSubType);

           destPage = DEPT_BOX_PAGE;
       } else destPage = CONTROLLER_PAGE + DEPTS_PAGE;
   }

   // Просмотр одной служебки
   else if (MEMO_VIEW_ACTION.equals(ACTION)) {
       
       // Передадим параметр ViewType, для того чтоб имелась возможность просмотра всех исполнителей служебки,
       // вызов осуществляется из Search
       request.setAttribute("ViewType", request.getParameter("ViewType"));
       //Передадим тип ящика из которого сделан вызов просмотра служебки
       request.setAttribute("boxType", request.getParameter("boxType"));
       // Передадим также идентификатор отдела, для того чтоб отражать кому поручено (сотрудникам только данного подразделения)
       request.setAttribute("deptID", request.getParameter("deptID"));

       // Получаем номер служебки для просмотра
       int memoID = -1;
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("Invalid memoID [" + memoID + "]!");}

       // Если если служебка с таким идентификатором существует - выводим ее
       memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);

       if (memo != null) {
           // Если данная служебка - ответ на другую, выведем номер родительской служебки
           memoDTO parentMemo = null;
           if (memo.getParentID() > 0) parentMemo = dH.getMemoDAO().findByID(dH, memo.getParentID());
           if (parentMemo != null) {
               request.setAttribute("parentMemoNumber", parentMemo.getMemoNumber());
           }
           // найдем подразделения ответившие на эту служебку, если таковы есть
           List recipientDept = dH.getRecipientDeptDAO().findDeptsRealizedMemo(dH, memoID, 1);
           // Передаем список ответивших в JSP
           request.setAttribute("recipientDept", recipientDept);
           request.setAttribute("memo", memo);
           destPage = MEMO_VIEW_PAGE;
       } else {
           request.setAttribute(Defaults.ERROR_MSG_PARAM, "СЛУЖЕБНАЯ ЗАПИСКА НЕ СУЩЕСТВУЕТ!");
       }
   }

   // Переход на форму для поиска служебки по атрибутам
   else if (MEMO_SEARCH_ACTION.equals(ACTION)) {

       // Форматируем дату конца интервала в строку в нужном формате            
       String date_end_default = JLibUtils.dateToPattern(new Date(), Defaults.DATE_PATTERN, null);
       // Делаем сдвиг в месяцах назад - нижняя граница диапазона поиска (начало интервала)
       Date dat = JLibUtils.dateToPeriod(new Date(), -Defaults.SEARCH_INTERVAL, DatePeriod.MONTH);
       // Форматируем дату начала интервала в строку в нужном формате
       String date_start_default = JLibUtils.dateToPattern(dat, Defaults.DATE_PATTERN,null);
       // Передаем нижнюю границу диапазона поиска на страницу поиска
       request.setAttribute("date_start_default", date_start_default);
       // Передаем верхнюю границу диапазона поиска на страницу поиска
       request.setAttribute("date_end_default", date_end_default);

       //Cписок сотрудников передаем
       request.setAttribute("memberList", this.getEmployeesDao().findAllActive());
       //Список подразделений
       request.setAttribute("departmentList", this.getDepartmentsDao().findAllActive());

       destPage = MEMO_SEARCH_PAGE;
   }
   // Непосредственно поиск
   else if (MEMO_SEARCH_DO_ACTION.equals(ACTION)) {
       // Форматируем дату конца интервала в строку в нужном формате
       String date_end_default = JLibUtils.dateToPattern(new Date(), Defaults.DATE_PATTERN,null);
       // Делаем сдвиг в месяцах назад - нижняя граница диапазона поиска (начало интервала)
       Date dat = JLibUtils.dateToPeriod(new Date(), -Defaults.SEARCH_INTERVAL, DatePeriod.MONTH);
       // Форматируем дату начала интервала в строку в нужном формате
       String date_start_default = JLibUtils.dateToPattern(dat, Defaults.DATE_PATTERN,null);
       // Передаем нижнюю границу диапазона поиска на страницу поиска
       request.setAttribute("date_start_default", date_start_default);
       // Передаем верхнюю границу диапазона поиска на страницу поиска
       request.setAttribute("date_end_default", date_end_default);
       //Cписок сотрудников передаем
       request.setAttribute("memberList", this.getEmployeesDao().findAll());
       //Список подразделений
       request.setAttribute("departmentList", this.getDepartmentsDao().findAllActive());

       // Список найденных служебок
       List memoList;
       // Кол-во найденных служебок
       int memosCount = 0;

       // Номер служебки для поиска
       int memoNumber = 0;
       try {memoNumber = Integer.parseInt(request.getParameter("memoNumber"));}
       catch (Exception e) {logger.debug("Invalid memoNumber parameter!");}
       // Тема служебки для поиска
       String subject = request.getParameter("subject");
       // Дата - начало интервала времени создания служебки
       String date_start = request.getParameter("date_start");
       // Дата - конец интервала времени создания служебки
       String date_end = request.getParameter("date_end");
       // Отдел-отправитель служебки
       int executorDeptId = -1;
       try {executorDeptId = Integer.parseInt(request.getParameter("executorDeptCode"));}
       catch (Exception e) {logger.debug("NOT executorDeptCode parameter!");}

       // Отдел-получатель служебки
       int recipientDeptId = -1;
       try {recipientDeptId = Integer.parseInt(request.getParameter("recipientDeptCode"));}
       catch (Exception e) {logger.debug("NOT recipientDeptCode parameter!");}

       // Исполнитель записки
       int executorUserId = -1;
       try {executorUserId = Integer.parseInt(request.getParameter("executorUserId"));}
       catch (Exception e) {logger.debug("NOT executorUserId parameter!");}

       // Непосредственное выполнение поиска служебок
       memoList = dH.getMemoDAO().find(dH, memoNumber, subject, date_start, date_end, executorDeptId,
               recipientDeptId, executorUserId);

       // Установка атрибутов для страницы назначения
       request.setAttribute("memoList", memoList);
       if (memoList != null) memosCount = memoList.size();
       request.setAttribute("memosCount", memosCount);
       destPage = MEMO_SEARCH_PAGE;
   }
   // Печатная форма служебки
   else if (MEMO_PRINT_ACTION.equals(ACTION)) {

       // Пытаемся получить идентификатор служебки для печати
       int memoID = 0;
       try {memoID = Integer.parseInt(request.getParameter("memoID"));}
       catch (Exception e) {logger.error("Invalid memoID parameter [" + memoID + "] !");}
       // Если мы получили верный параметр - работаем
       if (memoID > 0) {
           // Передадим в форму печати объект "служебка"
           memoDTO memo = dH.getMemoDAO().findByID(dH, memoID);
           // Посмотрим: получатель - подразделение или сектор
           if (memo.getRecipientsDepts().size() > 1) {
               request.setAttribute("boss", "Начальникам отделов: ");
           } else {
               //recipientDeptDTO recipientDept = (recipientDeptDTO) memo.getRecipientsDepts().get(0);

               //if (recipientDept.getRecipientDeptName().lastIndexOf("Сектор") > -1) {
               //    request.setAttribute("boss", "Начальнику сектора ");
               //} else {
               //    request.setAttribute("boss", "Начальнику отдела ");
               //}
           }
           request.setAttribute("memo", memo);

           destPage = MEMO_PRINT_PAGE;
       } else destPage = CONTROLLER_PAGE + "?action=depts";
   }
   else if (DEPT_NOTMAKE_ACTION.equals(ACTION)) {

       long personnelId = 0;
       //Получаем тип отчета
       String type = request.getParameter("type");
       //Если personnel - выборка невыполненные в срок поручения пользователя, иначе всего подразделения
       if(type != null && type.equals("personnel")){
           personnelId = employee.getId();
       }

       // Передадим код отдела
       request.setAttribute("deptCode", (depts != null && depts.size() > 0 ? depts.iterator().next().getCode() : null));
       // Поиск служебок на которые нет ответа, адресованных для выполнения сотрудникам подразделения
       request.setAttribute("memosList", dH.getRecipientUserDAO().NotMake(dH, (depts != null && depts.size() > 0 ? depts.iterator().next().getId() : 0), personnelId));
       // Передадим идентификатор отдела, ящик которого мы отображаем
       request.setAttribute("deptID", (depts != null && depts.size() > 0 ? depts.iterator().next().getId() : 0));
       // Передадим подтип ящика
       request.setAttribute("boxType", Defaults.INBOX);

       destPage = DEPT_BOX_PAGE;
   }
   
   logger.debug("ACTION: " + ACTION); logger.debug("DEST PAGE: " + destPage);
   // Получаем диспетчер данного запроса и перенаправляем запрос
   RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
   // Непосредственно перенаправление
   dispatcher.forward(request, response);

   logger.debug("LEAVING controller -> processRequest().");
  }

}
