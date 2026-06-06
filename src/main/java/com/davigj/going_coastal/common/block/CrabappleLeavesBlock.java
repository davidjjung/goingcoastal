package com.davigj.going_coastal.common.block;

import com.davigj.going_coastal.core.registry.GCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CrabappleLeavesBlock extends LeavesBlock {
    public CrabappleLeavesBlock(Properties properties) {
        super(properties);
    }

    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        int fruitLeaves = 0;
        int regularLeaves = 0;
        if (!(Boolean)state.getValue(PERSISTENT)) {
            Direction[] var7 = Direction.values();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                Direction direction = var7[var9];
                BlockPos relativePos = pos.relative(direction);
                if (level.getBlockState(relativePos).is(this)) {
                    ++fruitLeaves;
                }

                if (level.getBlockState(relativePos).is(GCBlocks.CRABAPPLE_LEAVES)) {
                    ++regularLeaves;
                }

                Direction[] var12 = Direction.values();
                int var13 = var12.length;

                for(int var14 = 0; var14 < var13; ++var14) {
                    Direction direction1 = var12[var14];
                    if (direction1 != direction.getOpposite()) {
                        BlockPos relativePos1 = relativePos.relative(direction1);
                        if (level.getBlockState(relativePos1).is(this)) {
                            ++fruitLeaves;
                        }

                        if (level.getBlockState(relativePos1).is(GCBlocks.CRABAPPLE_LEAVES)) {
                            ++regularLeaves;
                        }
                    }
                }
            }

            if (regularLeaves < 4) {
                this.rot(level, pos, state);
            }

            if (fruitLeaves > 4) {
                this.rot(level, pos, state);
            }
        }

        BlockPos fruitPos = pos.below();
        BlockState fruitState = level.getBlockState(fruitPos);
        if (level.isAreaLoaded(fruitPos, 1)) {
            if (level.getRawBrightness(fruitPos, 0) >= 9 && random.nextInt(3, 10) == 3) {
                Block var20 = fruitState.getBlock();
                if (var20 instanceof CrabapplesBlock crabapples) {
                    int fruitAge = (Integer)fruitState.getValue(CrabapplesBlock.AGE);
                    if (fruitAge < crabapples.getMaxAge()) {
                        level.setBlockAndUpdate(fruitPos, (BlockState)fruitState.setValue(CrabapplesBlock.AGE, fruitAge + 1));
                    }
                } else if (fruitState.isAir()) {
                    level.setBlockAndUpdate(fruitPos, (BlockState)GCBlocks.CRABAPPLES.get().defaultBlockState().setValue(CrabapplesBlock.AGE, 0));
                }
            }

        }
    }

    public void rot(Level level, BlockPos pos, BlockState state) {
        int distance = (Integer)state.getValue(DISTANCE);
        boolean waterlogged = (Boolean)state.getValue(WATERLOGGED);
        if (level.getBlockState(pos.below()).getBlock() instanceof CrabapplesBlock) {
            level.setBlockAndUpdate(pos.below(), Blocks.AIR.defaultBlockState());
        }

        level.setBlockAndUpdate(pos, (BlockState)((BlockState)((Block)GCBlocks.CRABAPPLE_LEAVES.get()).defaultBlockState().setValue(DISTANCE, distance)).setValue(WATERLOGGED, waterlogged));
    }
}
