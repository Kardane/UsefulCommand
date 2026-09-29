package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class Camera {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(literal("cameraset")
				.requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
				.then(argument("entity", EntityArgument.entity())
						.executes(ctx -> {
							camerSet(ctx.getSource(), EntityArgument.getEntity(ctx, "entity"));
							return 1;
						})));
	}

	private static int camerSet(CommandSourceStack source, Entity entity) throws CommandSyntaxException {
		ServerPlayer player = source.getPlayerOrException();
		player.setCamera(entity);
		return 1;
	}
}
