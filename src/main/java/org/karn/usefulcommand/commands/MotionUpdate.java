package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class MotionUpdate {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("motionupdate")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("entity", EntityArgument.entity())
                        .executes(ctx -> {
                            return motionUpdate(ctx.getSource(), EntityArgument.getEntity(ctx,"entity"));
                        })
                ));
    }

    private static int motionUpdate(CommandSourceStack source, Entity entity) {
        entity.syncVelocity = true;
        source.sendSuccess(() ->Component.literal("Updated Motion for: ").append(String.valueOf(entity.getDisplayName())), false);
        return 1;
    }
}
