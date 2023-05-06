package org.karn.usefulcommand;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.karn.usefulcommand.commands.*;


public class UsefulCommand implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, ignored, ignored1) -> {
            Fire.register(dispatcher);
            Freeze.register(dispatcher);
            Absorption.register(dispatcher);
            Gravity.register(dispatcher);
            Invulnerable.register(dispatcher);
            Motion.register(dispatcher);
            PlayerAbility.register(dispatcher);
        });

    }



}