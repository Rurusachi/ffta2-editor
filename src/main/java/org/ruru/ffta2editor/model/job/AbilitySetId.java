package org.ruru.ffta2editor.model.job;

import java.util.List;
import java.util.stream.IntStream;

public class AbilitySetId {

    
    public short id;
    public String name;

    public AbilitySetId(short id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static String[] abilitySetNames = {
        "",
        "Arts of War",
        "Thievery",
        "White Magick",
        "Black Magick",
        "Precision",
        "Chivalry",
        "Pugilism",
        "Flair",
        "Ninjutsu",
        "Illusion",
        "Blue Magick",
        "Trapping",
        "High Magick",
        "Arts of War",
        "Discipline",
        "Dragon Soul",
        "Warding",
        "Sparring",
        "Martial Arts",
        "Intercession",
        "Sacred Blade",
        "Cannonry",
        "Sleight of Hand",
        "Beast Lore",
        "Time Magick",
        "Alchemy",
        "Arcane Magick",
        "Sagacity",
        "Lore",
        "Turning",
        "Fencing",
        "Green Magick",
        "Elemental Magick",
        "Red Magick",
        "Blade Arts",
        "Summoning Magick",
        "Assassination",
        "Sharpshooting",
        "Calling",
        "Onslaught",
        "Gunmanship",
        "Acrobatics",
        "Clockwork",
        "Chococraft",
        "Ballistics",
        "Savagery",
        "Survivalism",
        "Astutia",
        "Brutality",
        "Devastation",
        "Feralism",
        "Geomancy",
        "Taktak",
        "Mischief",
        "Darkness",
        "Maw",
        "Enticement",
        "Call of the Wild",
        "Sands",
        "Shell Crush",
        "Halitosis",
        "Impale",
        "Deadly Nightshade",
        "Territorialism",
        "Chocobo Wiles",
        "Metamorphosis",
        "Volatility",
        "Death's Grasp",
        "Ghostly Touch",
        "Nightmare",
        "Parasite",
        "Enthrallment",
        "Revenge",
        "Brute Force",
        "Bestial Force",
        "???",
        "Breath",
        "Wyrmcraft",
        "Paper-Rock-Scissors",
        "Pom-pom Purée",
        "Ambush",
        "Ambrosia",
        "Elimination",
        "Abyss",
        "Dark Magicks",
        "Items",
        "Gyakuten Item",
        "Scion's Wrath",
        "Piracy",
        "Dance",
        "Song",
        "Instinct",
        "Guile",
        "Reconnaissance",
        "Defense",
        "Temp1",
        "Temp2",
        "Temp3",
        "Temp4",
        "Temp5",
        "Temp6",
        "Temp7",
        "Temp8",
    };

    //public static List<String> nameWithIndex = IntStream.range(0, abilitySetNames.length).mapToObj(i -> String.format("%X: %s", i, abilitySetNames[i])).toList();
}
