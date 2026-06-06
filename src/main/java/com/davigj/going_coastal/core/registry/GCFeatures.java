package com.davigj.going_coastal.core.registry;

import com.davigj.going_coastal.common.levelgen.feature.CrabbyTreeFeature;
import com.davigj.going_coastal.common.levelgen.feature.treedecorators.CrabapplesDecorator;
import com.davigj.going_coastal.common.levelgen.feature.treedecorators.CrabgrassDecorator;
import com.davigj.going_coastal.common.levelgen.feature.treedecorators.CrabgrassPatchDecorator;
import com.davigj.going_coastal.core.GoingCoastal;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class GCFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES;
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_TYPES;
    public static final DeferredHolder<Feature<?>, Feature<TreeConfiguration>> CRABAPPLE_TREE;
    public static final DeferredHolder<Feature<?>, Feature<TreeConfiguration>> FRUIT_CRABAPPLE_01;
    public static final DeferredHolder<Feature<?>, Feature<TreeConfiguration>> FRUIT_CRABAPPLE_WITH_CRABGRASS_05;
    public static final DeferredHolder<Feature<?>, Feature<TreeConfiguration>> CRABBY_CRABAPPLE_TREE;
    public static final DeferredHolder<Feature<?>, Feature<TreeConfiguration>> FRUIT_CRABBY_CRABAPPLE_TREE;
    public static final DeferredHolder<Feature<?>, Feature<TreeConfiguration>> BABY_CRABBY_CRABAPPLE_TREE;
    public static final DeferredHolder<Feature<?>, Feature<TreeConfiguration>> FRUIT_BABY_CRABBY_CRABAPPLE_TREE;
    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> CRABGRASS;
    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> CRABGRASS_PATCH;
    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> CRABAPPLES;

    public GCFeatures() {
    }

    static {
        FEATURES = DeferredRegister.create(Registries.FEATURE, GoingCoastal.MOD_ID);
        TREE_DECORATOR_TYPES = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, GoingCoastal.MOD_ID);
        CRABAPPLE_TREE = FEATURES.register("crabapple_tree", () -> {
            return new TreeFeature(TreeConfiguration.CODEC);
        });
        FRUIT_CRABAPPLE_01 = FEATURES.register("fruit_crabapple_01", () -> {
            return new TreeFeature(TreeConfiguration.CODEC);
        });
        FRUIT_CRABAPPLE_WITH_CRABGRASS_05 = FEATURES.register("fruit_crabapple_with_crabgrass_05", () -> {
            return new TreeFeature(TreeConfiguration.CODEC);
        });
        CRABBY_CRABAPPLE_TREE = FEATURES.register("crabby_crabapple_tree", () -> {
            return new CrabbyTreeFeature(TreeConfiguration.CODEC, false);
        });
        FRUIT_CRABBY_CRABAPPLE_TREE = FEATURES.register("fruit_crabby_crabapple_tree", () -> {
            return new CrabbyTreeFeature(TreeConfiguration.CODEC, false);
        });
        BABY_CRABBY_CRABAPPLE_TREE = FEATURES.register("baby_crabby_crabapple_tree", () -> {
            return new CrabbyTreeFeature(TreeConfiguration.CODEC, true);
        });
        FRUIT_BABY_CRABBY_CRABAPPLE_TREE = FEATURES.register("fruit_baby_crabby_crabapple_tree", () -> {
            return new CrabbyTreeFeature(TreeConfiguration.CODEC, true);
        });
        CRABGRASS = TREE_DECORATOR_TYPES.register("crabgrass", () -> {
            return new TreeDecoratorType<>(CrabgrassDecorator.CODEC);
        });
        CRABGRASS_PATCH = TREE_DECORATOR_TYPES.register("crabgrass_patch", () -> {
            return new TreeDecoratorType<>(CrabgrassPatchDecorator.CODEC);
        });
        CRABAPPLES = TREE_DECORATOR_TYPES.register("crabapples", () -> {
            return new TreeDecoratorType<>(CrabapplesDecorator.CODEC);
        });
    }

    public static final class GCPlacedFeatures {
        public static final ResourceKey<PlacedFeature> CRABAPPLE_TREE = createKey("crabapple_tree");
        public static final ResourceKey<PlacedFeature> FRUIT_CRABAPPLE_01 = createKey("fruit_crabapple_01");
        public static final ResourceKey<PlacedFeature> CRABBY_CRABAPPLE_TREE = createKey("crabby_crabapple_tree");
        public static final ResourceKey<PlacedFeature> FRUIT_CRABBY_CRABAPPLE_TREE = createKey("fruit_crabby_crabapple_tree");
        public static final ResourceKey<PlacedFeature> BABY_CRABBY_CRABAPPLE_TREE = createKey("baby_crabby_crabapple_tree");
        public static final ResourceKey<PlacedFeature> FRUIT_BABY_CRABBY_CRABAPPLE_TREE = createKey("fruit_baby_crabby_crabapple_tree");
        public static final ResourceKey<PlacedFeature> CRABAPPLE_WITH_CRABGRASS = createKey("crabapple_with_crabgrass");
        public static final ResourceKey<PlacedFeature> FRUIT_CRABAPPLE_WITH_CRABGRASS_05 = createKey("fruit_crabapple_with_crabgrass_05");
        public static final ResourceKey<PlacedFeature> TREES_COAST = createKey("trees_coast");
        public static final ResourceKey<PlacedFeature> PATCH_CRABGRASS = createKey("patch_crabgrass");
        public static final ResourceKey<PlacedFeature> PATCH_COASTAL_CRABGRASS = createKey("patch_coastal_crabgrass");

        public GCPlacedFeatures() {
        }

        public static void bootstrap(BootstrapContext<PlacedFeature> context) {
            register(context, CRABBY_CRABAPPLE_TREE, GCConfiguredFeatures.CRABBY_CRABAPPLE, List.of());
            register(context, FRUIT_CRABBY_CRABAPPLE_TREE, GCConfiguredFeatures.FRUIT_CRABBY_CRABAPPLE, List.of());
            register(context, BABY_CRABBY_CRABAPPLE_TREE, GCConfiguredFeatures.BABY_CRABBY_CRABAPPLE, List.of());
            register(context, FRUIT_BABY_CRABBY_CRABAPPLE_TREE, GCConfiguredFeatures.FRUIT_BABY_CRABBY_CRABAPPLE, List.of());
            register(context, CRABAPPLE_TREE, GCConfiguredFeatures.CRABAPPLE, List.of());
            register(context, FRUIT_CRABAPPLE_01, GCConfiguredFeatures.FRUIT_CRABAPPLE_01, List.of());
            register(context, CRABAPPLE_WITH_CRABGRASS, GCConfiguredFeatures.CRABAPPLE_WITH_CRABGRASS, List.of());
            register(context, FRUIT_CRABAPPLE_WITH_CRABGRASS_05, GCConfiguredFeatures.FRUIT_CRABAPPLE_WITH_CRABGRASS_05, List.of());
            register(context, TREES_COAST, GCConfiguredFeatures.TREES_COAST, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(10), GCBlocks.CRABAPPLE_SAPLING.get()));
            register(context, PATCH_CRABGRASS, GCConfiguredFeatures.PATCH_CRABGRASS, List.of(RarityFilter.onAverageOnceEvery(16), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
            register(context, PATCH_COASTAL_CRABGRASS, GCConfiguredFeatures.PATCH_COASTAL_CRABGRASS, List.of(RarityFilter.onAverageOnceEvery(16), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        }

        public static ResourceKey<PlacedFeature> createKey(String name) {
            return ResourceKey.create(Registries.PLACED_FEATURE, GoingCoastal.location(name));
        }

        public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
            context.register(key, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature), modifiers));
        }

        public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
            register(context, key, feature, List.of(modifiers));
        }
    }

    public static final class GCConfiguredFeatures {
        public static final ResourceKey<ConfiguredFeature<?, ?>> CRABBY_CRABAPPLE = createKey("crabby_crabapple_tree");
        public static final ResourceKey<ConfiguredFeature<?, ?>> FRUIT_CRABBY_CRABAPPLE = createKey("fruit_crabby_crabapple_tree");
        public static final ResourceKey<ConfiguredFeature<?, ?>> BABY_CRABBY_CRABAPPLE = createKey("baby_crabby_crabapple_tree");
        public static final ResourceKey<ConfiguredFeature<?, ?>> FRUIT_BABY_CRABBY_CRABAPPLE = createKey("fruit_baby_crabby_crabapple_tree");
        public static final ResourceKey<ConfiguredFeature<?, ?>> CRABAPPLE = createKey("crabapple");
        public static final ResourceKey<ConfiguredFeature<?, ?>> FRUIT_CRABAPPLE_01 = createKey("fruit_crabapple_01");
        public static final ResourceKey<ConfiguredFeature<?, ?>> CRABAPPLE_WITH_CRABGRASS = createKey("crabapple_with_crabgrass");
        public static final ResourceKey<ConfiguredFeature<?, ?>> FRUIT_CRABAPPLE_WITH_CRABGRASS_05 = createKey("fruit_crabapple_with_crabgrass_05");
        public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_COAST = createKey("trees_coast");
        public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CRABGRASS = createKey("patch_crabgrass");
        public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_COASTAL_CRABGRASS = createKey("patch_coastal_crabgrass");

        public GCConfiguredFeatures() {
        }

        public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
            HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

            register(context, CRABBY_CRABAPPLE, GCFeatures.CRABBY_CRABAPPLE_TREE.get(), Configs.CRABBY_CRABAPPLE);
            register(context, FRUIT_CRABBY_CRABAPPLE, GCFeatures.CRABBY_CRABAPPLE_TREE.get(), Configs.FRUIT_CRABBY_CRABAPPLE);
            register(context, BABY_CRABBY_CRABAPPLE, GCFeatures.BABY_CRABBY_CRABAPPLE_TREE.get(), Configs.BABY_CRABBY_CRABAPPLE);
            register(context, FRUIT_BABY_CRABBY_CRABAPPLE, GCFeatures.BABY_CRABBY_CRABAPPLE_TREE.get(), Configs.FRUIT_BABY_CRABBY_CRABAPPLE);
            register(context, CRABAPPLE, GCFeatures.CRABAPPLE_TREE.get(), Configs.CRABAPPLE);
            register(context, FRUIT_CRABAPPLE_01, GCFeatures.CRABAPPLE_TREE.get(), Configs.FRUIT_CRABAPPLE_01);
            register(context, CRABAPPLE_WITH_CRABGRASS, GCFeatures.CRABAPPLE_TREE.get(), Configs.CRABAPPLE_WITH_CRABGRASS);
            register(context, FRUIT_CRABAPPLE_WITH_CRABGRASS_05, GCFeatures.FRUIT_CRABAPPLE_WITH_CRABGRASS_05.get(), Configs.FRUIT_CRABAPPLE_WITH_CRABGRASS_05);
            register(context, TREES_COAST, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(
                    new WeightedPlacedFeature(placedFeatures.getOrThrow(GCPlacedFeatures.CRABBY_CRABAPPLE_TREE), 0.25F),
                    new WeightedPlacedFeature(placedFeatures.getOrThrow(GCPlacedFeatures.FRUIT_CRABBY_CRABAPPLE_TREE), 0.35F),
                    new WeightedPlacedFeature(placedFeatures.getOrThrow(GCPlacedFeatures.CRABAPPLE_WITH_CRABGRASS), 0.15F)),
                    placedFeatures.getOrThrow(GCPlacedFeatures.FRUIT_CRABAPPLE_WITH_CRABGRASS_05)));
            register(context, PATCH_COASTAL_CRABGRASS, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                            .add(GCBlocks.CRABGRASS.get().defaultBlockState(), 3)
                            .add(GCBlocks.COASTAL_CRABGRASS.get().defaultBlockState(), 1).build())),
                    List.of(Blocks.SAND, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.GRAVEL), 128));
            register(context, PATCH_CRABGRASS, Feature.RANDOM_PATCH, new RandomPatchConfiguration(120, 5, 3,
                    PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(GCBlocks.CRABGRASS.get())),
                            BlockPredicate.allOf(BlockPredicate.replaceable(),
                                    BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(),
                                            List.of(Blocks.SAND, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.GRAVEL))))));

        }

        public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, GoingCoastal.location(name));
        }

        public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
            context.register(key, new ConfiguredFeature<>(feature, config));
        }
    }

    public static final class Configs {
        private static final CrabgrassDecorator CRABGRASS;
        private static final CrabgrassPatchDecorator CRABGRASS_PATCH;
        private static final CrabapplesDecorator CRABAPPLES;
        public static final TreeConfiguration CRABAPPLE;
        public static final TreeConfiguration FRUIT_CRABAPPLE_01;
        public static final TreeConfiguration CRABBY_CRABAPPLE;
        public static final TreeConfiguration FRUIT_CRABBY_CRABAPPLE;
        public static final TreeConfiguration BABY_CRABBY_CRABAPPLE;
        public static final TreeConfiguration FRUIT_BABY_CRABBY_CRABAPPLE;
        public static final TreeConfiguration CRABAPPLE_WITH_CRABGRASS;
        public static final TreeConfiguration FRUIT_CRABAPPLE_WITH_CRABGRASS_05;
        public Configs() {
        }
        private static TreeConfiguration.TreeConfigurationBuilder createCrabapple() {
            return new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(Blocks.DARK_OAK_LOG),
                    new BendingTrunkPlacer(4, 2, 0, 3, UniformInt.of(1, 2)),
                    new WeightedStateProvider(
                            SimpleWeightedRandomList.<BlockState>builder()
                                    .add(GCBlocks.CRABAPPLE_LEAVES.get().defaultBlockState(), 3)
                                    .add(GCBlocks.FLOWERING_CRABAPPLE_LEAVES.get().defaultBlockState(), 1)),
                    new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 3),
                    new TwoLayersFeatureSize(1, 0, 1))
                    .dirt(BlockStateProvider.simple(Blocks.SAND));
        }

        static {
            CRABGRASS = new CrabgrassDecorator(0.3F, new WeightedStateProvider(
                    SimpleWeightedRandomList.<BlockState>builder()
                            .add((GCBlocks.CRABGRASS.get()).defaultBlockState(), 3)
                            .add((GCBlocks.COASTAL_CRABGRASS.get()).defaultBlockState(), 1).build()));
            CRABGRASS_PATCH = new CrabgrassPatchDecorator(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                    .add((GCBlocks.CRABGRASS.get()).defaultBlockState(), 3).
                    add((GCBlocks.COASTAL_CRABGRASS.get()).defaultBlockState(), 1).build()));
            CRABAPPLES = new CrabapplesDecorator(0.15F, new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                    .add((GCBlocks.CRABAPPLE_FRUIT_LEAVES.get()).defaultBlockState(), 3).build()));

            CRABAPPLE = createCrabapple().decorators(List.of(CRABGRASS)).build();
            FRUIT_CRABAPPLE_01 = createCrabapple().decorators(List.of(CRABGRASS, CRABAPPLES)).build();
            CRABBY_CRABAPPLE = createCrabapple().decorators(List.of(CRABGRASS, CRABGRASS_PATCH)).build();
            FRUIT_CRABBY_CRABAPPLE = createCrabapple().decorators(List.of(CRABGRASS, CRABGRASS_PATCH, CRABAPPLES)).build();
            BABY_CRABBY_CRABAPPLE = createCrabapple().decorators(List.of(CRABGRASS)).build();
            FRUIT_BABY_CRABBY_CRABAPPLE = createCrabapple().decorators(List.of(CRABGRASS, CRABAPPLES)).build();
            CRABAPPLE_WITH_CRABGRASS = createCrabapple().decorators(List.of(CRABGRASS, CRABGRASS_PATCH)).build();
            FRUIT_CRABAPPLE_WITH_CRABGRASS_05 = createCrabapple().decorators(List.of(CRABGRASS, CRABGRASS_PATCH, CRABAPPLES)).build();
        }
    }
}
