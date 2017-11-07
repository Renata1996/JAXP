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
        StudentXmlCreationUtils studentXmlCreationUtils = null;
        try {
            studentXmlCreationUtils = new StudentXmlCreationUtils(filePath);
            studentXmlCreationUtils.makeStudentList();
            studentList = studentXmlCreationUtils.getStudentList();
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
                StringBuilder builder = new StringBuilder();
                builder.append(FULL_NAME).append(student.getFullName()).append(NEW_LINE).append(REGION).append(student.getCity())
                        .append(NEW_LINE).append(EMAIL).append(student.getEmail()).append(NEW_LINE).append(CONTRACT)
                        .append(student.isContract()).append(NEW_LINE).append(START_DAY).append(student.getStartDate());
                jTextArea.setText(builder.toString());
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
                StringBuilder builder = new StringBuilder();
                builder.append(TITLE).append(profile.getProgramName()).append(NEW_LINE)
                        .append(AUTHOR).append(profile.getAuthor()).append(NEW_LINE)
                        .append(LAST_MODIFICATION).append(profile.getCreationDate()).append(NEW_LINE).append(DURATION).append(duration)
                        .append(NEW_LINE).append(NEW_LINE).append(COURSES).append(NEW_LINE).append(coursesName);

                jTextArea.setText(builder.toString());
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
                    StringBuilder builder = new StringBuilder();
                    builder.append(TITLE).append(course.getCourseName()).append(NEW_LINE).append(AUTHOR)
                            .append(course.getCourseAuthor()).append(NEW_LINE).append(DURATION).append(duration)
                            .append(NEW_LINE).append(NEW_LINE).append(TASKS).append(NEW_LINE).append(tasksName);
                    jTextArea.setText(builder.toString());

                }

            }
            for (Course course : profile.getCourseList()) {
                for (Task task : course.getTaskList()) {
                    if (nodeName.equals(task.getTaskName())) {
                        StringBuilder text = new StringBuilder();
                        text.append(TITLE).append(task.getTaskName()).append(NEW_LINE).append(TIME).append(task.getTimeMaking());
                        if (task.getTheoryTask() != null && task.getTheoryTask().getKey()) {
                            text.append(NEW_LINE).append(THEORY_TASK).append(task.getTheoryTask().getValue());
                        }
                        if (task.getPracticalTask() != null && task.getPracticalTask().getKey()) {
                            text.append(NEW_LINE).append(PRACTICAL_TASK).append(task.getPracticalTask().getValue());
                        }
                        if (task.getMark() > 0) {
                            text.append(NEW_LINE).append(MARK).append(task.getMark());
                        }

                        jTextArea.setText(text.toString());
                        return;
                    }
                }


            }
        }
    }
}