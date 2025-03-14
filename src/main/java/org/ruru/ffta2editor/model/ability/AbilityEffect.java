package org.ruru.ffta2editor.model.ability;

import javafx.beans.property.SimpleObjectProperty;

public class AbilityEffect {
    public enum Targets {
        NONE(0),
        CONTROLLABLE_MONSTER(0x01),
        TRAP(0x02),
        BLADED_USER(0x04),
        ALLIES_FOES(0x06),
        EMPTY_TILE(0x08),
        RESURRECTED(0x09),
        SELF(0x0A),
        ALLY(0x0B),
        ONLY_ALLIES(0x0C),
        FAWN_ASTRA(0x0E),
        ALL(0x0F),
        FEMALE_1(0x10),
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
        ALLIES_PHOENIX_ELPE(0x2A);

        public final byte value;

        Targets(int value) {
            this.value = (byte)value;
        }

        private static Targets[] values = Targets.values();
        public static Targets fromInteger(int value) {
            for (Targets e : values) {
                if (e.value == value) {
                    return e;
                }
            }
            return Targets.NONE;
        }

        public static Targets fromString(String string) {
            return valueOf(string);
        }
    }

    public enum Effect {
        NONE(0),
        HP_DAMAGE(0x01),
        MP_DAMAGE(0x03),
        KNOCKBACK(0x05),
        TARGET(0x07),
        SWITCH_HP_AND_MP(0x08),
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
        REMOVE_ALL_DEBUFFS(0x1B),
        REMOVE_MOST_DEBUFFS(0x1C),
        REMOVE_SOME_DEBUFFS(0x1D),
        REMOVE_ALL_BUFFS_AND_DEBUFFS(0x1F),
        RECOVER_ALL_HP_AND_MP_AND_REMOVE_ALL_DEBUFFS(0x21),
        REMOVE_BUFFS(0x23),
        RECOVER_1_HP(0x24),
        RECOVER_10_PERCENT_HP(0x25),
        RECOVER_ALL_HP_AND_MP(0x27),
        SLEEP_AND_REMOVE_ALL_DEBUFFS(0x2B),
        DEFEND(0x2C),
        INVISIBLE(0x2D),
        CRITICAL_UP(0x2E),
        FOCUS(0x30),
        SHELL(0x31),
        PROTECT(0x33),
        RERAISE(0x35),
        REFLECT(0x36),
        BERSERK(0x37),
        ASTRA(0x38),
        REGEN(0x39),
        HASTE(0x3A),
        NO_DAMAGE(0x3D),
        RANDOM_BUFF(0x3E),
        POISON(0x40),
        STONE(0x41),
        SILENCE(0x42),
        CONFUSE(0x43),
        SLEEP(0x44),
        BLIND(0x45),
        TOAD(0x47),
        OIL(0x48),
        ADDLE(0x49),
        SLOW(0x4A),
        IMMOBILIZE(0x4C),
        DISABLE(0x4D),
        CHARM(0x4E),
        DOOM(0x4F),
        STOP(0x50),
        RANDOM_DEBUFF_1(0x52),
        SLOW_PETRIFY_DISABLE_IMMOBILIZE_STOP(0x56),
        RANDOM_DEBUFF_2(0x57),
        PETRIFY_IMMOBILIZE_SLEEP(0x58),
        ATTACK_UP(0x59),
        DEFENSE_UP(0x5A),
        MAGICK_UP(0x5B),
        RESISTANCE_UP(0x5C),
        RESILIENCE_UP(0x5D),
        MOVE_UP(0x60),
        EVASION_UP(0x62),
        FORESIGHT(0x64),
        BUCKSHOT(0x65),
        SCOPE(0x66),
        PRIME1(0x67),
        ACCURACY_UP(0x6B),
        ATTACK_DOWN(0x6C),
        DEFENSE_DOWN(0x6D),
        RESISTANCE_DOWN(0x6E),
        MAGICK_DOWN(0x6F),
        RESILIENCE_DOWN(0x70),
        SPEED_DOWN(0x71),
        STATS_UP(0x72),
        DESTROY(0x8F),
        STEAL(0x90),
        STEAL_SKY_PIRATE(0x91),
        REVEAL_LOOT_AND_ITEMS(0x93),
        TRANSFORM_INTO_ITEM(0x94),
        GAUGE(0x95),
        TRAP(0x96),
        REVEAL_TRAPS(0x97),
        DELAY_TURN(0x99),
        QUICKEN(0x9B),
        NULLIFY_EVASION__1_TURN(0x9C),
        IMMOBILE_NEXT_TURN(0x9E),
        PRIME2(0x9F),
        REVEAL_UNITS(0xA0),
        EXTEND_BUFFS_AND_DEBUFFS(0xA1),
        COVER(0xA2),
        RANDOM_TELEPORT(0xA7),
        YOWIE_TELEPORT(0xA8),
        DEATH_WALL_TELEPORT(0xA9),
        ASK_FOR_ELIXIR(0xAA),
        KILL_SELF(0xAB),
        ATTACK_AGAIN(0xAE),
        RESTORE_NEUKHIA_WISP_HP(0xB1),
        RELIGHT_NEUKHIA_STONES(0xB2);
        

