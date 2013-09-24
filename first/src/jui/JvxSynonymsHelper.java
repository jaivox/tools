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
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPopupMenu;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;



public class JvxSynonymsHelper {
    static String datadir = JvxConfiguration.datadir;
    JvxMainFrame theFrame = null;
    
    
    public JvxSynonymsHelper(JvxMainFrame frame) {
        super();
        theFrame = frame;
    }
    public JMenu createSynonymsMenu(Object ox) {
        JMenu submenu = new JMenu("Synonyms");
        
        if(ox instanceof SentenceX) {
            SentenceX sx = (SentenceX)ox;
            String words[] = sx.getWords();
            //String syns[] = JvxDialogLoader.gen.getSynonyms( words[words.length/2]);
            for(String word : words) {
                String syns[] = JvxDialogLoader.gen.getSynonyms( word );
                if(syns != null) {
                    JMenu syMenu = new JMenu(word);
                    for(String s : syns) {
                        JMenuItem menuItem = new JMenuItem(s);
                        syMenu.add(menuItem);
                    }
                    submenu.add(syMenu);
                }
            }
            
        }
        return submenu;
    }
    public JMenu createOkaysSynonymsMenu(Object ox) {
        JMenu submenu = new JMenu("Synonyms");
        
        if(ox instanceof SentenceX) {
            SentenceX sx = (SentenceX)ox;
            String words[] = sx.getWords();
            String[][] okwords = sx.getOkayWords();
            for(String word : words) {
                String[] syns = null;
                if(word == null) continue;
                for(String[] oks : okwords) {
                    if(oks == null) continue;
                    for(String ok : oks) {
                        if(ok == null) continue;
                        if(ok.equals(word) && oks.length > 1) {
                            syns = oks;
                            break;
                        }
                    }
                    if(syns != null) break;
                }
                if(syns != null) {
                    JMenu syMenu = new JMenu(word);
                    final PopUpMenuAction menuAction = new PopUpMenuAction(theFrame, word);
        
                    for(String s : syns) {
                        if(word.equals(s)) continue;
                        //JCheckBoxMenuItem menuItem = new StayOpenCheckBoxMenuItem(s, true);
                        JCheckBox menuItem = new JCheckBox(s);
                        menuItem.setSelected( !sx.isExcluded(s) );
                        menuItem.addActionListener(menuAction);
                        syMenu.add(menuItem);
                    }
                    submenu.add(syMenu);
                }
            }
            
        }
        return submenu;
    }     

    
    void populateSynonymsTab(Object ox) {
        JTable table = theFrame.getSynsTab();
        if(ox instanceof SentenceX) {
            SentenceX sx = (SentenceX)ox;
           //model.setRowCount(0);
            table.setDefaultRenderer(SynsData.class, new SynsDataRenderer());
            table.setDefaultEditor(SynsData.class, new SynsDataEditor());
            SynsDataModel model = (SynsDataModel)table.getModel();
            model.setSentence(sx.getSentenceKey(), sx);
            if(sx.getTabModvalues() != null) {
                model.setValues( sx.getTabModvalues() );
                model.setDataVector(null, sx.getWords());
            }
            else {
                model.setDataVector(sx.getWordOptions(), sx.getWords());
                sx.setTabModvalues(model.getValues());
            }
            
            for (int col = 0; col < table.getColumnCount(); col++) {
                TableColumn tc = table.getColumnModel().getColumn(col);
                //tc.setHeaderRenderer(new SynsDataRenderer()); 
            }
      
            //model.debug();
            table.setShowGrid(true);
            //theFrame.getSynsTab().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            for (int col = 0; col < table.getColumnCount(); col++) {
                int width = 0;
                for (int row = 0; row < table.getRowCount(); row++) {
                    TableCellRenderer renderer = table.getCellRenderer(row, col);
                    Component comp = table.prepareRenderer(renderer, row, col);
                    width = Math.max (comp.getPreferredSize().width, width);
                }
                TableColumn tc = table.getColumnModel().getColumn(col);
                if(width > 0) tc.setPreferredWidth(width);
            }
            model.fireTableDataChanged();
            model.addTableModelListener(new TableActionHandler(theFrame));  // skip the first fire
        }
        else {
            SynsDataModel model = (SynsDataModel)table.getModel();
            model.setDataVector(new String[][]{}, new String[]{});
        }
    }
    
    void synsTabMouseRClicked(MouseEvent evt) {
        final PopUpMenuAction menuAction = new PopUpMenuAction(theFrame);
        
        if( evt.isPopupTrigger() ) {
            int x = evt.getX();
            int y = evt.getY();
            JTable table = (JTable)evt.getSource();
            int row = table.rowAtPoint( evt.getPoint() );
            int column = table.columnAtPoint( evt.getPoint() );

            if (! table.isRowSelected(row)) {
                table.changeSelection(row, column, false, false);
            }
            JPopupMenu popup = new JPopupMenu();
            JMenuItem addMenuItem = new JMenuItem("Add Row");
            JMenuItem delMenuItem = new JMenuItem("Delete");
            JMenuItem editMenuItem = new JMenuItem("Edit Row");
            
            addMenuItem.addActionListener(menuAction);
            delMenuItem.addActionListener(menuAction);
            editMenuItem.addActionListener(menuAction);
            
            popup.add(addMenuItem);
            popup.add(delMenuItem);
            popup.add(editMenuItem);
            
            popup.show(table, x, y);
        }
    }

    void dropSynonym(String line, int row, int col) {
        SynsDataModel model = (SynsDataModel)theFrame.getSynsTab().getModel();
        if(! (model.getValueAt(row, col) instanceof SynsData)) {
            if(! model.isInColumn(line, col) ) {
                model.setValueAt(line, row, col);
            }
        }
    }
    
}
class TableActionHandler  implements TableModelListener {
    JvxMainFrame theFrame = null;
    String columnName = null;
    SynsDataModel model = null;
    SynsData data = null;
    
