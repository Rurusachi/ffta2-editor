package org.ruru.ffta2editor.model.ability;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.Race;
import org.ruru.ffta2editor.model.ability.AbilityEffect.Accuracy;
import org.ruru.ffta2editor.model.ability.AbilityEffect.Effect;
import org.ruru.ffta2editor.model.ability.AbilityEffect.Modifier;
import org.ruru.ffta2editor.model.ability.AbilityEffect.Targets;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;;


public class ActiveAbilityData extends AbilityData {

    

    
    //public byte _0x0;
    //public byte _0x1;
    //public byte element;
    //public byte mpCost;
    //public byte _0x4;
    //public byte _0x5;
    //public byte power;
    //public byte _0x7;
    //public byte requiredWeapon;
    //public byte _0x9;
    //public byte specialRequirement;
    //public byte rangeAOEType;
    //public byte range;
    //public byte radius;
    //public byte heightDifference;
    //private byte properties1;
    //private byte properties2;
    //private byte properties3;
    //private byte properties4;
    //public Race race1;
    //public byte apIndex1;
    //public Race race2;
    //public byte apIndex2;
    //public Race race3;
    //public byte apIndex3;
    //public Race race4;
    //public byte apIndex4;
    //public Race race5;
    //public byte apIndex5;
    //public Race race6;
    //public byte apIndex6;
    //public byte _0x30;
    //public byte _0x31;
    //public short learned_ability;
    //public byte menuRoutine;
    //public String name;

    public AbilityEffect effect1;
    public AbilityEffect effect2;
    public AbilityEffect effect3;
    public AbilityEffect effect4;

    public SimpleObjectProperty<Short> _0x0 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<AbilityElement> abilityElement = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> mpCost = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x4 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x5 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Short> power = new SimpleObjectProperty<>();
    public SimpleObjectProperty<WeaponRequirement> weaponRequirement = new SimpleObjectProperty<>();
    public SimpleObjectProperty<SpecialRequirement> specialRequirement = new SimpleObjectProperty<>();
    public SimpleObjectProperty<RangeAOEType> rangeAOEType = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> range = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> radius = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> heightDifference = new SimpleObjectProperty<>();
    public SimpleObjectProperty<AbilityMenuRoutine> menuRoutine = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Race> race1 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> apIndex1 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Race> race2 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> apIndex2 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Race>  race3 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> apIndex3 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Race>  race4 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> apIndex4 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Race>  race5 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> apIndex5 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Race>  race6 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> apIndex6 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x30 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x31 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Short> learnedAbility = new SimpleObjectProperty<>();

    public class AbilityFlags {
        private BitSet flags;
        private int length;
        public SimpleBooleanProperty masteredByDefault = new SimpleBooleanProperty();
        public SimpleBooleanProperty targetSelf = new SimpleBooleanProperty();
        public SimpleBooleanProperty invertedOnUndead = new SimpleBooleanProperty();
        public SimpleBooleanProperty isMagickal = new SimpleBooleanProperty();
        public SimpleBooleanProperty learnableByBlueMagick = new SimpleBooleanProperty();
        public SimpleBooleanProperty counterable = new SimpleBooleanProperty();
        public SimpleBooleanProperty bypassReactions = new SimpleBooleanProperty();
        public SimpleBooleanProperty reflectable = new SimpleBooleanProperty();

        public SimpleBooleanProperty usableWithMagickFrenzyAndDoublecast = new SimpleBooleanProperty();
        public SimpleBooleanProperty ignoreSilence = new SimpleBooleanProperty();
        public SimpleBooleanProperty usableByAI = new SimpleBooleanProperty();
        public SimpleBooleanProperty activatesBonecrusher_Maybe = new SimpleBooleanProperty();
        public SimpleBooleanProperty magickCounterable = new SimpleBooleanProperty();
        public SimpleBooleanProperty throwsItem = new SimpleBooleanProperty();
        public SimpleBooleanProperty activatesAbsorbMP = new SimpleBooleanProperty();
        public SimpleBooleanProperty caughtByStickyFingers = new SimpleBooleanProperty();

        
        public SimpleBooleanProperty usableWithMix = new SimpleBooleanProperty();
        public SimpleBooleanProperty mirrorItem = new SimpleBooleanProperty();
        public SimpleBooleanProperty antiCounter = new SimpleBooleanProperty();
        public SimpleBooleanProperty canTargetEmptySquare = new SimpleBooleanProperty();
        public SimpleBooleanProperty spawnsObject = new SimpleBooleanProperty();
        public SimpleBooleanProperty attackWithSecondWeapon = new SimpleBooleanProperty();
        public SimpleBooleanProperty isItem = new SimpleBooleanProperty();
        public SimpleBooleanProperty bit_23 = new SimpleBooleanProperty();

        
        public SimpleBooleanProperty AICheckDamage_Maybe = new SimpleBooleanProperty();
        public SimpleBooleanProperty AICheckHealingBuff_Maybe = new SimpleBooleanProperty();
        public SimpleBooleanProperty AICheckDebuff_Maybe = new SimpleBooleanProperty();
        public SimpleBooleanProperty elpe_UnburdenSoul = new SimpleBooleanProperty();
        public SimpleBooleanProperty back_Side_DamageModifier = new SimpleBooleanProperty();
        public SimpleBooleanProperty counterable_Maybe = new SimpleBooleanProperty();
        public SimpleBooleanProperty bit_30 = new SimpleBooleanProperty();
        public SimpleBooleanProperty bit_31 = new SimpleBooleanProperty();

