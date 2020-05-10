/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.model;

/**
 *
 * @author shanil
 */
public class User {

    private String uid;
    private String userName;
    private String pasKey;
    private String role;
    private String status;

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    public User(String uid, String userName, String role) {
        this.uid = uid;
        this.userName = userName;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" + "uid=" + getUid() + ", userName=" + getUserName() + ", pasKey=" + getPasKey() + ", role=" + getRole() + '}';
    }

    public User() {
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    public User(String uid, String userName, String pasKey, String role,String status) {
        this.uid = uid;
        this.userName = userName;
        this.pasKey = pasKey;
        this.role = role;
        this.status = status;
    }

    public User(String uid, String userName, String role, String status) {
        this.uid = uid;
        this.userName = userName;
        this.role = role;
        this.status = status;
    }

    /**ddd
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the pasKey
     */
    public String getPasKey() {
        return pasKey;
    }

    /**
     * @param pasKey the pasKey to set
     */
    public void setPasKey(String pasKey) {
        this.pasKey = pasKey;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    public User(String role,String uid) {
        this.role = role;
        this.uid=uid;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
