package com.davigj.going_coastal.common.levelgen.feature.treedecorators;

import com.davigj.going_coastal.core.registry.GCBlocks;
import com.davigj.going_coastal.core.registry.GCFeatures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import static com.davigj.going_coastal.common.block.CrabapplesBlock.AGE;
import static com.davigj.going_coastal.core.other.compat.ModConstants.NML;

public class CrabapplesDecorator extends TreeDecorator {
    public static final MapCodec<CrabapplesDecorator> CODEC = RecordCodecBuilder.mapCodec((codec) -> {
        return codec.group(Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter((instance) -> {
            return instance.probability;
        }), BlockStateProvider.CODEC.fieldOf("block_provider").forGetter((instance) -> {
            return instance.blockProvider;
        })).apply(codec, CrabapplesDecorator::new);
    });
    private final float probability;
    private final BlockStateProvider blockProvider;

    public CrabapplesDecorator(float probability, BlockStateProvider blockProvider) {
        this.probability = probability;
        this.blockProvider = blockProvider;
    }

    public void place(Context context) {
        if (!NML) return;
        RandomSource random = context.random();

        for (BlockPos pos : context.leaves()) {
            if (random.nextFloat() < this.probability) {
                if (context.isAir(pos.below())) {
                    context.setBlock(pos, this.blockProvider.getState(random, pos));
//                    context.setBlock(pos, GCBlocks.CRABAPPLE_FRUIT_LEAVES.get().defaultBlockState());
                    context.setBlock(pos.below(), GCBlocks.CRABAPPLES.get().defaultBlockState().setValue(AGE, 3));
                }
            }
        }
    }

    protected @NotNull TreeDecoratorType<?> type() {
        return (TreeDecoratorType) GCFeatures.CRABAPPLES.get();
    }
}
