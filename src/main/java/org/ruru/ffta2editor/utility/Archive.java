package org.ruru.ffta2editor.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;

import org.ruru.ffta2editor.utility.Archive.ArchiveEntry.ArchiveEntryList;

public class Archive {
    private static class EncodedTableId {
        public int a;
        public int b;

        public EncodedTableId(int a, int b){
            this.a = a;
            this.b = b;
        }

        public DecodedTableId decode() {
            int of = (a & 0x3FFFFFF) * 2;
            int sz = ((int)(Integer.toUnsignedLong(a) / Math.pow(2, 26)) + (b & 0xFFFF)*64)*4;
            return new DecodedTableId(of, sz);
        }
    }
    
    private static class DecodedTableId {
        public int offset;
        public int size;

        public DecodedTableId(int offset, int size){
            this.offset = offset;
            this.size = size;
        }

        public EncodedTableId encode() {
            int a = Integer.divideUnsigned(offset, 2) + ((Integer.divideUnsigned(size, 4) & 0x3F) << 26 );
            int b = 0x360000 + Integer.divideUnsigned(Integer.divideUnsigned(size, 4),64);
            return new EncodedTableId(a, b);
        }
    }

    private static class ExtraData {
        public int offset;
        public int size;
        public byte chr;
        public int chr_position;

        public ExtraData(int offset, int size, byte chr, int chr_position) {
            this.offset = offset;
            this.size = size;
            this.chr = chr;
            this.chr_position = chr_position;
        }
    }

    public static class ArchiveEntry {
        public static class ArchiveEntryList {
            public byte chr;
            public int chr_position;
            public ByteBuffer file;

            public ArchiveEntryList(byte chr, int chr_position, ByteBuffer file) {
                this.chr = chr;
                this.chr_position = chr_position;
                this.file = file;
                file.order(ByteOrder.LITTLE_ENDIAN);
            }

            public ArchiveEntryList copy() {
                return new ArchiveEntryList(chr, chr_position, file);
            }
        }
        public boolean isList;
        public ByteBuffer file;
        public ArrayList<ArchiveEntryList> files;


        public ArchiveEntry(ArrayList<ArchiveEntryList> files) {
            isList = true;
            this.files = files;
        }

        public ArchiveEntry(ByteBuffer file) {
            isList = false;
            this.file = file;
        }

        public ArchiveEntry copy() {
            if (isList) {
                ArrayList<ArchiveEntryList> fileCopies = new ArrayList<>();
                for (ArchiveEntryList entry : files) {
                    fileCopies.add(entry.copy());
                }
                return new ArchiveEntry(fileCopies);
            } else {
                return new ArchiveEntry(file);
            }
        }

    }

    File pcIdxFile;
    File pcBinFile;
    ByteBuffer pcIdxBuffer;
    ByteBuffer pcBinBuffer;
    ArrayList<EncodedTableId> tableByIds = new ArrayList<>();
    ByteBuffer extra;
    int extraStart;

    ArrayList<ArchiveEntry> files = new ArrayList<>();
    
    public Archive(File pcIdxFile, File pcBinFile) throws FileNotFoundException {
        if (!pcIdxFile.exists()) throw new FileNotFoundException("pc.idx not found");
        if (!pcBinFile.exists()) throw new FileNotFoundException("pc.bin not found");
        try {
            
            FileInputStream pcIdx = new FileInputStream(pcIdxFile);
            pcIdxBuffer = ByteBuffer.wrap(pcIdx.readAllBytes());
            pcIdxBuffer.order(ByteOrder.LITTLE_ENDIAN);
            pcIdx.close();

            
            FileInputStream pcBin = new FileInputStream(pcBinFile);
            pcBinBuffer = ByteBuffer.wrap(pcBin.readAllBytes());
            pcBinBuffer.order(ByteOrder.LITTLE_ENDIAN);
            pcBin.close();
        } catch (Exception e) {
            System.err.println(e);
        }

            //ByteBuffer bb = ByteBuffer.wrap(pc_idx.readNBytes(4));
        var num_ids = pcIdxBuffer.getInt();
        pcIdxBuffer.getInt();
        for (int i = 0; i < num_ids; i++) {
            int a = pcIdxBuffer.getInt();
            int b = pcIdxBuffer.getInt();
            byte c = pcIdxBuffer.get(); // Ignore. Used in the game's code, but is always 0.
            EncodedTableId ids = new EncodedTableId(a, b);
            tableByIds.add(ids);
        }
        extraStart = pcIdxBuffer.position();
        extra = pcIdxBuffer.slice(extraStart, pcIdxBuffer.remaining()).order(ByteOrder.LITTLE_ENDIAN);

        buildFileSystem();
    }

