/*
 * GoldLabel.java
 *
 * Created on September 26, 2005, 2:08 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.ui;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import pms.whq.Settings;

/**
 *
 * @author psiegel
 */
public class GoldLabel extends JComponent {
  
  protected String      mGold;
  protected Image       mImage;
  protected Font        mGoldFont;
  protected Font        mLabelFont;
  
  /** Creates a new instance of GoldLabel */
  public GoldLabel(String gold, Image image) {
    mGold = gold;
    mImage = image;
    
    mLabelFont = Settings.getFont("CopperplateGothicBold", Font.PLAIN, 18);
    mGoldFont = mLabelFont.deriveFont(Font.BOLD, 26);
        
    setForeground(Color.BLACK);
  }
  
  public Dimension getPreferredSize() {
    return new Dimension(mImage.getWidth(null), mImage.getHeight(null));
  }
  
  public Dimension getMinimumSize() {
    return new Dimension(mImage.getWidth(null), mImage.getHeight(null));
  }
  
  public void paintComponent(Graphics g) {
    int w = Math.min(mImage.getWidth(null), getWidth());
    int h = Math.min(mImage.getHeight(null), getHeight());
        
    //  Draw the background image
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);

    g2d.drawImage(mImage, 0, 0, w, h, null);
    
    //  Draw the text
    g2d.setFont(mLabelFont);
    g2d.setColor(getForeground());
    FontMetrics metrics = g2d.getFontMetrics();

    int x = (w - metrics.stringWidth("Value")) / 2;
    int y = metrics.getHeight() + 6;
    g2d.drawString("Value", x, y);
    
    x = (w - metrics.stringWidth("Each")) / 2;
    y = h - metrics.getAscent();
    g2d.drawString("Each", x, y);
    
    g2d.setFont(mGoldFont);
    metrics = g2d.getFontMetrics();
    
    String gold = mGold + "g";
    x = (w - metrics.stringWidth(gold)) / 2;
    y = ((h - metrics.getHeight()) / 2) + (metrics.getHeight() / 2) + 7;
    g2d.drawString(gold, x, y);    
  }
}
