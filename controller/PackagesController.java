package controller;

import model.Course;
import model.Profile;
import model.Student;
import model.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import validation.ValidationXML;
import validation.ValidationXMLImpl;
import view.CurriculumViewer;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PackagesController {
    private static final String ROOT_NODE = "Students";
    private static final String STUDENT = "Student";
    private final CurriculumViewer form;
    private final DefaultTreeModel jTreeModel;
    private final JTree jTree;
    private ValidationXML validationXML;
    private Document doc;
    private String filePath;

    private List<Student> studentList;
    private List<Profile> programtList = new ArrayList<>();
    private List<Node> courseList = new ArrayList<>();
    private List<Node> taskList = new ArrayList<>();


    public PackagesController(CurriculumViewer form, DefaultTreeModel treeModel) {
        this.form = form;
        this.jTree = form.getPackages();
        jTreeModel = treeModel;
        studentList = new ArrayList<>();
    }

    public void loadDefaultXML(String filePath) throws ParserConfigurationException, SAXException, IOException {
        this.filePath = filePath;
        makeValidation();
        initDoc();
        initLists();

        makeTree();

    }


    private void initLists() {

        NodeList nodeList = doc.getElementsByTagName("Student");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Student student = new Student();
            for (int j = 0; j < nodeList.item(i).getChildNodes().getLength(); j++) {
                String text = nodeList.item(i).getChildNodes().item(j).getNodeName();
                String value = nodeList.item(i).getChildNodes().item(j).getTextContent();
                switch (text) {
                    case "fullName":
                        student.setFullName(value);
                        break;
                    case "city":
                        student.setCity(value);
                        break;
                    case "email":
                        student.setEmail(value);
                        break;
                    case "isContract":
                        student.setContract(Boolean.parseBoolean(value));
                        break;
                    case "startDay":
                        student.setStartDate(value);
                        break;
                    case "programProfile":
                        student.setProfile(makeProfile(value));
                        break;
                    default:
                        break;
                }
            }
            studentList.add(student);
        }

        System.out.println("");
       /* NodeList nodeList = doc.getElementsByTagName("Student");
        for (int i = 0; i < nodeList.getLength(); i++) {
            studentList.add(nodeList.item(i));
        }
        nodeList = doc.getElementsByTagName("Profile");
        for (int i = 0; i < nodeList.getLength(); i++) {
            programtList.add(nodeList.item(i));
        }
        nodeList = doc.getElementsByTagName("Course");
        for (int i = 0; i < nodeList.getLength(); i++) {
            courseList.add(nodeList.item(i));
        }
        nodeList = doc.getElementsByTagName("Task");
        for (int i = 0; i < nodeList.getLength(); i++) {
            taskList.add(nodeList.item(i));
        }
*/
    }



    private Profile makeProfile(String id) {
        Profile profile = new Profile();
        NodeList nodeList = doc.getElementsByTagName("Profile");
        for (int i = 0; i < nodeList.getLength(); i++) {
            for (int j = 0; j < nodeList.item(i).getChildNodes().getLength(); j++) {
                String text = nodeList.item(i).getChildNodes().item(j).getNodeName();
                String value = nodeList.item(i).getChildNodes().item(j).getTextContent();
                if (text.equals("programID") && value.equals(id)) {
                    profile = new Profile(Long.parseLong(value),
                            nodeList.item(i).getChildNodes().item(j + 2).getTextContent(),
                            nodeList.item(i).getChildNodes().item(j + 4).getTextContent(),
                            nodeList.item(i).getChildNodes().item(j + 6).getTextContent());
                    List<Course> courses = new ArrayList<>();
                    NodeList courseIDList = nodeList.item(i).getChildNodes().item(j + 8).getChildNodes();
                    for (int k = 0; k < courseIDList.getLength(); k++) {
                        String cousrseID = courseIDList.item(k).getNodeName();
                        String courseIDValue = courseIDList.item(k).getTextContent();
                        if (cousrseID.equals("courseID")) {
                            courses.add(makeCourse(courseIDValue));
                        }

                    }
                    profile.setCourseList(courses);
                }
            }
        }
        return profile;
    }

    private Course makeCourse(String courseIDValue) {
        Course course = new Course();
        NodeList nodeList = doc.getElementsByTagName("Course");
        for (int i = 0; i < nodeList.getLength(); i++) {
            for (int j = 0; j < nodeList.item(i).getChildNodes().getLength(); j++) {
                String text = nodeList.item(i).getChildNodes().item(j).getNodeName();
                String value = nodeList.item(i).getChildNodes().item(j).getTextContent();
                if (text.equals("courseID") && value.equals(courseIDValue)) {
                    course = new Course(Long.parseLong(value),
                            nodeList.item(i).getChildNodes().item(j + 2).getTextContent(),
                            nodeList.item(i).getChildNodes().item(j + 4).getTextContent());
                    List<Task> tasks = new ArrayList<>();
                    NodeList courseIDList = nodeList.item(i).getChildNodes().item(j + 6).getChildNodes();
                    for (int k = 0; k < courseIDList.getLength(); k++) {
                        String taskID = courseIDList.item(k).getNodeName();
                        String taskIDValue = courseIDList.item(k).getTextContent();
                        if (taskID.equals("taskID")) {
                            tasks.add(makeTask(taskIDValue));
                        }

                    }
                    course.setTaskList(tasks);
                }
            }
        }
        return course;
    }

    private Task makeTask(String taskIDValue) {
        return null;
    }

    private void makeValidation() throws IOException, SAXException {
        validationXML = new ValidationXMLImpl(filePath);
        validationXML.validate();
    }

    private void initDoc() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = builder = f.newDocumentBuilder();
        doc = builder.parse(new File(filePath));
    }

    private void makeTree() throws ParserConfigurationException, IOException, SAXException {

       /* DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        rootNode.setUserObject(ROOT_NODE);
        jTreeModel.setRoot(rootNode);

        Map<DefaultMutableTreeNode, String> map = new HashMap<>();

        for (Node node:studentList) {
            NodeList subNodeList = node.getChildNodes();
            DefaultMutableTreeNode childTreeNode = null;
            for (int j = 0; j < subNodeList.getLength(); j++) {
                Node subNode = subNodeList.item(j);
                if (subNode.getNodeName().equals("fullName")) {
                    childTreeNode = new DefaultMutableTreeNode();
                    childTreeNode.setUserObject(getText(subNode));
                    jTreeModel.insertNodeInto(childTreeNode, rootNode, 0);
                }
                if (subNode.getNodeName().equals("programProfile")) {
                    map.put(childTreeNode, getText(subNode));
                }
            }
        }

        for (Node node:programtList) {
            NodeList subNodeList = node.getChildNodes();
            DefaultMutableTreeNode childTreeNode = null;
            for (DefaultMutableTreeNode parent : map.keySet()) {
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node subNode = subNodeList.item(j);
                    if (subNode.getNodeName().equals("programID")) {
                        childTreeNode = new DefaultMutableTreeNode();
                        childTreeNode.setUserObject(getText(subNode));
                        if (map.get(parent).equals(getText(subNode))) {
                            jTreeModel.insertNodeInto(childTreeNode, parent, 0);

                        }
                    }
                }
            }
        }


        jTree.addTreeSelectionListener(e -> {
            Object obj = jTree.getLastSelectedPathComponent();
            if (obj != null) {
                DefaultMutableTreeNode sel = (DefaultMutableTreeNode) obj;
                System.out.println(sel.getLevel() + "/" + sel.toString());
                try {
                    showNodeInfo(sel.toString(), sel.getLevel());
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (SAXException e1) {
                    e1.printStackTrace();
                }
            }

        });*/


    }

    private void showNodeInfo(String nodeName, int level) throws ParserConfigurationException, IOException, SAXException {

        NodeList nodeList = doc.getElementsByTagName("Student");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NodeList subNodeList = node.getChildNodes();
            boolean flag = false;
            for (int j = 0; j < subNodeList.getLength(); j++) {
                Node subNode = subNodeList.item(j);
                if (subNode.getTextContent().equals(nodeName)) {
                    flag = true;
                }
            }
            if (flag) {
                form.getTextInfo().setText("");
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node subNode = subNodeList.item(j);
                    if (subNode.getNodeName() != "#text" && subNode.getNodeName() != "programProfile") {

                        form.getTextInfo().setText(form.getTextInfo().getText() + "\n" + subNode.getNodeName() + " : " + subNode.getTextContent());
                    }
                }
                break;


            }
        }
        nodeList = doc.getElementsByTagName("Profile");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NodeList subNodeList = node.getChildNodes();
            boolean flag = false;
            for (int j = 0; j < subNodeList.getLength(); j++) {
                Node subNode = subNodeList.item(j);
                if (subNode.getTextContent().equals(nodeName)) {
                    flag = true;
                }
            }
            if (flag) {
                form.getTextInfo().setText("");
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node subNode = subNodeList.item(j);
                    if (subNode.getNodeName() != "#text" && subNode.getNodeName() != "programID" && subNode.getNodeName() != "courseList") {
                        form.getTextInfo().setText(form.getTextInfo().getText() + "\n" + subNode.getNodeName() + " : " + subNode.getTextContent());
                    }

                }
                break;


            }
        }
    }


    public Node findSubNode(String name, Node node) {

        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        if (!node.hasChildNodes()) {
            return null;
        }
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node subnode = list.item(i);
            if (subnode.getNodeType() == Node.ELEMENT_NODE) {
                if (subnode.getNodeName().equals(name)) {
                    return subnode;
                }
            }
        }
        return null;
    }

    public String getText(Node node) {
        StringBuilder result = new StringBuilder();
        if (!node.hasChildNodes()) {
            return "";
        }
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node subnode = list.item(i);
            int type = subnode.getNodeType();
            if (type == Node.TEXT_NODE) {
                result.append(subnode.getNodeValue());
            } else if (type == Node.CDATA_SECTION_NODE) {
                result.append(subnode.getNodeValue());
            } else if (type == Node.ENTITY_REFERENCE_NODE) {             // Поиск текста по поддереву
                result.append(getText(subnode));
            }
        }
        return result.toString();
    }
}