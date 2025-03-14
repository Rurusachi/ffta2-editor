package org.ruru.ffta2editor.model.character;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.job.JobData;
import org.ruru.ffta2editor.model.job.JobGender;
import org.ruru.ffta2editor.utility.UnitSprite;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CharacterData {

    public int id;
    public StringProperty nameString = new SimpleStringProperty();

    public ObjectProperty<Byte> dialogueRole = new SimpleObjectProperty<>();
    public ObjectProperty<JobGender> gender = new SimpleObjectProperty<>();
    public ObjectProperty<Short> unitPortrait = new SimpleObjectProperty<>();
    public ObjectProperty<Short> enemyPortrait = new SimpleObjectProperty<>();
    public ObjectProperty<UnitSprite> unitSprite = new SimpleObjectProperty<>();
    public ObjectProperty<UnitSprite> unitAlternateSprite = new SimpleObjectProperty<>();
    public ObjectProperty<UnitSprite> enemySprite = new SimpleObjectProperty<>();
    public ObjectProperty<UnitSprite> enemyAlternateSprite = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> unitPalette = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> enemyPalette = new SimpleObjectProperty<>();
    public ObjectProperty<Short> name = new SimpleObjectProperty<>();
    public ObjectProperty<JobData> defaultJob = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x13 = new SimpleObjectProperty<>();
    public ObjectProperty<Short> _0x14 = new SimpleObjectProperty<>();
    public ObjectProperty<Short> _0x16 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> unitTopSprite = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> enemyTopSprite = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x1a = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x1b = new SimpleObjectProperty<>();

    public CharacterData(ByteBuffer bytes, int id) {
        //if (id < App.jobNames.size()) {
        //    this.name = App.jobNames.get(id);
        //} else {
        //    this.name = new SimpleStringProperty("");
        //}
        this.id = id;

        dialogueRole.set(bytes.get());
        gender.set(JobGender.fromInteger(bytes.get()));

        unitPortrait.set(bytes.getShort());
        enemyPortrait.set(bytes.getShort());

        short spriteIndex = bytes.getShort();
        unitSprite.set(spriteIndex != -1 ? App.unitSprites.get(spriteIndex) : null);

        spriteIndex = bytes.getShort();
        unitAlternateSprite.set(spriteIndex != -1 ? App.unitSprites.get(spriteIndex) : null);

        spriteIndex = bytes.getShort();
        enemySprite.set(spriteIndex != -1 ? App.unitSprites.get(spriteIndex) : null);

        spriteIndex = bytes.getShort();
        enemyAlternateSprite.set(spriteIndex != -1 ? App.unitSprites.get(spriteIndex) : null);

        unitPalette.set(bytes.get());
        enemyPalette.set(bytes.get());

        name.set(bytes.getShort());
        defaultJob.set(App.jobDataList.get(Byte.toUnsignedInt(bytes.get())));
        _0x13.set(bytes.get());
        _0x14.set(bytes.getShort());
        _0x16.set(bytes.getShort());
        unitTopSprite.set(bytes.get());
        enemyTopSprite.set(bytes.get());
        _0x1a.set(bytes.get());
        _0x1b.set(bytes.get());

        nameString.bind(new StringBinding() {
            {bind(name);}
            @Override
            protected String computeValue() {
                int index = Short.toUnsignedInt(name.getValue());
                if (index < App.characterNames.size()) {
                    return App.characterNames.get(index).getValue();
                } else {
                    return "";
                }
            }
        });
    }

    public CharacterData(int id) {
        this.id = id;

        dialogueRole.set((byte)0);
        gender.set(JobGender.fromInteger((byte)0));

        unitPortrait.set((short)0);
        enemyPortrait.set((short)0);

        short spriteIndex = (short)-1;
        unitSprite.set(spriteIndex != -1 ? App.unitSprites.get(spriteIndex) : null);

        spriteIndex = (short)-1;
        unitAlternateSprite.set(spriteIndex != -1 ? App.unitSprites.get(spriteIndex) : null);

        spriteIndex = (short)-1;
        enemySprite.set(spriteIndex != -1 ? App.unitSprites.get(spriteIndex) : null);

        spriteIndex = (short)-1;
        enemyAlternateSprite.set(spriteIndex != -1 ? App.unitSprites.get(spriteIndex) : null);

        unitPalette.set((byte)0);
        enemyPalette.set((byte)0);

        name.set((short)0);
        defaultJob.set(App.jobDataList.get(0));
        _0x13.set((byte)0);
        _0x14.set((short)0);
        _0x16.set((short)0);
        unitTopSprite.set((byte)0);
        enemyTopSprite.set((byte)0);
        _0x1a.set((byte)0);
        _0x1b.set((byte)0);

        nameString.bind(new StringBinding() {
            {bind(name);}
            @Override
            protected String computeValue() {
                return App.characterNames.get(Short.toUnsignedInt(name.getValue())).getValue();
            }
        });
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x1C).order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(dialogueRole.getValue());
        buffer.put(gender.getValue().value);

        buffer.putShort(unitPortrait.getValue());
        buffer.putShort(enemyPortrait.getValue());

        short unitIndex = unitSprite.getValue() == null ? -1 : (short)unitSprite.getValue().unitIndex;
        buffer.putShort(unitIndex);

        unitIndex = unitAlternateSprite.getValue() == null ? -1 : (short)unitAlternateSprite.getValue().unitIndex;
        buffer.putShort(unitIndex);

        unitIndex = enemySprite.getValue() == null ? -1 : (short)enemySprite.getValue().unitIndex;
        buffer.putShort(unitIndex);

        unitIndex = enemyAlternateSprite.getValue() == null ? -1 : (short)enemyAlternateSprite.getValue().unitIndex;
        buffer.putShort(unitIndex);

        buffer.put(unitPalette.getValue());
        buffer.put(enemyPalette.getValue());
        buffer.putShort(name.getValue());
        buffer.put((byte)defaultJob.getValue().id);
        buffer.put(_0x13.getValue());
        buffer.putShort(_0x14.getValue());
        buffer.putShort(_0x16.getValue());
        buffer.put(unitTopSprite.getValue());
        buffer.put(enemyTopSprite.getValue());
        buffer.put(_0x1a.getValue());
        buffer.put(_0x1b.getValue());

        return buffer.array();
    }
}
