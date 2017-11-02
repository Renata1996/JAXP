package model;

import java.util.List;

public class Course {

    private long courseID;
    private String courseName;
    private String courseAuthor;
    private List<Task> taskList;

    public Course() {
    }

    public Course(long courseID, String courseName, String courseAuthor) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseAuthor = courseAuthor;
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseAuthor() {
        return courseAuthor;
    }

    public void setCourseAuthor(String courseAuthor) {
        this.courseAuthor = courseAuthor;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
