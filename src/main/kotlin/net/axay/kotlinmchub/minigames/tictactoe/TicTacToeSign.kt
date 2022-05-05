package net.axay.kotlinmchub.minigames.tictactoe

import net.axay.fabrik.core.Fabrik
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.SignBlock
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun onClickSign(
    sign: SignBlock,
    state: BlockState,
    world: Level,
    pos: BlockPos,
    player: Player,
    hand: InteractionHand,
    hit: BlockHitResult,
    cir: CallbackInfoReturnable<InteractionResult>
) {
    val signEntity: SignBlockEntity = world.getBlockEntity(pos) as? SignBlockEntity ?: return
    repeat(4) {
        if ("tictactoe" in signEntity.getMessage(it, false).string.lowercase()) {
            if (player.isInTicTacToeQueue) {
                Fabrik.currentServer!!.commands.performCommand(
                    player.createCommandSourceStack(),
                    "/tictactoe queue leave"
                )
            } else Fabrik.currentServer!!.commands.performCommand(
                player.createCommandSourceStack(),
                "/tictactoe queue join"
            )
            return@repeat
        }
    }

}
