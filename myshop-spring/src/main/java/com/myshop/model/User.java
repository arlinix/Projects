
package com.myshop.model;

public interface User {
    int getId(); void setId(int id);
    String getFirstName(); void setFirstName(String firstName);
    String getLastName();  void setLastName(String lastName);
    String getPassword();  void setPassword(String password);   // legacy setter (repo hashes)
    String getEmail();     void setEmail(String email);

    Role getRole();        void setRole(Role role);              // NEW
}
