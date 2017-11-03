package controller;

import model.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PackagesController {
    private static final String ROOT_NODE = "Students";
    private static final int INDEX_IN_TREE = 0;
    private static final String FULL_NAME = "Full Name : ";
    private static final String NEW_LINE = "\n";
    private static final String REGION = "Region : ";
    private static final String EMAIL = "Email : ";
    private static final String START_DAY = "Start day : ";
    private static final String CONTRACT = "Contract : ";
    private static final String TITLE = "Title : ";
    private static final String AUTHOR = "Author : ";
    private static final String LAST_MODIFICATION = "Last modification : ";
    private static final String DURATION = "Duration : ";
    private static final String COURSES = "Courses : ";
    private static final String TASKS = "Tasks : ";
    private static final String MARK = "Mark : ";
    private static final String TIME = "Time : ";
    private static final String PRACTICAL_TASK = "Practical task : ";
    private static final String THEORY_TASK = "Theory task : ";

    private final DefaultTreeModel jTreeModel;
    private final JTree jTree;
    private final JTextArea jTextArea;

    private List<Student> studentList;

    public PackagesController(JTree jTree, DefaultTreeModel treeModel, JTextArea jTextArea) {
        this.jTree = jTree;
        this.jTextArea = jTextArea;
        this.jTreeModel = treeModel;
        studentList = new ArrayList<>();
    }

    public void loadDefaultXML(String filePath) {
        Students students = null;
        try {
            students = new Students(filePath);
            students.makeStudentList();
            studentList = students.getStudentList();
            makeTree();
            makeTreeListener();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            makeException(e);
        }
    }


    private void makeTree() throws ParserConfigurationException, IOException, SAXException {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        rootNode.setUserObject(ROOT_NODE);
        jTreeModel.setRoot(rootNode);
        if (studentList != null) {
            for (Student student : studentList) {
                DefaultMutableTreeNode fullNameTreeNode = new DefaultMutableTreeNode();
                fullNameTreeNode.setUserObject(student.getFullName());
                jTreeModel.insertNodeInto(fullNameTreeNode, rootNode, INDEX_IN_TREE);

                Profile profile = student.getProfile();
                DefaultMutableTreeNode profileTreeNode = new DefaultMutableTreeNode();
                profileTreeNode.setUserObject(profile.getProgramName());
                jTreeModel.insertNodeInto(profileTreeNode, fullNameTreeNode, INDEX_IN_TREE);

                for (Course course : profile.getCourseList()) {
                    DefaultMutableTreeNode courseTreeNode = new DefaultMutableTreeNode();
                    courseTreeNode.setUserObject(course.getCourseName());
                    jTreeModel.insertNodeInto(courseTreeNode, profileTreeNode, INDEX_IN_TREE);

                    for (Task task : course.getTaskList()) {
                        DefaultMutableTreeNode taskTreeNode = new DefaultMutableTreeNode();
                        taskTreeNode.setUserObject(task.getTaskName());
                        jTreeModel.insertNodeInto(taskTreeNode, courseTreeNode, INDEX_IN_TREE);
                    }
                }
            }
        }
    }

    private void makeTreeListener() {
        jTree.addTreeSelectionListener(e -> {
            Object obj = jTree.getLastSelectedPathComponent();
            if (obj != null) {
                DefaultMutableTreeNode sel = (DefaultMutableTreeNode) obj;
                try {
                    showNodeInfo(sel.toString(), sel.getLevel());
                } catch (ParserConfigurationException | IOException | SAXException ex) {
                    makeException(ex);
                }
            }

        });
    }

    private void makeException(Exception ex) {
        jTextArea.setBackground(Color.pink);
        jTextArea.setText(ex.getMessage());
    }


    private void showNodeInfo(String nodeName, int level) throws ParserConfigurationException, IOException, SAXException {

        for (Student student : studentList) {
            if (nodeName.equals(student.getFullName())) {
                jTextArea.setText(FULL_NAME + student.getFullName() + NEW_LINE + REGION + student.getCity() +
                        NEW_LINE + EMAIL + student.getEmail() + NEW_LINE + CONTRACT +
                        student.isContract() + NEW_LINE + START_DAY + student.getStartDate());
                return;
            }
            if (nodeName.equals(student.getProfile().getProgramName())) {
                int duration = 0;
                Profile profile = student.getProfile();
                StringBuilder coursesName = new StringBuilder();
                for (Course course : profile.getCourseList()) {
                    for (Task task : course.getTaskList()) {
                        duration += task.getTimeMaking();
                    }
                    coursesName.append(NEW_LINE).append(course.getCourseName());
                }
                jTextArea.setText(TITLE + profile.getProgramName() + NEW_LINE + AUTHOR + profile.getAuthor() + NEW_LINE +
                        LAST_MODIFICATION + profile.getCreationDate() + NEW_LINE + DURATION + duration + NEW_LINE + NEW_LINE + COURSES + NEW_LINE + coursesName);
                return;
            }

            Profile profile = student.getProfile();
            for (Course course : profile.getCourseList()) {
                if (nodeName.equals(course.getCourseName())) {
                    int duration = 0;
                    StringBuilder tasksName = new StringBuilder();
                    for (Task task : course.getTaskList()) {
                        duration += task.getTimeMaking();
                        tasksName.append(NEW_LINE).append(task.getTaskName());
                    }
                    jTextArea.setText(TITLE + course.getCourseName() + NEW_LINE + AUTHOR + course.getCourseAuthor() +
                            NEW_LINE + DURATION + duration + NEW_LINE + NEW_LINE + TASKS + NEW_LINE + tasksName);

                }

            }
            for (Course course : profile.getCourseList()) {
                for (Task task : course.getTaskList()) {
                    if (nodeName.equals(task.getTaskName())) {
                        String text = "";
                        text = TITLE + task.getTaskName() + NEW_LINE + TIME + task.getTimeMaking();

                        if (task.getTheoryTask() != null && task.getTheoryTask().getKey()) {
                            text += NEW_LINE + THEORY_TASK + task.getTheoryTask().getValue();
                        }
                        if (task.getPracticalTask() != null && task.getPracticalTask().getKey()) {
                            text += NEW_LINE + PRACTICAL_TASK + task.getPracticalTask().getValue();
                        }
                        if (task.getMark() > 0) {
                            text += NEW_LINE +MARK + task.getMark();
                        }

                        jTextArea.setText(text);
                        return;
                    }
                }


            }
        }
    }
}