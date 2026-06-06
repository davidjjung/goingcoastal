package com.davigj.going_coastal.core.data.server;

import com.davigj.going_coastal.core.GoingCoastal;
import com.davigj.going_coastal.core.registry.GCFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GCDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER;

    public GCDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(GoingCoastal.MOD_ID));
    }

    static {
        BUILDER = (new RegistrySetBuilder()).add(Registries.CONFIGURED_FEATURE, GCFeatures.GCConfiguredFeatures::bootstrap).add(Registries.PLACED_FEATURE, GCFeatures.GCPlacedFeatures::bootstrap);
    }
}


