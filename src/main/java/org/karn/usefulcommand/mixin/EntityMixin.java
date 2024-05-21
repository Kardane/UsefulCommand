package org.karn.usefulcommand.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract World getWorld();

    @Inject(method = "removePassenger", at = @At("TAIL"))
    private void removePassenger$karnscmd(Entity passenger, CallbackInfo callbackInfo)
    {
        if(!passenger.getWorld().isClient && passenger instanceof PlayerEntity)
            ((ServerPlayerEntity) passenger).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(passenger));
    }

    @Inject(method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z", at = @At("TAIL"))
    private void startRidingForce$karnscmd(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir)
    {
        if(!entity.getWorld().isClient && entity instanceof PlayerEntity)
            ((ServerPlayerEntity)entity).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(entity));
    }
}
