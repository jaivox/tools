/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jui;

import java.util.ArrayList;
import java.util.Collections;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.JTree;
import javax.swing.tree.TreePath;


/**
 *
 * @author lin
 */
public class JvxDialogHelper {
    JvxMainFrame theFrame = null;
    private JTree dialogTree = null;
    DefaultMutableTreeNode rightClickedNode = null;
    
    
    public JvxDialogHelper(JvxMainFrame frame) {
        super();
        theFrame = frame;
    }
    public JPopupMenu createPopup() {
        final DialogMenuAction menuAction = new DialogMenuAction(dialogTree, rightClickedNode);
        
        JPopupMenu popup = new JPopupMenu();
        JMenuItem addMenuItem = new JMenuItem("Add");
        JMenuItem delMenuItem = new JMenuItem("Delete");
        JMenuItem editMenuItem = new JMenuItem("Edit");
        
        addMenuItem.addActionListener(menuAction);
        delMenuItem.addActionListener(menuAction);
        editMenuItem.addActionListener(menuAction);
        
        popup.add(addMenuItem);
        popup.add(delMenuItem);
        popup.add(editMenuItem);
        
        return popup;
    }
    public void dialogTreeRClicked(java.awt.event.MouseEvent evt) {
        JTree tree = (JTree)evt.getSource();
            
        if( evt.isPopupTrigger() ) {
            int x = evt.getX();
            int y = evt.getY();
            dialogTree = tree;
            TreePath path = tree.getPathForLocation(x, y);
            if (path == null) return;
            
            rightClickedNode =
                            (DefaultMutableTreeNode)path.getLastPathComponent();

            TreePath[] selectionPaths = tree.getSelectionPaths();
            //check if node was selected
            boolean isSelected = false;
            if (selectionPaths != null) {
                for (TreePath selectionPath : selectionPaths) {
                    if (selectionPath.equals(path)) {
                        isSelected = true;
                    }
                }
            }
            //if clicked node was not selected, select it
            if(!isSelected){
                tree.setSelectionPath(path);
            }
            //if(rightClickedNode.isLeaf()){
                JPopupMenu popup = createPopup();
                popup.show(tree, x, y);
           // }
        }
        else {
        }
    }

    void dialogTreeMouseClicked(MouseEvent evt) {
        JTree tree = (JTree)evt.getSource();
        
        if(evt.getClickCount() == 1 || evt.getClickCount() == 2) {
            TreePath tpath = tree.getSelectionPath();
            if(tpath != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tpath.getLastPathComponent();
                System.out.println(node);
                theFrame.getGrammarList().setListData(Collections.list(node.breadthFirstEnumeration()).toArray());
            }
        }
         
    }


}
class DialogMenuAction implements ActionListener {

    private JTree dialogTree = null;
    DefaultMutableTreeNode rightClickedNode = null;
    
    DialogMenuAction(JTree tree, DefaultMutableTreeNode rNode) {
        dialogTree = tree;
        rightClickedNode = rNode;
    }
    
    public void actionPerformed(ActionEvent ae) {
        DefaultTreeModel model = (DefaultTreeModel)dialogTree.getModel();
        String action = ((JMenuItem)ae.getSource()).getText();
        System.out.println("Menu: " + action);
        // TODO - may be a confirm action here
        if(action.equals("Add")) {
            DefaultMutableTreeNode anotherNode = new DefaultMutableTreeNode(" ");
            rightClickedNode.add(anotherNode);
            model.reload(rightClickedNode);
            TreeNode[] nodes = ((DefaultTreeModel) dialogTree.getModel()).getPathToRoot(anotherNode);
            TreePath tpath = new TreePath(nodes);
            //dialogTree.scrollPathToVisible(tpath);
            dialogTree.expandPath(tpath);
            dialogTree.setSelectionPath(tpath);
            //dialogTree.startEditingAtPath(tpath);
        }
        else if(action.equals("Delete")) {
            rightClickedNode.removeAllChildren();
            model.removeNodeFromParent(rightClickedNode);
            rightClickedNode = null;
        }
        else if(action.equals("Edit")) {
            dialogTree.startEditingAtPath(dialogTree.getSelectionPath());
        }
        
        model.reload(rightClickedNode);
    }
    
}