        public AbilityFlags() {
            flags = new BitSet(4*8);
            length = 4;
        }

        public AbilityFlags(byte[] bytes) {
            flags = BitSet.valueOf(bytes);
            length = bytes.length;
            
            // Byte 1
            masteredByDefault.setValue(flags.get(0));
            targetSelf.setValue(flags.get(1));
            invertedOnUndead.setValue(flags.get(2));
            isMagickal.setValue(flags.get(3));
            learnableByBlueMagick.setValue(flags.get(4));
            counterable.setValue(flags.get(5));
            bypassReactions.setValue(flags.get(6));
            reflectable.setValue(flags.get(7));
            usableWithMagickFrenzyAndDoublecast.setValue(flags.get(8 + 0));
            ignoreSilence.setValue(flags.get(8 + 1));
            usableByAI.setValue(flags.get(8 + 2));
            activatesBonecrusher_Maybe.setValue(flags.get(8 + 3));
            magickCounterable.setValue(flags.get(8 + 4));
            throwsItem.setValue(flags.get(8 + 5));
            activatesAbsorbMP.setValue(flags.get(8 + 6));
            caughtByStickyFingers.setValue(flags.get(8 + 7));
            usableWithMix.setValue(flags.get(16 + 0));
            mirrorItem.setValue(flags.get(16 + 1));
            antiCounter.setValue(flags.get(16 + 2));
            canTargetEmptySquare.setValue(flags.get(16 + 3));
            spawnsObject.setValue(flags.get(16 + 4));
            attackWithSecondWeapon.setValue(flags.get(16 + 5));
            isItem.setValue(flags.get(16 + 6));
            bit_23.setValue(flags.get(16 + 7));
            AICheckDamage_Maybe.setValue(flags.get(24 + 0));
            AICheckHealingBuff_Maybe.setValue(flags.get(24 + 1));
            AICheckDebuff_Maybe.setValue(flags.get(24 + 2));
            elpe_UnburdenSoul.setValue(flags.get(24 + 3));
            back_Side_DamageModifier.setValue(flags.get(24 + 4));
            counterable_Maybe.setValue(flags.get(24 + 5));
            bit_30.setValue(flags.get(24 + 6));
            bit_31.setValue(flags.get(24 + 7));
        }

        public byte[] toBytes() {
            flags.set(0, masteredByDefault.getValue());
            flags.set(1, targetSelf.getValue());
            flags.set(2, invertedOnUndead.getValue());
            flags.set(3, isMagickal.getValue());
            flags.set(4, learnableByBlueMagick.getValue());
            flags.set(5, counterable.getValue());
            flags.set(6, bypassReactions.getValue());
            flags.set(7, reflectable.getValue());
            flags.set(8 + 0, usableWithMagickFrenzyAndDoublecast.getValue());
            flags.set(8 + 1, ignoreSilence.getValue());
            flags.set(8 + 2, usableByAI.getValue());
            flags.set(8 + 3, activatesBonecrusher_Maybe.getValue());
            flags.set(8 + 4, magickCounterable.getValue());
            flags.set(8 + 5, throwsItem.getValue());
            flags.set(8 + 6, activatesAbsorbMP.getValue());
            flags.set(8 + 7, caughtByStickyFingers.getValue());
            flags.set(16 + 0, usableWithMix.getValue());
            flags.set(16 + 1, mirrorItem.getValue());
            flags.set(16 + 2, antiCounter.getValue());
            flags.set(16 + 3, canTargetEmptySquare.getValue());
            flags.set(16 + 4, spawnsObject.getValue());
            flags.set(16 + 5, attackWithSecondWeapon.getValue());
            flags.set(16 + 6, isItem.getValue());
            flags.set(16 + 7, bit_23.getValue());
            flags.set(24 + 0, AICheckDamage_Maybe.getValue());
            flags.set(24 + 1, AICheckHealingBuff_Maybe.getValue());
            flags.set(24 + 2, AICheckDebuff_Maybe.getValue());
            flags.set(24 + 3, elpe_UnburdenSoul.getValue());
            flags.set(24 + 4, back_Side_DamageModifier.getValue());
            flags.set(24 + 5, counterable_Maybe.getValue());
            flags.set(24 + 6, bit_30.getValue());
            flags.set(24 + 7, bit_31.getValue());

            ByteBuffer newBytes = ByteBuffer.allocate(length);
            newBytes.put(flags.toByteArray());
            newBytes.put(new byte[newBytes.remaining()]);

            return newBytes.array();
        }
    }

