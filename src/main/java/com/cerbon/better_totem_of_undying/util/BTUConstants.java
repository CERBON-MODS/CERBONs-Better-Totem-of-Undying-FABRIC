package com.cerbon.better_totem_of_undying.util;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BTUConstants {
    public static final String MOD_ID = "better_totem_of_undying";
    public static final String TRINKETS_MOD_ID = "trinkets";

    public static final TagKey<Block> TOTEM_CANT_DESTROY_TAG = TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, "totem_cant_destroy"));
}
