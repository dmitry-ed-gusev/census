package org.census.webapp.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles and retrieves the login or denied page depending on the URI template.
 * COntroller declares two mappings -> /auth/login (shows the login page) and /auth/denied (shows the denied access page)
 * @author Gusev Dmitry (dgusev)
 * @version 1.0 (DATE: 12.05.12)
*/

@Controller
@RequestMapping("/auth")
public class AuthController {

 private static Log log = LogFactory.getLog(AuthController.class);

 /**
  * Handles and retrieves the login JSP page
  * @return the name of the JSP page
 */
 @RequestMapping(value = "/login", method = RequestMethod.GET)
 public String getLoginPage(@RequestParam(value="error", required=false) boolean error, ModelMap model) {
  log.debug("AuthController.getLoginPage() working. Received request to show login page.");

  // Add an error message to the model if login is unsuccessful
  // The 'error' parameter is set to true based on the when the authentication has failed.
  // We declared this under the authentication-failure-url attribute inside the spring-security.xml
  /* See below:
   <form-login
    login-page="/krams/auth/login"
    authentication-failure-url="/krams/auth/login?error=true"
    default-target-url="/krams/main/common"/>*/
  if (error) {
   log.info("error == true");
   // Assign an error message
   model.put("error", "Во время авторизации произошла ошибка. Возможно, Вы неверно ввели имя пользователя и/или пароль. Попробуйте еще раз.");
  } else {
   log.info("error == false");
   // Clean an error message
   model.put("error", "");
  }
  // This will resolve to /auth/loginpage.jsp
  return "/auth/loginpage";
 }

 /**
  * Handles and retrieves the denied JSP page. This is shown whenever a regular user tries to access an admin only page.
  * @return the name of the JSP page
 */
 /*
 @RequestMapping(value = "/denied", method = RequestMethod.GET)
  public String getDeniedPage() {
  log.debug("AuthController.getDeniedPage working. Received request to show denied page.");
  // This will resolve to /WEB-INF/jsp/deniedpage.jsp
  return "/deniedpage.jsp";
 }
 */

}