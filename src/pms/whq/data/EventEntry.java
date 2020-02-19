/*
 * EventEntry.java
 *
 * Created on September 28, 2005, 9:32 AM
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
public class EventEntry {
  
  public String       id;
  
  /** Creates a new instance of EventEntry */
  public EventEntry(String id) {
    this.id = id;
  }
  
  public EventEntry(Node node) {
    id = XMLUtil.getAttribute(node, "id");    
  }
}
