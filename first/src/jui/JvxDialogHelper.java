/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jui;

import gengram.SentenceX;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;



/**
 *
 * @author lin
 */
public class JvxDialogHelper {
    static String datadir = JvxConfiguration.datadir;
    JvxMainFrame theFrame = null;
    
    public JvxDialogHelper(JvxMainFrame frame) {
        super();
        theFrame = frame;
    }
    public JPopupMenu createPopup( DefaultMutableTreeNode rightClickedNode) {
        final DialogMenuAction menuAction = new DialogMenuAction(theFrame.getDialogTree(), rightClickedNode);
        
        JPopupMenu popup = new JPopupMenu();
        JMenuItem addMenuItem = new JMenuItem("Add");
        JMenuItem delMenuItem = new JMenuItem("Delete");
        JMenuItem editMenuItem = new JMenuItem("Edit");
        //JMenuItem synMenuItem = new JMenuItem("Synonyms");
        
        addMenuItem.addActionListener(menuAction);
        delMenuItem.addActionListener(menuAction);
        editMenuItem.addActionListener(menuAction);
        //synMenuItem.addActionListener(menuAction);
        
       
        
        popup.add(addMenuItem);
        popup.add(delMenuItem);
        popup.add(editMenuItem);
        //popup.add(createSynonymsMenu(rightClickedNode.getUserObject()));
        popup.add(theFrame.getSynsHelper().createOkaysSynonymsMenu(rightClickedNode.getUserObject()));
        
        return popup;
    }
    
