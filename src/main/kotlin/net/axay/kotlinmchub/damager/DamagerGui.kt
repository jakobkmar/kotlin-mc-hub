package net.axay.kotlinmchub.damager

import net.axay.fabrik.core.item.itemStack
import net.axay.fabrik.core.item.setCustomName
import net.axay.fabrik.core.text.literal
import net.axay.fabrik.igui.*
import net.axay.fabrik.igui.observable.toGuiList
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

private val damageItems = listOf(
    DamagerDifficulty("Noob", Items.BLUE_DYE, 3.0F),
    DamagerDifficulty("Easy", Items.LIME_DYE, 4.0F),
    DamagerDifficulty("Medium", Items.YELLOW_DYE, 5.0F),
    DamagerDifficulty("Hard", Items.RED_DYE, 7.0F),
    DamagerDifficulty("Legendary", Items.PURPLE_DYE, 9.0F),
)

val damagerGui = igui(GuiType.NINE_BY_ONE, "Damager Settings".literal, 0) {
    page(0) {
        placeholder(Slots.All, Items.GRAY_STAINED_GLASS_PANE.guiIcon)

        compound(
            slots = (1 sl 3) rectTo (1 sl 7),
            content = damageItems.toGuiList(),
            iconGenerator = {
                itemStack(it.item) { setCustomName(it.name) }
            },
            onClick = { event, element ->
                Damager.playerDifficulty[event.player.uuid] = element.damage
                (event.player as? ServerPlayer)?.closeContainer()
            }
        )
    }
}

private class DamagerDifficulty(
    val name: String,
    val item: Item,
    val damage: Float,
)
