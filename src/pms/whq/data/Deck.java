/*
 * Deck.java
 *
 * Created on September 29, 2005, 2:42 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pms.whq.data;

import java.util.*;

/**
 *
 * @author psiegel
 */
public class Deck implements EventList {
  
  protected List      mDraw;
  protected List      mDiscard;
  
  /** Creates a new instance of Deck */
  public Deck() {
    mDraw = new ArrayList();
    mDiscard = new ArrayList();
  }
  
  public void addEntry(Object o) {
    mDraw.add(o);
  }
  
  public void addEntries(Collection c) {
    mDraw.addAll(c);    
  }
  
  public Object getEntry() {
    Object entry = null;
    
    if (mDraw.size() < 1) {
      shuffle();
    }
    if (mDraw.size() > 0) {
      entry = mDraw.remove(0);
      mDiscard.add(entry);
    }
    
    return entry;
  }
  
  public int size() {
    return mDraw.size() + mDiscard.size();
  }
  
  public void shuffle() {
    mDiscard.addAll(mDraw);
    mDraw.clear();

    Iterator i = mDiscard.iterator();
    while (i.hasNext()) {
      mDraw.add((int)(Math.random() * (mDraw.size() - 1)), i.next());
    } 
    
    mDiscard.clear();
  }  
}
