package com.cerbon.better_totem_of_undying.mixin.entity;

import com.cerbon.better_totem_of_undying.util.BTUUtils;
import com.cerbon.better_totem_of_undying.util.ILivingEntityMixin;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = LivingEntity.class, priority = 1100)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityMixin {

    @Unique private long better_totem_of_undying_lastBlockPos;

    private LivingEntityMixin(EntityType<?> pEntityType, World world) {
        super(pEntityType, world);
    }

    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    private void better_totem_of_undying_checkTotemDeathProtection(DamageSource pDamageSource, @NotNull CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        cir.setReturnValue(BTUUtils.canSaveFromDeath(livingEntity, pDamageSource));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void better_totem_of_undying_addCustomData(@NotNull NbtCompound pCompound, CallbackInfo ci){
        pCompound.putLong("BTULastBlockPos", this.better_totem_of_undying_getLastBlockPos());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void better_totem_of_undying_readCustomData(@NotNull NbtCompound pCompound, CallbackInfo ci){
        this.better_totem_of_undying_lastBlockPos = pCompound.getLong("BTULastBlockPos");
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void better_totem_of_undying_saveEntityLastBlockPos(CallbackInfo ci) {
        if (!this.getWorld().isClient()) {
            World world = this.getWorld();
            BlockPos currentPos = this.getBlockPos();
            BlockState blockBelowEntityPos = world.getBlockState(currentPos.down());
            boolean isValidBlock = blockBelowEntityPos.isSolidBlock(world, currentPos.down());

            if (!Objects.equals(this.better_totem_of_undying_lastBlockPos, currentPos.asLong()) && isValidBlock) {
                this.better_totem_of_undying_lastBlockPos = currentPos.asLong();
            }
        }
    }

    @Unique
    @Override
    public long better_totem_of_undying_getLastBlockPos() {
        return better_totem_of_undying_lastBlockPos;
    }
}
