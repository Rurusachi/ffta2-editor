package org.ruru.ffta2editor.model.ability;

import javafx.beans.property.SimpleObjectProperty;

public class AbilityEffect {
    public enum Targets {
        NONE(0),
        CONTROLLABLE_MONSTER(0x01),
        TRAP(0x02),
        _0x03(0x03),
        BLADED_USER(0x04),
        _0x05(0x05),
        ALLIES_FOES(0x06),
        _0x07(0x07),
        EMPTY_TILE(0x08),
        RESURRECTED(0x09),
        SELF(0x0A),
        ALLY(0x0B),
        ONLY_ALLIES(0x0C),
        _0x0D(0x0D),
        FAWN_ASTRA(0x0E),
        ALL(0x0F),
        FEMALE_1(0x10),
        _0x11(0x11),
        LV_QUESTION_OTHER(0x12),
        LV_QUESTION_SELF(0x13),
        LV_3_OTHER(0x14),
        LV_3_SELF(0x15),
        LV_5_ALL(0x16),
        LV_QUESTION_ALL(0x17),
        FOE(0x18),
        CRITICAL_FOE(0x19),
        ALL_ALLIES(0x1A),
        ALL_FOES(0x1B),
        MALE_FOE(0x1C),
        MALE_ALLY(0x1D),
        FEMALE_FOE(0x1E),
        FEMALE_ALLY(0x1F),
        EXCEPT_SELF(0x20),
        NON_UNDEAD(0x21),
        FEMALE_2(0x22),
        NON_MONSTER(0x23),
        NON_DRAKE(0x24),
        UNDEAD(0x25),
        KOD_UNDEAD(0x26),
        MONSTER(0x27),
        CRITICAL_DRAKE(0x28),
        NEUKHIA_STONE(0x29),
        ALLIES_PHOENIX_ELPE(0x2A),
        _0x2B(0x2B),
        _0x2C(0x2C),
        _0x2D(0x2D),
        _0x2E(0x2E),
        _0x2F(0x2F),
        _0x30(0x30),
        _0x31(0x31),
        _0x32(0x32),
        _0x33(0x33),
        _0x34(0x34),
        _0x35(0x35),
        _0x36(0x36),
        _0x37(0x37),
        _0x38(0x38),
        _0x39(0x39),
        _0x3A(0x3A),
        _0x3B(0x3B),
        _0x3C(0x3C),
        _0x3D(0x3D),
        _0x3E(0x3E),
        _0x3F(0x3F),
        _0x40(0x40),
        _0x41(0x41),
        _0x42(0x42),
        _0x43(0x43),
        _0x44(0x44),
        _0x45(0x45),
        _0x46(0x46),
        _0x47(0x47),
        _0x48(0x48),
        _0x49(0x49),
        _0x4A(0x4A),
        _0x4B(0x4B),
        _0x4C(0x4C),
        _0x4D(0x4D),
        _0x4E(0x4E),
        _0x4F(0x4F),
        _0x50(0x50),
        _0x51(0x51),
        _0x52(0x52),
        _0x53(0x53),
        _0x54(0x54),
        _0x55(0x55),
        _0x56(0x56),
        _0x57(0x57),
        _0x58(0x58),
        _0x59(0x59),
        _0x5A(0x5A),
        _0x5B(0x5B),
        _0x5C(0x5C),
        _0x5D(0x5D),
        _0x5E(0x5E),
        _0x5F(0x5F),
        _0x60(0x60),
        _0x61(0x61),
        _0x62(0x62),
        _0x63(0x63),
        _0x64(0x64),
        _0x65(0x65);

        public final byte value;

        Targets(int value) {
            this.value = (byte)value;
        }

        private static Targets[] values = Targets.values();

        public static Targets fromInteger(byte value) {
            return fromInteger(Byte.toUnsignedInt(value));
        }

        public static Targets fromInteger(short value) {
            return fromInteger(Short.toUnsignedInt(value));

        }

        public static Targets fromInteger(int value) {
            for (Targets e : values) {
                if (e.value == (byte)value) {
                    return e;
                }
            }
            System.err.println(String.format("Unknown Targets: %02X", value));
            return Targets.NONE;
        }

