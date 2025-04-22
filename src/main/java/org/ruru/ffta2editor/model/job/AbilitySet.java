package org.ruru.ffta2editor.model.job;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;

import org.ruru.ffta2editor.App;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

public class AbilitySet {
    //public String name;
    public StringProperty name;
    public StringProperty description;
    public int id;
    
    private int firstAbilityIndex;
    private int lastAbilityIndex;

    public class RaceFlags {
        private BitSet flags;
        private int length;
        public SimpleBooleanProperty raceBit_0 = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceHume = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBangaa = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceNuMou = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceViera = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceMoogle = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceSeeq = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceGria = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBit_8 = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBit_9 = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBaknamy = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceSprite = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceLamia = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceWolf = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceDreamhare = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceWerewolf = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceAntlion = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceShelling = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceMalboro = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceTomato = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceCockatrice = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceChocobo = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceFlan = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBomb = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceZombie = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceGhost = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceDeathscythe = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceFloatingEye = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceAhriman = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceTonberry = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceHeadless = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBehemoth = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceMagickPot = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceDrake = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceMimic = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBit_35 = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceYowie = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceRafflesia = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceDemonWall = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceNeukhia = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceUpsilon = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBit_41 = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBit_42 = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBit_43 = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBit_44 = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBit_45 = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBit_46 = new SimpleBooleanProperty();
        public SimpleBooleanProperty raceBit_47 = new SimpleBooleanProperty();

        public RaceFlags() {
            flags = new BitSet(6*8);
            length = 6;
        }

        public RaceFlags(byte[] bytes) {
            flags = BitSet.valueOf(bytes);
            length = bytes.length;
            
            raceBit_0.setValue(flags.get(0 + 0));
            raceHume.setValue(flags.get(0 + 1));
            raceBangaa.setValue(flags.get(0 + 2));
            raceNuMou.setValue(flags.get(0 + 3));
            raceViera.setValue(flags.get(0 + 4));
            raceMoogle.setValue(flags.get(0 + 5));
            raceSeeq.setValue(flags.get(0 + 6));
            raceGria.setValue(flags.get(0 + 7));
            raceBit_8.setValue(flags.get(8 + 0));
            raceBit_9.setValue(flags.get(8 + 1));
            raceBaknamy.setValue(flags.get(8 + 2));
            raceSprite.setValue(flags.get(8 + 3));
            raceLamia.setValue(flags.get(8 + 4));
            raceWolf.setValue(flags.get(8 + 5));
            raceDreamhare.setValue(flags.get(8 + 6));
            raceWerewolf.setValue(flags.get(8 + 7));
            raceAntlion.setValue(flags.get(16 + 0));
            raceShelling.setValue(flags.get(16 + 1));
            raceMalboro.setValue(flags.get(16 + 2));
            raceTomato.setValue(flags.get(16 + 3));
            raceCockatrice.setValue(flags.get(16 + 4));
            raceChocobo.setValue(flags.get(16 + 5));
            raceFlan.setValue(flags.get(16 + 6));
            raceBomb.setValue(flags.get(16 + 7));
            raceZombie.setValue(flags.get(24 + 0));
            raceGhost.setValue(flags.get(24 + 1));
            raceDeathscythe.setValue(flags.get(24 + 2));
            raceFloatingEye.setValue(flags.get(24 + 3));
            raceAhriman.setValue(flags.get(24 + 4));
            raceTonberry.setValue(flags.get(24 + 5));
            raceHeadless.setValue(flags.get(24 + 6));
            raceBehemoth.setValue(flags.get(24 + 7));
            raceMagickPot.setValue(flags.get(32 + 0));
            raceDrake.setValue(flags.get(32 + 1));
            raceMimic.setValue(flags.get(32 + 2));
            raceBit_35.setValue(flags.get(32 + 3));
            raceYowie.setValue(flags.get(32 + 4));
            raceRafflesia.setValue(flags.get(32 + 5));
            raceDemonWall.setValue(flags.get(32 + 6));
            raceNeukhia.setValue(flags.get(32 + 7));
            raceUpsilon.setValue(flags.get(40 + 0));
            raceBit_41.setValue(flags.get(40 + 1));
            raceBit_42.setValue(flags.get(40 + 2));
            raceBit_43.setValue(flags.get(40 + 3));
            raceBit_44.setValue(flags.get(40 + 4));
            raceBit_45.setValue(flags.get(40 + 5));
            raceBit_46.setValue(flags.get(40 + 6));
            raceBit_47.setValue(flags.get(40 + 7));
        }

