package model;

import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import validation.ValidationXML;
import validation.ValidationXMLImpl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Students {

    private static final String STUDENT_NAME_TAG = "Student";
    private static final String PROFILE_NAME_TAG = "Profile";
    private static final String TASK_NAME_TAG = "Task";
    private static final String FULL_NAME = "fullName";
    private static final String CITY = "city";
    private static final String EMAIL = "email";
    private static final String IS_CONTRACT = "isContract";
    private static final String START_DAY = "startDay";
    private static final String PROGRAM_PROFILE = "programProfile";
    private static final String PROGRAM_ID = "programID";
    private static final String TASK_ID = "taskID";
    private static final String MARK = "mark";
    private static final String TIME_MAKING = "timeMaking";
    private static final String NAME = "name";
    private static final String TASKS = "tasks";
    private static final String COURSE_ID = "courseID";
    private static final String COURSE_NAME = "courseName";
    private static final String COURSE_AUTHOR = "courseAuthor";
    private static final String TASKS_LIST = "tasksList";
    private static final String PROGRAM_NAME = "programName";
    private static final String AUTHOR = "author";
    private static final String COURSE_LIST = "courseList";
    private static final String CREATION_DAY = "creationDay";
    private static final String EMPTY_TAG="";
    private static final String COURSE_TAG_NAME = "Course";
    private static final String THEORY_TASK = "theoryTask";
    private static final String PRACTICAL_TASK = "practicalTask";

    private List<Student> studentList;
    private Document doc;
    private String filePath;
    private ValidationXML validationXML;

    public Students(String filePath) throws IOException, SAXException, ParserConfigurationException {
        this.filePath = filePath;
        initDoc();
        makeValidation();

    }


    public void makeStudentList() {
        studentList = new ArrayList<>();
        makeObject(new Student(), EMPTY_TAG, EMPTY_TAG, STUDENT_NAME_TAG);
    }

    private void makeValidation() throws IOException, SAXException {
        validationXML = new ValidationXMLImpl(filePath);
        validationXML.validate();
    }

    private void initDoc() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false);
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        doc = builder.parse(new File(filePath));
    }

    private <T> T makeStudent(NodeList childNodes) {
        Student student = new Student();
        for (int i = 0; i < childNodes.getLength(); i++) {
            String text = childNodes.item(i).getNodeName();
            String value = childNodes.item(i).getTextContent();
            switch (text) {
                case FULL_NAME:
                    student.setFullName(value);
                    break;
                case CITY:
                    student.setCity(value);
                    break;
                case EMAIL:
                    student.setEmail(value);
                    break;
                case IS_CONTRACT:
                    student.setContract(Boolean.parseBoolean(value));
                    break;
                case START_DAY:
                    student.setStartDate(value);
                    break;
                case PROGRAM_PROFILE:
                    student.setProfile(makeObject(new Profile(), value, PROGRAM_ID, PROFILE_NAME_TAG));
                    break;
                default:
                    break;
            }
        }

        return (T) student;
    }

    private <T> T makeObject(T object, String id, String nameID, String tagName) {
        T obj = object;
        NodeList nodeList = doc.getElementsByTagName(tagName);
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                for (int j = 0; j < nodeList.item(i).getChildNodes().getLength(); j++) {
                    String text = nodeList.item(i).getChildNodes().item(j).getNodeName();
                    String value = nodeList.item(i).getChildNodes().item(j).getTextContent();
                    if (obj instanceof Student) {
                        obj = makeStudent(nodeList.item(i).getChildNodes());
                    } else if (nameID.equals(text) && id.equals(value)) {
                        if (obj instanceof Profile) {
                            obj = makeProfile(nodeList.item(i).getChildNodes());
                        } else if (obj instanceof Course) {
                            obj = makeCourse(nodeList.item(i).getChildNodes());
                        } else if (obj instanceof Task) {
                            obj = makeTask(nodeList.item(i).getChildNodes());
                        }
                    }
                }
                if (obj instanceof Student)
                    studentList.add((Student) obj);
            }
        }

        return obj;
    }

    private <T> T makeTask(NodeList childNodes) {
        Task task = new Task();
        for (int i = 0; i < childNodes.getLength(); i++) {
            String text = childNodes.item(i).getNodeName();
            String valueParam = childNodes.item(i).getTextContent();
            switch (text) {
                case TASK_ID:
                    task.setTaskID(Integer.parseInt(valueParam));
                    break;
                case MARK:
                    task.setMark(Integer.parseInt(valueParam));
                    break;
                case TIME_MAKING:
                    task.setTimeMaking(Integer.parseInt(valueParam));
                    break;
                case NAME:
                    task.setTaskName(valueParam);
                    break;
                case TASKS:
                    NodeList nodeList = childNodes.item(i).getChildNodes();
                    for (int j = 0; j < nodeList.getLength(); j++) {
                        Node node = nodeList.item(j);
                        if (node.getNodeName().equals(THEORY_TASK)) {
                            task.setTheoryTask(new Pair(true, node.getChildNodes().item(1).getNodeName()));
                        }
                        if (node.getNodeName().equals(PRACTICAL_TASK)) {
                            task.setPracticalTask(new Pair(true, node.getChildNodes().item(1).getNodeName()));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return (T) task;
    }


    private <T> T makeCourse(NodeList childNodes) {
        Course course = new Course();
        for (int i = 0; i < childNodes.getLength(); i++) {
            String text = childNodes.item(i).getNodeName();
            String valueParam = childNodes.item(i).getTextContent();
            switch (text) {
                case COURSE_ID:
                    course.setCourseID(Integer.parseInt(valueParam));
                    break;
                case COURSE_NAME:
                    course.setCourseName(valueParam);
                    break;
                case COURSE_AUTHOR:
                    course.setCourseAuthor(valueParam);
                    break;
                case TASKS_LIST:
                    List<Task> tasks = new ArrayList<>();
                    NodeList courseIDList = childNodes.item(i).getChildNodes();
                    for (int j = 0; j < courseIDList.getLength(); j++) {
                        String taskID = courseIDList.item(j).getNodeName();
                        String taskIDValue = courseIDList.item(j).getTextContent();
                        if (taskID.equals(TASK_ID)) {
                            tasks.add(makeObject(new Task(), taskIDValue, TASK_ID, TASK_NAME_TAG));
                        }
                    }
                    course.setTaskList(tasks);
                    break;
                default:
                    break;
            }
        }

        return (T) course;
    }

    private <T> T makeProfile(NodeList childNodes) {

        Profile profile = new Profile();
        for (int i = 0; i < childNodes.getLength(); i++) {
            String text = childNodes.item(i).getNodeName();
            String valueParam = childNodes.item(i).getTextContent();
            switch (text) {
                case PROGRAM_ID:
                    profile.setProfileID(Integer.parseInt(valueParam));
                    break;
                case PROGRAM_NAME:
                    profile.setProgramName(valueParam);
                    break;
                case AUTHOR:
                    profile.setAuthor(valueParam);
                case CREATION_DAY:
                    profile.setCreationDate(valueParam);
                    break;
                case COURSE_LIST:
                    List<Course> courses = new ArrayList<>();
                    NodeList courseIDList = childNodes.item(i).getChildNodes();
                    for (int j = 0; j < courseIDList.getLength(); j++) {
                        String cousrseID = courseIDList.item(j).getNodeName();
                        String courseIDValue = courseIDList.item(j).getTextContent();
                        if (cousrseID.equals(COURSE_ID)) {
                            courses.add(makeObject(new Course(), courseIDValue, COURSE_ID, COURSE_TAG_NAME));
                        }

                    }
                    profile.setCourseList(courses);
                    break;
                default:
                    break;
            }
        }

        return (T) profile;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ValidationXML getValidationXML() {
        return validationXML;
    }

    public void setValidationXML(ValidationXML validationXML) {
        this.validationXML = validationXML;
    }
}
