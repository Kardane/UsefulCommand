package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public  class Glide {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("glide")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("player", EntityArgument.player())
                                .executes(ctx -> {
                                    return startFallfly(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"));
                                })
                ));
    }

    private static int startFallfly(CommandSourceStack source, Player player) {
        player.startFallFlying();
        source.sendSuccess(() ->Component.literal("Started gliding"), false);
        return 1;
    }

}
