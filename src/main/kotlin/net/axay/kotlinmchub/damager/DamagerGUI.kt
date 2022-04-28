package net.axay.kotlinmchub.damager

import net.axay.fabrik.core.text.literal
import net.axay.fabrik.igui.*
import net.axay.fabrik.igui.observable.toGuiList
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

val damageItems = listOf(
    DamagerDifficulty("Easy", Items.LIME_DYE, 4.0F)
)


val damagerGUI = igui(GuiType.NINE_BY_ONE, "Damager Settings".literal, 0) {
    page(0) {
        placeholder(Slots.Border, Items.GRAY_STAINED_GLASS_PANE.guiIcon)

        val compound = compound(
            slots = (1 sl 1) rectTo (1 sl 9),
            content = damageItems.toGuiList(),
            iconGenerator = {
                it.item.defaultInstance
            },
            onClick = { event, element ->
                playerDifficulty[event.player] = element.damage
            }

        )
    }
}

class DamagerDifficulty(
    val name: String,
    val item: Item,
    val damage: Float,
)