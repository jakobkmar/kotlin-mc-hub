package net.axay.kotlinmchub.mixin;

import net.axay.kotlinmchub.features.ElytraLauncherKt;
import net.axay.kotlinmchub.features.SoupHealingKt;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Shadow public abstract Item getItem();

    @Inject(
        method = "use",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onUse(Level world,
                       Player user,
                       InteractionHand hand,
                       CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        final var item = getItem();

        if (item == Items.FEATHER) {
            if (user instanceof ServerPlayer serverPlayer) {
                ElytraLauncherKt.featherBoostElytra(serverPlayer);
            }
        } else if (item.isEdible()) {
            if (user instanceof ServerPlayer serverPlayer) {
                final var result = SoupHealingKt.trySoupHealing(serverPlayer, item);
                if (result != null) {
                    cir.setReturnValue(InteractionResultHolder.pass(result));
                }
            }
        }
    }
}
