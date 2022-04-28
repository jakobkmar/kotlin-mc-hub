package net.axay.kotlinmchub.damager

import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.entity.pos
import net.axay.fabrik.core.task.coroutineTask
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.GameType

val playerDifficulty = mutableMapOf<Player, Float>()

object Damager {
    val damagerPos = Pos3i(0, 100, 0) to Pos3i(5, 105, 5)

    fun enable() {
        damageCommand
        coroutineTask(period = 12L) {
            checkPlayersInDamager()
            playersInDamager.forEach {
                it.hurt(DamageSource.GENERIC, playerDifficulty.getOrDefault(it, 5.0F))
            }
        }
    }

    private val playersInDamager: MutableList<ServerPlayer> = mutableListOf()

    private fun checkPlayersInDamager() {
        val players = Fabrik.currentServer?.playerList?.players?.filter {
            it.pos.x > damagerPos.second.x && it.pos.x < damagerPos.first.x &&
                    it.pos.y > damagerPos.second.y && it.pos.y < damagerPos.first.y &&
                    it.pos.z > damagerPos.second.z && it.pos.z < damagerPos.first.z
        } ?: emptyList()
        playersInDamager.addAll(players)
        players.forEach {
            if (it !in playersInDamager) it.onEnterDamager()
        }
        playersInDamager.forEach {
            if (it !in players) it.onLeaveDamager()
        }
    }

    private fun ServerPlayer.onEnterDamager() {
        foodData.foodLevel = 20
        health = 20f
        setGameMode(GameType.ADVENTURE)
    }
    private fun ServerPlayer.onLeaveDamager() {
        foodData.foodLevel = 20
        health = 20f
        setGameMode(GameType.SURVIVAL)
    }
}

@kotlinx.serialization.Serializable
data class Pos3i(val x: Int, val y: Int, val z: Int) {
    fun toTriple() = Triple(x, y, z)
    fun toVec3i() = Vec3i(x, y, z)
    fun toBlockPos() = BlockPos(x, y, z)
}