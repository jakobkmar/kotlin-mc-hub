package net.axay.kotlinmchub.damager

import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import java.util.*


val playerModes = mutableMapOf<UUID, DamagerMode>()

enum class DamagerMode(val displayName: String, val item: Item) {
    DEFAULT(
        "Default",
        Items.IRON_SWORD,
    ),
    CRAP(
        "Crap",
        Items.DIRT,
    ),
    WITHER(
        "Wither",
        Items.WITHER_SKELETON_SKULL,
    );
}