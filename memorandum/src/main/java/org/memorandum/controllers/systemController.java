package org.memorandum.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Данный класс реализует контроллер, который предназначен в задаче "Служебки" для выполнения различных
 * технических опций по обслуживанию приложения.
 *
 * @author Gusev Dmitry
 * @version 1.0
 */

public class systemController extends AbstractController {

    /**
     * Тип действия: список E-mail для переадресации. Также список СОТРДНИК-ПОДРАЗДЕЛЕНИЕ
     */
    private static final String MAPPING_ACTION = "Mapping";
    /**
     * Тип действия: выбор отдела ГУР для назначения ему маппинга емайл сообщений.
     */
    private static final String SELECT_DEPT_FOR_MAPPING_ACTION = "selectDept";
    /**
     * Тип действия: выбор E-mail для отдела.
     */
    private static final String SELECT_EMAIL_FOR_MAPPING = "deptBox";
    /**
     * Тип действия: выбор сотруника ГУР для перевода в другой отдел.
     */
    private static final String SELECT_DPM_FOR_MAPPING = "deptBox_dpm";
    /**
     * Тип действия: выбор емайла отдела ГУР для включения его в маппинг.
     */
    private static final String ADD_DEPT_EMAIL_ACTION = "addDeptEmail";
    /**
     * Тип действия: Добавление пары Сотрудник-Подразделение в маппинг.
     */
    private static final String ADD_DPM_ACTION = "addDeptsPersonnelMapping";
    /**
     * Тип действия: Обновление данных пары Сотрудник-Подразделение в маппинг.
     */
    private static final String UPDATE_DPM_ACTION = "updateMappingDPM";
    /**
     * Тип действия: удаление емайла сотрудника ГУР
     */
    private static final String DELETE_MAPPING_ACTION = "deleteMapping";
    /**
     * Тип действия: удаление записи маппинга СОТРУДНИК-ПОДРАЗДЕЛЕНИЕ
     */
    private static final String DELETE_MAPPING_DPM_ACTION = "deleteMappingDPM";
    /**
     * Страница с списком маппингов емайл.
     */
    private static final String MAPPING_PAGE = "/system/mapping.jsp";
    /**
     * Страница для выбора отдела ГУР, которому будет назначен маппинг емайл.
     */
    private static final String SELECT_DEPT_FOR_MAPPING_PAGE = "/controller?action=depts";
    /**
     * Страница со списком емайлов отделов и сотрудников ГУР.
     */
    private static final String DEPTS_EMAILS_PAGE = "/system/deptsEmails.jsp";
    /**
     * Страница со списком сотрудников ГУР.
     */
    private static final String PERSONNEL_PAGE = "/system/memberList.jsp";
    /**
     * Страница с сообщениями об ошибке
     */
    private static final String ERROR_PAGE = "/error.jsp";

    /**
     * Тип действия: Поиск персонала по критериям
     */
    private static final String SEARCH_MEMBER = "searchMember";

    /**
     * Список сотрудников
     */
    private static final String FIND_EMPLOYEES_LIST = "/system/findEmployee.jsp";

