package com.davigj.going_coastal.common.levelgen.feature.treedecorators;

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

public class CrabgrassDecorator extends TreeDecorator {
    public static final MapCodec<CrabgrassDecorator> CODEC = RecordCodecBuilder.mapCodec((codec) -> {
        return codec.group(Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter((instance) -> {
            return instance.probability;
        }), BlockStateProvider.CODEC.fieldOf("block_provider").forGetter((instance) -> {
            return instance.blockProvider;
        })).apply(codec, CrabgrassDecorator::new);
    });
    private final float probability;
    private final BlockStateProvider blockProvider;

    public CrabgrassDecorator(float probability, BlockStateProvider blockProvider) {
        this.probability = probability;
        this.blockProvider = blockProvider;
    }

    public void place(TreeDecorator.Context context) {
        RandomSource random = context.random();

        for (BlockPos pos : context.leaves()) {
            if (random.nextFloat() < this.probability) {
                if (context.isAir(pos.above())) {
                    context.setBlock(pos.above(), this.blockProvider.getState(random, pos.above()));
                }
            }
        }
    }

    protected @NotNull TreeDecoratorType<?> type() {
        return (TreeDecoratorType) GCFeatures.CRABGRASS.get();
    }
}
