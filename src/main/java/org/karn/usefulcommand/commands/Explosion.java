package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Explosion {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("explosion")
                .requires(source -> source.hasPermissionLevel(2))
                        .then(argument("pos", Vec3ArgumentType.vec3(true))
                                .then(argument("power", FloatArgumentType.floatArg())
                                        .executes(ctx -> {
                                            return explode(ctx.getSource(), null, Vec3ArgumentType.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), false, World.ExplosionSourceType.NONE);
                                        })
                                        .then(argument("entity", EntityArgumentType.entity())
                                                .executes(ctx -> {
                                                    return explode(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), Vec3ArgumentType.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), false, World.ExplosionSourceType.NONE);
                                                })
                                                .then(argument("fire", BoolArgumentType.bool())
                                                        .executes(ctx -> {
                                                            return explode(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), Vec3ArgumentType.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), BoolArgumentType.getBool(ctx,"fire"), World.ExplosionSourceType.NONE);
                                                        })
                                                        .then(CommandManager.literal("none")
                                                                .executes(ctx -> {
                                                                    return explode(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), Vec3ArgumentType.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), BoolArgumentType.getBool(ctx,"fire"), World.ExplosionSourceType.NONE);
                                                                })
                                                        )
                                                        .then(CommandManager.literal("block")
                                                                .executes(ctx -> {
                                                                    return explode(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), Vec3ArgumentType.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), BoolArgumentType.getBool(ctx,"fire"), World.ExplosionSourceType.BLOCK);
                                                                }))
                                                        .then(CommandManager.literal("mob")
                                                                .executes(ctx -> {
                                                                    return explode(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), Vec3ArgumentType.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), BoolArgumentType.getBool(ctx,"fire"), World.ExplosionSourceType.MOB);
                                                                }))
                                                        .then(CommandManager.literal("tnt")
                                                                .executes(ctx -> {
                                                                    return explode(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), Vec3ArgumentType.getVec3(ctx, "pos"), FloatArgumentType.getFloat(ctx, "power"), BoolArgumentType.getBool(ctx,"fire"), World.ExplosionSourceType.TNT);
                                                                }))
                                                )
                                        )

                                )
                        )
                );
    }

    private static int explode(ServerCommandSource source, Entity entity, Vec3d pos, float power, boolean createFire, World.ExplosionSourceType sourceType) {
        World world = source.getWorld();
        world.createExplosion(entity, pos.getX(), pos.getY(), pos.getZ(), power, createFire, sourceType);
        source.sendFeedback(() ->Text.literal("Boom!"), false);
        return 1;
    }
}
