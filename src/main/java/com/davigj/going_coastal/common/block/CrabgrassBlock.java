package com.davigj.going_coastal.common.block;

import com.davigj.going_coastal.core.other.GCBlockTags;
import com.davigj.going_coastal.core.registry.GCBlocks;
import com.davigj.going_coastal.core.registry.GCFeatures;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.IShearable;

public class CrabgrassBlock extends BushBlock implements IShearable, BonemealableBlock {
    public static final MapCodec<CrabgrassBlock> CODEC = simpleCodec(CrabgrassBlock::new);
    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

    public CrabgrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(GCBlockTags.CRABGRASS_MAY_PLACE_ON);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        ResourceKey<ConfiguredFeature<?, ?>> feature;
        feature = state.is(GCBlocks.COASTAL_CRABGRASS) ? GCFeatures.GCConfiguredFeatures.PATCH_COASTAL_CRABGRASS :
                GCFeatures.GCConfiguredFeatures.PATCH_CRABGRASS;
        level.registryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap((value) -> {
            return value.getHolder(feature);
        }).ifPresent((value) -> {
            ((ConfiguredFeature)value.value()).place(level, level.getChunkSource().getGenerator(), random, pos.above());
        });
    }
}
