package com.davigj.going_coastal.core;

import com.davigj.going_coastal.core.data.server.GCDatapackProvider;
import com.davigj.going_coastal.core.other.GCClientCompat;
import com.davigj.going_coastal.core.other.GCCompat;
import com.davigj.going_coastal.core.registry.GCBlocks;
import com.davigj.going_coastal.core.registry.GCFeatures;
import com.davigj.going_coastal.core.registry.GCItems;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(GoingCoastal.MOD_ID)
public class GoingCoastal {
    public static final String MOD_ID = "going_coastal";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

    public GoingCoastal(IEventBus bus, ModContainer container) {
        GCBlocks.BLOCKS.register(bus);
        GCItems.ITEMS.register(bus);
        GCFeatures.FEATURES.register(bus);
        GCFeatures.TREE_DECORATOR_TYPES.register(bus);
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::dataSetup);

        container.registerConfig(ModConfig.Type.COMMON, GCConfig.COMMON_SPEC);
    }


    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(GCCompat::registerCompat);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(GCClientCompat::register);
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        boolean server = event.includeServer();
        GCDatapackProvider datapack = new GCDatapackProvider(output, provider);
        generator.addProvider(server, datapack);
        provider = datapack.getRegistryProvider();

    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}