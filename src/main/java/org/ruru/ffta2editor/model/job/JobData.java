package org.ruru.ffta2editor.model.job;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.PatchesController;
import org.ruru.ffta2editor.TextController.StringWithId;
import org.ruru.ffta2editor.model.Race;
import org.ruru.ffta2editor.model.topSprite.TopSprite;
import org.ruru.ffta2editor.model.unitFace.UnitFace;
import org.ruru.ffta2editor.utility.UnitSprite;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class JobData {
    
    //public String name;
    public StringProperty name;
    public StringProperty description;
    public int id;
    
    public SimpleObjectProperty<UnitFace> unitPortrait = new SimpleObjectProperty<>();
    public SimpleObjectProperty<UnitFace> enemyPortrait = new SimpleObjectProperty<>();
    public SimpleObjectProperty<UnitSprite> unitSprite = new SimpleObjectProperty<>();
    public SimpleObjectProperty<UnitSprite> unitAlternateSprite = new SimpleObjectProperty<>();
    public SimpleObjectProperty<UnitSprite> enemySprite = new SimpleObjectProperty<>();
    public SimpleObjectProperty<UnitSprite> enemyAlternateSprite = new SimpleObjectProperty<>();
    
    public SimpleObjectProperty<Byte> unitPalette = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> enemyPalette = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Race> race = new SimpleObjectProperty<>();
    public SimpleObjectProperty<JobMoveType> moveType = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> movablePlaces = new SimpleObjectProperty<>();

    
    public SimpleObjectProperty<Byte> move = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> jump = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> hpBase = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> hpGrowth = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> mpBase = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> mpGrowth = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> speedBase = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> speedGrowth = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> attackBase = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> attackGrowth = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> defenseBase = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> defenseGrowth = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> magickBase = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> magickGrowth = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> resistanceBase = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> resistanceGrowth = new SimpleObjectProperty<>();
    public SimpleObjectProperty<JobElementalResistance> fireResistance = new SimpleObjectProperty<>();
    public SimpleObjectProperty<JobElementalResistance> airResistance = new SimpleObjectProperty<>();
    public SimpleObjectProperty<JobElementalResistance> earthResistance = new SimpleObjectProperty<>();
    public SimpleObjectProperty<JobElementalResistance> waterResistance = new SimpleObjectProperty<>();
    public SimpleObjectProperty<JobElementalResistance> iceResistance = new SimpleObjectProperty<>();
    public SimpleObjectProperty<JobElementalResistance> electricityResistance = new SimpleObjectProperty<>();
    public SimpleObjectProperty<JobElementalResistance> holyResistance = new SimpleObjectProperty<>();
    public SimpleObjectProperty<JobElementalResistance> darkResistance = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> evasion = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x2a = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x2b = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x2c = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> resilience = new SimpleObjectProperty<>();

    public SimpleObjectProperty<AbilitySet> abilitySet = new SimpleObjectProperty<>();
    
    public SimpleObjectProperty<Byte> unarmedBonus = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> raceSomethingMaybe = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x32 = new SimpleObjectProperty<>(); // Possibly a short along with raceSomethingMaybe?
    public SimpleObjectProperty<JobGender> gender = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x34 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x35 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x36 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x37 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x38 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x39 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x3a = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x3b = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x3c = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x3d = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x3e = new SimpleObjectProperty<>();
    public SimpleObjectProperty<TopSprite> unitTopSprite = new SimpleObjectProperty<>();
    public SimpleObjectProperty<TopSprite> enemyTopSprite = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x41 = new SimpleObjectProperty<>();

    public class PropertyFlags {
        private BitSet flags;
        private int length;
        public SimpleBooleanProperty propertyBit0 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit1 = new SimpleBooleanProperty();
        public SimpleBooleanProperty canChangeJobs = new SimpleBooleanProperty();
        public SimpleBooleanProperty isUndead = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit4 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit5 = new SimpleBooleanProperty();
        public SimpleBooleanProperty canAlwaysUseItems = new SimpleBooleanProperty();
        public SimpleBooleanProperty cannotAttack = new SimpleBooleanProperty();

        public PropertyFlags() {
            flags = new BitSet(1*8);
            length = 1;
        }

        public PropertyFlags(byte[] bytes) {
            flags = BitSet.valueOf(bytes);
            length = bytes.length;
            
            // Byte 1
            propertyBit0.setValue(flags.get(0));
            propertyBit1.setValue(flags.get(1));
            canChangeJobs.setValue(flags.get(2));
            isUndead.setValue(flags.get(3));
            propertyBit4.setValue(flags.get(4));
            propertyBit5.setValue(flags.get(5));
            canAlwaysUseItems.setValue(flags.get(6));
            cannotAttack.setValue(flags.get(7));
        }

        public byte[] toBytes() {
            flags.set(0, propertyBit0.getValue());
            flags.set(1, propertyBit1.getValue());
            flags.set(2, canChangeJobs.getValue());
            flags.set(3, isUndead.getValue());
            flags.set(4, propertyBit4.getValue());
            flags.set(5, propertyBit5.getValue());
            flags.set(6, canAlwaysUseItems.getValue());
            flags.set(7, cannotAttack.getValue());

            ByteBuffer newBytes = ByteBuffer.allocate(length);
            newBytes.put(flags.toByteArray());
            newBytes.put(new byte[newBytes.remaining()]);

            return newBytes.array();
        }
    }

    public PropertyFlags propertyFlags;

    public class EquipFlags {
        private BitSet flags;
        private int length;
        public SimpleBooleanProperty equipBit0 = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipKnife = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipSword = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipBlade = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipSaber = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipKnightsword = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipRapier = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipGreatsword = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipBroadsword = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipKatana = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipSpear = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipRod = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipStaff = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipPole = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipKnuckles = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipBow = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipGreatbow = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipGun = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipInstrument = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipHandCannon = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipGrenade = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipAxe = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipHammer = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipMace = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipCard = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipBook = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipShield = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipHelm = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipHairAccessory = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipHat = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipHeavyArmor = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipLightArmor = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipRobe = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipBoots = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipGloves = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipAccessory = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipBit36 = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipBit37 = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipBit38 = new SimpleBooleanProperty();
        public SimpleBooleanProperty equipBit39 = new SimpleBooleanProperty();

        public EquipFlags() {
            flags = new BitSet(5*8);
            length = 5;
        }

        public EquipFlags(byte[] bytes) {
            flags = BitSet.valueOf(bytes);
            length = bytes.length;
            
            equipBit0.setValue(flags.get(0));
            equipKnife.setValue(flags.get(1));
            equipSword.setValue(flags.get(2));
            equipBlade.setValue(flags.get(3));
            equipSaber.setValue(flags.get(4));
            equipKnightsword.setValue(flags.get(5));
            equipRapier.setValue(flags.get(6));
            equipGreatsword.setValue(flags.get(7));
            equipBroadsword.setValue(flags.get(8 + 0));
            equipKatana.setValue(flags.get(8 + 1));
            equipSpear.setValue(flags.get(8 + 2));
            equipRod.setValue(flags.get(8 + 3));
            equipStaff.setValue(flags.get(8 + 4));
            equipPole.setValue(flags.get(8 + 5));
            equipKnuckles.setValue(flags.get(8 + 6));
            equipBow.setValue(flags.get(8 + 7));
            equipGreatbow.setValue(flags.get(16 + 0));
            equipGun.setValue(flags.get(16 + 1));
            equipInstrument.setValue(flags.get(16 + 2));
            equipHandCannon.setValue(flags.get(16 + 3));
            equipGrenade.setValue(flags.get(16 + 4));
            equipAxe.setValue(flags.get(16 + 5));
            equipHammer.setValue(flags.get(16 + 6));
            equipMace.setValue(flags.get(16 + 7));
            equipCard.setValue(flags.get(24 + 0));
            equipBook.setValue(flags.get(24 + 1));
            equipShield.setValue(flags.get(24 + 2));
            equipHelm.setValue(flags.get(24 + 3));
            equipHairAccessory.setValue(flags.get(24 + 4));
            equipHat.setValue(flags.get(24 + 5));
            equipHeavyArmor.setValue(flags.get(24 + 6));
            equipLightArmor.setValue(flags.get(24 + 7));
            equipRobe.setValue(flags.get(32 + 0));
            equipBoots.setValue(flags.get(32 + 1));
            equipGloves.setValue(flags.get(32 + 2));
            equipAccessory.setValue(flags.get(32 + 3));
            equipBit36.setValue(flags.get(32 + 4));
            equipBit37.setValue(flags.get(32 + 5));
            equipBit38.setValue(flags.get(32 + 6));
            equipBit39.setValue(flags.get(32 + 7));
        }

        public byte[] toBytes() {
            flags.set(0, equipBit0.getValue());
            flags.set(1, equipKnife.getValue());
            flags.set(2, equipSword.getValue());
            flags.set(3, equipBlade.getValue());
            flags.set(4, equipSaber.getValue());
            flags.set(5, equipKnightsword.getValue());
            flags.set(6, equipRapier.getValue());
            flags.set(7, equipGreatsword.getValue());
            flags.set(8 + 0, equipBroadsword.getValue());
            flags.set(8 + 1, equipKatana.getValue());
            flags.set(8 + 2, equipSpear.getValue());
            flags.set(8 + 3, equipRod.getValue());
            flags.set(8 + 4, equipStaff.getValue());
            flags.set(8 + 5, equipPole.getValue());
            flags.set(8 + 6, equipKnuckles.getValue());
            flags.set(8 + 7, equipBow.getValue());
            flags.set(16 + 0, equipGreatbow.getValue());
            flags.set(16 + 1, equipGun.getValue());
            flags.set(16 + 2, equipInstrument.getValue());
            flags.set(16 + 3, equipHandCannon.getValue());
            flags.set(16 + 4, equipGrenade.getValue());
            flags.set(16 + 5, equipAxe.getValue());
            flags.set(16 + 6, equipHammer.getValue());
            flags.set(16 + 7, equipMace.getValue());
            flags.set(24 + 0, equipCard.getValue());
            flags.set(24 + 1, equipBook.getValue());
            flags.set(24 + 2, equipShield.getValue());
            flags.set(24 + 3, equipHelm.getValue());
            flags.set(24 + 4, equipHairAccessory.getValue());
            flags.set(24 + 5, equipHat.getValue());
            flags.set(24 + 6, equipHeavyArmor.getValue());
            flags.set(24 + 7, equipLightArmor.getValue());
            flags.set(32 + 0, equipRobe.getValue());
            flags.set(32 + 1, equipBoots.getValue());
            flags.set(32 + 2, equipGloves.getValue());
            flags.set(32 + 3, equipAccessory.getValue());
            flags.set(32 + 4, equipBit36.getValue());
            flags.set(32 + 5, equipBit37.getValue());
            flags.set(32 + 6, equipBit38.getValue());
            flags.set(32 + 7, equipBit39.getValue());

            ByteBuffer newBytes = ByteBuffer.allocate(length);
            newBytes.put(flags.toByteArray());
            newBytes.put(new byte[newBytes.remaining()]);

            return newBytes.array();
        }
    }

    public EquipFlags equipFlags;

    public JobData(ByteBuffer bytes, int id) {
        if (id < App.jobNames.size()) {
            this.name = App.jobNames.get(id).string();
        } else {
            this.name = new SimpleStringProperty("");
        }
        if (id < App.jobDescriptions.size()) {
            this.description = App.jobDescriptions.get(id).string();
        } else {
            this.description = new SimpleStringProperty("\\var2:00\\\\end\\");
        }
        this.id = id;

        unitPortrait.set(App.unitFaces.get(Short.toUnsignedInt(bytes.getShort())));
        enemyPortrait.set(App.unitFaces.get(Short.toUnsignedInt(bytes.getShort())));

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
        race.set(Race.fromInteger(bytes.get()));
        moveType.set(JobMoveType.fromInteger(bytes.get()));
        movablePlaces.set(bytes.get());

        move.set(bytes.get());
        jump.set(bytes.get());
        hpBase.set(bytes.get());
        hpGrowth.set(bytes.get());
        mpBase.set(bytes.get());
        mpGrowth.set(bytes.get());
        speedBase.set(bytes.get());
        speedGrowth.set(bytes.get());
        attackBase.set(bytes.get());
        attackGrowth.set(bytes.get());
        defenseBase.set(bytes.get());
        defenseGrowth.set(bytes.get());
        magickBase.set(bytes.get());
        magickGrowth.set(bytes.get());
        resistanceBase.set(bytes.get());
        resistanceGrowth.set(bytes.get());
        fireResistance.set(JobElementalResistance.fromInteger(bytes.get()));
        airResistance.set(JobElementalResistance.fromInteger(bytes.get()));
        earthResistance.set(JobElementalResistance.fromInteger(bytes.get()));
        waterResistance.set(JobElementalResistance.fromInteger(bytes.get()));
        iceResistance.set(JobElementalResistance.fromInteger(bytes.get()));
        electricityResistance.set(JobElementalResistance.fromInteger(bytes.get()));
        holyResistance.set(JobElementalResistance.fromInteger(bytes.get()));
        darkResistance.set(JobElementalResistance.fromInteger(bytes.get()));
        evasion.set(bytes.get());
        _0x2a.set(bytes.get());
        _0x2b.set(bytes.get());
        _0x2c.set(bytes.get());
        resilience.set(bytes.get());

        abilitySet.set(App.abilitySetList.get(Short.toUnsignedInt(bytes.getShort())));

        unarmedBonus.set(bytes.get());
        raceSomethingMaybe.set(bytes.get());
        _0x32.set(bytes.get());
        gender.set(JobGender.fromInteger(bytes.get()));
        _0x34.set(bytes.get());
        _0x35.set(bytes.get());
        _0x36.set(bytes.get());
        _0x37.set(bytes.get());
        _0x38.set(bytes.get());
        _0x39.set(bytes.get());
        _0x3a.set(bytes.get());
        _0x3b.set(bytes.get());
        if (PatchesController.patchedExpandedTopSprites.getValue()) {
            unitTopSprite.set(App.topSprites.get(Short.toUnsignedInt(bytes.getShort())));
            enemyTopSprite.set(App.topSprites.get(Short.toUnsignedInt(bytes.getShort())));
            _0x3e.set(bytes.get()); // 0x3e -> 0x40
            
            _0x3c.set((byte)0);
            _0x3d.set((byte)0);
        } else {
            _0x3c.set(bytes.get());
            _0x3d.set(bytes.get());
            _0x3e.set(bytes.get());
            unitTopSprite.set(App.topSprites.get(Byte.toUnsignedInt(bytes.get())));
            enemyTopSprite.set(App.topSprites.get(Byte.toUnsignedInt(bytes.get())));
        }
        _0x41.set(bytes.get());

        propertyFlags = new PropertyFlags(new byte[]{bytes.get()});
        equipFlags = new EquipFlags(new byte[]{bytes.get(), bytes.get(), bytes.get(), bytes.get(), bytes.get()});
    }

    public JobData(String name, int id) {
        if (id < App.jobNames.size()) {
            this.name = App.jobNames.get(id).string();
        } else {
            this.name = new SimpleStringProperty(name);
            App.jobNames.add(new StringWithId(id, this.name));
        }
        if (id < App.jobDescriptions.size()) {
            this.description = App.jobDescriptions.get(id).string();
        } else {
            this.description = new SimpleStringProperty("\\var2:00\\\\end\\");
            App.jobDescriptions.add(new StringWithId(id, this.description));
        }
        this.id = id;

        unitPortrait.set(App.unitFaces.get(0));
        enemyPortrait.set(App.unitFaces.get(0));

        unitSprite.set(null);

        unitAlternateSprite.set(null);

        enemySprite.set(null);

        enemyAlternateSprite.set(null);

        unitPalette.set((byte)0);
        enemyPalette.set((byte)0);
        race.set(Race.fromInteger((byte)0));
        moveType.set(JobMoveType.fromInteger((byte)0));
        movablePlaces.set((byte)0);

        move.set((byte)0);
        jump.set((byte)0);
        hpBase.set((byte)0);
        hpGrowth.set((byte)0);
        mpBase.set((byte)0);
        mpGrowth.set((byte)0);
        speedBase.set((byte)0);
        speedGrowth.set((byte)0);
        attackBase.set((byte)0);
        attackGrowth.set((byte)0);
        defenseBase.set((byte)0);
        defenseGrowth.set((byte)0);
        magickBase.set((byte)0);
        magickGrowth.set((byte)0);
        resistanceBase.set((byte)0);
        resistanceGrowth.set((byte)0);
        fireResistance.set(JobElementalResistance.fromInteger((byte)1));
        airResistance.set(JobElementalResistance.fromInteger((byte)1));
        earthResistance.set(JobElementalResistance.fromInteger((byte)1));
        waterResistance.set(JobElementalResistance.fromInteger((byte)1));
        iceResistance.set(JobElementalResistance.fromInteger((byte)1));
        electricityResistance.set(JobElementalResistance.fromInteger((byte)1));
        holyResistance.set(JobElementalResistance.fromInteger((byte)1));
        darkResistance.set(JobElementalResistance.fromInteger((byte)1));
        evasion.set((byte)100);
        _0x2a.set((byte)0);
        _0x2b.set((byte)0);
        _0x2c.set((byte)0);
        resilience.set((byte)60);

        abilitySet.set(App.abilitySetList.get(0));

        unarmedBonus.set((byte)10);
        raceSomethingMaybe.set((byte)0);
        _0x32.set((byte)0);
        gender.set(JobGender.fromInteger((byte)0));
        _0x34.set((byte)0x14);
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
        unitTopSprite.set(App.topSprites.get(0));
        enemyTopSprite.set(App.topSprites.get(0));
        _0x41.set((byte)0);

        propertyFlags = new PropertyFlags();
        equipFlags = new EquipFlags();
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x48).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort((short)unitPortrait.getValue().id);
        buffer.putShort((short)enemyPortrait.getValue().id);

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
        buffer.put(race.getValue().value);
        buffer.put(moveType.getValue().value);
        buffer.put(movablePlaces.getValue());
        buffer.put(move.getValue());
        buffer.put(jump.getValue());
        buffer.put(hpBase.getValue());
        buffer.put(hpGrowth.getValue());
        buffer.put(mpBase.getValue());
        buffer.put(mpGrowth.getValue());
        buffer.put(speedBase.getValue());
        buffer.put(speedGrowth.getValue());
        buffer.put(attackBase.getValue());
        buffer.put(attackGrowth.getValue());
        buffer.put(defenseBase.getValue());
        buffer.put(defenseGrowth.getValue());
        buffer.put(magickBase.getValue());
        buffer.put(magickGrowth.getValue());
        buffer.put(resistanceBase.getValue());
        buffer.put(resistanceGrowth.getValue());
        buffer.put(fireResistance.getValue().value);
        buffer.put(airResistance.getValue().value);
        buffer.put(earthResistance.getValue().value);
        buffer.put(waterResistance.getValue().value);
        buffer.put(iceResistance.getValue().value);
        buffer.put(electricityResistance.getValue().value);
        buffer.put(holyResistance.getValue().value);
        buffer.put(darkResistance.getValue().value);
        buffer.put(evasion.getValue());
        buffer.put(_0x2a.getValue());
        buffer.put(_0x2b.getValue());
        buffer.put(_0x2c.getValue());
        buffer.put(resilience.getValue());
        buffer.putShort((short)abilitySet.getValue().id);
        buffer.put(unarmedBonus.getValue());
        buffer.put(raceSomethingMaybe.getValue());
        buffer.put(_0x32.getValue());
        buffer.put(gender.getValue().value);
        buffer.put(_0x34.getValue());
        buffer.put(_0x35.getValue());
        buffer.put(_0x36.getValue());
        buffer.put(_0x37.getValue());
        buffer.put(_0x38.getValue());
        buffer.put(_0x39.getValue());
        buffer.put(_0x3a.getValue());
        buffer.put(_0x3b.getValue());
        if (PatchesController.patchedExpandedTopSprites.getValue()) {
            buffer.putShort((short)unitTopSprite.getValue().id);
            buffer.putShort((short)enemyTopSprite.getValue().id);
            buffer.put(_0x3e.getValue()); // 0x3e -> 0x40
        } else {
            buffer.put(_0x3c.getValue());
            buffer.put(_0x3d.getValue());
            buffer.put(_0x3e.getValue());
            buffer.put((byte)unitTopSprite.getValue().id);
            buffer.put((byte)enemyTopSprite.getValue().id);
        }
        buffer.put(_0x41.getValue());

        buffer.put(propertyFlags.toBytes());
        buffer.put(equipFlags.toBytes());

        return buffer.array();
    }

    @Override
    public String toString() {
        return String.format("%X: %s", id , name.getValue());
    }
}
