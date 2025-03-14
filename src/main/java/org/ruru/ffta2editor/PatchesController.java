package org.ruru.ffta2editor;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ruru.ffta2editor.model.unitSst.UnitSst;
import org.ruru.ffta2editor.model.unitSst.UnitSst.SstHeaderNode;
import org.ruru.ffta2editor.utility.IdxAndPak;
import org.ruru.ffta2editor.utility.LZSS;
import org.ruru.ffta2editor.utility.LZSS.LZSSDecodeResult;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.util.Pair;

public class PatchesController {
    //private static List<Integer> bladed = Arrays.asList(0x21, 0x22, 0x23);
    //private static List<Integer> piercing = Arrays.asList(0x24, 0x25, 0x26);
    //private static List<Integer> bowGun = Arrays.asList(0x27, 0x28, 0x29);
    //private static List<Integer> bonk = Arrays.asList(0x2a, 0x2b, 0x2c);
    //private static List<Integer> explosive = Arrays.asList(0x2d, 0x2e, 0x2f);
    //private static List<Integer> knuckles = Arrays.asList(0xb, 0xc, 0xd);
    //private static List<Integer> card = Arrays.asList(0xf);
    private static List<Integer> dualWield = Arrays.asList(0x45, 0x46, 0x47, 0x48);

    private static int[] attackAnimations = {0x21, 0x22, 0x23,
                                             0x24, 0x25, 0x26,
                                             0x27, 0x28, 0x29,
                                             0x2a, 0x2b, 0x2c,
                                             0x2d, 0x2e, 0x2f,
                                             0xb, 0xc, 0xd,
                                             0xf,
                                             0x45, 0x46, 0x47, 0x48};
                                             
