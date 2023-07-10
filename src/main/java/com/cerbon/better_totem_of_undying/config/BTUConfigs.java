package com.cerbon.better_totem_of_undying.config;

import com.cerbon.better_totem_of_undying.util.BTUConstants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

@Config(name = BTUConstants.MOD_ID)
public class BTUConfigs implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    MinecraftDefaultTotemFeatures minecraftDefaultTotemFeatures = new MinecraftDefaultTotemFeatures();

    @ConfigEntry.Gui.CollapsibleObject
    NewTotemFeatures newTotemFeatures = new NewTotemFeatures();

    @ConfigEntry.Gui.CollapsibleObject
    Trinkets trinkets = new Trinkets();

    @ConfigEntry.Gui.CollapsibleObject
    Blacklists blacklists = new Blacklists();

    static class MinecraftDefaultTotemFeatures {

        @ConfigEntry.Gui.CollapsibleObject
        Effects effects = new Effects();

        @Comment("This value sets the health Totem of Undying will give to the entity upon use. DEFAULT: 1")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int SET_HEALTH = 1;

        @Comment("When Totem of Undying is used it removes all previous effects you had. If set to false, it will keep all the effects you had before using the totem. DEFAULT: TRUE")
        public boolean REMOVE_ALL_EFFECTS = true;

        static class Effects{

            @Comment("If false Totem of Undying will not give you fire resistance effect. DEFAULT: TRUE")
            public boolean ENABLE_FIRE_RESISTANCE = true;

            @Comment("Sets the duration of the fire resistance effect in ticks. DEFAULT: 800")
            public int FIRE_RESISTANCE_DURATION = 800;

            @Comment("If false Totem of Undying will not give you regeneration effect. DEFAULT: TRUE")
            public boolean ENABLE_REGENERATION = true;

            @Comment("Sets the duration of the regeneration effect in ticks. DEFAULT: 900")
            public int REGENERATION_DURATION = 900;

            @Comment("Sets the amplifier of the regeneration effect. DEFAULT: 1")
            @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
            public int REGENERATION_AMPLIFIER = 1;

            @Comment("If false Totem of Undying will not give you absorption effect. DEFAULT: TRUE")
            public boolean ENABLE_ABSORPTION = true;

            @Comment("Sets the duration of the absorption effect in ticks. DEFAULT: 100")
            public int ABSORPTION_DURATION  = 100;

            @Comment("Sets the amplifier of the absorption effect. DEFAULT: 1")
            @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
            public int ABSORPTION_AMPLIFIER  = 1;
        }
    }

    static class NewTotemFeatures{

        @ConfigEntry.Gui.CollapsibleObject
        Effects effects = new Effects();

        @ConfigEntry.Gui.CollapsibleObject
        IncreaseFoodLevel increaseFoodLevel = new IncreaseFoodLevel();

        @ConfigEntry.Gui.CollapsibleObject
        DestroyBlocksWhenSuffocatingOrFullyFrozen destroyBlocksWhenSuffocatingOrFullyFrozen = new DestroyBlocksWhenSuffocatingOrFullyFrozen();

        @ConfigEntry.Gui.CollapsibleObject
        KnockbackMobsAway knockbackMobsAway = new KnockbackMobsAway();

        @ConfigEntry.Gui.CollapsibleObject
        TeleportOutOfVoid teleportOutOfVoid = new TeleportOutOfVoid();

        @ConfigEntry.Gui.CollapsibleObject
        AddCooldown addCooldown = new AddCooldown();

        @Comment("If true you will be able to use the Totem of Undying from your inventory. DEFAULT: FALSE")
        public boolean USE_TOTEM_FROM_INVENTORY = false;

        @Comment("Apply fire resistance and water breathing effects only when needed, meaning you are going to receive the effects only if you are on fire or in water respectively. DEFAULT: TRUE")
        public boolean APPLY_EFFECTS_ONLY_WHEN_NEEDED = true;

        static class Effects{

            @Comment("If false Totem of Undying will not give you water breathing effect. DEFAULT: TRUE")
            public boolean ENABLE_WATER_BREATHING = true;

            @Comment("Sets the duration of the water breathing effect in ticks. DEFAULT: 800")
            public int WATER_BREATHING_DURATION = 800;

            @Comment("If false Totem of Undying will not give you slow falling effect. DEFAULT: TRUE")
            public boolean ENABLE_SLOW_FALLING = true;

            @Comment("Sets the duration of the slow falling effect in ticks. DEFAULT: 600")
            public int SLOW_FALLING_DURATION = 600;
        }

        static class IncreaseFoodLevel{

            @Comment("If false Totem of Undying will not increase your food level. DEFAULT: TRUE")
            public boolean ENABLE_INCREASE_FOOD_LEVEL = true;

            @Comment("Sets the minimum food level needed to Totem of Undying increase food level. DEFAULT: <= 6")
            public int MINIMUM_FOOD_LEVEL = 6;

            @Comment("Sets the food level that Totem of Undying will give upon use. DEFAULT: 8")
            public int SET_FOOD_LEVEL = 8;
        }

        static class DestroyBlocksWhenSuffocatingOrFullyFrozen{

            @Comment("If false Totem of Undying will not break blocks when you are suffocating. DEFAULT: TRUE")
            public boolean DESTROY_BLOCKS_WHEN_SUFFOCATING = true;

            @Comment("If false Totem of Undying will not break the powder snow when you die fully frozen. DEFAULT: TRUE")
            public boolean DESTROY_POWDER_SNOW_WHEN_FULLY_FROZEN = true;
        }

        static class KnockbackMobsAway{

            @Comment("If false Totem of Undying will not knockback mobs away. DEFAULT: TRUE")
            public boolean KNOCKBACK_MOBS_AWAY = true;

            @Comment("Sets the radius where entities needs to be for the totem knockback them. DEFAULT: 3.0")
            public double KNOCKBACK_RADIUS = 3.0D;

            @Comment("Sets the strength of the knockback. DEFAULT: 2.5")
            public double KNOCKBACK_STRENGTH = 2.5D;
        }

        static class TeleportOutOfVoid{

            @Comment("If false Totem of Undying will not save you from dying in the void. DEFAULT: TRUE")
            public boolean TELEPORT_OUT_OF_VOID = true;

            @Comment("If totem can't find a available position to teleport you back it will teleport you to the world's max build height plus this offset. DEFAULT:64")
            @ConfigEntry.BoundedDiscrete(min = 0, max = 1024)
            public int TELEPORT_HEIGHT_OFFSET = 64;
        }

        static class AddCooldown{

            @Comment("If true Totem of Undying will receive a cooldown after being used and you will not be able to use it again during this period. DEFAULT: FALSE")
            public boolean ADD_COOLDOWN = true;

            @Comment("Sets the cooldown duration in ticks. DEFAULT: 200")
            public int COOLDOWN = 200;
        }
    }

    static class Trinkets{

        @Comment("If false you will not be able to use Totem of Undying from trinkets slot (Trinkets mod must be installed). DEFAULT: TRUE")
        public boolean USE_TOTEM_FROM_TRINKETS_SLOT = true;

        @Comment("If false Totem of Undying will not be displayed on the chest when worn in the trinkets charm slot (Trinkets mod must be installed). DEFAULT: TRUE")
        public boolean DISPLAY_TOTEM_ON_CHEST = true;
    }

    static class Blacklists{

        @Comment("You can put here dimensions where you don't want Totem of Undying to work. Example: \"minecraft:overworld\", \"mod_id:dimension_id\" DEFAULT: Nothing")
        public List<String> BLACKLISTED_DIMENSIONS = new ArrayList<>();

        @Comment("You can put here structures where you don't want Totem of Undying to work. Example: \"minecraft:desert_pyramid\", \"mod_id:structure_id\" DEFAULT: Nothing")
        public List<String> BLACKLISTED_STRUCTURES = new ArrayList<>();
    }
}
