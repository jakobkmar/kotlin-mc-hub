package net.axay.kotlinmchub.damager

import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.entity.pos
import net.axay.fabrik.core.item.itemStack
import net.axay.fabrik.core.task.coroutineTask
import net.axay.fabrik.core.text.sendText
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameType
import java.util.*

val playerDifficulty = mutableMapOf<Player, Float>()

object Damager {
    val damagerPos = Pos3i(-8, -30, -22) to Pos3i(-2, -28, -13)
    val beforeGamemodes = mutableMapOf<UUID, GameType>()

    fun enable() {
        coroutineTask(period = 12L, howOften = Long.MAX_VALUE) {
            checkPlayersInDamager()
            playersInDamager.forEach {
                it.hurt(DamageSource.GENERIC, playerDifficulty.getOrDefault(it, 5.0F))
            }
        }
    }

    private val playersInDamager: MutableSet<ServerPlayer> = mutableSetOf()

    private fun checkPlayersInDamager() {
        val playersInArea = Fabrik.currentServer?.playerList?.players?.filter {
            it.pos.x > damagerPos.first.x && it.pos.x < damagerPos.second.x &&
                    it.pos.y > damagerPos.first.y && it.pos.y < damagerPos.second.y &&
                    it.pos.z > damagerPos.first.z && it.pos.z < damagerPos.second.z
        } ?: emptyList()
        playersInArea.forEach {
            if (it !in playersInDamager) it.onEnterDamager()
        }
        playersInDamager.addAll(playersInArea)
        playersInDamager.toMutableList().forEach {
            if (it !in playersInArea) {
                it.onLeaveDamager()
            }
        }
    }

    private fun ServerPlayer.onEnterDamager() {
        inventory.clearContent()
        repeat(36) {
            inventory.add(Items.MUSHROOM_STEW.defaultInstance)
        }
        inventory.setItem(13, itemStack(Items.BOWL, 64){})
        inventory.setItem(14, itemStack(Items.RED_MUSHROOM, 64){})
        inventory.setItem(15, itemStack(Items.BROWN_MUSHROOM, 64){})
        foodData.foodLevel = 20
        health = 20f
        beforeGamemodes[uuid] = gameMode.gameModeForPlayer
        setGameMode(GameType.ADVENTURE)
        sendText("Du hast den Damager betreten")
    }
    private fun ServerPlayer.onLeaveDamager() {
        inventory.clearContent()
        playersInDamager.remove(this)
        foodData.foodLevel = 20
        health = 20f
        setGameMode(beforeGamemodes[uuid] ?: GameType.SURVIVAL)
        sendText("Du hast den Damager verlassen")
    }
}

@kotlinx.serialization.Serializable
data class Pos3i(val x: Int, val y: Int, val z: Int) {
    fun toTriple() = Triple(x, y, z)
    fun toVec3i() = Vec3i(x, y, z)
    fun toBlockPos() = BlockPos(x, y, z)
}