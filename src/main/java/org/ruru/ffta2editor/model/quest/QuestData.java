package org.ruru.ffta2editor.model.quest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.ability.AbilityData;
import org.ruru.ffta2editor.model.item.ItemData;
import org.ruru.ffta2editor.model.job.JobData;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QuestData {
    public int id;
    public StringProperty name;
    public StringProperty description;
    
    public StringProperty locationString = new SimpleStringProperty();;

    public ObjectProperty<Byte> type = new SimpleObjectProperty<>(); // 1 = story quest (in pub), 2 = side quest (in pub), 3 = encounter on overworld, 4 = Clan Trial?, 5 = directly after other quest/event?
    public ObjectProperty<Byte> questGroup = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> rank = new SimpleObjectProperty<>();
    // Region
    public ObjectProperty<Short> storyRequirement = new SimpleObjectProperty<>();
    public ObjectProperty<Short> flagRequirement = new SimpleObjectProperty<>();
    public ObjectProperty<Short> unknownRequirementIndex = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> unknownRequirementValue = new SimpleObjectProperty<>();
    public ObjectProperty<FFTA2Month> monthRequirement = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> dayRequirement = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x0d = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> abilityRequirement = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> days = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> cooldownFailure = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> cooldownSuccess = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> fee = new SimpleObjectProperty<>();
    public ObjectProperty<ItemData> requiredItem1 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> requiredItemAmount1 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x17 = new SimpleObjectProperty<>();
    public ObjectProperty<ItemData> requiredItem2 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> requiredItemAmount2 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x1b = new SimpleObjectProperty<>();

    public ObjectProperty<Byte> negotiation = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> aptitude = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> teamwork = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> adaptability = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x20 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> questLocation = new SimpleObjectProperty<>();
    public ObjectProperty<Short> battlefield = new SimpleObjectProperty<>();
    
    public ObjectProperty<Byte> _0x24 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x25 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x26 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x27 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x28 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x29 = new SimpleObjectProperty<>();

    public BooleanProperty dispatch = new SimpleBooleanProperty();

    public ObjectProperty<Byte> _0x2b = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x2c = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x2d = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x2e = new SimpleObjectProperty<>();

    public ObjectProperty<JobData> recommendedDispatch = new SimpleObjectProperty<>();
    
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
    public ObjectProperty<Byte> _0x44 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x45 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x46 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x47 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x48 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x49 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x4a = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x4b = new SimpleObjectProperty<>();

    
    public ObjectProperty<Short> gilReward = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> apReward = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> cpReward = new SimpleObjectProperty<>();

    public ItemReward itemReward1; 
    public ItemReward itemReward2; 
    public ItemReward itemReward3; 
    public ItemReward itemReward4; 

    public ObjectProperty<Short> _0x58 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x5a = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x5b = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x5c = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x5d = new SimpleObjectProperty<>();

    public ObjectProperty<Byte> expReward = new SimpleObjectProperty<>();

    public ObjectProperty<Byte> _0x5f = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x60 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x61 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x62 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x63 = new SimpleObjectProperty<>();

    public class ItemReward {
        public ObjectProperty<ItemData> item = new SimpleObjectProperty<>();
        public ObjectProperty<Byte> amount = new SimpleObjectProperty<>();

        public ItemReward(ItemData item, int amount) {
            this.item.set(item);
            this.amount.set((byte)amount);
        }

        public ItemReward(short data) {
            this.item.set(App.itemList.get(data & 0x3FF));
            this.amount.set((byte)(data >>> 10));
        }

        public void set(short data){
            this.item.set(App.itemList.get(data & 0x3FF));
            this.amount.set((byte)(data >>> 10));
        }

        public short toShort() {
            return (short) (item.getValue().id | (amount.getValue() << 10));
        }
    }

    public class RegionFlags {
        private BitSet flags;
        private int length;
        public SimpleBooleanProperty regionBit0 = new SimpleBooleanProperty(); // Set on most non-trial quests?
        public SimpleBooleanProperty targ = new SimpleBooleanProperty();
        public SimpleBooleanProperty camoa = new SimpleBooleanProperty();
        public SimpleBooleanProperty graszton = new SimpleBooleanProperty();
        public SimpleBooleanProperty moorabella = new SimpleBooleanProperty();
        public SimpleBooleanProperty fluorgis = new SimpleBooleanProperty();
        public SimpleBooleanProperty goug = new SimpleBooleanProperty();
        public SimpleBooleanProperty regionBit7 = new SimpleBooleanProperty(); // Story Quest?

        public RegionFlags() {
            flags = new BitSet(1*8);
            length = 1;
        }

        public RegionFlags(byte[] bytes) {
            flags = BitSet.valueOf(bytes);
            length = bytes.length;
            
            // Byte 1
            regionBit0.setValue(flags.get(0));
            targ.setValue(flags.get(1));
            camoa.setValue(flags.get(2));
            graszton.setValue(flags.get(3));
            moorabella.setValue(flags.get(4));
            fluorgis.setValue(flags.get(5));
            goug.setValue(flags.get(6));
            regionBit7.setValue(flags.get(7));
        }

        public byte[] toBytes() {
            flags.set(0, regionBit0.getValue());
            flags.set(1, targ.getValue());
            flags.set(2, camoa.getValue());
            flags.set(3, graszton.getValue());
            flags.set(4, moorabella.getValue());
            flags.set(5, fluorgis.getValue());
            flags.set(6, goug.getValue());
            flags.set(7, regionBit7.getValue());

            ByteBuffer newBytes = ByteBuffer.allocate(length);
            newBytes.put(flags.toByteArray());
            newBytes.put(new byte[newBytes.remaining()]);

            return newBytes.array();
        }
    }
    public RegionFlags regionFlags;

    public QuestData(ByteBuffer bytes) {
        type.set(bytes.get());
        questGroup.set(bytes.get());
        rank.set(bytes.get());

        regionFlags = new RegionFlags(new byte[]{bytes.get()});

        storyRequirement.set(bytes.getShort());
        flagRequirement.set(bytes.getShort());
        unknownRequirementIndex.set(bytes.getShort());
        unknownRequirementValue.set(bytes.get());
        monthRequirement.set(FFTA2Month.fromInteger(Byte.toUnsignedInt(bytes.get())));
        dayRequirement.set(bytes.get());
        _0x0d.set(bytes.get());
        abilityRequirement.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        days.set(bytes.get());
        cooldownFailure.set(bytes.get());
        cooldownSuccess.set(bytes.get());
        fee.set(bytes.get());
        requiredItem1.set(App.itemList.get(Short.toUnsignedInt(bytes.getShort())));
        requiredItemAmount1.set(bytes.get());
        _0x17.set(bytes.get());
        requiredItem2.set(App.itemList.get(Short.toUnsignedInt(bytes.getShort())));
        requiredItemAmount2.set(bytes.get());
        _0x1b.set(bytes.get());

        negotiation.set(bytes.get());
        aptitude.set(bytes.get());
        teamwork.set(bytes.get());
        adaptability.set(bytes.get());
        _0x20.set(bytes.get());
        questLocation.set(bytes.get());
        battlefield.set(bytes.getShort());

        _0x24.set(bytes.get());
        _0x25.set(bytes.get());
        _0x26.set(bytes.get());
        _0x27.set(bytes.get());
        _0x28.set(bytes.get());
        _0x29.set(bytes.get());

        dispatch.set(bytes.get() == 1);
        
        _0x2b.set(bytes.get());
        _0x2c.set(bytes.get());
        _0x2d.set(bytes.get());
        _0x2e.set(bytes.get());

        recommendedDispatch.set(App.jobDataList.get(Byte.toUnsignedInt(bytes.get())));

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
        _0x44.set(bytes.get());
        _0x45.set(bytes.get());
        _0x46.set(bytes.get());
        _0x47.set(bytes.get());
        _0x48.set(bytes.get());
        _0x49.set(bytes.get());
        _0x4a.set(bytes.get());
        _0x4b.set(bytes.get());

        gilReward.set(bytes.getShort());
        apReward.set(bytes.get());
        cpReward.set(bytes.get());
        
        itemReward1 = new ItemReward(bytes.getShort());
        itemReward2 = new ItemReward(bytes.getShort());
        itemReward3 = new ItemReward(bytes.getShort());
        itemReward4 = new ItemReward(bytes.getShort());

        _0x58.set(bytes.getShort());
        _0x5a.set(bytes.get());
        _0x5b.set(bytes.get());
        _0x5c.set(bytes.get());
        _0x5d.set(bytes.get());

        expReward.set(bytes.get());

        _0x5f.set(bytes.get());
        _0x60.set(bytes.get());
        _0x61.set(bytes.get());
        _0x62.set(bytes.get());
        _0x63.set(bytes.get());
        
        locationString.bind(new StringBinding() {
            {bind(questLocation);}
            @Override
            protected String computeValue() {
                int index = Byte.toUnsignedInt(questLocation.getValue());
                if (index < App.locationNames.size()) {
                    return App.locationNames.get(index).getValue();
                } else {
                    return "";
                }
            }
        });
    }

    public QuestData(String name) {
        type.set((byte)0);
        questGroup.set((byte)0);
        rank.set((byte)0);

        regionFlags = new RegionFlags();

        storyRequirement.set((short)0);
        flagRequirement.set((short)0);
        unknownRequirementIndex.set((short)0);
        unknownRequirementValue.set((byte)0);
        monthRequirement.set(FFTA2Month.NONE);
        dayRequirement.set((byte)0);
        _0x0d.set((byte)0);
        abilityRequirement.set(App.abilityList.get(0));
        days.set((byte)0);
        cooldownFailure.set((byte)0);
        cooldownSuccess.set((byte)0);
        fee.set((byte)0);
        requiredItem1.set(App.itemList.get(0));
        requiredItemAmount1.set((byte)0);
        _0x17.set((byte)0);
        requiredItem2.set(App.itemList.get(0));
        requiredItemAmount2.set((byte)0);
        _0x1b.set((byte)0);

        negotiation.set((byte)0);
        aptitude.set((byte)0);
        teamwork.set((byte)0);
        adaptability.set((byte)0);
        _0x20.set((byte)0);
        questLocation.set((byte)0);
        battlefield.set((short)0);

        _0x24.set((byte)0);
        _0x25.set((byte)0);
        _0x26.set((byte)0);
        _0x27.set((byte)0);
        _0x28.set((byte)0);
        _0x29.set((byte)0);

        dispatch.set((byte)0 == 1);
        
        _0x2b.set((byte)0);
        _0x2c.set((byte)0);
        _0x2d.set((byte)0);
        _0x2e.set((byte)0);

        recommendedDispatch.set(App.jobDataList.get(0));

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
        _0x44.set((byte)0);
        _0x45.set((byte)0);
        _0x46.set((byte)0);
        _0x47.set((byte)0);
        _0x48.set((byte)0);
        _0x49.set((byte)0);
        _0x4a.set((byte)0);
        _0x4b.set((byte)0);

        gilReward.set((short)0);
        apReward.set((byte)0);
        cpReward.set((byte)0);
        
        itemReward1 = new ItemReward(App.itemList.get(0), 0);
        itemReward2 = new ItemReward(App.itemList.get(0), 0);
        itemReward3 = new ItemReward(App.itemList.get(0), 0);
        itemReward4 = new ItemReward(App.itemList.get(0), 0);

        _0x58.set((short)0);
        _0x5a.set((byte)0);
        _0x5b.set((byte)0);
        _0x5c.set((byte)0);
        _0x5d.set((byte)0);

        expReward.set((byte)0);

        _0x5f.set((byte)0);
        _0x60.set((byte)0);
        _0x61.set((byte)0);
        _0x62.set((byte)0);
        _0x63.set((byte)0);
        
        locationString.bind(new StringBinding() {
            {bind(questLocation);}
            @Override
            protected String computeValue() {
                int index = Byte.toUnsignedInt(questLocation.getValue());
                if (index < App.locationNames.size()) {
                    return App.locationNames.get(index).getValue();
                } else {
                    return "";
                }
            }
        });
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x64).order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(type.getValue());
        buffer.put(questGroup.getValue());
        buffer.put(rank.getValue());

        buffer.put(regionFlags.toBytes());

        buffer.putShort(storyRequirement.getValue());
        buffer.putShort(flagRequirement.getValue());
        buffer.putShort(unknownRequirementIndex.getValue());
        buffer.put(unknownRequirementValue.getValue());
        buffer.put(monthRequirement.getValue().value);
        buffer.put(dayRequirement.getValue());
        buffer.put(_0x0d.getValue());
        buffer.putShort((short)abilityRequirement.getValue().id);
        buffer.put(days.getValue());
        buffer.put(cooldownFailure.getValue());
        buffer.put(cooldownSuccess.getValue());
        buffer.put(fee.getValue());
        buffer.putShort((short)requiredItem1.getValue().id);
        buffer.put(requiredItemAmount1.getValue());
        buffer.put(_0x17.getValue());
        buffer.putShort((short)requiredItem2.getValue().id);
        buffer.put(requiredItemAmount2.getValue());
        buffer.put(_0x1b.getValue());

        buffer.put(negotiation.getValue());
        buffer.put(aptitude.getValue());
        buffer.put(teamwork.getValue());
        buffer.put(adaptability.getValue());
        buffer.put(_0x20.getValue());
        buffer.put(questLocation.getValue());
        buffer.putShort(battlefield.getValue());

        buffer.put(_0x24.getValue());
        buffer.put(_0x25.getValue());
        buffer.put(_0x26.getValue());
        buffer.put(_0x27.getValue());
        buffer.put(_0x28.getValue());
        buffer.put(_0x29.getValue());

        buffer.put((byte)(dispatch.getValue() ? 1 : 0));
        
        buffer.put(_0x2b.getValue());
        buffer.put(_0x2c.getValue());
        buffer.put(_0x2d.getValue());
        buffer.put(_0x2e.getValue());

        buffer.put((byte)recommendedDispatch.getValue().id);

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
        buffer.put(_0x44.getValue());
        buffer.put(_0x45.getValue());
        buffer.put(_0x46.getValue());
        buffer.put(_0x47.getValue());
        buffer.put(_0x48.getValue());
        buffer.put(_0x49.getValue());
        buffer.put(_0x4a.getValue());
        buffer.put(_0x4b.getValue());

        buffer.putShort(gilReward.getValue());
        buffer.put(apReward.getValue());
        buffer.put(cpReward.getValue());
        
        buffer.putShort(itemReward1.toShort());
        buffer.putShort(itemReward2.toShort());
        buffer.putShort(itemReward3.toShort());
        buffer.putShort(itemReward4.toShort());

        buffer.putShort(_0x58.getValue());
        buffer.put(_0x5a.getValue());
        buffer.put(_0x5b.getValue());
        buffer.put(_0x5c.getValue());
        buffer.put(_0x5d.getValue());

        buffer.put(expReward.getValue());

        buffer.put(_0x5f.getValue());
        buffer.put(_0x60.getValue());
        buffer.put(_0x61.getValue());
        buffer.put(_0x62.getValue());
        buffer.put(_0x63.getValue());

        return buffer.array();
    }
}
