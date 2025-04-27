package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.ruru.ffta2editor.model.battle.SBN;
import org.ruru.ffta2editor.model.battle.SBN.Command;
import org.ruru.ffta2editor.model.unitSst.SstHeaderNode;
import org.ruru.ffta2editor.model.unitSst.UnitAnimation;
import org.ruru.ffta2editor.model.unitSst.UnitAnimation.UnitAnimationFrame;
import org.ruru.ffta2editor.model.unitSst.UnitSst;
import org.ruru.ffta2editor.utility.LZSS;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ToggleButton;
import javafx.util.Pair;

public class PatchesController {
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

    public static BooleanProperty patchedExpandedTopSprites = new SimpleBooleanProperty();
    public static BooleanProperty patchedSignedEquipmentStats = new SimpleBooleanProperty();
    public static BooleanProperty patchedStartingMp = new SimpleBooleanProperty();
    public static BooleanProperty patchedSequencerPeytral = new SimpleBooleanProperty();
    
    public static IntegerProperty maxLevel = new SimpleIntegerProperty();
    
    
    
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

    private record PatchElement(int address, int originalBytes, int modifiedBytes){};

    private void applyPatchElements(List<PatchElement> patches, ByteBuffer file, boolean apply) {
        if (apply) {
            for (PatchElement patch : patches) {
                if (file.getInt(patch.address) != patch.originalBytes){
                    System.err.println(String.format("applyPatchElements: Unexpected instruction (%08x) at %08x in arm9", App.arm9.getInt(patch.address), patch.address));
                }
                file.putInt(patch.address, patch.modifiedBytes);
            }
        } else {
            for (PatchElement patch : patches) {
                if (file.getInt(patch.address) != patch.modifiedBytes){
                    System.err.println(String.format("applyPatchElements: Unexpected instruction (%08x) at %08x in arm9", App.arm9.getInt(patch.address), patch.address));
                }
                file.putInt(patch.address, patch.originalBytes);
            }
        }
    }
    //private void revertPatchElements(List<PatchElement> patches) {
    //    for (PatchElement patch : patches) {
    //        if (App.arm9.getInt(patch.address) != patch.modifiedBytes){
    //            System.err.println(String.format("applyPatchElements: Unexpected instruction (%08x) at %08x in arm9", App.arm9.getInt(patch.address), patch.address));
    //        }
    //        App.arm9.putInt(patch.address, patch.originalBytes);
    //    }
    //}
                                             
    //@FXML CheckBox animationFix;

