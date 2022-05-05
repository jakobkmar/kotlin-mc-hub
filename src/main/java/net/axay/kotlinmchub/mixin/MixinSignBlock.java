package net.axay.kotlinmchub.mixin;

import net.axay.kotlinmchub.damager.DamagerSignKt;
import net.axay.kotlinmchub.minigames.tictactoe.TicTacToeSignKt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignBlock.class)
public class MixinSignBlock {
    @Inject(
            method = "use",
            at = @At("HEAD")
    )
    public void onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        SignBlock sign = (SignBlock) (Object) this;
        DamagerSignKt.onClickSign(sign, state, world, pos, player, hand, hit, cir);
        TicTacToeSignKt.onClickSign(sign, state, world, pos, player, hand, hit, cir);
    }
}
