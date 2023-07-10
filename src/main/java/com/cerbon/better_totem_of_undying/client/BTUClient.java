package com.cerbon.better_totem_of_undying.client;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import com.cerbon.better_totem_of_undying.util.BTUConstants;
import com.cerbon.better_totem_of_undying.util.BTUUtils;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.Items;
import net.minecraft.util.math.RotationAxis;

public class BTUClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        if (BTUUtils.isModLoaded(BTUConstants.TRINKETS_MOD_ID) && BetterTotemOfUndying.CONFIG.DISPLAY_TOTEM_ON_CHEST){
            renderVoidTotemTrinket();
        }
    }

    private void renderVoidTotemTrinket() {
        TrinketRendererRegistry.registerRenderer(Items.TOTEM_OF_UNDYING, (stack, slotReference, contextModel, matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch) -> {

            if (entity instanceof AbstractClientPlayerEntity player){
                TrinketRenderer.translateToChest(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) contextModel, player);

                matrices.scale(0.35F, 0.35F, 0.35F);
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));

                MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, 15728880, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, MinecraftClient.getInstance().world, 0);
            }
        });
    }
}
