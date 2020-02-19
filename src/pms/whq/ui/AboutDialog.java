/*
 * AboutFrame.java
 *
 * Created on November 17, 2005, 11:39 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

import pms.whq.Settings;

/**
 *
 * @author psiegel
 */
public class AboutDialog extends JDialog implements ActionListener  {
  
  protected JButton       mOK;

  protected static AboutDialog      mInstance;
  
  /** Creates a new instance of AboutFrame */
  private AboutDialog() {
    setTitle("Event Deck");
    setContentPane(createContentPane());
    pack();
  }
  
  public static void showAbout() {
    if (mInstance == null) {
      mInstance = new AboutDialog();
    }

    mInstance.setVisible(true);
  }
  
  protected JPanel createContentPane() {
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
    
    String monsterImgDir = 
      Settings.getSetting(Settings.MONSTER_IMG_DIR);
    BufferedImage image = 
       Settings.loadImage(monsterImgDir + "minotaur.png");
    if (image != null) {
      JLabel icon = new JLabel();
      icon.setIcon(new ImageIcon(image));
      icon.setVerticalAlignment(SwingConstants.TOP);
      c.insets.set(4, 4, 4, 4);
      c.gridwidth = 1;
      c.gridheight = c.REMAINDER;
      c.weighty = 1;
      c.weightx = 0;
      panel.add(icon, c);
  
      c.weightx = 1;
      c.weighty = 0;
      c.gridwidth = c.REMAINDER;
      c.gridheight = 1;
      c.gridx++;
      c.insets.set(0, 0, 0, 0);
    }    
    
    JLabel spacer = new JLabel(" ");
    c.weighty = 0.5;
    panel.add(spacer, c);
    c.weighty = 0;
    c.gridy++;
    
    JLabel title = new JLabel("Event Deck version 1.0");
    title.setHorizontalAlignment(SwingConstants.CENTER);
    title.setFont(Settings.getFont("CaslonAntique", Font.BOLD, 14));
    panel.add(title, c);
    c.gridy++;
    
    JLabel subTitle = new JLabel("\u00a9 2005, Paul Siegel");
    subTitle.setFont(Settings.getFont("BookAntiqua", Font.PLAIN, 12));
    subTitle.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(subTitle, c);
    c.gridy++;
    
    String disclaimer[] = { "Warhammer Quest is a trademark of Games Workshop Limited.",
                            "Text and Images displayed by this application are the",
                            "property of Games Workshop Limited unless stated otherwise." };
    
    spacer = new JLabel(" ");
    panel.add(spacer, c);
    c.gridy++;

    for (int i=0;i<disclaimer.length;i++) {                            
      JLabel label = new JLabel(disclaimer[i]);
      label.setFont(new Font("Arial", Font.PLAIN, 9));
      label.setHorizontalAlignment(SwingConstants.CENTER);
      panel.add(label, c);
      c.gridy++;
    }

    spacer = new JLabel(" ");
    c.weighty = 0.5;
    panel.add(spacer, c);
    c.weighty = 0;      
    c.gridy++;

    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false);
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    
    mOK = new JButton("OK");
    mOK.addActionListener(this);
    buttonPanel.add(mOK);
    
    c.gridwidth = c.REMAINDER;
    c.gridheight = c.REMAINDER;
    panel.add(buttonPanel, c);
    c.gridy++;
                            
    return panel;
  }
  
    
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == mOK) {
      setVisible(false);
    }
  }
}
