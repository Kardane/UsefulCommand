package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public  class Absorption {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("absorption")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("entity", EntityArgumentType.entity())
                .then(CommandManager.literal("get")
                        .executes(ctx -> {
                            return getAbsorption(ctx.getSource(), (LivingEntity) EntityArgumentType.getEntity(ctx,"entity"));
                        })
                )
                .then(CommandManager.literal("set")
                        .then(argument("amount", FloatArgumentType.floatArg(0.1F))
                                .executes(ctx -> {
                                    return setAbsorption(ctx.getSource(), (LivingEntity) EntityArgumentType.getEntity(ctx, "entity"), ctx.getArgument("amount", Float.class), true);
                                })
                        )
                )
                .then(CommandManager.literal("add")
                        .then(argument("amount", FloatArgumentType.floatArg(0.1F))
                                .executes(ctx -> {
                                      return setAbsorption(ctx.getSource(), (LivingEntity) EntityArgumentType.getEntity(ctx, "entity"), ctx.getArgument("amount", Float.class), false);
                                })
                        )
                )
        ));
    }

    private static int getAbsorption(ServerCommandSource source, LivingEntity entity) {
        source.sendFeedback(Text.literal("Absorption Amount: ").append(String.valueOf(entity.getAbsorptionAmount())), false);
        return (int) entity.getAbsorptionAmount();
    }

    private static int setAbsorption(ServerCommandSource source, LivingEntity entity, float amount, boolean override) {
        if(override){
            entity.setAbsorptionAmount(amount);
        } else {
            float finalabsorption = entity.getAbsorptionAmount() + amount;
            entity.setAbsorptionAmount(finalabsorption);
        }

        source.sendFeedback(Text.literal("Absorption Amount: ").append(String.valueOf(entity.getAbsorptionAmount())), false);
        return (int) entity.getAbsorptionAmount();
    }

}