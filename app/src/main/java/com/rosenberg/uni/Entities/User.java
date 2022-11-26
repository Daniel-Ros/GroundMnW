package com.rosenberg.uni.Entities;

import java.util.HashMap;
import java.util.Map;

public class User implements Entity {
    private String id;
    private String name;
    private String mail;
    private String dob;
    private boolean isSoher; // Do i give my car to not

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id,String name, String mail, String dob,boolean isSoher) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.dob = dob;
        this.isSoher = isSoher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getId() {
        return id;
    }

    public boolean isSoher() {
        return isSoher;
    }


    public Map<String,Object> getMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("mail", mail);
        map.put("born", dob);
        map.put("isSoher", isSoher);
        return map;
    }
}
