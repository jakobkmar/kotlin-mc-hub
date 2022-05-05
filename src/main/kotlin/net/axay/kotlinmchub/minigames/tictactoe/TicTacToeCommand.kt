package net.axay.kotlinmchub.minigames.tictactoe

import net.axay.fabrik.commands.command
import net.axay.fabrik.core.text.literalText
import net.axay.fabrik.core.text.sendText
import net.minecraft.server.level.ServerPlayer

val ticTacToeCommand = command("ttc") {
    literal("queue") {
        literal("join") {
            runs {
                val player = source.playerOrException
                TicTacToe.addToQueue(player)
            }
        }
        literal("leave") {
            runs {
                val player = source.playerOrException
                TicTacToe.queue.remove(player as ServerPlayer)
                player.sendText(literalText {
                    color = 0xFF1821
                    text("Du hast die Warteschlange Verlassen")
                })
            }

        }
    }
    literal("teleport") {
        runs {
            source.playerOrException.teleportTo(-6.0, -30.0, 8.0)
        }
    }
}


