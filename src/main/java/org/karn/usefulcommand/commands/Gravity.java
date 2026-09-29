package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public  class Gravity {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("nogravity")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("entity", EntityArgument.entity())
                        .then(argument("on/off", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    return gravityChange(ctx.getSource(), EntityArgument.getEntity(ctx,"entity"), BoolArgumentType.getBool(ctx,"on/off"));
                                })
                        )
        ));
    }

    private static int gravityChange(CommandSourceStack source, Entity entity, boolean status) {
        entity.setNoGravity(status);
        source.sendSuccess(() ->Component.literal("NoGravity: ").append(String.valueOf(status)), false);
        return entity.isNoGravity() ? 1 : 0;
    }

}