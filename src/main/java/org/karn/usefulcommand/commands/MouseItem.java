package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class MouseItem {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("hotbar")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("player", EntityArgument.player())
                        .then(Commands.literal("set")
                            .then(argument("num", IntegerArgumentType.integer(0,8))
                                .executes(ctx -> {
                                    return hotbarSet(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"), ctx.getArgument("num",Integer.class));
                                })
                            )
                        )
                        .then(Commands.literal("get")
                                .executes(ctx -> {
                                    ctx.getSource().sendSuccess(() -> Component.literal("Selected Slot: ").append(String.valueOf(ctx.getSource().getPlayer().getInventory().getSelectedSlot())), false);
                                    return ctx.getSource().getPlayer().getInventory().getSelectedSlot();
                                })
                        )
                ));
    }

    private static int hotbarSet(CommandSourceStack source, ServerPlayer player, int slot) {
        player.getInventory().setSelectedSlot(slot);
        source.getServer().getPlayerList().sendAllPlayerInfo(player);
        source.sendSuccess(() -> Component.literal("Selected Slot: ").append(String.valueOf(player.getInventory().getSelectedSlot())), false);
        return player.getInventory().getSelectedSlot();
    }
}
