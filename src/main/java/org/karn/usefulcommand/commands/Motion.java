package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public  class Motion {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("motion")
                .requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_GAMEMASTERS))
                .then(argument("entity", EntityArgument.entity())
                .then(Commands.literal("add")
                        .then(argument("x", FloatArgumentType.floatArg())
                        .then(argument("y", FloatArgumentType.floatArg())
                        .then(argument("z", FloatArgumentType.floatArg())
                        .executes(ctx -> {
                              return addMotion(ctx.getSource(), EntityArgument.getEntity(ctx,"entity"), ctx.getArgument("x", Float.class) ,ctx.getArgument("y", Float.class),ctx.getArgument("z", Float.class));
                        }))))
                )
                .then(Commands.literal("set")
                        .then(argument("x", FloatArgumentType.floatArg())
                        .then(argument("y", FloatArgumentType.floatArg())
                        .then(argument("z", FloatArgumentType.floatArg())
                        .executes(ctx -> {
                            return setMotion(ctx.getSource(), EntityArgument.getEntity(ctx,"entity"), ctx.getArgument("x", Float.class) ,ctx.getArgument("y", Float.class),ctx.getArgument("z", Float.class));
                        }))))
                )
                .then(Commands.literal("forward")
                        .then(argument("speed", DoubleArgumentType.doubleArg())
                                .executes(ctx ->{
                                    return setMotionFacing(ctx.getSource(), EntityArgument.getEntity(ctx,"entity"), DoubleArgumentType.getDouble(ctx,"speed"));
                                })
                        )
                )));
    }

    private static int addMotion(CommandSourceStack source, Entity entity, float x ,float y, float z) {
        entity.push(x,y,z);
        if(entity.isAlwaysTicking()){
            entity.syncVelocity = true;
        }
        return 1;
    }

    private static int setMotion(CommandSourceStack source, Entity entity, float x ,float y, float z) {
        entity.setDeltaMovement(x,y,z);
        if(entity.isAlwaysTicking()){
            entity.syncVelocity = true;
        }
        return 1;
    }

    private static int setMotionFacing(CommandSourceStack source, Entity entity, double speed) {
        Vec3 vec3d = entity.getLookAngle();
        entity.setDeltaMovement((vec3d.x+0.01)*speed,(vec3d.y+0.01)*speed,(vec3d.z+0.01)*speed);
        if(entity.isAlwaysTicking()){
            entity.syncVelocity = true;
        }
        return 1;
    }

}