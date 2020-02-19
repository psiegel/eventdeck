/*
 * MultiLineLabel.java
 *
 * Created on September 28, 2005, 11:22 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.ui;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author psiegel
 */
public class MultiLineLabel extends JLabel {
  
  protected int       mPreferredWidth;
  protected boolean   mNeedsRecalc;
            
  /** Creates a new instance of MultiLineLabel */
  public MultiLineLabel(String text, int width) {
    super(text);
    mPreferredWidth = width;
    mNeedsRecalc = true;
  }
    
  public void paintComponent(Graphics g) {
    if (mNeedsRecalc) {
      recalcText();
    }
    
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);
    
    int w = getWidth();
    int h = getHeight();

    if (isOpaque()) {
      g2d.setColor(getBackground());
      g2d.fillRect(0, 0, w, h);
    }
    
    String text = getText();
    String lines[] = text.split("\n");
    
    g2d.setColor(getForeground());
    g2d.setFont(getFont());
    FontMetrics metrics = g2d.getFontMetrics();
    
    int x = 0;
    int y = (h - (metrics.getHeight() * lines.length)) / 2;
    y += metrics.getAscent();

    for (int i=0;i<lines.length;i++) {
      x = ((w - metrics.stringWidth(lines[i]))/2);      
      g2d.drawString(lines[i], x, y);
      
      y+= metrics.getHeight();
    }    
  }
  
  public void setSize(int w, int h) {
    super.setSize(w, h);
    mPreferredWidth = w;
    mNeedsRecalc = true;
  }
  
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    mPreferredWidth = width;
    mNeedsRecalc = true;
  }
  
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }
  
  public Dimension getPreferredSize() {
    if (mNeedsRecalc) {
      recalcText();
    }
    
    Dimension d = new Dimension();
    
    Graphics g = getGraphics();
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);
    FontMetrics metrics = g2d.getFontMetrics(getFont());

    String text = getText();
    String lines[] = text.split("\n");
    for (int i=0;i<lines.length;i++) {
      d.width = Math.max(d.width, metrics.stringWidth(lines[i]));
      d.height += metrics.getHeight();
    }
    d.height += metrics.getDescent();
    
    return d;
  }
  
  protected void recalcText() {
    String text = getText();
    text = text.replace('\n', ' ');
    String words[] = text.split(" ");
    
    Graphics g = getGraphics();
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);
    FontMetrics metrics = g2d.getFontMetrics(getFont());

    String finalText = "";
    String nextLine = "";
    for (int i=0;i<words.length;i++) {
      String testLine = nextLine;
      if (testLine.length() > 1) {
        testLine += " ";
      }
      testLine += words[i];
      
      if (metrics.stringWidth(testLine) > mPreferredWidth) {
        finalText += nextLine + "\n";
        nextLine = words[i];
      } else {
        nextLine = testLine;
      }
    }
    finalText += nextLine;
    
    setText(finalText);
    mNeedsRecalc = false;
  }
  
}
