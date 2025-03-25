package org.ruru.ffta2editor.model.bazaar;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.item.EquipmentData;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BazaarSet {
    public int id;
    public StringProperty name;
    public StringProperty description;
    
    public static class BazaarSetItem {
        public ObjectProperty<EquipmentData> item = new SimpleObjectProperty<>();
        public ObjectProperty<Byte> grade = new SimpleObjectProperty<>();
        public ObjectProperty<Byte> unused = new SimpleObjectProperty<>();

        public BazaarSetItem(short itemId, byte grade, byte unused) {
            this.item.set(App.equipmentList.get(Short.toUnsignedInt(itemId)));
            this.grade.set(grade);
            this.unused.set(unused);
        }

        public byte[] toBytes() {
            ByteBuffer buffer = ByteBuffer.allocate(0x4).order(ByteOrder.LITTLE_ENDIAN);

            buffer.putShort((short)item.getValue().id);
            buffer.put(grade.getValue());
            buffer.put(unused.getValue());

            return buffer.array();
        }
    }

    public ObjectProperty<Short> storyRequirement = new SimpleObjectProperty<>();
    public ObjectProperty<Short> flagRequirement = new SimpleObjectProperty<>();
    //public ObjectProperty<ItemData> item1 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> grade1 = new SimpleObjectProperty<>();
    //public ObjectProperty<ItemData> item2 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> grade2 = new SimpleObjectProperty<>();
    //public ObjectProperty<ItemData> item3 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> grade3 = new SimpleObjectProperty<>();
    //public ObjectProperty<ItemData> item4 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> grade4 = new SimpleObjectProperty<>();
    //public ObjectProperty<ItemData> item5 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> grade5 = new SimpleObjectProperty<>();
    //public ObjectProperty<ItemData> item6 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> grade6 = new SimpleObjectProperty<>();
    //public ObjectProperty<ItemData> item7 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> grade7 = new SimpleObjectProperty<>();
    //public ObjectProperty<ItemData> item8 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> grade8 = new SimpleObjectProperty<>();
    //public ObjectProperty<ItemData> item9 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> grade9 = new SimpleObjectProperty<>();
    //public ObjectProperty<ItemData> item10 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> grade10 = new SimpleObjectProperty<>();

    public ListProperty<BazaarSetItem> items = new SimpleListProperty<>();

    public BazaarSet(ByteBuffer bytes, int id) {
        if (id < App.bazaarSetNames.size()) {
            this.name = App.bazaarSetNames.get(id);
        } else {
            this.name = new SimpleStringProperty("");
        }
        if (id < App.bazaarSetDescriptions.size()) {
            this.description = App.bazaarSetDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:000\\\\end\\");
        }
        this.id = id;

        storyRequirement.set(bytes.getShort());
        flagRequirement.set(bytes.getShort());

        ObservableList<BazaarSetItem> itemList = FXCollections.observableArrayList();
        for (int i = 0; i < 10; i++) {
            BazaarSetItem item = new BazaarSetItem(bytes.getShort(), bytes.get(), bytes.get());
            itemList.add(item);
        }
        items.set(itemList);
    }

    public BazaarSet(String name, int id) {
        if (id < App.bazaarSetNames.size()) {
            this.name = App.bazaarSetNames.get(id);
        } else {
            this.name = new SimpleStringProperty(name);
            App.bazaarSetNames.add(this.name);
        }
        if (id < App.bazaarSetDescriptions.size()) {
            this.description = App.bazaarSetDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:000\\\\end\\");
            App.bazaarSetDescriptions.add(this.description);
        }
        this.id = id;

        storyRequirement.set((short)0);
        flagRequirement.set((short)0);

        ObservableList<BazaarSetItem> itemList = FXCollections.observableArrayList();
        for (int i = 0; i < 10; i++) {
            BazaarSetItem item = new BazaarSetItem((short)0, (byte)0, (byte)0);
            itemList.add(item);
        }
        items.set(itemList);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x2c).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(storyRequirement.getValue());
        buffer.putShort(flagRequirement.getValue());
        
        for (BazaarSetItem item : items) {
            buffer.put(item.toBytes());
        }

        return buffer.array();
    }
}
