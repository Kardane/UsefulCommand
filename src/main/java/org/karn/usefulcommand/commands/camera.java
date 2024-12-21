package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class camera {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("cameraset")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("entity", EntityArgumentType.entity())
                        .executes(ctx -> {
                            camerSet(ctx.getSource(), EntityArgumentType.getEntity(ctx, "entity"));
                            return 1;
                        })
                ));
    }

    private static int camerSet(ServerCommandSource source, Entity entity) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        player.setCameraEntity(entity);
        return 1;
    }
}
