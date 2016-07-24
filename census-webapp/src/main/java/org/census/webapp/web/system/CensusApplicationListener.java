package org.census.webapp.web.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

/**
 * @author Gusev Dmitry (DGusev)
 * @version 1.0 (DATE: 15.07.12)
*/

public class CensusApplicationListener implements ApplicationListener {

 private static Log log = LogFactory.getLog(CensusApplicationListener.class);

 public void onApplicationEvent(ApplicationEvent appEvent) {
  if (appEvent instanceof AuthenticationSuccessEvent) {
   AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) appEvent;
   // auth object
   Authentication auth = event.getAuthentication();
   log.info("1 -> " + auth);
   Object obj = auth.getDetails();
   log.info("2 -> " + obj);
   //WebAuthenticationDetails wauth = (WebAuthenticationDetails) obj;
   //UserDetails userDetails = (UserDetails) auth.getPrincipal();
   //userDetails.
  }
 }

}