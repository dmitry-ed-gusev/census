package org.census.webapp.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Gusev Dmitry (Дмитрий)
 * @version 1.0 (DATE: 10.05.12)
 */

@Controller
public class HelloController {

 private final Log log = LogFactory.getLog(HelloController.class);

 @RequestMapping("/hello")
 public ModelAndView helloWorld() {
  log.debug("HelloController.helloWorld() working.");
  String message = "Hello World, Spring 3.0!";
  return new ModelAndView("hello", "message", message);
 }

}