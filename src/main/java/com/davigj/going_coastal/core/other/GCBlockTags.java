package com.davigj.going_coastal.core.other;

import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static com.davigj.going_coastal.core.GoingCoastal.MOD_ID;

public class GCBlockTags {
    public static final TagKey<Block> CRABGRASS_MAY_PLACE_ON = blockTag("crabgrass_may_place_on");
    public static final TagKey<Block> CRABAPPLE_SAPLING_MAY_PLACE_ON = blockTag("crabapple_sapling_may_place_on");

    public GCBlockTags() {
    }

    private static TagKey<Block> blockTag(String name) {
        return TagUtil.blockTag(MOD_ID, name);
    }
}
