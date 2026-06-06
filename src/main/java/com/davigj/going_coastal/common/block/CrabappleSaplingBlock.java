package com.davigj.going_coastal.common.block;

import com.davigj.going_coastal.core.other.GCBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class CrabappleSaplingBlock extends SaplingBlock {
    public CrabappleSaplingBlock(TreeGrower treeGrower, Properties properties) {
        super(treeGrower, properties);
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(GCBlockTags.CRABAPPLE_SAPLING_MAY_PLACE_ON);
    }
}
