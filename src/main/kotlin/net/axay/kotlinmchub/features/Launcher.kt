package net.axay.kotlinmchub.features

import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.entity.modifyVelocity
import net.axay.fabrik.core.entity.posUnder
import net.axay.fabrik.core.packet.sendPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Blocks

fun handleLauncher(entity: Entity) {
    val pos = entity.posUnder
    var boost = 1.5
    if (entity.level.getBlockState(pos).block == Blocks.SPONGE) {
        var i = 1
        while (entity.level.getBlockState(pos.below(i)).block == Blocks.SPONGE) {
            boost *=1.5
            i++
        }
        entity.modifyVelocity(0, boost, 0, false)
        if (entity is ServerPlayer) {
            Fabrik.currentServer?.playerList?.players?.sendPacket(ClientboundSetEntityMotionPacket(entity))
        }
    }
}