/*
 * CardPanel.java
 *
 * Created on April 19, 2005, 10:32 AM
 */

package pms.whq.ui;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.border.*;

import pms.whq.Settings;
import pms.whq.data.Event;
import pms.whq.data.Monster;
import pms.whq.ui.SmartLabel;

/**
 *
 * @author psiegel
 */
public abstract class CardPanel extends JPanel {
  
  private static TexturePaint   mBGTexture = null;
 
  protected JComponent      mTitleLabel;
  protected String          mTitle;
  
  private static final int  BORDER_WIDTH        = 16;
  private static final int  TITLE_HEIGHT        = 80;
  private static final int  TYPE_CIRCLE_SIZE    = 64;
  
  private static final int  TYPE_MONSTER        = 1;
  private static final int  TYPE_EVENT          = 2;
      
  /** Creates a new instance of CardPanel */
  public CardPanel(String title) {    
    setBorder(new EmptyBorder(BORDER_WIDTH, 
                              BORDER_WIDTH, 
                              BORDER_WIDTH, 
                              BORDER_WIDTH));
    
    loadBGImage();
    setBackground(Color.WHITE);
    setOpaque(true);
    
    mTitle = title.toUpperCase();    
  }
  
  protected abstract char getTypeChar();
  protected abstract void doLayoutContents(GridBagConstraints c);
  
  public Dimension getPreferredSize() {
    Dimension d = new Dimension();
    
    d.width = Settings.getSettingAsInt(Settings.CARD_WIDTH);
    d.height = Settings.getSettingAsInt(Settings.CARD_HEIGHT);
    
    return d;
  }
  
  public void layoutPanel() {
    GridBagLayout layout = new GridBagLayout();
    setLayout(layout);

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridwidth = constraints.REMAINDER;
    constraints.fill = constraints.BOTH;
    
    //  Title
    SmartLabel titleComp = new SmartLabel(mTitle, 18, 48);
    titleComp.setForeground(Color.YELLOW);
    titleComp.setBackground(Color.BLUE);
    titleComp.setOpaque(false);
    mTitleLabel = titleComp;

    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1;
    constraints.weighty = 0;
    constraints.insets.left = constraints.insets.right = TYPE_CIRCLE_SIZE;
    add(mTitleLabel, constraints);    

    constraints.gridy++;
    constraints.insets.left = constraints.insets.right = 0;
    
    doLayoutContents(constraints);
  }
  
  public void validate() {
    super.validate();
    
    int w = getWidth();
    Dimension d = new Dimension(w - (TYPE_CIRCLE_SIZE*2), TITLE_HEIGHT);
    mTitleLabel.setPreferredSize(d);
    mTitleLabel.setMinimumSize(new Dimension(0, TITLE_HEIGHT));
    mTitleLabel.setMaximumSize(new Dimension(w, TITLE_HEIGHT));
  }
    
  private void loadBGImage() {
    if (mBGTexture == null) {
      String imgDir = Settings.getSetting(Settings.IMG_DIR);
      BufferedImage img = Settings.loadImage(imgDir + "paper-bg.gif");
      if (img != null) {
        Rectangle bounds = new Rectangle(0, 0, img.getWidth(), img.getHeight());
        mBGTexture = new TexturePaint(img, bounds);
      }
    }
  } 
    
  public void paintComponent(Graphics g) {
    int w = getWidth();
    int h = getHeight();
        
    //  Draw the border and fill it with our texture.
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);
      
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(2.0f));
    Paint p = g2d.getPaint();
    if (mBGTexture != null) {     
      g2d.setPaint(mBGTexture);
    }

    Shape rectBorder = getInnerBounds();    
    g2d.fill(rectBorder);
    g2d.setPaint(p);

    //  Draw the outer border
    g2d.draw(rectBorder);

    //  Fill the title background area
    Rectangle rectTop = new Rectangle(BORDER_WIDTH, BORDER_WIDTH, 
                                      w - (BORDER_WIDTH*2), TITLE_HEIGHT);
    g2d.fill(rectTop);
        
    //  Draw the type circles
    drawTypeCircle(g2d, 2, 2);
    drawTypeCircle(g2d, w-(TYPE_CIRCLE_SIZE+2), 2);    
  }  
  
  protected Shape getInnerBounds() {
    int w = getWidth();
    int h = getHeight();
    return new RoundRectangle2D.Float(BORDER_WIDTH, 
                                      BORDER_WIDTH, 
                                      w-(BORDER_WIDTH*2)-2, 
                                      h-(BORDER_WIDTH*2)-2, 
                                      32, 
                                      24);      
  }
  
  private void drawTypeCircle(Graphics2D g2d, int x, int y) {
    Ellipse2D circle = new Ellipse2D.Float(x, y, TYPE_CIRCLE_SIZE, TYPE_CIRCLE_SIZE);

    Paint p = g2d.getPaint();
    if (mBGTexture != null) {     
      g2d.setPaint(mBGTexture);
    }
    g2d.fill(circle);
    g2d.setPaint(p);
    
    g2d.draw(circle);
    
//    Font font = new Font("CasablancaAntique", Font.BOLD, 24);
    Font font = Settings.getFont("CaslonAntique", Font.BOLD, 48);
    g2d.setFont(font);
    
    char c = getTypeChar();
    FontMetrics metrics = g2d.getFontMetrics();
    int charWidth = metrics.charWidth(c);
    int charHeight = metrics.getAscent();
    g2d.drawString("" + c, 
                   x + ((TYPE_CIRCLE_SIZE - charWidth)/2), 
                   y + (TYPE_CIRCLE_SIZE/2) + (charHeight/2));
  }  
}
