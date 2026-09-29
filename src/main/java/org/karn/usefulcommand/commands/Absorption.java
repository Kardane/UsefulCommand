package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public  class Absorption {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("absorption")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("entity", EntityArgument.entity())
                .then(Commands.literal("get")
                        .executes(ctx -> {
                            return getAbsorption(ctx.getSource(), (LivingEntity) EntityArgument.getEntity(ctx,"entity"));
                        })
                )
                .then(Commands.literal("set")
                        .then(argument("amount", FloatArgumentType.floatArg(0F))
                                .executes(ctx -> {
                                    return setAbsorption(ctx.getSource(), (LivingEntity) EntityArgument.getEntity(ctx, "entity"), ctx.getArgument("amount", Float.class), true);
                                })
                        )
                )
                .then(Commands.literal("add")
                        .then(argument("amount", FloatArgumentType.floatArg(0F))
                                .executes(ctx -> {
                                      return setAbsorption(ctx.getSource(), (LivingEntity) EntityArgument.getEntity(ctx, "entity"), ctx.getArgument("amount", Float.class), false);
                                })
                        )
                )
        ));
    }

    private static int getAbsorption(CommandSourceStack source, LivingEntity entity) {
        source.sendSuccess(() -> Component.literal("Absorption Amount: ").append(String.valueOf(entity.getAbsorptionAmount())), false);
        return (int) entity.getAbsorptionAmount();
    }

    private static int setAbsorption(CommandSourceStack source, LivingEntity entity, float amount, boolean override) {
        entity.getAttributes().getInstance(Attributes.MAX_ABSORPTION).setBaseValue(amount);
        if(override){
            entity.setAbsorptionAmount(amount);
        } else {
            float finalabsorption = entity.getAbsorptionAmount() + amount;
            entity.setAbsorptionAmount(finalabsorption);
        }

        source.sendSuccess(() -> Component.literal("Absorption Amount: ").append(String.valueOf(entity.getAbsorptionAmount())), false);
        return (int) entity.getAbsorptionAmount();
    }

}