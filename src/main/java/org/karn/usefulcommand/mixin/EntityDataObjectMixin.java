package org.karn.usefulcommand.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.TagValueInput;

@Mixin(EntityDataAccessor.class)
public class EntityDataObjectMixin {
    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    @Final
    private Entity entity;

    @Inject(method="setData", at=@At("HEAD"), cancellable = true)
    void setNbt$karnscmd(CompoundTag nbt, CallbackInfo ci){
        if (this.entity instanceof Player){
            ProblemReporter errorReporter = new ProblemReporter.ScopedCollector(this.entity.problemPath(),LOGGER);
            TagValueInput.create(errorReporter,entity.registryAccess(),nbt);
            ci.cancel();
        }
    }
}
