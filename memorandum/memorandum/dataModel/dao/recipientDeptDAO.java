package memorandum.dataModel.dao;

import jpersonnel.dto.SimpleDepartmentDTO;
import jpersonnel.dto.SimpleEmployeeDTO;
import memorandum.daoHandler;
import memorandum.dataModel.dto.recipientDeptDTO;
import memorandum.dataModel.system.Connectors;

import java.sql.PreparedStatement;
import java.util.*;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * ����� ��������� ��������� ����������� � ��������� recipientDeptDTO (������ �� �������
 * recipientsDepts �� "��������").
 * ��������� ������ ������ DSCommonDAO, � ��� ����� getConnection(), ������� �������� �������� � ��������
 *
 * @author Gusev Dmitry
 * @version 1.0
 */
public class recipientDeptDAO extends Connectors{

    public recipientDeptDAO() {
    }

    /**
     * ������ ����� ���������� ������ ���� ������� ����������� ��������,
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

            // ���� ���-�� ����� - ��������� ������, ���� ��� - ���������� null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // �������������� ������
                recipientsList = new ArrayList();
                // � ����� �������� �� ������� � ��������� ������ ��������
                do {
                    recipientDeptDTO recipient = new recipientDeptDTO();
                    // ������������� ����������
                    recipient.setId(rs.getInt("ID"));
                    // ������������� ��������
                    recipient.setMemoID(rs.getInt("MemoID"));
                    // ������������� ������-����������
                    recipient.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                    // ��������� �� ������ �������� ���������� �����������
                    recipient.setRealized(rs.getInt("Realized"));

                    //������ ������ ������������� �� ��� ��������������
                    SimpleDepartmentDTO simpleDepartment = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, recipient.getRecipientDeptID(),"getSimpleDepartmentByID");
                    if (simpleDepartment != null) {
                        // ����������� ��� ������-���������� ��������
                        recipient.setRecipientDeptCode(simpleDepartment.getDepartmentCode());
                        // ������������ �������������
                        recipient.setRecipientDeptName(simpleDepartment.getDepartmentName());
                    }
                    recipientsList.add(recipient);
                }
                while (rs.next());

                if(recipientsList != null){
                    Collections.sort(recipientsList, new departmentsSort("recipientDeptId"));
                }

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
        logger.debug("LEAVING recipientDeptDAO.findAllByMemo("+memoID+")");
        return recipientsList;
    }

    /**
     * ������ ����� ���������� ������ ���� ������� ����������� ��������,
     * ������������� ������� ���������� � �������� ���������. ���� �� � ������ �������� ��� �����������,
     * �� ����� ���������� �������� null.
     *
     * @param dH       - ��������� ������ �� ��� DAO-���������� ����������
     * @param memoID   int ������������� ��������, ��� ������� �� ���� �����������
     *                 Defaults - Defaults.STATUS_DELETED � Defaults.STATUS_UNDELETED. ���� �������� deleted �� ����� �� ����� ��
     *                 ������ �������� - ����� ������ ��� ������ - � �������� � ���������.
     * @param realized - ������� ���������� ��������
     * @return List ������ �������/������������� ����������� �������� � ��������������� memoID
     *         ��� �������� null (���� ������ �� �������).
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

            // ���� ���-�� ����� - ��������� ������, ���� ��� - ���������� null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // �������������� ������
                recipientsList = new ArrayList();
                // � ����� �������� �� ������� � ��������� ������ ��������
                do {
                    recipientDeptDTO recipient = new recipientDeptDTO();
                    // ������������� ����������
                    recipient.setId(rs.getInt("ID"));
                    // ������������� ��������
                    recipient.setMemoID(rs.getInt("MemoID"));
                    // ������������� ������-����������
                    recipient.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                    // ��������� �� ������ �������� ���������� �����������
                    recipient.setRealized(rs.getInt("Realized"));
                    // ����������� ��� ������-���������� ��������
                    SimpleDepartmentDTO recDept = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, recipient.getRecipientDeptID(),"getSimpleDepartmentByID");
                    recipient.setRecipientDeptCode(recDept.getDepartmentCode());
                    recipientsList.add(recipient);

                }
                while (rs.next());
                if(recipientsList != null){
                    Collections.sort(recipientsList, new departmentsSort("recipientDeptId"));
                }
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
        logger.debug("ENTERING recipientDeptDAO.findAllByMemo()");
        return recipientsList;
    }

    /**
     * ������ ����� ���������� ������ "�����-����������" [recipientDept] �� ��� ��������������
     * �� ������� recipientsDepts
     *
     * @param dH              - ��������� ������ �� ��� DAO-���������� ����������
     * @param recipientDeptID - ������������� �������������
     * @return - ������ "�����-����������" [recipientDeptDTO]
     */
    public recipientDeptDTO findByID(daoHandler dH, int recipientDeptID) {
        logger.debug("ENTERING into recipientDeptDAO.findByID().");
        Connection conn = null;
        ResultSet rs;
        recipientDeptDTO recipientDept = null;

        try {

            conn = this.getMemoConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM recipientsDepts WHERE ID = ?");

            stmt.setInt(1, recipientDeptID);
            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // ���� ���-�� ����� - ��������� ������, ���� ��� - ���������� null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // �������������� ������
                recipientDept = new recipientDeptDTO();
                // ������������� ������-���������� �� ������� recipientsDepts
                recipientDept.setId(rs.getInt("ID"));
                // ������������� ������-���������� �� �� "�����"
                recipientDept.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                // ����������� ��� ������-����������
                SimpleDepartmentDTO recDept = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, recipientDept.getRecipientDeptID(),"getSimpleDepartmentByID");
                recipientDept.setRecipientDeptCode(recDept.getDepartmentCode());

                // ������������ ������
                //recipientDept.setRecipientDeptName(rs.getString("DepartmentName"));
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
        logger.debug("LEAVING recipientDeptDAO.findByID().");
        return recipientDept;
    }

