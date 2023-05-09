package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public  class Gravity {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("gravity")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("entity", EntityArgumentType.entity())
                .then(CommandManager.literal("on")
                        .executes(ctx -> {
                            return gravityChange(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), true);
                        })
                )
                .then(CommandManager.literal("off")
                        .executes(ctx -> {
                            return gravityChange(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), false);
                        })
                )
        ));
    }

    private static int gravityChange(ServerCommandSource source, Entity entity, boolean status) {
        entity.setNoGravity(status);
        source.sendFeedback(Text.literal("Gravity: ").append(String.valueOf(status)), false);
        return 1;
    }

}