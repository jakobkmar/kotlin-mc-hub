package net.axay.kotlinmchub.mixin;

import net.axay.kotlinmchub.features.TridentKt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public abstract class MixinTridentItem {

    @Inject(
        method = "use",
        at = @At("HEAD"),
        cancellable = true
    )
    public void use(Level world,
                    Player user,
                    InteractionHand hand,
                    CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        user.startUsingItem(hand);
        cir.setReturnValue(InteractionResultHolder.consume(user.getItemInHand(hand)));
    }

    @Redirect(
        method = "releaseUsing",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;push(DDD)V"
        )
    )
    public void onRelease(Player instance, double h, double k, double l) {
        TridentKt.onTridentRelease(instance, h, k, l);
    }
}
