package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public  class PlayerAbility {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("player_ability")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("player", EntityArgument.player())
                .then(Commands.literal("fly")
                        .then(argument("on/off", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    return setFly(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"), BoolArgumentType.getBool(ctx,"on/off"));
                                })
                        )
                        .then(argument("speed", FloatArgumentType.floatArg(0.1F))
                                .executes(ctx -> {
                                    return setFlySpeed(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"), ctx.getArgument("speed", Float.class));
                                })
                        )
                )
                .then(Commands.literal("walk")
                        .then(argument("speed", FloatArgumentType.floatArg(0.01F))
                                .executes(ctx -> {
                                    return setWalkSpeed(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"), ctx.getArgument("speed", Float.class));
                                })
                        )
                )
                .then(Commands.literal("allowBuild")
                        .then(argument("on/off", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    return setBuild(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"), BoolArgumentType.getBool(ctx,"on/off"));
                                })
                        )
                )
                .then(Commands.literal("instantBreak")
                        .then(argument("on/off", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    return setinstantBreak(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"), BoolArgumentType.getBool(ctx,"on/off"));
                                })
                        )
                )));
    }

    private static int setFly(CommandSourceStack source, Player player, boolean status) {
        player.getAbilities().mayfly = status;
        player.getAbilities().flying = status;
        player.onUpdateAbilities();
        source.sendSuccess(() ->Component.literal("Fly: ").append(String.valueOf(status)), false);
        return 1;
    }

    private static int setFlySpeed(CommandSourceStack source, Player player, float speed) {
        player.getAbilities().setFlyingSpeed(speed);
        player.onUpdateAbilities();
        source.sendSuccess(() ->Component.literal("Fly Speed: ").append(String.valueOf(speed)), false);
        return 1;
    }

    private static int setWalkSpeed(CommandSourceStack source, Player player, float speed) {
        player.getAbilities().setWalkingSpeed(speed);
        player.onUpdateAbilities();
        source.sendSuccess(() ->Component.literal("Walk Speed: ").append(String.valueOf(speed)), false);
        return 1;
    }

    private static int setBuild(CommandSourceStack source, Player player, boolean status) {
        player.getAbilities().mayBuild = status;
        player.onUpdateAbilities();
        source.sendSuccess(() ->Component.literal("BuildMode: ").append(String.valueOf(status)), false);
        return 1;
    }

    private static int setinstantBreak(CommandSourceStack source, Player player, boolean status) {
        player.getAbilities().instabuild = status;
        player.onUpdateAbilities();
        source.sendSuccess(() ->Component.literal("InstantBreak: ").append(String.valueOf(status)), false);
        return 1;
    }
}