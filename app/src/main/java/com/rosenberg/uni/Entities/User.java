package com.rosenberg.uni.Entities;

import java.util.HashMap;
import java.util.Map;

public class User implements Entity {
    private String id;
    private String name;
    private String mail;
    private String born;
    private boolean tenant; // Do i give my car to not

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id,String name, String mail, String dob,boolean tenant) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.born = dob;
        this.tenant = tenant;
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

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getId() {
        return id;
    }

    public boolean tenant() {
        return tenant;
    }


    public Map<String,Object> getMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("mail", mail);
        map.put("born", born);
        map.put("tenant", tenant);
        return map;
    }
}
