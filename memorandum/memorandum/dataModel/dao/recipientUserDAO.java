package memorandum.dataModel.dao;

import jpersonnel.dto.SimpleEmployeeDTO;
import memorandum.Defaults;
import memorandum.daoHandler;
import memorandum.dataModel.dto.memoDTO;
import memorandum.dataModel.dto.recipientUserDTO;
import memorandum.dataModel.system.Connectors;
import jlib.utils.JLibUtils;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Данный класс реализует выполнение различных операций с объектами recipientUserDTO (записи из таблицы
 * recipientsUsers). Операции: поиск, создание, изменение.
 * Наследует методы класса DSCommonDAO, в том числе getConnection(), который получает конекцию с сервером
 *
 * @author Gusev Dmitry
 * @version 1.0
 */
public class recipientUserDAO extends Connectors {

    public recipientUserDAO() {
    }

    /**
     * Данный метод возвращает список всех пользователей получателей служебки,
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
        logger.debug("ENTERING recipientUserDAO.findAllByMemo()");
        Connection conn = null;
        ResultSet rs;
        List<recipientUserDTO> recipientsList = null;

        try {

            conn = this.getMemoConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT ID, MemoID, RecipientDeptID, RecipientUserID, Subject, Answer, RealizedDate, Realized, " +
                            "TimeStamp, sendemail, appointeduserid " +
                            "FROM recipientsUsers " +
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
                recipientsList = new ArrayList<recipientUserDTO>();
                SimpleEmployeeDTO simpleEmployee;
                // В цикле проходим по курсору и формируем список объектов
                do {
                    recipientUserDTO recipient = new recipientUserDTO();
                    // Идентификатор получателя
                    recipient.setId(rs.getInt("ID"));
                    // Идентификатор служебки
                    recipient.setMemoID(rs.getInt("MemoID"));
                    // Идентификатор отдела пользователя-получателя
                    recipient.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                    // Идентификатор пользователя получателя
                    recipient.setRecipientUserID(rs.getInt("RecipientUserID"));
                    // Сообщение для пользователя, которому поручена служебка
                    recipient.setSubject(rs.getString("Subject"));
                    // Ответ пользователя, которому поручена служебка
                    recipient.setAnswer(rs.getString("Answer"));
                    // Срок ответа на данную служебку для данного получателя
                    recipient.setRealizedDate(JLibUtils.dateToPattern(rs.getDate("RealizedDate"), Defaults.DATE_PATTERN, null));
                    // Выполнена ли данная служебка конкретным получателем
                    recipient.setRealized(rs.getInt("Realized"));
                    // Признак отправки E-mail при выполнении поручения
                    recipient.setSendemail(rs.getInt("sendemail"));
                    // Получаем объект "сотрудник ГУР"
                    simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, recipient.getRecipientUserID(),"getSimpleEmployeeByID");
                    // Если объект найден - заполним значениями соотв. поля данного класса
                    if (simpleEmployee != null)
                        // Фамилия И.О. пользователя-получателя служебки
                        recipient.setRecipientShortName(simpleEmployee.getShortName());

                    // Получаем объект "сотрудник ГУР", чел который поручил служебку
                    simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, rs.getInt("appointeduserid"),"getSimpleEmployeeByID");
                    if (simpleEmployee != null)
                        // Фамилия И.О. пользователя-который поручил служебку
                        recipient.setAppointedUserName(simpleEmployee.getShortName());

                    // Добавляем созданный объект в список
                    recipientsList.add(recipient);
                }
                while (rs.next());
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
        logger.debug("ENTERING recipientUserDAO.findAllByMemo()");
        return recipientsList;
    }

    /**
     * Данный метод возвращает объект "сотрудник ГУР, которому поручена служебка" [recipientUserDTO] по идентификатору
     * сотрудника ГУР из таблицы personnel_list БД "Кадры" и по идентификатору служебной записки.
     * Возможны ситуции когда у чела более 1 поручения, метод вернет первую запись в списке
     *
     * @param dH       - Держатель ссылок на все DAO-компоненты приложения
     * @param memberID - идентификатор пользователя из БД "Кадры"
     * @param memoID   - идентификатор служебной записки
     * @param realized - признак выполненного поручения
     * @return - объект recipientUserDTO сотрудник ГУР, которому поручена служебка
     */
    public recipientUserDTO findByMemoDeptID(daoHandler dH, int memberID, int memoID, int realized) {
        logger.debug("ENTERING into recipientUserDAO.findByMemoDeptID().");
        Connection conn = null;
        ResultSet rs;
        recipientUserDTO recipient = null;

        try {

            conn = this.getMemoConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM recipientsUsers WHERE RecipientUserID = ? AND MemoID = ? AND realized = ?");

            stmt.setInt(1, memberID);
            stmt.setInt(2, memoID);
            stmt.setInt(3, realized);
            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // Если что-то нашли - формируем список, если нет - возвращаем null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // Инициализируем список
                recipient = new recipientUserDTO();
                // Идентификатор отдела-получателя из таблицы recipientsDepts
                recipient.setId(rs.getInt("ID"));
                // Идентификатор служебки
                recipient.setMemoID(rs.getInt("MemoID"));
                // Идентификатор отдела, в котором работает сотрудник-получатель из БД "Кадры"
                recipient.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                // Идентификатор пользователя-поручителя служебки (из БД "Кадры")
                recipient.setAppointedUserID(rs.getInt("AppointedUserID"));
                // Идентификатор пользователя-получателя служебки (из БД "Кадры")
                recipient.setRecipientUserID(rs.getInt("RecipientUserID"));
                // Комментарий поручителя служебки
                recipient.setSubject(rs.getString("Subject"));
                // Ответ пользователя на поручение
                recipient.setAnswer(rs.getString("Answer"));
                // Поле "выполнено/невыполнено" (Realized)
                recipient.setRealized(rs.getInt("Realized"));
                // Признак отправки E-mail при выполнении поручения
                recipient.setSendemail(rs.getInt("sendemail"));
                // Срок (дата) исполнения поручения
                recipient.setRealizedDate(JLibUtils.dateToPattern(rs.getDate("RealizedDate"), Defaults.DATE_PATTERN, null));
                // Получаем объект "сотрудник ГУР"
                SimpleEmployeeDTO simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, recipient.getRecipientUserID(),"getSimpleEmployeeByID");
                // Если объект найден - заполним значениями соотв. поля данного класса
                if (simpleEmployee != null)
                    // Фамилия И.О. пользователя-получателя служебки
                    recipient.setRecipientShortName(simpleEmployee.getShortName());
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
        logger.debug("LEAVING recipientUserDAO.findByMemoDeptID().");
        return recipient;
    }

