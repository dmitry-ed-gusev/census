package org.census;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dao.jpa.EmployeeWithOrdersDao;
import org.census.commons.dao.jpa.PositionJpaDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author Gusev Dmitry (dgusev)
 * @version 1.0 (DATE: 07.05.12)
*/

public class CensusTester {

 public static void main(String[] args) {
  Log log = LogFactory.getLog(CensusTester.class);
  log.info("CensusTester started...");
  // context loading
  ApplicationContext context = new FileSystemXmlApplicationContext("census-webapp/src/test/resources/censusContextTest.xml");
  log.info("Context loaded...");

  // getting DAO
  PositionJpaDao positionJpaDao = context.getBean(PositionJpaDao.class);

  //log.info("Dao received...");
  // searching
  //log.info(positionDao.findOne(1L));
  // adding data
  //PositionDto positionDto = new PositionDto("Еще должность. И еще.", 10, "Коммент", 1);
  //positionDao.save(positionDto);
  //log.info("Data saved.");


  // searching complex entity
  EmployeeWithOrdersDao employeeWithOrdersDao = context.getBean(EmployeeWithOrdersDao.class);
  log.info("Dao received. Searching.");
  //EmployeeWithOrdersDto employeeWithOrdersDto = employeeWithOrdersDao.getEmployee(1L);
  //log.info(employeeWithOrdersDto.getOrdersList());

  //EmployeeDao employeeDao = context.getBean(EmployeeDao.class);
  //log.info("Dao received. Searching.");
  //log.info("->" + employeeDao.getEM());
  //log.info(employeeDao.findOne(2L));
  //log.info(employeeDao.findAll());

  // we should manually close entity manager factory
  positionJpaDao.getEMF().close();
  log.info("EMF closed.");
 }

}