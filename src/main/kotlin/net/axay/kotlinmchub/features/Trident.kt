package net.axay.kotlinmchub.features

import net.axay.fabrik.core.entity.modifyVelocity
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

fun onTridentRelease(player: Player, x: Double, y: Double, z: Double) {
    if(player is ServerPlayer) {
        player.modifyVelocity(x, y, z, false)
        player.connection.send(ClientboundSetEntityMotionPacket(player))
    }
}