package org.census.commons.dao.simple.docs;

import org.census.commons.dao.hibernate.personnel.DepartmentsSimpleDao;
import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dto.personnel.employees.EmployeeDto;
//import org.census.commons.dto.docs.recipientDeptDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * Класс реализует различные манипуляции с объектами recipientDeptDTO (записи из таблицы
 * recipientsDepts БД "Служебки").
 * Наследует методы класса DSCommonDAO, в том числе getConnection(), который получает конекцию с сервером
 *
 * @author Gusev Dmitry
 * @version 1.0
 */
public class recipientDeptDAO extends Connectors{

    private DepartmentsSimpleDao departmentsDao;

    /**
     * Данный метод возвращает список всех отделов получателей служебки,
     * идентификатор которой передается в качестве параметра. Если же у данной служебки нет получателей,
     * то метод возвращает значение null.
     *
     * @param dH      - Держатель ссылок на все DAO-компоненты приложения
     * @param memoID  int идентификатор служебки, для которой мы ищем получателей
     * @param deleted int статус получаемых записей - задается константами из модуля
     *                Defaults - Defaults.STATUS_DELETED и Defaults.STATUS_UNDELETED. Если параметр deleted не равен ни одной из
     *                данных констант - метод верент все записи - и активные и удаленные.
     * @return List список отделов/пользователей получателей служебки с идентификатором memoID
     *         или значение null (если ничего не найдено).
     */
    public List findAllByMemo(daoHandler dH, int memoID, int deleted) {
        logger.debug("ENTERING recipientDeptDAO.findAllByMemo("+memoID+")");
        Connection conn = null;
        ResultSet rs;
        List recipientsList = null;

        try {

            conn = this.getMemoConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT ID, MemoID, RecipientDeptID, Realized " +
                            "FROM recipientsDepts " +
                            "WHERE memoID = ? AND deleted = ?");

            stmt.setInt(1, memoID);
            stmt.setInt(2, deleted);
            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // Если что-то нашли - формируем список, если нет - возвращаем null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // Инициализируем список
                recipientsList = new ArrayList();
                // В цикле проходим по курсору и формируем список объектов
                do {
                    //recipientDeptDTO recipient = new recipientDeptDTO();
                    // Идентификатор получателя
                    //recipient.setId(rs.getInt("ID"));
                    // Идентификатор служебки
                    //recipient.setMemoID(rs.getInt("MemoID"));
                    // Идентификатор отдела-получателя
                    //recipient.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                    // Выполнена ли данная служебка конкретным получателем
                    //recipient.setRealized(rs.getInt("Realized"));

                    //Найдем данные подразделения по его идентификатору
                    /*
                    DepartmentDto simpleDepartment = this.departmentsDao.findById(recipient.getRecipientDeptID());
                    if (simpleDepartment != null) {
                        // Трехзначный код отдела-получателя служебки
                        recipient.setRecipientDeptCode(simpleDepartment.getCode());
                        // Наименование подразделения
                        recipient.setRecipientDeptName(simpleDepartment.getName());
                    }
                    recipientsList.add(recipient);
                    */
                }
                while (rs.next());

                //if(recipientsList != null){
                //    Collections.sort(recipientsList, new departmentsSort("recipientDeptId"));
                //}

                logger.debug("Processing of ResultSet finished.");
            }
            // Если набор данных пуст - сообщение в лог
            else logger.debug("ResultSet is EMPTY!");
        }
        // Перехва ИС
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        // Освобождение ресурсов
        finally {
            try {
                if (conn != null) conn.close();
            }
            catch (Exception e_res) {
                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
            }
        }
        logger.debug("LEAVING recipientDeptDAO.findAllByMemo("+memoID+")");
        return recipientsList;
    }

    /**
     * Данный метод возвращает список всех отделов получателей служебки,
     * идентификатор которой передается в качестве параметра. Если же у данной служебки нет получателей,
     * то метод возвращает значение null.
     *
     * @param dH       - Держатель ссылок на все DAO-компоненты приложения
     * @param memoID   int идентификатор служебки, для которой мы ищем получателей
     *                 Defaults - Defaults.STATUS_DELETED и Defaults.STATUS_UNDELETED. Если параметр deleted не равен ни одной из
     *                 данных констант - метод верент все записи - и активные и удаленные.
     * @param realized - признак выполнения служебки
     * @return List список отделов/пользователей получателей служебки с идентификатором memoID
     *         или значение null (если ничего не найдено).
     */
    public List findDeptsRealizedMemo(daoHandler dH, int memoID, int realized) {
        logger.debug("ENTERING recipientDeptDAO.findDeptsRealizedMemo()");
        Connection conn = null;
        ResultSet rs;
        List recipientsList = null;

        try {

            conn = this.getMemoConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT ID, MemoID, RecipientDeptID, Realized " +
                            "FROM recipientsDepts " +
                            "WHERE memoID = ? AND realized = ?");

            stmt.setInt(1, memoID);
            stmt.setInt(2, realized);
            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // Если что-то нашли - формируем список, если нет - возвращаем null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // Инициализируем список
                recipientsList = new ArrayList();
                // В цикле проходим по курсору и формируем список объектов
                do {
                    //recipientDeptDTO recipient = new recipientDeptDTO();
                    // Идентификатор получателя
                    //recipient.setId(rs.getInt("ID"));
                    // Идентификатор служебки
                    //recipient.setMemoID(rs.getInt("MemoID"));
                    // Идентификатор отдела-получателя
                    //recipient.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                    // Выполнена ли данная служебка конкретным получателем
                    //recipient.setRealized(rs.getInt("Realized"));
                    // Трехзначный код отдела-получателя служебки
                    //DepartmentDto recDept = this.departmentsDao.findById(recipient.getRecipientDeptID());
                    //recipient.setRecipientDeptCode(recDept.getCode());
                    //recipientsList.add(recipient);

                }
                while (rs.next());

                //if(recipientsList != null){
                //    Collections.sort(recipientsList, new departmentsSort("recipientDeptId"));
                //}

                logger.debug("Processing of ResultSet finished.");
            }
            // Если набор данных пуст - сообщение в лог
            else logger.debug("ResultSet is EMPTY!");
        }
        // Перехва ИС
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        // Освобождение ресурсов
        finally {
            try {
                if (conn != null) conn.close();
            }
            catch (Exception e_res) {
                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
            }
        }
        logger.debug("ENTERING recipientDeptDAO.findAllByMemo()");
        return recipientsList;
    }

    /**
     * Данный метод возвращает объект "отдел-получатель" [recipientDept] по его идентификатору
     * из таблицы recipientsDepts
     *
     * @param dH              - Держатель ссылок на все DAO-компоненты приложения
     * @param recipientDeptID - идентификатор подразделения
     * @return - объект "отдел-получатель" [recipientDeptDTO]
     */
    public void /*recipientDeptDTO*/ findByID(daoHandler dH, long recipientDeptID) {
        logger.debug("ENTERING into recipientDeptDAO.findByID().");
        Connection conn = null;
        ResultSet rs;
        //recipientDeptDTO recipientDept = null;

        try {

            conn = this.getMemoConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM recipientsDepts WHERE ID = ?");

            stmt.setLong(1, recipientDeptID);
            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // Если что-то нашли - формируем список, если нет - возвращаем null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // Инициализируем список
                //recipientDept = new recipientDeptDTO();
                // Идентификатор отдела-получателя из таблицы recipientsDepts
                //recipientDept.setId(rs.getInt("ID"));
                // Идентификатор отдела-получателя из БД "Кадры"
                //recipientDept.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                // Трехзначный код отдела-получателя
                //DepartmentDto recDept = this.departmentsDao.findById(recipientDept.getRecipientDeptID());
                //recipientDept.setRecipientDeptCode(recDept.getCode());

                // Наименование отдела
                //recipientDept.setRecipientDeptName(rs.getString("DepartmentName"));
            }
            // Если набор данных пуст - сообщение(отладочное) в лог.
            else logger.debug("ResultSet is EMPTY!");
        }
        // Перехват ИС
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        // Обязательно нужно освободить ресурсы
        finally {
            try {
                if (conn != null) conn.close();
            }
            catch (Exception e_res) {
                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
            }
        }
        logger.debug("LEAVING recipientDeptDAO.findByID().");
        //return recipientDept;
    }

    /**
     * Данный метод возвращает объект "отдел-получатель" [recipientDept] из таблицы recipientsDepts
     * по идентификатору отдела из таблицы departments_list БД "Кадры".
     *
     * @param dH     - Держатель ссылок на все DAO-компоненты приложения
     * @param deptID - идентификатор подразделения
     * @return - объект "отдел-получатель" [recipientDeptDTO]
     */
    public void /*recipientDeptDTO*/ findByDeptID(daoHandler dH, int deptID) {
        logger.debug("ENTERING into recipientDeptDAO.findByDeptID().");
        Connection conn = null;
        ResultSet rs;
        //recipientDeptDTO recipientDept = null;

        try {

            conn = this.getMemoConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM recipientsDepts WHERE RecipientDeptID = ?");

            stmt.setInt(1, deptID);
            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // Если что-то нашли - формируем список, если нет - возвращаем null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // Инициализируем список
                //recipientDept = new recipientDeptDTO();
                // Идентификатор отдела-получателя из таблицы recipientsDepts
                //recipientDept.setId(rs.getInt("ID"));
                // Идентификатор отдела-получателя из БД "Кадры"
                //recipientDept.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                // Трехзначный код отдела-получателя
                //DepartmentDto recDept = this.departmentsDao.findById(recipientDept.getRecipientDeptID());
                //recipientDept.setRecipientDeptCode(recDept.getCode());
            }
            // Если набор данных пуст - сообщение(отладочное) в лог.
            else logger.debug("ResultSet is EMPTY!");
        }
        // Перехват ИС
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        // Обязательно нужно освободить ресурсы
        finally {
            try {
                if (conn != null) conn.close();
            }
            catch (Exception e_res) {
                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
            }
        }
        logger.debug("LEAVING recipientDeptDAO.findByDeptID().");
        //return recipientDept;
    }

    /**
     * Данный метод возвращает объект "отдел-получатель" [recipientDept] по идентификатору
     * отдела из таблицы departments_list БД "Кадры" и по идентификатору служебкной записки.
     * Подразумевается, что один отдел может быть получателем одной служебки один раз - поэтому
     * метод возвращает один объект.
     *
     * @param dH     - Держатель ссылок на все DAO-компоненты приложения
     * @param deptID - идентификатор подразделения
     * @param memoID - идентификатор служебкной записки
     * @return - объект "отдел-получатель" [recipientDeptDTO]
     */
    public /*recipientDeptDTO*/ void findByMemoDeptID(daoHandler dH, long deptID, int memoID) {
        logger.debug("ENTERING into recipientDeptDAO.findByMemoDeptID().");
        Connection conn = null;
        ResultSet rs;
        //recipientDeptDTO recipientDept = null;

        try {

            conn = this.getMemoConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM recipientsDepts WHERE RecipientDeptID = ? AND MemoID = ?");

            stmt.setLong(1, deptID);
            stmt.setInt(2, memoID);
            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // Если что-то нашли - формируем список, если нет - возвращаем null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // Инициализируем список
                //recipientDept = new recipientDeptDTO();
                // Идентификатор отдела-получателя из таблицы recipientsDepts
                //recipientDept.setId(rs.getInt("ID"));
                // Идентификатор отдела-получателя из БД "Кадры"
                //recipientDept.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                // Трехзначный код отдела-получателя
                //DepartmentDto recDept = this.departmentsDao.findById(recipientDept.getRecipientDeptID());
                //recipientDept.setRecipientDeptCode(recDept.getCode());
                // Идентификатор служебки
                //recipientDept.setMemoID(rs.getInt("MemoID"));
                // Поле "выполнено/невыполнено" (Realized)
                //recipientDept.setRealized(rs.getInt("Realized"));
            }
            // Если набор данных пуст - сообщение(отладочное) в лог.
            else logger.debug("ResultSet is EMPTY!");
        }
        // Перехват ИС
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        // Обязательно нужно освободить ресурсы
        finally {
            try {
                if (conn != null) conn.close();
            }
            catch (Exception e_res) {
                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
            }
        }
        logger.debug("LEAVING recipientDeptDAO.findByMemoDeptID().");
        //return recipientDept;
    }

    /**
     * Данный метод возвращает список объектов "отдел-получатель служебки"[recipientDeptDTO], найденных по
     * списку идентификаторов отделов из БД "Кадры", переданному в качустве параметра. Если ни одного
     * объекта не найдено - метод вернет значение null. Метод использует для поиска отдела другой метод
     * данного класса - findByDeptID.
     *
     * @param dH    - Держатель ссылок на все DAO-компоненты приложения
     * @param depts - строковый список идентификаторов отделов из БД "Кадры"
     * @return - список объектов "отдел-получатель служебки"[recipientDeptDTO]
     */
    public List findByDeptIDList(daoHandler dH, String[] depts) {
        logger.debug("ENTERING into recipientDeptDAO.findByDeptIDList().");
        int deptID; // <- временно храним тут числовой идентификатор
        List deptList = null;
        try {
            // Если список пуст или null - генерируем ошибку
            if ((depts == null) || (depts.length <= 0)) throw new Exception("Depts list are EMPTY!");
            // Разбираем переданный нам список идентификаторов - в цикле
            for (String dept : depts) {
                deptID = -1;
                // Берем текущий идентификатор (строковый)
                try {
                    deptID = Integer.parseInt(dept);
                }
                catch (Exception e) {
                    logger.error("DeptID [" + dept + "] is invalid!");
                }
                // Если удалось перевести идентификатор из строки в число - ищем отдел
                if (deptID > 0) {
                    logger.debug("DeptID [" + deptID + "]");
                    //Найдем подразделение
                    DepartmentDto  simpleDepartment = this.departmentsDao.findById((long) deptID);

                    if(dept != null){
                        logger.debug("DeptID [" + dept + "] FIND");
                        //recipientDeptDTO recipientDept = new recipientDeptDTO();
                        //recipientDept.setRecipientDeptID(deptID);
                        //recipientDept.setRecipientDeptCode(simpleDepartment.getCode());

                        if (deptList == null) deptList = new ArrayList();
                        //deptList.add(recipientDept);
                    }                                        
                }
            }

            //if (deptList != null) {
            //    Collections.sort(deptList, new departmentsSort("recipientDeptId"));
            //}

        }
        // Перехват ИС
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        logger.debug("LEAVING deptDAO.findByDeptIDList().");
        return deptList;
    }

    /**
     * Для создания новой СЗ, или при ответе на СЗ, данный метод возвращает список объектов "отдел-получатель служебки",
     * так же начальников отдела, найденных по списку идентификаторов отделов из БД "Кадры", переданному в качестве
     * параметра. Если ни одного объекта не найдено - метод вернет значение null.
     *
     * @param dH    - Держатель ссылок на все DAO-компоненты приложения
     * @param depts - строковый список идентификаторов отделов из БД "Кадры"
     * @return -   список объектов "отдел-получатель служебки"[recipientDeptDTO]
     */
    public List findDeptID(daoHandler dH, String[] depts) {
        logger.debug("ENTERING into recipientDeptDAO.findByDeptIDList().");
        int deptID; // <- временно храним тут числовой идентификатор
        List deptList = null;
        try {
            // Если список пуст или null - генерируем ошибку
            if ((depts == null) || (depts.length <= 0)) throw new Exception("Depts list are EMPTY!");

            
            // Разбираем переданный нам список идентификаторов - в цикле
            for (String dept : depts) {
                deptID = -1;
                // Берем текущий идентификатор (строковый)
                try {
                    deptID = Integer.parseInt(dept);
                }
                catch (Exception e) {
                    logger.error("DeptID [" + dept + "] is invalid!");
                }
                // Если удалось перевести идентификатор из строки в число - ищем отдел
                if (deptID > 0) {
                    //Найдем параметры отдела по его ID
                    DepartmentDto simpleDepartment = this.departmentsDao.findById((long) deptID);

                    // Создаем объект получателей
                    //recipientDeptDTO recipientDept = new recipientDeptDTO();

                    // ID отдела
                    //recipientDept.setRecipientDeptID(simpleDepartment.getId());
                    // Трехзначный код отдела
                    //recipientDept.setRecipientDeptCode(simpleDepartment.getCode());

                    // Обращение к начальнику отдела, в зависимости от пола
                    // Для начала найдем начальника
                    EmployeeDto simpleEmployeeChief = this.departmentsDao.getChiefOfDept(simpleDepartment.getId());

                    if(simpleEmployeeChief != null){
                        //recipientDept.setRecipientChief((simpleEmployeeChief.isMale() ? "Уважаемый " :"Уважаемая ") + simpleEmployeeChief.getShortRusName());
                    }

                    // Если мы что-то нашли - инициализируем список (если он еще не инициализирован)
                    // и добавляем в него найденный отдел
                    if (deptList == null) deptList = new ArrayList();
                    //deptList.add(recipientDept);
                }
            }

            //if (deptList != null) {
            //    Collections.sort(deptList, new departmentsSort("recipientDeptId"));
            //}
        }
        // Перехват ИС
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        logger.debug("LEAVING deptDAO.findByDeptIDList().");
        return deptList;
    }

    /**
     * Данный метод создает запись в таблице "отделы-получатели служебки" [recipientsDepts]. В качсестве шаблона
     * для создания записи используется объект recipientDeptDTO, переданный в качестве параметра.
     *
     * @param dH        - Держатель ссылок на все DAO-компоненты приложения
     * //@param recipient - Объект "отдел-получатель служебки" [recipientsDeptsDTO]
     * @throws Exception - ошибка
     */
    public void create(daoHandler dH/*, recipientDeptDTO recipient*/) throws Exception {
        logger.debug("ENTERING recipientDeptDAO.create()");
        Connection conn = null;

        try {

            conn = this.getMemoConnection();
            logger.debug("Connection established.");
            // Подготавливаем запрос для добавления служебки
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO recipientsDepts(MemoID, RecipientDeptID, Realized, UpdateUserID) VALUES (?,?,?,?)");

            //stmt.setInt(1, recipient.getMemoID());
            //stmt.setLong(2, recipient.getRecipientDeptID());
            //stmt.setInt(3, recipient.getRealized());
            //stmt.setLong(4, recipient.getUpdateUserID());
            logger.debug("Statement created.");

            stmt.executeUpdate();
            logger.debug("Query INSERT executed.");
        }
        // Перехват ИС
        catch (Exception e) {
            // Запишем в лог ошибку
            logger.error("ERROR occured: " + e.getMessage());
            // Передадим ошибку дальше
            throw new Exception(e.getMessage());
        }
        // Обязательно нужно освободить ресурсы
        finally {
            try {
                if (conn != null) conn.close();
            }
            catch (Exception e_res) {
                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
            }
        }
        logger.debug("LEAVING recipientDeptDAO.create()");
    }

    /**
     * Метод обновляет(изменяет) запись в таблице recipientsDepts по шаблону recipientDeptDTO, переданному в качестве
     * параметра. Идентификатор изменяемой записи также берется из шаблона. Если записи с таким идентификатором
     * не существует или переданный шаблон пуст или равен null, то никаких действий не происходит.
     *
     * @param dH        - Держатель ссылок на все DAO-компоненты приложения
     * //@param recipient - Объект "отдел-получатель служебки" [recipientsDeptsDTO]
     * @throws Exception - ошибка
     */
    public void update(daoHandler dH/*, recipientDeptDTO recipient*/) throws Exception {
        logger.debug("ENTERING into recipientDeptDAO.update().");
        Connection conn = null;

        try {
            logger.debug("Checking data for change.");
            // Вначале найдем изменяемую запись
            //if ((recipient == null) || (dH.getRecipientDeptDAO().findByID(dH, recipient.getId()) == null))
            //    throw new Exception("Recipient doesn't exists(nothing to update) !");
            logger.debug("Starting sql-query generation.");

            conn = this.getMemoConnection();
            logger.debug("Connection established.");
            // Подготавливаем запрос для добавления служебки
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE recipientsDepts SET Realized = ? WHERE id = ?");

            //stmt.setInt(1, recipient.getRealized());
            //stmt.setLong(2, recipient.getId());
            logger.debug("Statement created.");

            stmt.executeUpdate();
            logger.debug("Query INSERT executed.");

        }
        // Перехват ИС
        catch (Exception e) {
            logger.error("ERROR occured: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
        // Обязательно нужно освободить ресурсы
        finally {
            try {
                if (conn != null) conn.close();
            }
            catch (Exception e_res) {
                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
            }
        }
        logger.debug("LEAVING recipientDeptDAO.update().");
    }

    /**
     * Имеется список идентификатор подразделений, для которых, при направлении к ним служебной записки, требуется
     * направить документ и в другое определенное подразделение.
     * Метод просматривает список идентификаторов подразделений выбранныъх в форме, и если требуется добавляет индентификаторы
     * требуемых отделов.
     *
     * @param recipientDept        - список идентификаторов подразделений выбранныъх в форме
     * @return -
     */
    public String[] checkDepartment(String[] recipientDept){
        logger.debug("ENTERING into recipientDeptDAO.checkDepartment().");

        HashMap<String,String>departmentsJoin = new HashMap<String,String>();
        //Подразделения: 350->351
        departmentsJoin.put("478","463");

        List<String> recipientDept2 = new ArrayList<String>();
        List<String> wordList = Arrays.asList(recipientDept);

        //Пихаем все индентификаторы в список
        for (String aWordList : wordList) {
            recipientDept2.add(aWordList);
        }

        //Создаем сет для мап
        Set setHM = departmentsJoin.entrySet();

        //Пройдемся по списку связей подразделений и если что запихаем требуемые отделы в общий список
        for (Object aSetHM : setHM) {
            Map.Entry meHM = (Map.Entry) aSetHM;

            if (wordList.contains(meHM.getKey().toString()) && !wordList.contains(meHM.getValue().toString())) {
                recipientDept2.add(meHM.getValue().toString());
            }
        }

        logger.debug("LEAVING recipientDeptDAO.checkDepartment().");
        return recipientDept2.toArray(recipientDept);
    }
}