package com.davigj.going_coastal.core.other;

import com.davigj.going_coastal.core.GoingCoastal;
import com.davigj.going_coastal.core.registry.GCBlocks;
import com.davigj.going_coastal.core.registry.GCItems;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = GoingCoastal.MOD_ID, value = Dist.CLIENT)
public class GCClientCompat {
    public GCClientCompat() {
    }

    public static void register() {
        GCBlocks.setupTabEditors();
        GCItems.setupTabEditors();
        registerRenderLayers();
    }

    private static void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer((Block)GCBlocks.CRABGRASS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer((Block)GCBlocks.COASTAL_CRABGRASS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer((Block)GCBlocks.CRABAPPLE_SAPLING.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : GrassColor.get(0.5D, 1.0D),
                GCBlocks.CRABGRASS.get(),
                GCBlocks.COASTAL_CRABGRASS.get()
        );
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((item, tintIndex) -> GrassColor.get(0.5D, 1.0D), GCBlocks.CRABGRASS);
        event.register((item, tintIndex) -> GrassColor.get(0.5D, 1.0D), GCBlocks.COASTAL_CRABGRASS);
    }
}
