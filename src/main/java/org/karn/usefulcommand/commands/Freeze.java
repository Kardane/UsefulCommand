package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public  class Freeze {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("freeze")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("entity", EntityArgument.entity())
                .then(Commands.literal("get")
                        .executes(ctx -> {
                            return getFreeze(ctx.getSource(),EntityArgument.getEntity(ctx,"entity"));
                        })
                )
                .then(Commands.literal("set")
                        .then(argument("duration", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    return setFreeze(ctx.getSource(), EntityArgument.getEntity(ctx, "entity"), ctx.getArgument("duration", Integer.class), true);
                                })
                        )
                )
                .then(Commands.literal("add")
                        .then(argument("duration", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                      return setFreeze(ctx.getSource(), EntityArgument.getEntity(ctx, "entity"), ctx.getArgument("duration", Integer.class), false);
                                })
                        )
                )
        ));
    }

    private static int getFreeze(CommandSourceStack source, Entity entity) {
        source.sendSuccess(() ->Component.literal("Freeze Tick: ").append(String.valueOf(entity.getTicksFrozen())), false);
        return entity.getTicksFrozen();
    }

    private static int setFreeze(CommandSourceStack source, Entity entity, int duration, boolean override) {
        if(override){
            entity.setTicksFrozen(duration);
        } else {
            int finalFreezeticks = entity.getTicksFrozen() + duration;
            entity.setTicksFrozen(finalFreezeticks);
        }

        source.sendSuccess(() ->Component.literal("Freeze Tick: ").append(String.valueOf(entity.getTicksFrozen())), false);
        return entity.getTicksFrozen();
    }

}