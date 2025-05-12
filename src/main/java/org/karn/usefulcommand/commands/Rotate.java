package org.karn.usefulcommand.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec2ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

import java.util.Set;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Rotate {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("rotate")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("entity", EntityArgumentType.entity())
                        .then(literal("add")
                                .then(argument("rotation", Vec2ArgumentType.vec2())
                                        .executes(ctx -> {
                                            return rotateAdd(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), Vec2ArgumentType.getVec2(ctx, "rotation"));
                                        })
                                )
                        )
                        .then(literal("set")
                                .then(argument("rotation", Vec2ArgumentType.vec2())
                                        .executes(ctx -> {
                                            return rotateSet(ctx.getSource(), EntityArgumentType.getEntity(ctx,"entity"), Vec2ArgumentType.getVec2(ctx, "rotation"));
                                        })
                                )
                        )

                ));
    }

    private static int rotateAdd(ServerCommandSource source, Entity entity, Vec2f rotation) {
        entity.setYaw(rotation.x);
        entity.setHeadYaw(rotation.x);
        entity.setPitch(rotation.y);
        entity.updateTrackedHeadRotation(rotation.y,1);
        if (entity instanceof PlayerEntity){
            ((ServerPlayerEntity) entity).networkHandler.sendPacket(new PlayerPositionLookS2CPacket(0,0,0,rotation.x,rotation.y, Set.of(PositionFlag.values()),-1));
        }
        source.sendFeedback(() ->Text.literal("Added Rotation: "+rotation.x+"/"+rotation.y), false);
        return 1;
    }

    private static int rotateSet(ServerCommandSource source, Entity entity, Vec2f rotation) {
        if (entity instanceof PlayerEntity){
            ((ServerPlayerEntity) entity).networkHandler.sendPacket(new PlayerPositionLookS2CPacket(0,0,0,rotation.x-entity.getYaw(),rotation.y-entity.getPitch(), Set.of(PositionFlag.values()),-1));
        }
        entity.setYaw(rotation.x);
        entity.setHeadYaw(rotation.x);
        entity.setPitch(rotation.y);
        entity.updateTrackedHeadRotation(rotation.y,1);
        source.sendFeedback(() ->Text.literal("Set Rotation: "+rotation.x+"/"+rotation.y), false);
        return 1;
    }

}