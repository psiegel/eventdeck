/*
 * RulesDialog.java
 *
 * Created on October 10, 2005, 8:37 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import pms.whq.Settings;
import pms.whq.data.Rule;

/**
 *
 * @author psiegel
 */
public class RulesDialog extends JDialog implements ActionListener {
  
  protected JLabel        mTitle;
  protected JEditorPane   mText;
  protected JScrollPane   mScroll;
  protected JButton       mOK;
    
  protected static RulesDialog      mInstance;
  
  /** Creates a new instance of RulesDialog */
  private RulesDialog() {
    layoutDialog();
    
  }
  
  protected void layoutDialog() {
    setBackground(Color.GREEN);
    
    JPanel panel = new TexturedPanel();    
    panel.setLayout(new GridBagLayout());
    panel.setBackground(Color.BLUE);
    panel.setOpaque(false);
    
    GridBagConstraints c = new GridBagConstraints();
    c.fill = c.BOTH;
    c.gridwidth = c.REMAINDER;
    c.gridheight = 1;
    c.gridx = c.gridy = 0;
    c.weightx = 1;
    c.weighty = 0;
    
    mTitle = new JLabel("Title");
    mTitle.setOpaque(false);
    mTitle.setFont(Settings.getFont("CaslonAntique", Font.BOLD, 28));
    c.insets.top = c.insets.bottom = 4;
    panel.add(mTitle, c);
    c.insets.set(0, 0, 0, 0);
    c.gridy++;
    
    mText = new JEditorPane("text/html", "Text");
    mText.setOpaque(false);
    mText.setEditable(false);
    c.weighty = 1;
    mScroll = new JScrollPane(mText, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    mScroll.setOpaque(false);
    mScroll.getViewport().setOpaque(false);
    mScroll.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
    mScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
    panel.add(mScroll, c);
    c.gridy++;
    
    c.weighty = 0;
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false);
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    
    mOK = new JButton("OK");
    mOK.addActionListener(this);
    buttonPanel.add(mOK);
    
    c.gridheight = c.REMAINDER;
    panel.add(buttonPanel, c);
    c.gridy++;
    
    setContentPane(panel);
  }


  
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == mOK) {
      setVisible(false);
    }
  }

  public void setTitle(String s) {
    super.setTitle(s);
    mTitle.setText(s);
  }
  
  public void setText(String s) {
    String content = "<div style='font-family: \"BookAntiqua\"; font-size: 18'>" + s + "</div>";
    mText.setText(content);
    mText.setCaretPosition(0);
  }
  
  public static void showRule(Rule rule) {
    if (mInstance == null) {
      mInstance = new RulesDialog();
    }
    
    String title = rule.name;
    if (rule.type == "magic") {
      title += " Magic";
    }
    
    mInstance.setVisible(false);
    mInstance.setTitle(title);
    mInstance.setText(rule.text);
    mInstance.setSize(new Dimension(Settings.getSettingAsInt(Settings.CARD_HEIGHT),
                                    Settings.getSettingAsInt(Settings.CARD_WIDTH)));
    mInstance.invalidate();
    mInstance.setVisible(true);
  }
  
}
