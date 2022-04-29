package net.axay.kotlinmchub.features

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.EntityDamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.decoration.ItemFrame
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile

fun Entity.isProtected(damageSource: DamageSource): Boolean {
    if (this is Player) {
        if (damageSource is EntityDamageSource) {
            val attacker = damageSource.entity
            if (attacker is ServerPlayer && attacker.gameMode.isSurvival) {
                return true
            }
            return false
        } else {
            return when (damageSource) {
                DamageSource.CACTUS -> true
                else -> false
            }
        }
    }

    if (damageSource is EntityDamageSource) {
        val attacker = damageSource.entity
        when {
            attacker is ServerPlayer && attacker.gameMode.isSurvival -> return true
            attacker is Projectile && (this is ItemFrame || this is ArmorStand) -> return true
        }
    }

    return false
}
