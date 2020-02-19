/*
 * SmartLabel.java
 *
 * Created on September 26, 2005, 9:51 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.ui;

import java.awt.*;
import javax.swing.*;

import pms.whq.Settings;

/**
 *
 * @author psiegel
 */
public class SmartLabel extends JComponent {
  
  protected String        mLabel;
  protected String[]      mLines;
  protected Font          mFont;
  protected int           mMinFontSize;
  protected int           mMaxFontSize;
  
  /** Creates a new instance of SmartLabel */
  public SmartLabel(String label, int minFontSize, int maxFontSize) {
    mLabel = label;
    mMinFontSize = minFontSize;
    mMaxFontSize = maxFontSize;
    mFont = Settings.getFont("CaslonAntique", Font.PLAIN, mMaxFontSize);
  }
  
  public void validate() {
    super.validate();
    mLines = null;
  }
  
  public void paintComponent(Graphics g) {
    int w = getWidth();
    int h = getHeight();
    
    if (mLines == null) {
      recalcText(g);
    }
        
    //  Draw the border and fill it with our texture.
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);
    
    if (isOpaque()) {
      g2d.setColor(getBackground());
      g2d.fillRect(0, 0, w, h);
    }
    
    g2d.setColor(getForeground());
    g2d.setFont(mFont);
    FontMetrics metrics = g2d.getFontMetrics();
    Dimension maxBounds = getMaxBounds(g);
    
    int x = 0;
    int y = metrics.getMaxAscent() + ((h - maxBounds.height)/2) + 1;
    for (int i=0;i<mLines.length;i++) {
      String line = mLines[i];      
      while (metrics.stringWidth(line) > w) {
        line = line.substring(0, line.length() - 2);
      }
      
      x = (w - metrics.stringWidth(line))/2;      
      g2d.drawString(mLines[i], x, y);      
      y += metrics.getHeight();
    }
  }
  
  protected void recalcText(Graphics g) {
    int w = getWidth();
    int h = getHeight();
    mFont = mFont.deriveFont(mMaxFontSize);
       
    mLines = new String[1];
    mLines[0] = mLabel;
    Dimension d = getMaxBounds(g);
    
    while ((d.width > w) || (d.height > h)) {
      String[] words = mLabel.split(" ");
      if ((mLines.length < 2) && (words.length > 2)) {
        mLines = new String[2];
        
        mLines[0] = words[0];
        mLines[1] = words[words.length-1];
        
        int word1 = 1;
        int word2 = words.length - 2;
        while (word2 >= word1) {          
          if (mLines[0].length() > mLines[1].length()) {
            mLines[1] = words[word2] + " " + mLines[1];
            word2--;
          } else {
            mLines[0] += " " + words[word1];
            word1++;
          }         
        }        
      } else {
        if (mFont.getSize() > mMinFontSize) {
          mFont = mFont.deriveFont(mFont.getSize2D() - 1.0f);
        } else {
          break;
        }
      }
      
      d = getMaxBounds(g);
    }    
  }
  
  protected Dimension getMaxBounds(Graphics g) {
    Dimension d = new Dimension();
    FontMetrics metrics = g.getFontMetrics(mFont);
    
    d.height =  (metrics.getAscent() + metrics.getDescent()) * mLines.length;
    d.height += (metrics.getLeading() * (mLines.length - 1));

    d.width = 0;
    for (int i=0;i<mLines.length;i++) {
      d.width = Math.max(d.width, metrics.stringWidth(mLines[i]));
    }
    
    return d;
  }
  
  public void setText(String s) {
    mLabel = s;
    mLines = null;
  }    
}
