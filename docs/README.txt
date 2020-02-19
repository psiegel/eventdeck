Event Deck 1.0
=-=-=-=-=-=-=-=

Welcome to Event Deck 1.0, a tool for streamlining advanced games of Warhammer
Quest.  This application attempts to replicate the standard event deck in the
Warhammer Quest game in terms of visual appeal and ease of use, while being as
expandable as the system of tables found in the Roleplay book.  The intention is
to minimize wasted time spent hunting through the Roleplay book for tables and
stats once the game has progressed past the first level.

Requirements
============

Event Deck requires the Java Runtime Environment version 5.0 to be installed on
your computer.  The Java Runtime Environment can be obtained at www.java.com.

Usage
=====

To run the Event Deck, simply double-click on eventdeck.jar.

The application should be fairly self-explanatory.  Simply click on the image of
the deck to draw the next event card.  A series of windows will display the
monsters or events.  Clicking the deck again will cause any currently open cards
to close, and the next event will be displayed.

Card display takes full advantage of being in a standard window.  This means
card windows may be resized to allow more content to be displayed.  The special
rules section of a cards will display a scroll bar should the special text not
fit in its allocated space.

Text within the special rules area may contain hyperlinked keywords.  Clicking
on one of these keywords will display a rules window containing the full text of
the special rule.  

Options
=======

The options menu is accessible from the main window displaying the event card
back.  It contains the following options:

Activate Tables
---------------

This option displays the Table Activation window, which will also be displayed 
automatically if you click on the deck without having any tables activated.  The
Table Activation window simply lists all the monster and event tables available
to the application.  Clicking the activate check box for a table will mean that
all contents of that table will be included when picking a random event. 

WARNING - If you select all the tables, the next event will be randomly picked
from one of any active table.  It is more likely that you only want to activate
one or two tables at a time.


Set Party Size
--------------

Warhammer Quest is balanced against a four person party.  What if you have more
or less than four players?  This setting allows you to enter the number of
warriors in your group.  The number of monsters appearing will then be scaled to
that number of warriors (to a minimum of 1).

The number of monsters appearing is multiplied by (party size * 25) %.  This
means if you have a party size of 6, the number of monsters will be multiplied
by 150%.

Simulate Deck / Simulate Table
------------------------------

The application can be set to simulate either a deck of cards or a table at your 
option.  When simulating a table, each time you click on the deck a random entry
will be selected from all active tables.  When simulating a deck, a virtual deck
of cards is created containing one card for each entry of every active table. 
This deck is depleted as you draw cards, until it is emptied at which time the
application reshuffles the deck and starts fresh.  This means when simulating a
deck, no monster or event will be repeated until each one has been seen once.

Note, changing table activations will cause a deck simulation to reset based on
the new tables, effectively acting like a newly shuffled deck. 


Set Event Probability
---------------------

This option is only available when in table simulation mode (see above). 
According to the roleplay book, when using tables the players are told to draw a
card from the event deck first, then roll on either the event table or the
monster table if the card drawn was an event or monster respectively.

If you have multiple tables loading containing both monsters and events, you can 
simulate the above rule by setting this value.  The application first determines 
whether the next event should be a monster or an event, then selects from an 
appropriate table.  The Event Probability is the percentage chance an event will
be chosen rather than a monster.  The default value for this is 37% as the
original event deck was comprised of 19 cards of which 7 were events, which is
roughly 37%.


Customization
=============

The data used by the event deck is highly customizable.  While it includes all
data included with the basic game, you may have your own custom creations you
want to include in your game.

Graphics
--------

When possible, monster cards will display an image of the monster on the card. 
The event deck looks for monster images in the data/graphics/monsters directory.  
Specifically, it looks for a PNG image named the same as the monster's name or
id (see XML below).  These file names should be in all lowercase, with hyphens
(-) instead of spaces.  For example, the card "Giant Bat" will use either an
image named "giant-bat.png" or "rpb-giant-bat.png".  

Image names based on ID take precedence over image names based on monster name. 
This is to allow custom images for monsters found in different monster tables
with the same name.

XML
---

The data/xml directory contains the follow subdirectories:

events    - containing event data
monsters  - containing monster data
rules     - containing text for special rules
tables    - containing table definitions (lists of monsters and events)

The application will load all .xml files found in these directories, assuming 
they are formatted in an expected manner.  Each directory contains a schema
(.xsd) file which contains the rules for formatting the xml files.  Note the
schema files should not be modified, as they are not used directly by the
application, they merely indicate the data format the application expects.  They
are included solely as a tool for xml data creators to check the syntax of their
data.

Creation of new data is left as an exercise for the user.  A basic knowledge of
XML is needed to edit or create new monsters, events, rules, and tables.  Use
the existing files as a template for creating new data.  It is also recommended
that an XML editor which supports XML schema validation be used such that syntax
can be checked before running the application.  I used the jEdit text editor
with the XML plugin when creating the base data.

