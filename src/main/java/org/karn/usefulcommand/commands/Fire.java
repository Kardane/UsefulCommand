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

public  class Fire {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("fire")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("entity", EntityArgumentType.entity())
                .then(CommandManager.literal("get")
                        .executes(ctx -> {
                            return getFire(ctx.getSource(),EntityArgumentType.getEntity(ctx,"entity"));
                        })
                )
                .then(CommandManager.literal("set")
                        .then(argument("duration", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    return setFire(ctx.getSource(), EntityArgumentType.getEntity(ctx, "entity"), ctx.getArgument("duration", Integer.class), true);
                                })
                        )
                )
                .then(CommandManager.literal("add")
                        .then(argument("duration", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                      return setFire(ctx.getSource(), EntityArgumentType.getEntity(ctx, "entity"), ctx.getArgument("duration", Integer.class), false);
                                })
                        )
                )
        ));
    }

    private static int getFire(ServerCommandSource source, Entity entity) {
        source.sendFeedback(Text.literal("Fire Tick: ").append(String.valueOf(entity.getFireTicks())), false);
        return entity.getFireTicks();
    }

    private static int setFire(ServerCommandSource source, Entity entity, int duration, boolean override) {
        if(override){
            entity.setFireTicks(duration);
        } else {
            int finalfiretick = entity.getFireTicks() + duration;
            entity.setFireTicks(finalfiretick);
        }

        source.sendFeedback(Text.literal("Fire Tick: ").append(String.valueOf(entity.getFireTicks())), false);
        return entity.getFireTicks();
    }

}