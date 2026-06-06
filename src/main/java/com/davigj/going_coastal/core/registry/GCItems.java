package com.davigj.going_coastal.core.registry;

import com.davigj.going_coastal.core.GoingCoastal;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.function.Supplier;

public class GCItems {
    public static final ItemSubRegistryHelper ITEMS;
    public static final DeferredItem<Item> CRABAPPLES;

    static {
        ITEMS = (ItemSubRegistryHelper) GoingCoastal.REGISTRY_HELPER.getItemSubHelper();
        CRABAPPLES = ITEMS.createItem("crabapples", () -> {
        return new Item((new Item.Properties()).food(GCFoods.CRABAPPLES));
        });
    }

    public static void setupTabEditors() {
        CreativeModeTabContentsPopulator.mod(GoingCoastal.MOD_ID).tab(CreativeModeTabs.FOOD_AND_DRINKS).addItemsBefore(Ingredient.of(new ItemLike[]{Items.CHORUS_FRUIT}), new Supplier[]{CRABAPPLES});
    }

    public static final class GCFoods {
        public static final FoodProperties CRABAPPLES = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.1F).build();
    }
}
