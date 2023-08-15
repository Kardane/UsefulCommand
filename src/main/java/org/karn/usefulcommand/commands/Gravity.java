package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public  class Gravity {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("nogravity")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("entity", EntityArgumentType.entity())
                        .then(argument("on/off", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    return gravityChange(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), BoolArgumentType.getBool(ctx,"on/off"));
                                })
                        )
        ));
    }

    private static int gravityChange(ServerCommandSource source, Entity entity, boolean status) {
        entity.setNoGravity(status);
        source.sendFeedback(() ->Text.literal("NoGravity: ").append(String.valueOf(status)), false);
        return 1;
    }

}