    public void dialogTreeRClicked(java.awt.event.MouseEvent evt) {
        JTree tree = (JTree)evt.getSource();
            
        if( evt.isPopupTrigger() ) {
            int x = evt.getX();
            int y = evt.getY();
            
            TreePath path = tree.getPathForLocation(x, y);
            if (path == null) return;
            
            DefaultMutableTreeNode rightClickedNode =
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
                JPopupMenu popup = createPopup(rightClickedNode);
                popup.show(tree, x, y);
           // }
        }
        else {
        }
    }
    public void dumpTreeToFile(String fname) throws IOException {
        java.io.FileOutputStream out = new FileOutputStream(fname);
        //out.write(dumpTree(theFrame.getDialogTree()).getBytes());
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)theFrame.getDialogTree().getModel().getRoot();
        out.write(dumpTree(theFrame.getDialogTree().getModel(), root)
                .getBytes());
    }
    
    private String dumpTree(TreeModel model, DefaultMutableTreeNode node) {
        StringBuffer tdump = new StringBuffer();
        Object sx = node.getUserObject();
        if(sx instanceof SentenceX) {
            String s = ((SentenceX)sx).dump(node.getLevel() - 2); // skip the dialog.road
            tdump.append(s);
        }
        else {
            if(node.getLevel() > 2) tdump.append("\t").append(sx.toString()).append("\n");
        }
        for (int i = 0; i < model.getChildCount(node); i++) {
            String s = dumpTree(model, (DefaultMutableTreeNode)model.getChild(node, i));
            tdump.append( s );
        }
        return tdump.toString();
    }
    public String dumpTree(JTree tree) {
        StringBuffer tdump = new StringBuffer();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
        int level = 0;
        for(Enumeration e = root.depthFirstEnumeration(); e.hasMoreElements();) {
            DefaultMutableTreeNode nd = (DefaultMutableTreeNode)e.nextElement();
            
            Object sx = nd.getUserObject();
            if(sx instanceof SentenceX) {
                String s = ((SentenceX)sx).dump(nd.getLevel() - 2); // skip the dialog.road
                tdump.append(s);
                System.out.println(nd.getLevel() +": "+ s);
            }
        }
        return tdump.toString();
    }
    void debugTree(JTree tree) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
        for(Enumeration e = root.breadthFirstEnumeration(); e.hasMoreElements();) {
            DefaultMutableTreeNode nd = (DefaultMutableTreeNode)e.nextElement();
            System.out.println("---" + nd.getLevel() +"---");
            Object sx = nd.getUserObject();
            if(sx instanceof SentenceX) {
                ((SentenceX)sx).debug();
            }
            System.out.println("---" + nd.getLevel() +"---");
            
        }
    }
    void dialogTreeMouseClicked(MouseEvent evt) {
        JTree tree = (JTree)evt.getSource();
        //debugTree(tree);
        if(evt.getClickCount() == 1 || evt.getClickCount() == 2) {
            TreePath tpath = tree.getSelectionPath();
            if(tpath != null) {
                ArrayList<String> oks = new ArrayList<String>();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tpath.getLastPathComponent();
                System.out.println(node);
                for(Enumeration e = node.breadthFirstEnumeration(); e.hasMoreElements();) {
                    DefaultMutableTreeNode nd = (DefaultMutableTreeNode)e.nextElement();
                    ArrayList<String> al = null;
                    Object sx = nd.getUserObject();
                    if(sx instanceof SentenceX) {
                        al = new ArrayList<String> ();
                        ((SentenceX)sx).generateokays(al);
                    }
                    if(al != null) {
                        oks.addAll(al);
                    }
                    break; ///?
                }
                theFrame.getGrammarList().setListData(oks.toArray());
                theFrame.getSynsHelper().populateSynonymsTab(node.getUserObject());
            }
                
        }
    }
    //java 7
    void copyFile(String s, String t) throws IOException {
        File src = new File(s);
        File targ = new File(t);
        Files.copy(src.toPath(), targ.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }
    void generateApp(JvxMainFrame ui) {
        try {
            String appname = ui.getAppName();
            if(appname == null) appname = "test";
            String apploc = "./out/"+appname+"/";
            File f = new File(apploc);
            System.out.println("generateApp: "+ f.getAbsolutePath() +"---"+ f.getPath());
            f.mkdirs();
            dumpTreeToFile(apploc + appname + ".tree");
            
            copyFile("data/common_en.txt", apploc + "common_en.txt");
            copyFile("data/errors.dlg", apploc + "errors.dlg");
            applink.guiprep.generate(appname, apploc);

            if(ui.getCbConsole()) {
                StringBuffer code = new StringBuffer();
                copyFile("data/console.j", apploc + "console.java");
                String clz = buildAppCode(code, "console", appname);
                PrintWriter out = new PrintWriter (new FileWriter (apploc + clz + ".java"));
                out.println(code.toString());
                out.close ();
            }
            if(ui.getCbGoogleRecognizer()) {
                StringBuffer code = new StringBuffer();
                copyFile("data/runapp.j", apploc + "runapp.java");
                String clz = buildAppCode(code, "runapp", appname);
                PrintWriter out = new PrintWriter (new FileWriter (apploc + clz + ".java"));
                out.println(code.toString());
                out.close ();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    //temp code
    public String buildAppCode(StringBuffer code, String type, String appname) {
        String clz = Character.toUpperCase(appname.charAt(0)) + appname.substring(1) + 
                        Character.toUpperCase(type.charAt(0)) + type.substring(1);
            
        code.append("import com.jaivox.interpreter.Command;\nimport com.jaivox.interpreter.Interact;\n");
        code.append("import com.jaivox.synthesizer.web.Synthesizer;\nimport java.util.Properties;\n\n");
        code.append("public class ").append(clz);
        code.append(" {\n");
        code.append("\tpublic static void main(String[] args) {\n");
        code.append("\t\t").append(type).append(" c = new ").append(type);
        code.append("() {\n\t\t\t@Override\n\t\t\tvoid initializeInterpreter () {\n");
        code.append("\t\t\tProperties kv = new Properties ();\n\t\t\tkv.setProperty (\"common_words\", \"common_en.txt\");\n");
        code.append("\t\t\tkv.setProperty (\"questions_file\", \"").append(appname).append(".quest\");\n");
        code.append("\t\t\tkv.setProperty (\"grammar_file\", \"").append(appname).append(".dlg\");\n");
        code.append("\t\t\tkv.setProperty (\"ttslang\", \"en\");\n");
        code.append("\t\t\tCommand cmd = new Command ();\n");
        code.append("\t\t\tinter = new Interact (basedir, kv, cmd);\n");
        if(!type.equals("console")) code.append("\t\t\tspeaker = new Synthesizer (basedir, kv);\n");
        code.append("\t\t\t}\t\t\t\n};\n");
        code.append("\t}").append("\n}");
        return clz;    
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
        JMenuItem mi = (JMenuItem)ae.getSource();
        String action = mi.getText();
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
        if(action.equals("Synonyms")) {
            TreePath tpath = dialogTree.getSelectionPath();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tpath.getLastPathComponent();
            
            Object sx = node.getUserObject();
            if(sx instanceof SentenceX) {
   
                
            }
        }
        model.reload(rightClickedNode);
    }
}
class DragHandler extends TransferHandler {
    JvxMainFrame theFrame = null;
    public DragHandler(JvxMainFrame frame)
    {
        super();
        theFrame = frame;
    }
    public boolean canImport(TransferSupport support) {
         if (!support.isDrop()) {
             return false;
         }

         return support.isDataFlavorSupported(DataFlavor.stringFlavor);
     }

     public boolean importData(TransferSupport support) {
         if (!canImport(support)) {
           return false;
         }
         Component targ = support.getComponent();
         Transferable transferable = support.getTransferable();
         String line;
         try {
           line = (String) transferable.getTransferData(DataFlavor.stringFlavor);
         } catch (Exception e) {
           return false;
         }
         if(targ instanceof JTable) {
             JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
             theFrame.synsHelper.dropSynonym(line, dl.getRow(), dl.getColumn());
         }
         JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
         
         String[] data = line.split("\n");
         for (String item: data) {
             if (!item.isEmpty()) {
                //DefaultMutableTreeNode node = (DefaultMutableTreeNode)dl.getPath().getLastPathComponent();
                Point p = dl.getDropPoint();
                DefaultMutableTreeNode node = theFrame.getMouseOnNode((int)p.getX(), (int)p.getY());
                node.add(new DefaultMutableTreeNode(item));
                ((DefaultTreeModel)theFrame.getDialogTree().getModel()).reload(node);
             }
         }
         return true;
     }
}
