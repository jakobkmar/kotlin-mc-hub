package net.axay.kotlinmchub

import net.axay.kotlinmchub.commands.restartCommand
import net.axay.kotlinmchub.damager.Damager
import net.axay.kotlinmchub.damager.damageCommand
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

class Manager : ModInitializer {

    override fun onInitialize() {
        registerCommands()
        ServerLifecycleEvents.SERVER_STARTED.register {
            Damager.enable()
        }
    }

    private fun registerCommands() {
        damageCommand
        restartCommand
    }
}
