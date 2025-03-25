package org.ruru.ffta2editor.model.item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.ability.AbilityData;
import org.ruru.ffta2editor.model.ability.AbilityElement;
import org.ruru.ffta2editor.model.job.JobGroup;
import org.ruru.ffta2editor.utility.UnitSprite;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class EquipmentData extends ItemData {
    public ObjectProperty<EquipmentType> equipmentType = new SimpleObjectProperty<>(); 
    public ObjectProperty<Short> notName = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityElement> element = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> range = new SimpleObjectProperty<>();
    public ObjectProperty<EquipmentLocation> equipmentLocation = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> palette = new SimpleObjectProperty<>();
    public ObjectProperty<UnitSprite> sprite = new SimpleObjectProperty<>();
    public ObjectProperty<Short> attackEffect = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> hitEffect = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> hitSound = new SimpleObjectProperty<>();
    public ObjectProperty<Short> buy = new SimpleObjectProperty<>();
    public ObjectProperty<Short> sell = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> bonusEffect = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> attack = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> defense = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> magick = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> resistance = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> speed = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> evasion = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> move = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> jump = new SimpleObjectProperty<>();
    public ObjectProperty<JobGroup> jobGroup1 = new SimpleObjectProperty<>();
    public ObjectProperty<JobGroup> jobGroup2 = new SimpleObjectProperty<>();
    public ObjectProperty<JobGroup> jobGroup3 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> ability1 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> ability2 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> ability3 = new SimpleObjectProperty<>();

    public class PropertyFlags {
        private BitSet flags;
        private int length;
        public SimpleBooleanProperty isTwoHanded = new SimpleBooleanProperty();
        public SimpleBooleanProperty isDualWieldable = new SimpleBooleanProperty();
        public SimpleBooleanProperty isMeleeOneHanded = new SimpleBooleanProperty();
        public SimpleBooleanProperty isMeleeTwoHanded = new SimpleBooleanProperty();
        public SimpleBooleanProperty isSellable = new SimpleBooleanProperty();
        public SimpleBooleanProperty isLimitedStock = new SimpleBooleanProperty();
        public SimpleBooleanProperty startsInShop = new SimpleBooleanProperty();
        public SimpleBooleanProperty isBow = new SimpleBooleanProperty();

        public SimpleBooleanProperty isBladed = new SimpleBooleanProperty();
        public SimpleBooleanProperty isPiercing = new SimpleBooleanProperty();
        public SimpleBooleanProperty isBlunt = new SimpleBooleanProperty();
        public SimpleBooleanProperty isRanged = new SimpleBooleanProperty();
        public SimpleBooleanProperty isInstrument_Book = new SimpleBooleanProperty();
        public SimpleBooleanProperty isFemaleOnly = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit14 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit15 = new SimpleBooleanProperty();

        public SimpleBooleanProperty propertyBit16 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit17 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit18 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit19 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit20 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit21 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit22 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit23 = new SimpleBooleanProperty();
        
        public SimpleBooleanProperty propertyBit24 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit25 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit26 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit27 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit28 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit29 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit30 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit31 = new SimpleBooleanProperty();

        public PropertyFlags() {
            flags = new BitSet(4*8);
            length = 4;
        }

        public PropertyFlags(byte[] bytes) {
            flags = BitSet.valueOf(bytes);
            length = bytes.length;
            
            // Byte 1
            isTwoHanded.setValue(flags.get(0 + 0));
            isDualWieldable.setValue(flags.get(0 + 1));
            isMeleeOneHanded.setValue(flags.get(0 + 2));
            isMeleeTwoHanded.setValue(flags.get(0 + 3));
            isSellable.setValue(flags.get(0 + 4));
            isLimitedStock.setValue(flags.get(0 + 5));
            startsInShop.setValue(flags.get(0 + 6));
            isBow.setValue(flags.get(0 + 7));
            isBladed.setValue(flags.get(8 + 0));
            isPiercing.setValue(flags.get(8 + 1));
            isBlunt.setValue(flags.get(8 + 2));
            isRanged.setValue(flags.get(8 + 3));
            isInstrument_Book.setValue(flags.get(8 + 4));
            isFemaleOnly.setValue(flags.get(8 + 5));
            propertyBit14.setValue(flags.get(8 + 6));
            propertyBit15.setValue(flags.get(8 + 7));
            propertyBit16.setValue(flags.get(16 + 0));
            propertyBit17.setValue(flags.get(16 + 1));
            propertyBit18.setValue(flags.get(16 + 2));
            propertyBit19.setValue(flags.get(16 + 3));
            propertyBit20.setValue(flags.get(16 + 4));
            propertyBit21.setValue(flags.get(16 + 5));
            propertyBit22.setValue(flags.get(16 + 6));
            propertyBit23.setValue(flags.get(16 + 7));
            propertyBit24.setValue(flags.get(24 + 0));
            propertyBit25.setValue(flags.get(24 + 1));
            propertyBit26.setValue(flags.get(24 + 2));
            propertyBit27.setValue(flags.get(24 + 3));
            propertyBit28.setValue(flags.get(24 + 4));
            propertyBit29.setValue(flags.get(24 + 5));
            propertyBit30.setValue(flags.get(24 + 6));
            propertyBit31.setValue(flags.get(24 + 7));
        }

        public byte[] toBytes() {
            flags.set(0 + 0, isTwoHanded.getValue());
            flags.set(0 + 1, isDualWieldable.getValue());
            flags.set(0 + 2, isMeleeOneHanded.getValue());
            flags.set(0 + 3, isMeleeTwoHanded.getValue());
            flags.set(0 + 4, isSellable.getValue());
            flags.set(0 + 5, isLimitedStock.getValue());
            flags.set(0 + 6, startsInShop.getValue());
            flags.set(0 + 7, isBow.getValue());
            flags.set(8 + 0, isBladed.getValue());
            flags.set(8 + 1, isPiercing.getValue());
            flags.set(8 + 2, isBlunt.getValue());
            flags.set(8 + 3, isRanged.getValue());
            flags.set(8 + 4, isInstrument_Book.getValue());
            flags.set(8 + 5, isFemaleOnly.getValue());
            flags.set(8 + 6, propertyBit14.getValue());
            flags.set(8 + 7, propertyBit15.getValue());
            flags.set(16 + 0, propertyBit16.getValue());
            flags.set(16 + 1, propertyBit17.getValue());
            flags.set(16 + 2, propertyBit18.getValue());
            flags.set(16 + 3, propertyBit19.getValue());
            flags.set(16 + 4, propertyBit20.getValue());
            flags.set(16 + 5, propertyBit21.getValue());
            flags.set(16 + 6, propertyBit22.getValue());
            flags.set(16 + 7, propertyBit23.getValue());
            flags.set(24 + 0, propertyBit24.getValue());
            flags.set(24 + 1, propertyBit25.getValue());
            flags.set(24 + 2, propertyBit26.getValue());
            flags.set(24 + 3, propertyBit27.getValue());
            flags.set(24 + 4, propertyBit28.getValue());
            flags.set(24 + 5, propertyBit29.getValue());
            flags.set(24 + 6, propertyBit30.getValue());
            flags.set(24 + 7, propertyBit31.getValue());

            ByteBuffer newBytes = ByteBuffer.allocate(length);
            newBytes.put(flags.toByteArray());
            newBytes.put(new byte[newBytes.remaining()]);

            return newBytes.array();
        }
    }
    
    public PropertyFlags propertyFlags;

    public EquipmentData(ByteBuffer bytes, int id) {
        if (id < App.itemNames.size()) {
            this.name = App.itemNames.get(id);
        } else {
            this.name = new SimpleStringProperty("");
        }
        if (id < App.itemDescriptions.size()) {
            this.description = App.itemDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:000\\\\end\\");
        }
        this.id = id;

        equipmentType.set(EquipmentType.fromInteger(bytes.getShort()));
        notName.set(bytes.getShort());
        element.set(AbilityElement.fromInteger(bytes.get()));
        range.set(bytes.get());
        equipmentLocation.set(EquipmentLocation.fromInteger(bytes.get()));
        palette.set(bytes.get());

        short spriteIndex = bytes.getShort();
        sprite.set(spriteIndex != -1 ? App.unitSprites.get(spriteIndex) : null);
        
        attackEffect.set(bytes.getShort());
        hitEffect.set(bytes.get());
        hitSound.set(bytes.get());
        buy.set(bytes.getShort());
        sell.set(bytes.getShort());
        bonusEffect.set(bytes.get());
        attack.set(bytes.get());
        defense.set(bytes.get());
        magick.set(bytes.get());
        resistance.set(bytes.get());
        speed.set(bytes.get());
        evasion.set(bytes.get());
        move.set(bytes.get());
        jump.set(bytes.get());
        jobGroup1.set(App.jobGroupList.get(Byte.toUnsignedInt(bytes.get())));
        jobGroup2.set(App.jobGroupList.get(Byte.toUnsignedInt(bytes.get())));
        jobGroup3.set(App.jobGroupList.get(Byte.toUnsignedInt(bytes.get())));
        ability1.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        ability2.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        ability3.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));

        propertyFlags = new PropertyFlags(new byte[]{bytes.get(), bytes.get(), bytes.get(), bytes.get()});
    }

    public EquipmentData(String name, int id) {
        if (id < App.itemNames.size()) {
            this.name = App.itemNames.get(id);
        } else {
            this.name = new SimpleStringProperty(name);
            App.itemNames.add(this.name);
        }
        if (id < App.itemDescriptions.size()) {
            this.description = App.itemDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:000\\\\end\\");
            App.itemDescriptions.add(this.description);
        }
        this.id = id;

        equipmentType.set(EquipmentType.fromInteger(0));
        notName.set((short)0);
        element.set(AbilityElement.fromInteger(0));
        range.set((byte)0);
        equipmentLocation.set(EquipmentLocation.fromInteger(0));
        palette.set((byte)0);

        sprite.set(null);

        attackEffect.set((short)0);
        hitEffect.set((byte)0);
        hitSound.set((byte)0);
        buy.set((short)0);
        sell.set((short)0);
        bonusEffect.set((byte)0);
        attack.set((byte)0);
        defense.set((byte)0);
        magick.set((byte)0);
        resistance.set((byte)0);
        speed.set((byte)0);
        evasion.set((byte)0);
        move.set((byte)0);
        jump.set((byte)0);
        jobGroup1.set(App.jobGroupList.get(0));
        jobGroup2.set(App.jobGroupList.get(0));
        jobGroup3.set(App.jobGroupList.get(0));
        ability1.set(App.abilityList.get(0));
        ability2.set(App.abilityList.get(0));
        ability3.set(App.abilityList.get(0));

        propertyFlags = new PropertyFlags();
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x28).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(equipmentType.getValue().value);
        buffer.putShort(notName.getValue());
        buffer.put(element.getValue().value);
        buffer.put(range.getValue());
        buffer.put(equipmentLocation.getValue().value);
        buffer.put(palette.getValue());

        short unitIndex = sprite.getValue() == null ? -1 : (short)sprite.getValue().unitIndex;
        buffer.putShort(unitIndex);

        buffer.putShort(attackEffect.getValue());
        buffer.put(hitEffect.getValue());
        buffer.put(hitSound.getValue());
        buffer.putShort(buy.getValue());
        buffer.putShort(sell.getValue());
        buffer.put(bonusEffect.getValue());
        buffer.put(attack.getValue());
        buffer.put(defense.getValue());
        buffer.put(magick.getValue());
        buffer.put(resistance.getValue());
        buffer.put(speed.getValue());
        buffer.put(evasion.getValue());
        buffer.put(move.getValue());
        buffer.put(jump.getValue());
        buffer.put((byte)jobGroup1.getValue().id);
        buffer.put((byte)jobGroup2.getValue().id);
        buffer.put((byte)jobGroup3.getValue().id);
        buffer.putShort((short)ability1.getValue().id);
        buffer.putShort((short)ability2.getValue().id);
        buffer.putShort((short)ability3.getValue().id);

        buffer.put(propertyFlags.toBytes());

        return buffer.array();
    }

}
