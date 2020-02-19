/*
 * MonsterCardPanel.java
 *
 * Created on September 28, 2005, 9:51 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.util.Map;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.border.*;

import pms.whq.Settings;
import pms.whq.data.Monster;
import pms.whq.data.Rule;
import pms.whq.data.SpecialContainer;

import pms.whq.ui.CardPanel;
import pms.whq.ui.GoldLabel;

/**
 *
 * @author psiegel
 */
public class MonsterCardPanel extends CardPanel implements ActionListener {
  
  protected Monster           mMonster;
  protected SpecialContainer  mAltSpecials;
  protected boolean           mAppendSpecials;
  
  protected JComponent      mBottomPanel;
  protected Map             mRules;
  
  
  /** Creates a new instance of MonsterCardPanel */
  public MonsterCardPanel(Monster m, String title, SpecialContainer altSpecials, 
                          boolean appendSpecials, Map rules) 
  {    
    super(title);
    mMonster = m;
    mAltSpecials = altSpecials;
    mAppendSpecials = appendSpecials;
    mRules = rules;
  }
  
  protected void doLayoutContents(GridBagConstraints c) {
    //  Monster stats
    StatPanel statPanel = new StatPanel(mMonster);
    add(statPanel, c);
    c.gridy++;

    //  To hit chart
    ToHitPanel toHitPanel = new ToHitPanel(Integer.parseInt(mMonster.weaponskill));
    c.insets.right = 1;
    add(toHitPanel, c);
    c.insets.right = 0;
    c.gridy++;
        
    //  Monster description area
    mBottomPanel = createBottomPanel();
    c.weighty = 1;
    c.gridheight = c.REMAINDER;
    add(mBottomPanel, c);
  }
  
