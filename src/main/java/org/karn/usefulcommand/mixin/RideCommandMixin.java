package org.karn.usefulcommand.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.commands.RideCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RideCommand.class)
public class RideCommandMixin {
    @Redirect(method = "mount", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;is(Ljava/lang/Object;)Z"))
    private static boolean allowPlayerVehicle$karnscmd(Entity vehicle, Object type) {
        return type != net.minecraft.world.entity.EntityTypes.PLAYER && vehicle.is((EntityType<?>) type);
    }
}
