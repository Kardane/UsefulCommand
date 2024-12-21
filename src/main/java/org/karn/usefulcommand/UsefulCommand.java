package org.karn.usefulcommand;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import org.karn.usefulcommand.commands.*;


public class UsefulCommand implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("Karn's UsefulCommand is initialized!");
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, ignored1) -> {
            Fire.register(dispatcher);
            Freeze.register(dispatcher);
            Absorption.register(dispatcher);
            Gravity.register(dispatcher);
            Invulnerable.register(dispatcher);
            Motion.register(dispatcher);
            PlayerAbility.register(dispatcher);
            Heal.register(dispatcher);
            Food.register(dispatcher);
            Glide.register(dispatcher);
            Cooldown.register(dispatcher,commandRegistryAccess);
            Explosion.register(dispatcher);
            MotionUpdate.register(dispatcher);
            camera.register(dispatcher);
            MouseItem.register(dispatcher);
            DamageTilt.register(dispatcher);
            Ptime.register(dispatcher);
            Timeflow.register(dispatcher);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
        {
            if(handler.player.hasVehicle() && handler.player.getVehicle() instanceof PlayerEntity)
                handler.player.stopRiding();
        });

    }



}