    /**
     * Данный метод возвращает объект "пользователь-получатель" [recipientUserDTO] по его идентификатору
     * из таблицы recipientsUsers. Если по данному идентификатору объект не найден - метод возвращает
     * значение null.
     *
     * @param dH              - Держатель ссылок на все DAO-компоненты приложения
     * @param recipientUserID - идентификатор "пользователь-получатель"
     * @return - объект recipientUserDTO "пользователь-получатель"
     */
    public recipientUserDTO findByID(daoHandler dH, int recipientUserID) {
        logger.debug("ENTERING into recipientUserDAO.findByID().");
        Connection conn = null;
        ResultSet rs;
        recipientUserDTO recipient = null;

        try {

            conn = this.getMemoConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM recipientsUsers WHERE ID = ?");

            stmt.setInt(1, recipientUserID);
            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // Если что-то нашли - формируем список, если нет - возвращаем null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // Инициализируем возвращаемый объект
                recipient = new recipientUserDTO();
                // Идентификатор отдела-получателя из таблицы recipientsDepts
                recipient.setId(rs.getInt("ID"));
                // Идентификатор служебки
                recipient.setMemoID(rs.getInt("MemoID"));
                // Идентификатор пользователя-поручителя служебки (из БД "Кадры")
                recipient.setAppointedUserID(rs.getInt("AppointedUserID"));
                // Идентификатор отдела, в котором работает сотрудник-получатель из БД "Кадры"
                recipient.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                // Идентификатор пользователя-получателя служебки (из БД "Кадры")
                recipient.setRecipientUserID(rs.getInt("RecipientUserID"));
                // Комментарий поручителя служебки
                recipient.setSubject(rs.getString("Subject"));
                // Ответ пользователя на поручение
                recipient.setAnswer(rs.getString("Answer"));
                // Поле "выполнено/невыполнено" (Realized)
                recipient.setRealized(rs.getInt("Realized"));
                // Срок (дата) исполнения поручения
                recipient.setRealizedDate(JLibUtils.dateToPattern(rs.getDate("RealizedDate"), Defaults.DATE_PATTERN, null));
                // Уведомление по email
                recipient.setSendemail(rs.getInt("sendemail"));                
                // Получаем объект "сотрудник ГУР"
                SimpleEmployeeDTO simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, recipient.getRecipientUserID(),"getSimpleEmployeeByID");
                // Если объект найден - заполним значениями соотв. поля данного класса
                if (simpleEmployee != null)
                    // Фамилия И.О. пользователя-получателя служебки
                    recipient.setRecipientShortName(simpleEmployee.getShortName());
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
        logger.debug("LEAVING recipientUserDAO.findByID().");
        return recipient;
    }

