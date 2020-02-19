/*
 * EventDeckApp.java
 *
 * Created on April 19, 2005, 10:32 AM
 */

package pms.whq;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.SAXException;

import org.w3c.dom.*;

import pms.whq.data.Event;
import pms.whq.data.Monster;
import pms.whq.ui.*;
import pms.whq.data.MonsterEntry;
import pms.whq.data.Deck;
import pms.whq.data.EventList;
import pms.whq.data.Table;
import pms.whq.data.EventEntry;
import pms.whq.data.Rule;

/**
 *
 * @author psiegel
 */
public class EventDeckApp extends JFrame 
  implements ActionListener, FileFilter, WindowListener
{
  protected DOMParser   mParser;
 
  protected Map         mMonsters;
  protected Map         mEvents;
  protected Map         mTables;
  protected Map         mRules;
  
  protected EventList   mEventList;
  protected List        mCurrentCards;
  
  protected JButton     mDraw;
  protected JMenuItem   mTableSettings;
  protected JMenuItem   mAsDeckSetting;
  protected JMenuItem   mAsTableSetting;
  protected JMenuItem   mSetPartySize;
  protected JMenuItem   mSetEventProbability;
  protected JMenuItem   mHelpAbout;
    
  protected Point		mFirstCardLocation;
  protected Point       mNextCardLocation;
  
  private static final String EVENT_PROBABILITY_MSG = "Please enter the percentage chance of drawing\n" +
                                                      "an event card (as opposed to a monster card).\n" +
                                                      "In the original deck, this value was 37%.";
      
  public static void main(String[] args) {
    EventDeckApp app = new EventDeckApp();
    app.setVisible(true);    
  }
  
  /** Creates a new instance of EventDeckApp */
  public EventDeckApp() {
    super("Warhammer Quest Event Deck");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    try {
      mParser = new DOMParser();
      mParser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", true);
      mParser.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);
    } catch (Exception e) {
      e.printStackTrace();
    }

    mMonsters = new TreeMap();
    mEvents = new TreeMap();
    mTables = new TreeMap();
    mRules = new TreeMap();
    
    mCurrentCards = new ArrayList();

    Settings.load();

    loadRules();
    loadMonsters();
    loadEvents();
    loadTables();

    layoutMainWindow();
    pack();
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Point newLocation = new Point(screenSize.width - getWidth(), 
                                  (screenSize.height - getHeight()) / 2);
    setLocation(newLocation);
    
    resetEventList();
  }
  
  protected void layoutMainWindow() {
    Container content = getContentPane();
    mDraw = new JButton("[Click Here]");
    mDraw.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
    String imgDir = Settings.getSetting(Settings.IMG_DIR);
    java.awt.Image img = Settings.loadImage(imgDir + "eventback.png");
    if (img != null) {
      mDraw.setIcon(new ImageIcon(img));
      mDraw.setText("");
      mDraw.setBorder(new EmptyBorder(0, 0, 0, 0));
    } else {
      setSize(new java.awt.Dimension(Settings.getSettingAsInt(Settings.CARD_WIDTH),
                                     Settings.getSettingAsInt(Settings.CARD_HEIGHT)));
    }

    mDraw.addActionListener(this);
    content.add(mDraw);
    
    JMenuBar menuBar = new JMenuBar();
    
    JMenu optionsMenu = new JMenu("Options");
    
    mTableSettings = optionsMenu.add("Activate Tables");
    mTableSettings.addActionListener(this);
    
    mSetPartySize = optionsMenu.add("Set Party Size");
    mSetPartySize.addActionListener(this);
    
    mSetEventProbability = optionsMenu.add("Set Event Probability");
    mSetEventProbability.addActionListener(this);

    optionsMenu.addSeparator();
    
    ButtonGroup group = new ButtonGroup();
    mAsDeckSetting = new JRadioButtonMenuItem("Simulate Deck");
    group.add(mAsDeckSetting);
    optionsMenu.add(mAsDeckSetting);
    mAsTableSetting = new JRadioButtonMenuItem("Simulate Table");
    group.add(mAsTableSetting);
    mAsDeckSetting.addActionListener(this);
    mAsTableSetting.addActionListener(this);
    
    boolean asDeck = 
      Settings.getSettingAsBool(Settings.SIMULATE_DECK);
    mAsDeckSetting.setSelected(asDeck);
    mAsTableSetting.setSelected(!asDeck);
    mSetEventProbability.setEnabled(!asDeck);
        
    optionsMenu.add(mAsTableSetting);
    
    JMenu helpMenu = new JMenu("Help");
    
    mHelpAbout = helpMenu.add("About");
    mHelpAbout.addActionListener(this);
    
    menuBar.add(optionsMenu);    
    menuBar.add(helpMenu);
    
    setJMenuBar(menuBar);        
    
    addWindowListener(this);
  }
  
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == mDraw) {
      //  Draw a card or do a table lookup
      generateNextEvent();
    } else if (source == mTableSettings) {
      //  Set the table settings
      setTableSettings();
    } else if (source == mSetPartySize) {
      //  Set the party size
      setPartySize();
    } else if (source == mSetEventProbability) {
      //  Set the event probability
      setEventProbability();
    } else if ((source == mAsDeckSetting) || (source == mAsTableSetting)) {
      mSetEventProbability.setEnabled(mAsTableSetting.isSelected());
    } else if (source == mHelpAbout) {
      AboutDialog.showAbout();
    }
  }
  
  protected void generateNextEvent() {
    //  Check that some tables have been enabled
    if (mEventList.size() <  1) {
      int option = JOptionPane.showConfirmDialog(this, 
        "It appears that you have no tables activated.  Would you like to activate some tables now?", "No Active Tables", 
        JOptionPane.YES_NO_OPTION);
      if (option == JOptionPane.YES_OPTION) {
        setTableSettings();
      }
    }
    
    //  If we have events, pull the next one
    if (mEventList.size() > 0) {
      Object entry = mEventList.getEntry();
      if (entry != null) {
        closeAllCards();
        showCard(entry);
      } else {
        System.err.println("Null entry found.");
      }
    }
  }

  protected void setTableSettings() {
    TableSettingsPanel dialog = new TableSettingsPanel(this, mTables);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
    if (dialog.changesMade()) {
      resetEventList();
    }
  }
  
  protected void setPartySize() {
    int size = Settings.getSettingAsInt(Settings.PARTY_SIZE);
    String newSize = JOptionPane.showInputDialog(this, 
      "Please enter the number of Warriors in your adventuring party.", new Integer(size));
    if (newSize != null) {
      try {
        size = Integer.parseInt(newSize);
        Settings.setSetting(Settings.PARTY_SIZE, 
                                     String.valueOf(size));
      } catch (NumberFormatException nfe) {
        String message = "The value [" + newSize +"] does not appear to be valid.  Party size must be a postive numeric value.";
        JOptionPane.showMessageDialog(this, message, "Invalid Entry", JOptionPane.WARNING_MESSAGE);
      }
    }
  }
  
  protected void setEventProbability() {
    int probability = Settings.getSettingAsInt(Settings.EVENT_PROBABILITY);
    String newsize = JOptionPane.showInputDialog(this, EVENT_PROBABILITY_MSG, 
      new Integer(probability));
    if (newsize != null) {
      try {
        probability = Integer.parseInt(newsize);
        if ((probability < 1) || (probability > 100)) {
          JOptionPane.showMessageDialog(this, "Event probability must a number from 1 to 100.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } else {
          Settings.setSetting(Settings.EVENT_PROBABILITY, String.valueOf(probability));
        }
      } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(this, "Event probability must a number from 1 to 100.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
      }
    }
  }
  
  public void windowOpened(WindowEvent e) {}
  public void windowClosed(WindowEvent e) {}
  public void windowIconified(WindowEvent e) {}
  public void windowDeiconified(WindowEvent e) {}
  public void windowActivated(WindowEvent e) {}
  public void windowDeactivated(WindowEvent e) {}

  public void windowClosing(WindowEvent e) {
    if (e.getWindow() == this) {
      persistOptions();
    }
  }

  public boolean accept(File file) {
    if (file.getName().endsWith(".xml")) {
      return true;
    }
    return false;
  }
  
  protected void loadRules() {
    File file = 
      new File(Settings.getSetting(Settings.RULES_DIR));
    File[] files = file.listFiles(this);
    
    for (int i=0;i<files.length;i++) {
      try {
        mParser.parse(files[i].getAbsolutePath());

        Document doc = mParser.getDocument();
        NodeList nodes = doc.getChildNodes();
        for (int j=0;j<nodes.getLength();j++) {
          Node root = nodes.item(j);
          String rootName = root.getNodeName();
          if (rootName == "rules") {
            NodeList rules = root.getChildNodes();
            for (int k=0;k<rules.getLength();k++) {
              Node ruleNode = rules.item(k);
              String nodeName = ruleNode.getNodeName();
              if ((nodeName == "rule") || (nodeName == "magic")) {
                Rule rule = new Rule(ruleNode);
                mRules.put(rule.id, rule);
              }
            }
          }
        }

      } catch (SAXException saxe) {
        saxe.printStackTrace();
      } catch (IOException ioex) {
        ioex.printStackTrace();      
      }  
    }
  }
  
  protected void loadTables() {
    File file = 
      new File(Settings.getSetting(Settings.TABLE_DIR));
    File[] files = file.listFiles(this);
    
    for (int i=0;i<files.length;i++) {
      try {
        mParser.parse(files[i].getAbsolutePath());

        Document doc = mParser.getDocument();
        NodeList nodes = doc.getChildNodes();
        for (int j=0;j<nodes.getLength();j++) {
          Node root = nodes.item(j);
          String rootName = root.getNodeName();
          if (rootName == "tables") {
            NodeList tables = root.getChildNodes();
            for (int k=0;k<tables.getLength();k++) {
              Node tableNode = tables.item(k);
              if (tableNode.getNodeName() == "table") {
                Table table = new Table(tableNode);
                checkEntries(table.getEntries(), table.getName());
                
                String settingName = table.getName() + ".active";
                table.setActive(Settings.getSettingAsBool(settingName));
                
                mTables.put(table.getName(), table);
              }
            }
          }
        }

      } catch (SAXException saxe) {
        saxe.printStackTrace();
      } catch (IOException ioex) {
        ioex.printStackTrace();      
      }  
    }
  }
  
  protected void checkEntries(List entries, String tableName) {
    Iterator i = entries.iterator();
    while (i.hasNext()) {
      Object o = i.next();
      
      if (o instanceof List) {
        checkEntries((List)o, tableName);
      } else if (o instanceof MonsterEntry) {
        if (!mMonsters.containsKey(((MonsterEntry)o).id)) {
          StringBuffer buf = new StringBuffer();
          buf.append("While loading table [").append(tableName);
          buf.append("], the monster with id [").append(((MonsterEntry)o).id);
          buf.append("] could not be found.  This entry will be removed from the table");
          JOptionPane.showConfirmDialog(this, buf.toString(), "Monster Not Found", JOptionPane.WARNING_MESSAGE);

          i.remove();
        }
      } else if (o instanceof EventEntry) {
        if (!mEvents.containsKey(((EventEntry)o).id)) {
          StringBuffer buf = new StringBuffer();
          buf.append("While loading table [").append(tableName);
          buf.append("], the event with id [").append(((EventEntry)o).id);
          buf.append("] could not be found.  This entry will be removed from the table");
          JOptionPane.showConfirmDialog(this, buf.toString(), "Event Not Found", JOptionPane.WARNING_MESSAGE);

          i.remove();
        }
      } else {
        StringBuffer buf = new StringBuffer();
        buf.append("While loading table [").append(tableName);
        buf.append("], an unknown entry type (").append(o.getClass().getName());
        buf.append("was found.  This entry will be removed from the table");
        JOptionPane.showConfirmDialog(this, buf.toString(), "Unknown Entry Type", JOptionPane.WARNING_MESSAGE);

        i.remove();
      }
    }
  }
  
  protected void loadMonsters() {
    File file = 
      new File(Settings.getSetting(Settings.MONSTER_DIR));
    File[] files = file.listFiles(this);
    
    for (int i=0;i<files.length;i++) {
      try {
        mParser.parse(files[i].getAbsolutePath());

        Document doc = mParser.getDocument();
        NodeList nodes = doc.getChildNodes();
        for (int j=0;j<nodes.getLength();j++) {
          Node root = nodes.item(j);
          String rootName = root.getNodeName();
          if (rootName == "monsters") {
            NodeList monsters = root.getChildNodes();
            for (int k=0;k<monsters.getLength();k++) {
              Node monsterNode = monsters.item(k);
              if (monsterNode.getNodeName() == "monster") {
                Monster monster = new Monster(monsterNode);
                mMonsters.put(monster.id, monster);
              }
            }
          }
        }

      } catch (SAXException saxe) {
        saxe.printStackTrace();
      } catch (IOException ioex) {
        ioex.printStackTrace();      
      }    
    }
  }
  
  protected void loadEvents() {
    File file = 
      new File(Settings.getSetting(Settings.EVENT_DIR));
    File[] files = file.listFiles(this);
    
    for (int i=0;i<files.length;i++) {
      try {
        mParser.parse(files[i].getAbsolutePath());

        Document doc = mParser.getDocument();
        NodeList nodes = doc.getChildNodes();
        for (int j=0;j<nodes.getLength();j++) {
          Node root = nodes.item(j);
          String rootName = root.getNodeName();
          if (rootName == "events") {
            NodeList monsters = root.getChildNodes();
            for (int k=0;k<monsters.getLength();k++) {
              Node eventNode = monsters.item(k);
              if (eventNode.getNodeName() == "event") {
                Event event = new Event(eventNode);
                mEvents.put(event.id, event);
              }
            }
          }
        }

      } catch (SAXException saxe) {
        saxe.printStackTrace();
      } catch (IOException ioex) {
        ioex.printStackTrace();      
      }    
    }
  }
  
  protected void resetEventList() {
    mEventList = null;
    boolean asDeck = mAsDeckSetting.isSelected();
    
    if (asDeck) {
      mEventList = new Deck();
    } else {
      mEventList = new Table();
    }
    
    Iterator i = mTables.values().iterator();
    while (i.hasNext()) {
      Table table = (Table)i.next();
      if (table.isActive()) {
        mEventList.addEntries(table.getEntries());
      }
    }
    
    if (asDeck) {
      ((Deck)mEventList).shuffle();
    }
  }
    
  protected void showCard(Object entry) {
    CardPanel cardPanel = null;
    String title = "";
    
    if (entry instanceof List) {
      List list = (List)entry;
      Iterator iterator = list.iterator();
      int i = 0;
      while (iterator.hasNext()) {
        int cardWidth = Settings.getSettingAsInt(Settings.CARD_WIDTH);
        showCard(iterator.next());
      }
    } else if (entry instanceof MonsterEntry) {
      MonsterEntry mEntry = (MonsterEntry)entry;
      Monster monster = (Monster)mMonsters.get(mEntry.id);
      if (monster != null) {
        int partySize = Settings.getSettingAsInt(Settings.PARTY_SIZE);
        int numAppearing = mEntry.getNumber();
        if (numAppearing != 0) {
          numAppearing = Math.max(1, (numAppearing * partySize) / 4);
        }
        title = getMonsterTitle(monster, numAppearing);
                
        cardPanel = new MonsterCardPanel(monster, title, mEntry, 
                                         mEntry.appendSpecials, mRules);
      } else {
        String msg = "Unable to find monster: " + mEntry.id;
        JOptionPane.showMessageDialog(this, msg, "Monster Not Found", 
                                      JOptionPane.WARNING_MESSAGE);
      }
    } else if (entry instanceof EventEntry) {
      EventEntry eEntry = (EventEntry)entry;
      Event e = (Event)mEvents.get(eEntry.id);
      title = e.name;
      cardPanel = new EventCardPanel(e);
    } else {
      String msg;
      if (entry != null) {
        msg = "Unknown entry type: " + entry.getClass().getName();
      } else {
        msg = "Null entry found.";
      }
      JOptionPane.showMessageDialog(this, msg, "Unknown Entry Type", 
                                    JOptionPane.WARNING_MESSAGE);
    }

    if (cardPanel != null) {
      cardPanel.layoutPanel();
      
      JFrame cardFrame = new JFrame(title);
      cardFrame.setContentPane(cardPanel);
      cardFrame.pack();
      cardFrame.setVisible(true);
      
      cardFrame.setLocation(mNextCardLocation);      
      mNextCardLocation.x += cardFrame.getWidth();
      
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      if ((mNextCardLocation.x + cardFrame.getWidth()) > d.width) {
        mNextCardLocation.x = mFirstCardLocation.x;
        mNextCardLocation.y += cardFrame.getHeight();
        
        if ((mNextCardLocation.y + cardFrame.getHeight()) > d.height) {
          mNextCardLocation.x = mFirstCardLocation.x + (int)(cardFrame.getWidth() * 0.1);        
          mNextCardLocation.y = mFirstCardLocation.y + (int)(cardFrame.getHeight() * 0.1);
        }
      }
      
      mCurrentCards.add(cardFrame);
    }
  }  
    
  protected String getMonsterTitle(Monster m, int number) {
    String title = "";    

    if (number > 0) {
      title += number + " ";
      if (number > 1) {
        title += m.plural;
      } else {
        title += m.name;
      }
    } else {
      title = "* " + m.name;
    }
    
    return title;
  }
  
  protected void closeAllCards() {
	mFirstCardLocation = null;
    Iterator i = mCurrentCards.iterator();
    while (i.hasNext()) {
      JFrame card = (JFrame)(i.next());
	  if (mFirstCardLocation == null) {
		  mFirstCardLocation = new Point(card.getLocation());
	  }
      card.dispose();
    }
    
	if (mFirstCardLocation == null) {
		mFirstCardLocation = new Point(0, 0);
	}
	mNextCardLocation = new Point(mFirstCardLocation);
	
    mCurrentCards.clear();
  }
  
  protected void persistOptions() {
    Settings.setSetting(Settings.SIMULATE_DECK, 
      String.valueOf(mAsDeckSetting.isSelected()));
    
    Iterator i = mTables.values().iterator();
    while (i.hasNext()) {
      Table table = (Table)i.next();
      String name = table.getName()+".active";
      String value = String.valueOf(table.isActive());
      Settings.setSetting(name, value);
    }
    
    Settings.save();
  }
}
