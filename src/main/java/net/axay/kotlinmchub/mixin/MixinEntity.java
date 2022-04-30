package net.axay.kotlinmchub.mixin;

import net.axay.kotlinmchub.features.LauncherKt;
import net.axay.kotlinmchub.features.ProtectionKt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
           method = "move",
           at = @At("HEAD")
    )
    public void onMove(MoverType movementType, Vec3 movement, CallbackInfo ci) {
        LauncherKt.handleLauncher((Entity) (Object) this);
    }
}
