package org.karn.usefulcommand.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
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
    @Final
    private Entity entity = (Entity) (Object) this;

    @Inject(method = "removePassenger", at = @At("TAIL"))
    private void removePassengeForcer$karnscmd(Entity passenger, CallbackInfo callbackInfo)
    {
        if(!passenger.getWorld().isClient && passenger instanceof PlayerEntity){
            ((ServerPlayerEntity)passenger).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(passenger));
        }
        if(!passenger.getWorld().isClient && entity instanceof PlayerEntity){
            ((ServerPlayerEntity)entity).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(entity));
        }
    }

    @Inject(method = "addPassenger", at = @At("TAIL"))
    private void addPassengerForce$karnscmd(Entity passenger, CallbackInfo ci)
    {
        if(!passenger.getWorld().isClient && passenger instanceof PlayerEntity){
            ((ServerPlayerEntity)passenger).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(passenger));
        }
        if(!passenger.getWorld().isClient && entity instanceof PlayerEntity){
            ((ServerPlayerEntity)entity).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(entity));
        }
    }

    @WrapOperation(
            method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;isSaveable()Z")
    )
    private boolean playerladder$allowRidingPlayers(EntityType instance, Operation<Boolean> original) {
        if(instance == EntityType.PLAYER) {
            return true;
        }else{
            return original.call(instance);
        }
    }
}
