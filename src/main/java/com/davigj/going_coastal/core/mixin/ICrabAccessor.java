package com.davigj.going_coastal.core.mixin;

import com.ninni.spawn.server.entity.mob.CoastalCrab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Pseudo
@Mixin(CoastalCrab.class)
public interface ICrabAccessor {
    @Accessor
    boolean getDidShoot();

    @Invoker
    void callSetDidShoot();
}
