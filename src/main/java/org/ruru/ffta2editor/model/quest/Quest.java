package org.ruru.ffta2editor.model.quest;

import java.nio.ByteBuffer;

import org.ruru.ffta2editor.App;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;

public class Quest {
    public int id;
    public StringProperty name;
    public StringProperty description;

    public QuestInfo info;
    public QuestData data;

    public Quest(ByteBuffer infoBytes, ByteBuffer dataBytes, int id) {
        if (id < App.questNames.size()) {
            this.name = App.questNames.get(id);
        } else {
            this.name = new SimpleStringProperty("");
        }
        if (id < App.questDescriptions.size()) {
            this.description = App.questDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:000\\\\end\\");
        }
        this.id = id;

        info = new QuestInfo(infoBytes);
        data = new QuestData(dataBytes);
    }

    public Quest(String name, int id) {
        if (id < App.questNames.size()) {
            this.name = App.questNames.get(id);
        } else {
            this.name = new SimpleStringProperty(name);
            App.questNames.add(this.name);
        }
        if (id < App.questDescriptions.size()) {
            this.description = App.questDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:000\\\\end\\");
            App.questDescriptions.add(this.description);
        }
        this.id = id;

        info = new QuestInfo(name);
        data = new QuestData(name);

    }

    public Pair<byte[], byte[]> toBytes() {
        return new Pair<byte[], byte[]>(info.toBytes(), data.toBytes());
    }

}
