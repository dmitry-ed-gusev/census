package org.census.webapp.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Gusev Dmitry (DGusev)
 * @version 1.0 (DATE: 15.07.12)
*/

public class CensusAuthUserDetails implements Serializable, UserDetails {

 private static final long serialVersionUID = 1L;

 private long    id;
 private String  password;
 private String  username;
 private Set     roles;
 private boolean accountNonExpired;
 private boolean accountNonLocked;
 private boolean credentialsNonExpired;
 private boolean enabled;

 private String  myCustomParameter;

 public CensusAuthUserDetails() {
 }

 public Set getRoles() {
  return roles;
 }

 public void setRoles(Set roles) {
  this.roles = roles;
 }

 public long getId() {
  return id;
 }

 public void setId(long id) {
  this.id = id;
 }

 public String getPassword() {
  return password;
 }

 public void setPassword(String password) {
  this.password = password;
 }

 public String getUsername() {
  return username;
 }

 public void setUsername(String username) {
  this.username = username;
 }

 public boolean isAccountNonExpired() {
  return accountNonExpired;
 }

 public void setAccountNonExpired(boolean accountNonExpired) {
  this.accountNonExpired = accountNonExpired;
 }

 public boolean isAccountNonLocked() {
  return accountNonLocked;
 }

 public void setAccountNonLocked(boolean accountNonLocked) {
  this.accountNonLocked = accountNonLocked;
 }

 public boolean isCredentialsNonExpired() {
  return credentialsNonExpired;
 }

 public void setCredentialsNonExpired(boolean credentialsNonExpired) {
  this.credentialsNonExpired = credentialsNonExpired;
 }

 public boolean isEnabled() {
  return enabled;
 }

 public void setEnabled(boolean enabled) {
  this.enabled = enabled;
 }

 public String getMyCustomParameter() {
  return myCustomParameter;
 }

 public void setMyCustomParameter(String myCustomParameter) {
  this.myCustomParameter = myCustomParameter;
 }

 /***/
 public List<GrantedAuthority> getAuthorities() {
  List<String> authorities = Arrays.asList("adminsGroup", "docsAdminsGroup", "docsUsersGroup",
   "personnelAdminsGroup", "personnelUsersGroup", "storageAdminsGroup", "storageUsersGroup");

  List<GrantedAuthority> grantedAuthoritiesList = new ArrayList<GrantedAuthority>();

  for (String authName : authorities) {
   grantedAuthoritiesList.add(new SimpleGrantedAuthority(authName));
  }

  return grantedAuthoritiesList;
 }

}