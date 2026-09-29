package org.karn.usefulcommand.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "removePassenger", at = @At("TAIL"))
    private void removePassengeForcer$karnscmd(Entity passenger, CallbackInfo callbackInfo)
    {
        if(!passenger.level().isClientSide() && passenger instanceof Player){
            ((ServerPlayer)passenger).connection.send(new ClientboundSetPassengersPacket(passenger));
        }
        Entity vehicle = (Entity) (Object) this;
        if(!passenger.level().isClientSide() && vehicle instanceof ServerPlayer player){
            player.connection.send(new ClientboundSetPassengersPacket(vehicle));
        }
    }

    @Inject(method = "addPassenger", at = @At("TAIL"))
    private void addPassengerForce$karnscmd(Entity passenger, CallbackInfo ci)
    {
        if(!passenger.level().isClientSide() && passenger instanceof Player){
            ((ServerPlayer)passenger).connection.send(new ClientboundSetPassengersPacket(passenger));
        }
        Entity vehicle = (Entity) (Object) this;
        if(!passenger.level().isClientSide() && vehicle instanceof ServerPlayer player){
            player.connection.send(new ClientboundSetPassengersPacket(vehicle));
        }
    }

    @WrapOperation(
            method = "startRiding(Lnet/minecraft/world/entity/Entity;ZZ)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;canSerialize()Z")
    )
    private boolean playerladder$allowRidingPlayers(EntityType instance, Operation<Boolean> original) {
        if(instance == EntityTypes.PLAYER) {
            return true;
        }else{
            return original.call(instance);
        }
    }
}
