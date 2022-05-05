package net.axay.kotlinmchub.minigames.tictactoe

import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.text.literalText
import net.axay.fabrik.core.text.sendText
import net.axay.kotlinmchub.minigames.tictactoe.TicTacToe.queue
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.ClickEvent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.GameType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import java.util.*

object TicTacToe {

    val queue = arrayListOf<ServerPlayer>()

    private val players = arrayListOf<ServerPlayer>()

    private val fields = hashMapOf<BlockPos, ServerPlayer>()

    private val beforeGamemodes = mutableMapOf<UUID, GameType>()

    private var turn = 0
    private var gameRunning = false


    fun enable() {

        updateFields()

        AttackBlockCallback.EVENT.register { player: Player, _: Level, _: InteractionHand, blockPos: BlockPos, _: Direction ->
            handleGame(player as ServerPlayer, blockPos)
            return@register InteractionResult.PASS
        }

        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            println("disconnect")
            queue.remove(handler.player)
            if (gameRunning) {
                players.forEach {
                    if (handler.player?.equals(it) == true) {
                        players.remove(it)
                        resetGame()
                        return@forEach
                    }

                }
            }
        }
    }


    fun addToQueue(serverPlayer: ServerPlayer) {
        if (players.contains(serverPlayer)) return
        queue.add(serverPlayer)
        serverPlayer.sendText(literalText {
            color = 0x3BFF30
            text("Du bist der Warteschlange beigetreten")
        })

        if (queue.size < 2 || gameRunning) return
        gameRunning = true

        val secondPlayer = queue[0]
        val firstPlayer = queue[1]

        queue.remove(secondPlayer)
        queue.remove(firstPlayer)
        players.add(firstPlayer)
        players.add(secondPlayer)

        players.forEach {
            beforeGamemodes[it.uuid] = it.gameMode.gameModeForPlayer
            it.setGameMode(GameType.SURVIVAL)
        }

        firstPlayer.sendText(
            literalText {
                color = 0x3BFF30
                text("Du spielst jetzt gegen ")
                color = 0xFF1821
                text(secondPlayer.name)
            }
        )
        secondPlayer.sendText(
            literalText {
                color = 0x3BFF30
                text("Du spielst jetzt gegen ")
                color = 0xFF1821
                text(firstPlayer.name)
            }
        )
        players.forEach {
            it.sendText(literalText {
                color = 0xFF1821
                text("Teleportiere dich zum Spielfeld")
                clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tictactoe teleport")
            })
        }
    }

    private fun handleGame(player: ServerPlayer, blockPos: BlockPos) {
        if (gameRunning) {
            if (!blockPos.isTicTacToeBlock) return
            if (player == players[turn]) {

                if (fields.containsKey(blockPos)) {
                    player.sendText(
                        literalText {
                            color = 0xB736D5
                            text("This field is already claimed")
                        }
                    )
                    return
                }
                turn = if (turn == 1) 0 else 1
                fields[blockPos] = player
                updateFields()

            }
            checkWinner()
        }
    }


    private fun updateFields(reset: Boolean = false) {
        if (reset || !gameRunning) {
            ticTacToeBlocks.forEach {
                Fabrik.currentServer!!.overworld().setBlockAndUpdate(
                    it,
                    Blocks.WHITE_CONCRETE.defaultBlockState()
                )
            }
            return
        }


        fields.forEach { (blockPos, serverPlayer) ->
            val color =
                if (players.first() == serverPlayer) Blocks.RED_WOOL.defaultBlockState() else Blocks.GREEN_WOOL.defaultBlockState()
            Fabrik.currentServer!!.overworld().setBlockAndUpdate(blockPos, color)

        }

    }

    val ticTacToeBlocks: List<BlockPos>
        get() {
            val blocks = arrayListOf<BlockPos>()

            val cornerPos = BlockPos(-5, -31, 9)
            for (x in 0..2) {
                for (z in 0..2) {
                    blocks.add(BlockPos(cornerPos.x - x, cornerPos.y, cornerPos.z + z))
                }
            }

            return blocks
        }


    private fun checkWinner() {
        repeat(9) {
            val line = when (it) {
                0 -> fields[getBlockPosByFieldNumber(1)]?.character +
                    fields[getBlockPosByFieldNumber(2)]?.character +
                    fields[getBlockPosByFieldNumber(3)]?.character
                1 -> fields[getBlockPosByFieldNumber(6)]?.character +
                    fields[getBlockPosByFieldNumber(5)]?.character +
                    fields[getBlockPosByFieldNumber(4)]?.character
                2 -> fields[getBlockPosByFieldNumber(9)]?.character +
                    fields[getBlockPosByFieldNumber(8)]?.character +
                    fields[getBlockPosByFieldNumber(7)]?.character

                3 -> fields[getBlockPosByFieldNumber(1)]?.character +
                        fields[getBlockPosByFieldNumber(4)]?.character +
                        fields[getBlockPosByFieldNumber(7)]?.character
                4 -> fields[getBlockPosByFieldNumber(3)]?.character +
                        fields[getBlockPosByFieldNumber(6)]?.character +
                        fields[getBlockPosByFieldNumber(9)]?.character

                5 -> fields[getBlockPosByFieldNumber(3)]?.character +
                        fields[getBlockPosByFieldNumber(5)]?.character +
                        fields[getBlockPosByFieldNumber(7)]?.character
                6 -> fields[getBlockPosByFieldNumber(1)]?.character +
                        fields[getBlockPosByFieldNumber(5)]?.character +
                        fields[getBlockPosByFieldNumber(9)]?.character

                7 -> fields[getBlockPosByFieldNumber(2)]?.character +
                        fields[getBlockPosByFieldNumber(5)]?.character +
                        fields[getBlockPosByFieldNumber(8)]?.character
                else -> "/"
            }
            var winner: ServerPlayer? = null
            if (line == "XXX") {
                winner = players.first()
            } else if (line == "OOO") winner = players[1]
            if (winner != null) {
                players.forEach { player ->
                    player.sendText(literalText {
                        color = 0xFF1821
                        text(winner.name)
                        color = 0x3BFF30
                        text(" hat das Spiel gewonnen")
                    })
                }
                resetGame()
            } else if (fields.size == 9) {
                players.forEach { player ->
                    player.sendText(literalText {
                        color = 0x3BFF30
                        text("unentschieden, keiner gewinnt")
                    })
                }
                resetGame()
                return
            }
        }

    }

    private fun resetGame() {
        players.forEach {
            it.setGameMode(beforeGamemodes.remove(it.uuid) ?: GameType.SURVIVAL)
        }
        players.clear()
        fields.clear()
        gameRunning = false
        updateFields()
    }

    private fun getBlockPosByFieldNumber(number: Int): BlockPos {
        var fieldNumber = 0
        ticTacToeBlocks.forEach {
            fieldNumber += 1
            if (fieldNumber == number) return it
        }
        return BlockPos(0, 0, 0)
    }

    private val Player.character: String
        get() {
            if (!players.contains(this)) return "NONE"
            return if (players.first() == this) "X" else "O"
        }

}

val Player.isInTicTacToeQueue: Boolean
    get() {
        return queue.contains(this)
    }

val BlockPos.isTicTacToeBlock: Boolean
    get() {
        TicTacToe.ticTacToeBlocks.forEach {
            if (it == this) return true
        }
        return false
    }
