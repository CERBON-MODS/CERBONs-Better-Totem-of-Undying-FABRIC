package com.cerbon.better_totem_of_undying.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class BTUUtils {

    public static boolean isModLoaded(String mod_id){
       return FabricLoader.getInstance().isModLoaded(mod_id);
    }

    public static boolean canSaveFromDeath(LivingEntity livingEntity, DamageSource damageSource){
        return true;
    }
}
