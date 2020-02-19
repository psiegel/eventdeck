/*
 * StatPanel.java
 *
 * Created on April 21, 2005, 3:59 PM
 */

package pms.whq.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import pms.whq.data.Monster;
import pms.whq.Settings;

/**
 *
 * @author psiegel
 */
public class StatPanel extends JPanel {
  
  private JComponent      mWounds;
  private JComponent      mMove;
  private JComponent      mWeaponSkill;
  private JComponent      mBallisticSkill;
  private JComponent      mStrength;
  private JComponent      mToughness;
  private JComponent      mAttacks;
  private JComponent      mDamage;
  
  /** Creates a new instance of StatPanel */
  public StatPanel(Monster monster) {
    setOpaque(false);
    layoutSelf(monster);
  }
  
  private void layoutSelf(Monster m) {
    setBorder(new LineBorder(Color.BLACK));    
    
    setLayout(new GridBagLayout());    
    
    mWounds = createLabelPair("Wounds:", m.wounds, 0);
    mMove = createLabelPair("Move:", m.move, 1);
    mWeaponSkill = createLabelPair("Weapon Skill:", m.weaponskill, 2);
    mBallisticSkill = createLabelPair("Ballistic Skill:", m.ballisticskill, 3);
    mStrength = createLabelPair("Strength:", m.strength, 4);
    mToughness = createLabelPair("Toughness:", getToughness(m), 5);
    mAttacks = createLabelPair("Attacks:", m.attacks, 6);
    mDamage = createLabelPair("Damage:", m.damage, 7);
  }
  
  private JComponent createLabelPair(String name, String value, int row) {
    Font font = Settings.getFont("CopperplateGothicBold", Font.PLAIN, 18);

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.weighty = 0;
    constraints.fill = constraints.NONE;
    
    JLabel label = new JLabel(name, SwingConstants.RIGHT);
    label.setFont(font);
    constraints.gridx = 0;
    constraints.gridy = row;
    constraints.weightx = 0.5;
    constraints.insets.left = 0;
    constraints.insets.right = 4;
      constraints.anchor = constraints.EAST;
    add(label, constraints);
    
    label = new JLabel(value, SwingConstants.LEFT);
    label.setFont(font);
    if (value == "S") {
      label.setForeground(new Color(192, 0, 0));
    }

    constraints.gridx = 1;
    constraints.gridy = row;
    constraints.weightx = 0.5;
    constraints.anchor = constraints.WEST;
    constraints.insets.left = 4;
    constraints.insets.right = 0;
    add(label, constraints);    
    
    return label;
  }
  
  protected String getToughness(Monster m) {
    String toughness = "";
    
    if (m.armor != "-") {
      StringBuffer buf = new StringBuffer();
      buf.append(m.toughness);
      buf.append(" (");
      buf.append(m.armor);
      buf.append(")");
      toughness = buf.toString();
    } else {
      toughness = m.toughness;
    }
    
    return toughness;
  }
  
  protected void checkForSpecial(JLabel label) {
    String text = label.getText();
    if (text == "S") {
      label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
  }
  
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);
    super.paintComponent(g2d);
  } 
}
