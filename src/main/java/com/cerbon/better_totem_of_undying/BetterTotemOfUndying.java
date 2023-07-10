package com.cerbon.better_totem_of_undying;

import com.cerbon.better_totem_of_undying.util.BTUConstants;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterTotemOfUndying implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(BTUConstants.MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}