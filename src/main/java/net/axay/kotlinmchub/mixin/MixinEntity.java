package net.axay.kotlinmchub.mixin;

import net.axay.kotlinmchub.features.ProtectionKt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(
        method = "isInvulnerableTo",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onHurt(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        final var entity = (Entity) (Object) this;
        if (ProtectionKt.isProtected(entity, damageSource)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "isInWaterOrRain",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isInRain(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }


}
