package org.ruru.ffta2editor.model.item;

import javafx.beans.property.StringProperty;

public abstract class ItemData {
    public StringProperty name;
    public StringProperty description;
    public int id;

    abstract public byte[] toBytes();
}
