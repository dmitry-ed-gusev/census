package org.census.webapp.web.controllers;

import org.census.webapp.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Gusev Dmitry (Дмитрий)
 * @version 1.0 (DATE: 11.06.12)
*/

// todo: there is a problem (issue?) with LAZY fetch assotiated entity collection - we should use session/entity
// todo: manager while its closed. Two solutions - 1. mark controller as @Transactional (works) 2. add a filter
// todo: into web.xml file for url = /* with filter-class = org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter
// todo: (for JPA) or org.springframework.orm.hibernate4.support.OpenSessionInViewFilter (Hiber template).

// todo: http://stackoverflow.com/questions/439184/lazyinitializationexception-even-though-opensessioninviewinterceptor
// todo: http://stackoverflow.com/questions/3041259/hibernate-spring-failed-to-lazily-initialize-no-session-or-session-was-closed
// todo: http://stackoverflow.com/questions/7000166/org-hibernate-lazyinitializationexception-failed-to-lazily-initialize-a-collect

@Controller
//@Transactional
public class MainPageController {

 //@Autowired
 //private EmployeeWithOrdersDao employeeWithOrdersDao;

 //public EmployeeWithOrdersDao getEmployeeWithOrdersDao() {
 // return employeeWithOrdersDao;
 //}

 //public void setEmployeeWithOrdersDao(EmployeeWithOrdersDao employeeWithOrdersDao) {
 // this.employeeWithOrdersDao = employeeWithOrdersDao;
 //}

 @Autowired
 private EmployeeService employeeService;

 @RequestMapping("/main")
 public String mainPage() {
  //System.out.println(employeeWithOrdersDao.getEmployee(1L).getOrdersList());
  //System.out.println(employeeService.getEmployee(1L).getOrdersList());
  return "/mainPage";
 }

}