package model;

import java.util.Date;

public class Student {

   private String fullName;
   private String city;
   private String email;
   private String startDate;
   private boolean isContract;
   private Profile profile;

   public Student(){

   }
    public Student(String fullName, String city, String email, String startDate, boolean isContract, Profile profile) {
        this.fullName = fullName;
        this.city = city;
        this.email = email;
        this.startDate = startDate;
        this.isContract = isContract;
        this.profile = profile;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public boolean isContract() {
        return isContract;
    }

    public void setContract(boolean contract) {
        isContract = contract;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
