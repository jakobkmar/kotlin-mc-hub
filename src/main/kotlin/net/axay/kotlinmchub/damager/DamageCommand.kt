package net.axay.kotlinmchub.damager

import net.axay.fabrik.commands.command
import net.axay.fabrik.igui.openGui

val damageCommand = command("damage") {
    runs {
        source.playerOrException.openGui(damagerGUI, 0)
    }
    argument<Float>("damage") {
        runs {
            playerDifficulty[source.playerOrException] = it()
        }
    }
}