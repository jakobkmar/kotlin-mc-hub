package net.axay.kotlinmchub.mixin;

import net.axay.kotlinmchub.features.OutOfWorldKt;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Inject(
        method = "outOfWorld",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onOutOfWorld(CallbackInfo ci) {
        if ((LivingEntity) (Object) this instanceof ServerPlayer player) {
            OutOfWorldKt.teleportBackUp(player);
            ci.cancel();
        }
    }
}
