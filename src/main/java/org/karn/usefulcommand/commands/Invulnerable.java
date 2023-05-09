package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
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
                .then(CommandManager.literal("on")
                        .executes(ctx -> {
                            return invulnerableChange(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), true);
                        })
                )
                .then(CommandManager.literal("off")
                        .executes(ctx -> {
                            return invulnerableChange(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), false);
                        })
                )
        ));
    }

    private static int invulnerableChange(ServerCommandSource source, Entity entity, boolean status) {
        entity.setInvulnerable(status);
        source.sendFeedback(Text.literal("Invulnerable: ").append(String.valueOf(status)), false);
        return 1;
    }

}