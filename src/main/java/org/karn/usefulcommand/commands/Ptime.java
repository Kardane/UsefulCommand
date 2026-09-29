package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.clock.ClockNetworkState;

import java.util.HashMap;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class Ptime {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("ptime")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("player", EntityArgument.player())
                        .then(argument("time", IntegerArgumentType.integer(0))
                                .executes(ctx -> {
                                    return setPtime(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx.getArgument("time", Integer.class));
                                })
                        )
                ));
    }

    private static int setPtime(CommandSourceStack source, Player entity, int time) {
        ServerPlayer player = (ServerPlayer) entity;
        ClientboundSetTimePacket current = player.level().clockManager().createFullSyncPacket();
        var clocks = new HashMap<>(current.clockUpdates());
        clocks.replaceAll((clock, state) -> new ClockNetworkState(time, 0, 0));
        player.connection.send(new ClientboundSetTimePacket(current.gameTime(), clocks));

        source.sendSuccess(() -> Component.literal("Set Time: ").append(String.valueOf(time)), false);
        return time;
    }
}
