package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MouseItem {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("hotbar")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("entity", EntityArgumentType.entity())
                        .then(CommandManager.literal("set")
                            .then(argument("num", IntegerArgumentType.integer(0,8))
                                .executes(ctx -> {
                                    return hotbarSet(ctx.getSource(), ctx.getSource().getPlayer(), ctx.getArgument("num",Integer.class));
                                })
                            )
                        )
                        .then(CommandManager.literal("get")
                                .executes(ctx -> {
                                    ctx.getSource().sendFeedback(() -> Text.literal("Selected Slot: ").append(String.valueOf(ctx.getSource().getPlayer().getInventory().selectedSlot)), false);
                                    return ctx.getSource().getPlayer().getInventory().selectedSlot;
                                })
                        )
                ));
    }

    private static int hotbarSet(ServerCommandSource source, ServerPlayerEntity player, int slot) {
        player.getInventory().selectedSlot = slot;
        source.getServer().getPlayerManager().sendPlayerStatus(player);
        source.sendFeedback(() -> Text.literal("Selected Slot: ").append(String.valueOf(player.getInventory().selectedSlot)), false);
        return player.getInventory().selectedSlot;
    }
}
