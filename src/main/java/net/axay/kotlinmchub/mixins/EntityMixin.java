package net.axay.kotlinmchub.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
    private void makeEverythingGlow(CallbackInfoReturnable<Boolean> cir) {
        if((Object) this instanceof Player) {
            cir.setReturnValue(true);
        }
    }

}
