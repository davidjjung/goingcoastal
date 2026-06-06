package com.davigj.going_coastal.core.mixin;

import com.davigj.going_coastal.core.other.GCItemTags;
import com.davigj.going_coastal.core.registry.GCBlocks;
import com.ninni.spawn.server.entity.mob.CoastalCrab;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static com.davigj.going_coastal.core.other.compat.SpawnCompat.FRUITY;

@Pseudo
@Mixin(CoastalCrab.class)
public abstract class CoastalCrabMixin extends Animal {
    protected CoastalCrabMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract ResourceLocation getVariant();

    @Shadow public abstract void setPregnant(boolean pregnant);

    @Shadow public abstract void setBirthingCooldown(int cooldown);

    @Shadow public abstract boolean isPregnant();

    @Shadow public abstract boolean isBuried();

    @Shadow public abstract int getBuryingCooldown();

    @Shadow public abstract void setBuryingCooldown(int cooldown);

    @Inject(method = "isFood", at = @At("RETURN"), cancellable = true)
    private void isCrabappleFood(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (this.getVariant().equals(FRUITY)) {
            cir.setReturnValue(stack.is(GCItemTags.FRUITY_COASTAL_CRAB_FEEDS));
        }
    }

    @Inject(method = "spawnChildFromBreeding", at = @At("HEAD"), cancellable = true)
    private void fruitsOfLabor(ServerLevel level, Animal mate, CallbackInfo ci) {
        if (this.getVariant().equals(FRUITY)) {
            if (gc$canFindSapling(this.blockPosition()) || (gc$canFindSapling(mate.blockPosition()))) {
                Optional.ofNullable(this.getLoveCause()).or(() -> {
                    return Optional.ofNullable(mate.getLoveCause());
                }).ifPresent((player) -> {
                    player.awardStat(Stats.ANIMALS_BRED);
                    CriteriaTriggers.BRED_ANIMALS.trigger(player, this, mate, (AgeableMob) null);
                });
                this.setAge(6000);
                mate.setAge(6000);
                level.broadcastEntityEvent(this, (byte) 18);
                if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                    level.addFreshEntity(new ExperienceOrb(level, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
                }

                if (this.random.nextBoolean()) {
                    this.setPregnant(true);
                    this.setBirthingCooldown(2400);
                } else if (mate instanceof CoastalCrab crab) {
                    crab.setPregnant(true);
                    crab.setBirthingCooldown(2400);
                }
            } else {
                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER, this.getX(),
                            this.getY() + 0.5, this.getZ(), 5, 0.5, 0.5, 0.5, 0);
                    serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER, mate.getX(),
                            mate.getY() + 0.5, mate.getZ(), 5, 0.5, 0.5, 0.5, 0);
                }
                this.setAge(100);
                mate.setAge(100);
            }
            ci.cancel();
        }
    }

    @Unique
    private boolean gc$canFindSapling(BlockPos pos) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        blockpos$mutableblockpos.setWithOffset(pos, k, i, l);
                        if (this.level().getBlockState(blockpos$mutableblockpos).is(GCBlocks.CRABAPPLE_SAPLING.get())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        if (this.getVariant().equals(FRUITY)) return otherAnimal instanceof CoastalCrab mate && mate.getVariant().equals(FRUITY);
        return super.canMate(otherAnimal);
    }

    @Inject(method = "handleBurying", at = @At("HEAD"))
    private void noPregnantBurialsUnlessISaySo(CallbackInfo ci) {
        if (this.getVariant().equals(FRUITY) && this.isPregnant() && !this.isBuried()) {
            if (this.getBuryingCooldown() <= 20) {
                this.setBuryingCooldown(400);
            }
        }
    }

    @Inject(method = "emerge", at = @At("HEAD"))
    private void annoyance(CallbackInfo ci) {
        if (this.getVariant().equals(FRUITY) && this.isPregnant()) {
            this.setPregnant(false);
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER, this.getX(),
                        this.getY() + 0.5, this.getZ(), 9, 0.5, 0.5, 0.5, 0);
            }
        }
    }

}
