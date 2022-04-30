package net.axay.kotlinmchub.commands

import net.axay.fabrik.commands.PermissionLevel
import net.axay.fabrik.commands.command
import net.axay.fabrik.core.text.literalText

val restartCommand = command("restart") {
    requiresPermissionLevel(PermissionLevel.OWNER)
    runs {
        val restartText = literalText {
            text("Der Server startet neu.") {
                color = 0x5CFF8F
            }
            emptyLine()
            text("Er ist gleich wieder online ") {
                color = 0xB7FF54
            }
            text("☻") {
                color = 0xFFD817
            }
        }

        with(source.server) {
            playerList.players.forEach {
                it.connection.disconnect(restartText)
            }

            halt(false)
        }
    }
}
