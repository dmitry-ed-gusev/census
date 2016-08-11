package org.census.commons.dao.simple.docs;

import org.census.commons.dao.hibernate.personnel.DepartmentsSimpleDao;
import org.census.commons.dao.hibernate.personnel.EmployeesSimpleDao;
import org.census.commons.dto.docs.memoDTO;
//import org.census.commons.dto.docs.recipientDeptDTO;
import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dto.personnel.employees.EmployeeDto;
import org.census.commons.utils.JLibUtils;

import java.sql.*;
import java.util.*;
import java.util.Date;



/**
 * Данный класс предназначен для непосредственной работы с объектами "служебная записка" (memoDTO).
 * Класс реализует методы поиска, создания и обновления служебной записки. Методы данного класса
 * работают с объектами memoDTO.
 * Наследует методы класса DSCommonDAO, в том числе getConnection(), который получает конекцию с сервером
 * @author Gusev Dmitry
 * @version 1.0
*/

public class  memoDAO extends Connectors
 {
  //private static int counter = 0;

     private DepartmentsSimpleDao departmentsDao;
     private EmployeesSimpleDao   employeesDao;

  //public memoDAO(){counter++; logger.info("************** MEMO DAO COUNT: " + counter);}

  /*
  public static int getCounter() {return counter;}

     @Override
     protected void finalize() throws Throwable
     {
       counter--;
       logger.info("************** MEMO DAO REAL COUNT: " + counter);
     }*/

     /**
   * Данный метод производит поиск служебной записки по переданным параметрам и
   * возвращает список(List) найденных служебок или значение null, если ничего
   * не найдено. Параметры с постфиксом *_type - тип поиска по данному (одноименному)
   * парметру. Ести четыре типа поиска (см. константы в модуле Defaults.java):
   * @param memoNumber int - номер служебки для поиска. Если указанный номер <= 0 - данный параметр
   * игнорируется в запросе на поиск.
   * @param subject String - тема служебки для поиска. Если параметр пуст или равен null, то в запросе он
   * игнорируется.
   * данный параметр в запросе игнорируется.
   *
   * @param dH - Держатель ссылок на все DAO-компоненты приложения
   * @param date_start String - начало интервала времени создания служебки
   * @param date_end String - конец интервала времени создания служебки
   * @param executorDeptId int - идентификатор отдела-отправителя служебки
   * @param recipientDeptId int - идентификатор отдела-получателя служебки
   * @param executorUserId - идентификатор пользователя создавшего служебку
   * @return List - список найденных служебок (объектов memoDTO), найденных по соотв. критериям.
  */
  public List find(daoHandler dH, int memoNumber, String subject, String date_start, String date_end,
                   int executorDeptId, int recipientDeptId, int executorUserId){
      
      logger.debug("ENTERING into memoDAO.find().");
      Connection conn = null;
      ResultSet rs;
      List<memoDTO>memoList = null;

      try {

          conn = this.getMemoConnection();

          //Посмотрим какие параметры были заполнены пользователем, и включим данные значения в запрос
          String where = "";
          if(memoNumber > 0){
              where = where + ("AND memoNumber = ? ");
          }
          if(subject != null && !subject.trim().equals("")){
              where = where + ("AND lower(Subject) LIKE ? ");
          }
          if(executorDeptId > 0){
              where = where + ("AND ExecutorDeptID = ? ");
          }
          if(recipientDeptId > 0){
              where = where + ("AND recipientDeptID = ? ");
          }
          if (executorUserId > 0){
              where = where + ("AND ExecutorUserID = ? ");
          }
          
          PreparedStatement stmt = conn.prepareStatement(
                 "SELECT DISTINCT memos.ID, memos.memoNumber, memos.Subject, memos.ExecutorDeptID, " +
                         "memos.SendDate, memos.TimeStamp " +
                     "FROM memos INNER JOIN recipientsDepts ON memos.ID = recipientsDepts.MemoID " +
                     "WHERE memos.TimeStamp >= ? AND memos.TimeStamp < ? " + where + "ORDER BY memos.TimeStamp desc LIMIT 50");

          int i = 0;

          //Устанавливаем значенияе даты  "Начала выборки" в нужном формате
          if ((date_start != null) && (!date_start.trim().equals(""))) {
              i++;
              stmt.setString(i, JLibUtils.dateStrToPattern(date_start, Defaults.DATE_PATTERN, Defaults.DATE_MSSQL_PATTERN, null));
          } else {
              i++;
              stmt.setNull(i, Types.DATE);}

          //Устанавливаем значенияе даты  "Окончания выборки" в нужном формате
          if ((date_end != null) && (!date_end.trim().equals(""))) {
              i++;
              //Поставим дату окончания след день, т.к. при выборке ставится время 0 часов 0 минут
              //и документы за текущую дату не попадают в интервал
              Date dat = JLibUtils.dateToPeriod(date_end, Defaults.DATE_PATTERN, 1, JLibUtils.DatePeriod.DAY);
              stmt.setString(i, JLibUtils.dateToPattern(dat, Defaults.DATE_MSSQL_PATTERN, null));
          } else{
              i++;
              stmt.setNull(i, Types.DATE);}

          //Добавляем в запрос номер служебной записки
          if(memoNumber > 0){
              i++;
              stmt.setInt(i, memoNumber);
          }
          //Добавляем поле "Тема"
          if(subject != null && !subject.trim().equals("")){
              i++;
              stmt.setString (i, '%' + subject.trim().toLowerCase() + '%');
          }
          // Добавляем в запрос поле "отдел-отправитель служебки"
          if(executorDeptId > 0){
              i++;
              stmt.setInt   (i, executorDeptId);
          }
          // Добавляем в запрос поле "отдел-получатель служебки"
          if(recipientDeptId > 0){
              i++;
              stmt.setInt   (i, recipientDeptId);
          }
          // Добавляем поле "Подготовил служебную записку"
          if (executorUserId > 0){
              i++;
              stmt.setInt (i, executorUserId);
          }

          rs = stmt.executeQuery();
          logger.debug("Query executed.");
          

          // Если что-нить нашли - работаем
          if (rs.next()) {
              logger.debug("ResultSet is NOT EMPTY! Processing.");
              memoList = new ArrayList<memoDTO>();
              do {
                  memoDTO memo = new memoDTO();
                  // Идентификатор служебки
                  memo.setId(rs.getInt("ID"));
                  // Номер служебки
                  memo.setMemoNumber(rs.getInt("MemoNumber"));
                  // Тема служебки
                  memo.setSubject(rs.getString("Subject"));
                  // Идентификатор отдела-отправителя служебки
                  memo.setExecutorDeptID(rs.getInt("ExecutorDeptID"));
                  // Трехзначный код отдела-отправителя
                  DepartmentDto execDept = this.departmentsDao.findById(memo.getExecutorDeptID());
                  memo.setExecutorDeptCode(execDept.getCode());
                  // Список отделов-получателей служебки
                  memo.setRecipientsDepts(dH.getRecipientDeptDAO().findAllByMemo(dH, memo.getId(), Defaults.STATUS_UNDELETED));
                  // Дата отправки служебки
                  memo.setSendDate(JLibUtils.dateToPattern(rs.getDate("SendDate"), Defaults.DATE_PATTERN,null));
                  // Дата создания служебки (приводим к нужному формату)
                  memo.setTimeStamp(JLibUtils.dateToPattern(rs.getDate("TimeStamp"), Defaults.DATE_PATTERN,null));
                  // Добавим созданную служебку в список
                  memoList.add(memo);
              }
              while (rs.next());
          }
          // Если ничего не нашли - отладочная запись в лог
          else logger.debug("ResultSet is empty!");
      }
      // Перехват ИС
      catch (Exception e) {
          logger.error("Error occured: " + e.getMessage() );
      }
      // Освобождение ресурсов
      finally{
          try {if (conn != null) conn.close();}
          catch (Exception e_res) {logger.error("Can't close connection! [" + e_res.getMessage() + "]");}
      }
      logger.debug("LEAVING memoDAO.find().");
      return memoList;
  }

  /**
   * Метод возвращает служебку по ее идентификатору. Если такой служебки не найдено, то метод вернет значение null.
   *
   * @param dH - Держатель ссылок на все DAO-компоненты приложения
   * @param memoID int - идентификатор служебки, которую надо найти.
   * @return memoDTO - объект "служебка", если мы ее нашли, если же нет - занчение null.
  */
  public memoDTO findByID(daoHandler dH, int memoID) {
      logger.debug("ENTERING memoDAO.findByID(" + memoID + ").");
      Connection conn = null;
      ResultSet rs;
      memoDTO memo = null;
      String str;

      try {

          conn = this.getMemoConnection();
          PreparedStatement stmt = conn.prepareStatement(
                 "SELECT memos.ID, memos.MemoNumber, memos.ParentID, memos.Subject, memos.Note, " +
                         "memos.ExecutorDeptID, memos.ExecutorUserID, memos.RealizedDate, memos.SendDate, " +
                         "memos.SendUserId, memos.AttachedFile, memos.TimeStamp, memos_text.Text " +
                     "FROM memos INNER JOIN memos_text ON memos_text.MemoID = memos.ID " +
                     "WHERE memos.ID = ?");

          stmt.setInt(1, memoID);
          logger.debug("Statement created.");

          rs = stmt.executeQuery();
          logger.debug("Query executed.");

          // Если что-нить нашли - работаем
          if (rs.next()) {
              logger.debug("ResultSet is NOT EMPTY! Processing.");
              memo = new memoDTO();
              // Идентификатор служебки
              memo.setId(rs.getInt("ID"));
              // Идентификатор служебки, ответом на которую является данная
              memo.setParentID(rs.getInt("ParentID"));
              // Номер служебной записки
              memo.setMemoNumber(rs.getInt("MemoNumber"));
              // Тема служебки
              memo.setSubject(rs.getString("Subject"));
              // Текст служебки
              memo.setText(rs.getString("Text").replaceAll("\n", "\n<br>"));
              // Примечание
              memo.setNote(rs.getString("Note"));
              // Идентификатор исполнителя служебки
              memo.setExecutorUserID(rs.getInt("ExecutorUserID"));

              // Фамилия И.О. исполнителя служебки
              EmployeeDto execEmployee = this.employeesDao.findById(memo.getExecutorUserID());

              //memo.setExecutorShortName(execEmployee.getShortRusName());
              // Идентификатор отдела-отправителя служебки
              memo.setExecutorDeptID(rs.getInt("ExecutorDeptID"));

              //Найдем начальника подразделения отправителя
              EmployeeDto  simpleEmployeeChief = this.departmentsDao.getChiefOfDept(rs.getInt("ExecutorDeptID"));
              if(simpleEmployeeChief != null){
                  // Данные о начальнике подразделения из БД"Кадры"
                  memo.setChiefdeptexecutor(simpleEmployeeChief);

                  // Трехзначный код отдела-отправителя
                  // todo: temporary solution!
                  Set<DepartmentDto> depts = simpleEmployeeChief.getDepartments();
                  memo.setExecutorDeptCode((depts != null && depts.size() > 0 ? depts.iterator().next().getCode() : null));
              }

              // Идентификатор отправителя служебки
              memo.setSendUserId(rs.getInt("SendUserId"));
              // Фамилия И.О. отправителя служебки
              if (memo.getSendUserId() > 0) {
                  EmployeeDto sendEmployee = this.employeesDao.findById(memo.getSendUserId());
                  //memo.setSendShortName(sendEmployee.getShortRusName());
              }

              // Прикрепленный файл
              str = rs.getString("AttachedFile");
              if ((str != null) && (!str.trim().equals(""))) memo.setAttachedFile(str);
              // Список отделов-получателей служебки
              memo.setRecipientsDepts(dH.getRecipientDeptDAO().findAllByMemo(dH, memo.getId(), Defaults.STATUS_UNDELETED));
              // Список служебок, являющихся ответом на данную
              memo.setMemoChild(dH.getMemoDAO().findChildMemo(dH, memo.getId(), Defaults.STATUS_UNDELETED));
              // Срок ответа отделами на данную служебку
              memo.setRealizedDate(JLibUtils.dateToPattern(rs.getDate("RealizedDate"), Defaults.DATE_PATTERN,null));
              // Список сотрудников-получателей служебки
              memo.setRecipientsUsers(dH.getRecipientUserDAO().findAllByMemo(dH, memo.getId(), Defaults.STATUS_UNDELETED));
              // Дата создания служебки
              memo.setTimeStamp(JLibUtils.dateToPattern(rs.getDate("TimeStamp"), Defaults.DATE_PATTERN,null));
              // Дата отправки служебки (если она была отправлена)
              memo.setSendDate(JLibUtils.dateToPattern(rs.getDate("SendDate"), Defaults.DATE_PATTERN,null));
          }
          // Если ничего не нашли - отладочная запись в лог
          else logger.debug("ResultSet is empty!");
      }
      // Перехват ИС
      catch (Exception e) {
          logger.error("Error occured [" + memoID + "]:" + e.getMessage());
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
      logger.debug("LEAVING memoDAO.findByID().");
      return memo;
  }

  /**
   * Метод возвращает кол-во служебок в ящиках "входящие"/"исходящие" для отдела с кодом deptID.
   * Если отдела deptID не существует или указано число меньшее 0 - метод вернет 0.
   * Параметр deleted указывает считаем мы кол-во всех служебок (deleted <> 0,1), только активных/неудаленных
   * (deleted = 0[Defaults.MEMO_TYPE_UNDELETED]) или только неактивных/удаленных (deleted = 1[Defaults.MEMO_TYPE_DELETED]).
   * Параметр year указывает год, для которого считается кол-во служебок (год - положительное целое, напр.
   * 2004, 2005 .....). Если параметр year <= 0, то метод возвращает кол-во служебок в "исходящих" для текущего года.
   * Параметр boxType указывает на тип ящика, для которого мы считаем кол-во служебок. Тип ящика может быть -
   * "входящие"[Defaults.INBOX] или "исходящие"[Defaults.OUTBOX].
   *
   * @param dH - Держатель ссылок на все DAO-компоненты приложения
   * @param boxType String - тип ящика для возвращаемого кол-ва служебок, тип может быть Defaults.OUTBOX или
   * Defaults.INBOX.
   * @param deptID int - идентификатор отдела (из БД "Кадры"), для которого считаем кол-во служебок
   * @param deleted int - тип служебок, которые считаем - удаленные или активные.
   * @return int - кол-во служебок, посчитанное по указанным критериям.
  */
  public int boxCount(daoHandler dH, String boxType, long deptID, int deleted) {
      logger.debug("ENTERING into memoDAO.boxCount().");
      int res = 0;
      Connection conn = null;
      ResultSet rs;

      try {

          // Для начала проверим параметры deptID и boxType - если они неверены, возвращаем 0.
          if ((boxType == null) || (boxType.trim().equals("")) || (deptID <= 0))
              throw new Exception("Empty boxType [" + boxType + "] or deptID [" + deptID + "] parameter!");
          logger.debug("boxType[" + boxType + "] and deptID[" + deptID + "] parameters are not empty. Processing.");
          
          conn = this.getMemoConnection();
          PreparedStatement stmt;

          // В зависимости от типа ящика формируем sql-запрос
          if (boxType.equals(Defaults.OUTBOX)){
              // <- считаем кол-во "исходящих"
              stmt = conn.prepareStatement(
                "SELECT count(ID) as count FROM memos " +
                    "WHERE ExecutorDeptID = ? " +
                        "AND memos.deleted = ? " +
                        "AND memos.timestamp > ?");
          }
          else if (boxType.equals(Defaults.INBOX)){
              // <- считаем кол-во "входящих"
              stmt = conn.prepareStatement(
                "SELECT count(memos.ID) as count FROM memos, recipientsDepts " +
                    "WHERE memos.ID = recipientsDepts.MemoID " +
                        "AND NOT (memos.SendDate IS NULL) " +
                        "AND recipientsDepts.RecipientDeptID = ? " +
                        "AND memos.deleted = ? " +
                        "AND memos.timestamp > ?");
          }
          else throw new Exception("Invalid box type [" + boxType + "]!");

          //Идентификатор подразделения получателя/отправителя
          //stmt.setLong(1, deptID);
          // Тип служебок для подсчета - удаленные/активные
          //stmt.setInt(2, deleted);
          
          // Текущую дату откручиваем 6 месяцев назад, запросы выполняются за полгода
          Date dat = JLibUtils.dateToPeriod(new Date(), -Defaults.SEARCH_INTERVAL, JLibUtils.DatePeriod.MONTH);
          //stmt.setString(3, JLibUtils.dateToPattern(dat, Defaults.DATE_MSSQL_PATTERN, null));

          logger.debug("Statement created.");

          rs = stmt.executeQuery();
          logger.debug("Query executed.");

          // Если что-нить нашли - работаем
          if (rs.next()) res = rs.getInt("count");
              // Если ничего не нашли - отладочная запись в лог
          else logger.debug("ResultSet is empty!");
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
      logger.debug("LEAVING into memoDAO.boxCount().");
      return res;
  }

  /**
   * Данный метод реализует ящик "входящих"/"исходящих" служебных записок - те записки,
   * которые были получены данным отделом или созданы и отправлены в другие отделы.
   * Параметр boxType указывает на тип ящика, для которого мы считаем кол-во служебок. Тип ящика может быть -
   * "входящие"[Defaults.INBOX] или "исходящие"[Defaults.OUTBOX].
   * Если отдела deptID не существует или указано число меньшее 0 - метод вернет 0.
   * Параметр deleted указывает все ли служебки мы воводим (deleted <> 0,1),
   * только активные/неудаленные (deleted = 0[Defaults.STATUS_UNDELETED]) или
   * только неактивные/удаленные (deleted = 1[Defaults.STATUS_DELETED]).
   * Параметр year указывает год, для которого выводятся служебки в ящике (год -
   * положительное целое, напр. 2004, 2005 .....). Если параметр year <= 0, то
   * метод возвращает кол-во служебок в ящике для текущего года.
   * Параметр pageNumber указывает какую страницу из ящика служебок
   * мы хотим получить: значение < 0 - метод вернет null, при значении параметра
   * pageNumber = 0, метод вернет ВСЕ служебки в ящике (конечно, в зависимости
   * от указанного года и статуса deleted), а при значении > 0 - необходимую страницу ящика.
   * Размер страницы (кол-во служебок на одной странице) определяется параметром
   * Defaults.PAGE_SIZE из класса констант (Defaults) данного модуля.
   *
   * @param dH - Держатель ссылок на все DAO-компоненты приложения
   * @param boxType String - тип ящика для возвращаемого списка служебок, тип может быть Defaults.OUTBOX или
   * Defaults.INBOX.
   * @param boxSubType String - подтип ящика для возвращаемого списка служебок. Для ящика "входящие" подтип
   * может быть Defaults.INBOX_ALL(все) и Defaults.INBOX_NO_ANSWER(требующие ответ и не имеющие его), любые другие
   * значения, а также null для данного поля игнорируются - возвращаются все "входящие". Для ящика "исходящие"
   * подтип может быть Defaults.OUTBOX_ALL(все) и Defaults.OUTBOX_NO_ANSWER(служебки, на которые получатель не ответил),
   * любые другие значения, а также null для данного поля игнорируются - возвращаются все "исходящие".
   * @param deptID int - идентификатор отдела (из БД "Кадры"), для которого возвращаем список служебок.
   * @param deleted int - тип служебок, которые возвращаем - удаленные или активные.
   * @param pageNumber int - служебки в ящике выводятся постранично, размер страницы указывается в константе
   * Defaults.PAGE_SIZE, данный параметр указывает, какую по номеру страницу необходимо вернуть. Если параметр
   * < 0 - метод вернет null, если параметр = 0 - метод вернет все служебки, соотв. критерию, если же
   * параметр > 0 - метод вернет указанную страницу из списка служебок. Если указана страница, превышающая
   * кол-во возвращаемых служебок(колво_служебок < номер_стр*колво_служебок_на_стр ) - метод вернет null.
   * @return List - список объектов memoDTO, которые были найдены по соотв. критериям поиска.
  */
  public List box (daoHandler dH, String boxType, String boxSubType, long deptID, int deleted, int pageNumber) {
      logger.debug("ENTERING into memoDAO.box().");
      Connection conn = null;
      ResultSet rs;
      List memoList = null;
      memoDTO memo;

      try {

          // Для начала проверим параметры  deptID и pageNumber - если они неверен, возвращаем null.
          if ((boxType == null) || (boxType.trim().equals("")) || (deptID <= 0) || (pageNumber < 0))
              throw new Exception("Empty boxType[" + boxType + "] or deptID[" + deptID + "] or pageNumber[" + pageNumber + "] parameter!");

          conn = this.getMemoConnection();
          // Создаваемый нами объект Statement должен генерировать курсоры, которые могут перематываться вперед
          // и назад, а также быть readonly. ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
          PreparedStatement stmt;

          // Текущую дату откручиваем 6 месяцев назад, запросы выполняются за полгода
          Date dat = JLibUtils.dateToPeriod(new Date(), -Defaults.SEARCH_INTERVAL, JLibUtils.DatePeriod.MONTH);
          String date_start = JLibUtils.dateToPattern(dat, Defaults.DATE_MSSQL_PATTERN, null);

          // В зависимости от типа ящика формируем sql-запрос
          if (boxSubType.equals(Defaults.OUTBOX_NO_ANSWER)){
              // Запрос на выборку служебных записок на которые не получены ответы
              stmt = conn.prepareStatement(
                "SELECT memos.ID, memos.MemoNumber, memos.Subject, memos.ExecutorUserID, memos.ExecutorDeptID, " +
                        "memos.SendDate, memos.TimeStamp " +
                    "FROM memos, recipientsDepts " +
                    "WHERE memos.ExecutorDeptID = ? " +
                        "AND NOT (memos.sendDate IS NULL) " +
                        "AND NOT (memos.realizedDate IS NULL) " +
                        "AND memos.ID = recipientsDepts.MemoID " +
                        "AND recipientsDepts.realized = 0 " +
                        "AND memos.deleted = ? " +
                        "AND memos.timestamp > ? " +
                    "ORDER BY ID DESC",
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

              stmt.setLong(1, deptID);
              stmt.setInt(2, deleted);
              stmt.setString(3, date_start);
          }
          else if (boxType.equals(Defaults.OUTBOX)){
              // Запрос на выборку служебных записок, которые являются "Исходящими" для данного подразделения
              stmt = conn.prepareStatement(
                "SELECT ID, MemoNumber, Subject, ExecutorUserID, ExecutorDeptID, SendDate, TimeStamp " +
                    "FROM memos WHERE ExecutorDeptID = ? " +
                        "AND memos.deleted = ? " +
                        "AND memos.timestamp > ? " +
                    "ORDER BY ID DESC LIMIT " + Defaults.PAGE_SIZE * pageNumber,
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

              stmt.setLong(1, deptID);
              stmt.setInt(2, deleted);
              stmt.setString(3, date_start);
          }
          else if(boxSubType.equals(Defaults.INBOX_NO_ANSWER)){
              // Запрос на выборку служебных записок на которые требуется ответить
              stmt = conn.prepareStatement(
                "SELECT memos.ID, memos.MemoNumber, memos.Subject, " +
                        "memos.ExecutorDeptID, memos.TimeStamp, memos.RealizedDate, memos.SendDate, " +
                        "recipientsDepts.Realized " +
                    "FROM memos, recipientsDepts " +
                    "WHERE memos.ID = recipientsDepts.MemoID " +
                        "AND NOT (memos.SendDate IS NULL) " +
                        "AND recipientsDepts.Realized = 0 " +
                        "AND NOT (memos.realizedDate IS NULL) " +
                        "AND recipientsDepts.RecipientDeptID = ? " +
                        "AND memos.deleted = ? " +
                        "AND memos.timestamp > ? " +
                    "ORDER BY ID DESC LIMIT " + Defaults.PAGE_SIZE * pageNumber,
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              
              stmt.setLong(1, deptID);
              stmt.setInt(2, deleted);
              stmt.setString(3, date_start);
          }
          else if (boxSubType.equals(Defaults.INBOX_NO_ANSWER_DATE)){
              // Запрос на выборку служебных записок, которые требуют ответа на текущую дату                            
              stmt = conn.prepareStatement(
                "SELECT memos.ID, memos.MemoNumber, memos.Subject, " +
                        "memos.ExecutorDeptID, memos.TimeStamp, memos.RealizedDate, memos.SendDate, " +
                        "recipientsDepts.Realized " +
                    "FROM memos, recipientsDepts " +
                    "WHERE memos.ID = recipientsDepts.MemoID " +
                        "AND NOT (memos.SendDate IS NULL) " +
                        //"AND NOT (memos.RealizedDate IS NULL) " +
                        "AND recipientsDepts.Realized = 0 " +
                        "AND recipientsDepts.RecipientDeptID = ? " +
                        "AND memos.deleted = ? " +
                        "AND memos.timestamp > ? " +
                        "AND memos.RealizedDate < ? " +
                    "ORDER BY ID DESC LIMIT " + Defaults.PAGE_SIZE * pageNumber,
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              
              stmt.setLong(1, deptID);
              stmt.setInt(2, deleted);
              stmt.setString(3, date_start);
              stmt.setString(4, JLibUtils.dateToPattern(new Date(), Defaults.DATE_MSSQL_PATTERN,null));
          }
          else if (boxType.equals(Defaults.INBOX)){
              // Запрос на выборку служебных записок, которые являются "Входящими" для данного подразделения
              stmt = conn.prepareStatement(
                "SELECT memos.ID, memos.MemoNumber, memos.Subject, " +
                        "memos.ExecutorDeptID, memos.TimeStamp, memos.RealizedDate, memos.SendDate, " +
                        "recipientsDepts.Realized " +
                    "FROM memos, recipientsDepts " +
                    "WHERE memos.ID = recipientsDepts.MemoID " +
                        "AND NOT (memos.SendDate IS NULL) " +
                        "AND recipientsDepts.RecipientDeptID = ? " +
                        "AND memos.deleted = ? " +
                        "AND memos.timestamp > ? " +
                    "ORDER BY ID DESC LIMIT " + Defaults.PAGE_SIZE * pageNumber,
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

              stmt.setLong(1, deptID);
              stmt.setInt(2, deleted);
              stmt.setString(3, date_start);
          }
          else throw new Exception("Invalid box type [" + boxType + "]!");

          logger.debug("Statement created.");

          rs = stmt.executeQuery();
          logger.debug("Query executed.");

          // Если что-нить нашли - работаем
          if (rs.next()) {
              logger.debug("ResultSet is NOT EMPTY! Processing.");
              // Если мы выводим непосредственно одну страницу (pageNumber > 1), то необходимо перемотать записи до этой страницы.
              // Если указана первая станица (pageNumber = 1) - перемотка не нужна!
              // Если указана страница, превышающая кол-во возвращаемых служебок(колво_служебок < номер_стр*колво_служебок_на_стр ),
              // то необходимо предотвратить ошибку (мы достигнем конца курсора раньше, чем достигнем нужной страницы)
              if (pageNumber > 1) {
                  // Непосредственно перемотка записей до последней записи на странице, предшествующей той, которую
                  // мы хотим вернуть из данного метода.
                  int Counter = 1;
                  do {
                      Counter++;
                  } while ((Counter <= (Defaults.PAGE_SIZE * (pageNumber - 1))) && (rs.next()));
                  // Если после перемотки записей больше нет - ошибка! После выполнения данного оператора указатель в
                  // курсоре данных будет установлен на первую запись той страницы, которую мы хотим вывести на экран
                  if (!rs.next()) throw new Exception("PageNumber is too big!");
              }

              memoList = new ArrayList();
              do {
                  memo = new memoDTO();
                  // Идентификатор служебки
                  memo.setId(rs.getInt("ID"));
                  // Номер служебки
                  memo.setMemoNumber(rs.getInt("memoNumber"));
                  // Тема служебки
                  memo.setSubject(rs.getString("Subject"));
                  // Дата создания служебки (приводим к нужному формату)
                  memo.setTimeStamp(JLibUtils.dateToPattern(rs.getDate("TimeStamp"), Defaults.DATE_PATTERN,null));
                  // Список отделов-получателей служебки
                  memo.setRecipientsDepts(dH.getRecipientDeptDAO().findAllByMemo(dH, memo.getId(), Defaults.STATUS_UNDELETED));
                  // Дата отправки служебки (если она была отправлена)
                  memo.setSendDate(JLibUtils.dateToPattern(rs.getDate("SendDate"), Defaults.DATE_PATTERN,null));
                  // Поля, которые используются только в одном из ящиков - "исходящие"
                  if (boxType.equals(Defaults.OUTBOX)) {
                      // Идентификатор исполнителя служебки
                      memo.setExecutorUserID(rs.getInt("ExecutorUserID"));

                      // Фамилия И.О. исполнителя служебки
                      EmployeeDto simpleEmployee = this.employeesDao.findById(memo.getExecutorUserID());

                      //if (simpleEmployee != null) memo.setExecutorShortName(simpleEmployee.getShortRusName());
                      //else memo.setExecutorShortName("-");
                  }
                  // Поля, которые используются только в одном из ящиков - "входящие"
                  if (boxType.equals(Defaults.INBOX)) {
                      // Идентификатор отдела-отправителя служебки
                      memo.setExecutorDeptID(rs.getInt("ExecutorDeptID"));
                      // Трехзначный код отдела-отправителя
                      DepartmentDto execDept = this.departmentsDao.findById((long) memo.getExecutorDeptID());
                      memo.setExecutorDeptCode(execDept.getCode());
                      // Срок ответа на данную служебку
                      memo.setRealizedDate(JLibUtils.dateToPattern(rs.getDate("RealizedDate"), Defaults.DATE_PATTERN,null));
                      // Если дата срока ответа менее текущей, то присвоим значение "просрочено"
                      // затем еще в JSP проверяется был ли ответ на служебку
                      if (memo.getRealizedDate() != null) {
                          // Получаем григорианский календарь с текущей датой
                          Calendar cal2 = new GregorianCalendar();
                          if (rs.getDate("RealizedDate").before(cal2.getTime())) {
                              memo.setOverdue(1);
                          }
                      }
                  }
                  // Добавим созданную служебку в список
                  memoList.add(memo);
              }
              while (rs.next());
          }

          // Если ничего не нашли - отладочная запись в лог
          else logger.debug("ResultSet is empty!");
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
      logger.debug("LEAVING memoDAO.box().");
      return memoList;
  }

  /**
   * Метод создает новую служебку. В качестве параметра метод получает непосредственно
   * объект memoDTO (служебка) и список объектов recipientDTO (получатель). Выполняется
   * транзакция, в котрой происходит добавление записей в две таблицы: memos и recipients.
   * Если список получателей пуст - служебка не регистрируется. Метод возвращает идентификатор
   * только что зарегистрированной служебки (>0), если же произошла ошибка и/или служебка не
   * была зарегистрирована - метод вернет значение = -1.
   *
   * @param dH - Держатель ссылок на все DAO-компоненты приложения
   * @param memo - объект служебная записка
   * @param recipientsDeptsList - список объектов подразделений получателей
   * @return - идентификатор созданной записи
   * @throws Exception - ошибка транзакции при создании
   */
  public int create(daoHandler dH, memoDTO memo, List recipientsDeptsList) throws Exception {
      logger.debug("ENTERING into memoDAO.create().");
      Connection conn = null;
      // Подготовленный sql-запрос для добавления слежебки
      PreparedStatement memoStmt;
      // Идентификатор только что добавленной служебки.
      int memoID = -1;
      try {
          // если список получателей пуст - ошибка
          if ((recipientsDeptsList == null) || (recipientsDeptsList.size() <= 0))
              throw new Exception("Recipients list is EMPTY!");

          conn = this.getMemoConnection();
          logger.debug("Connection established.");
          // Подготавливаем запрос для добавления служебки
          memoStmt = conn.prepareStatement(
                  "INSERT INTO memos(ParentID, Subject, Note, ExecutorDeptID, ExecutorUserID, AttachedFile, " +
                  "UpdateUserID, RealizedDate, ExecWorkgroupId) " +
                  "VALUES (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);          

          logger.debug("memoSql prepared.");

          // Непосредственное выполнение транзакции. Транзакия заключена в блок try...catch для того,
          // чтобы в случае ошибки внутри транзакции можно было откатит ее(транзакцию) назад.
          try {
              
              conn.createStatement().executeUpdate("start transaction;");
              logger.debug("Transaction started.");
              // Заполняем параметрами запрос на создание служебки
              memoStmt.setInt(1, memo.getParentID());
              memoStmt.setString(2, memo.getSubject());
              memoStmt.setString(3, memo.getNote());
              memoStmt.setLong(4, memo.getExecutorDeptID());
              memoStmt.setLong(5, memo.getExecutorUserID());
              memoStmt.setString(6, memo.getAttachedFile());
              memoStmt.setLong(7, memo.getUpdateUserID());
              // Если дата ответа передана нам - форматируем ее из строки в тип java.sql.Date
              // (в противном случае устанавливаем значение поля RealizedDate в null)
              // Строка для перевода в формат java.sql.Date должна содержать дату в виде yyyy-mm-dd             
              String realizedDate = JLibUtils.dateStrToPattern(memo.getRealizedDate(), Defaults.DATE_PATTERN, Defaults.DATE_MSSQL_PATTERN,null);
              memoStmt.setString(8, realizedDate);
              
              memoStmt.setInt(9,    memo.getExecWorkgroupId());

              // Выполняем запрос на создание служебки
              logger.debug("Trying to execute memoStmt query.");
              memoStmt.executeUpdate();

              //Определение идентификатора ID, вcтавленной записи
              ResultSet rs = memoStmt.getGeneratedKeys();
              if (rs.next()) {
                  memoID = rs.getInt(1);
              }

              // Завершаем транзакцию
              conn.createStatement().executeUpdate("commit;");
          }
          // Перехватываем ошибку внутри транзакциии аннулируем ее (транзакцию)
          catch (Exception e) {
              logger.error("Error in transaction: [" + e.getMessage() + "]!");
              // Непосредственный откат транзакции
              conn.createStatement().executeUpdate("rollback;");
              logger.error("Transaction aborted (\"rollback transaction\" executed)!");
              // Передадим ИС дальше - чтобы не выполнились операторы добавления
              // получателей служебки в таблицу recipients
              throw new Exception("Transaction aborted! New memo not created!");
          }

          // Если мы не получили номер - ошибка
          if (memoID <= 0) throw new Exception("Last inserted memo ID not recieved [memoID = " + memoID + "]!");
          logger.debug("Transaction finished. Recieved memoID = " + memoID);
          
          // Нам необходимо заполнить поле "year" таблицы memos (данное поле содержит
          // целочисленное значение года из поля TimeStamp таблицы memos). Заполнять это
          // поле можно также триггером - но пока заполняем его программно (триггер
          // понадобится для увеличения быстродействия при добавлении/регистрации новой
          // служебки).
          logger.debug("Updating table [memos] - setting year value.");
          PreparedStatement stmt = conn.prepareStatement("UPDATE memos set year = YEAR(TimeStamp) WHERE ID = ?");
          stmt.setInt(1,memoID);
          stmt.executeUpdate();

          // Вычисляем максимальный идентификатор в таблице служебок, для определения номера СЗ в этом году          
          stmt = conn.prepareStatement("SELECT MAX(ID) AS MAXID FROM memos WHERE year = ?");
          //stmt.setInt (1, new GregorianCalendar().get(Calendar.YEAR) - 1);
          ResultSet rs = stmt.executeQuery();

          //Определеяем номер в текущем году, и пробиваем в таблицу
          if (rs.next()) {
              stmt = conn.prepareStatement("UPDATE memos set MemoNumber = ? WHERE id = ?");
              stmt.setInt(1,(memoID - rs.getInt("MAXID")));
              stmt.setInt(2,memoID);
              stmt.executeUpdate();
              
              logger.debug("Calculated MemoNumber = [" + (memoID - rs.getInt("MAXID")) + "].");
          } else logger.error("Can't calculate MemoNumber value!");
    
          // Записываем текст служебки в таблицу memos_text
          logger.debug("Inserting memo text into table [memos_text].");
          stmt = conn.prepareStatement("INSERT INTO memos_text(Text, MemoID) VALUES (?,?)");
          stmt.setString(1,memo.getText());
          stmt.setInt(2,memoID);
          stmt.executeUpdate();
                    
          // Создаем записи в таблице получателей данной служебки
          logger.debug("Creating recipients for this memo (table [recipients]).");
          // В цикле проходим по списку получателей и создаем их
          for (Object aObject : recipientsDeptsList) {
              // Берем из списка получателя служебки
              //recipientDeptDTO recipientDept = (recipientDeptDTO) aObject;
              // Присваиваем идентификатор созданной служебки
              //recipientDept.setMemoID(memoID);
              // Создаем запись в таблице получателей
              //dH.getRecipientDeptDAO().create(dH, recipientDept);
          }
          logger.debug("Recipients departments created.");
          
      }
      // Перехват ИС
      catch (Exception e) {
          // Запись ошибки в лог-файл
          logger.error("ERROR occured: " + e.getMessage());
          // Передадим ошибку дальше - на уровень вверх
          throw new Exception(e.getMessage());
      }
      // Освобождаем ресурсы
      finally {
          try {
              if (conn != null) conn.close();
          }
          catch (Exception e_res) {
              logger.error("Can't close resources! [" + e_res.getMessage() + "]");
          }
      }
      logger.debug("LEAVING memoDAO.create().");
      return memoID;
  }

  /**
   * Метод изменяет уже существующую служебку. В качестве шаблона для изменения используется
   * переданный методу объект memoDTO. Также изменяется список получателей - если переданный
   * методу список recipientsDeptsList не пуст, при этом старые получатели удаляются, а новые
   * создаются (происходит физическое удаление записей из таблицы recipientsDepts).
   *
   * @param dH - Держатель ссылок на все DAO-компоненты приложения
   * @param memo memoDTO
   * @param recipientsDeptsList List
   * @throws Exception ИС возникает, если в процессе изменения записи происходит ошибка (тогда
   * запись не обновляется).
  */
  public void update(daoHandler dH, memoDTO memo, List recipientsDeptsList) throws Exception {
      logger.debug("ENTERING into memoDAO.update().");
      Connection conn = null;

      try {

          conn = this.getMemoConnection();
          PreparedStatement stmt;

          //Подготавливаем запсрос об изменении данных в служебной записке
          stmt = conn.prepareStatement(
                    "UPDATE memos SET parentid = ?, Subject = ?, Note = ?, AttachedFile = ?, RealizedDate = ?, " +
                            "SendDate = ?, UpdateUserID = ?, SendUserId = ? WHERE id = ?");

          // Номер служебки, отчетом на которую является эта
          stmt.setInt   (1, memo.getParentID());
          // Тема
          stmt.setString(2, memo.getSubject());
          // Примечание
          stmt.setString(3, memo.getNote());
          // Добавляем в запрос поле AttachedFile, приложение
          stmt.setString(4, memo.getAttachedFile());
          // Если дата ответа передана нам - форматируем ее из строки в yyyymmdd
          stmt.setString(5, JLibUtils.dateStrToPattern(memo.getRealizedDate(), Defaults.DATE_PATTERN, Defaults.DATE_MSSQL_PATTERN,null));
          //Проделываем формативание даты отправки как и RealizedDate
          stmt.setString(6, JLibUtils.dateStrToPattern(memo.getSendDate(), Defaults.DATE_PATTERN, Defaults.DATE_MSSQL_PATTERN,null));
          //Идентификатор пользователя изменяющий данные по служебке
          stmt.setLong   (7, memo.getUpdateUserID());
          //Идентификатор пользователя отправивший слуэебку
          stmt.setLong   (8, memo.getSendUserId());
          //Идентификатор служебной записки
          stmt.setInt   (9, memo.getId());
          logger.debug("Statement dataChange created.");

          stmt.executeUpdate();
          logger.debug("Query dataChange executed.");

          //Подготавливаем запрос на изменение текста служебной записки
          stmt = conn.prepareStatement(
                    "UPDATE memos_text SET Text = ?, UpdateUserID = ? WHERE MemoID = ?");

          //Текст записки
          stmt.setString(1, memo.getText());
          //Идентификатор пользователя изменяющий текс служебки
          stmt.setLong   (2, memo.getUpdateUserID());
          //Идентификатор служебной записки
          stmt.setInt   (3, memo.getId());
          logger.debug("Statement memoText created.");

          stmt.executeUpdate();
          logger.debug("Query memoText executed.");

          // Если список отделов-получателей не пуст - создаем получателей (перед этим удаляем старых, если нет параметра deptID)
          if ((recipientsDeptsList != null) && (recipientsDeptsList.size() > 0)) {

              //Подготавливаем запрос на удаление всех получателей данной служебки (выполняется перед
              // созданием нового списка получателей, если он не пуст)
              stmt = conn.prepareStatement(
                        "DELETE FROM recipientsDepts WHERE MemoID = ?");

              stmt.setInt   (1, memo.getId());
              logger.debug("Statement deleteRecipientDepts created.");

              stmt.executeUpdate();
              logger.debug("Query deleteRecipientDepts executed.");
              
              logger.debug("Current recipientsDepts list cleared! Creating new recipients Depts list.");
              // В цикле проходим по списку получателей и создаем их
              for (Object aObject : recipientsDeptsList) {
                  // Берем из списка получателя служебки
                  //recipientDeptDTO recipientDept = (recipientDeptDTO) aObject;
                  // Создаем запись в таблице получателей
                  //dH.getRecipientDeptDAO().create(dH, recipientDept);
              }

              logger.debug("New recipients Depts list crated!");
          }
      }
      // Перехватываем ИС
      catch (Exception e) {
          // Запись ошибки в лог-файл
          logger.error("ERROR occured: " + e.getMessage());
          // Передадим ошибку дальше - на уровень вверх
          throw new Exception(e.getMessage());
      }
      // Обязательно освобождаем ресурсы
      finally {
          try {
              if (conn != null) conn.close();
          }
          catch (Exception e_res) {
              logger.error("Can't close connection! [" + e_res.getMessage() + "]");
          }
      }
      logger.debug("LEAVING memoDAO.update().");
  }
  /**
   * Метод возвращает список служебок, которые являются ответом на служебку с parentID
   *
   * @param dH - Держатель ссылок на все DAO-компоненты приложения
   * @param parentID int - идентификатор служебки, ответы на которую надо найти.
   * @param deleted - 1 - признак что запись удалена
   * @return memoDTO - объект "служебка", если мы ее нашли, если же нет - занчение null.
  */
  public List findChildMemo(daoHandler dH, int parentID, int deleted) {
      logger.debug("ENTERING memoDAO.findChildMemo().");
      Connection conn = null;
      ResultSet rs;
      List memoChildList = null;

      try {

          conn       = this.getMemoConnection();
          PreparedStatement stmt = conn.prepareStatement(
                  "SELECT memos.ID, memos.MemoNumber, memos.ExecutorDeptID, memos.sendDate " +
                      "FROM memos " +
                      "WHERE memos.parentID = ? AND memos.deleted = ? " +
                      "ORDER BY memos.ExecutorDeptID");

          stmt.setInt(1, parentID);
          stmt.setInt(2, deleted);
          logger.debug("Statement created.");

          rs = stmt.executeQuery();
          logger.debug("Query executed.");

          // Если что-нить нашли - работаем
          if (rs.next()) {

              logger.debug("ResultSet is NOT EMPTY! Processing.");
              do {   // Проверим, если служебка не отправлена, то и не учитываем ее
                  if (rs.getDate("sendDate") != null) {
                      memoDTO memo = new memoDTO();
                      // Идентификатор служебки
                      memo.setId(rs.getInt("ID"));
                      // Номер служебной записки
                      memo.setMemoNumber(rs.getInt("MemoNumber"));
                      // Идентификатор отдела-отправителя служебки
                      memo.setExecutorDeptID(rs.getInt("ExecutorDeptID"));
                      // Трехзначный код отдела-отправителя
                      logger.debug("ID = " + memo.getExecutorDeptID());
                      DepartmentDto execDept = this.departmentsDao.findById(memo.getExecutorDeptID());
                      memo.setExecutorDeptCode(execDept.getCode());

                      if(execDept != null){
                          logger.debug("DEPTCODE = " + execDept.getCode());
                      }else{
                          logger.debug("NOTTTTT!!!!");
                      }

                      // Инициализируем список
                      if (memoChildList == null) memoChildList = new ArrayList();
                      // Добавляем объект в список
                      memoChildList.add(memo);
                  }
              }
              while (rs.next());
          }
          // Если ничего не нашли - отладочная запись в лог
          else logger.debug("ResultSet is empty!");
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
      logger.debug("LEAVING memoDAO.findChildMemo().");
      return memoChildList;
  }
}
