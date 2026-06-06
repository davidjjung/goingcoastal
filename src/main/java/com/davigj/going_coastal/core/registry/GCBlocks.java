package com.davigj.going_coastal.core.registry;

import com.davigj.going_coastal.common.block.CrabappleLeavesBlock;
import com.davigj.going_coastal.common.block.CrabappleSaplingBlock;
import com.davigj.going_coastal.common.block.CrabapplesBlock;
import com.davigj.going_coastal.common.block.CrabgrassBlock;
import com.davigj.going_coastal.core.GoingCoastal;
import com.davigj.going_coastal.core.other.GCTreeGrowers;
import com.teamabnormals.blueprint.core.util.PropertyUtil;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Supplier;

import static com.davigj.going_coastal.core.GoingCoastal.MOD_ID;

public class GCBlocks {
    public static final BlockSubRegistryHelper BLOCKS;
    public static final DeferredBlock<Block> CRABGRASS;
    public static final DeferredBlock<Block> CRABAPPLE_SAPLING;
    public static final DeferredBlock<Block> POTTED_CRABAPPLE_SAPLING;
    public static final DeferredBlock<Block> COASTAL_CRABGRASS;
    public static final DeferredBlock<Block> CRABAPPLES;
    public static final DeferredBlock<Block> CRABAPPLE_LEAVES;
    public static final DeferredBlock<Block> CRABAPPLE_FRUIT_LEAVES;
    public static final DeferredBlock<Block> FLOWERING_CRABAPPLE_LEAVES;

    static {
        BLOCKS = (BlockSubRegistryHelper) GoingCoastal.REGISTRY_HELPER.getBlockSubHelper();
        CRABGRASS = BLOCKS.createBlock("crabgrass", () -> {
            return new CrabgrassBlock(Blocks.SHORT_GRASS.properties());
        });
        COASTAL_CRABGRASS = BLOCKS.createBlock("coastal_crabgrass", () -> {
            return new CrabgrassBlock(Blocks.SHORT_GRASS.properties());
        });
        CRABAPPLE_SAPLING = BLOCKS.createBlock("crabapple_sapling", () -> {
            return new CrabappleSaplingBlock(GCTreeGrowers.CRABAPPLE, Blocks.DARK_OAK_SAPLING.properties());
        });
        POTTED_CRABAPPLE_SAPLING = BLOCKS.createBlockNoItem("potted_crabapple_sapling", () -> {
            return new FlowerPotBlock((Block)CRABAPPLE_SAPLING.get(), PropertyUtil.flowerPot(new FeatureFlag[0]));
        });
        CRABAPPLES = BLOCKS.createBlock("crabapples_fruit", () -> {
            return new CrabapplesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).randomTicks().instabreak()
                    .sound(SoundType.AZALEA).pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XYZ).dynamicShape());
        });
        CRABAPPLE_LEAVES = BLOCKS.createBlock("crabapple_leaves", () -> {
            return new LeavesBlock(Blocks.AZALEA_LEAVES.properties());
        });
        CRABAPPLE_FRUIT_LEAVES = BLOCKS.createBlock("crabapple_fruit_leaves", () -> {
            return new CrabappleLeavesBlock(Blocks.AZALEA_LEAVES.properties());
        });
        FLOWERING_CRABAPPLE_LEAVES = BLOCKS.createBlock("flowering_crabapple_leaves", () -> {
            return new LeavesBlock(Blocks.FLOWERING_AZALEA_LEAVES.properties());
        });
    }

    public static void setupTabEditors() {
        CreativeModeTabContentsPopulator.mod(MOD_ID).tab(CreativeModeTabs.NATURAL_BLOCKS)
                .addItemsAfter(Ingredient.of(new ItemLike[]{Blocks.LILY_OF_THE_VALLEY}),
                        new Supplier[]{CRABGRASS, COASTAL_CRABGRASS})
                .addItemsBefore(Ingredient.of(new ItemLike[]{Blocks.AZALEA_LEAVES}),
                        new Supplier[]{CRABAPPLE_LEAVES, FLOWERING_CRABAPPLE_LEAVES})
                .addItemsBefore(Ingredient.of(new ItemLike[]{Blocks.AZALEA}),
                        new Supplier[]{CRABAPPLE_SAPLING})
        ;
    }
}