    int entryLength;

    
    @FXML ToggleButton expandedTopSprites;
    @FXML ToggleButton signedEquipmentStats;
    @FXML ToggleButton startingMp;

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
                portAnimations(i, compressedLandAnimations, landAnimationSources);
            }
            portAnimations(102, compressedLandAnimations, landAnimationSources); // Frimelda

            // In water
            numUnits = 48;
            for (int i = 249; i < 249+numUnits; i++) {
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
            newNode.value = compressedAnimation;
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
    public void applyExpandedTopSprites() {
        if (App.archive != null) {
            List<PatchElement> arm9Patches = new ArrayList<>();
            
            // Character data top sprite and enemy top sprite
            arm9Patches.add(new PatchElement(0x000b5ab4, 0xe5d00018, 0xe1d001b8)); // ldrb r0, [r0, 0x18] -> ldrh r0, [r0, 0x18]
            arm9Patches.add(new PatchElement(0x000b5abc, 0xe5d00019, 0xe1d001ba)); // ldrb r0, [r0, 0x19] -> ldrh r0, [r0, 0x1a]
            
            // Job Data top sprite and enemy top sprite
            arm9Patches.add(new PatchElement(0x000b5e20, 0xe5d0003f , 0xe1d003bc)); // ldrb r0, [r0, 0x3f] -> ldrh r0, [r0, 0x3c]
            arm9Patches.add(new PatchElement(0x000b5e28, 0xe5d00040 , 0xe1d003be)); // ldrb r0, [r0, 0x40] -> ldrh r0, [r0, 0x3e]
            arm9Patches.add(new PatchElement(0x000b5e78, 0xe5d0003e , 0xe5d00040)); // ldrb r0, [r0, 0x3e] -> ldrb r0, [r0, 0x40]
            
            // The value is already flipped
            boolean newValue = patchedExpandedTopSprites.getValue();
            applyPatchElements(arm9Patches, App.arm9, newValue);
            String alertText = newValue ? "Patch applied" : "Patch removed";
            
            Alert loadAlert = new Alert(AlertType.INFORMATION);
            loadAlert.setTitle("Max Starting MP patch.");
            loadAlert.setHeaderText(String.format("%s. Please restart the editor", alertText));
            loadAlert.show();
        }
    }

    @FXML
    public void applySignedEquipmentStats() {
        if (App.archive != null) {
            List<PatchElement> arm9Patches = new ArrayList<>();
            List<PatchElement> overlay11Patches = new ArrayList<>();

            // MOVE
            arm9Patches.add(new PatchElement(0x000b9b10, 0xe5d40019, 0xe1d401d9)); // ldrb -> ldrsb r0, [r4, 0x19]
            arm9Patches.add(new PatchElement(0x000cfd28, 0xe5d01019, 0xe1d011d9)); // ldrb -> ldrsb r1, [r0, 0x19]
            arm9Patches.add(new PatchElement(0x00110724, 0xe5d41019, 0xe1d411d9)); // ldrb -> ldrsb r1, [r4, 0x19]
            arm9Patches.add(new PatchElement(0x00110728, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x0011072c, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x0011073c, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00110764, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00110f14, 0xe5d41019, 0xe1d411d9)); // ldrb -> ldrsb r1, [r4, 0x19]
            arm9Patches.add(new PatchElement(0x00110f18, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00110f1c, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x00110f24, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00110f54, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00111b30, 0xe5d41019, 0xe1d411d9)); // ldrb -> ldrsb r1, [r4, 0x19]
            arm9Patches.add(new PatchElement(0x00111b34, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111b38, 0x0a000013, 0xe58d0018)); // beq ... -> str r0, [sp, local_50]
            arm9Patches.add(new PatchElement(0x00111b48, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00111b70, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_50] -> mov r0, 0x1
            // JUMP
            arm9Patches.add(new PatchElement(0x000b9b20, 0xe5d4001a, 0xe1d401da)); // ldrb -> ldrsb r0, [r4, 0x1a]
            arm9Patches.add(new PatchElement(0x000cfd50, 0xe5d0101a, 0xe1d011da)); // ldrb -> ldrsb r1, [r0, 0x1a]
            arm9Patches.add(new PatchElement(0x00110770, 0xe5d4101a, 0xe1d411da)); // ldrb -> ldrsb r1, [r4, 0x1a]
            arm9Patches.add(new PatchElement(0x00110774, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00110778, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x00110788, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x001107b0, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00110f60, 0xe5d4101a, 0xe1d411da)); // ldrb -> ldrsb r1, [r4, 0x1a]
            arm9Patches.add(new PatchElement(0x00110f64, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00110f68, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x00110f70, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00110fa0, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00111b8c, 0xe5d4101a, 0xe1d411da)); // ldrb -> ldrsb r1, [r4, 0x1a]
            arm9Patches.add(new PatchElement(0x00111b90, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111b94, 0x0a000013, 0xe58d0018)); // beq ... -> str r0, [sp, local_50]
            arm9Patches.add(new PatchElement(0x00111ba4, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00111bcc, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_50] -> mov r0, 0x1
            // EVASION
            overlay11Patches.add(new PatchElement(0xe2a8, 0xe5d07018, 0xe1d071d8)); // ldrb -> ldrsb r7, [r0, 0x18]
            arm9Patches.add(new PatchElement(0x000b9b04, 0x05d40018, 0x01d401d8)); // ldrbeq -> ldrsbeq r0, [r4, 0x18] // Underflow bug
            arm9Patches.add(new PatchElement(0x000cfd00, 0xe5d01018, 0xe1d011d8)); // ldrb -> ldrsb r1, [r0, 0x18]
            arm9Patches.add(new PatchElement(0x001107bc, 0xe5d41018, 0xe1d411d8)); // ldrb -> ldrsb r1, [r4, 0x18]
            arm9Patches.add(new PatchElement(0x001107c0, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x001107c4, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x001107d4, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x001107fc, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00110fac, 0xe5d41018, 0xe1d411d8)); // ldrb -> ldrsb r1, [r4, 0x18]
            arm9Patches.add(new PatchElement(0x00110fb0, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00110fb4, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x00110fbc, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00110fec, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00111be8, 0xe5d41018, 0xe1d411d8)); // ldrb -> ldrsb r1, [r4, 0x18]
            arm9Patches.add(new PatchElement(0x00111bec, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111bf0, 0x0a000013, 0xe58d0018)); // beq ... -> str r0, [sp, local_50]
            arm9Patches.add(new PatchElement(0x00111c00, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00111c28, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_50] -> mov r0, 0x1
            // SPEED
            arm9Patches.add(new PatchElement(0x000b9b30, 0xe5d40017, 0xe1d401d7)); // ldrb -> ldrsb r0, [r4, 0x17]
            arm9Patches.add(new PatchElement(0x000cfcd8, 0xe5d01017, 0xe1d011d7)); // ldrb -> ldrsb r1, [r0, 0x17]
            arm9Patches.add(new PatchElement(0x00110808, 0xe5d41017, 0xe1d411d7)); // ldrb -> ldrsb r1, [r4, 0x17]
            arm9Patches.add(new PatchElement(0x0011080c, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00110810, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x00110820, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00110848, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00110ff8, 0xe5d41017, 0xe1d411d7)); // ldrb -> ldrsb r1, [r4, 0x17]
            arm9Patches.add(new PatchElement(0x00110ffc, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111000, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x00111008, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00111038, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00111c44, 0xe5d41017, 0xe1d411d7)); // ldrb -> ldrsb r1, [r4, 0x17]
            arm9Patches.add(new PatchElement(0x00111c48, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111c4c, 0x0a000013, 0xe58d0018)); // beq ... -> str r0, [sp, local_50]
            arm9Patches.add(new PatchElement(0x00111c5c, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00111c84, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_50] -> mov r0, 0x1
            // ATTACK
            overlay11Patches.add(new PatchElement(0xe260, 0xe5d07013, 0xe1d071d3)); // ldrb -> ldrsb r7, [r0, 0x13]
            arm9Patches.add(new PatchElement(0x000b9b54, 0xe5d41013, 0xe1d411d3)); // ldrb -> ldrsb r1, [r4, 0x13]
            arm9Patches.add(new PatchElement(0x000cfc38, 0xe5d01013, 0xe1d011d3)); // ldrb -> ldrsb r1, [r0, 0x13]
            arm9Patches.add(new PatchElement(0x00110854, 0xe5d41013, 0xe1d411d3)); // ldrb -> ldrsb r1, [r4, 0x13]
            arm9Patches.add(new PatchElement(0x00110858, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x0011085c, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x0011086c, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00110894, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00111048, 0xe5d45013, 0xe1d451d3)); // ldrb -> ldrsb r5, [r4, 0x13]
            arm9Patches.add(new PatchElement(0x0011106c, 0xe3550000, 0xe3a00000)); // cmp r5, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111070, 0xda000010, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x00111078, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x001110ac, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00111ca4, 0xe5d45013, 0xe1d451d3)); // ldrb -> ldrsb r5, [r4, 0x13]
            arm9Patches.add(new PatchElement(0x00111cc8, 0xe3550000, 0xe3a00000)); // cmp r5, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111ccc, 0xda000014, 0xe58d0018)); // beq ... -> str r0, [sp, local_50]
            arm9Patches.add(new PatchElement(0x00111cdc, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00111d08, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_50] -> mov r0, 0x1
            // DEFENSE
            overlay11Patches.add(new PatchElement(0xe2ac, 0xe5d08014, 0xe1d081d4)); // ldrb -> ldrsb r8, [r0, 0x14]
            overlay11Patches.add(new PatchElement(0xe2ec, 0xe5d07014, 0xe1d071d4)); // ldrb -> ldrsb r7, [r0, 0x14]
            arm9Patches.add(new PatchElement(0x000b9b98, 0xe5d40014, 0xe1d401d4)); // ldrb -> ldrsb r0, [r4, 0x14]
            arm9Patches.add(new PatchElement(0x000cfc60, 0xe5d01014, 0xe1d011d4)); // ldrb -> ldrsb r1, [r0, 0x14]
            arm9Patches.add(new PatchElement(0x001108a0, 0xe5d41014, 0xe1d411d4)); // ldrb -> ldrsb r1, [r4, 0x14]
            arm9Patches.add(new PatchElement(0x001108a4, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x001108a8, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x001108b8, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x001108e0, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x001110c4, 0xe5d45014, 0xe1d451d4)); // ldrb -> ldrsb r5, [r4, 0x14]
            arm9Patches.add(new PatchElement(0x001110e4, 0xe3550000, 0xe3a00000)); // cmp r5, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x001110e8, 0xda000010, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x001110f0, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00111124, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00111d30, 0xe5d45014, 0xe1d451d4)); // ldrb -> ldrsb r5, [r4, 0x14]
            arm9Patches.add(new PatchElement(0x00111d50, 0xe3550000, 0xe3a00000)); // cmp r5, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111d54, 0xda000014, 0xe58d0018)); // beq ... -> str r0, [sp, local_50]
            arm9Patches.add(new PatchElement(0x00111d64, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00111d90, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_50] -> mov r0, 0x1
            // MAGIC
            overlay11Patches.add(new PatchElement(0xe264, 0xe5d08015, 0xe1d081d5)); // ldrb -> ldrsb r8, [r0, 0x15]
            arm9Patches.add(new PatchElement(0x000b9ba8, 0xe5d40015, 0xe1d401d5)); // ldrb -> ldrsb r0, [r4, 0x15]
            arm9Patches.add(new PatchElement(0x000cfc88, 0xe5d01015, 0xe1d011d5)); // ldrb -> ldrsb r1, [r0, 0x15]
            arm9Patches.add(new PatchElement(0x001108ec, 0xe5d41015, 0xe1d411d5)); // ldrb -> ldrsb r1, [r4, 0x15]
            arm9Patches.add(new PatchElement(0x001108f0, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x001108f4, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x00110904, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x0011092c, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00111130, 0xe5d41015, 0xe1d411d5)); // ldrb -> ldrsb r1, [r4, 0x15]
            arm9Patches.add(new PatchElement(0x00111134, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111138, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x00111140, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00111170, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00111dac, 0xe5d41015, 0xe1d411d5)); // ldrb -> ldrsb r1, [r4, 0x15]
            arm9Patches.add(new PatchElement(0x00111db0, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111db4, 0x0a000013, 0xe58d0018)); // beq ... -> str r0, [sp, local_50]
            arm9Patches.add(new PatchElement(0x00111dc4, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00111dec, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_50] -> mov r0, 0x1
            // RESISTANCE
            overlay11Patches.add(new PatchElement(0xe2b0, 0xe5d00016, 0xe1d001d6)); // ldrb -> ldrsb r0, [r0, 0x16]
            overlay11Patches.add(new PatchElement(0xe2f0, 0xe5d08016, 0xe1d081d6)); // ldrb -> ldrsb r8, [r0, 0x16]
            arm9Patches.add(new PatchElement(0x000b9bb8, 0xe5d40016, 0xe1d401d6)); // ldrb -> ldrsb r0, [r4, 0x16]
            arm9Patches.add(new PatchElement(0x000cfcb0, 0xe5d01016, 0xe1d011d6)); // ldrb -> ldrsb r1, [r0, 0x16]
            arm9Patches.add(new PatchElement(0x00110938, 0xe5d41016, 0xe1d411d6)); // ldrb -> ldrsb r1, [r4, 0x16]
            arm9Patches.add(new PatchElement(0x0011093c, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00110940, 0x0a000220, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x00110950, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00110978, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x0011117c, 0xe5d41016, 0xe1d411d6)); // ldrb -> ldrsb r1, [r4, 0x16]
            arm9Patches.add(new PatchElement(0x00111180, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111184, 0x0a00000f, 0xe58d0018)); // beq ... -> str r0, [sp, local_28]
            arm9Patches.add(new PatchElement(0x0011118c, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x001111bc, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_28] -> mov r0, 0x1
            arm9Patches.add(new PatchElement(0x00111e08, 0xe5d41016, 0xe1d411d6)); // ldrb -> ldrsb r1, [r4, 0x16]
            arm9Patches.add(new PatchElement(0x00111e0c, 0xe3510000, 0xe3a00000)); // cmp r1, 0x0 -> mov r0, 0x0
            arm9Patches.add(new PatchElement(0x00111e10, 0x0a000013, 0xe58d0018)); // beq ... -> str r0, [sp, local_50]
            arm9Patches.add(new PatchElement(0x00111e20, 0xe3a00000, 0xe1a00000)); // mov r0, 0x0 -> nop
            arm9Patches.add(new PatchElement(0x00111e48, 0xe58d0018, 0xe3a00001)); // str r0, [sp, local_50] -> mov r0, 0x1

            // LOWER BOUNDARY (replace useless code)
            arm9Patches.add(new PatchElement(0x000b9a04, 0xea00000c, 0xea000015)); // b LAB_020b9a3c -> LAB_020b9a60
            arm9Patches.add(new PatchElement(0x000b9a08, 0xea00000c, 0xea000014)); // b LAB_020b9a48 -> LAB_020b9a60
            arm9Patches.add(new PatchElement(0x000b9a0c, 0xea00000c, 0xea000013)); // b LAB_020b9a54 -> LAB_020b9a60
            // move/jump
            arm9Patches.add(new PatchElement(0x000b9d08, 0xe1570006, 0xeaffff4f)); // cmp r7, r6 -> b 020b9a3c
            arm9Patches.add(new PatchElement(0x000b9a3c, 0xe59d0004, 0xe3570001)); // ... -> cmp r7, 0x1
            arm9Patches.add(new PatchElement(0x000b9a40, 0xe2606ffa, 0x33a07001)); // ... -> movlo r7, 0x1
            arm9Patches.add(new PatchElement(0x000b9a44, 0xea000007, 0xe1570006)); // ... -> cmp r7, r6
            arm9Patches.add(new PatchElement(0x000b9a48, 0xe59d0004, 0xea0000b3)); // ... -> b 020b9d0c
            // evasion
            arm9Patches.add(new PatchElement(0x000b9ce4, 0xe1570006, 0xeaffff58)); // cmp r7, r6 -> b 020b9a4c
            arm9Patches.add(new PatchElement(0x000b9a4c, 0xe2606ffa, 0xe3570000)); // ... -> cmp r7, 0x0
            arm9Patches.add(new PatchElement(0x000b9a50, 0xea000004, 0x33a07000)); // ... -> movlo r7, 0x0
            arm9Patches.add(new PatchElement(0x000b9a54, 0xe59d0004, 0xe1570006)); // ... -> cmp r7, r6
            arm9Patches.add(new PatchElement(0x000b9a58, 0xe2606ffa, 0xea0000a6)); // ... -> b 020b9ce8

            
            // The value is already flipped
            boolean newValue = patchedSignedEquipmentStats.getValue();
            applyPatchElements(arm9Patches, App.arm9, newValue);
            applyPatchElements(overlay11Patches, App.overlay11, newValue);
            String alertText = newValue ? "Patch applied" : "Patch removed";
            
            Alert loadAlert = new Alert(AlertType.INFORMATION);
            loadAlert.setTitle("Signed Equipment Stats patch");
            loadAlert.setHeaderText(alertText);
            loadAlert.show();
        }
    }

    @FXML 
    public void applyStartingMpPatch() {
        if (App.archive != null) {
            List<PatchElement> arm9Patches = new ArrayList<>();

            arm9Patches.add(new PatchElement(0x000b9180, 0xe3a01000, 0xe1c403b8)); // mov r1, 0x0 -> strh r0, [r4, 0x38]
            arm9Patches.add(new PatchElement(0x000b918c, 0xe1c413b8, 0xe3a01000)); // strh r1, [r4, 0x38] -> mov r1, 0x0
            arm9Patches.add(new PatchElement(0x000bac44, 0xe3a00000, 0xe1d503ba)); // mov r0, 0x0 -> ldrh r0, [r5, 0x3A]
            
            // The value is already flipped
            boolean newValue = patchedStartingMp.getValue();
            applyPatchElements(arm9Patches, App.arm9, newValue);
            String alertText = newValue ? "Patch applied" : "Patch removed";

            Alert loadAlert = new Alert(AlertType.INFORMATION);
            loadAlert.setTitle("Max Starting MP patch");
            loadAlert.setHeaderText(alertText);
            loadAlert.show();
        }
    }

    @FXML
    public void applyMaxLevelPatch() {
        if (App.archive != null) {
            
            TextInputDialog dialog = new TextInputDialog(maxLevel.getValue().toString());
            dialog.setTitle("Max Level patch");
            var result = dialog.showAndWait();
            if (!result.isPresent()) return;
            
            int newLevel;
            try {
                newLevel = Integer.parseInt(result.get());
                if (newLevel < 1 || newLevel > 127) throw new Exception("Value must be between 1 and 127");
            } catch (Exception e) {
                Alert loadAlert = new Alert(AlertType.ERROR);
                loadAlert.setTitle("Max Level patch");
                loadAlert.setHeaderText(e.toString());
                loadAlert.show();
                return;
            }
            maxLevel.set(newLevel);

            App.arm9.put(0x000b9094, (byte)newLevel);
            App.arm9.put(0x000b909c, (byte)newLevel);

            
            String alertText = String.format("Max level set to %d", newLevel);

            Alert loadAlert = new Alert(AlertType.INFORMATION);
            loadAlert.setTitle("Max Level patch");
            loadAlert.setHeaderText(alertText);
            loadAlert.show();
        }

    }

    @FXML
    public void applyMPGainPatch() {
        if (App.archive != null) {
            
            TextInputDialog dialog = new TextInputDialog(Integer.toString(10));
            dialog.setTitle("MP gain");
            dialog.setHeaderText("Flat MP per turn");
            var result = dialog.showAndWait();
            if (!result.isPresent()) return;
            
            int flatRegen;
            try {
                flatRegen = Integer.parseInt(result.get());
                if (flatRegen < 0) throw new Exception("Value must be 0 or higher");
            } catch (Exception e) {
                Alert loadAlert = new Alert(AlertType.ERROR);
                loadAlert.setTitle("MP gain patch");
                loadAlert.setHeaderText(e.toString());
                loadAlert.show();
                return;
            }

            dialog.setHeaderText("%Max MP per turn");
            result = dialog.showAndWait();
            if (!result.isPresent()) return;
            
            int percentage;
            try {
                percentage = Integer.parseInt(result.get());
                if (percentage < 0 || 100 < percentage) throw new Exception("Value must be between 0 and 100");
            } catch (Exception e) {
                Alert loadAlert = new Alert(AlertType.ERROR);
                loadAlert.setTitle("MP gain patch");
                loadAlert.setHeaderText(e.toString());
                loadAlert.show();
                return;
            }
            int divisor = percentage != 0 ? 100 / percentage : 99999;


            ByteBuffer btlprocessFile = App.archive.getFile("battle/btlprocess.sbn");
            SBN btlprocess = new SBN(btlprocessFile.rewind());

            int insertion_point = 0x160c;
            int i = 0;
            for (; i < btlprocess.commands.size(); i++) {
                if (btlprocess.commands.get(i).address == insertion_point) break;
            }

            ByteBuffer flatParams = ByteBuffer.allocate(6).order(ByteOrder.LITTLE_ENDIAN);
            flatParams.putShort((short)0);
            flatParams.putInt(flatRegen);
            ByteBuffer divisorParams = ByteBuffer.allocate(6).order(ByteOrder.LITTLE_ENDIAN);
            divisorParams.putShort((short)0);
            divisorParams.putInt(divisor);

            ArrayList<Command> newCommands = new ArrayList<>();
            newCommands.add(new Command(0, (byte)0x0, (byte)0x8, flatParams.array()));
            newCommands.add(new Command(0, (byte)0x2, (byte)0x8, new byte[]{0x00, 0x00, (byte)0xfe, (byte)0xff, 0x01, 0x00}));
            newCommands.add(new Command(0, (byte)0x1c, (byte)0x4, new byte[]{0x01, 0x00}));
            newCommands.add(new Command(0, (byte)0x32, (byte)0x4, new byte[]{(byte)0xff, (byte)0xff}));
            newCommands.add(new Command(0, (byte)0x31, (byte)0x4, new byte[]{0x18, 0x06}));
            newCommands.add(new Command(0, (byte)0x1c, (byte)0x4, new byte[]{(byte)0xff, (byte)0xff}));
            newCommands.add(new Command(0, (byte)0x2b, (byte)0x4, new byte[]{0x00, 0x00}));
            newCommands.add(new Command(0, (byte)0x0, (byte)0x8, divisorParams.array()));
            newCommands.add(new Command(0, (byte)0x9, (byte)0x2, new byte[]{}));
            newCommands.add(new Command(0, (byte)0x6, (byte)0x2, new byte[]{}));

            int additionalBytes = newCommands.stream().mapToInt(x -> x.size).sum();
            newCommands.add(new Command(0, (byte)0x2d, (byte)0x10, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));
            

            if (btlprocess.commands.get(i).opcode == 0x30) {
                // Already applied
                System.out.println("Already applied");
                ByteBuffer params = ByteBuffer.wrap(btlprocess.commands.get(i).parameters).order(ByteOrder.LITTLE_ENDIAN);
                params.getShort();
                int jumpAddress = params.getInt();
                for (; i < btlprocess.commands.size(); i++) {
                    if (btlprocess.commands.get(i).address == insertion_point+jumpAddress) break;
                }
                int j = 0;
                for (; i < btlprocess.commands.size(); i++, j++) {
                    btlprocess.commands.set(i, newCommands.get(j));
                }
                if (i < btlprocess.commands.size() || j < newCommands.size()) {
                    System.err.println(String.format("Sizes don't match: %d < %d or %d < %d", i, btlprocess.commands.size(), j, newCommands.size()));
                }
            } else {
                System.out.println("Not applied yet");
                for (Command c : newCommands) {
                    btlprocess.commands.add(c);
                }
    
                ByteBuffer params = ByteBuffer.allocate(6).order(ByteOrder.LITTLE_ENDIAN);
                params.putShort((short)0);
                params.putInt(btlprocess.endAddress - insertion_point);
                //btlprocess.commands.set(i, new Command(0, (byte)0x30, (byte)0x8, params.array()));
                btlprocess.commands.get(i).opcode = 0x30;
                btlprocess.commands.get(i).size = 0x8;
                btlprocess.commands.get(i).parameters = params.array();
            }
            

            ByteBuffer newBtlprocess = btlprocess.toByteBuffer();

            App.archive.setFile("battle/btlprocess.sbn", newBtlprocess);

            
            String alertText = String.format("MP gain set to %d + %d%% Max MP per turn", flatRegen, percentage);
            Alert loadAlert = new Alert(AlertType.INFORMATION);
            loadAlert.setTitle("MP gain patch");
            loadAlert.setHeaderText(alertText);
            loadAlert.show();
        }

    }

    @FXML 
    public void applySequencerPeytralPatch() {
        if (App.archive != null) {
            List<PatchElement> arm9Patches = new ArrayList<>();
            List<PatchElement> overlay11Patches = new ArrayList<>();

            overlay11Patches.add(new PatchElement(0x0000e26c, 0xe350003f, 0xea000003)); // cmp r0, 0x3f -> b LAB_overlay_11__02141240
            arm9Patches.add(new PatchElement(0x000b9b40, 0xe358003f, 0xea000003)); // cmp r8, 0x3f -> b LAB_020b9b54
            arm9Patches.add(new PatchElement(0x00111050, 0xe350003f, 0xea000005)); // cmp r0, 0x3f -> b LAB_0211106c
            arm9Patches.add(new PatchElement(0x00111cac, 0xe350003f, 0xea000005)); // cmp r0, 0x3f -> b LAB_02111cc8

            overlay11Patches.add(new PatchElement(0x0000e2fc, 0xe1510000, 0xea000003)); // cmp r1, r0 -> b LAB_overlay_11__021412d0
            arm9Patches.add(new PatchElement(0x000b9b84, 0xe1580000, 0xea000003)); // cmp r8, r0 -> b LAB_020b9b98
            arm9Patches.add(new PatchElement(0x001110c8, 0xe1510000, 0xea000005)); // cmp r1, r0 -> b LAB_021110e4
            arm9Patches.add(new PatchElement(0x00111d34, 0xe1510000, 0xea000005)); // cmp r1, r0 -> b LAB_021110e4
            
            boolean newValue = patchedSequencerPeytral.getValue();
            applyPatchElements(arm9Patches, App.arm9, newValue);
            applyPatchElements(overlay11Patches, App.overlay11, newValue);
            String alertText = newValue ? "Patch applied" : "Patch removed";

            Alert loadAlert = new Alert(AlertType.INFORMATION);
            loadAlert.setTitle("Sequencer / Peytral stat growth patch");
            loadAlert.setHeaderText(alertText);
            loadAlert.show();
        }
    }

    public void loadPatches() {
        patchedExpandedTopSprites.set(App.arm9.getInt(0x000b5ab4) != 0xe5d00018);
        patchedSignedEquipmentStats.set(App.arm9.getInt(0x000cfcd8) != 0xe5d01017);
        patchedStartingMp.set(App.arm9.getInt(0x000b9180) != 0xe3a01000);
        patchedSequencerPeytral.set(App.arm9.getInt(0x000b9b40) != 0xe358003f);
        maxLevel.set(App.arm9.get(0x000b9094));
        
        
        expandedTopSprites.selectedProperty().bindBidirectional(patchedExpandedTopSprites);
        signedEquipmentStats.selectedProperty().bindBidirectional(patchedSignedEquipmentStats);
        startingMp.selectedProperty().bindBidirectional(patchedStartingMp);
        sequencerPeytral.selectedProperty().bindBidirectional(patchedSequencerPeytral);
    }
    
    public void applyPatches() {
        //if (animationFix.selectedProperty().getValue()) {
        //    applyAnimationFix();
        //}
    }
}