/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gengram;

import java.util.ArrayList;

/**
 *
 * @author lin
 */
public class SentenceX {
    private sentence theSentence = null;
    public SentenceX(sentence c)
    {
        theSentence = c;
    }
    public sentence getSentence() {
        return theSentence;
    }
    public String[] getWords() {
        return this.theSentence.words;
    }
    public String[][] getOkayWords() {
        return this.theSentence.okay;
    }
    public String toString() {
        return this.theSentence.orig;
    }
    public void generateokays (ArrayList <String> oks) {
        theSentence.generateokays();
        oks.addAll(theSentence.alts);
    }
}
