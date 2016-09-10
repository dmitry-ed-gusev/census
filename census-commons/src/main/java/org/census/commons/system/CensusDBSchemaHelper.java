package org.census.commons.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dao.hibernate.admin.LogicGroupsSimpleDao;
import org.census.commons.dto.admin.LogicGroupDto;
import org.census.commons.dto.admin.LogicUserDto;
import org.census.commons.dto.docs.CategoryDto;
import org.census.commons.dto.docs.DocumentDto;
import org.census.commons.dto.docs.DocumentTypeDto;
import org.census.commons.dto.personnel.PositionDto;
import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dto.personnel.employees.EmployeeDto;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * Database schema helper. Functions: create database schema programmatically.
 * @author Gusev Dmitry (Dmitry)
 * @version 1.0 (DATE: 23.03.2014)
*/

//@Component
public class CensusDBSchemaHelper {

    private static Log log = LogFactory.getLog(CensusDBSchemaHelper.class);

    //@Autowired @Qualifier("censusSessionFactory")
    //private SessionFactory sessionFactory;

    @Value("${census.jdbc.driver}") private String driverClass;
    @Value("${census.db.url}")      private String url;
    @Value("${census.db.user}")     private String username;
    @Value("${census.db.pass}")     private String password;
    @Value("${hibernate.dialect}")  private String hibernateDialect;

    @Autowired private LogicGroupsSimpleDao groupsDao;

    /***/
    @PostConstruct
    public void initDB() {
        log.debug("CensusDBSchemaHelper.initDB() working.");
        try {
            this.groupsDao.findById(1L);
        } catch (Exception e) {
            log.warn("---> expected database schema does not exist, will create. Error is: " + e.getMessage());
            this.createSchema();
        }
        log.info("---> database schema exists, normal startup");
    }

    /***/
    private void createSchema() {
        log.debug("CensusDBSchemaHelper.createSchema() working [PRIVATE].");

        // Hibernate configuration object
        Configuration config = new Configuration();
        // put Hibernate properties
        config.setProperty("hibernate.connection.driver_class", driverClass);
        config.setProperty("hibernate.connection.url", url);
        config.setProperty("hibernate.connection.username", username);
        config.setProperty("hibernate.connection.password", password);
        config.setProperty("hibernate.dialect", hibernateDialect);

        // annotated classes from [admin] package
        config.addAnnotatedClass(LogicGroupDto.class);
        config.addAnnotatedClass(LogicUserDto.class);
        // annotated classes from [docs] package
        config.addAnnotatedClass(CategoryDto.class);
        config.addAnnotatedClass(DocumentDto.class);
        config.addAnnotatedClass(DocumentTypeDto.class);
        // annotated classes from [personnel] package
        config.addAnnotatedClass(DepartmentDto.class);
        config.addAnnotatedClass(EmployeeDto.class);
        config.addAnnotatedClass(PositionDto.class);
        log.debug("Annotated entity classes added to config.");

        // set all hibernate properties/datasource here
        //SchemaExport schema = new SchemaExport(config);
        //schema.create(true, true);
        //log.debug("DB schema created.");
    }

}