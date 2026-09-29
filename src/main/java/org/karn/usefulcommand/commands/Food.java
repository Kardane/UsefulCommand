package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public  class Food {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("food")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("player", EntityArgument.player())
                        .then(Commands.literal("hunger")
                                .then(Commands.literal("get")
                                        .executes(ctx -> {
                                              return getFood(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"));
                                        })
                                )
                                .then(Commands.literal("set")
                                        .then(argument("amount", IntegerArgumentType.integer(0))
                                                .executes(ctx -> {
                                                    return setFood(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"), ctx.getArgument("amount", Integer.class), true);
                                                })
                                        )
                                )
                                .then(Commands.literal("add")
                                        .then(argument("amount", IntegerArgumentType.integer(0))
                                                .executes(ctx -> {
                                                    return setFood(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"), ctx.getArgument("amount", Integer.class), false);
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("saturation")
                                .then(Commands.literal("get")
                                        .executes(ctx -> {
                                            return getSaturation(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"));
                                        })
                                )
                                .then(Commands.literal("set")
                                        .then(argument("amount", FloatArgumentType.floatArg(0.001F))
                                                .executes(ctx -> {
                                                    return setSaturation(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"), ctx.getArgument("amount", Float.class), true);
                                                })
                                        )
                                )
                                .then(Commands.literal("add")
                                        .then(argument("amount", FloatArgumentType.floatArg(0.001F))
                                                .executes(ctx -> {
                                                    return setSaturation(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"), ctx.getArgument("amount", Float.class), false);
                                                })
                                        )
                                )
                        )
                )
        );
    }
    private static int getFood(CommandSourceStack source, Player player) {
        source.sendSuccess(() ->Component.literal("Hunger: ").append(String.valueOf(player.getFoodData().getFoodLevel())), false);
        return player.getFoodData().getFoodLevel();
    }
    private static int setFood(CommandSourceStack source, Player player, int hunger, boolean override) {
        if(override) {
            player.getFoodData().setFoodLevel(hunger);
        } else {
            int finalhunger = player.getFoodData().getFoodLevel() + hunger;
            player.getFoodData().setFoodLevel(finalhunger);
        }
        source.sendSuccess(() ->Component.literal("Hunger: ").append(String.valueOf(player.getFoodData().getFoodLevel())), false);
        return player.getFoodData().getFoodLevel();
    }

    private static int getSaturation(CommandSourceStack source, Player player) {
        source.sendSuccess(() ->Component.literal("Saturation: ").append(String.valueOf(player.getFoodData().getSaturationLevel())), false);
        return (int) player.getFoodData().getSaturationLevel();
    }
    private static int setSaturation(CommandSourceStack source, Player player, float saturation, boolean override) {
        if(override) {
            player.getFoodData().setSaturation(saturation);
        } else {
            float finalsaturation = player.getFoodData().getSaturationLevel() + saturation;
            player.getFoodData().setSaturation(finalsaturation);
        }
        source.sendSuccess(() ->Component.literal("Saturation: ").append(String.valueOf(player.getFoodData().getSaturationLevel())), false);
        return (int) player.getFoodData().getSaturationLevel();
    }

}