    TableActionHandler(JvxMainFrame frame) {
        theFrame = frame;
    }
    @Override
    public void tableChanged(TableModelEvent e) {
        if(e.getColumn() == TableModelEvent.ALL_COLUMNS) return;
        if(e.getFirstRow() == TableModelEvent.HEADER_ROW) return;
        int row = e.getFirstRow();
        int col = e.getColumn();
        int lastrow = e.getLastRow();
        if(col < 0) return;
        model = (SynsDataModel)e.getSource();
        if(model.getRowCount() <= 0) return;
        if((lastrow - row) >= model.getRowCount()) return;
        
        columnName = model.getColumnName(col);
        Object value = model.getValueAt(row, col);

        if(value instanceof SynsData) {
            data = (SynsData)value;
            if(data.getSelected() && data.getValue().length() > 0) {
                System.out.println("TableModelListener: ("+ columnName +": "+ row +", "+ col +") "+ model.getSentence() +" - "+ value.toString());
                SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        String key = model.getSentence();
                        theFrame.dlgLoader.gen.generateAlts(key, columnName, new String[] { data.getValue().trim() });
                        model.getSentenceX().setTheSentence( theFrame.dlgLoader.gen.getSentence(key) );
                        theFrame.getGrammarList().setListData(model.getSentenceX().getSentenceOptions());
                
                        //fireMouseclick();
                    }
                } );    
            }
            else if((!data.getSelected()) && data.getValue().length() > 0) {
                model.getSentenceX().addExclusion(data.getValue());
                System.out.println("TableModelListener: Excluded: ("+ columnName +": "+ row +", "+ col +") "+ model.getSentence() +" - "+ value.toString());
            }
        }
        else System.out.println("TableModelListener: (" + row +", "+ col +") "+ value.getClass() +" "+ value.toString());
    } 
    void fireMouseclick() {
        JTree tree = theFrame.getDialogTree();
        TreePath tpath = tree.getSelectionPath();
        if(tpath != null) {
            Rectangle rec = tree.getPathBounds(tpath);
            MouseEvent me = new MouseEvent(tree, 0, 0, 0, rec.y, rec.y, 1, false);
            for(MouseListener ml: tree.getMouseListeners()){
                ml.mousePressed(me);
            }
        }
    }
}
class PopUpMenuAction implements ActionListener {

    private JvxMainFrame theFrame = null;
    private String word = null;             // an easy way to identify the tab:col
    
    public PopUpMenuAction(JvxMainFrame frame) {
        theFrame = frame;
    }
    public PopUpMenuAction(JvxMainFrame frame, String w) {
        theFrame = frame;
        word = w;
    }
    
