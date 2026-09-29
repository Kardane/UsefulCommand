package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class DamageTilt {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("damagetilt")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("player", EntityArgument.player())
                        .then(argument("angle", FloatArgumentType.floatArg())
                                .executes(ctx -> {
                                    return tilt(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx.getArgument("angle", Float.class));
                                })
                        )
                ));
    }

    private static int tilt(CommandSourceStack source, Player entity, float angle) {
        ServerPlayer player = (ServerPlayer) entity;
        player.connection.send(new ClientboundHurtAnimationPacket(entity.getId(), angle));

        source.sendSuccess(() ->Component.literal("Tilt Angle: ").append(String.valueOf(angle)), false);
        return (int) angle;
    }
}
