/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gengram;

import java.util.ArrayList;
import java.util.Arrays;
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
/*
        // generate using okays instead of subs
        for (int i=0; i<n; i++) {
            String key = tests [i];
            sentence s = sentences.get (key);
            System.out.println ("Sentence "+i+" Generating okays for: "+key);
            s.generateokays ();
        }
*/    }

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
    
    public boolean generateAlts(String key, String word, String[] addsyns) {
        int news = 0;
        TreeMap <String, sentence> sentences = P.sentences;
        sentence sent = sentences.get (key);
        if(sent == null) return false;

        String[] ar = W.syns.get(word);
        ArrayList<String> arl = new ArrayList<String>();
        if(ar != null) arl.addAll(Arrays.asList(ar));
        for(String k : addsyns) {
            if(!arl.contains(k)) {
                arl.add(k);
                news++;
            }
        }
        //arl.addAll(Arrays.asList(addsyns));
        ar = arl.toArray(new String[0]);
        W.syns.put(word, ar);

        ar = W.dbsyns.get(word);
        if(ar != null) {
            arl = new ArrayList<String>();
            if(ar != null) arl.addAll(Arrays.asList(ar));
            for(String k : addsyns) {
                if(!arl.contains(k)) {
                    arl.add(k);
                }
            }
            //arl.addAll(Arrays.asList(addsyns));
            ar = arl.toArray(new String[0]);
            W.dbsyns.put(word, ar);
        }
        if(news > 0) {
            sent = new sentence(sent.orig, sent.form, sent.tree);
            sent.multiwordsubs (P, W);
            sent.generateokays ();
        }
        sentences.put(key, sent);
        return news > 0;
    }
    public sentence generateAlts(String key) {
        TreeMap <String, sentence> sentences = P.sentences;
        sentence old = sentences.get (key);
        if(old == null) return null;
        sentence sent = new sentence(old.orig, old.form, old.tree);
        sent.setSelectionhandler(old.getSelectionhandler());
        sent.multiwordsubs (P, W);
        sent.generateokays ();
        sentences.put(key, sent);
        return sent;
    }
}