    public AbilityFlags abilityFlags;

    public ActiveAbilityData(ByteBuffer bytes, int id) {
        if (id < App.abilityNames.size()) {
            this.name = App.abilityNames.get(id);
        } else {
            this.name = new SimpleStringProperty("");
        }
        if (id < App.abilityDescriptions.size()) {
            this.description = App.abilityDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:000\\\\end\\");
        }
        this.id = id;
        _0x0.set(bytes.getShort()); 
        abilityElement.set(AbilityElement.fromInteger(bytes.get()));
        mpCost.set(bytes.get());
        _0x4.set(bytes.get());
        _0x5.set(bytes.get());
        power.set(bytes.getShort());
        weaponRequirement.set(WeaponRequirement.fromInteger(bytes.getShort()));
        specialRequirement.set(SpecialRequirement.fromInteger(bytes.get()));
        rangeAOEType.set(RangeAOEType.fromInteger(bytes.get()));
        range.set(bytes.get());
        radius.set(bytes.get());
        heightDifference.set(bytes.get());
        menuRoutine.set(AbilityMenuRoutine.fromInteger(bytes.get()));
        effect1 = new AbilityEffect(Targets.fromInteger(bytes.get()), Effect.fromInteger(bytes.get()), Accuracy.fromInteger(bytes.get()), Modifier.fromInteger(bytes.get()));
        effect2 = new AbilityEffect(Targets.fromInteger(bytes.get()), Effect.fromInteger(bytes.get()), Accuracy.fromInteger(bytes.get()), Modifier.fromInteger(bytes.get()));
        effect3 = new AbilityEffect(Targets.fromInteger(bytes.get()), Effect.fromInteger(bytes.get()), Accuracy.fromInteger(bytes.get()), Modifier.fromInteger(bytes.get()));
        effect4 = new AbilityEffect(Targets.fromInteger(bytes.get()), Effect.fromInteger(bytes.get()), Accuracy.fromInteger(bytes.get()), Modifier.fromInteger(bytes.get()));

        abilityFlags = new AbilityFlags(new byte[]{bytes.get(), bytes.get(), bytes.get(), bytes.get()});
        race1.set(Race.fromInteger(bytes.get()));
        apIndex1.set(bytes.get());
        race2.set(Race.fromInteger(bytes.get()));
        apIndex2.set(bytes.get());
        race3.set(Race.fromInteger(bytes.get()));
        apIndex3.set(bytes.get());
        race4.set(Race.fromInteger(bytes.get()));
        apIndex4.set(bytes.get());
        race5.set(Race.fromInteger(bytes.get()));
        apIndex5.set(bytes.get());
        race6.set(Race.fromInteger(bytes.get()));
        apIndex6.set(bytes.get());
        _0x30.set(bytes.get());
        _0x31.set(bytes.get());
        learnedAbility.set(bytes.getShort());

        abilityFlags.learnableByBlueMagick.bind(new BooleanBinding() {
            { bind(learnedAbility); }
            @Override
            protected boolean computeValue() {
                return learnedAbility.getValue() != 0;
            }
            
        });
    }

