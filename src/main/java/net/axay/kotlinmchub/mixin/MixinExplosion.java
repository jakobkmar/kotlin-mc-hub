package net.axay.kotlinmchub.mixin;

import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class MixinExplosion {

    @Inject(
        method = "explode",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onExplode(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(
        method = "finalizeExplosion",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onFinalizeExplode(CallbackInfo ci) {
        ci.cancel();
    }
}
