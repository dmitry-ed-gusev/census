package memorandum.dataModel.dao;

import jpersonnel.dto.SimpleDepartmentDTO;
import memorandum.daoHandler;
import memorandum.dataModel.system.Connectors;
import memorandum.dataModel.dto.deptsEmailsMappingDTO;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;

/**
* ����� ��������� ����������� � ��������� deptsEmailsMappingDTO (�������� �� ������� deptsEmailsMapping �� �� "��������").
*/

public class deptsEmailsMappingDAO extends Connectors{

    public deptsEmailsMappingDAO() {
    }

    /**
     * ������ ����� ���������� ������ ���� ��������� ��. ������� ��� ������� ���.
     *
     * @param dH - ��������� ������ �� ��� DAO-���������� ����������
     * @return List ������ ���� ��������� ��� �������� null - ���� ������ �� �������.
     */
    public List findAll(daoHandler dH) {
        logger.debug("ENTERING into deptsEmailsMappingDAO.findAll()");
        Connection conn = null;
        ResultSet rs;
        List mappingList = null;
        SimpleDepartmentDTO simpleDepartment;

        try {
            conn = this.getMemoConnection();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM deptsEmailsMapping ORDER BY deptID");
            logger.debug("Statement created.");

            // ���������������� ���������� �������
            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            if (rs.next()) {
                mappingList = new ArrayList();
                // ���� ���-�� ����� - ��������� ������, ���� ��� - ���������� null
                do {
                    deptsEmailsMappingDTO deptsEmailsMapping = new deptsEmailsMappingDTO();

                    //������������� ��������
                    deptsEmailsMapping.setId(rs.getInt("ID"));
                    // ������������� ������ �� �� "�����"
                    deptsEmailsMapping.setDeptID(rs.getInt("deptID"));
                    // ����� ���� ������ �� ��� ��������������
                    simpleDepartment = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, deptsEmailsMapping.getDeptID(),"getSimpleDepartmentByID");
                    if (simpleDepartment != null) {
                        deptsEmailsMapping.setDeptname(simpleDepartment.getDepartmentCode());
                    }
                    deptsEmailsMapping.setEmailId(rs.getInt("emailsId"));
                    //����� email � �� LDAP �� ��� ��������������
                    deptsEmailsMapping.setEmail((String)dH.getConnectorSource().getSomething(dH, rs.getInt("emailsId"),"getEmailById"));

                    mappingList.add(deptsEmailsMapping);
                }
                while (rs.next());
            }
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
        logger.debug("LEAVING  deptsEmailsMappingDAO.findAll()");
        return mappingList;
    }

    /**
     * ������ ����� ������� ��� �������� ����� ��� ������ � ��������������� deptID.
     *
     * @param dH     - ��������� ������ �� ��� DAO-���������� ����������
     * @param deptId int ������������ ������, ��� �������� ������ ��������
     * @return List ������ ��������� ���������(������� deptsEmailsMappingDTO) ��� ������ deptID
     */
    public List<deptsEmailsMappingDTO> findDeptId(daoHandler dH, int deptId) {
        logger.debug("ENTERING into deptsEmailsMappingDAO.findDeptId()");
        Connection conn = null;
        ResultSet rs;
        List<deptsEmailsMappingDTO>mappingList = null;
        SimpleDepartmentDTO simpleDepartment;

        try {
            conn = this.getMemoConnection();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM deptsEmailsMapping WHERE deptid =?");
            //������������� �������������
            stmt.setInt(1, deptId);
            logger.debug("Statement created.");

            // ���������������� ���������� �������
            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            if (rs.next()) {
                mappingList = new ArrayList<deptsEmailsMappingDTO>();
                // ���� ���-�� ����� - ��������� ������, ���� ��� - ���������� null
                do {
                    deptsEmailsMappingDTO deptsEmailsMapping = new deptsEmailsMappingDTO();
                    deptsEmailsMapping.setId(rs.getInt("ID"));
                    // ������������� ������ �� �� "�����"
                    deptsEmailsMapping.setDeptID(rs.getInt("deptID"));
                    // ����� ���� ������ �� ��� ��������������
                    simpleDepartment = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, deptsEmailsMapping.getDeptID(), "getSimpleDepartmentByID");
                    if (simpleDepartment != null) {
                        deptsEmailsMapping.setDeptname(simpleDepartment.getDepartmentCode());
                    }
                    //����� email � �� LDAP �� ��� ��������������
                    deptsEmailsMapping.setEmail((String)dH.getConnectorSource().getSomething(dH, rs.getInt("emailsId"),"getEmailById"));

                    mappingList.add(deptsEmailsMapping);
                }
                while (rs.next());
            }
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
        logger.debug("LEAVING  deptsEmailsMappingDAO.findDeptId()");
        return mappingList;
    }

