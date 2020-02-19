/*
 * Table.java
 *
 * Created on September 25, 2005, 6:25 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.data;

import java.util.*;

import org.w3c.dom.*;

import pms.whq.Settings;
import pms.whq.util.XMLUtil;

/**
 *
 * @author psiegel
 */
public class Table implements EventList {
  
  protected String      mName;
  protected List        mMonsters;
  protected List        mEvents;
  protected boolean     mActive;
  
  public Table() {
    mName = "Table";
    mMonsters = new ArrayList();
    mEvents = new ArrayList();
    mActive = true;
  }
  
  /** Creates a new instance of Table */
  public Table(Node node) {
    this();
    mName = XMLUtil.getAttribute(node, "name");
    
    NodeList children = node.getChildNodes();
    for (int i=0;i<children.getLength();i++) {
      addNodeEntry(children.item(i));
    }
  }
    
  public void addEntry(Object o) {
    if ((o instanceof MonsterEntry) || (o instanceof List)) {
      mMonsters.add(o);
    } else if (o instanceof EventEntry) {
      mEvents.add(o);
    }
  }
  
  public void addEntries(Collection c) {
    Iterator i = c.iterator();
    while (i.hasNext()) {
      addEntry(i.next());
    }
  }
  
 public List getEntries() {
   List list = new ArrayList();
   list.addAll(mMonsters);
   list.addAll(mEvents);
   return list;
 }
  
  protected void addNodeEntry(Node node) {
    String type = node.getNodeName();
      
    if (type == "group") {
      List entryList = new ArrayList();

      NodeList entryNodes = node.getChildNodes();
      for (int k=0;k<entryNodes.getLength();k++) {
        Object entry = nodeToEntry(entryNodes.item(k));
        if (entry != null) {
          entryList.add(entry);
        }
      }
      
      mMonsters.add(entryList);
    } else if (type == "monster") {
      mMonsters.add(nodeToEntry(node));
    } else if (type == "event") {
      mEvents.add(nodeToEntry(node));
    }
  }
  
  protected Object nodeToEntry(Node node) {
    String type = node.getNodeName();
    if (type == "monster") {
      return new MonsterEntry(node);
    } else if (type == "event") {
      return new EventEntry(node);
    }
    return null;
  }
  
  public int size() {
    return mMonsters.size() + mEvents.size();
  }
  
  public String getName() {
    return mName;
  }
  
  public Object getEntry() {
    Object entry = null;
    boolean getMonster = false;
    boolean getEvent = false;

    if (mMonsters.size() > 0) {
      if (mEvents.size() > 0) {
        int eventProbablity = 
          Settings.getSettingAsInt(Settings.EVENT_PROBABILITY);
        if ((Math.random() * 100) > eventProbablity) {
          getMonster = true;
        } else {
          getEvent = true;
        }
      } else {
        getMonster = true;
      }
    } else if (mEvents.size() > 0) {
      getEvent = true;
    }
    
    if (getMonster) {
      entry = mMonsters.get((int)(Math.random() * (mMonsters.size() - 1)));
    } else if (getEvent) {
      entry = mEvents.get((int)(Math.random() * (mMonsters.size() - 1)));
    }
    
    return entry;
  }
  
  public boolean isActive() {
    return mActive;
  }
  
  public void setActive(boolean active) {
    mActive = active;
  }
  
}
