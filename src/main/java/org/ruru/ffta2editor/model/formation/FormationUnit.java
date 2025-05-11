package org.ruru.ffta2editor.model.formation;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.ability.AbilityData;
import org.ruru.ffta2editor.model.ability.SPAbilityData;
import org.ruru.ffta2editor.model.character.CharacterData;
import org.ruru.ffta2editor.model.item.EquipmentData;
import org.ruru.ffta2editor.model.job.AbilitySet;
import org.ruru.ffta2editor.model.job.JobData;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FormationUnit {
    public StringProperty nameString = new SimpleStringProperty();

    public ObjectProperty<CharacterData> character = new SimpleObjectProperty<>();
    public ObjectProperty<JobData> job = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> minLevel = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> maxLevel = new SimpleObjectProperty<>();
    public ObjectProperty<Short> name = new SimpleObjectProperty<>();

    public ObjectProperty<Byte> _0x06 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x07 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x08 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x09 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> xPosition = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> yPosition = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x0c = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x0d = new SimpleObjectProperty<>();

    public ObjectProperty<AbilityData> primaryAbility1 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> primaryAbility2 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> primaryAbility3 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> primaryAbility4 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> primaryAbility5 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> primaryAbility6 = new SimpleObjectProperty<>();
    
    public ObjectProperty<AbilitySet> secondaryAbilitySet = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> secondaryAbility1 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> secondaryAbility2 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> secondaryAbility3 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilityData> secondaryAbility4 = new SimpleObjectProperty<>();

    public ObjectProperty<SPAbilityData> reactionAbility = new SimpleObjectProperty<>();
    public ObjectProperty<SPAbilityData> passiveAbility = new SimpleObjectProperty<>();
    
    public ObjectProperty<EquipmentData> equipment1 = new SimpleObjectProperty<>();
    public ObjectProperty<EquipmentData> equipment2 = new SimpleObjectProperty<>();
    public ObjectProperty<EquipmentData> equipment3 = new SimpleObjectProperty<>();
    public ObjectProperty<EquipmentData> equipment4 = new SimpleObjectProperty<>();
    public ObjectProperty<EquipmentData> equipment5 = new SimpleObjectProperty<>();

    public ObjectProperty<Byte> _0x32 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x33 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> lootLevel1 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> lootLevel2 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> lootLevel3 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> lootLevel4 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> lootConsumable = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> lootGil = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> faction = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x3b = new SimpleObjectProperty<>();

    public FormationUnit(ByteBuffer bytes) {

        character.set(App.characterList.get(Byte.toUnsignedInt(bytes.get())));
        job.set(App.jobDataList.get(Byte.toUnsignedInt(bytes.get())));
        minLevel.set(bytes.get());
        maxLevel.set(bytes.get());
        name.set(bytes.getShort());

        _0x06.set(bytes.get());
        _0x07.set(bytes.get());
        _0x08.set(bytes.get());
        _0x09.set(bytes.get());
        xPosition.set(bytes.get());
        yPosition.set(bytes.get());
        _0x0c.set(bytes.get());
        _0x0d.set(bytes.get());
    
        primaryAbility1.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        primaryAbility2.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        primaryAbility3.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        primaryAbility4.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        primaryAbility5.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        primaryAbility6.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        
        secondaryAbilitySet.set(App.abilitySetList.get(Short.toUnsignedInt(bytes.getShort())));
        secondaryAbility1.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        secondaryAbility2.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        secondaryAbility3.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
        secondaryAbility4.set(App.abilityList.get(Short.toUnsignedInt(bytes.getShort())));
    
        final int reactionAbilityId = Short.toUnsignedInt(bytes.getShort());
        if (reactionAbilityId != 0){
            reactionAbility.set((SPAbilityData)App.abilityList.get(reactionAbilityId));
        } else {
            reactionAbility.set(App.reactionAbilityList.get(0));
        }
        //reactionAbility.set(App.reactionAbilityList.filtered(x -> x.id == reactionAbilityId).getFirst());
        final int passiveAbilityId = Short.toUnsignedInt(bytes.getShort());
        if (passiveAbilityId != 0){
            passiveAbility.set((SPAbilityData)App.abilityList.get(passiveAbilityId));
        } else {
            passiveAbility.set(App.passiveAbilityList.get(0));
        }
        //passiveAbility.set(App.passiveAbilityList.filtered(x -> x.id == passiveAbilityId).getFirst());

        equipment1.set(App.equipmentList.get(Short.toUnsignedInt(bytes.getShort())));
        equipment2.set(App.equipmentList.get(Short.toUnsignedInt(bytes.getShort())));
        equipment3.set(App.equipmentList.get(Short.toUnsignedInt(bytes.getShort())));
        equipment4.set(App.equipmentList.get(Short.toUnsignedInt(bytes.getShort())));
        equipment5.set(App.equipmentList.get(Short.toUnsignedInt(bytes.getShort())));
    
        _0x32.set(bytes.get());
        _0x33.set(bytes.get());
        lootLevel1.set(bytes.get());
        lootLevel2.set(bytes.get());
        lootLevel3.set(bytes.get());
        lootLevel4.set(bytes.get());
        lootConsumable.set(bytes.get());
        lootGil.set(bytes.get());
        faction.set(bytes.get());
        _0x3b.set(bytes.get());

        nameString.bind(new StringBinding() {
            {bind(name);}
            @Override
            protected String computeValue() {
                int index = Short.toUnsignedInt(name.getValue());
                if (index < App.characterNames.size()) {
                    return App.characterNames.get(index).string().getValue();
                } else {
                    return "";
                }
            }
        });
    }

    public FormationUnit() {

        character.set(App.characterList.get(0));
        job.set(App.jobDataList.get(0));
        minLevel.set((byte)0);
        maxLevel.set((byte)0);
        name.set((short)0);

        _0x06.set((byte)0);
        _0x07.set((byte)0);
        _0x08.set((byte)0);
        _0x09.set((byte)0);
        xPosition.set((byte)0);
        yPosition.set((byte)0);
        _0x0c.set((byte)0);
        _0x0d.set((byte)0);
    
        primaryAbility1.set(App.abilityList.get(0));
        primaryAbility2.set(App.abilityList.get(0));
        primaryAbility3.set(App.abilityList.get(0));
        primaryAbility4.set(App.abilityList.get(0));
        primaryAbility5.set(App.abilityList.get(0));
        primaryAbility6.set(App.abilityList.get(0));
        
        secondaryAbilitySet.set(App.abilitySetList.get(0));
        secondaryAbility1.set(App.abilityList.get(0));
        secondaryAbility2.set(App.abilityList.get(0));
        secondaryAbility3.set(App.abilityList.get(0));
        secondaryAbility4.set(App.abilityList.get(0));
    
        reactionAbility.set(App.reactionAbilityList.get(0));
        passiveAbility.set(App.passiveAbilityList.get(0));

        equipment1.set(App.equipmentList.get(0));
        equipment2.set(App.equipmentList.get(0));
        equipment3.set(App.equipmentList.get(0));
        equipment4.set(App.equipmentList.get(0));
        equipment5.set(App.equipmentList.get(0));
    
        _0x32.set((byte)0);
        _0x33.set((byte)0);
        lootLevel1.set((byte)0);
        lootLevel2.set((byte)0);
        lootLevel3.set((byte)0);
        lootLevel4.set((byte)0);
        lootConsumable.set((byte)0);
        lootGil.set((byte)0);
        faction.set((byte)0);
        _0x3b.set((byte)0);

        nameString.bind(new StringBinding() {
            {bind(name);}
            @Override
            protected String computeValue() {
                int index = Short.toUnsignedInt(name.getValue());
                if (index < App.characterNames.size()) {
                    return App.characterNames.get(index).string().getValue();
                } else {
                    return "";
                }
            }
        });
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x3c).order(ByteOrder.LITTLE_ENDIAN);

        buffer.put((byte)character.getValue().id);
        buffer.put((byte)job.getValue().id);
        buffer.put(minLevel.getValue());
        buffer.put(maxLevel.getValue());
        buffer.putShort(name.getValue());

        buffer.put(_0x06.getValue());
        buffer.put(_0x07.getValue());
        buffer.put(_0x08.getValue());
        buffer.put(_0x09.getValue());
        buffer.put(xPosition.getValue());
        buffer.put(yPosition.getValue());
        buffer.put(_0x0c.getValue());
        buffer.put(_0x0d.getValue());
    
        buffer.putShort((short)primaryAbility1.getValue().id);
        buffer.putShort((short)primaryAbility2.getValue().id);
        buffer.putShort((short)primaryAbility3.getValue().id);
        buffer.putShort((short)primaryAbility4.getValue().id);
        buffer.putShort((short)primaryAbility5.getValue().id);
        buffer.putShort((short)primaryAbility6.getValue().id);
        
        buffer.putShort((short)secondaryAbilitySet.getValue().id);
        buffer.putShort((short)secondaryAbility1.getValue().id);
        buffer.putShort((short)secondaryAbility2.getValue().id);
        buffer.putShort((short)secondaryAbility3.getValue().id);
        buffer.putShort((short)secondaryAbility4.getValue().id);
    
        buffer.putShort((short)reactionAbility.getValue().id);
        buffer.putShort((short)passiveAbility.getValue().id);

        buffer.putShort((short)equipment1.getValue().id);
        buffer.putShort((short)equipment2.getValue().id);
        buffer.putShort((short)equipment3.getValue().id);
        buffer.putShort((short)equipment4.getValue().id);
        buffer.putShort((short)equipment5.getValue().id);
    
        buffer.put(_0x32.getValue());
        buffer.put(_0x33.getValue());
        buffer.put(lootLevel1.getValue());
        buffer.put(lootLevel2.getValue());
        buffer.put(lootLevel3.getValue());
        buffer.put(lootLevel4.getValue());
        buffer.put(lootConsumable.getValue());
        buffer.put(lootGil.getValue());
        buffer.put(faction.getValue());
        buffer.put(_0x3b.getValue());

        return buffer.array();
    }

}
