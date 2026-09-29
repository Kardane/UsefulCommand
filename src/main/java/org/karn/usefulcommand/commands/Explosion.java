package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class Explosion {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("explosion")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                        .then(argument("pos", Vec3Argument.vec3(true))
                                .then(argument("power", FloatArgumentType.floatArg())
                                        .executes(ctx -> {
                                            return explode(ctx.getSource(), null, Vec3Argument.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), false, Level.ExplosionInteraction.NONE);
                                        })
                                        .then(argument("entity", EntityArgument.entity())
                                                .executes(ctx -> {
                                                    return explode(ctx.getSource(), EntityArgument.getEntity(ctx,"entity"), Vec3Argument.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), false, Level.ExplosionInteraction.NONE);
                                                })
                                                .then(argument("fire", BoolArgumentType.bool())
                                                        .executes(ctx -> {
                                                            return explode(ctx.getSource(), EntityArgument.getEntity(ctx,"entity"), Vec3Argument.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), BoolArgumentType.getBool(ctx,"fire"), Level.ExplosionInteraction.NONE);
                                                        })
                                                        .then(Commands.literal("none")
                                                                .executes(ctx -> {
                                                                    return explode(ctx.getSource(), EntityArgument.getEntity(ctx,"entity"), Vec3Argument.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), BoolArgumentType.getBool(ctx,"fire"), Level.ExplosionInteraction.NONE);
                                                                })
                                                        )
                                                        .then(Commands.literal("block")
                                                                .executes(ctx -> {
                                                                    return explode(ctx.getSource(), EntityArgument.getEntity(ctx,"entity"), Vec3Argument.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), BoolArgumentType.getBool(ctx,"fire"), Level.ExplosionInteraction.BLOCK);
                                                                }))
                                                        .then(Commands.literal("mob")
                                                                .executes(ctx -> {
                                                                    return explode(ctx.getSource(), EntityArgument.getEntity(ctx,"entity"), Vec3Argument.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), BoolArgumentType.getBool(ctx,"fire"), Level.ExplosionInteraction.MOB);
                                                                }))
                                                        .then(Commands.literal("tnt")
                                                                .executes(ctx -> {
                                                                    return explode(ctx.getSource(), EntityArgument.getEntity(ctx,"entity"), Vec3Argument.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), BoolArgumentType.getBool(ctx,"fire"), Level.ExplosionInteraction.TNT);
                                                                }))
                                                )
                                        )

                                )
                        )
                );
    }

    private static int explode(CommandSourceStack source, Entity entity, Vec3 pos, float power, boolean createFire, Level.ExplosionInteraction sourceType) {
        Level world = source.getLevel();
        world.explode(entity, pos.x(), pos.y(), pos.z(), power, createFire, sourceType);
        source.sendSuccess(() ->Component.literal("Boom!"), false);
        return (int) power;
    }
}
