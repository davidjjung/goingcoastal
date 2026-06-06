package com.davigj.going_coastal.core.other;

import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.davigj.going_coastal.core.GoingCoastal.MOD_ID;

public class GCItemTags {
    public static final TagKey<Item> FRUITY_COASTAL_CRAB_FEEDS = itemTag("fruity_coastal_crab_feeds");
    public static final TagKey<Item> FRUITY_COASTAL_CRAB_TEMPTS = itemTag("fruity_coastal_crab_tempts");

    public GCItemTags() {
    }

    private static TagKey<Item> itemTag(String name) {
        return TagUtil.itemTag(MOD_ID, name);
    }
}
