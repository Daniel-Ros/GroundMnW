package com.rosenberg.uni.Entities;

import com.google.firebase.firestore.DocumentId;

import java.util.HashMap;
import java.util.Map;

public class User{
    @DocumentId
    private String documentId;

    private String id;

    //    private String name;
    private String firstName, lastName;

    private String mail;
    private String born; // date
    private boolean tenant; // Do i give my car to not - am i tenant?
    private boolean male; // true for male, false for female
    private String phoneNum;
    private String city;

    private String writingOnMe; // for the view profile, let every user write something about themselves

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getDocumentId() {
        return documentId;
    }


    public User(String id, String fName, String lName, String mail, String dob, boolean tenant,
                boolean gender, String phoneNumber, String city) {
        this.id = id;
//        this.name = name;
        this.firstName = fName;
        this.lastName = lName;
        this.mail = mail;
        this.born = dob;
        this.tenant = tenant;
        this.male = gender;
        this.phoneNum = phoneNumber;
        this.city = city;
        this.writingOnMe = ""; // for now
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public boolean getTenant() {
        return tenant;
    }

    public boolean getGender() {
        return male;
    }

    public void setGender(boolean gender) {
        this.male = gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWritingOnMe() {
        return writingOnMe;
    }

    public void setWritingOnMe(String writingOnMe) {
        this.writingOnMe = writingOnMe;
    }

//    public Map<String,Object> getMap(){
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        map.put("name", name);
//        map.put("mail", mail);
//        map.put("born", born);
//        map.put("tenant", tenant);
//        return map;
//    }
}
