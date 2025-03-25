package org.ruru.ffta2editor.model.quest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.formation.FormationData;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

public class QuestInfo {
    public int id;
    public StringProperty name;
    public StringProperty description;

    public ObjectProperty<FormationData> formation = new SimpleObjectProperty<>();
    public ObjectProperty<Short> _0x02 = new SimpleObjectProperty<>();
    public ObjectProperty<Short> startEvent = new SimpleObjectProperty<>();
    public ObjectProperty<Short> endEvent = new SimpleObjectProperty<>();

    public QuestInfo(ByteBuffer bytes) {
        formation.set(App.formationList.get(Short.toUnsignedInt(bytes.getShort())));
        
        _0x02.set(bytes.getShort());
        startEvent.set(bytes.getShort());
        endEvent.set(bytes.getShort());
    }

    public QuestInfo(String name) {
        formation.set(App.formationList.get(0));
        
        _0x02.set((short)0);
        startEvent.set((short)0);
        endEvent.set((short)0);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x8).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort((short)formation.getValue().id);
        buffer.putShort(_0x02.getValue());
        buffer.putShort(startEvent.getValue());
        buffer.putShort(endEvent.getValue());
        
        return buffer.array();
    }

}
