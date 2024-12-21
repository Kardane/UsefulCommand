package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.DamageTiltS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class DamageTilt {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("damagetilt")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("player", EntityArgumentType.player())
                        .then(argument("angle", FloatArgumentType.floatArg())
                                .executes(ctx -> {
                                    return tilt(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), ctx.getArgument("angle", Float.class));
                                })
                        )
                ));
    }

    private static int tilt(ServerCommandSource source, PlayerEntity entity, float angle) {
        ServerPlayerEntity player = (ServerPlayerEntity) entity;
        player.networkHandler.sendPacket(new DamageTiltS2CPacket(entity.getId(), angle));

        source.sendFeedback(() ->Text.literal("Tilt Angle: ").append(String.valueOf(angle)), false);
        return (int) angle;
    }
}
