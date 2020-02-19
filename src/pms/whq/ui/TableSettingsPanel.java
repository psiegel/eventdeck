/*
 * TableSettingsPanel.java
 *
 * Created on September 29, 2005, 2:40 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import java.util.*;

import pms.whq.data.Table;

/**
 *
 * @author psiegel
 */
public class TableSettingsPanel extends JDialog implements ActionListener {
  
  protected JTable      mTable;
  protected JButton     mOK;
  protected JButton     mCancel;
  
  protected boolean     mChangesMade;
  
  protected SettingsTableData    mData;
  private static final String[]  kColumns = { "Table", "Active" };
  
  /** Creates a new instance of TableSettingsPanel */
  public TableSettingsPanel(JFrame parent, Map tables) {
    super(parent, "Activate Tables", true);
    
    mData = new SettingsTableData(tables);    
    mChangesMade = false;
    
    layoutControls();
    pack();
  }
  
  public boolean changesMade() {
    return mChangesMade;
  }
  
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    
    if (source == mCancel) {
      dispose();
    } else if (source == mOK) {
      mData.applyChanges();
      mChangesMade = true;
      dispose();
    }
  }
  
  protected void layoutControls() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = c.BOTH;
    c.gridwidth = c.REMAINDER;
    c.gridx = 0;
    c.gridy = 0;    
    
    mTable = new JTable(mData);
    TableColumn column = null;
    for (int i=0;i<kColumns.length;i++) {
      column = mTable.getColumnModel().getColumn(i);
      if (i == 0) {
        column.setPreferredWidth(800);
      } else {
        column.setPreferredWidth(100);
      }
    }
    
    c.weightx = 1;
    c.weighty = 1;
    c.insets.set(4, 4, 4, 4);
    add(new JScrollPane(mTable), c);
    
    c.insets.set(0, 0, 0, 0);
    
    JPanel buttonPanel = createButtonPanel();
    c.gridy++;
    c.weighty = 0;
    add(buttonPanel, c);
  }
  
  protected JPanel createButtonPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    
    mCancel = new JButton("Cancel");
    mCancel.addActionListener(this);
    panel.add(mCancel);
    
    mOK = new JButton("OK");
    mOK.addActionListener(this);
    panel.add(mOK);
    
    return panel;
  }
    
  class SettingsTableData extends AbstractTableModel {
    
    private Map         mTables;
    private Object[][]  mTableSettings;

    public SettingsTableData(Map tables) {
      mTables = tables;
      mTableSettings = new Object[mTables.size()][kColumns.length];
      
      int index = 0;
      Iterator i = tables.values().iterator();
      while (i.hasNext()) {
        Table table = (Table)i.next();

        mTableSettings[index][0] = table.getName();
        mTableSettings[index][1] = new Boolean(table.isActive());
        
        index++;
      }
    }

    public int getColumnCount() {
      return kColumns.length;
    }

    public int getRowCount() {
      return mTableSettings.length;
    }

    public String getColumnName(int col) {
      return kColumns[col];
    }

    public Object getValueAt(int row, int col) {
      return mTableSettings[row][col];
    }

    public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
      return (col > 0);
    }

    public void setValueAt(Object value, int row, int col) {
      mTableSettings[row][col] = value;
      fireTableCellUpdated(row, col);
    }
    
    public void applyChanges() {
      for (int i=0;i<mTableSettings.length;i++) {
        Table table = (Table)mTables.get(mTableSettings[i][0]);
        if (table != null) {
          table.setActive(((Boolean)mTableSettings[i][1]).booleanValue());
        }
      }
    }
  }  
}