    private HashMap<Integer, ArrayList<ExtraData>> unpackBottomTable() {
        HashMap<Integer, ArrayList<ExtraData>> extras = new HashMap<>();

        int startOffset;
        byte num_entries;
        int offset;
        int size;
        byte chr;
        int chrPosition;
        while (extra.remaining() > 0) {
            startOffset = extra.position();

            num_entries = extra.get();
            if (num_entries == 0) break;

            ArrayList<ExtraData> lf = new ArrayList<>();

            for (int i = 0; i < num_entries; i++) {
                offset = extra.getInt();
                if (i == 0) {
                    size = 0;
                }
                else {
                    size = extra.getInt();
                    extra.getInt(); // Skip 4 bytes
                }
                chr = extra.get();
                chrPosition = extra.get();
                extra.get(); // Skip 1 byte
                lf.add(new ExtraData(offset*4, size*4, chr, chrPosition));
            }
            extras.put(startOffset, lf);
        }
        return extras;
    }

    private void buildFileSystem() {
        files = new ArrayList<>();
        HashMap<Integer, ArrayList<ExtraData>> bottomTableEntries = unpackBottomTable();

        for (int i = 0; i < tableByIds.size(); i++) {
            EncodedTableId tableId = tableByIds.get(i);
            if (tableId.a == 0 && tableId.b == 0) {
                files.add(null);
                continue;
            }
            DecodedTableId decoded = tableId.decode();
            if (Integer.remainderUnsigned(tableId.a, 2) == 1) {
                // Multiple files

                int index = Integer.divideUnsigned(decoded.offset, 4) - extraStart;

                ArchiveEntry fileList = new ArchiveEntry(new ArrayList<>());

                for (ExtraData extraData : bottomTableEntries.get(index)) {
                    int newSize = extraData.size == 0 ? decoded.size : extraData.size;
                    fileList.files.add(new ArchiveEntryList(extraData.chr, extraData.chr_position, pcBinBuffer.slice(extraData.offset, newSize).order(ByteOrder.LITTLE_ENDIAN)));
                }
                files.add(fileList);
            } else {
                if (decoded.size < 0) {
                    Integer.toUnsignedLong(decoded.size);
                }
                files.add(new ArchiveEntry(pcBinBuffer.slice(decoded.offset, decoded.size).order(ByteOrder.LITTLE_ENDIAN)));
            }
        }
    }

    private int filename_crc(String name) throws UnsupportedEncodingException {
        byte[] asBytes = name.getBytes("UTF-8");

        int crc = asBytes[0];
        for (int i = 1; i < asBytes.length; i++) {
            crc = (crc * 0x25 + asBytes[i]);
        }
        return crc & 0x3FF;
    }

    public ByteBuffer getFile(int id) {
        String name = "";
        return getFile(id, name);
    }

    public ByteBuffer getFile(int id, String name) {
        ArchiveEntry fileList = files.get(id);
        if (fileList.isList) {
            for (var archiveEntry : fileList.files) {
                if (archiveEntry.chr_position < name.length() & name.charAt(archiveEntry.chr_position) == archiveEntry.chr) {
                    //System.out.println(String.format("getFile: %s (%d) nested: %d(%s) == %s", name, id, archiveEntry.chr_position, name.charAt(archiveEntry.chr_position), (char)archiveEntry.chr));
                    return archiveEntry.file;
                }
            }
        } else {
            //System.out.println(String.format("getFile: %s (%d)", name, id));
            return fileList.file;
        }
        return null;
    }

