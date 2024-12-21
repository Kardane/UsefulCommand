package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.DamageTiltS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Ptime {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("ptime")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("player", EntityArgumentType.player())
                        .then(argument("time", IntegerArgumentType.integer(0))
                                .executes(ctx -> {
                                    return setPtime(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), ctx.getArgument("time", Integer.class));
                                })
                        )
                ));
    }

    private static int setPtime(ServerCommandSource source, PlayerEntity entity, int time) {
        ServerPlayerEntity player = (ServerPlayerEntity) entity;
        player.networkHandler.sendPacket(new WorldTimeUpdateS2CPacket(time, 0, false));

        source.sendFeedback(() -> Text.literal("Set Time: ").append(String.valueOf(time)), false);
        return time;
    }
}
