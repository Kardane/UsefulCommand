package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public  class Motion {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("motion")
                .then(argument("entity", EntityArgumentType.entity())
                .then(CommandManager.literal("add")
                        .then(argument("x", FloatArgumentType.floatArg())
                        .then(argument("y", FloatArgumentType.floatArg())
                        .then(argument("z", FloatArgumentType.floatArg())
                        .executes(ctx -> {
                              return addMotion(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), ctx.getArgument("x", Float.class) ,ctx.getArgument("y", Float.class),ctx.getArgument("z", Float.class));
                        }))))
                )
                .then(CommandManager.literal("set")
                        .then(argument("x", FloatArgumentType.floatArg())
                        .then(argument("y", FloatArgumentType.floatArg())
                        .then(argument("z", FloatArgumentType.floatArg())
                        .executes(ctx -> {
                            return setMotion(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), ctx.getArgument("x", Float.class) ,ctx.getArgument("y", Float.class),ctx.getArgument("z", Float.class));
                        }))))
                )
                .then(CommandManager.literal("forward")
                        .then(argument("speed", DoubleArgumentType.doubleArg())
                                .executes(ctx ->{
                                    return setMotionFacing(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), DoubleArgumentType.getDouble(ctx,"speed"));
                                })
                        )
                )));
    }

    private static int addMotion(ServerCommandSource source, Entity entity, float x ,float y, float z) {
        entity.addVelocity(x,y,z);
        if(entity.isPlayer()){
            entity.velocityModified = true;
        }
        return 1;
    }

    private static int setMotion(ServerCommandSource source, Entity entity, float x ,float y, float z) {
        entity.setVelocity(x,y,z);
        if(entity.isPlayer()){
            entity.velocityModified = true;
        }
        return 1;
    }

    private static int setMotionFacing(ServerCommandSource source, Entity entity, double speed) {
        Vec3d vec3d = entity.getRotationVector();
        entity.setVelocity((vec3d.x+0.01)*speed,(vec3d.y+0.01)*speed,(vec3d.z+0.01)*speed);
        if(entity.isPlayer()){
            entity.velocityModified = true;
        }
        return 1;
    }

}