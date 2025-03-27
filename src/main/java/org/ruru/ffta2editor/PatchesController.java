package org.ruru.ffta2editor;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ruru.ffta2editor.model.unitSst.UnitAnimation;
import org.ruru.ffta2editor.model.unitSst.UnitAnimation.UnitAnimationFrame;
import org.ruru.ffta2editor.model.unitSst.UnitSst;
import org.ruru.ffta2editor.model.unitSst.UnitSst.SstHeaderNode;
import org.ruru.ffta2editor.utility.LZSS;

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
    private static List<Integer> dualWield = Arrays.asList(0x45, 0x46, 0x47, 0x48, 0x49);

    private static int[] attackAnimations = {0x21, 0x22, 0x23,
                                             0x24, 0x25, 0x26,
                                             0x27, 0x28, 0x29,
                                             0x2a, 0x2b, 0x2c,
                                             0x2d, 0x2e, 0x2f,
                                             0xb, 0xc, 0xd,
                                             0xf,
                                             0x45, 0x46, 0x47, 0x48
                                            };
                                             
    @FXML CheckBox animationFix;
    private void applyAnimationFix() {
            if (App.archive != null) {

            // Gather required animations from Hume and Moogle
            ArrayList<Pair<Integer, byte[]>> compressedAnimations = new ArrayList<>();

            UnitSst humeSoldierSst = App.unitSstList.get(0);
            UnitSst moogleAnimistSst = App.unitSstList.get(44);

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
            
            int entryLength = Short.toUnsignedInt(App.naUnitAnimTable.getShort(2)) + 1;
            int numUnits = 0x46;
            for (int i = 0; i < numUnits; i++) {
                // TODO: Port other missing animations to Illua
                
                UnitSst unitSst = App.unitSstList.get(i);
                for (Pair<Integer, byte[]> animation : compressedAnimations) {
                    if (unitSst.find(animation.getKey()) != null) continue;
                    byte[] compressedAnimation = animation.getValue();
                    if (i == 67) {
                        UnitAnimation decodedAnimation;
                        int animationKey = dualWield.contains(animation.getKey() >> 8) ? (0x45 << 8) | (animation.getKey() & 0xFF) : animation.getKey();
                        if (humeSoldierSst.find(animationKey) != null) {
                            decodedAnimation = humeSoldierSst.getAnimation(animationKey);
                        } else {
                            decodedAnimation = moogleAnimistSst.getAnimation(animationKey);
                        }
                        for (UnitAnimationFrame frame : decodedAnimation.frames) {
                            if (frame.spriteIndex >= 12) {
                                frame.spriteIndex += 2;
                            }
                        }
                        ByteBuffer newCompressedAnimation = LZSS.encode(ByteBuffer.wrap(decodedAnimation.toBytes()).order(ByteOrder.LITTLE_ENDIAN));
                        ByteBuffer newCompressedValue = ByteBuffer.allocate(newCompressedAnimation.capacity()+4).order(ByteOrder.LITTLE_ENDIAN);
                        newCompressedValue.putShort(newCompressedAnimation.getShort(1));
                        for (int j = 2; j < 4; j++) {
                            newCompressedValue.put(decodedAnimation.animHeader[j]);
                        }
                        newCompressedValue.put(newCompressedAnimation);
                        compressedAnimation = newCompressedValue.array();
                    }

                    unitSst.hasChanged = true;
                    byte animationId = (byte)(animation.getKey() >>> 8);
                    byte animationType = (byte)(animation.getKey() & 0xFF);
                    SstHeaderNode newNode = new SstHeaderNode(unitSst.size, animationType, animationId, 0);
                    newNode.compressedValue = compressedAnimation;
                    unitSst.insert(newNode);
                    
                    int oldValue = App.naUnitAnimTable.get(4+1 + i*entryLength + animationId);
                    byte newValue = (byte)(oldValue | 0x11);
                    App.naUnitAnimTable.put(4+1 + i*entryLength + animationId, newValue);
                }
            }
            //Path testPath = Path.of("G:\\zzz");
            //try {
            //        FileOutputStream testOutput = new FileOutputStream(testPath.resolve("naUnitAnimTable").toFile());
            //        testOutput.write(App.naUnitAnimTable.array());
            //        testOutput.close();
            //    } catch (Exception e) {
            //        System.err.println(e);
            //}
        }
    }
    
    public void applyPatches() {
        if (animationFix.selectedProperty().getValue()) {
            applyAnimationFix();
        }
    }
}