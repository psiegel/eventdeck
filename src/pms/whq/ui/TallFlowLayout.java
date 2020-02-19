/*
 * TallPanelLayout.java
 *
 * Created on October 7, 2005, 4:45 PM
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
public class TallFlowLayout extends FlowLayout {
  
  /** Creates a new instance of TallPanelLayout */
  public TallFlowLayout() {
    super();
  }
  
  public TallFlowLayout(int align) {
    super(align);
  }
  
  public TallFlowLayout(int align, int hgap, int vgap) {
    super(align, hgap, vgap);
  }
  
  public Dimension preferredLayoutSize(Container target) {
      
    synchronized (target.getTreeLock()) {
        
      int maxwidth = target.getParent().getSize().width;

      Dimension dim = new Dimension(0, 0);
      int nmembers = target.getComponentCount();
      boolean firstVisibleComponent = true;

      Insets insets = target.getInsets();

      int x = 0, y = insets.top + getVgap();
      int rowh = 0, roww = 0;
      for (int i = 0 ; i < nmembers ; i++) {
        Component m = target.getComponent(i);
        if (m.isVisible()) {
          Dimension d = m.getPreferredSize();
          m.setSize(d.width, d.height);

          if ((roww == 0) || ((roww + d.width) <= maxwidth)) {
            if (roww > 0) {
              roww += getHgap();
            }
            roww += d.width;
            rowh = Math.max(rowh, d.height);
          } else {
            dim.width = Math.max(dim.width, roww);
            dim.height += rowh;
            rowh = d.height;
            roww = d.width;
          }
        }
      }
      
      dim.height += rowh;

      dim.width += insets.left + insets.right + getHgap()*2;
      dim.height += insets.top + insets.bottom + getVgap()*2;

      return dim;
    }
  }
}