    public void actionPerformed(ActionEvent ae) {
        JTable table = theFrame.getSynsTab();
        SynsDataModel model = (SynsDataModel)table.getModel();
        
        if(ae.getSource() instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox)ae.getSource();
            String syn = cb.getText().trim();
            SentenceX sx = (SentenceX) theFrame.getSelectedNode().getUserObject();
            if(!cb.isSelected()) {
                sx.addExclusion(syn);
                int col = model.findColumn(word);
                int row = model.findRow(col, syn);
                model.updateValue(row, col, cb.isSelected());
                System.out.println("PopUpMenuAction: Excluded: " + sx.getSentenceKey() +" - "+ word +" "+ col +" "+ cb.getText() +" "+ cb.isSelected());
            }
            return;
        }
        JMenuItem mi = (JMenuItem)ae.getSource();
        String action = mi.getText();
        System.out.println("Menu: " + action);
        // TODO - may be a confirm action here
        if(action.equals("Add Row")) {
            model.insertRow(model.getRowCount());
        }
        else if(action.equals("Delete")) {
            model.deleteColumn(table.getSelectedRow(), table.getSelectedColumn());
        }
        else if(action.equals("Edit")) {
        }
    }
}

class SynsData implements Comparable<SynsData> {

    private Boolean selected;
    private String value;

    public SynsData(Boolean selected, String value) {
        this.selected = selected;
        this.value = value;
    }
    @Override
    public String toString() {
        return value.toString() +" - "+selected.toString();
    }
    void debug() {
        System.out.println("SynsData ("+ value +","+ selected.toString() +")");
    }
    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(SynsData v) {
        return this.value.compareTo(v.value);
    }