        public final byte value;

        Effect(int value) {
            this.value = (byte)value;
        }

        private static Effect[] values = Effect.values();
        public static Effect fromInteger(int value) {
            for (Effect e : values) {
                if (e.value == value) {
                    return e;
                }
            }
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
        QUARTER_NORMAL(0x06),
        NORMAL_DEBUFF(0x07),
        PERFECT(0x09),
        ON_BUFFED_DEBUFFED(0x0A),
        PERFECT_NORMAL(0x0B),
        PERFECT_ON_HIT(0x0C),
        PERFECT_DEBUFF(0x0E),
        NORMAL_DEBUFF_ON_HIT(0x0F),
        HALF_DEBUFF_ON_HIT(0x10),
        QUARTER_DEBUFF_ON_HIT(0x17),
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
        DESTRUCTION(0x27),
        SUMMON(0x2B),
        REMOVE(0x2D),
        CONTROL(0x2F),
        PERFECT_ON_KILL(0x38);
        
        public final byte value;

        Accuracy(int value) {
            this.value = (byte)value;
        }
        
        private static Accuracy[] values = Accuracy.values();
        public static Accuracy fromInteger(int value) {
            for (Accuracy e : values) {
                if (e.value == value) {
                    return e;
                }
            }
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
        THROWN_ITEM_DAMAGE(0x0A),
        TOTAL_USER_DEBUFFS(0x0D),
        BACK_SIDE_ATTACK(0x0E),
        MAX_HP_MINUS_CURRENT_HP(0x0F),
        TOTAL_FEMALE_ALLIES(0x11),
        UNDEAD_DAMAGE(0x13),
        FIXED_BONUS_ONLY(0x15),
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
        MAX_USER_HP(0x29),
        TOTAL_KILLS_X10(0x2B),
        MAX_HP_MINUS_1(0x2E),
        _999(0x30),
        NORMAL_DAMAGE_DEALT(0x31),
        NORMAL_DAMAGE_DEALT_DRAW_IN(0x32),
        QUARTER_DAMAGE_DEALT(0x33),
        PREVIOUS_HP(0x36),
        PREVIOUS_MP(0x37),
        TOTAL_TARGET_DEBUFFS(0x38),
        QUARTER_MAX_HP_MINUS_CURRENT_HP(0x3D),
        _40(0x3E),
        GIL(0x42),
        TOTAL_THEFTS(0x45),
        STEAL_SMASH_GAUGE(0x46),
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
        FIXED(0x60),
        STEAL_LOOT_GIL_ITEMS(0x63);
        
        public final byte value;

        Modifier(int value) {
            this.value = (byte)value;
        }
        
        private static Modifier[] values = Modifier.values();
        public static Modifier fromInteger(int value) {
            for (Modifier e : values) {
                if (e.value == value) {
                    return e;
                }
            }
            return Modifier.NONE;
        }
        
        public static Modifier fromString(String string) {
            return valueOf(string);
        }
    }

    //Targets targets;
    //Effect effect;
    //Accuracy accuracy;
    //Modifier modifier;

    public SimpleObjectProperty<Targets> targetsProperty = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Effect> effectProperty = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Accuracy> accuracyProperty = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Modifier> modifierProperty = new SimpleObjectProperty<>();

    public AbilityEffect(Targets targets, Effect effect, Accuracy accuracy, Modifier modifier) {
        targetsProperty.setValue(targets);
        effectProperty.setValue(effect);
        accuracyProperty.setValue(accuracy);
        modifierProperty.setValue(modifier);
        ///this.targets = targets;
        ///this.effect = effect;
        ///this.accuracy = accuracy;
        ///this.modifier = modifier;
    }

    public byte[] toBytes() {
        byte[] bytes = {((Targets)targetsProperty.getValue()).value, ((Effect)effectProperty.getValue()).value, ((Accuracy)accuracyProperty.getValue()).value, ((Modifier)modifierProperty.getValue()).value};
        return bytes;
    }

}
