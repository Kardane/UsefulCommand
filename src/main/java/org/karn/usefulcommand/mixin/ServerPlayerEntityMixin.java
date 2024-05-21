package org.karn.usefulcommand.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Final
    private ServerPlayerEntity player;
    @Redirect(method = "changeGameMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;stopRiding()V"))
    private void changeGameMode$karnscmd(ServerPlayerEntity player) {
        if(player.hasPassengers())
            player.getFirstPassenger().stopRiding();
    }

    @Inject(method="onDisconnect", at=@At("HEAD"))
    public void onDisconnect(CallbackInfo ci) {
        if(player.hasVehicle() && player.getVehicle() instanceof PlayerEntity)
            player.stopRiding();
    }
}
