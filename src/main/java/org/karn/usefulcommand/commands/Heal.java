package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public  class Heal {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("heal")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("entity", EntityArgumentType.entity())
                        .then(argument("amount", FloatArgumentType.floatArg(0.001F))
                                .executes(ctx -> {
                                    return setheal(ctx.getSource(), (LivingEntity) EntityArgumentType.getEntity(ctx,"entity"), ctx.getArgument("amount", Float.class));
                                })
                        )
                ));
    }

    private static int setheal(ServerCommandSource source, LivingEntity entity, float healamount) {
        entity.heal(healamount);
        source.sendFeedback(Text.literal("Healed: ").append(String.valueOf(healamount)), false);
        return 1;
    }

}
