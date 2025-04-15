package org.ruru.ffta2editor.model.stringTable;

import java.util.List;
import java.util.stream.IntStream;

public class MessageId {
    
    public short id;
    public String name;

    public MessageId(short id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return name;
    }
    public static String[] messageNames = {
        "Character names",
        "Job names",
        "Ability set names",
        "Ability names",
        "Region names",
        "Location names",
        "Quest names",
        "Clan titles(?) + Auction text",
        "Rumor titles",
        "Miscellaneous menu text",
        "Notice titles",
        "Item names",
        "Status names",
        "Clan names",
        "Law names",
        "Clan privilege names",
        "Auction menu text",
        "Quest related text (for various quest menus?)",
        "Miscellaneous item text (item types, item effects?)",
        "Keyboard 1?",
        "Keyboard 2?",
        "Keyboard 3?",
        "Just A's?",
        "Just A's?",
        "Bazaar text (Category titles, menu text)",
        "Doesn't exist?",
        "Doesn't exist?",
        "Doesn't exist?",
        "Doesn't exist?",
        "Doesn't exist?",
        "Ability set descriptions",
        "Ability descriptions",
        "Region descriptions",
        "Location descriptions",
        "Doesn't exist?",
        "Quest descriptions (short)",
        "Doesn't exist?",
        "Battle text",
        "Job descriptions",
        "Item descriptions",
        "Ally death text",
        "Status descriptions",
        "Law descriptions",
        "Clan privilege descriptions",
        "Auction House text",
        "Pub text",
        "Doesn't exist?",
        "Ability help text?",
        "Unit dismiss text + some intro text",
        "New notice text",
        "Shop text",
        "Opportunity text",
        "Various Menu text + job requirements",
        "Bazaar category descriptions",
        "Clan descriptions",
    };

    public static List<String> nameWithIndex = IntStream.range(0, messageNames.length).mapToObj(i -> String.format("%X: %s", i, messageNames[i])).toList();
}
