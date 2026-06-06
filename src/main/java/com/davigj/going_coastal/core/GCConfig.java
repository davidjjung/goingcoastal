package com.davigj.going_coastal.core;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class GCConfig {
    public static class Common {
        public final ModConfigSpec.ConfigValue<Boolean> crabbyTrees;

        Common (ModConfigSpec.Builder builder) {
            builder.push("common");
            builder.push("compat");
            builder.push("spawn");
            crabbyTrees = builder.comment("Crabapple trees may occasionally generate with coastal crabs on top").define("crabbyTrees", true);
            builder.pop();
            builder.pop();
            builder.pop();
        }
    }

    static final ModConfigSpec COMMON_SPEC;
    public static final GCConfig.Common COMMON;


    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(GCConfig.Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}

