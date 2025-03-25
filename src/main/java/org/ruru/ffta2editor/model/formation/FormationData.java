package org.ruru.ffta2editor.model.formation;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

public class FormationData {
    public int id;
    public FormationHeader header;
    public ObservableList<FormationUnit> units;

    public FormationData(ByteBuffer headerBytes, ByteBuffer unitBytes, int id) {
        this.id = id;
        header = new FormationHeader(headerBytes);
        units = FXCollections.observableArrayList();
        for (int i = 0; i < Byte.toUnsignedInt(header.numUnits.getValue()); i++) {
            units.add(new FormationUnit(unitBytes));
        }
    }

    public FormationData(int id) {
        this.id = id;

        header = new FormationHeader();
        units = FXCollections.observableArrayList();
    }

    public Pair<ByteBuffer, ByteBuffer> toBytes() {
        ByteBuffer unitBytes = ByteBuffer.allocate(0x3c*units.size()).order(ByteOrder.LITTLE_ENDIAN);

        for (FormationUnit unit : units) {
            unitBytes.put(unit.toBytes());
        }
        header.numUnits.set((byte)units.size());

        return new Pair<ByteBuffer,ByteBuffer>(ByteBuffer.wrap(header.toBytes()), unitBytes.rewind());
    }
}
