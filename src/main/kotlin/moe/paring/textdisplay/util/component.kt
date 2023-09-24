package moe.paring.textdisplay.util

import moe.paring.textdisplay.entities.CustomTextDisplay
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor

fun Component.displayInfo(display: CustomTextDisplay) : Component {
    return this.hoverEvent(HoverEvent.showText(Component.text("Click to get info")))
        .clickEvent(ClickEvent.runCommand("/textdisplay info ${display.name}"))
        .color(NamedTextColor.GREEN)
}