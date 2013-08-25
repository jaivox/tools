/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author lin
 */
public class JvxConfiguration {
    
    static final String genFolder = "./gen/";
    Properties conf = new Properties();
    String appName = null;
    JvxConfiguration(String text) {
        try {
        conf.load(new BufferedReader(new FileReader("template.conf")));
        } catch (Exception e) { e.printStackTrace(); }
        appName = text;
    }

    void save(JvxMainFrame theFrame) {
        if(appName == null) return;
        setContentSpec(theFrame);
        setTargetSpec(theFrame);
        setMisc(theFrame);
        validatefields(theFrame);
        try {
        conf.store(new BufferedWriter(new FileWriter(genFolder + appName + ".conf")), "Jvgen-" + new Date());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void setContentSpec(JvxMainFrame theFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setMisc(JvxMainFrame theFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setTargetSpec(JvxMainFrame theFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void validatefields(JvxMainFrame theFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
