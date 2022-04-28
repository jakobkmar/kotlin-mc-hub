package net.axay.kotlinmchub

import net.axay.kotlinmchub.damager.Damager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer

class Manager : ModInitializer {
    override fun onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register {
            Damager.enable()
        }
    }


}
