package org.ruru.ffta2editor.model.formation;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FormationHeader {
    public StringProperty lawString = new SimpleStringProperty();

    public ObjectProperty<Byte> law = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> numUnits = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x02 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> partyLimit = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x04 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x05 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x06 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x07 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x08 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x09 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x0a = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x0b = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x0c = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x0d = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x0e = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x0f = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x10 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x11 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x12 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x13 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x14 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x15 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x16 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x17 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x18 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x19 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x1a = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x1b = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x1c = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x1d = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x1e = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x1f = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x20 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x21 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x22 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x23 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x24 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x25 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x26 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x27 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x28 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x29 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x2a = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x2b = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x2c = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x2d = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x2e = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x2f = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x30 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x31 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x32 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x33 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x34 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x35 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x36 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x37 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x38 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x39 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x3a = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x3b = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x3c = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x3d = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x3e = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x3f = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x40 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x41 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x42 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x43 = new SimpleObjectProperty<>();

    public FormationHeader(ByteBuffer bytes) {
        law.set(bytes.get());
        numUnits.set(bytes.get());
        _0x02.set(bytes.get());
        partyLimit.set(bytes.get());
        _0x04.set(bytes.get());
        _0x05.set(bytes.get());
        _0x06.set(bytes.get());
        _0x07.set(bytes.get());
        _0x08.set(bytes.get());
        _0x09.set(bytes.get());
        _0x0a.set(bytes.get());
        _0x0b.set(bytes.get());
        _0x0c.set(bytes.get());
        _0x0d.set(bytes.get());
        _0x0e.set(bytes.get());
        _0x0f.set(bytes.get());
        _0x10.set(bytes.get());
        _0x11.set(bytes.get());
        _0x12.set(bytes.get());
        _0x13.set(bytes.get());
        _0x14.set(bytes.get());
        _0x15.set(bytes.get());
        _0x16.set(bytes.get());
        _0x17.set(bytes.get());
        _0x18.set(bytes.get());
        _0x19.set(bytes.get());
        _0x1a.set(bytes.get());
        _0x1b.set(bytes.get());
        _0x1c.set(bytes.get());
        _0x1d.set(bytes.get());
        _0x1e.set(bytes.get());
        _0x1f.set(bytes.get());
        _0x20.set(bytes.get());
        _0x21.set(bytes.get());
        _0x22.set(bytes.get());
        _0x23.set(bytes.get());
        _0x24.set(bytes.get());
        _0x25.set(bytes.get());
        _0x26.set(bytes.get());
        _0x27.set(bytes.get());
        _0x28.set(bytes.get());
        _0x29.set(bytes.get());
        _0x2a.set(bytes.get());
        _0x2b.set(bytes.get());
        _0x2c.set(bytes.get());
        _0x2d.set(bytes.get());
        _0x2e.set(bytes.get());
        _0x2f.set(bytes.get());
        _0x30.set(bytes.get());
        _0x31.set(bytes.get());
        _0x32.set(bytes.get());
        _0x33.set(bytes.get());
        _0x34.set(bytes.get());
        _0x35.set(bytes.get());
        _0x36.set(bytes.get());
        _0x37.set(bytes.get());
        _0x38.set(bytes.get());
        _0x39.set(bytes.get());
        _0x3a.set(bytes.get());
        _0x3b.set(bytes.get());
        _0x3c.set(bytes.get());
        _0x3d.set(bytes.get());
        _0x3e.set(bytes.get());
        _0x3f.set(bytes.get());
        _0x40.set(bytes.get());
        _0x41.set(bytes.get());
        _0x42.set(bytes.get());
        _0x43.set(bytes.get());

        lawString.bind(new StringBinding() {
            {bind(law);}
            @Override
            protected String computeValue() {
                int index = Short.toUnsignedInt(law.getValue());
                if (index < App.lawNames.size()) {
                    return App.lawNames.get(index).getValue();
                } else {
                    return "";
                }
            }
        });
    }

    public FormationHeader() {
        law.set((byte)0);
        numUnits.set((byte)0);
        _0x02.set((byte)0);
        partyLimit.set((byte)0);
        _0x04.set((byte)0);
        _0x05.set((byte)0);
        _0x06.set((byte)0);
        _0x07.set((byte)0);
        _0x08.set((byte)0);
        _0x09.set((byte)0);
        _0x0a.set((byte)0);
        _0x0b.set((byte)0);
        _0x0c.set((byte)0);
        _0x0d.set((byte)0);
        _0x0e.set((byte)0);
        _0x0f.set((byte)0);
        _0x10.set((byte)0);
        _0x11.set((byte)0);
        _0x12.set((byte)0);
        _0x13.set((byte)0);
        _0x14.set((byte)0);
        _0x15.set((byte)0);
        _0x16.set((byte)0);
        _0x17.set((byte)0);
        _0x18.set((byte)0);
        _0x19.set((byte)0);
        _0x1a.set((byte)0);
        _0x1b.set((byte)0);
        _0x1c.set((byte)0);
        _0x1d.set((byte)0);
        _0x1e.set((byte)0);
        _0x1f.set((byte)0);
        _0x20.set((byte)0);
        _0x21.set((byte)0);
        _0x22.set((byte)0);
        _0x23.set((byte)0);
        _0x24.set((byte)0);
        _0x25.set((byte)0);
        _0x26.set((byte)0);
        _0x27.set((byte)0);
        _0x28.set((byte)0);
        _0x29.set((byte)0);
        _0x2a.set((byte)0);
        _0x2b.set((byte)0);
        _0x2c.set((byte)0);
        _0x2d.set((byte)0);
        _0x2e.set((byte)0);
        _0x2f.set((byte)0);
        _0x30.set((byte)0);
        _0x31.set((byte)0);
        _0x32.set((byte)0);
        _0x33.set((byte)0);
        _0x34.set((byte)0);
        _0x35.set((byte)0);
        _0x36.set((byte)0);
        _0x37.set((byte)0);
        _0x38.set((byte)0);
        _0x39.set((byte)0);
        _0x3a.set((byte)0);
        _0x3b.set((byte)0);
        _0x3c.set((byte)0);
        _0x3d.set((byte)0);
        _0x3e.set((byte)0);
        _0x3f.set((byte)0);
        _0x40.set((byte)0);
        _0x41.set((byte)0);
        _0x42.set((byte)0);
        _0x43.set((byte)0);

        lawString.bind(new StringBinding() {
            {bind(law);}
            @Override
            protected String computeValue() {
                int index = Short.toUnsignedInt(law.getValue());
                if (index < App.lawNames.size()) {
                    return App.lawNames.get(index).getValue();
                } else {
                    return "";
                }
            }
        });
    }
    
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x44).order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(law.getValue());
        buffer.put(numUnits.getValue());
        buffer.put(_0x02.getValue());
        buffer.put(partyLimit.getValue());
        buffer.put(_0x04.getValue());
        buffer.put(_0x05.getValue());
        buffer.put(_0x06.getValue());
        buffer.put(_0x07.getValue());
        buffer.put(_0x08.getValue());
        buffer.put(_0x09.getValue());
        buffer.put(_0x0a.getValue());
        buffer.put(_0x0b.getValue());
        buffer.put(_0x0c.getValue());
        buffer.put(_0x0d.getValue());
        buffer.put(_0x0e.getValue());
        buffer.put(_0x0f.getValue());
        buffer.put(_0x10.getValue());
        buffer.put(_0x11.getValue());
        buffer.put(_0x12.getValue());
        buffer.put(_0x13.getValue());
        buffer.put(_0x14.getValue());
        buffer.put(_0x15.getValue());
        buffer.put(_0x16.getValue());
        buffer.put(_0x17.getValue());
        buffer.put(_0x18.getValue());
        buffer.put(_0x19.getValue());
        buffer.put(_0x1a.getValue());
        buffer.put(_0x1b.getValue());
        buffer.put(_0x1c.getValue());
        buffer.put(_0x1d.getValue());
        buffer.put(_0x1e.getValue());
        buffer.put(_0x1f.getValue());
        buffer.put(_0x20.getValue());
        buffer.put(_0x21.getValue());
        buffer.put(_0x22.getValue());
        buffer.put(_0x23.getValue());
        buffer.put(_0x24.getValue());
        buffer.put(_0x25.getValue());
        buffer.put(_0x26.getValue());
        buffer.put(_0x27.getValue());
        buffer.put(_0x28.getValue());
        buffer.put(_0x29.getValue());
        buffer.put(_0x2a.getValue());
        buffer.put(_0x2b.getValue());
        buffer.put(_0x2c.getValue());
        buffer.put(_0x2d.getValue());
        buffer.put(_0x2e.getValue());
        buffer.put(_0x2f.getValue());
        buffer.put(_0x30.getValue());
        buffer.put(_0x31.getValue());
        buffer.put(_0x32.getValue());
        buffer.put(_0x33.getValue());
        buffer.put(_0x34.getValue());
        buffer.put(_0x35.getValue());
        buffer.put(_0x36.getValue());
        buffer.put(_0x37.getValue());
        buffer.put(_0x38.getValue());
        buffer.put(_0x39.getValue());
        buffer.put(_0x3a.getValue());
        buffer.put(_0x3b.getValue());
        buffer.put(_0x3c.getValue());
        buffer.put(_0x3d.getValue());
        buffer.put(_0x3e.getValue());
        buffer.put(_0x3f.getValue());
        buffer.put(_0x40.getValue());
        buffer.put(_0x41.getValue());
        buffer.put(_0x42.getValue());
        buffer.put(_0x43.getValue());

        return buffer.array();
    }

}
