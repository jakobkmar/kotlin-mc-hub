package net.axay.kotlinmchub.features

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

fun ServerPlayer.trySoupHealing(
    item: Item,
): ItemStack? {
    val hungerManager = foodData

    if (!item.isStew || (health >= maxHealth && !hungerManager.needsFood())) return null

    var consumedSoup = false

    if (health < maxHealth) {
        heal(7F)
        consumedSoup = true
    } else if (hungerManager.needsFood()) {
        hungerManager.foodLevel += item.restoredFood
        hungerManager.setSaturation(hungerManager.saturationLevel + item.restoredSaturation)
        consumedSoup = true
    }

    return if (consumedSoup) Items.BOWL.defaultInstance else null
}

private val Item.isStew: Boolean
    get() = when (this) {
        Items.MUSHROOM_STEW -> true
        Items.BEETROOT_SOUP -> true
        Items.RABBIT_STEW -> true
        else -> false
    }

private val Item.restoredFood: Int
    get() = this.foodProperties?.nutrition ?: 0

private val Item.restoredSaturation: Float
    get() = this.foodProperties?.saturationModifier ?: 0f
