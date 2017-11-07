package controller;

import view.CurriculumViewer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class Controller {

    private static final String XML_FILTER = "xml";
    private static final String OPEN_FILE = "Открыть файл";
    private static final int EXIT = 1;
    private static final int IMPORT_FILE = 2;

    private String filePath = "src/resources/StudentReport.xml";
    private final CurriculumViewer form;
    private final TreeModel jTreeModel;
    private JTree jTree;
    private PackagesController packagesController;
    private  JTextArea jTextArea;

    public Controller(CurriculumViewer form, TreeModel jTreeModel) {
        this.form = form;
        this.jTreeModel = jTreeModel;
        this.jTextArea = form.getTextInfo();
        this.jTree = form.getPackages();
        initJTreeModel();
        initComboBoxListener();
        initPackagesController();

    }


    private void initPackagesController() {
        packagesController = new PackagesController(jTree ,(DefaultTreeModel) jTreeModel, jTextArea);
        packagesController.loadDefaultXML(filePath);
    }

    private void initComboBoxListener() {
        form.getComboBox().addItemListener(e -> {
            switch (form.getComboBox().getSelectedIndex()) {
                case EXIT:
                    form.dispose();
                    break;
                case IMPORT_FILE:
                    makeImport();
                    makeTree();
                    break;
                default:
                    break;
            }
        });
    }

    private void makeTree() {
        initPackagesController();
    }

    private void makeImport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(XML_FILTER, XML_FILTER));
        int ret = chooser.showDialog(null, OPEN_FILE);
        if (ret == JFileChooser.APPROVE_OPTION) {
            filePath = chooser.getSelectedFile().getPath();
        }
    }

    private void initJTreeModel() {
        form.getPackages().setModel(jTreeModel);
    }




}
