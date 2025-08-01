package org.ruru.ffta2editor.utility;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.util.Pair;

public class IdxAndPak {
    String name;
    ArrayList<ByteBuffer> files = new ArrayList<>();

    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

    public IdxAndPak(String name, ByteBuffer idx, ByteBuffer pak) {
        this.name = name;
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
                if (i != numEntries-1) {
                    // Skip ending null file
                    files.add(null);
                }
                continue;
            } 

            idx.mark();
            a2 = idx.getInt();
            idx.reset();
            //ab2 = ((a2 << 5) & 0xFFFFFFFF) >> 8;
            ab2 = Integer.divideUnsigned((a2 << 5) & 0xFFFFFFFF, 0x100);
            size = (ab2-ab1) << 2;
            offset = (ab1) << 2;

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

    public void setNumFiles(int newCount) {
        if (files.size() < newCount) {
            while (files.size() < newCount) {
                files.add(null);
            }
        } else {
            while (files.size() > newCount) {
                files.removeLast();
            }
        }
    }

    private int align16(int d) {
        return (d + 0x1f) & ~0x1f;
    }

    public Pair<ByteBuffer, ByteBuffer> repack() {
        // Always end with null file + pad size to multiple of 4
        int newIdxSize = 4 + 6*(files.size()+1) + (files.size() % 2 != 1 ? 2 : 0);
        ByteBuffer newIdx = ByteBuffer.allocate(newIdxSize).order(ByteOrder.LITTLE_ENDIAN);
        int newPakSize = files.stream().filter(f -> f != null).mapToInt(f -> {
            f.rewind();
            return  align16(f.remaining());
        }).sum();
        if (newPakSize % 4 != 0) {
            // Not possible?
            String message = String.format("%s: Pak size not multiple of 4", name);
            logger.info(message);
            System.out.println(message);
            newPakSize += 4 - (newPakSize % 4);
        }
        ByteBuffer newPak = ByteBuffer.allocate(newPakSize).order(ByteOrder.LITTLE_ENDIAN);
        byte[] zeroes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        newIdx.putInt(0);
        int offset = 0;
        int c;
        for (int i = 0; i < files.size(); i++) {
            ByteBuffer file = files.get(i);
            if (file == null) c = 7;
            else c = 0;

            newIdx.putInt((2*offset+c) & 0xFFFFFF);
            newIdx.putShort((short)0);

            if (file == null) continue;
            file.rewind();
            int size = file.remaining();
            newPak.put(file);
            
            newPak.put(zeroes, 0, align16(offset+size)-newPak.position());
            offset = align16(offset+size);
        }
        newIdx.putInt((2*offset+7) & 0xFFFFFF);
        return new Pair<ByteBuffer,ByteBuffer>(newIdx.rewind(), newPak.rewind());
    }

}
