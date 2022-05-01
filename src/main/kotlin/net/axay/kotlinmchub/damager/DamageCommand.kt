package net.axay.kotlinmchub.damager

import net.axay.fabrik.commands.command
import net.axay.fabrik.igui.openGui

val damageCommand = command("damage") {
    runs {
        source.playerOrException.openGui(damagerGui, 0)
    }
    literal("mode") {
        runs {
            source.playerOrException.openGui(damagerModeGui, 0)
        }
    }
    argument<Float>("damage") {
        runs {
            Damager.playerDifficulty[source.playerOrException.uuid] = it()
        }
    }
}
