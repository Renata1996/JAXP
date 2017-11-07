package model;

import java.util.List;

public class Profile {
    private long profileID;
    private String programName;
    private String author;
    private String creationDate;
    private List<Course> courseList;

    public Profile() {
    }

    public Profile(long profileID, String programName, String author, String creationDate) {
        this.profileID = profileID;
        this.programName = programName;
        this.author = author;
        this.creationDate = creationDate;
    }

    public long getProfileID() {
        return profileID;
    }

    public void setProfileID(long profileID) {
        this.profileID = profileID;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
}
