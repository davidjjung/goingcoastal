package com.davigj.going_coastal.core.other.compat;

import com.davigj.going_coastal.common.block.CrabappleSaplingBlock;
import com.davigj.going_coastal.core.other.GCItemTags;
import com.davigj.going_coastal.core.registry.GCBlocks;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnSoundEvents;
import com.ninni.spawn.registry.SpawnTags;
import com.ninni.spawn.server.entity.mob.CoastalCrab;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SpawnCompat {
    public static final ResourceLocation FRUITY = ResourceLocation.fromNamespaceAndPath("going_coastal", "fruity");

    public static void addCrabs(List<BlockPos> potentialSpawns, WorldGenLevel level, RandomSource random, boolean baby) {
        Collections.shuffle(potentialSpawns, new Random(level.getSeed()));

        int entitiesToSpawn = 2 + random.nextInt(2);
        if (baby) entitiesToSpawn += 1 + random.nextInt(3);
        int spawnedCount = 0;
        for (BlockPos spawnPos : potentialSpawns) {
            if (spawnedCount >= entitiesToSpawn) break;
            CoastalCrab crab = SpawnEntityType.COASTAL_CRAB.get().create(level.getLevel());
            if (crab != null) {
                crab.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, random.nextFloat() * 360.0F, 0.0F);
                crab.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.NATURAL, null);
                if (!baby) crab.bury();
                crab.setBaby(baby);
                crab.setVariant(FRUITY);
                level.addFreshEntity(crab);
                spawnedCount++;
            }
        }
    }

    public static boolean isCrabby(BlockPos pos, LevelAccessor level) {
        List<CoastalCrab> crabs = level.getEntitiesOfClass(CoastalCrab.class, new AABB(pos.below()).inflate(3.5));
        List<CoastalCrab> pregnantCrabs = new ArrayList<>(Collections.emptyList());
        for (CoastalCrab crab : crabs) {
            if (crab.getVariant().equals(FRUITY) && crab.isPregnant() && crab.isBuried()) {
                pregnantCrabs.add(crab);
            }
        }
        if (!pregnantCrabs.isEmpty()) {
            CoastalCrab crab = pregnantCrabs.getFirst();
            if (crab.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, crab.getX(),
                        crab.getY() + 0.5, crab.getZ(), 8, 0.5, 0.5, 0.5, 0);
            }
            crab.setPregnant(false);
            crab.setBuryingCooldown(0);
            return true;
        }
        return false;
    }

    public static void filterGoals(LivingEntity living) {
        if (living instanceof CoastalCrab crab) {
            crab.goalSelector.getAvailableGoals().removeIf(wrappedGoal -> wrappedGoal.getGoal() instanceof TemptGoal);
            TagKey<Item> temptTag = SpawnTags.COASTAL_CRAB_TEMPTS;
            if (crab.getVariant().equals(FRUITY)) {
                temptTag = GCItemTags.FRUITY_COASTAL_CRAB_TEMPTS;
                crab.goalSelector.addGoal(0, new BuryBirthGoal(crab, 1.0));
            }
            crab.goalSelector.addGoal(1, new TemptGoal(crab, 1.25, Ingredient.of(temptTag), false));
        }
    }

    public static void crabbyCrabs(Level level, BlockPos pos, LivingEntity target) {
        if (target instanceof Player player && player.isCreative()) return;
        int mercy = 2;
        for (CoastalCrab crab : level.getEntitiesOfClass(CoastalCrab.class, new AABB(pos).inflate(8))) {
            if (crab.getVariant().equals(FRUITY) && !crab.isBaby() && mercy > 0 && !crab.isPregnant()) {
                if (crab.isBuried()) crab.emerge(true);
                crab.playSound((SoundEvent) SpawnSoundEvents.COASTAL_CRAB_IDLE.get(), 1.1F, crab.getVoicePitch() + 0.1F);
                crab.setTarget(target);
                mercy--;
            }
        }
    }

    public static void pregnantPause(Entity entity) {
        if (!entity.level().isClientSide && entity instanceof CoastalCrab crab) {
            if (crab.tickCount % 100 == 0 && crab.getVariant().equals(FRUITY) && crab.isPregnant() && crab.isBuried()) {
                ServerLevel level = (ServerLevel) crab.level();
                BlockPos crabPos = crab.blockPosition();

                BlockPos saplingPos = gimmeCrabSaplin(level, crabPos);

                if (saplingPos != null) {
                    BlockState plantState = level.getBlockState(saplingPos);
                    Block plantBlock = plantState.getBlock();
                    if (plantBlock instanceof BonemealableBlock growable) {
                        if (growable.isValidBonemealTarget(level, saplingPos, plantState) &&
                                CommonHooks.canCropGrow(level, saplingPos, plantState, true)) {
                            level.levelEvent(1505, saplingPos, 15);
                            if (growable.isBonemealSuccess(level, level.random, saplingPos, plantState)) {
                                growable.performBonemeal(level, level.random, saplingPos, plantState);
                                CommonHooks.fireCropGrowPost(level, saplingPos, plantState);
                            }
                        }
                    }
                } else {
                    crab.emerge(true);
                }
            }
        }
    }

    private static BlockPos gimmeCrabSaplin(Level level, BlockPos center) {
        BlockPos nearestPos = null;
        double closestDistance = Double.MAX_VALUE;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int x = -3; x <= 3; x++) {
            for (int y = -1; y <= 2; y++) {
                for (int z = -3; z <= 3; z++) {
                    mutablePos.setWithOffset(center, x, y, z);
                    BlockState state = level.getBlockState(mutablePos);

                    if (state.is(GCBlocks.CRABAPPLE_SAPLING.get())) {
                        double distance = center.distSqr(mutablePos);

                        if (distance < closestDistance) {
                            closestDistance = distance;
                            nearestPos = mutablePos.immutable();
                        }
                    }
                }
            }
        }
        return nearestPos;
    }

    static class BuryBirthGoal extends MoveToBlockGoal {
        protected int ticksWaited;
        private BlockPos targetSaplingPos;

        public BuryBirthGoal(CoastalCrab crab, double speedIn) {
            super((PathfinderMob) crab, speedIn, 16);
        }

        @Override
        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            if (!level.getBlockState(pos.above()).isAir() || !level.getBlockState(pos).is(SpawnTags.COASTAL_CRAB_BURY_BLOCKS)) {
                return false;
            }

            for (int x = -2; x <= 2; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -2; z <= 2; z++) {
                        BlockPos checkPos = pos.above().offset(x, y, z);

                        if (level.getBlockState(checkPos).getBlock() instanceof CrabappleSaplingBlock) {
                            this.targetSaplingPos = checkPos;
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        @Override
        public void tick() {
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= 60) {
                    this.onReachedTarget();
                } else {
                    if (this.ticksWaited > 10 && this.targetSaplingPos != null) {
                        if (this.ticksWaited % 16 == 0) this.mob.playSound(SpawnSoundEvents.COASTAL_CRAB_IDLE.get(), 0.7F, 0.65F + this.mob.getRandom().nextFloat() * 0.2F);
                        this.mob.getLookControl().setLookAt(this.targetSaplingPos.getX() + 0.5D,
                                this.targetSaplingPos.getY() + 0.5D, this.targetSaplingPos.getZ() + 0.5D);
                    }
                    ++this.ticksWaited;
                }
            }
            super.tick();
        }

        protected void onReachedTarget() {
            Level level = this.mob.level();
            CoastalCrab crab = (CoastalCrab) this.mob;

            if (this.targetSaplingPos != null) {
                crab.getLookControl().setLookAt(this.targetSaplingPos.getX() + 0.5D,
                        this.targetSaplingPos.getY() + 0.5D, this.targetSaplingPos.getZ() + 0.5D);
            }

            if (crab.tickCount % 30 == 0) {
                if (!level.isClientSide) {
                    crab.bury();
                    crab.setBuryingCooldown(24000);
                    crab.setBirthingCooldown(24000);
                    if (this.mob.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.mob.getX(),
                                this.mob.getY() + 0.5, this.mob.getZ(), 8, 0.5, 0.5, 0.5, 0);
                    }
                    this.stop();
                }
            }
        }

        @Override
        public boolean canUse() {
            CoastalCrab crab = (CoastalCrab) this.mob;
            return super.canUse() && crab.getVariant().equals(FRUITY) && crab.isPregnant() && !crab.isBuried();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !((CoastalCrab)this.mob).isBuried();
        }

        @Override
        public void start() {
            super.start();
            this.ticksWaited = 0;
            this.targetSaplingPos = null;
        }

        @Override
        public double acceptedDistance() {
            return 1.4D;
        }
    }
}
