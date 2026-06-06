package com.davigj.going_coastal.common.levelgen.feature;

import com.davigj.going_coastal.core.GCConfig;
import com.davigj.going_coastal.core.other.compat.SpawnCompat;
import com.davigj.going_coastal.core.registry.GCBlocks;
import com.mojang.serialization.Codec;
import com.teamabnormals.blueprint.common.levelgen.feature.BlueprintTreeFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static com.davigj.going_coastal.core.other.compat.ModConstants.SPAWN;

public class CrabbyTreeFeature extends BlueprintTreeFeature {
    public boolean baby;

    public CrabbyTreeFeature(Codec<TreeConfiguration> config, boolean baby) {
        super(config);
        this.baby = baby;
    }

    @Override
    public BlockState getSapling() {
        return GCBlocks.CRABAPPLE_SAPLING.get().defaultBlockState();
    }

    @Override
    public void doPlace(FeaturePlaceContext<TreeConfiguration> context, BlueprintTreeFeature.TreeInfo info) {
        TreeConfiguration config = context.config();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        int trunkHeight = config.trunkPlacer.getTreeHeight(random);
        int foliageHeight = config.foliagePlacer.foliageHeight(random, trunkHeight, config);
        int foliageRadius = config.foliagePlacer.foliageRadius(random, trunkHeight - foliageHeight);

        BiConsumer<BlockPos, BlockState> trunkSetter = (pos, state) -> {
            if (TreeFeature.validTreePos(level, pos)) {
                info.addLog(pos, state);
            }
        };

        List<FoliagePlacer.FoliageAttachment> attachments = config.trunkPlacer.placeTrunk(
                level, trunkSetter, random, trunkHeight, origin, config
        );

        // defining a whole-ass FoliageSetter is likely a terrible terrible sin and I should just use my own blob placer :sob:
        FoliagePlacer.FoliageSetter foliageSetter = new FoliagePlacer.FoliageSetter() {
            @Override
            public void set(BlockPos pos, BlockState state) {
                if (TreeFeature.validTreePos(level, pos)) {
                    info.addFoliage(pos, state);
                }
            }
            @Override
            public boolean isSet(BlockPos pos) {
                return info.foliageMap().containsKey(pos);
            }
        };

        attachments.forEach(attachment -> config.foliagePlacer.createFoliage(level, foliageSetter, random, config,
                trunkHeight, attachment, foliageHeight, foliageRadius));
    }

    @Override
    public void doPostPlace(FeaturePlaceContext<TreeConfiguration> context, BlueprintTreeFeature.TreeInfo info) {
        if (!GCConfig.COMMON.crabbyTrees.get()) return;
        WorldGenLevel level = context.level();
        if (!level.getLevel().getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) return;

        List<BlockPos> potentialSpawns = new ArrayList<>();
        for (BlockPos pos : info.foliageMap().keySet()) {
            BlockPos above = pos.above();
            if (!info.foliageMap().containsKey(above) && !info.logMap().containsKey(above) && level.isEmptyBlock(above)) {
                potentialSpawns.add(above);
            }
        }
        if (potentialSpawns.isEmpty()) return;
        if (SPAWN) {
            SpawnCompat.addCrabs(potentialSpawns, level, context.random(), this.baby);
        }
    }
}