    public ByteBuffer getFile(String name) {
        int id;
        try {
            id = filename_crc(name);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
        return getFile(id, name);
    }

    public void setFile(int id, ByteBuffer newFile) {
        String name = "";
        setFile(id, name, newFile);
    }

    
    public void setFile(int id, String name, ByteBuffer newFile) {
        ArchiveEntry fileList = files.get(id);
        if (fileList.isList) {
            for (var archiveEntry : fileList.files) {
                if (archiveEntry.chr_position < name.length() & name.charAt(archiveEntry.chr_position) == archiveEntry.chr) {
                    archiveEntry.file = newFile;
                    return;
                }
            }
            // TODO: Support adding new files to list
        } else {
            fileList.file = newFile;
        }
        return;
    }

    public void setFile(String name, ByteBuffer newFile) {
        int id;
        try {
            id = filename_crc(name);
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
        setFile(id, name, newFile);
    }
    
    private int align16(int d) {
        return (d + 0x1f) & ~0x1f;
    }

    public void repack(ByteBuffer newPcIdx, ByteBuffer newPcBin) {
        newPcIdx.order(ByteOrder.LITTLE_ENDIAN);
        newPcBin.order(ByteOrder.LITTLE_ENDIAN);
        newPcIdx.putInt(files.size());
        newPcIdx.putInt(0);
        int currentOffset = 0;
        int bottomTableEntriesOffset = files.size()*9 + 8;
        ByteBuffer bottomTableEntries = ByteBuffer.allocate(256*1024*1024).order(ByteOrder.LITTLE_ENDIAN); // Twice the size of original rom. Should be more than enough
        byte[] zeroes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        
        //for (int index = 0; index < files.size(); index++) {
        for (ArchiveEntry archiveEntry : files) {
            if (archiveEntry == null) {
                newPcIdx.put(zeroes, 0, 9);
                continue;
            }
            if (archiveEntry.isList) {
                bottomTableEntries.put((byte)archiveEntry.files.size());
                for (int i = 0; i < archiveEntry.files.size(); i++) {
                    var entry = archiveEntry.files.get(i);
                    entry.file.rewind();
                    int size = entry.file.remaining();
                    bottomTableEntries.putInt(Integer.divideUnsigned(currentOffset, 4));
                    if (i == 0) {
                        var encoded = new DecodedTableId(bottomTableEntriesOffset*4, size).encode();
                        newPcIdx.putInt(encoded.a + 1);
                        newPcIdx.putInt(encoded.b);
                        newPcIdx.position(newPcIdx.position() - 2);
                        newPcIdx.put((byte)0x42);
                        newPcIdx.put((byte)0);
                        newPcIdx.put((byte)0);
                    } else {
                        bottomTableEntries.putInt(Integer.divideUnsigned(size, 4));
                        bottomTableEntries.putInt(0);
                    }
                    bottomTableEntries.put(entry.chr);
                    bottomTableEntries.put((byte)entry.chr_position);
                    bottomTableEntries.put((byte)0);

                    newPcBin.put(entry.file);

                    // currentOffset += size;
                    // if (Integer.remainderUnsigned(currentOffset, 0x10) != 0) {
                    //     int padding = 0x10 - Integer.remainderUnsigned(currentOffset, 0x10);
                    //     System.out.println(String.format("%d + %d", currentOffset, padding));
                    //     System.out.println(padding);
                    //     newPcBin.put(zeroes, 0, padding);
                    //     currentOffset += padding;
                    //     assert Integer.remainderUnsigned(currentOffset, 0x10) == 0;
                    // } else {
                    //     newPcBin.put(zeroes, 0, 0x10);
                    //     currentOffset += 0x10;
                    // }
                    int newOffset = align16(currentOffset+size);
                    newPcBin.put(zeroes, 0, newOffset-(currentOffset+size));
                    currentOffset = newOffset;

                }
                bottomTableEntriesOffset = bottomTableEntries.position() + files.size()*9 + 8;
            } else {
                archiveEntry.file.rewind();
                int size = archiveEntry.file.remaining();
                newPcBin.put(archiveEntry.file);
                var encoded = new DecodedTableId(currentOffset, size).encode();

                newPcIdx.putInt(encoded.a);
                newPcIdx.putInt(encoded.b);
                newPcIdx.position(newPcIdx.position() - 2);
                newPcIdx.put((byte)0x42);
                newPcIdx.put((byte)0);
                newPcIdx.put((byte)0);

                
                // currentOffset += size;
                // if (Integer.remainderUnsigned(currentOffset, 0x10) != 0) {
                //     int padding = 0x10 - Integer.remainderUnsigned(currentOffset, 0x10);
                //     System.out.println(String.format("%d + %d", currentOffset, padding));
                //     newPcBin.put(zeroes, 0, padding);
                //     currentOffset += padding;
                //     assert Integer.remainderUnsigned(currentOffset, 0x10) == 0;
                // } else {
                //     newPcBin.put(zeroes, 0, 0x10);
                //     currentOffset += 0x10;
                // }
                
                int newOffset = align16(currentOffset+size);
                newPcBin.put(zeroes, 0, newOffset-(currentOffset+size));
                currentOffset = newOffset;
            }
        }
        newPcIdx.put(bottomTableEntries.limit(bottomTableEntries.position()).rewind());
        newPcIdx.limit(newPcIdx.position());
        newPcBin.limit(newPcBin.position());
    }


}
