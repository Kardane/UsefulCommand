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

public  class Invulnerable {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("invulnerable")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("entity", EntityArgumentType.entity())
                        .then(argument("on/off", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    return invulnerableChange(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), BoolArgumentType.getBool(ctx,"on/off"));
                                })
                        )
        ));
    }

    private static int invulnerableChange(ServerCommandSource source, Entity entity, boolean status) {
        entity.setInvulnerable(status);
        source.sendFeedback(() ->Text.literal("Invulnerable: ").append(String.valueOf(status)), false);
        return entity.isInvulnerable() ? 1 : 0;
    }

}