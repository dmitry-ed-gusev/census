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
 * ������ ����� ��������� ���������� ��������� �������� � ��������� recipientUserDTO (������ �� �������
 * recipientsUsers). ��������: �����, ��������, ���������.
 * ��������� ������ ������ DSCommonDAO, � ��� ����� getConnection(), ������� �������� �������� � ��������
 *
 * @author Gusev Dmitry
 * @version 1.0
 */
public class recipientUserDAO extends Connectors {

    public recipientUserDAO() {
    }

    /**
     * ������ ����� ���������� ������ ���� ������������� ����������� ��������,
     * ������������� ������� ���������� � �������� ���������. ���� �� � ������ �������� ��� �����������,
     * �� ����� ���������� �������� null.
     *
     * @param dH      - ��������� ������ �� ��� DAO-���������� ����������
     * @param memoID  int ������������� ��������, ��� ������� �� ���� �����������
     * @param deleted int ������ ���������� ������� - �������� ����������� �� ������
     *                Defaults - Defaults.STATUS_DELETED � Defaults.STATUS_UNDELETED. ���� �������� deleted �� ����� �� ����� ��
     *                ������ �������� - ����� ������ ��� ������ - � �������� � ���������.
     * @return List ������ �������/������������� ����������� �������� � ��������������� memoID
     *         ��� �������� null (���� ������ �� �������).
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

            // ���� ���-�� ����� - ��������� ������, ���� ��� - ���������� null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // �������������� ������
                recipientsList = new ArrayList<recipientUserDTO>();
                SimpleEmployeeDTO simpleEmployee;
                // � ����� �������� �� ������� � ��������� ������ ��������
                do {
                    recipientUserDTO recipient = new recipientUserDTO();
                    // ������������� ����������
                    recipient.setId(rs.getInt("ID"));
                    // ������������� ��������
                    recipient.setMemoID(rs.getInt("MemoID"));
                    // ������������� ������ ������������-����������
                    recipient.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                    // ������������� ������������ ����������
                    recipient.setRecipientUserID(rs.getInt("RecipientUserID"));
                    // ��������� ��� ������������, �������� �������� ��������
                    recipient.setSubject(rs.getString("Subject"));
                    // ����� ������������, �������� �������� ��������
                    recipient.setAnswer(rs.getString("Answer"));
                    // ���� ������ �� ������ �������� ��� ������� ����������
                    recipient.setRealizedDate(JLibUtils.dateToPattern(rs.getDate("RealizedDate"), Defaults.DATE_PATTERN, null));
                    // ��������� �� ������ �������� ���������� �����������
                    recipient.setRealized(rs.getInt("Realized"));
                    // ������� �������� E-mail ��� ���������� ���������
                    recipient.setSendemail(rs.getInt("sendemail"));
                    // �������� ������ "��������� ���"
                    simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, recipient.getRecipientUserID(),"getSimpleEmployeeByID");
                    // ���� ������ ������ - �������� ���������� �����. ���� ������� ������
                    if (simpleEmployee != null)
                        // ������� �.�. ������������-���������� ��������
                        recipient.setRecipientShortName(simpleEmployee.getShortName());

                    // �������� ������ "��������� ���", ��� ������� ������� ��������
                    simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, rs.getInt("appointeduserid"),"getSimpleEmployeeByID");
                    if (simpleEmployee != null)
                        // ������� �.�. ������������-������� ������� ��������
                        recipient.setAppointedUserName(simpleEmployee.getShortName());

                    // ��������� ��������� ������ � ������
                    recipientsList.add(recipient);
                }
                while (rs.next());
                logger.debug("Processing of ResultSet finished.");
            }
            // ���� ����� ������ ���� - ��������� � ���
            else logger.debug("ResultSet is EMPTY!");
        }
        // ������� ��
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        // ������������ ��������
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
     * ������ ����� ���������� ������ "��������� ���, �������� �������� ��������" [recipientUserDTO] �� ��������������
     * ���������� ��� �� ������� personnel_list �� "�����" � �� �������������� ��������� �������.
     * �������� ������� ����� � ���� ����� 1 ���������, ����� ������ ������ ������ � ������
     *
     * @param dH       - ��������� ������ �� ��� DAO-���������� ����������
     * @param memberID - ������������� ������������ �� �� "�����"
     * @param memoID   - ������������� ��������� �������
     * @param realized - ������� ������������ ���������
     * @return - ������ recipientUserDTO ��������� ���, �������� �������� ��������
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

            // ���� ���-�� ����� - ��������� ������, ���� ��� - ���������� null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // �������������� ������
                recipient = new recipientUserDTO();
                // ������������� ������-���������� �� ������� recipientsDepts
                recipient.setId(rs.getInt("ID"));
                // ������������� ��������
                recipient.setMemoID(rs.getInt("MemoID"));
                // ������������� ������, � ������� �������� ���������-���������� �� �� "�����"
                recipient.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                // ������������� ������������-���������� �������� (�� �� "�����")
                recipient.setAppointedUserID(rs.getInt("AppointedUserID"));
                // ������������� ������������-���������� �������� (�� �� "�����")
                recipient.setRecipientUserID(rs.getInt("RecipientUserID"));
                // ����������� ���������� ��������
                recipient.setSubject(rs.getString("Subject"));
                // ����� ������������ �� ���������
                recipient.setAnswer(rs.getString("Answer"));
                // ���� "���������/�����������" (Realized)
                recipient.setRealized(rs.getInt("Realized"));
                // ������� �������� E-mail ��� ���������� ���������
                recipient.setSendemail(rs.getInt("sendemail"));
                // ���� (����) ���������� ���������
                recipient.setRealizedDate(JLibUtils.dateToPattern(rs.getDate("RealizedDate"), Defaults.DATE_PATTERN, null));
                // �������� ������ "��������� ���"
                SimpleEmployeeDTO simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, recipient.getRecipientUserID(),"getSimpleEmployeeByID");
                // ���� ������ ������ - �������� ���������� �����. ���� ������� ������
                if (simpleEmployee != null)
                    // ������� �.�. ������������-���������� ��������
                    recipient.setRecipientShortName(simpleEmployee.getShortName());
            }
            // ���� ����� ������ ���� - ���������(����������) � ���.
            else logger.debug("ResultSet is EMPTY!");
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
        logger.debug("LEAVING recipientUserDAO.findByMemoDeptID().");
        return recipient;
    }

    /**
     * ������ ����� ���������� ������ "������������-����������" [recipientUserDTO] �� ��� ��������������
     * �� ������� recipientsUsers. ���� �� ������� �������������� ������ �� ������ - ����� ����������
     * �������� null.
     *
     * @param dH              - ��������� ������ �� ��� DAO-���������� ����������
     * @param recipientUserID - ������������� "������������-����������"
     * @return - ������ recipientUserDTO "������������-����������"
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

            // ���� ���-�� ����� - ��������� ������, ���� ��� - ���������� null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // �������������� ������������ ������
                recipient = new recipientUserDTO();
                // ������������� ������-���������� �� ������� recipientsDepts
                recipient.setId(rs.getInt("ID"));
                // ������������� ��������
                recipient.setMemoID(rs.getInt("MemoID"));
                // ������������� ������������-���������� �������� (�� �� "�����")
                recipient.setAppointedUserID(rs.getInt("AppointedUserID"));
                // ������������� ������, � ������� �������� ���������-���������� �� �� "�����"
                recipient.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                // ������������� ������������-���������� �������� (�� �� "�����")
                recipient.setRecipientUserID(rs.getInt("RecipientUserID"));
                // ����������� ���������� ��������
                recipient.setSubject(rs.getString("Subject"));
                // ����� ������������ �� ���������
                recipient.setAnswer(rs.getString("Answer"));
                // ���� "���������/�����������" (Realized)
                recipient.setRealized(rs.getInt("Realized"));
                // ���� (����) ���������� ���������
                recipient.setRealizedDate(JLibUtils.dateToPattern(rs.getDate("RealizedDate"), Defaults.DATE_PATTERN, null));
                // ����������� �� email
                recipient.setSendemail(rs.getInt("sendemail"));                
                // �������� ������ "��������� ���"
                SimpleEmployeeDTO simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, recipient.getRecipientUserID(),"getSimpleEmployeeByID");
                // ���� ������ ������ - �������� ���������� �����. ���� ������� ������
                if (simpleEmployee != null)
                    // ������� �.�. ������������-���������� ��������
                    recipient.setRecipientShortName(simpleEmployee.getShortName());
            }
            // ���� ����� ������ ���� - ���������(����������) � ���.
            else logger.debug("ResultSet is EMPTY!");
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
        logger.debug("LEAVING recipientUserDAO.findByID().");
        return recipient;
    }

    /**
     * ������ ����� ���������� ������ �������� �� ����������� �����������/������������ �������������.
     * ���� �� ������� �������������� ������ �� ������ - ����� ���������� �������� null.
     *
     * @param dH     - ��������� ������ �� ��� DAO-���������� ����������
     * @param DeptID - ������������� ������������� �� �� "�����"
     * @param personnelId - ������������� ��������� �� �������� ��������� ����� �� ����������� ���������
     * @return - ������ ��������� ������� �������� memoDTO
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

            //���������� ������ �������� �� ����������� ����� ������������ �������������
            if(personnelId == 0){
                stmt = conn.prepareStatement(
                    "SELECT DISTINCT TOP 100 memoid FROM recipientsusers " +
                            "WHERE realized = 0 AND recipientdeptid = ? ORDER BY memoid DESC");

                stmt.setInt(1, DeptID);

            //���������� ������ �������� �� ����������� ����������� (personnelId)    
            }else{
                stmt = conn.prepareStatement(
                    "SELECT DISTINCT TOP 100 memoid FROM recipientsusers " +
                            "WHERE realized = 0 AND recipientuserid = ? ORDER BY memoid DESC");

                stmt.setInt(1, personnelId);
            }

            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // ���� ���-�� ����� (������ �������� �� ������� ������ ������������� �� ��������) - ��������� ������,
            // ���� ��� - ���������� null
            while (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // ������ �������� �� ID
                memo = dH.getMemoDAO().findByID(dH, rs.getInt("memoid"));
                // �������������� ������
                if (memoList == null) {
                    memoList = new ArrayList<memoDTO>();
                }

                // ������� � ���� Subject ���������� � ���, ��� �� �������� ��������� �� ������� �������������
                if (memo != null && memo.getRecipientsUsers() != null) {

                    memo.setSubject(memo.getSubject() + " ; ��������:");
                    for (int i = 0; i < memo.getRecipientsUsers().size(); i++) {
                        recipientUserDTO recipientUser = (recipientUserDTO) memo.getRecipientsUsers().get(i);
                        // ��������, ���� ��������� �� ��������� � ��� �� ����� ������, �� ������� ��� ���
                        if (recipientUser.getRealized() == 0 && recipientUser.getRecipientDeptID() == DeptID) {
                            memo.setSubject(memo.getSubject() + " " + recipientUser.getRecipientShortName());
                        }
                    }
                }
                memoList.add(memo);
            }
            // ���� ����� ������ ���� - ���������(����������) � ���.
            //else logger.debug("ResultSet is EMPTY!");
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
        logger.debug("LEAVING recipientUserDAO.NotMake().");
        return memoList;
    }

    /**
     * ����� ������� ������������-���������� �������� (������ � ������� recipientsUsers � ��
     * "��������"). � �������� ������� ��� �������� ������� ������������ ���������� � ��������
     * ��������� ������ recipientUserDTO.
     *
     * @param dH        - ��������� ������ �� ��� DAO-���������� ����������
     * @param recipient - ������ ������������-���������� ��������
     * @throws Exception - ����������� ������ ���������� ����������
     */
    public void create(daoHandler dH, recipientUserDTO recipient) throws Exception {
        logger.debug("ENTERING recipientUserDAO.create()");
        Connection conn = null;

        try {
            conn = this.getMemoConnection();
            logger.debug("Connection established.");
            // �������������� ������ ��� ���������� ��������
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
        // �������� ��
        catch (Exception e) {
            // ������� � ��� ������
            logger.error("ERROR occured: " + e.getMessage());
            // ��������� ������ ������
            throw new Exception(e.getMessage());
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
        logger.debug("LEAVING recipientUserDAO.create()");
    }

    /**
     * ����� ���������(��������) ������ � ������� recipientsUsers �� ������� recipientUserDTO, ����������� � ��������
     * ���������. ������������� ���������� ������ ����� ������� �� �������. ���� ������ � ����� ���������������
     * �� ���������� ��� ���������� ������ ���� ��� ����� null, �� ������� �������� �� ����������.
     *
     * @param dH        - ��������� ������ �� ��� DAO-���������� ����������
     * @param recipient - ������ ������������-���������� ��������
     * @throws Exception - ����������� ������ ���������� ����������
     */
    public void update(daoHandler dH, recipientUserDTO recipient) throws Exception {
        logger.debug("ENTERING into recipientUserDAO.update().");
        Connection conn = null;

        try {
            // ������� ������ ���������� ������
            if ((recipient == null) || (dH.getRecipientUserDAO().findByID(dH, recipient.getId()) == null))
                throw new Exception("Recipient doesn't exists(nothing to update) !");
            logger.debug("Starting sql-query generation.");

            conn = this.getMemoConnection();
            logger.debug("Connection established.");
            // �������������� ������ ��� ���������� ��������
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE recipientsUsers SET Realized = ?, Answer = ? WHERE id = ?");

            stmt.setInt(1, recipient.getRealized());
            stmt.setString(2, recipient.getAnswer());
            stmt.setInt(3, recipient.getId());
            logger.debug("Statement created.");

            stmt.executeUpdate();
            logger.debug("Query INSERT executed.");
        }
        // �������� ��
        catch (Exception e) {
            logger.error("ERROR occured: " + e.getMessage());
            throw new Exception(e.getMessage());
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
        logger.debug("LEAVING recipientUserDAO.update().");
    }
}