    /**
     * ������ ����� ���������� ������ "�����-����������" [recipientDept] �� ������� recipientsDepts
     * �� �������������� ������ �� ������� departments_list �� "�����".
     *
     * @param dH     - ��������� ������ �� ��� DAO-���������� ����������
     * @param deptID - ������������� �������������
     * @return - ������ "�����-����������" [recipientDeptDTO]
     */
    public recipientDeptDTO findByDeptID(daoHandler dH, int deptID) {
        logger.debug("ENTERING into recipientDeptDAO.findByDeptID().");
        Connection conn = null;
        ResultSet rs;
        recipientDeptDTO recipientDept = null;

        try {

            conn = this.getMemoConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM recipientsDepts WHERE RecipientDeptID = ?");

            stmt.setInt(1, deptID);
            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // ���� ���-�� ����� - ��������� ������, ���� ��� - ���������� null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // �������������� ������
                recipientDept = new recipientDeptDTO();
                // ������������� ������-���������� �� ������� recipientsDepts
                recipientDept.setId(rs.getInt("ID"));
                // ������������� ������-���������� �� �� "�����"
                recipientDept.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                // ����������� ��� ������-����������
                SimpleDepartmentDTO recDept = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, recipientDept.getRecipientDeptID(),"getSimpleDepartmentByID");
                recipientDept.setRecipientDeptCode(recDept.getDepartmentCode());
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
        logger.debug("LEAVING recipientDeptDAO.findByDeptID().");
        return recipientDept;
    }

