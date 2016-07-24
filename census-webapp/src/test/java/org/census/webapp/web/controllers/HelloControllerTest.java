package org.census.webapp.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;

/**
 * @author Gusev Dmitry (Дмитрий)
 * @version 1.0 (DATE: 10.05.12)
*/

public class HelloControllerTest {

 private final Log log = LogFactory.getLog(HelloControllerTest.class);

 @Test
 public void testHandleRequestView() throws Exception {
  HelloController controller = new HelloController();
  ModelAndView modelAndView = controller.helloWorld();
  log.info("HelloControllerTest -> " + modelAndView.getViewName());
  assertEquals("hello", modelAndView.getViewName());
 }

}