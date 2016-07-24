//package memorandum.dataModel.dao;
//
//import jdb.exceptions.DBConnectionException;
//import jdb.processing.data.DataChanger;
//import jpersonnel.dto.SimpleDepartmentDTO;
//import jpersonnel.dto.SimpleEmployeeDTO;
//import memorandum.daoHandler;
//import memorandum.dataModel.system.Connectors;
//import memorandum.dataModel.dto.deptsPersonnelMappingDTO;
//
//import java.sql.*;
//import java.util.List;
//import java.util.ArrayList;
//
///**
// * ����� ��������� ����������� � ��������� deptsEmailsMappingDTO (�������� �� ������� deptsEmailsMapping �� �� "��������").
// * ��������� ������ ������ DSCommonDAO, � ��� ����� getConnection(), ������� �������� �������� � ��������
// */
//
//public class deptsPersonnelMappingDAO extends Connectors {
//
//
//    public deptsPersonnelMappingDAO() {
//    }
//
//    /**
//     * ������ ����� ������� ������� �� �������������� ���������� �� �� "�����".
//     *
//     * @param dH          - ��������� ������ �� ��� DAO-���������� ����������
//     * @param personnelID int ������������� ����������, ������� ���������� �����.
//     * @return deptsPersonnelMappingDTO ��������� �� �������������� ������� ��� �������� null.
//     */
//    public deptsPersonnelMappingDTO findMappingByPersonnelID(daoHandler dH, int personnelID) {
//        logger.debug("ENTERING into deptsPersonnelMappingDAO.findMappingByPersonnelID(" + personnelID + ")");
//        Connection conn = null;
//        ResultSet rs;
//        deptsPersonnelMappingDTO mapping = null;
//
//        try {
//
//            conn = this.getMemoConnection();
//            PreparedStatement stmt = conn.prepareStatement(
//                    "SELECT * FROM deptsPersonnelMapping WHERE PersonnelID = ?");
//
//            stmt.setInt(1, personnelID);
//            logger.debug("Statement created.");
//
//            rs = stmt.executeQuery();
//            logger.debug("Query executed.");
//
//            if (rs.next()) {
//                mapping = new deptsPersonnelMappingDTO();
//                mapping.setId(rs.getInt("ID"));
//                // ������������� ������ �� �� "�����"
//                mapping.setDeptID(rs.getInt("deptID"));
//                // ������������� ���������� ������ �� �� "�����"
//                mapping.setPersonnelID(rs.getInt("personnelID"));
//                // ������� ���������� �������������
//                mapping.setIsChief(rs.getInt("isChief"));
//                // 3-� ������� ��� �������������
//                mapping.setDeptCode(dH.getConnectorSource().getSimpleDepartmentByID(dH, rs.getInt("deptID")).getDepartmentCode());
//            }
//        }
//        // ������� ��
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // ������������ ��������
//        finally {
//            try {
//                if (conn != null) conn.close();
//            }
//            catch (Exception e_res) {
//                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
//            }
//        }
//        logger.debug("LEAVING  deptsPersonnelsMappingDAO.findMappingByPersonnelID()");
//        return mapping;
//    }
//
//
//    /**
//     * ������ ����� ������� ������� �� �������������� ������������� �� �� "�����".
//     *
//     * @param dH     - ��������� ������ �� ��� DAO-���������� ����������
//     * @param deptID int ������������� �������������, �������� �������� ���������� �����.
//     * @return deptsPersonnelMappingDTO ��������� �� �������������� ������� ��� �������� null.
//     */
//    public List findMappingByDeptID(daoHandler dH, int deptID) {
//        logger.debug("ENTERING into deptsPersonnelMappingDAO.findMappingByDeptID(" + deptID + ")");
//        Connection conn = null;
//        ResultSet rs;
//        List<deptsPersonnelMappingDTO> memberList = null;
//
//        try {
//
//            conn = this.getMemoConnection();
//            PreparedStatement stmt = conn.prepareStatement(
//                    "SELECT * FROM deptsPersonnelMapping WHERE deptID = ?");
//
//            stmt.setInt(1, deptID);
//            logger.debug("Statement created.");
//
//            rs = stmt.executeQuery();
//            logger.debug("Query executed.");
//
//            while (rs.next()) {
//
//                deptsPersonnelMappingDTO mapping = new deptsPersonnelMappingDTO();
//                mapping.setId(rs.getInt("ID"));
//                // ������������� ������ �� �� "�����"
//                mapping.setDeptID(rs.getInt("deptID"));
//                // ������������� ���������� ������ �� �� "�����"
//                mapping.setPersonnelID(rs.getInt("personnelID"));
//                // ������� ���������� �������������
//                mapping.setIsChief(rs.getInt("isChief"));
//                if (memberList == null) {
//                    memberList = new ArrayList<deptsPersonnelMappingDTO>();
//                }
//                memberList.add(mapping);
//            }
//        }
//        // ������� ��
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // ������������ ��������
//        finally {
//            try {
//                if (conn != null) conn.close();
//            }
//            catch (Exception e_res) {
//                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
//            }
//        }
//        logger.debug("LEAVING  deptsPersonnelsMappingDAO.findMappingByDeptID()");
//        return memberList;
//    }
//
//    /**
//     * ������ ����� �������������� ��� ������� deptsPersonnelMapping � ���������.
//     *
//     * @param dH - ��������� ������ �� ��� DAO-���������� ����������
//     * @return deptsPersonnelMappingDTO ��������� �� �������������� ������� ��� �������� null.
//     */
//    public List findMappingAll(daoHandler dH) {
//        logger.debug("ENTERING into deptsPersonnelMappingDAO.findMappingByDeptID()");
//        Connection conn = null;
//        Statement stmt;
//        ResultSet rs;
//        List<deptsPersonnelMappingDTO> memberList = null;
//        String sql = "SELECT * FROM deptsPersonnelMapping ORDER BY id";
//
//        try {
//
//            conn = this.getMemoConnection();
//            stmt = conn.createStatement();
//            logger.debug("Statement created.");
//            // ���������������� ���������� �������
//            rs = stmt.executeQuery(sql);
//            logger.debug("Query executed.");
//            while (rs.next()) {
//                deptsPersonnelMappingDTO mapping = new deptsPersonnelMappingDTO();
//                mapping.setId(rs.getInt("ID"));
//                // ������������� ������ �� �� "�����"
//                mapping.setDeptID(rs.getInt("deptID"));
//                // ������������� ���������� ������ �� �� "�����"
//                mapping.setPersonnelID(rs.getInt("personnelID"));
//                // ������� ���������� �������������
//                mapping.setIsChief(rs.getInt("isChief"));
//
//                // ������ ��� �������������
//                SimpleDepartmentDTO simpleDepartment = dH.getConnectorSource().getSimpleDepartmentByID(dH, mapping.getDeptID());
//                if (simpleDepartment != null) {
//                    mapping.setDeptCode(simpleDepartment.getDepartmentCode());
//                }
//                // ������ ����
//                SimpleEmployeeDTO simpleEmployee = dH.getConnectorSource().getSimpleEmployeeByID(dH, mapping.getPersonnelID());
//                if (simpleEmployee != null) {
//                    mapping.setShortName(simpleEmployee.getShortName());
//                }
//
//                if (memberList == null) {
//                    memberList = new ArrayList<deptsPersonnelMappingDTO>();
//                }
//                memberList.add(mapping);
//            }
//        }
//        // ������� ��
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // ������������ ��������
//        finally {
//            try {
//                if (conn != null) conn.close();
//            }
//            catch (Exception e_res) {
//                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
//            }
//        }
//        logger.debug("LEAVING  deptsPersonnelsMappingDAO.findMappingByDeptID()");
//        return memberList;
//    }
//
//    /**
//     * ������ ����� ������� ������� deptsPersonnelMappingDTO � ������� ��������� �� ��� ��������������.
//     *
//     * @param dH - ��������� ������ �� ��� DAO-���������� ����������
//     * @param id int ������������� ��������, ������� ���������� �����.
//     * @return deptsPersonnelMappingDTO ��������� �� �������������� ������� ��� �������� null.
//     */
//    public deptsPersonnelMappingDTO findByID(daoHandler dH, int id) {
//        logger.debug("ENTERING into deptsPersonnelMappingDAO.findMappingByPersonnelID(" + id + ")");
//        Connection conn = null;
//        ResultSet rs;
//        deptsPersonnelMappingDTO mapping = null;
//
//        try {
//
//            conn = this.getMemoConnection();
//            PreparedStatement stmt = conn.prepareStatement(
//                    "SELECT * FROM deptsPersonnelMapping WHERE ID = ?");
//
//            stmt.setInt(1, id);
//            logger.debug("Statement created.");
//
//            rs = stmt.executeQuery();
//            logger.debug("Query executed.");
//
//            if (rs.next()) {
//                mapping = new deptsPersonnelMappingDTO();
//                mapping.setId(rs.getInt("ID"));
//                // ������������� ������ �� �� "�����"
//                mapping.setDeptID(rs.getInt("deptID"));
//                // ������������� ���������� ������ �� �� "�����"
//                mapping.setPersonnelID(rs.getInt("personnelID"));
//                // ������� ���������� �������������
//                mapping.setIsChief(rs.getInt("isChief"));
//            }
//        }
//        // ������� ��
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // ������������ ��������
//        finally {
//            try {
//                if (conn != null) conn.close();
//            }
//            catch (Exception e_res) {
//                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
//            }
//        }
//        logger.debug("LEAVING  deptsPersonnelsMappingDAO.findMappingByPersonnelID()");
//        return mapping;
//    }
//
//    /**
//     * ������ ����� ������� ������ � ������� ��������� �� "��������", ���������� ������ ��� �������� ������
//     * �������� ���������� � �������� �������� ����� deptsEmailsMappingDTO. ���� ���������� �������� ���� -
//     * ������� �������� ��������� �� �����.
//     *
//     * @param dH                    - ��������� ������ �� ��� DAO-���������� ����������
//     * @param deptsPersonnelMapping deptsEmailsMappingDTO ������ ��� �������� ������ � ������� ��������.
//     */
//    public void create(daoHandler dH, deptsPersonnelMappingDTO deptsPersonnelMapping) {
//        logger.debug("ENTERING into deptsPersonnelMappingDAO.create().");
//        Connection conn = null;
//
//        try {
//            // �������� ���������� ��� ���������
//            if (deptsPersonnelMapping != null) {
//                conn = this.getMemoConnection();
//                PreparedStatement stmt = conn.prepareStatement(
//                        "INSERT INTO deptsPersonnelMapping (deptid, PersonnelID, isChief) VALUES(?,?,?)");
//
//                stmt.setInt(1, deptsPersonnelMapping.getDeptID());
//                stmt.setInt(2, deptsPersonnelMapping.getPersonnelID());
//                stmt.setInt(3, 0);
//
//                logger.debug("Statement created.");
//
//                stmt.executeUpdate();
//                logger.debug("Query executed.");
//
//            } else throw new Exception("Nothing to update (input parameter is null)!");
//        }
//        // ������� ��
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // ������������ ��������
//        finally {
//            try {
//                if (conn != null) conn.close();
//            }
//            catch (Exception e_res) {
//                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
//            }
//        }
//        logger.debug("LEAVING deptsPersonnelMapping.create().");
//    }
//
//    public void update(daoHandler dH, deptsPersonnelMappingDTO deptsPersonnelMapping) {
//        logger.debug("ENTERING into deptsPersonnelMappingDAO.update().");
//        Connection conn = null;
//
//        try {
//            if (deptsPersonnelMapping != null) {
//                conn = this.getMemoConnection();
//                PreparedStatement stmt = conn.prepareStatement(
//                        "UPDATE deptsPersonnelMapping SET isChief = ? WHERE id = ?");
//
//                stmt.setInt(1, deptsPersonnelMapping.getIsChief());
//                stmt.setInt(2, deptsPersonnelMapping.getId());
//                logger.debug("Statement created.");
//
//                stmt.executeUpdate();
//                logger.debug("Query executed.");
//
//            } else throw new Exception("Nothing to update (input parameter is null)!");
//        }
//        // ������� ��
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // ������������ ��������
//        finally {
//            try {
//                if (conn != null) conn.close();
//            }
//            catch (Exception e_res) {
//                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
//            }
//        }
//        logger.debug("LEAVING deptsPersonnelMapping.update().");
//    }
//
//    /**
//     * ��������� �������� ������ �� ������� deptsPersonnelMappingDAO �� ��������������
//     *
//     * @param dH - ��������� ������ �� ��� DAO-���������� ����������
//     * @param Id - ������������� ��������� ������
//     */
//    public void delete(daoHandler dH, int Id) {
//        logger.debug("ENTERING into deptsPersonnelMappingDAO.delete().");
//        Connection conn = null;
//
//        try {
//            conn = this.getMemoConnection();
//            DataChanger.deleteRecord(conn, "deptsPersonnelMapping", Id);
//        }
//        // ������� ��
//        catch (DBConnectionException e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        catch (SQLException e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//
//        // ������������ ��������
//        finally {
//            try {
//                if (conn != null) conn.close();
//            }
//            catch (Exception e_res) {
//                logger.error("Can't close connection! [" + e_res.getMessage() + "]");
//            }
//        }
//        logger.debug("LEAVING deptsPersonnelMappingDAO.delete().");
//    }
//
//}