    /**
     * ������ ����� ���������� ������ "�����-����������" [recipientDept] �� ��������������
     * ������ �� ������� departments_list �� "�����" � �� �������������� ���������� �������.
     * ���������������, ��� ���� ����� ����� ���� ����������� ����� �������� ���� ��� - �������
     * ����� ���������� ���� ������.
     *
     * @param dH     - ��������� ������ �� ��� DAO-���������� ����������
     * @param deptID - ������������� �������������
     * @param memoID - ������������� ���������� �������
     * @return - ������ "�����-����������" [recipientDeptDTO]
     */
    public recipientDeptDTO findByMemoDeptID(daoHandler dH, int deptID, int memoID) {
        logger.debug("ENTERING into recipientDeptDAO.findByMemoDeptID().");
        Connection conn = null;
        ResultSet rs;
        recipientDeptDTO recipientDept = null;

        try {

            conn = this.getMemoConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM recipientsDepts WHERE RecipientDeptID = ? AND MemoID = ?");

            stmt.setInt(1, deptID);
            stmt.setInt(2, memoID);
            logger.debug("Statement created.");

            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            // ���� ���-�� ����� - ��������� ������, ���� ��� - ���������� null
            if (rs.next()) {
                logger.debug("ResultSet is NOT EMPTY! Processing.");
                // �������������� ������
                recipientDept = new recipientDeptDTO();
                // ������������� ������-���������� �� ������� recipientsDepts
                recipientDept.setId(rs.getInt("ID"));
                // ������������� ������-���������� �� �� "�����"
                recipientDept.setRecipientDeptID(rs.getInt("RecipientDeptID"));
                // ����������� ��� ������-����������
                SimpleDepartmentDTO recDept = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, recipientDept.getRecipientDeptID(),"getSimpleDepartmentByID");
                recipientDept.setRecipientDeptCode(recDept.getDepartmentCode());
                // ������������� ��������
                recipientDept.setMemoID(rs.getInt("MemoID"));
                // ���� "���������/�����������" (Realized)
                recipientDept.setRealized(rs.getInt("Realized"));
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
        logger.debug("LEAVING recipientDeptDAO.findByMemoDeptID().");
        return recipientDept;
    }

    /**
     * ������ ����� ���������� ������ �������� "�����-���������� ��������"[recipientDeptDTO], ��������� ��
     * ������ ��������������� ������� �� �� "�����", ����������� � �������� ���������. ���� �� ������
     * ������� �� ������� - ����� ������ �������� null. ����� ���������� ��� ������ ������ ������ �����
     * ������� ������ - findByDeptID.
     *
     * @param dH    - ��������� ������ �� ��� DAO-���������� ����������
     * @param depts - ��������� ������ ��������������� ������� �� �� "�����"
     * @return - ������ �������� "�����-���������� ��������"[recipientDeptDTO]
     */
    public List findByDeptIDList(daoHandler dH, String[] depts) {
        logger.debug("ENTERING into recipientDeptDAO.findByDeptIDList().");
        int deptID; // <- �������� ������ ��� �������� �������������
        List deptList = null;
        try {
            // ���� ������ ���� ��� null - ���������� ������
            if ((depts == null) || (depts.length <= 0)) throw new Exception("Depts list are EMPTY!");
            // ��������� ���������� ��� ������ ��������������� - � �����
            for (String dept : depts) {
                deptID = -1;
                // ����� ������� ������������� (���������)
                try {
                    deptID = Integer.parseInt(dept);
                }
                catch (Exception e) {
                    logger.error("DeptID [" + dept + "] is invalid!");
                }
                // ���� ������� ��������� ������������� �� ������ � ����� - ���� �����
                if (deptID > 0) {
                    logger.debug("DeptID [" + deptID + "]");
                    //������ �������������
                    SimpleDepartmentDTO  simpleDepartment= (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, deptID, "getSimpleDepartmentByID");
                    if(dept != null){
                        logger.debug("DeptID [" + dept + "] FIND");
                        recipientDeptDTO recipientDept = new recipientDeptDTO();
                        recipientDept.setRecipientDeptID(deptID);
                        recipientDept.setRecipientDeptCode(simpleDepartment.getDepartmentCode());

                        if (deptList == null) deptList = new ArrayList();
                        deptList.add(recipientDept);
                    }                                        
                }
            }
            if (deptList != null) {
                Collections.sort(deptList, new departmentsSort("recipientDeptId"));
            }
        }
        // �������� ��
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        logger.debug("LEAVING deptDAO.findByDeptIDList().");
        return deptList;
    }

    /**
     * ��� �������� ����� ��, ��� ��� ������ �� ��, ������ ����� ���������� ������ �������� "�����-���������� ��������",
     * ��� �� ����������� ������, ��������� �� ������ ��������������� ������� �� �� "�����", ����������� � ��������
     * ���������. ���� �� ������ ������� �� ������� - ����� ������ �������� null.
     *
     * @param dH    - ��������� ������ �� ��� DAO-���������� ����������
     * @param depts - ��������� ������ ��������������� ������� �� �� "�����"
     * @return -   ������ �������� "�����-���������� ��������"[recipientDeptDTO]
     */
    public List findDeptID(daoHandler dH, String[] depts) {
        logger.debug("ENTERING into recipientDeptDAO.findByDeptIDList().");
        int deptID; // <- �������� ������ ��� �������� �������������
        List deptList = null;
        try {
            // ���� ������ ���� ��� null - ���������� ������
            if ((depts == null) || (depts.length <= 0)) throw new Exception("Depts list are EMPTY!");

            
            // ��������� ���������� ��� ������ ��������������� - � �����
            for (String dept : depts) {
                deptID = -1;
                // ����� ������� ������������� (���������)
                try {
                    deptID = Integer.parseInt(dept);
                }
                catch (Exception e) {
                    logger.error("DeptID [" + dept + "] is invalid!");
                }
                // ���� ������� ��������� ������������� �� ������ � ����� - ���� �����
                if (deptID > 0) {
                    //������ ��������� ������ �� ��� ID
                    SimpleDepartmentDTO simpleDepartment = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, deptID,"getSimpleDepartmentByID");

                    // ������� ������ �����������
                    recipientDeptDTO recipientDept = new recipientDeptDTO();

                    // ID ������
                    recipientDept.setRecipientDeptID(simpleDepartment.getId());
                    // ����������� ��� ������
                    recipientDept.setRecipientDeptCode(simpleDepartment.getDepartmentCode());

                    // ��������� � ���������� ������, � ����������� �� ����
                    // ��� ������ ������ ����������
                    SimpleEmployeeDTO simpleEmployeeChief = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, simpleDepartment.getId(), "getChiefDepartment");

                    if(simpleEmployeeChief != null){
                        recipientDept.setRecipientChief((simpleEmployeeChief.isMale() ? "��������� " :"��������� ") + simpleEmployeeChief.getFullInitials());
                    }

                    // ���� �� ���-�� ����� - �������������� ������ (���� �� ��� �� ���������������)
                    // � ��������� � ���� ��������� �����
                    if (deptList == null) deptList = new ArrayList();
                    deptList.add(recipientDept);
                }
            }
            if (deptList != null) {
                Collections.sort(deptList, new departmentsSort("recipientDeptId"));
            }            
        }
        // �������� ��
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        logger.debug("LEAVING deptDAO.findByDeptIDList().");
        return deptList;
    }

    /**
     * ������ ����� ������� ������ � ������� "������-���������� ��������" [recipientsDepts]. � ��������� �������
     * ��� �������� ������ ������������ ������ recipientDeptDTO, ���������� � �������� ���������.
     *
     * @param dH        - ��������� ������ �� ��� DAO-���������� ����������
     * @param recipient - ������ "�����-���������� ��������" [recipientsDeptsDTO]
     * @throws Exception - ������
     */
    public void create(daoHandler dH, recipientDeptDTO recipient) throws Exception {
        logger.debug("ENTERING recipientDeptDAO.create()");
        Connection conn = null;

        try {

            conn = this.getMemoConnection();
            logger.debug("Connection established.");
            // �������������� ������ ��� ���������� ��������
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO recipientsDepts(MemoID, RecipientDeptID, Realized, UpdateUserID) VALUES (?,?,?,?)");

            stmt.setInt(1, recipient.getMemoID());
            stmt.setInt(2, recipient.getRecipientDeptID());
            stmt.setInt(3, recipient.getRealized());
            stmt.setInt(4, recipient.getUpdateUserID());
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
        logger.debug("LEAVING recipientDeptDAO.create()");
    }

    /**
     * ����� ���������(��������) ������ � ������� recipientsDepts �� ������� recipientDeptDTO, ����������� � ��������
     * ���������. ������������� ���������� ������ ����� ������� �� �������. ���� ������ � ����� ���������������
     * �� ���������� ��� ���������� ������ ���� ��� ����� null, �� ������� �������� �� ����������.
     *
     * @param dH        - ��������� ������ �� ��� DAO-���������� ����������
     * @param recipient - ������ "�����-���������� ��������" [recipientsDeptsDTO]
     * @throws Exception - ������
     */
    public void update(daoHandler dH, recipientDeptDTO recipient) throws Exception {
        logger.debug("ENTERING into recipientDeptDAO.update().");
        Connection conn = null;

        try {
            logger.debug("Checking data for change.");
            // ������� ������ ���������� ������
            if ((recipient == null) || (dH.getRecipientDeptDAO().findByID(dH, recipient.getId()) == null))
                throw new Exception("Recipient doesn't exists(nothing to update) !");
            logger.debug("Starting sql-query generation.");

            conn = this.getMemoConnection();
            logger.debug("Connection established.");
            // �������������� ������ ��� ���������� ��������
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE recipientsDepts SET Realized = ? WHERE id = ?");

            stmt.setInt(1, recipient.getRealized());
            stmt.setInt(2, recipient.getId());
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
        logger.debug("LEAVING recipientDeptDAO.update().");
    }

    /**
     * ������� ������ ������������� �������������, ��� �������, ��� ����������� � ��� ��������� �������, ���������
     * ��������� �������� � � ������ ������������ �������������.
     * ����� ������������� ������ ��������������� ������������� ���������� � �����, � ���� ��������� ��������� ���������������
     * ��������� �������.
     *
     * @param recipientDept        - ������ ��������������� ������������� ���������� � �����
     * @return -
     */
    public String[] checkDepartment(String[] recipientDept){
        logger.debug("ENTERING into recipientDeptDAO.checkDepartment().");

        HashMap<String,String>departmentsJoin = new HashMap<String,String>();
        //�������������: 350->351
        departmentsJoin.put("478","463");

        List<String> recipientDept2 = new ArrayList<String>();
        List<String> wordList = Arrays.asList(recipientDept);

        //������ ��� ��������������� � ������
        for (String aWordList : wordList) {
            recipientDept2.add(aWordList);
        }

        //������� ��� ��� ���
        Set setHM = departmentsJoin.entrySet();

        //��������� �� ������ ������ ������������� � ���� ��� �������� ��������� ������ � ����� ������
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