        public byte[] toBytes() {
            flags.set(0 + 0, raceBit_0.getValue());
            flags.set(0 + 1, raceHume.getValue());
            flags.set(0 + 2, raceBangaa.getValue());
            flags.set(0 + 3, raceNuMou.getValue());
            flags.set(0 + 4, raceViera.getValue());
            flags.set(0 + 5, raceMoogle.getValue());
            flags.set(0 + 6, raceSeeq.getValue());
            flags.set(0 + 7, raceGria.getValue());
            flags.set(8 + 0, raceBit_8.getValue());
            flags.set(8 + 1, raceBit_9.getValue());
            flags.set(8 + 2, raceBaknamy.getValue());
            flags.set(8 + 3, raceSprite.getValue());
            flags.set(8 + 4, raceLamia.getValue());
            flags.set(8 + 5, raceWolf.getValue());
            flags.set(8 + 6, raceDreamhare.getValue());
            flags.set(8 + 7, raceWerewolf.getValue());
            flags.set(16 + 0, raceAntlion.getValue());
            flags.set(16 + 1, raceShelling.getValue());
            flags.set(16 + 2, raceMalboro.getValue());
            flags.set(16 + 3, raceTomato.getValue());
            flags.set(16 + 4, raceCockatrice.getValue());
            flags.set(16 + 5, raceChocobo.getValue());
            flags.set(16 + 6, raceFlan.getValue());
            flags.set(16 + 7, raceBomb.getValue());
            flags.set(24 + 0, raceZombie.getValue());
            flags.set(24 + 1, raceGhost.getValue());
            flags.set(24 + 2, raceDeathscythe.getValue());
            flags.set(24 + 3, raceFloatingEye.getValue());
            flags.set(24 + 4, raceAhriman.getValue());
            flags.set(24 + 5, raceTonberry.getValue());
            flags.set(24 + 6, raceHeadless.getValue());
            flags.set(24 + 7, raceBehemoth.getValue());
            flags.set(32 + 0, raceMagickPot.getValue());
            flags.set(32 + 1, raceDrake.getValue());
            flags.set(32 + 2, raceMimic.getValue());
            flags.set(32 + 3, raceBit_35.getValue());
            flags.set(32 + 4, raceYowie.getValue());
            flags.set(32 + 5, raceRafflesia.getValue());
            flags.set(32 + 6, raceDemonWall.getValue());
            flags.set(32 + 7, raceNeukhia.getValue());
            flags.set(40 + 0, raceUpsilon.getValue());
            flags.set(40 + 1, raceBit_41.getValue());
            flags.set(40 + 2, raceBit_42.getValue());
            flags.set(40 + 3, raceBit_43.getValue());
            flags.set(40 + 4, raceBit_44.getValue());
            flags.set(40 + 5, raceBit_45.getValue());
            flags.set(40 + 6, raceBit_46.getValue());
            flags.set(40 + 7, raceBit_47.getValue());

            ByteBuffer newBytes = ByteBuffer.allocate(length);
            newBytes.put(flags.toByteArray());
            newBytes.put(new byte[newBytes.remaining()]);

            return newBytes.array();
        }
    }

    public RaceFlags raceFlags;

    public SimpleListProperty<AbilitySetAbility> abilities = new SimpleListProperty<>();
    public SimpleObjectProperty<Byte> _0xa = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0xb = new SimpleObjectProperty<>();

    public AbilitySet(ByteBuffer setBytes, ByteBuffer abilityBytes, int id) {
        if (id < App.abilitySetNames.size()) {
            this.name = App.abilitySetNames.get(id);
        } else {
            this.name = new SimpleStringProperty("");
        }
        if (id < App.abilitySetDescriptions.size()) {
            this.description = App.abilitySetDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:00\\\\end\\");
        }
        this.id = id;
        
        firstAbilityIndex = setBytes.getShort();
        lastAbilityIndex = setBytes.getShort();
        raceFlags = new RaceFlags(new byte[]{setBytes.get(), setBytes.get(), setBytes.get(), setBytes.get(), setBytes.get(), setBytes.get()});
        _0xa.setValue(setBytes.get());
        _0xb.setValue(setBytes.get());

        ObservableList<AbilitySetAbility> abilityList = FXCollections.observableArrayList();
        abilityBytes.position(0xc*firstAbilityIndex);
        //if (firstAbilityIndex != lastAbilityIndex) {
        for (int i = 0; i < lastAbilityIndex - firstAbilityIndex + 1; i++) {
            abilityList.add(new AbilitySetAbility(abilityBytes));
        }
        //}
        abilities.set(abilityList);
    }

    public AbilitySet(String name, int id) {
        if (id < App.abilitySetNames.size()) {
            this.name = App.abilitySetNames.get(id);
        } else {
            this.name = new SimpleStringProperty(name);
            App.abilitySetNames.add(this.name);
        }
        if (id < App.abilitySetDescriptions.size()) {
            this.description = App.abilitySetDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:00\\\\end\\");
            App.abilitySetDescriptions.add(this.description);
        }
        this.id = id;
        firstAbilityIndex = 0;
        lastAbilityIndex = 0;
        raceFlags = new RaceFlags();
        _0xa.setValue((byte)0);
        _0xb.setValue((byte)0);
        abilities.set(FXCollections.observableArrayList());
    }

    public Pair<byte[], byte[]> tobytes(int abilityStartIndex) {
        ByteBuffer setBuffer = ByteBuffer.allocate(0xc).order(ByteOrder.LITTLE_ENDIAN);
        
        setBuffer.putShort((short)abilityStartIndex);
        setBuffer.putShort((short)(abilityStartIndex + abilities.size()-1));
        setBuffer.put(raceFlags.toBytes());
        setBuffer.put(_0xa.getValue());
        setBuffer.put(_0xb.getValue());

        ByteBuffer abilityBuffer = ByteBuffer.allocate(0xc*abilities.size()).order(ByteOrder.LITTLE_ENDIAN);
        for (AbilitySetAbility ability : abilities) {
            abilityBuffer.put(ability.toBytes());
        }

        return new Pair<byte[],byte[]>(setBuffer.array(), abilityBuffer.array());
    }

    @Override
    public String toString() {
        return String.format("%X: %s", id , name.getValue());
    }
}