        public static Targets fromString(String string) {
            return valueOf(string);
        }
    }

    public enum Effect {
        NONE(0),
        HP_DAMAGE(0x01),
        _0x02(0x02),
        MP_DAMAGE(0x03),
        _0x04(0x04),
        KNOCKBACK(0x05),
        _0x06(0x06),
        TARGET(0x07),
        SWITCH_HP_AND_MP(0x08),
        _0x09(0x09),
        _0x0A(0x0A),
        HP_AND_MP_DAMAGE(0x0B),
        COUNTER(0x0C),
        KO(0x0D),
        REMOVE(0x0E),
        SUMMON(0x0F),
        STALK(0x10),
        RECOVER_HP(0x11),
        RECOVER_MP(0x12),
        REMOVE_POISON(0x13),
        REMOVE_STONE(0x14),
        REMOVE_SILENCE(0x15),
        REMOVE_BLIND(0x16),
        REMOVE_TOAD(0x17),
        REMOVE_OIL(0x18),
        REMOVE_DISABLE_AND_IMMOBILIZE(0x19),
        _0x1A(0x1A),
        REMOVE_ALL_DEBUFFS(0x1B),
        REMOVE_MOST_DEBUFFS(0x1C),
        REMOVE_SOME_DEBUFFS(0x1D),
        _0x1E(0x1E),
        REMOVE_ALL_BUFFS_AND_DEBUFFS(0x1F),
        _0x20(0x20),
        RECOVER_ALL_HP_AND_MP_AND_REMOVE_ALL_DEBUFFS(0x21),
        _0x22(0x22),
        REMOVE_BUFFS(0x23),
        RECOVER_1_HP(0x24),
        RECOVER_10_PERCENT_HP(0x25),
        _0x26(0x26),
        RECOVER_ALL_HP_AND_MP(0x27),
        _0x28(0x28),
        _0x29(0x29),
        _0x2A(0x2A),
        SLEEP_AND_REMOVE_ALL_DEBUFFS(0x2B),
        DEFEND(0x2C),
        INVISIBLE(0x2D),
        CRITICAL_UP(0x2E),
        _0x2F(0x2F),
        FOCUS(0x30),
        SHELL(0x31),
        _0x32(0x32),
        PROTECT(0x33),
        _0x34(0x34),
        RERAISE(0x35),
        REFLECT(0x36),
        BERSERK(0x37),
        ASTRA(0x38),
        REGEN(0x39),
        HASTE(0x3A),
        _0x3B(0x3B),
        _0x3C(0x3C),
        NO_DAMAGE(0x3D),
        RANDOM_BUFF(0x3E),
        _0x3F(0x3F),
        POISON(0x40),
        STONE(0x41),
        SILENCE(0x42),
        CONFUSE(0x43),
        SLEEP(0x44),
        BLIND(0x45),
        _0x46(0x46),
        TOAD(0x47),
        OIL(0x48),
        ADDLE(0x49),
        SLOW(0x4A),
        _0x4B(0x4B),
        IMMOBILIZE(0x4C),
        DISABLE(0x4D),
        CHARM(0x4E),
        DOOM(0x4F),
        STOP(0x50),
        _0x51(0x51),
        RANDOM_DEBUFF_1(0x52),
        _0x53(0x53),
        _0x54(0x54),
        _0x55(0x55),
        SLOW_PETRIFY_DISABLE_IMMOBILIZE_STOP(0x56),
        RANDOM_DEBUFF_2(0x57),
        PETRIFY_IMMOBILIZE_SLEEP(0x58),
        ATTACK_UP(0x59),
        DEFENSE_UP(0x5A),
        MAGICK_UP(0x5B),
        RESISTANCE_UP(0x5C),
        RESILIENCE_UP(0x5D),
        _0x5E(0x5E),
        _0x5F(0x5F),
        MOVE_UP(0x60),
        _0x61(0x61),
        EVASION_UP(0x62),
        _0x63(0x63),
        FORESIGHT(0x64),
        BUCKSHOT(0x65),
        SCOPE(0x66),
        PRIME_CANNONEER(0x67),
        _0x68(0x68),
        _0x69(0x69),
        _0x6A(0x6A),
        ACCURACY_UP(0x6B),
        ATTACK_DOWN(0x6C),
        DEFENSE_DOWN(0x6D),
        RESISTANCE_DOWN(0x6E),
        MAGICK_DOWN(0x6F),
        RESILIENCE_DOWN(0x70),
        SPEED_DOWN(0x71),
        STATS_UP(0x72),
        _0x73(0x73),
        _0x74(0x74),
        _0x75(0x75),
        _0x76(0x76),
        _0x77(0x77),
        _0x78(0x78),
        _0x79(0x79),
        _0x7A(0x7A),
        _0x7B(0x7B),
        _0x7C(0x7C),
        _0x7D(0x7D),
        _0x7E(0x7E),
        _0x7F(0x7F),
        _0x80(0x80),
        _0x81(0x81),
        _0x82(0x82),
        _0x83(0x83),
        _0x84(0x84),
        _0x85(0x85),
        _0x86(0x86),
        _0x87(0x87),
        _0x88(0x88),
        _0x89(0x89),
        _0x8A(0x8A),
        _0x8B(0x8B),
        _0x8C(0x8C),
        _0x8D(0x8D),
        _0x8E(0x8E),
        DESTROY(0x8F),
        STEAL(0x90),
        STEAL_SKY_PIRATE(0x91),
        _0x92(0x92),
        REVEAL_LOOT_AND_ITEMS(0x93),
        TRANSFORM_INTO_ITEM(0x94),
        GAUGE(0x95),
        TRAP(0x96),
        REVEAL_TRAPS(0x97),
        _0x98(0x98),
        DELAY_TURN(0x99),
        _0x9A(0x9A),
        QUICKEN(0x9B),
        NULLIFY_EVASION__1_TURN(0x9C),
        _0x9D(0x9D),
        IMMOBILE_NEXT_TURN(0x9E),
        PRIME_FLINTLOCK(0x9F),
        REVEAL_UNITS(0xA0),
        EXTEND_BUFFS_AND_DEBUFFS(0xA1),
        COVER(0xA2),
        _0xA3(0xA3),
        _0xA4(0xA4),
        _0xA5(0xA5),
        _0xA6(0xA6),
        RANDOM_TELEPORT(0xA7),
        YOWIE_TELEPORT(0xA8),
        DEATH_WALL_TELEPORT(0xA9),
        ASK_FOR_ELIXIR(0xAA),
        KILL_SELF(0xAB),
        _0xAC(0xAC),
        _0xAD(0xAD),
        ATTACK_AGAIN(0xAE),
        _0xAF(0xAF),
        _0xB0(0xB0),
        RESTORE_NEUKHIA_WISP_HP(0xB1),
        RELIGHT_NEUKHIA_STONES(0xB2);
        

