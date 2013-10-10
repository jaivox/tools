package applink;


import java.io.*;

public class guiprep {
	
	static String outfile = "test.dlg";
	static String questions = "test.quest";
	static String errors = "data/errors.txt";

        public static void main (String args []) {
            generate(null, null);
        }
	public static void generate (String appname, String apploc) {
            if(appname != null && apploc != null) {
                outfile = apploc + appname +".dlg";
                questions = apploc + appname +".quest";
                Gui2Gram.dlgtree = apploc + appname +".tree";
            }
            Rule2Fsm rf = new Rule2Fsm ();
            Gui2Gram gg = new Gui2Gram ();
            try {
                    PrintWriter out = new PrintWriter (new FileWriter (outfile));
                    rf.writeRules (out);
                    gg.writeRules (out);
                    out.close ();
                    out = new PrintWriter (new FileWriter (questions));
                    BufferedReader in = new BufferedReader (new FileReader (errors));
                    String line;
                    while ((line = in.readLine ()) != null) {
                            String s = line.trim ().toLowerCase ();
                            if (s.length () == 0) continue;
                            out.println (s+"\t"+s+"\t(_,_,_,_,_,_,_)");
                    }
                    gg.writeQuestions (out);
                    out.close ();
            }
            catch (Exception e) {
                    e.printStackTrace ();
            }
	}
	
}
		
