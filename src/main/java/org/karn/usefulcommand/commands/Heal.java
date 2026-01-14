package org.karn.usefulcommand.commands;

import me.lucko.fabric.api.permissions.v0.Permissions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Heal {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("heal")
				.requires(Permissions.require("usefulcommand.admin", 2))
				.then(argument("entity", EntityArgumentType.entity())
						.then(argument("amount", FloatArgumentType.floatArg(0F))
								.executes(ctx -> {
									Entity entity = EntityArgumentType.getEntity(ctx, "entity");
									if (entity instanceof LivingEntity) {
										return setHeal(ctx.getSource(), (LivingEntity) entity,
												FloatArgumentType.getFloat(ctx, "amount"));
									} else {
										ctx.getSource().sendError(Text.literal("Target must be a living entity!"));
										return 0;
									}
								}))));
	}

	private static int setHeal(ServerCommandSource source, LivingEntity entity, float healamount) {
		entity.heal(healamount);
		source.sendFeedback(() -> Text.literal("Healed: ").append(String.valueOf(healamount)), false);
		return (int) entity.getHealth();
	}

}