    @FXML CheckBox animationFix;
    private void applyAnimationFix() {
            if (App.archive != null) {

            // Gather required animations from Hume and Moogle
            ArrayList<Pair<Integer, byte[]>> compressedAnimations = new ArrayList<>();

            ByteBuffer humeSoldierSstBytes = App.unitSsts.getFile(0);
            UnitSst humeSoldierSst = new UnitSst(humeSoldierSstBytes);
            ByteBuffer moogleAnimistSstBytes = App.unitSsts.getFile(44);
            UnitSst moogleAnimistSst = new UnitSst(moogleAnimistSstBytes);
            for (int animationId : attackAnimations) {
                int[] keys = {(animationId << 8), (animationId << 8) | 0x10};
                for (int key : keys) {
                    int animationKey = dualWield.contains(key >> 8) ? (0x45 << 8) | (key & 0xFF) : key;
                    if (humeSoldierSst.find(animationKey) != null) {
                        compressedAnimations.add(new Pair<Integer,byte[]>(key, humeSoldierSst.getCompressedValue(animationKey)));
                    } else if (moogleAnimistSst.find(animationKey) != null) {
                        compressedAnimations.add(new Pair<Integer,byte[]>(key, moogleAnimistSst.getCompressedValue(animationKey)));
                    } else {
                        System.err.println(String.format("Animation not in Hume and Moogle: %04X", key));
                    }
                }
            }

            try {
                byte[] testData = compressedAnimations.get(0).getValue();
                //FileOutputStream lzssTestOutput = new FileOutputStream(new File("G:\\testOriginal"));
                //lzssTestOutput.write(testData);
                //lzssTestOutput.close();
                LZSSDecodeResult decoded = LZSS.decode(ByteBuffer.wrap(testData).position(4));
                //lzssTestOutput = new FileOutputStream(new File("G:\\testDecoded"));
                //lzssTestOutput.write(decoded.decodedData.array());
                //lzssTestOutput.close();
                ByteBuffer encoded = LZSS.encode(ByteBuffer.wrap(decoded.decodedData.array()));
                FileOutputStream lzssTestOutput = new FileOutputStream(new File("G:\\testEncoded"));
                lzssTestOutput.write( Arrays.copyOfRange(encoded.array(), 0, encoded.limit()) );
                lzssTestOutput.close();
                LZSSDecodeResult redecoded = LZSS.decode(encoded.rewind());
                lzssTestOutput = new FileOutputStream(new File("G:\\testReDecoded"));
                lzssTestOutput.write(redecoded.decodedData.array());
                lzssTestOutput.close();
                
            } catch (Exception e) {
                System.err.println(e);
                // TODO: handle exception
            }
            
            Path testPath = Path.of("G:\\zzz");
            for (int i = 0; i < 0x3D; i++) {
                ByteBuffer unitSstBytes = App.unitSsts.getFile(i);
                UnitSst unitSst = new UnitSst(unitSstBytes);

                ByteBuffer newSstHeader = ByteBuffer.allocate(1024*1024).order(ByteOrder.LITTLE_ENDIAN);
                ByteBuffer newSstData = ByteBuffer.allocate(1024*1024).order(ByteOrder.LITTLE_ENDIAN);
                //newSstHeader.put(unitSstBytes.array(), 0, unitSstBytes.position());
                int dataOffset = unitSst.root.offset*0x10;
                unitSstBytes.position(unitSst.root.offset*0x10);
                newSstData.put(unitSstBytes);

                int initialNodes = unitSst.size;
                int numAdded = 0;
                for (Pair<Integer, byte[]> animation : compressedAnimations) {
                    if (unitSst.find(animation.getKey()) == null) {
                        numAdded++;
                        SstHeaderNode newNode = new SstHeaderNode(unitSst.size, (byte)(animation.getKey() & 0xFF), (byte)(animation.getKey() >>> 8), (newSstData.position() + dataOffset) >>> 4);
                        unitSst.insert(newNode);
                        newSstData.put(animation.getValue());
                        if (newSstData.position() % 0x10 != 0) {
                            newSstData.put(new byte[0x10 - (newSstData.position() % 0x10)]);
                        }
                    }
                }
                newSstData.limit(newSstData.position());

                int offsetDifference = numAdded >>> 1;
                if (numAdded % 2 == 1 && initialNodes % 2 == 1) {
                    offsetDifference++;
                }
                for (var node : unitSst.asList()) {
                    node.offset += offsetDifference;
                    newSstHeader.put(node.toBytes());
                }
                newSstHeader.mark();
                newSstHeader.put(new byte[newSstHeader.position() % 0x10 == 0 ? 0x10 : 0x8]);
                newSstHeader.limit(newSstHeader.position());

                int fullLength = newSstHeader.position() + newSstData.position();
                newSstHeader.reset();
                newSstHeader.position(newSstHeader.position()+2);
                newSstHeader.putInt(fullLength);

                ByteBuffer newSstBytes = ByteBuffer.allocate(fullLength).order(ByteOrder.LITTLE_ENDIAN);
                newSstHeader.rewind();
                newSstBytes.put(newSstHeader);
                newSstData.rewind();
                newSstBytes.put(newSstData);
                App.unitSsts.setFile(i, newSstBytes);
            
                //Pair<ByteBuffer, ByteBuffer> idxPak = App.unitSsts.repack();
                //App.archive.setFile("char/rom/rom_idx/UnitSst.rom_idx", idxPak.getKey());
                //App.archive.setFile("char/rom/pak/UnitSst.pak", idxPak.getValue());

                try {
                    FileOutputStream testOutput = new FileOutputStream(testPath.resolve(String.format("unit_sst%03d", i)).toFile());
                    testOutput.write(newSstBytes.array());
                    testOutput.close();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            
            /* 
            ByteBuffer soldierSstBytes = unitSsts.getFile(0);
            soldierSst = new UnitSst(soldierSstBytes);
            ArrayList<SstHeaderNode> nodes = soldierSst.asList();
            soldierSstBytes.position(soldierSstBytes.position() + soldierSstBytes.remaining());
            soldierSst.insert(new SstHeaderNode(nodes.size(), 0, 0x21, 4000, 0, 0));
            
            */
        }
    }
    
    public void applyPatches() {
        if (animationFix.selectedProperty().getValue()) {
            applyAnimationFix();
        }
    }
}