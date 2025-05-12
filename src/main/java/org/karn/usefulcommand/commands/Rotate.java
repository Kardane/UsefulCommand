package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec2ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Rotate {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("rotate")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("entity", EntityArgumentType.entity())
                        .then(argument("rotation", Vec2ArgumentType.vec2())
                                .executes(ctx -> {
                                    return rotate(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), Vec2ArgumentType.getVec2(ctx, "rotation"));
                                })
                        )
                ));
    }

    private static int rotate(ServerCommandSource source, Entity entity, Vec2f rotation) {
        source.sendFeedback(() ->Text.literal("Rotated: "+rotation.x+"/"+rotation.y), false);
        return 1;
    }

}