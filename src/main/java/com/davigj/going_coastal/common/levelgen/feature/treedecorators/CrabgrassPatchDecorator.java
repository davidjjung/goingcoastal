package com.davigj.going_coastal.common.levelgen.feature.treedecorators;

import com.davigj.going_coastal.core.other.GCBlockTags;
import com.davigj.going_coastal.core.registry.GCFeatures;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class CrabgrassPatchDecorator extends TreeDecorator {
    public static final MapCodec<CrabgrassPatchDecorator> CODEC = RecordCodecBuilder.mapCodec((codec) -> {
        return codec.group(BlockStateProvider.CODEC.fieldOf("block_provider").forGetter((instance) -> {
            return instance.blockProvider;
        })).apply(codec, CrabgrassPatchDecorator::new);
    });
    private final BlockStateProvider blockProvider;

    public CrabgrassPatchDecorator(BlockStateProvider blockProvider) {
        this.blockProvider = blockProvider;
    }

    public void place(TreeDecorator.Context context) {
        LevelSimulatedReader level = context.level();
        RandomSource random = context.random();
        BlockPos pos = getLowestBlockPos(context.logs());

        for(int j = 0; j < 84; ++j) {
            BlockPos offsetPos = pos.offset(random.nextInt(6) - random.nextInt(6), random.nextInt(4) - random.nextInt(4), random.nextInt(6) - random.nextInt(6));
            if (level.isStateAtPosition(offsetPos.below(), (state) -> {
                return state.is(GCBlockTags.CRABGRASS_MAY_PLACE_ON);
            })) {
                if (context.isAir(offsetPos)) {
                    context.setBlock(offsetPos, this.blockProvider.getState(random, offsetPos));
                }
            }
        }

    }

    public static BlockPos getLowestBlockPos(ObjectArrayList<BlockPos> positions) {
        BlockPos lowest = (BlockPos)positions.get(0);
        ObjectListIterator var2 = positions.iterator();

        while(var2.hasNext()) {
            BlockPos pos = (BlockPos)var2.next();
            if (pos.getY() < lowest.getY()) {
                lowest = pos;
            }
        }

        return lowest;
    }

    protected TreeDecoratorType<?> type() {
        return (TreeDecoratorType) GCFeatures.CRABGRASS_PATCH.get();
    }
}
