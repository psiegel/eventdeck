/*
 * Monster.java
 *
 * Created on April 21, 2005, 4:00 PM
 */

package pms.whq.data;

import java.util.*;
import org.w3c.dom.*;

import pms.whq.util.XMLUtil;

/**
 *
 * @author psiegel
 */
public class Monster extends SpecialContainer {
  public String     id;
  public String     name;
  public String     plural;
  public String     move;
  public String     weaponskill;
  public String     ballisticskill;
  public String     strength;
  public String     toughness;
  public String     wounds;
  public String     initiative;
  public String     attacks;
  public String     gold;
  public String     armor;
  public String     damage;
  
  /** Creates a new instance of Monster */
  public Monster() {
    super();
  }
  
  public Monster(Node node) {
    super(node);
    
    id = XMLUtil.getAttribute(node, "id");
    name = XMLUtil.getAttribute(node, "name");
    plural = XMLUtil.getAttribute(node, "plural");
    move = XMLUtil.getChildValue(node, "move");
    weaponskill = XMLUtil.getChildValue(node, "weaponskill");
    ballisticskill = XMLUtil.getChildValue(node, "ballisticskill");
    strength = XMLUtil.getChildValue(node, "strength");
    toughness = XMLUtil.getChildValue(node, "toughness");
    wounds = XMLUtil.getChildValue(node, "wounds");
    initiative = XMLUtil.getChildValue(node, "initiative");
    attacks = XMLUtil.getChildValue(node, "attacks");
    gold = XMLUtil.getChildValue(node, "gold");
    armor = XMLUtil.getChildValue(node, "armor");
    damage = XMLUtil.getChildValue(node, "damage");
  }  
}
