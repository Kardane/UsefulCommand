package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public  class Food {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("food")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("player", EntityArgumentType.player())
                        .then(CommandManager.literal("hunger")
                                .then(CommandManager.literal("get")
                                        .executes(ctx -> {
                                              return getFood(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"));
                                        })
                                )
                                .then(CommandManager.literal("set")
                                        .then(argument("amount", IntegerArgumentType.integer(0))
                                                .executes(ctx -> {
                                                    return setFood(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), ctx.getArgument("amount", Integer.class), true);
                                                })
                                        )
                                )
                                .then(CommandManager.literal("add")
                                        .then(argument("amount", IntegerArgumentType.integer(0))
                                                .executes(ctx -> {
                                                    return setFood(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), ctx.getArgument("amount", Integer.class), false);
                                                })
                                        )
                                )
                        )
                        .then(CommandManager.literal("saturation")
                                .then(CommandManager.literal("get")
                                        .executes(ctx -> {
                                            return getSaturation(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"));
                                        })
                                )
                                .then(CommandManager.literal("set")
                                        .then(argument("amount", FloatArgumentType.floatArg(0.001F))
                                                .executes(ctx -> {
                                                    return setSaturation(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), ctx.getArgument("amount", Float.class), true);
                                                })
                                        )
                                )
                                .then(CommandManager.literal("add")
                                        .then(argument("amount", FloatArgumentType.floatArg(0.001F))
                                                .executes(ctx -> {
                                                    return setSaturation(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), ctx.getArgument("amount", Float.class), false);
                                                })
                                        )
                                )
                        )
                )
        );
    }
    private static int getFood(ServerCommandSource source, PlayerEntity player) {
        source.sendFeedback(Text.literal("Hunger: ").append(String.valueOf(player.getHungerManager().getFoodLevel())), false);
        return player.getHungerManager().getFoodLevel();
    }
    private static int setFood(ServerCommandSource source, PlayerEntity player, int hunger, boolean override) {
        if(override) {
            player.getHungerManager().setFoodLevel(hunger);
        } else {
            int finalhunger = player.getHungerManager().getFoodLevel() + hunger;
            player.getHungerManager().setFoodLevel(finalhunger);
        }
        source.sendFeedback(Text.literal("Hunger: ").append(String.valueOf(player.getHungerManager().getFoodLevel())), false);
        return player.getHungerManager().getFoodLevel();
    }

    private static int getSaturation(ServerCommandSource source, PlayerEntity player) {
        source.sendFeedback(Text.literal("Saturation: ").append(String.valueOf(player.getHungerManager().getSaturationLevel())), false);
        return (int) player.getHungerManager().getSaturationLevel();
    }
    private static int setSaturation(ServerCommandSource source, PlayerEntity player, float saturation, boolean override) {
        if(override) {
            player.getHungerManager().setSaturationLevel(saturation);
        } else {
            float finalsaturation = player.getHungerManager().getSaturationLevel() + saturation;
            player.getHungerManager().setSaturationLevel(finalsaturation);
        }
        source.sendFeedback(Text.literal("Saturation: ").append(String.valueOf(player.getHungerManager().getSaturationLevel())), false);
        return (int) player.getHungerManager().getSaturationLevel();
    }

}