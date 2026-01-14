package org.karn.usefulcommand.commands;

import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MotionUpdate {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("motionupdate")
                .requires(Permissions.require("usefulcommand.admin", 2))
                .then(argument("entity", EntityArgumentType.entity())
                        .executes(ctx -> {
                            return motionUpdate(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"));
                        })
                ));
    }

    private static int motionUpdate(ServerCommandSource source, Entity entity) {
        entity.velocityDirty = true;
        source.sendFeedback(() ->Text.literal("Updated Motion for: ").append(String.valueOf(entity.getDisplayName())), false);
        return 1;
    }
}

