package org.karn.usefulcommand;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.karn.usefulcommand.commands.*;


public class UsefulCommand implements ModInitializer {

    @Override
    public void onInitialize() {
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
            Random.register(dispatcher);
            Fallfly.register(dispatcher);
            Cooldown.register(dispatcher,commandRegistryAccess);
        });

    }



}