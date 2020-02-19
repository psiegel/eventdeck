/*
 * HoverButton.java
 *
 * Created on October 7, 2005, 4:55 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author psiegel
 */
public class HoverButton extends JButton {
  
  protected String      mRuleID;
  protected Color       mForegroundColor;
  protected Color       mHoverColor;
  
  /** Creates a new instance of HoverButton */
  public HoverButton(String ruleID) {
    super();
    mRuleID = ruleID;
    mForegroundColor = getForeground();
    mHoverColor = getForeground();
  }
  
  public HoverButton(String ruleID, String label) {
    super(label);
    mRuleID = ruleID;
    mForegroundColor = getForeground();
    mHoverColor = getForeground();
  }
  
  public String getRuleID() {
    return mRuleID;
  }
  
  public void setForegroundColor(Color c) {
    mForegroundColor = c;
    setForeground(c);
  }
  
  public void setHoverColor(Color c) {
    mHoverColor = c;
  }

  protected void processMouseEvent(MouseEvent e) {    
    switch (e.getID()) {
      case MouseEvent.MOUSE_ENTERED:
        setForeground(mHoverColor);
        break;
      case MouseEvent.MOUSE_EXITED:
        setForeground(mForegroundColor);
        break;
    }
    super.processMouseEvent(e);
  }
  
}
