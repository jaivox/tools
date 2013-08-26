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
import java.util.Vector;

/**
 *
 * @author lin
 */
public class JvxDialogLoader {
    public JvxDialogLoader() {
        
    }
    public void loadDialogs(JTree dialogTree) {
        
        DefaultMutableTreeNode root = readConversation("eattemplate.txt");
        readExpressions(root);
        DefaultTreeModel model = (DefaultTreeModel)dialogTree.getModel();
        model.setRoot(root);
    }
    void readExpressions(DefaultMutableTreeNode root) {
        QaList qs = new QaList("eattemplate.txt");
        Vector <String []> hold = new Vector <String []> ();
        Set <String> keys = qs.getLookup().keySet ();
        for (Iterator<String> it = keys.iterator (); it.hasNext (); ) {
            String key = it.next ();
            QaNode node = qs.getLookup().get (key);
            String tail [] = node.getTail();
            DefaultMutableTreeNode knode = new DefaultMutableTreeNode(key);
            for(String s : tail) knode.add(new DefaultMutableTreeNode(s));
            root.add(knode);
        }
    }
    public DefaultMutableTreeNode readConversation(String filename) {
        BufferedReader in = null;
        int level = 0;
        DefaultMutableTreeNode node[] = new DefaultMutableTreeNode[10];
        node[0] = new DefaultMutableTreeNode("Dailog");
        
        try {
            in = new BufferedReader (new FileReader (filename));
            String line;
            boolean skip = false;
            
            while ((line = in.readLine ()) != null) {
                String tabline = line;
                line = line.trim();
                if(line.length() <= 0) continue;
                if(line.startsWith("{")) skip = true;
                if(line.startsWith("}")) { skip = false; continue; }
                if(skip) continue;
                level = 1;
                for(int i = 0; tabline.charAt(i) == '\t'; i++) level++;
                DefaultMutableTreeNode tn = new DefaultMutableTreeNode(line);
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
