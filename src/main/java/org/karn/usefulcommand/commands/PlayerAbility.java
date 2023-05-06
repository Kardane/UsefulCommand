package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public  class PlayerAbility {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("player_ability")
                .then(argument("player", EntityArgumentType.player())
                .then(CommandManager.literal("fly")
                       .then(CommandManager.literal("on")
                                .executes(ctx -> {
                                       return setFly(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), true);
                                })
                       )
                        .then(CommandManager.literal("off")
                                .executes(ctx -> {
                                    return setFly(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), false);
                                })
                        )
                        .then(argument("speed", FloatArgumentType.floatArg(0.1F))
                                .executes(ctx -> {
                                    return setFlySpeed(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), ctx.getArgument("speed", Float.class));
                                })
                        )
                )
                .then(CommandManager.literal("walk")
                        .then(argument("speed", FloatArgumentType.floatArg(0.1F))
                                .executes(ctx -> {
                                    return setWalkSpeed(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), ctx.getArgument("speed", Float.class));
                                })
                        )
                )
                .then(CommandManager.literal("allowBuild")
                        .then(CommandManager.literal("on")
                                .executes(ctx -> {
                                    return setBuild(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), true);
                                })
                        )
                        .then(CommandManager.literal("off")
                                .executes(ctx -> {
                                    return setBuild(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), false);
                                })
                        )
                )
                .then(CommandManager.literal("instantBreak")
                        .then(CommandManager.literal("on")
                                .executes(ctx -> {
                                                    return setinstantBreak(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), true);
                                                })
                                        )
                        .then(CommandManager.literal("off")
                                                .executes(ctx -> {
                                                    return setinstantBreak(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), false);
                                                })
                                        )
                        )
                ));
    }

    private static int setFly(ServerCommandSource source, PlayerEntity player, boolean status) {
        player.getAbilities().allowFlying = status;
        player.getAbilities().flying = status;
        player.sendAbilitiesUpdate();
        source.sendFeedback(Text.literal("Fly: ").append(String.valueOf(status)), false);
        return 1;
    }

    private static int setFlySpeed(ServerCommandSource source, PlayerEntity player, float speed) {
        player.getAbilities().setFlySpeed(speed);
        player.sendAbilitiesUpdate();
        source.sendFeedback(Text.literal("Fly Speed: ").append(String.valueOf(speed)), false);
        return 1;
    }

    private static int setWalkSpeed(ServerCommandSource source, PlayerEntity player, float speed) {
        player.getAbilities().setWalkSpeed(speed);
        player.sendAbilitiesUpdate();
        source.sendFeedback(Text.literal("Walk Speed: ").append(String.valueOf(speed)), false);
        return 1;
    }

    private static int setBuild(ServerCommandSource source, PlayerEntity player, boolean status) {
        player.getAbilities().allowModifyWorld = status;
        player.sendAbilitiesUpdate();
        source.sendFeedback(Text.literal("BuildMode: ").append(String.valueOf(status)), false);
        return 1;
    }

    private static int setinstantBreak(ServerCommandSource source, PlayerEntity player, boolean status) {
        player.getAbilities().creativeMode = status;
        player.sendAbilitiesUpdate();
        source.sendFeedback(Text.literal("InstantBreak: ").append(String.valueOf(status)), false);
        return 1;
    }
}