    /**
     * ������ ����� ������� ������� deptsEmailsMappingDTO � ������� ��������� �� ��� ��������������.
     *
     * @param dH -
     * @param id int ������������� ��������, ������� ���������� �����.
     * @return deptsEmailsMappingDTO ��������� �� �������������� ������� ��� �������� null.
     */
    public deptsEmailsMappingDTO findMappingByID(daoHandler dH, int id) {
        logger.debug("ENTERING into deptsEmailsMappingDAO.findMappingByID()");
        Connection conn = null;
        ResultSet rs;
        deptsEmailsMappingDTO mapping = null;
        SimpleDepartmentDTO simpleDepartment;

        try {
            conn = this.getMemoConnection();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM deptsEmailsMapping WHERE ID = ?");
            //������������� ��������
            stmt.setInt(1, id);
            logger.debug("Statement created.");

            // ���������������� ���������� �������
            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            if (rs.next()) {
                mapping = new deptsEmailsMappingDTO();
                mapping.setId(rs.getInt("ID"));
                // ������������� ������ �� �� "�����"
                mapping.setDeptID(rs.getInt("deptID"));
                // ����� ���� ������ �� ��� ��������������
                simpleDepartment = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, mapping.getDeptID(), "getSimpleDepartmentByID");
                if (simpleDepartment != null) {
                    mapping.setDeptname(simpleDepartment.getDepartmentCode());
                }
                //����� email � �� LDAP �� ��� ��������������
                mapping.setEmail((String)dH.getConnectorSource().getSomething(dH, rs.getInt("emailsId"), "getEmailById"));
            }
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
        logger.debug("LEAVING  deptsEmailsMappingDAO.findMappingByID()");
        return mapping;
    }

    /**
     * ������ ����� ������� ������ � ������� ��������� �� "��������", ���������� ������ ��� �������� ������
     * �������� ���������� � �������� �������� ����� deptsEmailsMappingDTO. ���� ���������� �������� ���� -
     * ������� �������� ��������� �� �����.
     *
     * @param deptsEmailsMapping deptsEmailsMappingDTO ������ ��� �������� ������ � ������� ��������.
     */
    public void create(deptsEmailsMappingDTO deptsEmailsMapping) {
        logger.debug("ENTERING into deptsEmailsMappingDAO.insert().");
        Connection conn = null;

        try {
            // �������� ���������� ��� ���������
            if (deptsEmailsMapping != null) {

                conn = this.getMemoConnection();

                PreparedStatement stmt =
                        conn.prepareStatement("INSERT INTO deptsEmailsMapping (deptid, emailsId) VALUES(?,?)");

                //������������� ������������� ��"�����"
                stmt.setInt(1, deptsEmailsMapping.getDeptID());
                //������������� email �� �� LDAP
                stmt.setInt(2, deptsEmailsMapping.getEmailId());
                logger.debug("Statement created.");

                // ���������������� ���������� �������
                stmt.executeUpdate();
                logger.debug("Query executed.");
            } else throw new Exception("Nothing to update (input parameter is null)!");
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
        logger.debug("LEAVING memberDAO.insert().");
    }

    /**
     * �������� �������� �� ��������������
     *
     * @param Id - ������������� ������
     */
    public void delete(int Id) {

        logger.debug("ENTERING into deptsEmailsMappingDAO.delete().");
        Connection conn = null;

        try {
            conn = this.getMemoConnection();

            PreparedStatement stmt =
                    conn.prepareStatement("DELETE from deptsEmailsMapping WHERE id = ?");

            //������������� ������������� ��"�����"
            stmt.setInt(1, Id);
            logger.debug("Statement created.");

            // ���������������� ���������� �������
            stmt.executeUpdate();
            logger.debug("Query executed.");
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
        logger.debug("LEAVING memberDAO.delete().");
    }

}
