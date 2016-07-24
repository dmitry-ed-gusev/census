package org.census.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dao.hibernate.docs.DocumentsSimpleDao;
import org.census.commons.dao.hibernate.personnel.EmployeesSimpleDao;
import org.census.commons.dto.docs.DocumentDto;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Just test class - for development process.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 14.03.14)
*/

public class CommonsMain {

    /** Just for test. */
    public static void main(String[] args) {
        Log log = LogFactory.getLog(CommonsMain.class);
        log.info("CommonsMain starting...");

        // loading container
        final String SPRING_CONFIG_NAME = "CensusCommonContext.xml";
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(new String[] {SPRING_CONFIG_NAME}, false);
        context.refresh();
        log.debug("Spring context loaded.");

        // employees
        EmployeesSimpleDao employeeDao = (EmployeesSimpleDao) context.getBean("employeesSimpleDao");
        //EmployeeDto employee = employeeDao.findById(8L);
        //List<EmployeeDto> employees = employeeDao.getEmployeesByGroupName("memo_chief");
        /*
        List<EmployeeDto> employees = employeeDao.getEmployeesByPositionName("продаж");
        for (EmployeeDto employee : employees) {
            System.out.println("-> " + employee.getShortRusName());
            // positions
            System.out.print("--> [");
            for (PositionDto position : employee.getPositions()) {
                System.out.print(position.getName() + " ");
            }
            System.out.println("]");
            // logic users and groups
            for (LogicUserDto user : employee.getLogicUsers()) {
                System.out.print("--> " + user.getLogin() + " [");
                for (LogicGroupDto group : user.getLogicGroups()) {
                    System.out.print(group.getName() + " ");
                }
                System.out.println("]");
            }
        }
        */

        System.out.println("-> " + employeeDao.getEmployeeByLogin("admin"));

        // positions
        //PositionsSimpleDao positionsDao = (PositionsSimpleDao) context.getBean("positionsSimpleDao");
        //PositionDto position = positionsDao.findById(7L);
        //System.out.println("-> " + position);
        //Set<EmployeeDto> employees = position.getEmployees();
        //for (EmployeeDto employee : employees) {
        //    System.out.println("--> " + employee);
        //}
        //System.out.println(employees);

        // departments
        //DepartmentsSimpleDao departmentsDao = (DepartmentsSimpleDao) context.getBean("departmentsSimpleDao");
        //DepartmentDto department = departmentsDao.findById(3L);
        //System.out.println("-> " + department);

        // logic users
        //LogicUsersSimpleDao logicUsersDao = (LogicUsersSimpleDao) context.getBean("logicUsersSimpleDao");
        //LogicUserDto logicUser = logicUsersDao.findById(2L);
        //System.out.println("-> " + logicUser);

        // logic groups
        //LogicGroupsSimpleDao logicGroupsDao = (LogicGroupsSimpleDao) context.getBean("logicGroupsSimpleDao");
        //LogicGroupDto logicGroup = logicGroupsDao.findById(1L);
        //System.out.println("-> " + logicGroup);

        // contacts
        //ContactsSimpleDao contactsDao = (ContactsSimpleDao) context.getBean("contactsSimpleDao");
        //ContactDto contact = contactsDao.findById(8L);
        //System.out.println("-> " + contact);

        // documents
        DocumentsSimpleDao documentsDao = (DocumentsSimpleDao) context.getBean("documentsSimpleDao");
        List<DocumentDto> docsList = documentsDao.findAll();
        for (DocumentDto doc : docsList) {
            System.out.println("-> " + doc);
        }

    }

}