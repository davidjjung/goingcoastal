package com.davigj.going_coastal.core.other;

import com.davigj.going_coastal.core.registry.GCBlocks;
import com.teamabnormals.blueprint.core.util.DataUtil;
import net.minecraft.world.level.block.Block;

public class GCCompat {
    public GCCompat() {
    }

    public static void registerCompat() {
        registerFlammables();
    }

    public static void registerFlammables() {
        DataUtil.registerFlammable((Block) GCBlocks.CRABGRASS.get(), 60, 100);
        DataUtil.registerFlammable((Block) GCBlocks.COASTAL_CRABGRASS.get(), 60, 100);
        DataUtil.registerFlammable((Block) GCBlocks.CRABAPPLE_SAPLING.get(), 60, 100);
        DataUtil.registerFlammable((Block) GCBlocks.CRABAPPLE_LEAVES.get(), 30, 60);
        DataUtil.registerFlammable((Block) GCBlocks.CRABAPPLE_FRUIT_LEAVES.get(), 30, 60);
        DataUtil.registerFlammable((Block) GCBlocks.FLOWERING_CRABAPPLE_LEAVES.get(), 30, 60);
    }
}
