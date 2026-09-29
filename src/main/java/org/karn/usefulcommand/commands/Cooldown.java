package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;


public  class Cooldown {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess) {
        dispatcher.register(literal("cooldown")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("player", EntityArgument.player())
                        .then(argument("item", ItemArgument.item(commandRegistryAccess))
                                .then(argument("cooldown", IntegerArgumentType.integer())
                                        .executes(ctx ->{
                                            return setCooldown(ctx.getSource(), EntityArgument.getPlayer(ctx,"player"), ItemArgument.getItem(ctx, "item"), ctx.getArgument("cooldown", Integer.class));
                                        })
                                )
                        )
                ));
    }

    private static int setCooldown(CommandSourceStack source, Player player, ItemInput item, int duration) {
        player.getCooldowns().addCooldown(item.item().value().getDefaultInstance(), duration);
        source.sendSuccess(() ->Component.literal("Set Cooldown to ")
                .append(player.getDisplayName())
                .append("'s ")
                .append(item.item().value().getName(item.item().value().getDefaultInstance()))
                .append("for "+duration+" ticks"), false);
        return duration;
    }

}
