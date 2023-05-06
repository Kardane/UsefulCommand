package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public  class Freeze {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("freeze")
                .requires(source -> source.hasPermissionLevel(4))
                .then(argument("entity", EntityArgumentType.entity())
                .then(CommandManager.literal("get")
                        .executes(ctx -> {
                            return getFreeze(ctx.getSource(),EntityArgumentType.getEntity(ctx,"entity"));
                        })
                )
                .then(CommandManager.literal("set")
                        .then(argument("duration", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    return setFreeze(ctx.getSource(), EntityArgumentType.getEntity(ctx, "entity"), ctx.getArgument("duration", Integer.class), true);
                                })
                        )
                )
                .then(CommandManager.literal("add")
                        .then(argument("duration", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                      return setFreeze(ctx.getSource(), EntityArgumentType.getEntity(ctx, "entity"), ctx.getArgument("duration", Integer.class), false);
                                })
                        )
                )
        ));
    }

    private static int getFreeze(ServerCommandSource source, Entity entity) {
        source.sendFeedback(Text.literal("Freeze Tick: ").append(String.valueOf(entity.getFrozenTicks())), false);
        return entity.getFrozenTicks();
    }

    private static int setFreeze(ServerCommandSource source, Entity entity, int duration, boolean override) {
        if(override){
            entity.setFrozenTicks(duration);
        } else {
            int finalFreezeticks = entity.getFrozenTicks() + duration;
            entity.setFrozenTicks(finalFreezeticks);
        }

        source.sendFeedback(Text.literal("Freeze Tick: ").append(String.valueOf(entity.getFrozenTicks())), false);
        return entity.getFrozenTicks();
    }

}