        public final byte value;

        Effect(int value) {
            this.value = (byte)value;
        }

        private static Effect[] values = Effect.values();

        public static Effect fromInteger(byte value) {
            return fromInteger(Byte.toUnsignedInt(value));
        }

        public static Effect fromInteger(short value) {
            return fromInteger(Short.toUnsignedInt(value));

        }

        public static Effect fromInteger(int value) {
            for (Effect e : values) {
                if (e.value == (byte)value) {
                    return e;
                }
            }
            System.err.println(String.format("Unknown Effect: %02X", value));
            return Effect.NONE;
        }
        
        public static Effect fromString(String string) {
            return valueOf(string);
        }
    }

    public enum Accuracy {
        NONE(0),
        NORMAL(0x01),
        DOUBLE_NORMAL(0x02),
        HALF_NORMAL(0x03),
        _0x04(0x04),
        _0x05(0x05),
        QUARTER_NORMAL(0x06),
        NORMAL_DEBUFF(0x07),
        _0x08(0x08),
        PERFECT(0x09),
        ON_BUFFED_DEBUFFED(0x0A),
        PERFECT_NORMAL(0x0B),
        PERFECT_ON_HIT(0x0C),
        _0x0D(0x0D),
        PERFECT_DEBUFF(0x0E),
        NORMAL_DEBUFF_ON_HIT(0x0F),
        HALF_DEBUFF_ON_HIT(0x10),
        _0x11(0x11),
        _0x12(0x12),
        _0x13(0x13),
        _0x14(0x14),
        _0x15(0x15),
        _0x16(0x16),
        QUARTER_DEBUFF_ON_HIT(0x17),
        _0x18(0x18),
        PERFECT_AT_1_HP(0x19),
        STEAL_ON_ITEM_HOLDER(0x1A),
        STEAL_ON_LOOT_HOLDER(0x1B),
        STEAL_ON_ACCESSORY_HOLDER(0x1C),
        STEAL_ON_SMASH_GAUGE_HOLDER(0x1D),
        STEAL_ON_GIL_HOLDER(0x1E),
        PERFECT_ON_HIT_AND_ITEM1(0x1F),
        PERFECT_ON_HIT_AND_GIL_HOLDER(0x20),
        PERFECT_ON_HIT_AND_ARMOR_HOLDER(0x21),
        PERFECT_ON_HIT_AND_LOOT_GIL_ITEM_HOLDER(0x22),
        PERFECT_ON_HIT_AND_ITEM2(0x23),
        THROW(0x24),
        PERFECT_ON_EYE_CONTACT(0x25),
        _0x26(0x26),
        DESTRUCTION(0x27),
        _0x28(0x28),
        _0x29(0x29),
        _0x2A(0x2A),
        SUMMON(0x2B),
        _0x2C(0x2C),
        REMOVE(0x2D),
        _0x2E(0x2E),
        CONTROL(0x2F),
        _0x30(0x30),
        _0x31(0x31),
        _0x32(0x32),
        _0x33(0x33),
        _0x34(0x34),
        _0x35(0x35),
        _0x36(0x36),
        _0x37(0x37),
        PERFECT_ON_KILL(0x38);
        
