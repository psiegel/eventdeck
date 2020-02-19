/*
 * TexturedPanel.java
 *
 * Created on October 10, 2005, 9:15 AM
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
public class TexturedPanel extends JPanel {
  
  protected Paint   mBGTexture;
  
  /** Creates a new instance of TexturedPanel */
  public TexturedPanel() {
    String imgDir = Settings.getSetting(Settings.IMG_DIR);
    BufferedImage img = Settings.loadImage(imgDir + "paper-bg.gif");
    if (img != null) {
      Rectangle bounds = new Rectangle(0, 0, img.getWidth(), img.getHeight());
      mBGTexture = new TexturePaint(img, bounds);
    }
  }
  
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);
      
    super.paintComponent(g2d);

    int w = getWidth();
    int h = getHeight();
        
    //  Fill the background
    if (mBGTexture != null) {     
      Paint p = g2d.getPaint();
      g2d.setPaint(mBGTexture);
      g2d.fillRect(0, 0, w, h);
      g2d.setPaint(p);
    }
  }
  
}
