package model;

import javafx.util.Pair;

public class Task {

    private long taskID;
    private int mark;
    private String taskName;
    private int timeMaking;
    private Pair<Boolean,String> theoryTask;
    private Pair<Boolean,String> practicalTask;

    public Task(){}

    public Task(long taskID, String taskName, int timeMaking, Pair<Boolean, String> theoryTask, Pair<Boolean, String> practicalTask, int mark) {
        this.taskID = taskID;
        this.mark = mark;
        this.taskName = taskName;
        this.timeMaking = timeMaking;
        this.theoryTask = theoryTask;
        this.practicalTask = practicalTask;
    }

    public Task(long taskID, String taskName) {
        this.taskID = taskID;
        this.taskName = taskName;
    }

    public long getTaskID() {
        return taskID;
    }

    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTimeMaking() {
        return timeMaking;
    }

    public void setTimeMaking(int timeMaking) {
        this.timeMaking = timeMaking;
    }

    public Pair<Boolean, String> getTheoryTask() {
        return theoryTask;
    }

    public void setTheoryTask(Pair<Boolean, String> theoryTask) {
        this.theoryTask = theoryTask;
    }

    public Pair<Boolean, String> getPracticalTask() {
        return practicalTask;
    }

    public void setPracticalTask(Pair<Boolean, String> practicalTask) {
        this.practicalTask = practicalTask;
    }
}
