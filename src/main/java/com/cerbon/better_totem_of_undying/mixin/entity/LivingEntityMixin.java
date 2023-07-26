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

    @Unique public long lastBlockPos;

    public LivingEntityMixin(EntityType<?> pEntityType, World world) {
        super(pEntityType, world);
    }

    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    private void checkTotemDeathProtection(DamageSource pDamageSource, @NotNull CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        cir.setReturnValue(BTUUtils.canSaveFromDeath(livingEntity, pDamageSource));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void addCustomData(@NotNull NbtCompound pCompound, CallbackInfo ci){
        pCompound.putLong("LastBlockPos", this.lastBlockPos);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomData(@NotNull NbtCompound pCompound, CallbackInfo ci){
        this.lastBlockPos = pCompound.getLong("LastBlockPos");
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    public void saveEntityLastBlockPos(CallbackInfo ci) {
        if (!this.getWorld().isClient()) {
            World world = this.getWorld();
            BlockPos currentPos = this.getBlockPos();
            BlockState blockBelowEntityPos = world.getBlockState(currentPos.down());
            boolean isValidBlock = blockBelowEntityPos.isSolidBlock(world, currentPos.down());

            if (!Objects.equals(this.lastBlockPos, currentPos.asLong()) && isValidBlock) {
                this.lastBlockPos = currentPos.asLong();
            }
        }
    }

    @Unique
    @Override
    public long getLastBlockPos() {
        return lastBlockPos;
    }
}
