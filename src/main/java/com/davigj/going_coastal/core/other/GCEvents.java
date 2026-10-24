package com.davigj.going_coastal.core.other;


import com.davigj.going_coastal.core.GoingCoastal;
import com.davigj.going_coastal.core.other.compat.SpawnCompat;
import com.davigj.going_coastal.core.registry.GCBlocks;
import com.davigj.going_coastal.core.registry.GCFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.level.BlockGrowFeatureEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Iterator;
import java.util.Objects;

import static com.davigj.going_coastal.core.other.compat.ModConstants.NML;
import static com.davigj.going_coastal.core.other.compat.ModConstants.SPAWN;

@EventBusSubscriber(modid = GoingCoastal.MOD_ID)
public class GCEvents {
    @SubscribeEvent
    public static void onSaplingGrowth(BlockGrowFeatureEvent event) {
        if (!Objects.requireNonNull(event.getFeature()).is(GCFeatures.GCConfiguredFeatures.CRABAPPLE)) return;
        BlockPos pos = event.getPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        LevelAccessor level = event.getLevel();

        Iterator<BlockPos> it = BlockPos.betweenClosedStream(x - 8, y - 12, z - 8, x + 8, y + 12, z + 8).iterator();
        int fruitsPresent = 0;
        while(it.hasNext()) {
            BlockPos bp = (BlockPos)it.next();
            BlockState state = level.getBlockState(bp);
            if (state.is(GCBlocks.CRABAPPLES)) {
                ++fruitsPresent;
            }
        }

        RandomSource random = event.getRandom();
        boolean fruiting = NML && (fruitsPresent >= 12 || (fruitsPresent >= 6 && random.nextBoolean()) || (fruitsPresent > 0 && random.nextInt(10) == 0));
        boolean crab = SPAWN && SpawnCompat.isCrabby(pos, level);
        if (fruiting && crab) {
            event.setFeature(GCFeatures.GCConfiguredFeatures.FRUIT_BABY_CRABBY_CRABAPPLE);
            return;
        }
        if (fruiting) {
            event.setFeature(GCFeatures.GCConfiguredFeatures.FRUIT_CRABAPPLE_01);
            return;
        }
        if (crab) {
            event.setFeature(GCFeatures.GCConfiguredFeatures.BABY_CRABBY_CRABAPPLE);
        }
    }

    @SubscribeEvent
    public static void fruityPriorities(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (SPAWN && entity instanceof LivingEntity living) {
            SpawnCompat.filterGoals(living);
        }
    }

    @SubscribeEvent
    public static void onFruityCrabTick(EntityTickEvent.Post event) {
        if (SPAWN) SpawnCompat.pregnantPause(event.getEntity());
    }
}