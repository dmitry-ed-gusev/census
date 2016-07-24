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
* Класс реализует манипуляции с объектами deptsEmailsMappingDTO (записями из таблицы deptsEmailsMapping из БД "Служебки").
*/

public class deptsEmailsMappingDAO extends Connectors{

    public deptsEmailsMappingDAO() {
    }

    /**
     * Данный метод возвращает список всех маппингов эл. адресов для отделов ГУР.
     *
     * @param dH - Держатель ссылок на все DAO-компоненты приложения
     * @return List список всех маппингов или значение null - если ничего не найдено.
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

            // Непосредственное выполнение запроса
            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            if (rs.next()) {
                mappingList = new ArrayList();
                // Если что-то нашли - формируем список, если нет - возвращаем null
                do {
                    deptsEmailsMappingDTO deptsEmailsMapping = new deptsEmailsMappingDTO();

                    //Идентификатор маппинга
                    deptsEmailsMapping.setId(rs.getInt("ID"));
                    // Идентификатор отдела из БД "Кадры"
                    deptsEmailsMapping.setDeptID(rs.getInt("deptID"));
                    // Поиск кода отдела по его идентификатору
                    simpleDepartment = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, deptsEmailsMapping.getDeptID(),"getSimpleDepartmentByID");
                    if (simpleDepartment != null) {
                        deptsEmailsMapping.setDeptname(simpleDepartment.getDepartmentCode());
                    }
                    deptsEmailsMapping.setEmailId(rs.getInt("emailsId"));
                    //Поиск email в БД LDAP по его идентификатору
                    deptsEmailsMapping.setEmail((String)dH.getConnectorSource().getSomething(dH, rs.getInt("emailsId"),"getEmailById"));

                    mappingList.add(deptsEmailsMapping);
                }
                while (rs.next());
            }
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
        logger.debug("LEAVING  deptsEmailsMappingDAO.findAll()");
        return mappingList;
    }

    /**
     * Данный метод находит все маппинги почты для отдела с идентификатором deptID.
     *
     * @param dH     - Держатель ссылок на все DAO-компоненты приложения
     * @param deptId int идетификатор отдела, для которого ищутся маппинги
     * @return List список найденных маппингов(объекты deptsEmailsMappingDTO) для отдела deptID
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
            //Идентификатор подразделения
            stmt.setInt(1, deptId);
            logger.debug("Statement created.");

            // Непосредственное выполнение запроса
            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            if (rs.next()) {
                mappingList = new ArrayList<deptsEmailsMappingDTO>();
                // Если что-то нашли - формируем список, если нет - возвращаем null
                do {
                    deptsEmailsMappingDTO deptsEmailsMapping = new deptsEmailsMappingDTO();
                    deptsEmailsMapping.setId(rs.getInt("ID"));
                    // Идентификатор отдела из БД "Кадры"
                    deptsEmailsMapping.setDeptID(rs.getInt("deptID"));
                    // Поиск кода отдела по его идентификатору
                    simpleDepartment = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, deptsEmailsMapping.getDeptID(), "getSimpleDepartmentByID");
                    if (simpleDepartment != null) {
                        deptsEmailsMapping.setDeptname(simpleDepartment.getDepartmentCode());
                    }
                    //Поиск email в БД LDAP по его идентификатору
                    deptsEmailsMapping.setEmail((String)dH.getConnectorSource().getSomething(dH, rs.getInt("emailsId"),"getEmailById"));

                    mappingList.add(deptsEmailsMapping);
                }
                while (rs.next());
            }
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
        logger.debug("LEAVING  deptsEmailsMappingDAO.findDeptId()");
        return mappingList;
    }

    /**
     * Данный метод находит маппинг deptsEmailsMappingDTO в таблице маппингов по его идентификатору.
     *
     * @param dH -
     * @param id int идентификатор маппинга, который необходимо найти.
     * @return deptsEmailsMappingDTO найденный по идентификатору маппинг или значение null.
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
            //Идентификатор маппинга
            stmt.setInt(1, id);
            logger.debug("Statement created.");

            // Непосредственное выполнение запроса
            rs = stmt.executeQuery();
            logger.debug("Query executed.");

            if (rs.next()) {
                mapping = new deptsEmailsMappingDTO();
                mapping.setId(rs.getInt("ID"));
                // Идентификатор отдела из БД "Кадры"
                mapping.setDeptID(rs.getInt("deptID"));
                // Поиск кода отдела по его идентификатору
                simpleDepartment = (SimpleDepartmentDTO)dH.getConnectorSource().getSomething(dH, mapping.getDeptID(), "getSimpleDepartmentByID");
                if (simpleDepartment != null) {
                    mapping.setDeptname(simpleDepartment.getDepartmentCode());
                }
                //Поиск email в БД LDAP по его идентификатору
                mapping.setEmail((String)dH.getConnectorSource().getSomething(dH, rs.getInt("emailsId"), "getEmailById"));
            }
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
        logger.debug("LEAVING  deptsEmailsMappingDAO.findMappingByID()");
        return mapping;
    }

    /**
     * Данный метод создает запись в таблице маппингов БД "Служебки", источником данных для создания записи
     * является переданный в качестве парметра класс deptsEmailsMappingDTO. Если переданный параметр пуст -
     * никаких действий выполнено не будет.
     *
     * @param deptsEmailsMapping deptsEmailsMappingDTO шаблон для создания записи в таблице маппинга.
     */
    public void create(deptsEmailsMappingDTO deptsEmailsMapping) {
        logger.debug("ENTERING into deptsEmailsMappingDAO.insert().");
        Connection conn = null;

        try {
            // Проверим переданные нам параметры
            if (deptsEmailsMapping != null) {

                conn = this.getMemoConnection();

                PreparedStatement stmt =
                        conn.prepareStatement("INSERT INTO deptsEmailsMapping (deptid, emailsId) VALUES(?,?)");

                //Идентификатор подразделения БД"Кадры"
                stmt.setInt(1, deptsEmailsMapping.getDeptID());
                //Идентификатор email из БД LDAP
                stmt.setInt(2, deptsEmailsMapping.getEmailId());
                logger.debug("Statement created.");

                // Непосредственное выполнение запроса
                stmt.executeUpdate();
                logger.debug("Query executed.");
            } else throw new Exception("Nothing to update (input parameter is null)!");
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
        logger.debug("LEAVING memberDAO.insert().");
    }

    /**
     * Удаление маппинга по идентификатору
     *
     * @param Id - идентификатор записи
     */
    public void delete(int Id) {

        logger.debug("ENTERING into deptsEmailsMappingDAO.delete().");
        Connection conn = null;

        try {
            conn = this.getMemoConnection();

            PreparedStatement stmt =
                    conn.prepareStatement("DELETE from deptsEmailsMapping WHERE id = ?");

            //Идентификатор подразделения БД"Кадры"
            stmt.setInt(1, Id);
            logger.debug("Statement created.");

            // Непосредственное выполнение запроса
            stmt.executeUpdate();
            logger.debug("Query executed.");
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
        logger.debug("LEAVING memberDAO.delete().");
    }

}