    public ActiveAbilityData(String name, int id) {
        if (id < App.abilityNames.size()) {
            this.name = App.abilityNames.get(id);
        } else {
            this.name = new SimpleStringProperty(name);
            App.abilityNames.add(this.name);
        }
        if (id < App.abilityDescriptions.size()) {
            this.description = App.abilityDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:000\\\\end\\");
            App.abilityDescriptions.add(this.description);
        }
        this.id = id;
        _0x0.set((short)0); 
        abilityElement.set(AbilityElement.fromInteger((byte)0));
        mpCost.set((byte)0);
        _0x4.set((byte)0);
        _0x5.set((byte)0);
        power.set((short)0);
        weaponRequirement.set(WeaponRequirement.fromInteger((short)0));
        specialRequirement.set(SpecialRequirement.fromInteger((byte)0));
        rangeAOEType.set(RangeAOEType.fromInteger((byte)0));
        range.set((byte)0);
        radius.set((byte)0);
        heightDifference.set((byte)0);
        menuRoutine.set(AbilityMenuRoutine.fromInteger((byte)0));
        effect1 = new AbilityEffect(Targets.fromInteger((byte)0), Effect.fromInteger((byte)0), Accuracy.fromInteger((byte)0), Modifier.fromInteger((byte)0));
        effect2 = new AbilityEffect(Targets.fromInteger((byte)0), Effect.fromInteger((byte)0), Accuracy.fromInteger((byte)0), Modifier.fromInteger((byte)0));
        effect3 = new AbilityEffect(Targets.fromInteger((byte)0), Effect.fromInteger((byte)0), Accuracy.fromInteger((byte)0), Modifier.fromInteger((byte)0));
        effect4 = new AbilityEffect(Targets.fromInteger((byte)0), Effect.fromInteger((byte)0), Accuracy.fromInteger((byte)0), Modifier.fromInteger((byte)0));

        abilityFlags = new AbilityFlags(new byte[]{(byte)0, (byte)0, (byte)0, (byte)0});
        race1.set(Race.fromInteger((byte)0));
        apIndex1.set((byte)0);
        race2.set(Race.fromInteger((byte)0));
        apIndex2.set((byte)0);
        race3.set(Race.fromInteger((byte)0));
        apIndex3.set((byte)0);
        race4.set(Race.fromInteger((byte)0));
        apIndex4.set((byte)0);
        race5.set(Race.fromInteger((byte)0));
        apIndex5.set((byte)0);
        race6.set(Race.fromInteger((byte)0));
        apIndex6.set((byte)0);
        _0x30.set((byte)0);
        _0x31.set((byte)0);
        learnedAbility.set((short)0);

        abilityFlags.learnableByBlueMagick.bind(new BooleanBinding() {
            { bind(learnedAbility); }
            @Override
            protected boolean computeValue() {
                return learnedAbility.getValue() != 0;
            }
            
        });
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x34).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(_0x0.getValue().byteValue());
        buffer.put(abilityElement.getValue().value);
        buffer.put(mpCost.getValue().byteValue());
        buffer.put(_0x4.getValue().byteValue());
        buffer.put(_0x5.getValue().byteValue());
        buffer.putShort(power.getValue().byteValue());
        buffer.putShort(weaponRequirement.getValue().value);
        buffer.put(specialRequirement.getValue().value);
        buffer.put(rangeAOEType.getValue().value);
        buffer.put(range.getValue().byteValue());
        buffer.put(radius.getValue().byteValue());
        buffer.put(heightDifference.getValue().byteValue());
        buffer.put(menuRoutine.getValue().value);
        buffer.put(effect1.toBytes());
        buffer.put(effect2.toBytes());
        buffer.put(effect3.toBytes());
        buffer.put(effect4.toBytes());
        buffer.put(abilityFlags.toBytes());
        //buffer.put(properties1);
        //buffer.put(properties2);
        //buffer.put(properties3);
        //buffer.put(properties4);
        buffer.put(race1.getValue().value);
        buffer.put(apIndex1.getValue().byteValue());
        buffer.put(race2.getValue().value);
        buffer.put(apIndex2.getValue().byteValue());
        buffer.put(race3.getValue().value);
        buffer.put(apIndex3.getValue().byteValue());
        buffer.put(race4.getValue().value);
        buffer.put(apIndex4.getValue().byteValue());
        buffer.put(race5.getValue().value);
        buffer.put(apIndex5.getValue().byteValue());
        buffer.put(race6.getValue().value);
        buffer.put(apIndex6.getValue().byteValue());
        buffer.put(_0x30.getValue().byteValue());
        buffer.put(_0x31.getValue().byteValue());
        buffer.putShort(learnedAbility.getValue());

        return buffer.array();
    }
}