    /**
     * Непосредственная обработка поступившего запроса.
     *
     * @param request  HttpServletRequest параметр - HTTP-запрос
     * @param response HttpServletResponse параметр - HTTP-ответ
     * @throws ServletException ошибка сервлета
     * @throws IOException      ошибка ввода/вывода
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("ENTERING systemController -> processRequest().");
        
        // Начальная страница по умолчанию
        String destPage = ERROR_PAGE;
        // Префикс для страницы "ошибка" - для правильного отображения стилей. Остальным страницам данный параметр не помешает
        request.setAttribute("prefix", "../");       

        acc_logger.info("systemController [" + request.getRemoteUser() + "] [" + ACTION + "]");

        // Начальная страница маппинга
        if (MAPPING_ACTION.equals(ACTION)) {
            throw new UnsupportedOperationException();
            /*
            List map = dH.getDeptsEmailsMappingDAO().findAll(dH);
            request.setAttribute("mapping", map);
//            List map2 = dH.getDeptsPersonnelMappingDAO().findMappingAll(dH);
//            request.setAttribute("mapping2", map2);
            List map2 = null;
            request.setAttribute("mapping2", map2);

            if (map == null) {
                request.setAttribute("error", "error");
            }

            destPage = MAPPING_PAGE;
            */
        }

        // Выбор отдела для назначения ему маппинга
        else if (SELECT_DEPT_FOR_MAPPING_ACTION.equals(ACTION)) {
            // Передадим тип маппинга
            request.setAttribute("action", request.getParameter("type"));
            // Переходим на страницу выбора подразделения
            destPage = SELECT_DEPT_FOR_MAPPING_PAGE;
        }

        // Выбор емайла отдела для добавления маппинга
        else if (SELECT_EMAIL_FOR_MAPPING.equals(ACTION)) {
            throw new UnsupportedOperationException("Operation doesn't supported!");
            /*
            int deptid = 0;
            try {
                deptid = Integer.parseInt(request.getParameter("deptID"));
            }
            catch (Exception e) {
                logger.error("Invalid deptid!");
            }

            acc_logger.info("systemController [" + request.getRemoteUser() + "] [DeptID for mapping: " + deptid + "]");

            request.setAttribute("deptID", deptid);

            TreeMap sd = (TreeMap)dH.getConnectorSource().getSomething(dH, null,"getAllEmails");
            request.setAttribute("emailsList", sd);        

            destPage = DEPTS_EMAILS_PAGE;
            */
        }

        // Выбор сотрудника ГУР для добавления маппинга
        else if (SELECT_DPM_FOR_MAPPING.equals(ACTION)) {
            int deptid = 0;
            try {
                deptid = Integer.parseInt(request.getParameter("deptID"));
            }
            catch (Exception e) {
                logger.error("Invalid deptid!");
            }

            acc_logger.info("systemController [" + request.getRemoteUser() + "] [DeptID for mapping: " + deptid + "]");

            request.setAttribute("deptID", deptid);

            //Подготавливаем полный список сотрудников
            List simpleEmployees = this.getEmployeesDao().findAllActive();
            request.setAttribute("membersList", simpleEmployees);

            destPage = PERSONNEL_PAGE;
        }

        // Добавление пары Сотрудник-Подразделение в маппинг.
        else if (ADD_DPM_ACTION.equals(ACTION)) {
            throw new UnsupportedOperationException();
            /*
            deptsPersonnelMappingDTO deptsPersonnelMapping = new deptsPersonnelMappingDTO();
            deptsPersonnelMapping.setDeptID(Integer.parseInt(request.getParameter("deptID")));
            deptsPersonnelMapping.setPersonnelID(Integer.parseInt(request.getParameter("memberID")));

            acc_logger.info("systemController [" + request.getRemoteUser() + "] [ADD for DPM_mapping: " + deptsPersonnelMapping.getDeptID() + "] " +
                    "[PersonnelID for mapping: " + deptsPersonnelMapping.getPersonnelID() + "]");
            // Непосредственно добавление записи в deptsPersonnelMapping
            //dH.getDeptsPersonnelMappingDAO().create(dH, deptsPersonnelMapping);

            // Возвращаемся в маппинг
            List map = dH.getDeptsEmailsMappingDAO().findAll(dH);
            request.setAttribute("mapping", map);
            //List map2 = dH.getDeptsPersonnelMappingDAO().findMappingAll(dH);
            List map2 = null;
            request.setAttribute("mapping2", map2);

            if (map == null) {
                request.setAttribute("error", "error");
            }

            destPage = MAPPING_PAGE;
            */
            
        } else if (ADD_DEPT_EMAIL_ACTION.equals(ACTION)) {
            throw new UnsupportedOperationException();
            /*
            deptsEmailsMappingDTO deptsEmailsMapping = new deptsEmailsMappingDTO();

            deptsEmailsMapping.setDeptID(Integer.parseInt(request.getParameter("deptID")));
            deptsEmailsMapping.setEmailId(Integer.parseInt(request.getParameter("emailID")));

            acc_logger.info("systemController [" + request.getRemoteUser() + "] [DeptID for mapping: " + deptsEmailsMapping.getDeptID() + "] " +
                    "[emailID for mapping: " + deptsEmailsMapping.getEmailId() + "]");

            dH.getDeptsEmailsMappingDAO().create(deptsEmailsMapping);
            List map = dH.getDeptsEmailsMappingDAO().findAll(dH);
            request.setAttribute("mapping", map);
            //List map2 = dH.getDeptsPersonnelMappingDAO().findMappingAll(dH);
            List map2 = null;
            request.setAttribute("mapping2", map2);

            if (map == null) {
                request.setAttribute("error", "error");
            }
            destPage = MAPPING_PAGE;
            */
        }

        //Удаление записи из deptsEmailsMapping
        else if (DELETE_MAPPING_ACTION.equals(ACTION)) {
            throw new UnsupportedOperationException();
            /*
            int Id = 0;
            try {
                Id = Integer.parseInt(request.getParameter("ID"));
            }
            catch (Exception e) {
                logger.error("Invalid Id!");
            }

            // Найдем удаляемый маппинг
            deptsEmailsMappingDTO mapping = dH.getDeptsEmailsMappingDAO().findMappingByID(dH, Id);
            // занесем его в лог
            if (mapping != null)
                acc_logger.info("systemController [" + request.getRemoteUser() + "] [DELETE MAPPING: dept=" + mapping.getDeptname() +
                        ", id=" + Id + "]");
            else
                acc_logger.info("systemController [" + request.getRemoteUser() + "] [DELETE MAPPING: NULL]");

            //Вызов функции удаления
            if (Id > 0) {
                dH.getDeptsEmailsMappingDAO().delete(Id);
            }
            List map = dH.getDeptsEmailsMappingDAO().findAll(dH);
            request.setAttribute("mapping", map);
            //List map2 = dH.getDeptsPersonnelMappingDAO().findMappingAll(dH);
            List map2 = null;
            request.setAttribute("mapping2", map2);

            if (map == null) {
                request.setAttribute("error", "error");
            }
            destPage = MAPPING_PAGE;
            */
        }
        //Удаление записи из deptsPersonnelMapping
        else if (DELETE_MAPPING_DPM_ACTION.equals(ACTION)) {

            throw new UnsupportedOperationException();
            /*
            int Id = 0;
            try {
                Id = Integer.parseInt(request.getParameter("ID"));
            }
            catch (Exception e) {
                logger.error("Invalid Id!");
            }

            // Найдем удаляемый маппинг
            //deptsPersonnelMappingDTO mapping = dH.getDeptsPersonnelMappingDAO().findByID(dH, Id);
            deptsPersonnelMappingDTO mapping = null;
            // занесем его в лог
            if (mapping != null)
                acc_logger.info("systemController [" + request.getRemoteUser() + "] [DELETE MAPPING: deptid=" + mapping.getDeptID() +
                        ", PersonnelID=" + mapping.getPersonnelID() + ",isChief=" + mapping.getIsChief() + "]");
            else
                acc_logger.info("systemController [" + request.getRemoteUser() + "] [DELETE MAPPING_DPM: NULL]");

            //Вызов функции удаления
            if (mapping != null && Id > 0) {
                //dH.getDeptsPersonnelMappingDAO().delete(dH, Id);
            }

            // Возвращаемся в маппинг
            List map = dH.getDeptsEmailsMappingDAO().findAll(dH);
            request.setAttribute("mapping", map);
            //List map2 = dH.getDeptsPersonnelMappingDAO().findMappingAll(dH);
            List map2 = null;
            request.setAttribute("mapping2", map2);

            if (map == null) {
                request.setAttribute("error", "error");
            }

            destPage = MAPPING_PAGE;
            */
        }

        //Обновление записи из deptsPersonnelMapping
        else if (UPDATE_DPM_ACTION.equals(ACTION)) {
            throw new UnsupportedOperationException();

            /*
            int Id = 0;
            try {
                Id = Integer.parseInt(request.getParameter("ID"));
            }
            catch (Exception e) {
                logger.error("Invalid Id!");
            }

            // Найдем маппинг для обновления, пока обновляется тока данные по Начальнику подразделения
            //deptsPersonnelMappingDTO mapping = dH.getDeptsPersonnelMappingDAO().findByID(dH, Id);
            deptsPersonnelMappingDTO mapping = null;
            // занесем его в лог
            if (mapping != null)
                acc_logger.info("systemController [" + request.getRemoteUser() + "] [UPDATE MAPPING: deptid=" + mapping.getDeptID() +
                        ", PersonnelID=" + mapping.getPersonnelID() + ",isChief=" + mapping.getIsChief() + "]");
            else
                acc_logger.info("systemController [" + request.getRemoteUser() + "] [UPDATE MAPPING_DPM: NULL]");

            // Изменим признак начальника подразделения
            if (mapping.getIsChief() == 1) {
                mapping.setIsChief(0);
            } else {
                mapping.setIsChief(1);
            }
            //Вызов функции Update
            if (mapping != null && Id > 0) {
                //dH.getDeptsPersonnelMappingDAO().update(dH, mapping);
            }

            // Возвращаемся в маппинг
            List map = dH.getDeptsEmailsMappingDAO().findAll(dH);
            request.setAttribute("mapping", map);
            //List map2 = dH.getDeptsPersonnelMappingDAO().findMappingAll(dH);
            List map2 = null;
            request.setAttribute("mapping2", map2);

            if (map == null) {
                request.setAttribute("error", "error");
            }

            destPage = MAPPING_PAGE;
            */
        }


        //Поиск сотрудников по критериям
        else if(SEARCH_MEMBER.equals(ACTION)){ // todo: fix implementation!
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        logger.debug("DESTPAGE: " + destPage);
        // Получаем диспетчер данного запроса и перенаправляем запрос
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
        // Непосредственно перенаправление
        dispatcher.forward(request, response);

        logger.debug("LEAVING systemController -> processRequest().");
    }
}