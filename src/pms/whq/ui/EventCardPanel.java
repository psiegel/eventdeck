/*
 * EventCardPanel.java
 *
 * Created on September 28, 2005, 10:13 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import pms.whq.Settings;
import pms.whq.data.Event;

/**
 *
 * @author psiegel
 */
public class EventCardPanel extends CardPanel {
  
  public static final int     BOTTOM_BAR    = 38;
  
  protected Event     mEvent;
  
  /** Creates a new instance of EventCardPanel */
  public EventCardPanel(Event e) {
    super(e.name);
    mEvent = e;
  }
  
  protected char getTypeChar() {
    return 'E';
  }
  
  protected void doLayoutContents(GridBagConstraints c) {
    c.insets.left = 4;
    c.insets.right = 2;

    if (mEvent.flavor.length() > 0) {
      JTextArea flavorText = new JTextArea(mEvent.flavor);
      flavorText.setFont(new Font("Arial", Font.ITALIC, 14));
      flavorText.setOpaque(false);
      flavorText.setLineWrap(true);
      flavorText.setWrapStyleWord(true);
      flavorText.setEditable(false);
      
      add(flavorText, c);
      c.gridy++;
    }
    
    String content = "<div style='font-family: \"Arial\"; font-size: 18'>" + mEvent.rules + "</div>";
    
    JEditorPane text = new JEditorPane("text/html", content);
    text.setBackground(Color.BLUE);
    text.setOpaque(false);
    text.setEditable(false);
    text.setCaretPosition(0);

    JScrollPane scrollPane = 
      new JScrollPane(text, 
                      ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
    scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
    
    c.weighty = 1;
        
    add(scrollPane, c);
    c.weighty = 0;
    c.gridy++;
        
    if (mEvent.special.length() > 0) {
      int cardW = Settings.getSettingAsInt(Settings.CARD_WIDTH);
      JLabel specialText = new MultiLineLabel(mEvent.special, cardW);
      specialText.setFont(new Font("Arial Black", Font.PLAIN, 14));
      specialText.setBackground(Color.YELLOW);
      specialText.setOpaque(false);
      
      add(specialText, c);
      c.gridy++;
    }
    
    c.ipady = BOTTOM_BAR - 18;
    JPanel space = new JPanel();
    space.setOpaque(false);
    add(space, c);
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);
    
    //  Fill the bottom area with black
    Shape clip = g2d.getClip();
    g2d.setClip(getInnerBounds());

    int y = getHeight() - BOTTOM_BAR;
    Rectangle bottom = new Rectangle(0, y, getWidth(), BOTTOM_BAR);
    g2d.setColor(Color.BLACK);
    g2d.fill(bottom);
    
    if (!mEvent.treasure) {
      String s = "NO TREASURE FOR COMPLETING EVENT";
      g2d.setFont(new Font("Arial Bold", Font.BOLD, 14));
      FontMetrics metrics = g2d.getFontMetrics();
      g2d.setColor(Color.YELLOW);
      
      int x = (getWidth() - metrics.stringWidth(s))/2;
      y += (BOTTOM_BAR - metrics.getHeight())/2 + 4;
      g2d.drawString(s, x, y);
    }

    g2d.setClip(clip);
  }
}
