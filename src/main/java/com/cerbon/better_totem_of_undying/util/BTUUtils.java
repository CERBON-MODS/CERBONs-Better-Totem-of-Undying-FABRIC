package com.cerbon.better_totem_of_undying.util;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class BTUUtils {

    public static boolean isModLoaded(String mod_id){
       return FabricLoader.getInstance().isModLoaded(mod_id);
    }

    public static boolean canSaveFromDeath(@NotNull LivingEntity livingEntity, DamageSource damageSource){
        boolean isTotemOnCooldown = livingEntity instanceof ServerPlayerEntity player && player.getItemCooldownManager().isCoolingDown(Items.TOTEM_OF_UNDYING);
        boolean isTeleportOutOfVoidEnabled = BetterTotemOfUndying.CONFIG.TELEPORT_OUT_OF_VOID;
        BlockPos entityPos = livingEntity.getBlockPos();
        World world = livingEntity.getWorld();

        if (isDimensionBlacklisted(world) || isStructureBlacklisted(entityPos, (ServerWorld) world)  || damageBypassInvulnerability(damageSource, livingEntity) || (!isTeleportOutOfVoidEnabled && isInVoid(livingEntity, damageSource)) || isTotemOnCooldown){
            return false;
        }else {
            ItemStack itemStack = getTotemItemStack(livingEntity);

            if (itemStack != null){
                if (livingEntity instanceof ServerPlayerEntity player){
                    giveUseStatAndCriterion(itemStack, player);
                    addCooldown(itemStack, player, BetterTotemOfUndying.CONFIG.COOLDOWN);
                }

                itemStack.decrement(1);

                if (BetterTotemOfUndying.CONFIG.REMOVE_ALL_EFFECTS)
                    livingEntity.clearStatusEffects();

                livingEntity.setHealth(BetterTotemOfUndying.CONFIG.SET_HEALTH);

                applyTotemEffects(livingEntity);
                increaseFoodLevel(livingEntity, BetterTotemOfUndying.CONFIG.SET_FOOD_LEVEL);
                destroyBlocksWhenSuffocatingOrFullyFrozen(livingEntity, world);
                knockbackMobsAway(livingEntity, world);
                teleportOutOfVoid(livingEntity, world, damageSource);

                livingEntity.getWorld().sendEntityStatus(livingEntity, EntityStatuses.USE_TOTEM_OF_UNDYING);
            }
            return itemStack != null;
        }
    }

    public static boolean isDimensionBlacklisted(@NotNull World world){
        return BetterTotemOfUndying.CONFIG.BLACKLISTED_DIMENSIONS.contains(world.getRegistryKey().getValue().toString());
    }

    public static boolean isStructureBlacklisted(BlockPos pos, @NotNull ServerWorld world){
        List<String> blacklistedStructures = BetterTotemOfUndying.CONFIG.BLACKLISTED_STRUCTURES;
        Registry<ConfiguredStructureFeature<?, ?>> structureRegistry = world.getStructureAccessor().method_41036().get(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY);

        boolean flag = false;
        for (String structureName : blacklistedStructures){
            ConfiguredStructureFeature<?, ?> structure = structureRegistry.get(new Identifier(structureName));

            if (structure != null){
                if (world.getStructureAccessor().getStructureAt(pos, structure).hasChildren()){
                    flag = true;
                }
            }
        }
        return flag;
    }

    public static boolean damageBypassInvulnerability(@NotNull DamageSource damageSource, LivingEntity livingEntity){
        return damageSource.bypassesArmor() && !(livingEntity.getY() < livingEntity.getWorld().getBottomY());
    }

    public static boolean isInVoid(LivingEntity livingEntity, @NotNull DamageSource damageSource){
        return damageSource.isOutOfWorld() && livingEntity.getY() < livingEntity.getWorld().getBottomY();
    }

    public static ItemStack getTotemItemStack(LivingEntity livingEntity){
        List<ItemStack> possibleTotemStacks = filterPossibleTotemStacks(getTotemFromTrinketsSlot(livingEntity, itemStack -> itemStack.isOf(Items.TOTEM_OF_UNDYING)), getTotemFromInventory(livingEntity), getTotemFromHands(livingEntity));
        return possibleTotemStacks.stream().findFirst().orElse(null);
    }

    public static List<ItemStack> filterPossibleTotemStacks(ItemStack... stacks){
        return Arrays.stream(stacks).filter(Objects::nonNull).toList();
    }

    public static @Nullable ItemStack getTotemFromTrinketsSlot(LivingEntity livingEntity, Predicate<ItemStack> predicate){
        if (isModLoaded(BTUConstants.TRINKETS_MOD_ID) && BetterTotemOfUndying.CONFIG.USE_TOTEM_FROM_TRINKETS_SLOT){
            return TrinketsApi.getTrinketComponent(livingEntity).map(component -> {
                List<Pair<SlotReference, ItemStack>> res = component.getEquipped(predicate);
                return res.size() > 0 ? res.get(0).getRight() : null;
            }).orElse(null);
        }
        return null;
    }

    public static @Nullable ItemStack getTotemFromInventory(LivingEntity livingEntity){
        if (BetterTotemOfUndying.CONFIG.USE_TOTEM_FROM_INVENTORY && livingEntity instanceof ServerPlayerEntity player){
            for (ItemStack itemStack : player.getInventory().main){
                if (itemStack.isOf(Items.TOTEM_OF_UNDYING)) return itemStack;
            }
        }
        return null;
    }

    public static @Nullable ItemStack getTotemFromHands(LivingEntity livingEntity){
        for (Hand hand : Hand.values()){
            ItemStack itemStack = livingEntity.getStackInHand(hand);
            if (itemStack.isOf(Items.TOTEM_OF_UNDYING)) return itemStack;
        }
        return null;
    }

    public static void giveUseStatAndCriterion(@NotNull ItemStack itemStack, ServerPlayerEntity player){
        if (!itemStack.isEmpty()){
            player.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
            Criteria.USED_TOTEM.trigger(player, itemStack);
        }
    }

    public static void addCooldown(ItemStack itemStack, ServerPlayerEntity player, int cooldown){
        if(BetterTotemOfUndying.CONFIG.ADD_COOLDOWN){
            player.getItemCooldownManager().set(itemStack.getItem(), cooldown);
        }
    }

    public static void applyTotemEffects(LivingEntity livingEntity){
        int fireResistanceEffectDuration = BetterTotemOfUndying.CONFIG.FIRE_RESISTANCE_DURATION;
        int regenerationEffectDuration = BetterTotemOfUndying.CONFIG.REGENERATION_DURATION;
        int regenerationEffectAmplifier = BetterTotemOfUndying.CONFIG.REGENERATION_AMPLIFIER;
        int absorptionEffectDuration = BetterTotemOfUndying.CONFIG.ABSORPTION_DURATION;
        int absorptionEffectAmplifier = BetterTotemOfUndying.CONFIG.ABSORPTION_AMPLIFIER;
        int waterBreathingEffectDuration = BetterTotemOfUndying.CONFIG.WATER_BREATHING_DURATION;

        if (BetterTotemOfUndying.CONFIG.APPLY_EFFECTS_ONLY_WHEN_NEEDED){
            if (livingEntity.isOnFire() && BetterTotemOfUndying.CONFIG.ENABLE_FIRE_RESISTANCE) livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, fireResistanceEffectDuration, 0));
            if (livingEntity.isInsideWaterOrBubbleColumn() && BetterTotemOfUndying.CONFIG.ENABLE_WATER_BREATHING) livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, waterBreathingEffectDuration, 0));
        }else {
            if (BetterTotemOfUndying.CONFIG.ENABLE_FIRE_RESISTANCE) livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, fireResistanceEffectDuration, 0));
            if (BetterTotemOfUndying.CONFIG.ENABLE_WATER_BREATHING) livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, waterBreathingEffectDuration, 0));
        }
        if (BetterTotemOfUndying.CONFIG.ENABLE_REGENERATION) livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, regenerationEffectDuration, regenerationEffectAmplifier));
        if (BetterTotemOfUndying.CONFIG.ENABLE_ABSORPTION) livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, absorptionEffectDuration, absorptionEffectAmplifier));
    }

    public static void increaseFoodLevel(LivingEntity livingEntity, int foodLevel){
        if (BetterTotemOfUndying.CONFIG.ENABLE_INCREASE_FOOD_LEVEL && livingEntity instanceof ServerPlayerEntity player){
            int currentFoodLevel = player.getHungerManager().getFoodLevel();
            int minimumFoodLevel = BetterTotemOfUndying.CONFIG.MINIMUM_FOOD_LEVEL;

            if (currentFoodLevel <= minimumFoodLevel){
                player.getHungerManager().setFoodLevel(foodLevel);
            }
        }
    }

    public static void destroyBlocksWhenSuffocatingOrFullyFrozen(LivingEntity livingEntity, World world){
        if (isInWallOrFullyFrozen(livingEntity)){
            BlockPos blockPos = livingEntity.getBlockPos();
            BlockState blockAtEntityPos = world.getBlockState(blockPos);
            BlockState blockAboveEntityPos = world.getBlockState(blockPos.up());

            if (canDestroy(blockAtEntityPos)) world.breakBlock(blockPos, true);
            if (canDestroy(blockAboveEntityPos)) world.breakBlock(blockPos.up(), true);

            int distance = 2;
            while(true){
                if (isInstanceOfFallingBlock(blockPos, world, distance) && canDestroy(world.getBlockState(blockPos.up(distance)))){
                    world.breakBlock(blockPos.up(distance), true);
                    distance++;
                }else break;
            }
        }
    }

    public static boolean isInWallOrFullyFrozen(@NotNull LivingEntity livingEntity){
        return (livingEntity.isInsideWall() && BetterTotemOfUndying.CONFIG.DESTROY_BLOCKS_WHEN_SUFFOCATING) ||
                (livingEntity.isFrozen() && BetterTotemOfUndying.CONFIG.DESTROY_POWDER_SNOW_WHEN_FULLY_FROZEN);
    }

    public static boolean canDestroy(@NotNull BlockState block){
        return !block.isIn(BTUConstants.TOTEM_CANT_DESTROY_TAG) && !block.isOf(Blocks.BEDROCK) && !block.isOf(Blocks.END_PORTAL_FRAME);
    }

    public static boolean isInstanceOfFallingBlock(@NotNull BlockPos pos, @NotNull World world, int distance){
        return world.getBlockState(pos.up(distance)).getBlock() instanceof FallingBlock;
    }

    public static void knockbackMobsAway(LivingEntity livingEntity, World world){
        if (BetterTotemOfUndying.CONFIG.KNOCKBACK_MOBS_AWAY){
            List<LivingEntity> nearbyEntities = getNearbyEntities(livingEntity, world, BetterTotemOfUndying.CONFIG.KNOCKBACK_RADIUS);
            double strength = BetterTotemOfUndying.CONFIG.KNOCKBACK_STRENGTH;

            for (LivingEntity entity : nearbyEntities){
                if (!entity.equals(livingEntity)){
                    entity.takeKnockback(strength, livingEntity.getX() - entity.getX(), livingEntity.getZ() - entity.getZ());
                }
            }
        }
    }

    public static List<LivingEntity> getNearbyEntities(@NotNull LivingEntity livingEntity, @NotNull World world, double radius){
        return world.getEntitiesByClass(LivingEntity.class, livingEntity.getBoundingBox().expand(radius), LivingEntity::isAlive);
    }

    public static void teleportOutOfVoid(LivingEntity livingEntity, World world, DamageSource damageSource){
        if (isInVoid(livingEntity, damageSource)){
            BlockPos lastBlockPos = BlockPos.fromLong(((ILivingEntityMixin) livingEntity).getLastBlockPos());

            BlockPos positionNearby = randomTeleportNearby(livingEntity, world, lastBlockPos);
            if (positionNearby == null){
                livingEntity.teleport(lastBlockPos.getX(), ((ServerWorld) world).getLogicalHeight() + BetterTotemOfUndying.CONFIG.TELEPORT_HEIGHT_OFFSET, lastBlockPos.getZ());
                applySlowFallingEffect(livingEntity);
            }
        }
    }

    public static BlockPos randomTeleportNearby(LivingEntity livingEntity, World world, BlockPos blockPos){
        BlockPos teleportPos = null;

        for (int i = 0; i < 16; i++){
            double x = blockPos.getX() + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;
            double y = MathHelper.clamp(blockPos.getY() + (double) (livingEntity.getRandom().nextInt(16) - 8), world.getBottomY(), ((ServerWorld) world).getLogicalHeight());
            double z = blockPos.getZ() + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;

            BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
            if (livingEntity.teleport(x, y, z, true)){
                teleportPos = pos;
                livingEntity.fallDistance = 0;
                break;
            }
        }
        return  teleportPos;
    }

    public static void applySlowFallingEffect(LivingEntity livingEntity){
        int duration = BetterTotemOfUndying.CONFIG.SLOW_FALLING_DURATION;

        if (BetterTotemOfUndying.CONFIG.ENABLE_SLOW_FALLING)
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, duration, 0));
    }
}