    @Override
    public boolean equals(Object v) {
        return v instanceof SynsData && this.selected == ((SynsData) v).selected
                && this.value.equals(((SynsData) v).value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
}


class SynsDataModel extends AbstractTableModel {

    String sentence = null;
    SentenceX theSentence = null;
    private ArrayList<String> names = null;
    private ArrayList<ArrayList<Object>> values;

    public SynsDataModel() {
        values = new ArrayList<ArrayList<Object>>();
        names = new ArrayList<String>();
    }
    public void setSentence(String s, SentenceX sx) {
        sentence = s;
        this.theSentence = sx;
    }
    public String getSentence() {
        return sentence;
    }
    public SentenceX getSentenceX() {
        return theSentence;
    }
    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<ArrayList<Object>> getValues() {
        return values;
    }

    public void setValues(ArrayList<ArrayList<Object>> values) {
        this.values = values;
    }

    @Override
    public int getRowCount() {
        return values.size();
    }

    @Override
    public int getColumnCount() {
        return names.size();
    }
    static Object defaultValue(int row, int col) {
        return "";
        //return new SynsData(false, "");
    }
    @Override
    public Object getValueAt(int row, int col) {
        if(row >= getRowCount()) return defaultValue(row, col);
        if(col >= getColumnCount()) return new SynsData(false, "");
        
        ArrayList rows = values == null ? null : values.get(row);
        
        return (rows == null || col >= rows.size()) ? defaultValue(row, col) : rows.get(col);
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        //System.out.println("setValueAt: (" + row +", "+ col +") "+ value.getClass() +" "+ value.toString());
        
        if(row >= getRowCount()) return;
        if(col >= getColumnCount()) return;
        if(value == null) return;
        SynsData prev = new SynsData(Boolean.FALSE, "");
        if(value instanceof String) {
            String v = (String)value;
            if(v.length() > 0) {
                values.get(row).set(col, new SynsData(Boolean.TRUE, v));
            }
        }
        if(value instanceof Boolean) {
            SynsData d = (SynsData)values.get(row).get(col);
            prev.setSelected(d.getSelected());
            prev.setValue(d.getValue());
            
            d.setSelected((Boolean) value);
        }
        if(! prev.equals(values.get(row).get(col))) {
            this.fireTableCellUpdated(row, col);
            //fireTableRowsUpdated(row, col);
            //this.fireTableDataChanged();
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return SynsData.class;
        //return getValueAt(0, col).getClass();
    }

    @Override
    public String getColumnName(int col) {
        return names.get(col);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    void setDataVector(Object[][] rows, String[] words) {
        names = new ArrayList<String>();
        for(String word : words) {
            names.add(word);
        }
        
        if(rows != null) {
            values = new ArrayList<ArrayList<Object>>();
        
            for(Object[] row : rows) {
                ArrayList<Object> rd = new ArrayList();
                for(Object cell : row) {
                    String v = (String)cell;
                    if(v == null || v.length() <= 0) {
                        rd.add(defaultValue(0, 0));
                    }
                    else {
                        if(!names.contains(v)) {
                            boolean f = theSentence.isExcluded(cell.toString()) ? false : true;
                            rd.add(new SynsData(f, (String) cell));
                        }
                        else rd.add(defaultValue(0, 0));
                    }
                }
                values.add(rd);
            }
            for(Iterator<ArrayList<Object>> it = values.iterator(); it.hasNext();) {
                boolean empty = true;
                ArrayList<Object> row = it.next();
                for(Object cell : row) {
                    if(cell != null && !cell.equals(defaultValue(0,0))) {
                        empty = false;
                        break;
                    }
                }
                if(empty) it.remove();
                empty = true;
            }
        }
        this.fireTableStructureChanged();
    }

    public void insertRow(int at) {
        ArrayList<Object> rd = new ArrayList();
        for(int i = 0; i < names.size(); i++) {
            rd.add("");
        }
        values.add(rd);
        this.fireTableRowsInserted(at, at);
        //this.fireTableChanged(null);
    }
    public void deleteColumn(int row, int col) {
        values.get(row).set(col, defaultValue(0,0));
        this.fireTableCellUpdated(row, col);
    }
    void debug() {
        StringBuffer sb = new StringBuffer();
        for(String name : names) {
                sb.append(name).append(", ");
        }
        System.out.println(sb.toString());
        
        for(ArrayList cells : values) {
            for(Object cell : cells) {
                if(cell instanceof SynsData) ((SynsData)cell).debug();
                else System.out.println("<String>");
            }
        }
    }

    boolean isInColumn(String line, int col) {
        return findRow(col, line) != -1;
    }

    int findRow(int col, String syn) {
        int i = 0;
        for(ArrayList cells : values) {
            if(cells.get(col) instanceof SynsData) {
                SynsData sd = (SynsData)cells.get(col);
                if(sd.getValue().equals(syn)) return i;
            }
            i++;
        }
        return -1;
    }

    void updateValue(int row, int col, boolean selected) {
        Object o = getValueAt(row, col);
        if(o instanceof SynsData) {
            //SynsData sd = new SynsData(selected, ((SynsData)o).getValue());
            this.setValueAt(selected, row, col);
        }
    }
}
class ComboRenderer implements TableCellRenderer {
    
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int col) {
        
        System.out.println("ComboRenderer: (" + row +", "+ col +") "+ value.getClass() +" "+ value.toString());
        JPanel jpCell = new JPanel();
        SynsData v = (SynsData) value;
        jpCell.add(new JTextField(v.getValue()));
        if(v.getValue().trim().length() > 0) {
            jpCell.add(new JCheckBox("", v.getSelected().booleanValue()));
        }
        return jpCell;
    }
}
class SynsDataRenderer extends JCheckBox implements TableCellRenderer {

    public SynsDataRenderer() {
        super();
        setOpaque(true);
    }
    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int col) {
        //System.out.println("SynsDataRenderer: (" + row +", "+ col +") "+ value.getClass() +" "+ value.toString());
        if(value instanceof String) {
            Component c = (Component)table.getDefaultRenderer(String.class)
                            .getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            c.setBackground(table.getBackground());  
            return c;
            //return new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        }
        if(value instanceof SynsData) {
            SynsData v = (SynsData) value;
            this.setSelected(v.getSelected());
            this.setText(v.getValue());
            SynsDataModel model = (SynsDataModel)table.getModel();
            SynsData d = (SynsData)model.getValueAt(row, col);
            if(d != null) this.setText(d.getValue());
        }

        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        return this;
    }
}

class SynsDataEditor extends AbstractCellEditor implements TableCellEditor, ItemListener {

    SynsDataRenderer rend = null;
    private TableCellEditor editor = null;


    public SynsDataEditor() {
        rend = new SynsDataRenderer();
        rend.addItemListener(this);
    }
    
    @Override
    public Object getCellEditorValue() {
        if (editor != null) {
            return editor.getCellEditorValue();
        }

        return rend.isSelected();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,
        Object value, boolean isSelected, int row, int col) {
        
        System.out.println("SynsDataEditor: (" + row +", "+ col +","+ isSelected +") "+ value.getClass() +" "+ value.toString());
        
        if(value instanceof String) {
            JTextField tf = new JTextField();
            tf.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    JTextField etf = (JTextField)evt.getSource();
                    if(etf.getText().length() > 0) SynsDataEditor.this.stopCellEditing();
                }
                });
            Rectangle r = table.getCellRect(row, col, true);
            tf.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            tf.setBounds(r);
            editor = new DefaultCellEditor(tf);
            //editor = table.getDefaultEditor(String.class);
            return editor.getTableCellEditorComponent(table, value, false, row, col);
        }
        editor = null;
        SynsData v = (SynsData) value;
        rend.setSelected(v.getSelected());
        rend.setText(v.getValue());
        
        return rend;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        this.fireEditingStopped();
    }
}
