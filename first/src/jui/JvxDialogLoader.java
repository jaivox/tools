/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import java.util.Map.Entry;

import com.jaivox.tools.*;
import com.jaivox.util.Log;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 *
 * @author lin
 */
public class JvxDialogLoader {
    public JvxDialogLoader() {
        
    }
    public void loadDialogs(JTree dialogTree) {
        //QaList qs = new QaList("eattemplate.txt");
        
        DefaultMutableTreeNode root = readConversation("eattemplate.txt");
        DefaultTreeModel model = (DefaultTreeModel)dialogTree.getModel();
        model.setRoot(root);
    }
    public DefaultMutableTreeNode readConversation(String filename) {
        BufferedReader in = null;
        int level = 0;
        DefaultMutableTreeNode node[] = new DefaultMutableTreeNode[10];
        node[0] = new DefaultMutableTreeNode("Dailog");
        
        try {
            in = new BufferedReader (new FileReader (filename));
            String line;

            while ((line = in.readLine ()) != null) {
                if(line.trim().length() <= 0) continue;
                level = 1;
                for(int i = 0; line.charAt(i) == '\t'; i++) level++;
                DefaultMutableTreeNode tn = new DefaultMutableTreeNode(line.trim());
                node[level] = tn;
                node[level-1].add(tn);
                
                System.out.println(line);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return node[0];
    }

    private void addNode(DefaultMutableTreeNode root, DefaultMutableTreeNode node, int level) {
        
    }
}
