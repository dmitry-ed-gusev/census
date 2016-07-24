package memorandum.dataModel.dao;

import jpersonnel.dto.SimpleDepartmentDTO;
import jpersonnel.dto.SimpleEmployeeDTO;
import memorandum.*;
import memorandum.dataModel.dto.memoDTO;
import memorandum.dataModel.dto.recipientDeptDTO;
import memorandum.dataModel.system.Connectors;
import jlib.utils.JLibUtils;
import static jlib.utils.JLibUtils.DatePeriod;

import java.sql.*;
import java.util.*;
import java.util.Date;


/**
 * ������ ����� ������������ ��� ���������������� ������ � ��������� "��������� �������" (memoDTO).
 * ����� ��������� ������ ������, �������� � ���������� ��������� �������. ������ ������� ������
 * �������� � ��������� memoDTO.
 * ��������� ������ ������ DSCommonDAO, � ��� ����� getConnection(), ������� �������� �������� � ��������
 * @author Gusev Dmitry
 * @version 1.0
*/

public class  memoDAO extends Connectors
 {
  //private static int counter = 0;

  public memoDAO(){}
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
   * ������ ����� ���������� ����� ��������� ������� �� ���������� ���������� �
   * ���������� ������(List) ��������� �������� ��� �������� null, ���� ������
   * �� �������. ��������� � ���������� *_type - ��� ������ �� ������� (������������)
   * ��������. ���� ������ ���� ������ (��. ��������� � ������ Defaults.java):
   * @param memoNumber int - ����� �������� ��� ������. ���� ��������� ����� <= 0 - ������ ��������
   * ������������ � ������� �� �����.
   * @param subject String - ���� �������� ��� ������. ���� �������� ���� ��� ����� null, �� � ������� ��
   * ������������.
   * ������ �������� � ������� ������������.
   *
   * @param dH - ��������� ������ �� ��� DAO-���������� ����������
   * @param date_start String - ������ ��������� ������� �������� ��������
   * @param date_end String - ����� ��������� ������� �������� ��������
   * @param executorDeptId int - ������������� ������-����������� ��������
   * @param recipientDeptId int - ������������� ������-���������� ��������
   * @param executorUserId - ������������� ������������ ���������� ��������
   * @return List - ������ ��������� �������� (�������� memoDTO), ��������� �� �����. ���������.
  */
  public List find(daoHandler dH, int memoNumber, String subject, String date_start, String date_end,
                   int executorDeptId, int recipientDeptId, int executorUserId){
      
      logger.debug("ENTERING into memoDAO.find().");
      Connection conn = null;
      ResultSet rs;
      List<memoDTO>memoList = null;

      try {

          conn = this.getMemoConnection();

          //��������� ����� ��������� ���� ��������� �������������, � ������� ������ �������� � ������
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

          //������������� ��������� ����  "������ �������" � ������ �������
          if ((date_start != null) && (!date_start.trim().equals(""))) {
              i++;
              stmt.setString(i, JLibUtils.dateStrToPattern(date_start, Defaults.DATE_PATTERN, Defaults.DATE_MSSQL_PATTERN, null));
          } else {
              i++;
              stmt.setNull(i, Types.DATE);}

          //������������� ��������� ����  "��������� �������" � ������ �������
          if ((date_end != null) && (!date_end.trim().equals(""))) {
              i++;
              //�������� ���� ��������� ���� ����, �.�. ��� ������� �������� ����� 0 ����� 0 �����
              //� ��������� �� ������� ���� �� �������� � ��������
              Date dat = JLibUtils.dateToPeriod(date_end, Defaults.DATE_PATTERN, 1, DatePeriod.DAY);
              stmt.setString(i, JLibUtils.dateToPattern(dat, Defaults.DATE_MSSQL_PATTERN, null));
          } else{
              i++;
              stmt.setNull(i, Types.DATE);}

          //��������� � ������ ����� ��������� �������
          if(memoNumber > 0){
              i++;
              stmt.setInt(i, memoNumber);
          }
          //��������� ���� "����"
          if(subject != null && !subject.trim().equals("")){
              i++;
              stmt.setString (i, '%' + subject.trim().toLowerCase() + '%');
          }
          // ��������� � ������ ���� "�����-����������� ��������"
          if(executorDeptId > 0){
              i++;
              stmt.setInt   (i, executorDeptId);
          }
          // ��������� � ������ ���� "�����-���������� ��������"
          if(recipientDeptId > 0){
              i++;
              stmt.setInt   (i, recipientDeptId);
          }
          // ��������� ���� "���������� ��������� �������"
          if (executorUserId > 0){
              i++;
              stmt.setInt (i, executorUserId);
          }

          rs = stmt.executeQuery();
          logger.debug("Query executed.");
          

          // ���� ���-���� ����� - ��������
          if (rs.next()) {
              logger.debug("ResultSet is NOT EMPTY! Processing.");
              memoList = new ArrayList<memoDTO>();
              do {
                  memoDTO memo = new memoDTO();
                  // ������������� ��������
                  memo.setId(rs.getInt("ID"));
                  // ����� ��������
                  memo.setMemoNumber(rs.getInt("MemoNumber"));
                  // ���� ��������
                  memo.setSubject(rs.getString("Subject"));
                  // ������������� ������-����������� ��������
                  memo.setExecutorDeptID(rs.getInt("ExecutorDeptID"));
                  // ����������� ��� ������-�����������
                  SimpleDepartmentDTO execDept = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, memo.getExecutorDeptID(), "getSimpleDepartmentByID");
                  memo.setExecutorDeptCode(execDept.getDepartmentCode());
                  // ������ �������-����������� ��������
                  memo.setRecipientsDepts(dH.getRecipientDeptDAO().findAllByMemo(dH, memo.getId(), Defaults.STATUS_UNDELETED));
                  // ���� �������� ��������
                  memo.setSendDate(JLibUtils.dateToPattern(rs.getDate("SendDate"), Defaults.DATE_PATTERN,null));
                  // ���� �������� �������� (�������� � ������� �������)
                  memo.setTimeStamp(JLibUtils.dateToPattern(rs.getDate("TimeStamp"), Defaults.DATE_PATTERN,null));
                  // ������� ��������� �������� � ������
                  memoList.add(memo);
              }
              while (rs.next());
          }
          // ���� ������ �� ����� - ���������� ������ � ���
          else logger.debug("ResultSet is empty!");
      }
      // �������� ��
      catch (Exception e) {
          logger.error("Error occured: " + e.getMessage() );
      }
      // ������������ ��������
      finally{
          try {if (conn != null) conn.close();}
          catch (Exception e_res) {logger.error("Can't close connection! [" + e_res.getMessage() + "]");}
      }
      logger.debug("LEAVING memoDAO.find().");
      return memoList;
  }

  /**
   * ����� ���������� �������� �� �� ��������������. ���� ����� �������� �� �������, �� ����� ������ �������� null.
   *
   * @param dH - ��������� ������ �� ��� DAO-���������� ����������
   * @param memoID int - ������������� ��������, ������� ���� �����.
   * @return memoDTO - ������ "��������", ���� �� �� �����, ���� �� ��� - �������� null.
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

          // ���� ���-���� ����� - ��������
          if (rs.next()) {
              logger.debug("ResultSet is NOT EMPTY! Processing.");
              memo = new memoDTO();
              // ������������� ��������
              memo.setId(rs.getInt("ID"));
              // ������������� ��������, ������� �� ������� �������� ������
              memo.setParentID(rs.getInt("ParentID"));
              // ����� ��������� �������
              memo.setMemoNumber(rs.getInt("MemoNumber"));
              // ���� ��������
              memo.setSubject(rs.getString("Subject"));
              // ����� ��������
              memo.setText(rs.getString("Text").replaceAll("\n", "\n<br>"));
              // ����������
              memo.setNote(rs.getString("Note"));
              // ������������� ����������� ��������
              memo.setExecutorUserID(rs.getInt("ExecutorUserID"));
              // ������� �.�. ����������� ��������
              SimpleEmployeeDTO execEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, memo.getExecutorUserID(),"getSimpleEmployeeByID");
              memo.setExecutorShortName(execEmployee.getShortName());
              // ������������� ������-����������� ��������
              memo.setExecutorDeptID(rs.getInt("ExecutorDeptID"));

              //������ ���������� ������������� �����������
              SimpleEmployeeDTO  simpleEmployeeChief = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, rs.getInt("ExecutorDeptID"),"getChiefDepartment");
              if(simpleEmployeeChief != null){                  
                  // ������ � ���������� ������������� �� ��"�����"
                  memo.setChiefdeptexecutor(simpleEmployeeChief);
                  // ����������� ��� ������-�����������
                  memo.setExecutorDeptCode(simpleEmployeeChief.getDepartmentCode());
              }

              // ������������� ����������� ��������
              memo.setSendUserId(rs.getInt("SendUserId"));
              // ������� �.�. ����������� ��������
              if (memo.getSendUserId() > 0) {
                  SimpleEmployeeDTO sendEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, memo.getSendUserId(),"getSimpleEmployeeByID");
                  memo.setSendShortName(sendEmployee.getShortName());
              }

              // ������������� ����
              str = rs.getString("AttachedFile");
              if ((str != null) && (!str.trim().equals(""))) memo.setAttachedFile(str);
              // ������ �������-����������� ��������
              memo.setRecipientsDepts(dH.getRecipientDeptDAO().findAllByMemo(dH, memo.getId(), Defaults.STATUS_UNDELETED));
              // ������ ��������, ���������� ������� �� ������
              memo.setMemoChild(dH.getMemoDAO().findChildMemo(dH, memo.getId(), Defaults.STATUS_UNDELETED));
              // ���� ������ �������� �� ������ ��������
              memo.setRealizedDate(JLibUtils.dateToPattern(rs.getDate("RealizedDate"), Defaults.DATE_PATTERN,null));
              // ������ �����������-����������� ��������
              memo.setRecipientsUsers(dH.getRecipientUserDAO().findAllByMemo(dH, memo.getId(), Defaults.STATUS_UNDELETED));
              // ���� �������� ��������
              memo.setTimeStamp(JLibUtils.dateToPattern(rs.getDate("TimeStamp"), Defaults.DATE_PATTERN,null));
              // ���� �������� �������� (���� ��� ���� ����������)
              memo.setSendDate(JLibUtils.dateToPattern(rs.getDate("SendDate"), Defaults.DATE_PATTERN,null));
          }
          // ���� ������ �� ����� - ���������� ������ � ���
          else logger.debug("ResultSet is empty!");
      }
      // �������� ��
      catch (Exception e) {
          logger.error("Error occured [" + memoID + "]:" + e.getMessage());
      }
      // ����������� ����� ���������� �������
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
   * ����� ���������� ���-�� �������� � ������ "��������"/"���������" ��� ������ � ����� deptID.
   * ���� ������ deptID �� ���������� ��� ������� ����� ������� 0 - ����� ������ 0.
   * �������� deleted ��������� ������� �� ���-�� ���� �������� (deleted <> 0,1), ������ ��������/�����������
   * (deleted = 0[Defaults.MEMO_TYPE_UNDELETED]) ��� ������ ����������/��������� (deleted = 1[Defaults.MEMO_TYPE_DELETED]).
   * �������� year ��������� ���, ��� �������� ��������� ���-�� �������� (��� - ������������� �����, ����.
   * 2004, 2005 .....). ���� �������� year <= 0, �� ����� ���������� ���-�� �������� � "���������" ��� �������� ����.
   * �������� boxType ��������� �� ��� �����, ��� �������� �� ������� ���-�� ��������. ��� ����� ����� ���� -
   * "��������"[Defaults.INBOX] ��� "���������"[Defaults.OUTBOX].
   *
   * @param dH - ��������� ������ �� ��� DAO-���������� ����������
   * @param boxType String - ��� ����� ��� ������������� ���-�� ��������, ��� ����� ���� Defaults.OUTBOX ���
   * Defaults.INBOX.
   * @param deptID int - ������������� ������ (�� �� "�����"), ��� �������� ������� ���-�� ��������
   * @param deleted int - ��� ��������, ������� ������� - ��������� ��� ��������.
   * @return int - ���-�� ��������, ����������� �� ��������� ���������.
  */
  public int boxCount(daoHandler dH, String boxType, int deptID, int deleted) {
      logger.debug("ENTERING into memoDAO.boxCount().");
      int res = 0;
      Connection conn = null;
      ResultSet rs;

      try {

          // ��� ������ �������� ��������� deptID � boxType - ���� ��� ��������, ���������� 0.
          if ((boxType == null) || (boxType.trim().equals("")) || (deptID <= 0))
              throw new Exception("Empty boxType [" + boxType + "] or deptID [" + deptID + "] parameter!");
          logger.debug("boxType[" + boxType + "] and deptID[" + deptID + "] parameters are not empty. Processing.");
          
          conn = this.getMemoConnection();
          PreparedStatement stmt;

          // � ����������� �� ���� ����� ��������� sql-������
          if (boxType.equals(Defaults.OUTBOX)){
              // <- ������� ���-�� "���������"
              stmt = conn.prepareStatement(
                "SELECT count(ID) as count FROM memos " +
                    "WHERE ExecutorDeptID = ? " +
                        "AND memos.deleted = ? " +
                        "AND memos.timestamp > ?");
          }
          else if (boxType.equals(Defaults.INBOX)){
              // <- ������� ���-�� "��������"
              stmt = conn.prepareStatement(
                "SELECT count(memos.ID) as count FROM memos, recipientsDepts " +
                    "WHERE memos.ID = recipientsDepts.MemoID " +
                        "AND NOT (memos.SendDate IS NULL) " +
                        "AND recipientsDepts.RecipientDeptID = ? " +
                        "AND memos.deleted = ? " +
                        "AND memos.timestamp > ?");
          }
          else throw new Exception("Invalid box type [" + boxType + "]!");

          //������������� ������������� ����������/�����������
          stmt.setInt(1, deptID);
          // ��� �������� ��� �������� - ���������/��������
          stmt.setInt(2, deleted);
          
          // ������� ���� ����������� 6 ������� �����, ������� ����������� �� �������
          Date dat = JLibUtils.dateToPeriod(new Date(), -Defaults.SEARCH_INTERVAL, DatePeriod.MONTH);
          stmt.setString(3, JLibUtils.dateToPattern(dat, Defaults.DATE_MSSQL_PATTERN, null));

          logger.debug("Statement created.");

          rs = stmt.executeQuery();
          logger.debug("Query executed.");

          // ���� ���-���� ����� - ��������
          if (rs.next()) res = rs.getInt("count");
              // ���� ������ �� ����� - ���������� ������ � ���
          else logger.debug("ResultSet is empty!");
      }
      // �������� ��
      catch (Exception e) {
          logger.error("Error occured: " + e.getMessage());
      }
      // ����������� ����� ���������� �������
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
   * ������ ����� ��������� ���� "��������"/"���������" ��������� ������� - �� �������,
   * ������� ���� �������� ������ ������� ��� ������� � ���������� � ������ ������.
   * �������� boxType ��������� �� ��� �����, ��� �������� �� ������� ���-�� ��������. ��� ����� ����� ���� -
   * "��������"[Defaults.INBOX] ��� "���������"[Defaults.OUTBOX].
   * ���� ������ deptID �� ���������� ��� ������� ����� ������� 0 - ����� ������ 0.
   * �������� deleted ��������� ��� �� �������� �� ������� (deleted <> 0,1),
   * ������ ��������/����������� (deleted = 0[Defaults.STATUS_UNDELETED]) ���
   * ������ ����������/��������� (deleted = 1[Defaults.STATUS_DELETED]).
   * �������� year ��������� ���, ��� �������� ��������� �������� � ����� (��� -
   * ������������� �����, ����. 2004, 2005 .....). ���� �������� year <= 0, ��
   * ����� ���������� ���-�� �������� � ����� ��� �������� ����.
   * �������� pageNumber ��������� ����� �������� �� ����� ��������
   * �� ����� ��������: �������� < 0 - ����� ������ null, ��� �������� ���������
   * pageNumber = 0, ����� ������ ��� �������� � ����� (�������, � �����������
   * �� ���������� ���� � ������� deleted), � ��� �������� > 0 - ����������� �������� �����.
   * ������ �������� (���-�� �������� �� ����� ��������) ������������ ����������
   * Defaults.PAGE_SIZE �� ������ �������� (Defaults) ������� ������.
   *
   * @param dH - ��������� ������ �� ��� DAO-���������� ����������
   * @param boxType String - ��� ����� ��� ������������� ������ ��������, ��� ����� ���� Defaults.OUTBOX ���
   * Defaults.INBOX.
   * @param boxSubType String - ������ ����� ��� ������������� ������ ��������. ��� ����� "��������" ������
   * ����� ���� Defaults.INBOX_ALL(���) � Defaults.INBOX_NO_ANSWER(��������� ����� � �� ������� ���), ����� ������
   * ��������, � ����� null ��� ������� ���� ������������ - ������������ ��� "��������". ��� ����� "���������"
   * ������ ����� ���� Defaults.OUTBOX_ALL(���) � Defaults.OUTBOX_NO_ANSWER(��������, �� ������� ���������� �� �������),
   * ����� ������ ��������, � ����� null ��� ������� ���� ������������ - ������������ ��� "���������".
   * @param deptID int - ������������� ������ (�� �� "�����"), ��� �������� ���������� ������ ��������.
   * @param deleted int - ��� ��������, ������� ���������� - ��������� ��� ��������.
   * @param pageNumber int - �������� � ����� ��������� �����������, ������ �������� ����������� � ���������
   * Defaults.PAGE_SIZE, ������ �������� ���������, ����� �� ������ �������� ���������� �������. ���� ��������
   * < 0 - ����� ������ null, ���� �������� = 0 - ����� ������ ��� ��������, �����. ��������, ���� ��
   * �������� > 0 - ����� ������ ��������� �������� �� ������ ��������. ���� ������� ��������, �����������
   * ���-�� ������������ ��������(�����_�������� < �����_���*�����_��������_��_��� ) - ����� ������ null.
   * @return List - ������ �������� memoDTO, ������� ���� ������� �� �����. ��������� ������.
  */
  public List box (daoHandler dH, String boxType, String boxSubType, int deptID, int deleted, int pageNumber) {
      logger.debug("ENTERING into memoDAO.box().");
      Connection conn = null;
      ResultSet rs;
      List memoList = null;
      memoDTO memo;

      try {

          // ��� ������ �������� ���������  deptID � pageNumber - ���� ��� �������, ���������� null.
          if ((boxType == null) || (boxType.trim().equals("")) || (deptID <= 0) || (pageNumber < 0))
              throw new Exception("Empty boxType[" + boxType + "] or deptID[" + deptID + "] or pageNumber[" + pageNumber + "] parameter!");

          conn = this.getMemoConnection();
          // ����������� ���� ������ Statement ������ ������������ �������, ������� ����� �������������� ������
          // � �����, � ����� ���� readonly. ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
          PreparedStatement stmt;

          // ������� ���� ����������� 6 ������� �����, ������� ����������� �� �������
          Date dat = JLibUtils.dateToPeriod(new Date(), -Defaults.SEARCH_INTERVAL, DatePeriod.MONTH);
          String date_start = JLibUtils.dateToPattern(dat, Defaults.DATE_MSSQL_PATTERN, null);

          // � ����������� �� ���� ����� ��������� sql-������
          if (boxSubType.equals(Defaults.OUTBOX_NO_ANSWER)){
              // ������ �� ������� ��������� ������� �� ������� �� �������� ������
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

              stmt.setInt(1, deptID);
              stmt.setInt(2, deleted);
              stmt.setString(3, date_start);
          }
          else if (boxType.equals(Defaults.OUTBOX)){
              // ������ �� ������� ��������� �������, ������� �������� "����������" ��� ������� �������������
              stmt = conn.prepareStatement(
                "SELECT ID, MemoNumber, Subject, ExecutorUserID, ExecutorDeptID, SendDate, TimeStamp " +
                    "FROM memos WHERE ExecutorDeptID = ? " +
                        "AND memos.deleted = ? " +
                        "AND memos.timestamp > ? " +
                    "ORDER BY ID DESC LIMIT " + Defaults.PAGE_SIZE * pageNumber,
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

              stmt.setInt(1, deptID);
              stmt.setInt(2, deleted);
              stmt.setString(3, date_start);
          }
          else if(boxSubType.equals(Defaults.INBOX_NO_ANSWER)){
              // ������ �� ������� ��������� ������� �� ������� ��������� ��������
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
              
              stmt.setInt(1, deptID);
              stmt.setInt(2, deleted);
              stmt.setString(3, date_start);
          }
          else if (boxSubType.equals(Defaults.INBOX_NO_ANSWER_DATE)){
              // ������ �� ������� ��������� �������, ������� ������� ������ �� ������� ����                            
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
              
              stmt.setInt(1, deptID);
              stmt.setInt(2, deleted);
              stmt.setString(3, date_start);
              stmt.setString(4, JLibUtils.dateToPattern(new Date(), Defaults.DATE_MSSQL_PATTERN,null));
          }
          else if (boxType.equals(Defaults.INBOX)){
              // ������ �� ������� ��������� �������, ������� �������� "���������" ��� ������� �������������
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

              stmt.setInt(1, deptID);
              stmt.setInt(2, deleted);
              stmt.setString(3, date_start);
          }
          else throw new Exception("Invalid box type [" + boxType + "]!");

          logger.debug("Statement created.");

          rs = stmt.executeQuery();
          logger.debug("Query executed.");

          // ���� ���-���� ����� - ��������
          if (rs.next()) {
              logger.debug("ResultSet is NOT EMPTY! Processing.");
              // ���� �� ������� ��������������� ���� �������� (pageNumber > 1), �� ���������� ���������� ������ �� ���� ��������.
              // ���� ������� ������ ������� (pageNumber = 1) - ��������� �� �����!
              // ���� ������� ��������, ����������� ���-�� ������������ ��������(�����_�������� < �����_���*�����_��������_��_��� ),
              // �� ���������� ������������� ������ (�� ��������� ����� ������� ������, ��� ��������� ������ ��������)
              if (pageNumber > 1) {
                  // ��������������� ��������� ������� �� ��������� ������ �� ��������, �������������� ���, �������
                  // �� ����� ������� �� ������� ������.
                  int Counter = 1;
                  do {
                      Counter++;
                  } while ((Counter <= (Defaults.PAGE_SIZE * (pageNumber - 1))) && (rs.next()));
                  // ���� ����� ��������� ������� ������ ��� - ������! ����� ���������� ������� ��������� ��������� �
                  // ������� ������ ����� ���������� �� ������ ������ ��� ��������, ������� �� ����� ������� �� �����
                  if (!rs.next()) throw new Exception("PageNumber is too big!");
              }

              memoList = new ArrayList();
              do {
                  memo = new memoDTO();
                  // ������������� ��������
                  memo.setId(rs.getInt("ID"));
                  // ����� ��������
                  memo.setMemoNumber(rs.getInt("memoNumber"));
                  // ���� ��������
                  memo.setSubject(rs.getString("Subject"));
                  // ���� �������� �������� (�������� � ������� �������)
                  memo.setTimeStamp(JLibUtils.dateToPattern(rs.getDate("TimeStamp"), Defaults.DATE_PATTERN,null));
                  // ������ �������-����������� ��������
                  memo.setRecipientsDepts(dH.getRecipientDeptDAO().findAllByMemo(dH, memo.getId(), Defaults.STATUS_UNDELETED));
                  // ���� �������� �������� (���� ��� ���� ����������)
                  memo.setSendDate(JLibUtils.dateToPattern(rs.getDate("SendDate"), Defaults.DATE_PATTERN,null));
                  // ����, ������� ������������ ������ � ����� �� ������ - "���������"
                  if (boxType.equals(Defaults.OUTBOX)) {
                      // ������������� ����������� ��������
                      memo.setExecutorUserID(rs.getInt("ExecutorUserID"));
                      // ������� �.�. ����������� ��������
                      SimpleEmployeeDTO simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, memo.getExecutorUserID(),"getSimpleEmployeeByID");
                      if (simpleEmployee != null) memo.setExecutorShortName(simpleEmployee.getShortName());
                      else memo.setExecutorShortName("-");
                  }
                  // ����, ������� ������������ ������ � ����� �� ������ - "��������"
                  if (boxType.equals(Defaults.INBOX)) {
                      // ������������� ������-����������� ��������
                      memo.setExecutorDeptID(rs.getInt("ExecutorDeptID"));
                      // ����������� ��� ������-�����������
                      SimpleDepartmentDTO execDept = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, memo.getExecutorDeptID(), "getSimpleDepartmentByID");
                      memo.setExecutorDeptCode(execDept.getDepartmentCode());
                      // ���� ������ �� ������ ��������
                      memo.setRealizedDate(JLibUtils.dateToPattern(rs.getDate("RealizedDate"), Defaults.DATE_PATTERN,null));
                      // ���� ���� ����� ������ ����� �������, �� �������� �������� "����������"
                      // ����� ��� � JSP ����������� ��� �� ����� �� ��������
                      if (memo.getRealizedDate() != null) {
                          // �������� ������������� ��������� � ������� �����
                          Calendar cal2 = new GregorianCalendar();
                          if (rs.getDate("RealizedDate").before(cal2.getTime())) {
                              memo.setOverdue(1);
                          }
                      }
                  }
                  // ������� ��������� �������� � ������
                  memoList.add(memo);
              }
              while (rs.next());
          }

          // ���� ������ �� ����� - ���������� ������ � ���
          else logger.debug("ResultSet is empty!");
      }
      // �������� ��
      catch (Exception e) {
          logger.error("Error occured: " + e.getMessage());
      }
      // ����������� ����� ���������� �������
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
   * ����� ������� ����� ��������. � �������� ��������� ����� �������� ���������������
   * ������ memoDTO (��������) � ������ �������� recipientDTO (����������). �����������
   * ����������, � ������ ���������� ���������� ������� � ��� �������: memos � recipients.
   * ���� ������ ����������� ���� - �������� �� ��������������. ����� ���������� �������������
   * ������ ��� ������������������ �������� (>0), ���� �� ��������� ������ �/��� �������� ��
   * ���� ���������������� - ����� ������ �������� = -1.
   *
   * @param dH - ��������� ������ �� ��� DAO-���������� ����������
   * @param memo - ������ ��������� �������
   * @param recipientsDeptsList - ������ �������� ������������� �����������
   * @return - ������������� ��������� ������
   * @throws Exception - ������ ���������� ��� ��������
   */
  public int create(daoHandler dH, memoDTO memo, List recipientsDeptsList) throws Exception {
      logger.debug("ENTERING into memoDAO.create().");
      Connection conn = null;
      // �������������� sql-������ ��� ���������� ��������
      PreparedStatement memoStmt;
      // ������������� ������ ��� ����������� ��������.
      int memoID = -1;
      try {
          // ���� ������ ����������� ���� - ������
          if ((recipientsDeptsList == null) || (recipientsDeptsList.size() <= 0))
              throw new Exception("Recipients list is EMPTY!");

          conn = this.getMemoConnection();
          logger.debug("Connection established.");
          // �������������� ������ ��� ���������� ��������
          memoStmt = conn.prepareStatement(
                  "INSERT INTO memos(ParentID, Subject, Note, ExecutorDeptID, ExecutorUserID, AttachedFile, " +
                  "UpdateUserID, RealizedDate, ExecWorkgroupId) " +
                  "VALUES (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);          

          logger.debug("memoSql prepared.");

          // ���������������� ���������� ����������. ��������� ��������� � ���� try...catch ��� ����,
          // ����� � ������ ������ ������ ���������� ����� ���� ������� ��(����������) �����.
          try {
              
              conn.createStatement().executeUpdate("begin transaction;");
              logger.debug("Transaction started.");
              // ��������� ����������� ������ �� �������� ��������
              memoStmt.setInt(1, memo.getParentID());
              memoStmt.setString(2, memo.getSubject());
              memoStmt.setString(3, memo.getNote());
              memoStmt.setInt(4, memo.getExecutorDeptID());
              memoStmt.setInt(5, memo.getExecutorUserID());
              memoStmt.setString(6, memo.getAttachedFile());
              memoStmt.setInt(7, memo.getUpdateUserID());
              // ���� ���� ������ �������� ��� - ����������� �� �� ������ � ��� java.sql.Date
              // (� ��������� ������ ������������� �������� ���� RealizedDate � null)
              // ������ ��� �������� � ������ java.sql.Date ������ ��������� ���� � ���� yyyy-mm-dd             
              String realizedDate = JLibUtils.dateStrToPattern(memo.getRealizedDate(), Defaults.DATE_PATTERN, Defaults.DATE_MSSQL_PATTERN,null);
              memoStmt.setString(8, realizedDate);
              
              memoStmt.setInt(9,    memo.getExecWorkgroupId());

              // ��������� ������ �� �������� ��������
              logger.debug("Trying to execute memoStmt query.");
              memoStmt.executeUpdate();

              //����������� �������������� ID, �c��������� ������
              ResultSet rs = memoStmt.getGeneratedKeys();
              if (rs.next()) {
                  memoID = rs.getInt(1);
              }

              // ��������� ����������
              conn.createStatement().executeUpdate("commit transaction");
          }
          // ������������� ������ ������ ����������� ���������� �� (����������)
          catch (Exception e) {
              logger.error("Error in transaction: [" + e.getMessage() + "]!");
              // ���������������� ����� ����������
              conn.createStatement().executeUpdate("rollback transaction");
              logger.error("Transaction aborted (\"rollback transaction\" executed)!");
              // ��������� �� ������ - ����� �� ����������� ��������� ����������
              // ����������� �������� � ������� recipients
              throw new Exception("Transaction aborted! New memo not created!");
          }

          // ���� �� �� �������� ����� - ������
          if (memoID <= 0) throw new Exception("Last inserted memo ID not recieved [memoID = " + memoID + "]!");
          logger.debug("Transaction finished. Recieved memoID = " + memoID);
          
          // ��� ���������� ��������� ���� "year" ������� memos (������ ���� ��������
          // ������������� �������� ���� �� ���� TimeStamp ������� memos). ��������� ���
          // ���� ����� ����� ��������� - �� ���� ��������� ��� ���������� (�������
          // ����������� ��� ���������� �������������� ��� ����������/����������� �����
          // ��������).
          logger.debug("Updating table [memos] - setting year value.");
          PreparedStatement stmt = conn.prepareStatement("UPDATE memos set year = YEAR(TimeStamp) WHERE ID = ?");
          stmt.setInt(1,memoID);
          stmt.executeUpdate();

          // ��������� ������������ ������������� � ������� ��������, ��� ����������� ������ �� � ���� ����          
          stmt = conn.prepareStatement("SELECT MAX(ID) AS MAXID FROM memos WHERE year = ?");
          stmt.setInt (1, new GregorianCalendar().get(Calendar.YEAR) - 1);
          ResultSet rs = stmt.executeQuery();

          //����������� ����� � ������� ����, � ��������� � �������
          if (rs.next()) {
              stmt = conn.prepareStatement("UPDATE memos set MemoNumber = ? WHERE id = ?");
              stmt.setInt(1,(memoID - rs.getInt("MAXID")));
              stmt.setInt(2,memoID);
              stmt.executeUpdate();
              
              logger.debug("Calculated MemoNumber = [" + (memoID - rs.getInt("MAXID")) + "].");
          } else logger.error("Can't calculate MemoNumber value!");
    
          // ���������� ����� �������� � ������� memos_text
          logger.debug("Inserting memo text into table [memos_text].");
          stmt = conn.prepareStatement("INSERT INTO memos_text(Text, MemoID) VALUES (?,?)");
          stmt.setString(1,memo.getText());
          stmt.setInt(2,memoID);
          stmt.executeUpdate();
                    
          // ������� ������ � ������� ����������� ������ ��������
          logger.debug("Creating recipients for this memo (table [recipients]).");
          // � ����� �������� �� ������ ����������� � ������� ��
          for (Object aObject : recipientsDeptsList) {
              // ����� �� ������ ���������� ��������
              recipientDeptDTO recipientDept = (recipientDeptDTO) aObject;
              // ����������� ������������� ��������� ��������
              recipientDept.setMemoID(memoID);
              // ������� ������ � ������� �����������
              dH.getRecipientDeptDAO().create(dH, recipientDept);
          }
          logger.debug("Recipients departments created.");
          
      }
      // �������� ��
      catch (Exception e) {
          // ������ ������ � ���-����
          logger.error("ERROR occured: " + e.getMessage());
          // ��������� ������ ������ - �� ������� �����
          throw new Exception(e.getMessage());
      }
      // ����������� �������
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
   * ����� �������� ��� ������������ ��������. � �������� ������� ��� ��������� ������������
   * ���������� ������ ������ memoDTO. ����� ���������� ������ ����������� - ���� ����������
   * ������ ������ recipientsDeptsList �� ����, ��� ���� ������ ���������� ���������, � �����
   * ��������� (���������� ���������� �������� ������� �� ������� recipientsDepts).
   *
   * @param dH - ��������� ������ �� ��� DAO-���������� ����������
   * @param memo memoDTO
   * @param recipientsDeptsList List
   * @throws Exception �� ���������, ���� � �������� ��������� ������ ���������� ������ (�����
   * ������ �� �����������).
  */
  public void update(daoHandler dH, memoDTO memo, List recipientsDeptsList) throws Exception {
      logger.debug("ENTERING into memoDAO.update().");
      Connection conn = null;

      try {

          conn = this.getMemoConnection();
          PreparedStatement stmt;

          //�������������� ������� �� ��������� ������ � ��������� �������
          stmt = conn.prepareStatement(
                    "UPDATE memos SET parentid = ?, Subject = ?, Note = ?, AttachedFile = ?, RealizedDate = ?, " +
                            "SendDate = ?, UpdateUserID = ?, SendUserId = ? WHERE id = ?");

          // ����� ��������, ������� �� ������� �������� ���
          stmt.setInt   (1, memo.getParentID());
          // ����
          stmt.setString(2, memo.getSubject());
          // ����������
          stmt.setString(3, memo.getNote());
          // ��������� � ������ ���� AttachedFile, ����������
          stmt.setString(4, memo.getAttachedFile());
          // ���� ���� ������ �������� ��� - ����������� �� �� ������ � yyyymmdd
          stmt.setString(5, JLibUtils.dateStrToPattern(memo.getRealizedDate(), Defaults.DATE_PATTERN, Defaults.DATE_MSSQL_PATTERN,null));
          //����������� ������������ ���� �������� ��� � RealizedDate
          stmt.setString(6, JLibUtils.dateStrToPattern(memo.getSendDate(), Defaults.DATE_PATTERN, Defaults.DATE_MSSQL_PATTERN,null));
          //������������� ������������ ���������� ������ �� ��������
          stmt.setInt   (7, memo.getUpdateUserID());
          //������������� ������������ ����������� ��������
          stmt.setInt   (8, memo.getSendUserId());
          //������������� ��������� �������
          stmt.setInt   (9, memo.getId());
          logger.debug("Statement dataChange created.");

          stmt.executeUpdate();
          logger.debug("Query dataChange executed.");

          //�������������� ������ �� ��������� ������ ��������� �������
          stmt = conn.prepareStatement(
                    "UPDATE memos_text SET Text = ?, UpdateUserID = ? WHERE MemoID = ?");

          //����� �������
          stmt.setString(1, memo.getText());
          //������������� ������������ ���������� ���� ��������
          stmt.setInt   (2, memo.getUpdateUserID());
          //������������� ��������� �������
          stmt.setInt   (3, memo.getId());
          logger.debug("Statement memoText created.");

          stmt.executeUpdate();
          logger.debug("Query memoText executed.");

          // ���� ������ �������-����������� �� ���� - ������� ����������� (����� ���� ������� ������, ���� ��� ��������� deptID)
          if ((recipientsDeptsList != null) && (recipientsDeptsList.size() > 0)) {

              //�������������� ������ �� �������� ���� ����������� ������ �������� (����������� �����
              // ��������� ������ ������ �����������, ���� �� �� ����)
              stmt = conn.prepareStatement(
                        "DELETE FROM recipientsDepts WHERE MemoID = ?");

              stmt.setInt   (1, memo.getId());
              logger.debug("Statement deleteRecipientDepts created.");

              stmt.executeUpdate();
              logger.debug("Query deleteRecipientDepts executed.");
              
              logger.debug("Current recipientsDepts list cleared! Creating new recipients Depts list.");
              // � ����� �������� �� ������ ����������� � ������� ��
              for (Object aObject : recipientsDeptsList) {
                  // ����� �� ������ ���������� ��������
                  recipientDeptDTO recipientDept = (recipientDeptDTO) aObject;
                  // ������� ������ � ������� �����������
                  dH.getRecipientDeptDAO().create(dH, recipientDept);
              }

              logger.debug("New recipients Depts list crated!");
          }
      }
      // ������������� ��
      catch (Exception e) {
          // ������ ������ � ���-����
          logger.error("ERROR occured: " + e.getMessage());
          // ��������� ������ ������ - �� ������� �����
          throw new Exception(e.getMessage());
      }
      // ����������� ����������� �������
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
   * ����� ���������� ������ ��������, ������� �������� ������� �� �������� � parentID
   *
   * @param dH - ��������� ������ �� ��� DAO-���������� ����������
   * @param parentID int - ������������� ��������, ������ �� ������� ���� �����.
   * @param deleted - 1 - ������� ��� ������ �������
   * @return memoDTO - ������ "��������", ���� �� �� �����, ���� �� ��� - �������� null.
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

          // ���� ���-���� ����� - ��������
          if (rs.next()) {

              logger.debug("ResultSet is NOT EMPTY! Processing.");
              do {   // ��������, ���� �������� �� ����������, �� � �� ��������� ��
                  if (rs.getDate("sendDate") != null) {
                      memoDTO memo = new memoDTO();
                      // ������������� ��������
                      memo.setId(rs.getInt("ID"));
                      // ����� ��������� �������
                      memo.setMemoNumber(rs.getInt("MemoNumber"));
                      // ������������� ������-����������� ��������
                      memo.setExecutorDeptID(rs.getInt("ExecutorDeptID"));
                      // ����������� ��� ������-�����������
                      logger.debug("ID = " + memo.getExecutorDeptID());
                      SimpleDepartmentDTO execDept = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, memo.getExecutorDeptID(), "getSimpleDepartmentByID");
                      memo.setExecutorDeptCode(execDept.getDepartmentCode());

                      if(execDept != null){
                          logger.debug("DEPTCODE = " + execDept.getDepartmentCode());
                      }else{
                          logger.debug("NOTTTTT!!!!");
                      }

                      // �������������� ������
                      if (memoChildList == null) memoChildList = new ArrayList();
                      // ��������� ������ � ������
                      memoChildList.add(memo);
                  }
              }
              while (rs.next());
          }
          // ���� ������ �� ����� - ���������� ������ � ���
          else logger.debug("ResultSet is empty!");
      }
      // �������� ��
      catch (Exception e) {
          logger.error("Error occured: " + e.getMessage());
      }
      // ����������� ����� ���������� �������
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