        public final byte value;

        Accuracy(int value) {
            this.value = (byte)value;
        }
        
        private static Accuracy[] values = Accuracy.values();

        public static Accuracy fromInteger(byte value) {
            return fromInteger(Byte.toUnsignedInt(value));
        }

        public static Accuracy fromInteger(short value) {
            return fromInteger(Short.toUnsignedInt(value));

        }

        public static Accuracy fromInteger(int value) {
            for (Accuracy e : values) {
                if (e.value == (byte)value) {
                    return e;
                }
            }
            System.err.println(String.format("Unknown Accuracy: %02X", value));
            return Accuracy.NONE;
        }
        
        public static Accuracy fromString(String string) {
            return valueOf(string);
        }
    }

    public enum Modifier {
        NONE(0),
        NORMAL_DAMAGE(0x01),
        DOUBLE_DAMAGE(0x02),
        TRIPLE_DAMAGE(0x03),
        HALF_DAMAGE(0x04),
        QUARTER_DAMAGE(0x05),
        DRAIN_DAMAGE(0x06),
        BYPASS_DEFENSE(0x07),
        _0x08(0x08),
        _0x09(0x09),
        THROWN_ITEM_DAMAGE(0x0A),
        _0x0B(0x0B),
        _0x0C(0x0C),
        TOTAL_USER_DEBUFFS(0x0D),
        BACK_SIDE_ATTACK(0x0E),
        MAX_HP_MINUS_CURRENT_HP(0x0F),
        _0x10(0x10),
        TOTAL_FEMALE_ALLIES(0x11),
        _0x12(0x12),
        UNDEAD_DAMAGE(0x13),
        _0x14(0x14),
        FIXED_BONUS_ONLY(0x15),
        _0x16(0x16),
        MAX_HP(0x17),
        MAX_MP(0x18),
        HALF_MAX_HP(0x19),
        QUARTER_MAX_HP(0x1A),
        THREE_QUARTER_CURRENT_HP(0x1B),
        HALF_CURRENT_HP(0x1C),
        CURRENT_HP(0x1D),
        CURRENT_USER_HP(0x1E),
        _10_PERCENT_CURRENT_HP(0x1F),
        _20_PERCENT_CURRENT_HP(0x20),
        _90_PERCENT_CURRENT_HP(0x21),
        _0x22(0x22),
        _0x23(0x23),
        _0x24(0x24),
        _0x25(0x25),
        _0x26(0x26),
        _0x27(0x27),
        _0x28(0x28),
        MAX_USER_HP(0x29),
        LIFE_BOND(0x2A),
        TOTAL_KILLS_X10(0x2B),
        _0x2C(0x2C),
        _0x2D(0x2D),
        MAX_HP_MINUS_1(0x2E),
        _0x2F(0x2F),
        _999(0x30),
        NORMAL_DAMAGE_DEALT(0x31),
        NORMAL_DAMAGE_DEALT_DRAW_IN(0x32),
        QUARTER_DAMAGE_DEALT(0x33),
        _0x34(0x34),
        _0x35(0x35),
        PREVIOUS_HP(0x36),
        PREVIOUS_MP(0x37),
        TOTAL_TARGET_DEBUFFS(0x38),
        KNOT_OF_RUST(0x39),
        _0x3A(0x3A),
        DARK_MATTER(0x3B),
        _0x3C(0x3C),
        QUARTER_MAX_HP_MINUS_CURRENT_HP(0x3D),
        _40(0x3E),
        _0x3F(0x3F),
        _0x40(0x40),
        _0x41(0x41),
        GIL(0x42),
        _0x43(0x43),
        _0x44(0x44),
        TOTAL_THEFTS(0x45),
        STEAL_SMASH_GAUGE(0x46),
        _0x47(0x47),
        STEAL_ANY_LOOT(0x48),
        STEAL_LV_1_LOOT(0x49),
        STEAL_LV_2_LOOT(0x4A),
        STEAL_LV_3_LOOT(0x4B),
        STEAL_LV_4_LOOT(0x4C),
        DESTROY_WEAPON(0x4D),
        DESTROY_SHIELD(0x4E),
        DESTROY_HELMET(0x4F),
        DESTROY_ARMOUR(0x50),
        DESTROY_WEAPON_SHIELD_ARMOUR(0x51),
        STEAL_ACCESSORY(0x52),
        STEAL_ITEMS(0x53),
        STEAL_GIL(0x54),
        STEAL_2X_GIL(0x55),
        STEAL_ARMOUR(0x56),
        HP_TRAP(0x57),
        SILENCE_TRAP(0x58),
        MP_TRAP(0x59),
        CHARM_TRAP(0x5A),
        _0x5B(0x5B),
        _0x5C(0x5C),
        _0x5D(0x5D),
        _0x5E(0x5E),
        _0x5F(0x5F),
        FIXED(0x60),
        _0x61(0x61),
        _0x62(0x62),
        STEAL_LOOT_GIL_ITEMS(0x63);
        
