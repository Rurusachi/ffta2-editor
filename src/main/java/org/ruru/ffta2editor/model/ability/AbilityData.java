package org.ruru.ffta2editor.model.ability;

import javafx.beans.property.StringProperty;

public abstract class AbilityData {
    public StringProperty name;
    public StringProperty description;
    public int id;

    abstract public byte[] toBytes();
}
