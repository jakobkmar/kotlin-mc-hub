package net.axay.kotlinmchub.features

import net.axay.fabrik.core.entity.changePos
import net.axay.fabrik.core.entity.modifyVelocity
import net.minecraft.server.level.ServerPlayer

fun ServerPlayer.teleportBackUp() {
    changePos(y = level.maxBuildHeight + 1)
    modifyVelocity(y = -20)
    // TODO remove this once fabrikmc-core has the fix
    hurtMarked = true
}
