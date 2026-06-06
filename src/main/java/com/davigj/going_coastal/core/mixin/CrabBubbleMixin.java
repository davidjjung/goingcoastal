package com.davigj.going_coastal.core.mixin;

import com.ninni.spawn.server.entity.misc.CrabBubble;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(CrabBubble.class)
public abstract class CrabBubbleMixin {
    @Redirect(method = "handleBlockCollisions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z"))
    private boolean effectiveTurrets(Level level, Entity entity, AABB box) {
        if (level.noCollision(entity, box)) {
            return true;
        }
        for (BlockPos pos : BlockPos.betweenClosed(
                Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ),
                Mth.floor(box.maxX), Mth.floor(box.maxY), Mth.floor(box.maxZ))) {
            BlockState state = level.getBlockState(pos);
            if (!state.isAir() && !state.is(BlockTags.LEAVES)) {
                VoxelShape shape = state.getCollisionShape(level, pos, CollisionContext.of(entity));
                if (!shape.isEmpty() && Shapes.joinIsNotEmpty(
                        shape.move(pos.getX(), pos.getY(), pos.getZ()),
                        Shapes.create(box),
                        BooleanOp.AND)) {
                    return false;
                }
            }
        }
        return true;
    }
}