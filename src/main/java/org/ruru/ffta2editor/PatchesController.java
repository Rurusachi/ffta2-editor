package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ruru.ffta2editor.model.character.CharacterData;
import org.ruru.ffta2editor.model.job.JobData;
import org.ruru.ffta2editor.model.unitSst.UnitAnimation;
import org.ruru.ffta2editor.model.unitSst.UnitAnimation.UnitAnimationFrame;
import org.ruru.ffta2editor.model.unitSst.UnitSst;
import org.ruru.ffta2editor.model.unitSst.UnitSst.SstHeaderNode;
import org.ruru.ffta2editor.utility.LZSS;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
                                             0x45, 0x46, 0x47, 0x48,
                                             0x30, 0x40, 0x41 // One of these is used by Iai Blow. Only some races have 0x40 and 0x41
                                            };

    private static int[] alwaysOverride = {57, 58, 59, 60, 67};
                                             
    //@FXML CheckBox animationFix;

    int entryLength;

    @FXML
    private void applyAnimationFix() {
            if (App.archive != null) {

            // Gather required animations from Hume and Moogle
            ArrayList<Pair<Integer, byte[]>> compressedLandAnimations = new ArrayList<>();

            ArrayList<UnitSst> landAnimationSources = new ArrayList<>();
            landAnimationSources.add(App.unitSstList.get(0));
            landAnimationSources.add(App.unitSstList.get(44));

            for (int animationId : attackAnimations) {
                int[] keys = {(animationId << 8), (animationId << 8) | 0x10};
                for (int key : keys) {
                    int animationKey = dualWield.contains(key >> 8) ? (0x45 << 8) | (key & 0xFF) : key;
                    
                    Pair<Integer,byte[]> compressedAnimation = null;
                    for (UnitSst unitSst : landAnimationSources) {
                        if (unitSst.find(animationKey) != null) {
                            compressedAnimation = new Pair<Integer,byte[]>(key, unitSst.getCompressedValue(animationKey));
                            break;
                        }
                    }
                    if (compressedAnimation == null) {
                        System.err.println(String.format("Animation not in Hume and Moogle: %04X", key));
                    } else {
                        compressedLandAnimations.add(compressedAnimation);
                    }
                }
            }
            ArrayList<Pair<Integer, byte[]>> compressedWaterAnimations = new ArrayList<>();

            ArrayList<UnitSst> waterAnimationSources = new ArrayList<>();
            waterAnimationSources.add(App.unitSstList.get(249));
            waterAnimationSources.add(App.unitSstList.get(262));

            for (int animationId : attackAnimations) {
                int[] keys = {(animationId << 8), (animationId << 8) | 0x10};
                for (int key : keys) {
                    int animationKey = dualWield.contains(key >> 8) ? (0x45 << 8) | (key & 0xFF) : key;
                    
                    Pair<Integer,byte[]> compressedAnimation = null;
                    for (UnitSst unitSst : waterAnimationSources) {
                        if (unitSst.find(animationKey) != null) {
                            compressedAnimation = new Pair<Integer,byte[]>(key, unitSst.getCompressedValue(animationKey));
                            break;
                        }
                    }
                    if (compressedAnimation == null) {
                        System.err.println(String.format("Animation not in Hume and Bangaa: %04X", key));
                    } else {
                        compressedWaterAnimations.add(compressedAnimation);
                    }
                }
            }
            
            entryLength = Short.toUnsignedInt(App.naUnitAnimTable.getShort(2)) + 1;
            int numUnits = 70;
            for (int i = 0; i < numUnits; i++) {
                // TODO: Port other missing animations to Illua
                portAnimations(i, compressedLandAnimations, landAnimationSources);
            }
            portAnimations(102, compressedLandAnimations, landAnimationSources); // Frimelda

            // In water
            numUnits = 48;
            for (int i = 249; i < 249+numUnits; i++) {
                // TODO: Port other missing animations to Illua
                portAnimations(i, compressedWaterAnimations, waterAnimationSources);
            }
            portAnimations(298, compressedWaterAnimations, waterAnimationSources);
            //Path testPath = Path.of("G:\\zzz");
            //try {
            //        FileOutputStream testOutput = new FileOutputStream(testPath.resolve("naUnitAnimTable").toFile());
            //        testOutput.write(App.naUnitAnimTable.array());
            //        testOutput.close();
            //    } catch (Exception e) {
            //        System.err.println(e);
            //}
            
            // Fix Agent's hardcoded limitations
            App.arm9.putInt(0x000b812c, 0xEA000005);
            
            
            Alert loadAlert = new Alert(AlertType.INFORMATION);
            loadAlert.setTitle("Animation Fix patch");
            loadAlert.setHeaderText("Patch applied");
            //saveAlert.setDialogPane(new DialogPane());
            loadAlert.show();
        }
    }

    private void portAnimations(int i, ArrayList<Pair<Integer, byte[]>> compressedAnimations, List<UnitSst> animationSources) {
        UnitSst unitSst = App.unitSstList.get(i);
        for (Pair<Integer, byte[]> animation : compressedAnimations) {
            if (unitSst.find(animation.getKey()) != null){
                if (i != 67 && !(57 <= i  && i < 61)) {
                    continue;
                }
            }
            //System.out.println(String.format("%d: %04X", i, animation.getKey()));
            byte[] compressedAnimation = animation.getValue();
            if (i == 67 || i == 295) {
                // Adjust animations for Al-Cid
                compressedAnimation =  offsetAnimation(animation, animationSources, 12, 2);
            } else if (57 <= i  && i < 61) {
                // Adjust animations for Gria
                compressedAnimation =  offsetAnimation(animation, animationSources, 12, 6);
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

    private byte[] offsetAnimation(Pair<Integer, byte[]> animation, List<UnitSst> animationSources, int start, int offset) {
        UnitAnimation decodedAnimation = null;
        int animationKey = dualWield.contains(animation.getKey() >> 8) ? (0x45 << 8) | (animation.getKey() & 0xFF) : animation.getKey();
        for (UnitSst unitSst : animationSources) {
            if (unitSst.find(animationKey) != null) {
                decodedAnimation = unitSst.getAnimation(animationKey);
                break;
            }
        }
        for (UnitAnimationFrame frame : decodedAnimation.frames) {
            byte spriteIndex = frame.spriteIndex.getValue();
            if (spriteIndex >= start) {
                frame.spriteIndex.set((byte)(spriteIndex + offset));
            }
        }
        ByteBuffer newCompressedAnimation = LZSS.encode(ByteBuffer.wrap(decodedAnimation.toBytes()).order(ByteOrder.LITTLE_ENDIAN));
        ByteBuffer newCompressedValue = ByteBuffer.allocate(newCompressedAnimation.capacity()+4).order(ByteOrder.LITTLE_ENDIAN);
        newCompressedValue.putShort(newCompressedAnimation.getShort(1));
        for (int j = 2; j < 4; j++) {
            newCompressedValue.put(decodedAnimation.animHeader[j]);
        }
        newCompressedValue.put(newCompressedAnimation);
        //compressedAnimation = newCompressedValue.array();
        return newCompressedValue.array();
    }

    @FXML 
    public void applyExpandedTopSpritesFix() {
        if (App.archive != null) {
            // Character data top sprite and enemy top sprite
            App.arm9.putInt(0x000b5ab4, 0xe1d001b8); // ldrh r0, [r0, 0x18]
            App.arm9.putInt(0x000b5abc, 0xe1d001ba); // ldrh r0, [r0, 0x1a]
            
            // Job Data top sprite and enemy top sprite
            App.arm9.putInt(0x000b5e20, 0xe1d003bc); // ldrh r0, [r0, 0x3c]
            App.arm9.putInt(0x000b5e28, 0xe1d003be); // ldrh r0, [r0, 0x3e]

            App.arm9.putInt(0x000b5e78, 0xe5d00040); // ldrb r0, [r0, 0x40]
            


            JobData.patchedTopSprite = true;
            CharacterData.patchedTopSprite = true;
            
            Alert loadAlert = new Alert(AlertType.INFORMATION);
            loadAlert.setTitle("Expanded Top Sprite Index Limit patch");
            loadAlert.setHeaderText("Patch applied");
            //saveAlert.setDialogPane(new DialogPane());
            loadAlert.show();
        }
    }
    
    public void applyPatches() {
        //if (animationFix.selectedProperty().getValue()) {
        //    applyAnimationFix();
        //}
    }
}