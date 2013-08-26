/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gen;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JTree;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author lin
 */
public class JvxDialogLoader {
	
	static String datadir = "data/";
	
    public JvxDialogLoader() {
        
    }
    public void loadDialogs(JTree dialogTree) {
        //QaList qs = new QaList("eattemplate.txt");
        String filename = datadir + "road.tree";
		File f = new File (filename);
		System.out.println (f.getAbsolutePath ());
		DefaultMutableTreeNode root = readConversation (datadir + "road.tree");
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

	public String [] loadGrammar () {
		ArrayList<String> lines = new ArrayList<String> ();
		try {
			BufferedReader in = new BufferedReader (new FileReader (datadir +"grammar.txt"));
			String line;
			while ((line = in.readLine ()) != null) {
				lines.add (line);
			}
			in.close ();
			int n = lines.size ();
			String array [] = lines.toArray (new String [n]);
			return array;
		}
		catch (Exception e) {
			e.printStackTrace ();
			String array [] = new String [1];
			array [0] = "Error loading grammar samples.";
			return array;
		}
	}
	
	public String [][] loadQualData () {
		ArrayList<String> lines = new ArrayList<String> ();
		try {
			BufferedReader in = new BufferedReader (new FileReader (datadir +"road.qdb"));
			String line;
			while ((line = in.readLine ()) != null) {
				if (line.trim ().length () == 0) continue;
				lines.add (line);
			}
			in.close ();
			int n = lines.size ();
			String line0 = lines.get (0);
			StringTokenizer st = new StringTokenizer (line0, ",");
			int cols = st.countTokens ();
			String table [][] = new String [n-1][cols+1];
			for (int i=1; i<n; i++) {
				int j = i-1;
				String row = lines.get (i);
				st = new StringTokenizer (row, ",");
				if (st.countTokens () != cols) continue;
				table [j][0] = ""+i;
				for (int k=0; k<cols; k++) {
					String word = st.nextToken ().trim ();
					table [j][k+1] = word;
				}
			}
			return table;
		}
		catch (Exception e) {
			e.printStackTrace ();
			String table [][] = new String [1][4];
			table [0][1] = "error";
			table [0][2] = "no value";
			table [0][3] = "no value";
			return table;
		}
	}

}
