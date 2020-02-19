/*
 * MonsterEntry.java
 *
 * Created on September 25, 2005, 6:28 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.data;

import org.w3c.dom.*;

import pms.whq.util.XMLUtil;

/**
 *
 * @author psiegel
 */
public class MonsterEntry extends SpecialContainer {
  
  public String       id;
  public int          min;
  public int          max;
  
  public boolean      appendSpecials = true;
  
  /** Creates a new instance of MonsterEntry */
  public MonsterEntry(String id, int min, int max) {
    super();
    this.id = id;
    this.min = min;
    this.max = max;
  }
  
  public MonsterEntry(Node node) {
    super(node);
    
    id = XMLUtil.getAttribute(node, "id");    
    String number = XMLUtil.getAttribute(node, "number");
    
    Node special = XMLUtil.getNamedChild(node, "special");
    if (special != null) {
      appendSpecials = Boolean.parseBoolean(XMLUtil.getAttribute(special, "append"));
    }
    
    min = max = 0;
    
    try {
      if (number.indexOf('-') > -1) {
        String[] values = number.split("-");
        min = Integer.parseInt(values[0]);
        max = Integer.parseInt(values[1]);
      } else {
        min = max = Integer.parseInt(number);
      }
    } catch (NumberFormatException nfe) {
      //  Just make sure the range remains at 0
      min = max = 0;
    }
  }

  public int getNumber() {
    return min + (int)((max - min) * Math.random());
  }
  
}
