package org.ruru.ffta2editor.model.job;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.character.CharacterData;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class JobRequirementData {
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

    public int id;

    public ObjectProperty<Short> flag = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> indexOrder = new SimpleObjectProperty<>();
    public ObjectProperty<JobData> jobId = new SimpleObjectProperty<>();
    public ObjectProperty<CharacterData> character1 = new SimpleObjectProperty<>();
    public ObjectProperty<CharacterData> character2 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilitySet> job1 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> abilityNum1 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilitySet> job2 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> abilityNum2 = new SimpleObjectProperty<>();
    public ObjectProperty<AbilitySet> job3 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> abilityNum3 = new SimpleObjectProperty<>();

    public JobRequirementData(ByteBuffer bytes, int id) {
        this.id = id;

        flag.set(bytes.getShort());
        indexOrder.set(bytes.get());
        jobId.set(App.jobDataList.get(Byte.toUnsignedInt(bytes.get())));
        character1.set(App.characterList.get(Byte.toUnsignedInt(bytes.get())));
        character2.set(App.characterList.get(Byte.toUnsignedInt(bytes.get())));
        

        int abilitySetIndex = Byte.toUnsignedInt(bytes.get());
        if (abilitySetIndex < App.abilitySetList.size()) {
            job1.set(App.abilitySetList.get(abilitySetIndex));
        } else {
            String warningMessage = String.format("Job Requirement %d: Ability Set %d not found. Defaulting to 0", this.id, abilitySetIndex);
            logger.warning(warningMessage);
            App.loadWarningList.add(warningMessage);
            job1.set(App.abilitySetList.get(0));
        }
        abilityNum1.set(bytes.get());
        
        abilitySetIndex = Byte.toUnsignedInt(bytes.get());
        if (abilitySetIndex < App.abilitySetList.size()) {
            job2.set(App.abilitySetList.get(abilitySetIndex));
        } else {
            String warningMessage = String.format("Job Requirement %d: Ability Set %d not found. Defaulting to 0", this.id, abilitySetIndex);
            logger.warning(warningMessage);
            App.loadWarningList.add(warningMessage);
            job2.set(App.abilitySetList.get(0));
        }
        abilityNum2.set(bytes.get());

        abilitySetIndex = Byte.toUnsignedInt(bytes.get());
        if (abilitySetIndex < App.abilitySetList.size()) {
            job3.set(App.abilitySetList.get(abilitySetIndex));
        } else {
            String warningMessage = String.format("Job Requirement %d: Ability Set %d not found. Defaulting to 0", this.id, abilitySetIndex);
            logger.warning(warningMessage);
            App.loadWarningList.add(warningMessage);
            job3.set(App.abilitySetList.get(0));
        }
        abilityNum3.set(bytes.get());
    }

    public JobRequirementData(int id) {
        this.id = id;
        
        flag.set((short)0);
        indexOrder.set((byte)id);
        jobId.set(App.jobDataList.get(0));
        character1.set(App.characterList.get(0));
        character2.set(App.characterList.get(0));
        job1.set(App.abilitySetList.get(0));
        abilityNum1.set((byte)0);
        job2.set(App.abilitySetList.get(0));
        abilityNum2.set((byte)0);
        job3.set(App.abilitySetList.get(0));
        abilityNum3.set((byte)0);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0xc).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(flag.getValue());
        buffer.put(indexOrder.getValue());
        buffer.put((byte)jobId.getValue().id);
        buffer.put((byte)character1.getValue().id);
        buffer.put((byte)character2.getValue().id);
        buffer.put((byte)job1.getValue().id);
        buffer.put(abilityNum1.getValue());
        buffer.put((byte)job2.getValue().id);
        buffer.put(abilityNum2.getValue());
        buffer.put((byte)job3.getValue().id);
        buffer.put(abilityNum3.getValue());

        return buffer.array();
    }
}
