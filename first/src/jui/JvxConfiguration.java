/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.Properties;
import javax.swing.JTextField;

/**
 *
 * @author lin
 */
public class JvxConfiguration {
    
    public static final String genFolder = "data/";
    public static final String datadir = "data/";
    
    Properties conf = null; //new Properties();
    String appName = null;
    JvxConfiguration(String text) {
        conf = new Properties() {
            @Override
            public Object put(Object key, Object value) {
                Object o = null;
                String s = (String)value;
                if(s != null && s.trim().startsWith("{") /*&& s.trim().endsWith("}")*/) {
                    int len = s.indexOf('}', 1);
                    s = s.substring(1, len);
                    if(s.length() > 0) o = get(s);
                    if(o != null && ((String)o).trim().length() > 0) {
                        String v = (String)value;
                        v = v.replace("{"+s+"}", appName);
                        value = v;
                    }
                }
                return super.put(key, value);
            }
        };
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader(datadir + "template.conf"));
            conf.load(bf);
        } catch (Exception e) { e.printStackTrace(); }
        finally {
            try{ if(bf != null) bf.close(); } catch (Exception ex) { ex.printStackTrace(); }
        }
        appName = text;
    }

    void save(JvxMainFrame theFrame) {
        if(appName == null) return;
        setContentSpec(theFrame);
        setTargetSpec(theFrame);
        setMisc(theFrame);
        validatefields(theFrame);
        BufferedWriter bf = null;
        try {
            File f = new File(genFolder + appName + ".conf");
            f.mkdirs();
            if(conf.get("overwrite_files").equals("yes")) {
                f.delete();
            }
            f.createNewFile();
            
            bf = new BufferedWriter(new FileWriter(genFolder + appName + ".conf"));
            conf.store(bf, "Jvgen-" + new Date());
        }
        catch (Exception e) { e.printStackTrace(); }
        finally {
            try{ if(bf != null) bf.close(); } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    private void setContentSpec(JvxMainFrame theFrame) {
    }

    private void setMisc(JvxMainFrame theFrame) {
    }

    private void setTargetSpec(JvxMainFrame theFrame) {
        conf.put("project", appName);
        for(Object k : conf.keySet()) {
            conf.put(k, conf.get(k));       // easy way for now...
        }
    }

    private boolean validatefields(JvxMainFrame theFrame) {
        if(appName == null || appName.equals("")) return false;
        return true;
    }


    public Properties getConf() {
        return conf;
    }

    public void setConf(Properties conf) {
        this.conf = conf;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
    
}
