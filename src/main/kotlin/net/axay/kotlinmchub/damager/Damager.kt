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
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameType
import net.minecraft.world.phys.Vec3
import java.util.*

object Damager {
    val playerDifficulty = mutableMapOf<UUID, Float>()

    private val damagerPos = Pos3i(-8, -31, -21) to Pos3i(-2, -28, -13)
    private val damagerSpawn = Vec3(-4.5, -30.0, -9.5)
    private val beforeGamemodes = mutableMapOf<UUID, GameType>()

    fun enable() {
        coroutineTask(period = (1000L / 20L) * 12L, howOften = Long.MAX_VALUE) {
            checkPlayersInDamager()
            playersInDamager.forEach { player ->
                val damage = playerDifficulty.getOrDefault(player.uuid, 5.0F)
                if (player.health - damage <= 0) {
                    player.teleportTo(Fabrik.currentServer!!.overworld(), damagerSpawn.x, damagerSpawn.y, damagerSpawn.z, 180F, 0F)
                    player.health = 20f
                } else {
                    player.hurt(DamageSource.GENERIC, damage)
                    when(playerModes[player.uuid] ?: DamagerMode.DEFAULT) {
                        DamagerMode.CRAP -> {
                            if(Random().nextInt(4) == 3) {
                                player.inventory.add(ItemStack(Items.DIRT, 1))
                            }
                        }
                        DamagerMode.WITHER -> {
                            player.addEffect(MobEffectInstance(MobEffects.WITHER, 40, 1))
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    val playersInDamager: MutableSet<ServerPlayer> = mutableSetOf()

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
        sendText("You entered the damager.")
    }

    private fun ServerPlayer.onLeaveDamager() {
        inventory.clearContent()
        playersInDamager.remove(this)
        foodData.foodLevel = 20
        health = 20f
        setGameMode(beforeGamemodes.remove(uuid) ?: GameType.SURVIVAL)
        sendText("You left the damager.")
    }
}

// make this class serializable in fabrikmc-core
@kotlinx.serialization.Serializable
private data class Pos3i(val x: Int, val y: Int, val z: Int) {
    fun toTriple() = Triple(x, y, z)
    fun toVec3i() = Vec3i(x, y, z)
    fun toBlockPos() = BlockPos(x, y, z)
}
