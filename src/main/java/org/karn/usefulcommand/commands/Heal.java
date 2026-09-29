package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class Heal {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(literal("heal")
				.requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
				.then(argument("entity", EntityArgument.entity())
						.then(argument("amount", FloatArgumentType.floatArg(0F))
								.executes(ctx -> {
									Entity entity = EntityArgument.getEntity(ctx, "entity");
									if (entity instanceof LivingEntity) {
										return setHeal(ctx.getSource(), (LivingEntity) entity,
												FloatArgumentType.getFloat(ctx, "amount"));
									} else {
										ctx.getSource().sendFailure(Component.literal("Target must be a living entity!"));
										return 0;
									}
								}))));
	}

	private static int setHeal(CommandSourceStack source, LivingEntity entity, float healamount) {
		entity.heal(healamount);
		source.sendSuccess(() -> Component.literal("Healed: ").append(String.valueOf(healamount)), false);
		return (int) entity.getHealth();
	}

}
