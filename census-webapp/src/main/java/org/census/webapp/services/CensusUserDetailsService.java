package org.census.webapp.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sql.DataSource;

/**
 * @author Gusev Dmitry (DGusev)
 * @version 1.0 (DATE: 15.07.12)
*/

public class CensusUserDetailsService implements UserDetailsService {

 private static Log log = LogFactory.getLog(CensusUserDetailsService.class);

 private DataSource dataSource;

 public void setDataSource(DataSource dataSource) {
  this.dataSource = dataSource;
 }

 @Override
 public CensusAuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

  log.info("CensusUserDetailsService.loadUserByUsername() working. User name = [" + username + "].");

  //String sql = "select * from user where email like :username";
  //MapSqlParameterSource source = new MapSqlParameterSource();
  //source.addValue("username", username);

  //SimpleJdbcTemplate sjt = new SimpleJdbcTemplate(getDataSource());
  //User user = sjt.queryForObject(sql, new UserMapper(), source);

  CensusAuthUserDetails user = new CensusAuthUserDetails();

  user.setAccountNonExpired(true);
  user.setAccountNonLocked(true);
  user.setCredentialsNonExpired(true);
  user.setEnabled(true);
  user.setId(1L);
  user.setMyCustomParameter("zzzz");
  user.setPassword("admin");
  user.setUsername("admin");
  user.setRoles(null);

  return user;
 }

}