        public final byte value;

        Modifier(int value) {
            this.value = (byte)value;
        }
        
        private static Modifier[] values = Modifier.values();

        public static Modifier fromInteger(byte value) {
            return fromInteger(Byte.toUnsignedInt(value));
        }

        public static Modifier fromInteger(short value) {
            return fromInteger(Short.toUnsignedInt(value));

        }

        public static Modifier fromInteger(int value) {
            for (Modifier e : values) {
                if (e.value == (byte)value) {
                    return e;
                }
            }
            System.err.println(String.format("Unknown Modifier: %02X", value));
            return Modifier.NONE;
        }
        
        public static Modifier fromString(String string) {
            return valueOf(string);
        }
    }

    public SimpleObjectProperty<Targets> targetsProperty = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Effect> effectProperty = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Accuracy> accuracyProperty = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Modifier> modifierProperty = new SimpleObjectProperty<>();

    public AbilityEffect(Targets targets, Effect effect, Accuracy accuracy, Modifier modifier) {
        targetsProperty.setValue(targets);
        effectProperty.setValue(effect);
        accuracyProperty.setValue(accuracy);
        modifierProperty.setValue(modifier);
    }

    public void copyFrom(AbilityEffect source) {
        targetsProperty.set(source.targetsProperty.get());
        effectProperty.set(source.effectProperty.get());
        accuracyProperty.set(source.accuracyProperty.get());
        modifierProperty.set(source.modifierProperty.get());
    }

    public byte[] toBytes() {
        byte[] bytes = {((Targets)targetsProperty.getValue()).value, ((Effect)effectProperty.getValue()).value, ((Accuracy)accuracyProperty.getValue()).value, ((Modifier)modifierProperty.getValue()).value};
        return bytes;
    }

}
