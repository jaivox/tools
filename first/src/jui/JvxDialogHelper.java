/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jui;

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
    private JTree dialogTree = null;
    DefaultMutableTreeNode rightClickedNode = null;
    
    
    public JvxDialogHelper() {
        super();
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
        if( evt.isPopupTrigger() ) {
            int x = evt.getX();
            int y = evt.getY();
            JTree tree = (JTree)evt.getSource();
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
        if(action.equals("Add")) {
            rightClickedNode.add(new DefaultMutableTreeNode("another_child"));
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