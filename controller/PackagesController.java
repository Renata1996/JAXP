package controller;

import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import validation.ValidationXML;
import validation.ValidationXMLImpl;
import view.CurriculumViewer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackagesController {
    private static final String ROOT_NODE = "Students";
    private static final String STUDENT = "Student";
    private final CurriculumViewer form;
    private final DefaultTreeModel jTreeModel;
    private final JTree jTree;
    private ValidationXML validationXML;
    private Document doc;
    private String filePath;

    private List<Node> studentList;
    private List<Node> programtList;
    private List<Node> courseList;
    private List<Node> taskList;


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

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
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
                    jTreeModel.insertNodeInto(childTreeNode, rootNode, i);
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

        System.out.println();
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

        });


    }

    private void showNodeInfo(String nodeName, int level) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = builder = f.newDocumentBuilder();
        Document doc = builder.parse(new File("C:\\Users\\Renata_Karimova\\Desktop\\jaxp\\src\\resources\\StudenReport.xml"));
        // if (level == 1) {
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
        //  } else {
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
                    NodeList coursesNodeList = doc.getElementsByTagName("Courses");
                }
                break;


            }
        }
        //   }
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