    /**
     * Данный метод возвращает список служебок не выполненных сотрудником/сотрудниками подразделения.
     * Если по данному идентификатору объект не найден - метод возвращает значение null.
     *
     * @param dH     - Держатель ссылок на все DAO-компоненты приложения
     * @param DeptID - идентификатор подразделения из БД "Кадры"
     * @param personnelId - идентификатор сотрудниа по которому требуется найти не выполненные поручения
     * @return - список служебных записок объектов memoDTO
     */
    public List NotMake(daoHandler dH, int DeptID, int personnelId) {
        logger.debug("ENTERING into recipientUserDAO.NotMake().");
        Connection conn = null;
        ResultSet rs;
        List<memoDTO> memoList = null;
        memoDTO memo;
        PreparedStatement stmt;

        try {

            conn = this.getMemoConnection();

            //возвращает список служебок не выполненных всеми сотрудниками подразделения
            if(personnelId == 0){
                stmt = conn.prepareStatement(
                    "SELECT DISTINCT TOP 100 memoid FROM recipientsusers " +
                            "WHERE realized = 0 AND recipientdeptid = ? ORDER BY memoid DESC");

                stmt.setInt(1, DeptID);

            //возвращает список служебок не выполненных сотрудником (personnelId)    
            }else{
                stmt = conn.prepareStatement(
                    "SELECT DISTINCT TOP 100 memoid FROM recipientsusers " +
                            "WHERE realized = 0 AND recipientuserid = ? ORDER BY memoid DESC");

                stmt.setInt(1, personnelId);
            }

            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // Если что-то нашли (список служебок на которые внутри подразделения не ответили) - формируем список,
            // если нет - возвращаем null
            while (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // Найдем служебку по ID
                memo = dH.getMemoDAO().findByID(dH, rs.getInt("memoid"));
                // Инициализируем список
                if (memoList == null) {
                    memoList = new ArrayList<memoDTO>();
                }

                // Добавим в поле Subject информацию о том, кто не выполнил поручение из данного подразделения
                if (memo != null && memo.getRecipientsUsers() != null) {

                    memo.setSubject(memo.getSubject() + " ; Поручено:");
                    for (int i = 0; i < memo.getRecipientsUsers().size(); i++) {
                        recipientUserDTO recipientUser = (recipientUserDTO) memo.getRecipientsUsers().get(i);
                        // Проверим, если поручение не выполнено и чел из этого отдела, то выводим его ФИО
                        if (recipientUser.getRealized() == 0 && recipientUser.getRecipientDeptID() == DeptID) {
                            memo.setSubject(memo.getSubject() + " " + recipientUser.getRecipientShortName());
                        }
                    }
                }
                memoList.add(memo);
            }
            // Если набор данных пуст - сообщение(отладочное) в лог.
            //else logger.debug("ResultSet is EMPTY!");
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
        logger.debug("LEAVING recipientUserDAO.NotMake().");
        return memoList;
    }

    /**
     * Метод создает пользователя-получателя служебки (запись в таблице recipientsUsers в БД
     * "Служебки"). В качестве шаблона для создания объекта используется переданный в качестве
     * параметра объект recipientUserDTO.
     *
     * @param dH        - Держатель ссылок на все DAO-компоненты приложения
     * @param recipient - объект пользователь-получатель служебки
     * @throws Exception - отлавливаем ошибку выполнения транзакции
     */
    public void create(daoHandler dH, recipientUserDTO recipient) throws Exception {
        logger.debug("ENTERING recipientUserDAO.create()");
        Connection conn = null;

        try {
            conn = this.getMemoConnection();
            logger.debug("Connection established.");
            // Подготавливаем запрос для добавления служебки
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO recipientsUsers(MemoID, RecipientDeptID, RecipientUserID, Subject, RealizedDate, " +
                            "Realized, UpdateUserID, AppointedUserID, SendEmail) VALUES (?,?,?,?,?,?,?,?,?)");

            stmt.setInt(1, recipient.getMemoID());
            stmt.setInt(2, recipient.getRecipientDeptID());
            stmt.setInt(3, recipient.getRecipientUserID());
            stmt.setString(4, recipient.getSubject());
            stmt.setString(5, JLibUtils.dateStrToPattern(recipient.getRealizedDate(), Defaults.DATE_PATTERN, Defaults.DATE_MSSQL_PATTERN, null));
            stmt.setInt(6, recipient.getRealized());
            stmt.setInt(7, recipient.getUpdateUserID());
            stmt.setInt(8, recipient.getAppointedUserID());
            stmt.setInt(9, recipient.getSendemail());
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
        logger.debug("LEAVING recipientUserDAO.create()");
    }

    /**
     * Метод обновляет(изменяет) запись в таблице recipientsUsers по шаблону recipientUserDTO, переданному в качестве
     * параметра. Идентификатор изменяемой записи также берется из шаблона. Если записи с таким идентификатором
     * не существует или переданный шаблон пуст или равен null, то никаких действий не происходит.
     *
     * @param dH        - Держатель ссылок на все DAO-компоненты приложения
     * @param recipient - объект пользователь-получатель служебки
     * @throws Exception - отлавливаем ошибку выполнения транзакции
     */
    public void update(daoHandler dH, recipientUserDTO recipient) throws Exception {
        logger.debug("ENTERING into recipientUserDAO.update().");
        Connection conn = null;

        try {
            // Вначале найдем изменяемую запись
            if ((recipient == null) || (dH.getRecipientUserDAO().findByID(dH, recipient.getId()) == null))
                throw new Exception("Recipient doesn't exists(nothing to update) !");
            logger.debug("Starting sql-query generation.");

            conn = this.getMemoConnection();
            logger.debug("Connection established.");
            // Подготавливаем запрос для добавления служебки
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE recipientsUsers SET Realized = ?, Answer = ? WHERE id = ?");

            stmt.setInt(1, recipient.getRealized());
            stmt.setString(2, recipient.getAnswer());
            stmt.setInt(3, recipient.getId());
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
        logger.debug("LEAVING recipientUserDAO.update().");
    }
}