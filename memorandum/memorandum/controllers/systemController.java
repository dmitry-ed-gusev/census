package memorandum.controllers;

import jpersonnel.JPersonnelConsts;
import jpersonnel.dto.SimpleEmployeeDTO;
import memorandum.Defaults;
import memorandum.dataModel.dto.deptsEmailsMappingDTO;
import memorandum.dataModel.dto.deptsPersonnelMappingDTO;
import jlib.utils.JLibUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.TreeMap;

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
            List simpleEmployees = (List)dH.getConnectorSource().getSomething(dH, 0, "findAllPersonnelWithDepartmentId");
            request.setAttribute("membersList", simpleEmployees);

            destPage = PERSONNEL_PAGE;
        }

        // Добавление пары Сотрудник-Подразделение в маппинг.
        else if (ADD_DPM_ACTION.equals(ACTION)) {
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
            
        } else if (ADD_DEPT_EMAIL_ACTION.equals(ACTION)) {
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
        }

        //Удаление записи из deptsEmailsMapping
        else if (DELETE_MAPPING_ACTION.equals(ACTION)) {
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
        }
        //Удаление записи из deptsPersonnelMapping
        else if (DELETE_MAPPING_DPM_ACTION.equals(ACTION)) {
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
        }

        //Обновление записи из deptsPersonnelMapping
        else if (UPDATE_DPM_ACTION.equals(ACTION)) {
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
        }
        //Поиск сотрудников по критериям
        else if(SEARCH_MEMBER.equals(ACTION)){

            //Признак нового запроса, стартовая страница
            String newQuery = request.getParameter("newQuery");

            int personnelId = 0;
            try {personnelId = Integer.parseInt(request.getParameter("id"));}
            catch (Exception e) {logger.debug("Invalid personnelId");}

            String family = request.getParameter("family");
            String initials = request.getParameter("initials");
            String post = request.getParameter("post");

            int departmentType = 0;
            try {departmentType = Integer.parseInt(request.getParameter("departmentType"));}
            catch (Exception e) {logger.debug("Invalid departmentType");}

            int departmentId = 0;
            try {departmentId = Integer.parseInt(request.getParameter("departmentId"));}
            catch (Exception e) {logger.debug("Invalid departmentId");}

            String hiredDate = request.getParameter("hiredDate");
            java.sql.Date sqlHiredDate = null;
            if(hiredDate != null){
                 try {
                    sqlHiredDate = new java.sql.Date(JLibUtils.dateToPeriod(hiredDate, Defaults.DATE_PATTERN, 0, JLibUtils.DatePeriod.MONTH).getTime());
                 }catch (ParseException e) {e.getMessage();}
            }

            String firedDate = request.getParameter("firedDate");
            java.sql.Date sqlFiredDate = null;
            if(firedDate != null){
                 try {
                    sqlFiredDate = new java.sql.Date(JLibUtils.dateToPeriod(firedDate, Defaults.DATE_PATTERN, 0, JLibUtils.DatePeriod.MONTH).getTime());
                 }catch (ParseException e) {e.getMessage();}
            }

            int isFired = 0;
            try {isFired = Integer.parseInt(request.getParameter("isFired"));}
            catch (Exception e) {logger.debug("Invalid isFired");}

            // Если пришел запрос с параметрами, то ищем
            if(newQuery == null){
                // Заполняем объект "сотрудник"
                SimpleEmployeeDTO findSimpleEmployee = new SimpleEmployeeDTO.Builder(personnelId, family, initials)
                                .isDepartmentHO(departmentType == 0)
                                .departmentId(departmentId)
                                .hiredDate(sqlHiredDate)
                                .firedDate(sqlFiredDate)
                                .isFired(isFired == 1)
                                .post(post)
                                .build();

                List df = (List)dH.getConnectorSource().getSomething(dH, findSimpleEmployee, "findAllPersonnelWithSimpleEmployeeDTO");
                request.setAttribute("simpleEmployees", df);             

            }
            //Список подразделений
            request.setAttribute("departmentList", dH.getConnectorSource().getSomething(dH, JPersonnelConsts.DeptType.ALL, "getDepartmentsList"));

            destPage = FIND_EMPLOYEES_LIST;

        }

        logger.debug("DESTPAGE: " + destPage);
        // Получаем диспетчер данного запроса и перенаправляем запрос
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
        // Непосредственно перенаправление
        dispatcher.forward(request, response);

        logger.debug("LEAVING systemController -> processRequest().");
    }
}