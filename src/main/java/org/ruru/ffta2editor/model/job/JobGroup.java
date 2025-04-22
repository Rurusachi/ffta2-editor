package org.ruru.ffta2editor.model.job;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class JobGroup {
    public int id;

    public ObjectProperty<JobData> job1 = new SimpleObjectProperty<>();
    public ObjectProperty<JobData> job2 = new SimpleObjectProperty<>();
    public ObjectProperty<JobData> job3 = new SimpleObjectProperty<>();
    public ObjectProperty<JobData> job4 = new SimpleObjectProperty<>();

    public JobGroup(ByteBuffer bytes, int id) {
        this.id = id;

        job1.set(App.jobDataList.get(Byte.toUnsignedInt(bytes.get())));
        job2.set(App.jobDataList.get(Byte.toUnsignedInt(bytes.get())));
        job3.set(App.jobDataList.get(Byte.toUnsignedInt(bytes.get())));
        job4.set(App.jobDataList.get(Byte.toUnsignedInt(bytes.get())));
    }

    public JobGroup(int id) {
        this.id = id;

        job1.set(App.jobDataList.get(0));
        job2.set(App.jobDataList.get(0));
        job3.set(App.jobDataList.get(0));
        job4.set(App.jobDataList.get(0));
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x4).order(ByteOrder.LITTLE_ENDIAN);

        buffer.put((byte)job1.getValue().id);
        buffer.put((byte)job2.getValue().id);
        buffer.put((byte)job3.getValue().id);
        buffer.put((byte)job4.getValue().id);

        return buffer.array();
    }

    @Override
    public String toString() {
        return String.format("%X: (%s, %s, %s, %s)", id, job1.getValue().name.getValue(),
                                                                              job2.getValue().name.getValue(),
                                                                              job3.getValue().name.getValue(), 
                                                                              job4.getValue().name.getValue());
    }
}
