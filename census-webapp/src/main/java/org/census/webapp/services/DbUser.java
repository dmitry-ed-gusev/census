package org.census.webapp.services;

/**
 * @author Gusev Dmitry (dgusev)
 * @version 1.0 (DATE: 12.05.12)
 */
/**
 * User dto
 */
public class DbUser {

 /**
  * The username
  */
 private String username;

 /**
  * The password as an MD5 value
  */
 private String password;

 /**
  * Access level of the user.
  * 1 = Admin user
  * 2 = Regular user
  */
 private Integer access;

 public String getUsername() {
  return username;
 }
 public void setUsername(String username) {
  this.username = username;
 }
 public String getPassword() {
  return password;
 }
 public void setPassword(String password) {
  this.password = password;
 }
 public Integer getAccess() {
  return access;
 }
 public void setAccess(Integer access) {
  this.access = access;
 }


}