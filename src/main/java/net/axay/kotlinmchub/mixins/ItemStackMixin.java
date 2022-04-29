package net.axay.kotlinmchub.mixins;

import net.axay.kotlinmchub.functionality.SoupHealingKt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onUse(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemStack = (ItemStack) (Object) this;
        if (itemStack.getItem().isEdible()) {
            SoupHealingKt.INSTANCE.onPotentialSoupUse(user, itemStack.getItem(), cir, world, hand);
        }
    }
}
