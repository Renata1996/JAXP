package controller;

import org.xml.sax.SAXException;
import view.CurriculumViewer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Controller {

    private String filePath = "C:\\Users\\Renata_Karimova\\Desktop\\jaxp\\src\\resources\\StudenReport.xml";
    private final CurriculumViewer form;
    private final TreeModel jTreeModel;
    private PackagesController packagesController;

    public Controller(CurriculumViewer form, TreeModel jTreeModel) {
        this.form = form;
        this.jTreeModel = jTreeModel;
        initPackagesController();
        initJTreeModel();
        initComboBOxListener();

    }

    private void initPackagesController() {
        packagesController = new PackagesController(form, (DefaultTreeModel) jTreeModel);
        try {
            packagesController.loadDefaultXML(filePath);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initComboBOxListener() {
        form.getComboBox().addItemListener(e -> {
            switch (form.getComboBox().getSelectedIndex()) {
                case 1:
                    form.dispose();
                    break;
                case 2:
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
        chooser.setFileFilter(new FileNameExtensionFilter("xml", "xml"));
        int ret = chooser.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            filePath = chooser.getSelectedFile().getPath();
        }
    }

    private void initJTreeModel() {
        form.getPackages().setModel(jTreeModel);
    }


}
