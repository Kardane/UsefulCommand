package org.karn.usefulcommand.commands;

import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public  class Glide {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("glide")
                .requires(Permissions.require("usefulcommand.admin", 2))
                .then(argument("player", EntityArgumentType.player())
                                .executes(ctx -> {
                                    return startFallfly(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"));
                                })
                ));
    }

    private static int startFallfly(ServerCommandSource source, PlayerEntity player) {
        player.startGliding();
        source.sendFeedback(() ->Text.literal("Started gliding"), false);
        return 1;
    }

}

