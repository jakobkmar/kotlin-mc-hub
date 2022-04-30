package net.axay.kotlinmchub.mixin;

import net.axay.kotlinmchub.damager.Damager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {

    @Inject(
        method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onDrop(CallbackInfoReturnable<ItemEntity> cir) {
        if (Damager.INSTANCE.getPlayersInDamager().contains((ServerPlayer) (Object) this)) {
            cir.setReturnValue(null);
        }
    }
}
