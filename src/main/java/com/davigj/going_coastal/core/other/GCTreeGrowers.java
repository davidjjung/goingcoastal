package com.davigj.going_coastal.core.other;

import com.davigj.going_coastal.core.registry.GCFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class GCTreeGrowers {
    public static final TreeGrower CRABAPPLE;

    public GCTreeGrowers() {
    }

    static {
        CRABAPPLE = new TreeGrower("going_coastal:crabapple", Optional.empty(),
                Optional.of(GCFeatures.GCConfiguredFeatures.CRABAPPLE), Optional.empty());
    }
}
