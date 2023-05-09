package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public  class Random {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("random")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("min", IntegerArgumentType.integer())
                        .then(argument("max", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    return getRandom(ctx.getSource(), ctx.getArgument("min", int.class), ctx.getArgument("max", int.class));
                                })
                        )
                ));
    }

    private static int getRandom(ServerCommandSource source, int min, int max) {
        int output = (int) (Math.random() * max + min);
        source.sendFeedback(Text.literal("Random Output: ").append(String.valueOf(output)), false);
        return output;
    }
}

