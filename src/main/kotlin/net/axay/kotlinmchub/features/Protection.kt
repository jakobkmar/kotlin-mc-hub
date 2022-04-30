package net.axay.kotlinmchub.features

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.EntityDamageSource
import net.minecraft.world.damagesource.IndirectEntityDamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.decoration.ItemFrame
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile

fun Entity.isProtected(damageSource: DamageSource): Boolean {
    if (this is Player) {
        return if (damageSource is EntityDamageSource) {
            damageSource.entity.isSurvivalPlayer()
        } else {
            when (damageSource) {
                DamageSource.CACTUS -> true
                else -> false
            }
        }
    }

    if (damageSource is EntityDamageSource) {
        if (damageSource.entity?.isSurvivalPlayer() == true)
            return true

        if (damageSource is IndirectEntityDamageSource) {
            val attacker = damageSource.directEntity
            if (attacker is Projectile && (this is ItemFrame || this is ArmorStand))
                return true
        }
    }

    return false
}

private fun Entity?.isSurvivalPlayer() = this is ServerPlayer && gameMode.isSurvival
