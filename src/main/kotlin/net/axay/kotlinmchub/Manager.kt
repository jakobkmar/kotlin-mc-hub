package net.axay.kotlinmchub

import net.axay.kotlinmchub.commands.restartCommand
import net.axay.kotlinmchub.damager.Damager
import net.axay.kotlinmchub.damager.damageCommand
import net.axay.kotlinmchub.minigames.tictactoe.TicTacToe
import net.axay.kotlinmchub.minigames.tictactoe.ticTacToeCommand
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

class Manager : ModInitializer {

    override fun onInitialize() {
        registerCommands()
        ServerLifecycleEvents.SERVER_STARTED.register {
            Damager.enable()
            TicTacToe.enable()
        }
    }

    private fun registerCommands() {
        ticTacToeCommand
        damageCommand
        restartCommand
    }
}
