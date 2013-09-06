/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gengram;

import static gengram.generate.P;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 *
 * @author lin
 */
public class GrammarGenerator {
    public static String DLG_DLIM = "()\r\n";
    static parse P;
    static wnlink W;

    static String tests [];

    public GrammarGenerator(String dlgFile, String datFile) {
        W = new wnlink ();
        W.createsyns ();
        W.addtablecolumn (datFile, ",\r\n", 3, 0);
        
        P = new parse (dlgFile);
        if (P.Valid) P.createsentences ();
    }
    public String[] getSynonyms(String word) {
        return W.getsynonyms (word);
    }
    public sentence getSentence(String key) {
        return P.sentences.get(key);
    }
    public ArrayList <String> getParsedStatements() {
        return P.statements;
    }
    public void generate (String filename) {
        TreeMap <String, sentence> sentences = P.sentences;
        Set <String> keys = sentences.keySet ();
        int n = keys.size ();
        tests = keys.toArray (new String [n]);
        for (int i=0; i<n; i++) {
            String key = tests [i];
            sentence s = sentences.get (key);
            // s.show (""+i+" ");
            // s.findmultiwords (W);
            s.multiwordsubs (P, W);
        }

        // generate using okays instead of subs
        for (int i=0; i<n; i++) {
            String key = tests [i];
            sentence s = sentences.get (key);
            System.out.println ("Sentence "+i+" Generating okays for: "+key);
            s.generateokays ();
        }
    }

    public ArrayList<String> parseDialog(String dlg) {
        ArrayList<String> sents = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer (dlg, DLG_DLIM);
        while (st.hasMoreTokens ()) {
            String token = st.nextToken ().trim ();
            if (token.length () == 0) continue;
            if (!token.endsWith ("?") && !token.endsWith (".")) {
                    token = token + ".";
            }
            sents.add(token);
        }
        return sents;
    }

}
