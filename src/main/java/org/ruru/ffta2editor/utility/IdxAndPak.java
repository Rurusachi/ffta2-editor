package org.ruru.ffta2editor.utility;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javafx.util.Pair;

public class IdxAndPak {
    ByteBuffer Pak;
    ArrayList<ByteBuffer> files = new ArrayList<>();

    public IdxAndPak(ByteBuffer idx, ByteBuffer pak) {
        idx.order(ByteOrder.LITTLE_ENDIAN).rewind();
        pak.order(ByteOrder.LITTLE_ENDIAN).rewind();

        idx.getInt();
        int numEntries = idx.remaining() / 6;

        int a1;
        int ab1;
        int offset;
        int size;
        int a2;
        int ab2;
        for (int i = 0; i < numEntries; i++) {
            a1 = idx.getInt();
            idx.getShort();
            //ab1 = ((a1 << 5) & 0xFFFFFFFF) >> 8;
            ab1 = Integer.divideUnsigned((a1 << 5) & 0xFFFFFFFF, 0x100);
            if ((a1 & 7) != 0) {
                files.add(null);
                continue;
            } 

            idx.mark();
            a2 = idx.getInt();
            idx.reset();
            //ab2 = ((a2 << 5) & 0xFFFFFFFF) >> 8;
            ab2 = Integer.divideUnsigned((a2 << 5) & 0xFFFFFFFF, 0x100);
            size = (ab2-ab1) << 2;
            offset = (ab1) << 2;

            //System.out.println(String.format("offset: %d, size: %d, ab1: %d, ab2: %d, a1: %d, a2: %d", offset, size, ab1, ab2, a1, a2));
            files.add(pak.slice(offset, size).order(ByteOrder.LITTLE_ENDIAN));
        }
    }

    public int numFiles() {
        return files.size();
    }

    public ByteBuffer getFile(int id) {
        return files.get(id);
    }

    public void setFile(int id, ByteBuffer newFile) {
        files.set(id, newFile);
    }

    private int align16(int d) {
        return (d + 0x1f) & ~0x1f;
    }

    public Pair<ByteBuffer, ByteBuffer> repack() {

        ByteBuffer newIdx = ByteBuffer.allocate(1024*1024).order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer newPak = ByteBuffer.allocate(256*1024*1024).order(ByteOrder.LITTLE_ENDIAN);
        byte[] zeroes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        newIdx.putInt(0);
        int offset = 0;
        int c;
        for (int i = 0; i < files.size(); i++) {
        //for (ByteBuffer file : files) {
            //System.out.println(i);
            ByteBuffer file = files.get(i);
            if (file == null) c = 7;
            else c = 0;

            //System.out.println(String.format("i: %d, offset: %8X", i, 2*offset+c));
            newIdx.putInt((2*offset+c) & 0xFFFFFF);
            //newIdx.put(zeroes, 0, 2);
            newIdx.putShort((short)0);

            if (file == null) continue;
            file.rewind();
            int size = file.remaining();
            newPak.put(file);
            
            /* 
            offset += size;
            if (offset % 0x10 != 0) {
                int padding = 0x10 - (offset % 0x10);
                newPak.put(zeroes, 0, padding);
                offset += padding;
            }
            */
            newPak.put(zeroes, 0, align16(offset+size)-newPak.position());
            offset = align16(offset+size);

        }
        newIdx.putInt((2*offset+7) & 0xFFFFFF);
        return new Pair<ByteBuffer,ByteBuffer>(newIdx.limit(newIdx.position()).rewind(), newPak.limit(newPak.position()).rewind());
    }

}
