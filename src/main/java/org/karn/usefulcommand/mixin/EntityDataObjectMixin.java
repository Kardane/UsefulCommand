package org.karn.usefulcommand.mixin;

import net.minecraft.command.EntityDataObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.ReadView;
import net.minecraft.util.ErrorReporter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;

import java.util.UUID;

@Mixin(EntityDataObject.class)
public class EntityDataObjectMixin {
    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    @Final
    private Entity entity;

    @Inject(method="setNbt", at=@At("HEAD"), cancellable = true)
    void setNbt$karnscmd(NbtCompound nbt, CallbackInfo ci){
        if (this.entity instanceof PlayerEntity){
            ErrorReporter errorReporter = new ErrorReporter.Logging(this.entity.getErrorReporterContext(),LOGGER);
            NbtReadView.create(errorReporter,entity.getRegistryManager(),nbt);
            ci.cancel();
        }
    }
}
