import controller.Controller;
import view.CurriculumViewer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class Main {

    private CurriculumViewer form;
    private TreeModel jTreeModel;

    public Main() {
        SwingUtilities.invokeLater(() -> {
            jTreeModel= new DefaultTreeModel(new DefaultMutableTreeNode());
            form = new CurriculumViewer();
            form.setVisible(true);
            postGUIConstruction();

        });

    }

    private void postGUIConstruction() {
        new Controller(form,jTreeModel);
    }

    public static void main(String[] args) {
        //спец класс для запуска юай
        new Main();

    }
}