package org.census.webapp.web.system;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Gusev Dmitry (dgusev)
 * @version 1.0 (DATE: 12.06.12)
 */
public class CensusEncodingFilter implements Filter {
 public void destroy() {
 }

 public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
  req.setCharacterEncoding("UTF-8");
  chain.doFilter(req, resp);
 }

 public void init(FilterConfig config) throws ServletException {

 }

}
