package com.cerbon.better_totem_of_undying;

import com.cerbon.better_totem_of_undying.config.BTUConfigs;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class BetterTotemOfUndying implements ModInitializer {
	public static BTUConfigs CONFIG;

	@Override
	public void onInitialize() {
		AutoConfig.register(BTUConfigs.class, Toml4jConfigSerializer::new).getConfig();
		CONFIG = AutoConfig.getConfigHolder(BTUConfigs.class).getConfig();
	}
}