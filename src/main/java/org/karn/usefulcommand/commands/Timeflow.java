package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTickingStatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class Timeflow {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("timeflow")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("player", EntityArgument.player())
                        .then(argument("rate", FloatArgumentType.floatArg(0))
                                .then(argument("freeze", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            return setTickrate(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx.getArgument("rate", Float.class), ctx.getArgument("freeze", Boolean.class));
                                        })
                                )
                                .executes(ctx -> {
                                    return setTickrate(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx.getArgument("rate", Float.class),false);
                                })
                        )
                        .then(literal("reset")
                                .executes(ctx -> {
                                    return setTickrate(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), 20, false);
                                })
                        )
                        .then(literal("sync")
                                .executes(ctx -> {
                                    return setTickrate(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx.getSource().getServer().tickRateManager().tickrate(), true);
                                })
                        )
                ));
    }

    private static int setTickrate(CommandSourceStack source, Player entity, float time, boolean override) {
        ServerPlayer player = (ServerPlayer) entity;
        player.connection.send(new ClientboundTickingStatePacket(time, override));

        source.sendSuccess(() -> Component.literal("Set Tick Rate: ").append(String.valueOf(time)), false);
        return (int) time;
    }
}
