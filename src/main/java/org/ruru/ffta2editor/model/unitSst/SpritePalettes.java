package org.ruru.ffta2editor.model.unitSst;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import org.ruru.ffta2editor.utility.LZSS;

public class SpritePalettes {
    public int numColors;
    public ArrayList<byte[]> palettes = new ArrayList<>();

    public SpritePalettes(ByteBuffer paletteBytes, int numColors) {
        //if (paletteBytes.remaining() % 0x100 != 0){
        //    this.count = paletteBytes.remaining() / 64;
        //    numColors = 32;
        //} else {
        //    this.count = paletteBytes.remaining() / 256;
        //    numColors = 128;
        //}
        //numColors = (paletteBytes.remaining() / count) / 2;
        this.numColors = numColors;
        int count = paletteBytes.remaining() / (numColors*2);
        for (int i = 0; i < count; i++) {
        //while(paletteBytes.remaining() > 0) {
            //numColors = paletteBytes.remaining() > 0x100 ? 128 : 32;
            byte[] palette = new byte[numColors*2];
            paletteBytes.get(palette);
            palettes.add(palette);
        }
    }

    public byte[] toBytes() {
        return toByteBuffer().array();
    }

    public ByteBuffer toByteBuffer() {
        ByteBuffer newPalettes = ByteBuffer.allocate(palettes.stream().mapToInt(x -> x.length).sum()).order(ByteOrder.LITTLE_ENDIAN);
        
        for (byte[] paletteBytes : palettes) {
            newPalettes.put(paletteBytes);
        }
        
        newPalettes.limit(newPalettes.position());
        ByteBuffer compressedPalettes = LZSS.encode(newPalettes.rewind(), true);
        

        return compressedPalettes;
    }
}
