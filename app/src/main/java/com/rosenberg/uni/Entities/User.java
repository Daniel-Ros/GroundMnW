package com.rosenberg.uni.Entities;

import com.google.firebase.firestore.DocumentId;

import java.util.HashMap;
import java.util.Map;

public class User{
    @DocumentId
    private String documentId; // same as car docID (look there)

    private String id;  // UNIQUE userID
    private String firstName, lastName;
    private String mail;
    private String born; // date of birth
    private boolean tenant; // true for tenant, false for renter (tenant - "seller")
    private boolean male; // true for male, false for female
    private String phoneNum;
    private String city;

    private String writingOnMe; // for the view profile, let every user write something about themselves

    /**
     * Default constructor required for calls to DataSnapshot.getValue(User.class)
     */
    public User() {}

    /**
     * constructor - this obj hold all the data of a user
     * @param id - UNIQUE userID
     * @param fName - first name
     * @param lName - last name
     * @param mail - email is UNIQUE
     * @param dob - date of birth
     * @param tenant - boolean, true=tenant, false=renter
     * @param gender - boolean, true=male, false=female
     * @param phoneNumber .
     * @param city .
     */
    public User(String id, String fName, String lName, String mail, String dob, boolean tenant,
                boolean gender, String phoneNumber, String city) {
        this.id = id;
        this.firstName = fName;
        this.lastName = lName;
        this.mail = mail;
        this.born = dob;
        this.tenant = tenant;
        this.male = gender;
        this.phoneNum = phoneNumber;
        this.city = city;
        this.writingOnMe = "hello MnW im new user ^^"; // its the default till the user
                                                                // edits his profile
    }

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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String docId) {
        this.documentId = docId;
    }
}