  protected JPanel createBottomPanel() {
    JPanel panel = new JPanel();
    panel.setBackground(Color.YELLOW);
    panel.setOpaque(false);
    panel.setBorder(new EmptyBorder(0, 0, 6, 6));
    panel.setLayout(new GridBagLayout());

    JLabel label = new JLabel("Special Rules");
    label.setFont(new Font("Arial", Font.BOLD, 18));
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridwidth = constraints.REMAINDER;
    constraints.fill = constraints.BOTH;
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1;
    constraints.weighty = 0;
    constraints.gridwidth = 1;
    constraints.gridheight = 1;
    constraints.insets.left = 4;
    panel.add(label, constraints);
    
    JComponent special = createSpecialArea();

    JScrollPane scrollPane = 
      new JScrollPane(special, 
                      ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setOpaque(false);
    scrollPane.setBackground(Color.RED);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
    scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.weightx = 1;
    constraints.weighty = 1;
    constraints.gridwidth = 1;
    constraints.gridheight = 1;
    panel.add(scrollPane, constraints);

    String imgDir = Settings.getSetting(Settings.IMG_DIR);
    
    GoldLabel gold = 
      new GoldLabel(mMonster.gold, Settings.loadImage(imgDir + "gold.png"));
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.weightx = 1;
    constraints.weighty = 0;
    constraints.gridwidth = constraints.REMAINDER;
    constraints.gridheight = constraints.REMAINDER;
    panel.add(gold, constraints);
        
    String monsterImgDir = 
      Settings.getSetting(Settings.MONSTER_IMG_DIR);
    String imageName = mMonster.id + ".png";
    imageName = imageName.toLowerCase().replace(' ', '-');
    BufferedImage image = 
       Settings.loadImage(monsterImgDir + imageName);
    if (image == null) {
      imageName = mMonster.name + ".png";
      imageName = imageName.toLowerCase().replace(' ', '-');
      image = 
        Settings.loadImage(monsterImgDir + imageName);
    }
    
    if (image != null) {
      JLabel icon = new JLabel();
      icon.setIcon(new ImageIcon(image));
      icon.setVerticalAlignment(SwingConstants.BOTTOM);
      
      constraints.gridx = 1;
      constraints.gridy = 0;
      constraints.weightx = 0;
      constraints.weighty = 1;
      constraints.gridwidth = constraints.REMAINDER;
      constraints.gridheight = constraints.REMAINDER;
      constraints.insets.left = constraints.insets.right = 2;
      panel.add(icon, constraints);
    }
    
    return panel;
  }
  
  protected JComponent createSpecialArea() {    
    JPanel specialPanel = new JPanel();
    specialPanel.setLayout(new GridBagLayout());
    specialPanel.setOpaque(false);
    specialPanel.setBackground(Color.GREEN);

    GridBagConstraints c = new GridBagConstraints();
    c.fill = c.BOTH;
    c.gridx = c.gridy = 0;
    c.weightx = 1;
    c.weighty = 1;
    c.insets.bottom = 4;
    c.anchor = c.NORTHEAST;    

    boolean includeAltSpecials = mAltSpecials.hasAnySpecial();    
    boolean includeMonsterSpecials = mMonster.hasAnySpecial() &&
                                     (mAppendSpecials || !includeAltSpecials);

    //  First, any straight up special text
    String specialText = "";
    if (includeMonsterSpecials && mMonster.hasSpecialText()) {
      specialText += mMonster.special;
      if (includeAltSpecials && mAltSpecials.hasSpecialText()) {
        specialText += "\n";
      }
    }
    if (includeAltSpecials && mAltSpecials.hasSpecialText()) {
      specialText += mAltSpecials.special;
    }
    if (specialText.length() > 0) {
      JTextArea text = new JTextArea(specialText);
      text.setFont(new Font("Arial", Font.PLAIN, 18));
      text.setOpaque(false);
      text.setBackground(Color.YELLOW);
      text.setLineWrap(true);
      text.setWrapStyleWord(true);
      text.setEditable(false);      
      specialPanel.add(text, c);
      c.gridy++;
      
      c.weighty = 0;
    }
    
    if ((includeMonsterSpecials && (mMonster.hasSpecialLinks() || mMonster.hasMagic())) ||
        (includeAltSpecials && (mAltSpecials.hasSpecialLinks() || mAltSpecials.hasMagic())))
    {
      JPanel panel = new JPanel();
      panel.setLayout(new TallFlowLayout(TallFlowLayout.LEFT, 0, 0));
      panel.setBackground(Color.BLUE);
      panel.setOpaque(false);
      
      boolean anyLinks = mMonster.hasSpecialLinks() || mAltSpecials.hasSpecialLinks();
      
      if (includeMonsterSpecials) {
        if (mMonster.hasMagic()) {
          addMagic(mMonster.magicType, mMonster.magicLevel, panel, anyLinks || (mAltSpecials.hasMagic()));
        }
        addSpecials(mMonster.specialLinks, panel);
      }
      if (includeAltSpecials) {
        if (mAltSpecials.hasMagic()) {
          addMagic(mAltSpecials.magicType, mAltSpecials.magicLevel, panel, anyLinks);
        }
        addSpecials(mAltSpecials.specialLinks, panel);
      }
            
      specialPanel.add(panel, c);
      c.gridy++;
    }
    
    
    return specialPanel;
  }
  
  protected void addMagic(String type, int level, Container c, boolean moreSpecials) {
    Rule rule = (Rule)mRules.get(type);
    JButton button = createLinkedItem(type, rule.name + " ");
    button.setToolTipText(rule.name + " Magic -- Click for Spell List");
    c.add(button);

    String text = "Magic " + mMonster.magicLevel;
    if (moreSpecials) {
      text += "; ";
    } else {
      text += ".";
    }

    c.add(createLinkedItem("rpb-magic", text));
  }
  
  protected void addSpecials(Map specials, Container c) {
    Iterator i = specials.keySet().iterator();
    
    while (i.hasNext()) {
      String id = (String)i.next();
      String text = (String)specials.get(id);

      if (i.hasNext()) {
        text += "; ";
      } else {
        text += ".";
      }

      c.add(createLinkedItem(id, text));
    }
  }
  
  protected JButton createLinkedItem(String id, String label) {    
    HoverButton button = new HoverButton(id, label);
    button.setOpaque(false);
    button.setBorder(new EmptyBorder(0, 0, 0, 0));
    button.setContentAreaFilled(false);
    button.setHoverColor(new Color(192, 0, 0));
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    button.setFont(new Font("Arial", Font.PLAIN, 14));
    button.setToolTipText(label + " -- Click for Details");      
    button.addActionListener(this);    
    return button;
  }
  
  protected char getTypeChar() { 
    return 'M';
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);
    
    //  Fill the bottom area with a gradient
    if (mBottomPanel != null) {
      Paint p = g2d.getPaint();
      
      Shape clip = g2d.getClip();
      g2d.setClip(getInnerBounds());

      Rectangle bottom = mBottomPanel.getBounds();
      g2d.setPaint(new GradientPaint(bottom.width/2, 
                                     bottom.y, 
                                     new Color(99, 130, 197), 
                                     bottom.width/2, 
                                     bottom.y + bottom.height, 
                                     Color.WHITE));
      g2d.fill(bottom);
      
      g2d.setClip(clip);
      g2d.setPaint(p);
    }
  }
  
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() instanceof HoverButton) {
      HoverButton button = (HoverButton)e.getSource();
      String id = button.getRuleID();

      Rule rule = (Rule)mRules.get(id);
      if (rule != null) {
        RulesDialog.showRule(rule);
      } else {
        String msg = "Unable to find rule: " + id;
        JOptionPane.showMessageDialog(this, msg, "Rule Not Found", 
                                      JOptionPane.WARNING_MESSAGE);
      }
    }
  }
}
