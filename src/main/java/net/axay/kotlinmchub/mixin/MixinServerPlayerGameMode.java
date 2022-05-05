package net.axay.kotlinmchub.mixin;

import net.axay.kotlinmchub.minigames.tictactoe.TicTacToe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockBreakAckPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerGameMode.class)
public class MixinServerPlayerGameMode {

    @Shadow @Final protected ServerPlayer player;

    @Shadow protected ServerLevel level;

    @Inject(
        method = "handleBlockBreakAction",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onHandleBlockBreakAction(BlockPos pos,
                                          ServerboundPlayerActionPacket.Action action,
                                          Direction direction,
                                          int worldHeight,
                                          CallbackInfo ci) {
        if (player.gameMode.isSurvival() || TicTacToe.INSTANCE.getTicTacToeBlocks().contains(pos)) {
            player.connection.send(new ClientboundBlockBreakAckPacket(pos, level.getBlockState(pos), action, false));
            ci.cancel();
        }
    }
}