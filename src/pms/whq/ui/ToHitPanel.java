/*
 * ToHitPanel.java
 *
 * Created on April 21, 2005, 2:34 PM
 */

package pms.whq.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 *
 * @author psiegel
 */
public class ToHitPanel extends JPanel {
  
  private int        mWeaponSkill;
  private JLabel     mTargetWS[];
  private JLabel     mRollNeeded[];
  
  private static final int    NUM_ENTRIES     = 10;
  
  /** Creates a new instance of ToHitPanel */
  public ToHitPanel(int weaponskill) {
    setOpaque(false);
    layoutSelf();
    
    setWeaponSkill(weaponskill);
  }
  
  private void layoutSelf() {
    setBorder(new LineBorder(Color.BLACK));
    
    Font font = new Font("Arial", Font.BOLD, 18);

    mRollNeeded = new JLabel[NUM_ENTRIES];
    mTargetWS = new JLabel[NUM_ENTRIES];
    
    setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.weighty = 0;
    constraints.fill = constraints.HORIZONTAL;
    constraints.ipadx = constraints.ipady = 2;

    LineBorder border = new LineBorder(Color.GRAY);
    
    JLabel targetWS = new JLabel("Opponent's WS");
    targetWS.setFont(font);
    targetWS.setHorizontalAlignment(SwingConstants.LEFT);
    targetWS.setBorder(border);
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1;
    add(targetWS, constraints);
    
    JLabel rollNeeded = new JLabel("To Hit Foe");
    rollNeeded.setFont(font);
    rollNeeded.setHorizontalAlignment(SwingConstants.LEFT);
    rollNeeded.setBorder(border);
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.weightx = 1;
    add(rollNeeded, constraints);
        
    constraints.weightx = 0;
    constraints.ipadx = 10;
    for (int i=0;i<NUM_ENTRIES;i++) {         
      mTargetWS[i] = new JLabel();
      mTargetWS[i].setFont(font);
      mTargetWS[i].setHorizontalAlignment(SwingConstants.CENTER);
      mTargetWS[i].setBorder(border);
      constraints.gridx = (i+1);
      constraints.gridy = 0;
      add(mTargetWS[i], constraints);

      mRollNeeded[i] = new JLabel();
      mRollNeeded[i].setFont(font);
      mRollNeeded[i].setHorizontalAlignment(JLabel.CENTER);
      mRollNeeded[i].setBorder(border);
      constraints.gridx = (i+1);
      constraints.gridy = 1;      
      add(mRollNeeded[i], constraints);
    }        
  }
  
  public void setWeaponSkill(int ws) {
    mWeaponSkill = ws;

    //  If there was ever evidence of WS > 10, the following
    //  would allow this panel to adapt.
//    int startingWS = Math.max(1, mWeaponSkill - 4);
    
    int startingWS = 1;    
    for (int i=0;i<NUM_ENTRIES;i++) {
      int nextWS = startingWS+i;
      mTargetWS[i].setText(String.valueOf(nextWS));
      
      int rollNeeded = 0;
      if ((nextWS < (mWeaponSkill - 5)) ||
         ((mWeaponSkill > 2) && (nextWS == 1)))
      {
        rollNeeded = 2;
      } else if (nextWS < mWeaponSkill) {
        rollNeeded = 3;      
      } else if (nextWS <= (mWeaponSkill * 2)) {
        rollNeeded = 4;
      } else if (nextWS <= (mWeaponSkill * 3)) {
        rollNeeded = 5;
      } else {
        rollNeeded = 6;
      }
      mRollNeeded[i].setText(String.valueOf(rollNeeded));
    }            
  }
  
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);
    super.paintComponent(g2d);
  }
}
