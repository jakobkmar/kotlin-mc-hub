package net.axay.kotlinmchub.features

import com.mojang.math.Vector3d
import net.axay.fabrik.core.entity.modifyVelocity
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

val boost = 2.0
fun onTridentRelease(player: Player, x: Double, y: Double, z: Double) {
    if(player is ServerPlayer) {
        val vec = Vector3d(x,y,z)
        vec.scale(boost)
        player.modifyVelocity(vec.x, vec.y, vec.z, false)
        player.connection.send(ClientboundSetEntityMotionPacket(player))
    }
}