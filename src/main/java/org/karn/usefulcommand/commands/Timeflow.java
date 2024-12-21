package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.UpdateTickRateS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Timeflow {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("timeflow")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("player", EntityArgumentType.player())
                        .then(argument("rate", FloatArgumentType.floatArg(0))
                                .then(argument("freeze", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            return setTickrate(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), ctx.getArgument("rate", Float.class), ctx.getArgument("freeze", Boolean.class));
                                        })
                                )
                                .executes(ctx -> {
                                    return setTickrate(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), ctx.getArgument("rate", Float.class),false);
                                })
                        )
                        .then(literal("reset")
                                .executes(ctx -> {
                                    return setTickrate(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), 20, false);
                                })
                        )
                        .then(literal("sync")
                                .executes(ctx -> {
                                    return setTickrate(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), ctx.getSource().getServer().getTickManager().getTickRate(), true);
                                })
                        )
                ));
    }

    private static int setTickrate(ServerCommandSource source, PlayerEntity entity, float time, boolean override) {
        ServerPlayerEntity player = (ServerPlayerEntity) entity;
        player.networkHandler.sendPacket(new UpdateTickRateS2CPacket(time, override));

        source.sendFeedback(() -> Text.literal("Set Tick Rate: ").append(String.valueOf(time)), false);
        return (int) time;
    }
}
