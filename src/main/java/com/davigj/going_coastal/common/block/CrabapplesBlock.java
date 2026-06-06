package com.davigj.going_coastal.common.block;

import com.davigj.going_coastal.core.other.compat.SpawnCompat;
import com.davigj.going_coastal.core.registry.GCBlocks;
import com.davigj.going_coastal.core.registry.GCItems;
import com.mojang.serialization.MapCodec;
import com.ninni.spawn.server.entity.mob.CoastalCrab;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

import static com.davigj.going_coastal.core.other.compat.ModConstants.SPAWN;

public class CrabapplesBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;
    public static final VoxelShape[] shapesByAge;
    public CrabapplesBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return null;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.above(2)).is(GCBlocks.CRABAPPLE_FRUIT_LEAVES);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE});
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        VoxelShape voxelshape = this.shapesByAge[(Integer)state.getValue(AGE)];
        return voxelshape.move(vec3.x, vec3.y, vec3.z);
    }

    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return super.getOcclusionShape(state, level, pos);
    }

    protected @NotNull InteractionResult useWithoutItem(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if ((Integer)state.getValue(AGE) != this.getMaxAge()) {
            return InteractionResult.PASS;
        } else {
            if (player.isCreative() && player.getInventory().hasAnyMatching((stack) -> {
                return stack.getItem() == GCItems.CRABAPPLES.asItem();
            })) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, (level.random.nextFloat() - level.random.nextFloat()) * 1.4F + 2.0F);
            } else {
                ItemStack fruitStack = new ItemStack((ItemLike)GCItems.CRABAPPLES);
                if (!player.addItem(fruitStack)) {
                    player.drop(fruitStack, false);
                } else {
                    level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, (level.random.nextFloat() - level.random.nextFloat()) * 1.4F + 2.0F);
                }
            }
            if (SPAWN) {
                SpawnCompat.crabbyCrabs(level, pos, player);
            }

            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    public int getMaxAge() {
        return 3;
    }

    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        level.destroyBlock(hit.getBlockPos(), !projectile.isOnFire());
    }

    protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (explosion.canTriggerBlocks()) {
            level.destroyBlock(pos, true);
        }
        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }

    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return GCItems.CRABAPPLES.asItem().getDefaultInstance();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return (Integer)state.getValue(AGE) < this.getMaxAge();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlockAndUpdate(pos, (BlockState)state.setValue(AGE, (Integer)state.getValue(AGE) + 1));
    }

    static {
        AGE = BlockStateProperties.AGE_3;
        shapesByAge = new VoxelShape[]{
                Block.box(6.0, 11.0, 7.0, 10.0, 15.0, 11.0),
                Block.box(6.0, 11.0, 7.0, 10.0, 15.0, 11.0),
                Block.box(5.5, 10.0, 6.5, 10.5, 15.0, 11.5),
                Block.box(5.5, 10.0, 6.5, 10.5, 15.0, 11.5)};
    }
}
