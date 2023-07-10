package com.cerbon.better_totem_of_undying.util;

import net.fabricmc.loader.api.FabricLoader;

public class BTUUtils {
    public static boolean isModLoaded(String mod_id){
       return FabricLoader.getInstance().isModLoaded(mod_id);
    }
}
