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
// * Класс реализует манипуляции с объектами deptsEmailsMappingDTO (записями из таблицы deptsEmailsMapping из БД "Служебки").
// * Наследует методы класса DSCommonDAO, в том числе getConnection(), который получает конекцию с сервером
// */
//
//public class deptsPersonnelMappingDAO extends Connectors {
//
//
//    public deptsPersonnelMappingDAO() {
//    }
//
//    /**
//     * Данный метод находит маппинг по идентификатору сотрудника из БД "Кадры".
//     *
//     * @param dH          - Держатель ссылок на все DAO-компоненты приложения
//     * @param personnelID int идентификатор сотрудника, который необходимо найти.
//     * @return deptsPersonnelMappingDTO найденный по идентификатору маппинг или значение null.
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
//                // Идентификатор отдела из БД "Кадры"
//                mapping.setDeptID(rs.getInt("deptID"));
//                // Идентификатор сотрудника отдела из БД "Кадры"
//                mapping.setPersonnelID(rs.getInt("personnelID"));
//                // Признак начальника подразделения
//                mapping.setIsChief(rs.getInt("isChief"));
//                // 3-х значный код подразделения
//                mapping.setDeptCode(dH.getConnectorSource().getSimpleDepartmentByID(dH, rs.getInt("deptID")).getDepartmentCode());
//            }
//        }
//        // Перехва ИС
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // Освобождение ресурсов
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
//     * Данный метод находит маппинг по идентификатору подразделения из БД "Кадры".
//     *
//     * @param dH     - Держатель ссылок на все DAO-компоненты приложения
//     * @param deptID int идентификатор подразделения, маппинги которого необходимо найти.
//     * @return deptsPersonnelMappingDTO найденный по идентификатору маппинг или значение null.
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
//                // Идентификатор отдела из БД "Кадры"
//                mapping.setDeptID(rs.getInt("deptID"));
//                // Идентификатор сотрудника отдела из БД "Кадры"
//                mapping.setPersonnelID(rs.getInt("personnelID"));
//                // Признак начальника подразделения
//                mapping.setIsChief(rs.getInt("isChief"));
//                if (memberList == null) {
//                    memberList = new ArrayList<deptsPersonnelMappingDTO>();
//                }
//                memberList.add(mapping);
//            }
//        }
//        // Перехва ИС
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // Освобождение ресурсов
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
//     * Данный метод подготавливает всю таблицу deptsPersonnelMapping к просмотру.
//     *
//     * @param dH - Держатель ссылок на все DAO-компоненты приложения
//     * @return deptsPersonnelMappingDTO найденный по идентификатору маппинг или значение null.
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
//            // Непосредственное выполнение запроса
//            rs = stmt.executeQuery(sql);
//            logger.debug("Query executed.");
//            while (rs.next()) {
//                deptsPersonnelMappingDTO mapping = new deptsPersonnelMappingDTO();
//                mapping.setId(rs.getInt("ID"));
//                // Идентификатор отдела из БД "Кадры"
//                mapping.setDeptID(rs.getInt("deptID"));
//                // Идентификатор сотрудника отдела из БД "Кадры"
//                mapping.setPersonnelID(rs.getInt("personnelID"));
//                // Признак начальника подразделения
//                mapping.setIsChief(rs.getInt("isChief"));
//
//                // Найдем код подразделения
//                SimpleDepartmentDTO simpleDepartment = dH.getConnectorSource().getSimpleDepartmentByID(dH, mapping.getDeptID());
//                if (simpleDepartment != null) {
//                    mapping.setDeptCode(simpleDepartment.getDepartmentCode());
//                }
//                // Найдем чела
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
//        // Перехва ИС
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // Освобождение ресурсов
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
//     * Данный метод находит маппинг deptsPersonnelMappingDTO в таблице маппингов по его идентификатору.
//     *
//     * @param dH - Держатель ссылок на все DAO-компоненты приложения
//     * @param id int идентификатор маппинга, который необходимо найти.
//     * @return deptsPersonnelMappingDTO найденный по идентификатору маппинг или значение null.
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
//                // Идентификатор отдела из БД "Кадры"
//                mapping.setDeptID(rs.getInt("deptID"));
//                // Идентификатор сотрудника отдела из БД "Кадры"
//                mapping.setPersonnelID(rs.getInt("personnelID"));
//                // Признак начальника подразделения
//                mapping.setIsChief(rs.getInt("isChief"));
//            }
//        }
//        // Перехва ИС
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // Освобождение ресурсов
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
//     * Данный метод создает запись в таблице маппингов БД "Служебки", источником данных для создания записи
//     * является переданный в качестве парметра класс deptsEmailsMappingDTO. Если переданный параметр пуст -
//     * никаких действий выполнено не будет.
//     *
//     * @param dH                    - Держатель ссылок на все DAO-компоненты приложения
//     * @param deptsPersonnelMapping deptsEmailsMappingDTO шаблон для создания записи в таблице маппинга.
//     */
//    public void create(daoHandler dH, deptsPersonnelMappingDTO deptsPersonnelMapping) {
//        logger.debug("ENTERING into deptsPersonnelMappingDAO.create().");
//        Connection conn = null;
//
//        try {
//            // Проверим переданные нам параметры
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
//        // Перехва ИС
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // Освобождение ресурсов
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
//        // Перехва ИС
//        catch (Exception e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        // Освобождение ресурсов
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
//     * Полностью удаление записи из таблице deptsPersonnelMappingDAO по идентификатору
//     *
//     * @param dH - Держатель ссылок на все DAO-компоненты приложения
//     * @param Id - идентификатор удаляемой записи
//     */
//    public void delete(daoHandler dH, int Id) {
//        logger.debug("ENTERING into deptsPersonnelMappingDAO.delete().");
//        Connection conn = null;
//
//        try {
//            conn = this.getMemoConnection();
//            DataChanger.deleteRecord(conn, "deptsPersonnelMapping", Id);
//        }
//        // Перехва ИС
//        catch (DBConnectionException e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//        catch (SQLException e) {
//            logger.error("Error occured: " + e.getMessage());
//        }
//
//        // Освобождение ресурсов
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
