package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.command.argument.ItemStackArgumentType.itemStack;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public  class Cooldown {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("cooldown")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("player", EntityArgumentType.player())
                        .then(argument("item", ItemStackArgumentType.itemStack())
                                .then(argument("cooldown", IntegerArgumentType.integer(1))
                                        .executes(ctx ->{
                                            return setCooldown(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), ItemStackArgumentType.itemStack(ctx, "item"), ctx.getArgument("cooldown", Integer.class));
                                        })
                                )
                        )
                ));
    }

    private static int setCooldown(ServerCommandSource source, PlayerEntity player, ItemStack item, int duration) {
        Item coolItem = item.getItem();
        player.getItemCooldownManager().set(coolItem, duration);
        source.sendFeedback(Text.literal("Set Cooldown"), false);
        return 1;
    }

}
