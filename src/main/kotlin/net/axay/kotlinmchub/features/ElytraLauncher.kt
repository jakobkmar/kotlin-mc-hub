package net.axay.kotlinmchub.features

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.fabrik.core.entity.markVelocityDirty
import net.axay.fabrik.core.entity.modifyVelocity
import net.axay.fabrik.core.item.itemStack
import net.axay.fabrik.core.item.setCustomName
import net.axay.fabrik.core.task.mcCoroutineScope
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

fun Entity.launchElytra(pos: BlockPos, state: BlockState) {
    if (state.block != Blocks.CRIMSON_PRESSURE_PLATE) return
    if (level.getBlockState(pos.below()).block != Blocks.SHROOMLIGHT) return

    modifyVelocity(y = 3.0)

    if (this is ServerPlayer) {
        connection.send(ClientboundSetEntityMotionPacket(this))
        playNotifySound(SoundEvents.BEACON_ACTIVATE, SoundSource.MASTER, 3f, 1f)

        if (inventory.items.none { it.item == Items.FEATHER }) {
            inventory.add(
                itemStack(Items.FEATHER) {
                    setCustomName("Elytra Boost")
                }
            )
        }

        mcCoroutineScope.launch {
            delay(1500)
            setItemSlot(EquipmentSlot.CHEST, Items.ELYTRA.defaultInstance)
            startFallFlying()
        }
    }
}

fun ServerPlayer.featherBoostElytra() {
    if (isFallFlying) {
        deltaMovement = directionVector.scale(1.5)
        hurtMarked = true
    }
}

// until this is in fabrikmc-core
private val Entity.directionVector: Vec3
    get() {
        val rotY = Math.toRadians(yRot.toDouble())
        val rotX = Math.toRadians(xRot.toDouble())
        val xz = cos(rotX)
        return Vec3(-xz * sin(rotY), -sin(rotX), xz * cos(rotY))
    }
