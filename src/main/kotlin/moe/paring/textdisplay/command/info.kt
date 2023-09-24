package moe.paring.textdisplay.command

import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.RootNode
import moe.paring.textdisplay.entities.CustomTextDisplay
import moe.paring.textdisplay.util.display
import moe.paring.textdisplay.util.displayAttributes
import moe.paring.textdisplay.util.displayInfo
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import java.text.DecimalFormat

private val dec = DecimalFormat("#.##")

fun RootNode.info() {
    then("info") {
        requires { hasPermission("textdisplay.command.info") }

        then("display" to display) {
            executes { context ->
                val display: CustomTextDisplay by context

                fun TextComponent.Builder.appendInfo(
                    name: String,
                    value: Component,
                    copyValue: String? = null,
                    hoverEvent: HoverEvent<*>? = if (copyValue != null) HoverEvent.showText(Component.text("Copy to clipboard")) else null,
                    clickAction: ClickEvent? = copyValue?.let { ClickEvent.copyToClipboard(it) },
                ): TextComponent.Builder {
                    return appendNewline().append(
                        Component.text(name).color(NamedTextColor.YELLOW).hoverEvent(
                            HoverEvent.showText(
                                Component.text("Click to edit")
                            )
                        ).clickEvent(ClickEvent.suggestCommand("/textdisplay set ${display.name} $name "))
                    ).append(Component.text(": "))
                        .append(value.color(NamedTextColor.LIGHT_PURPLE).hoverEvent(hoverEvent).clickEvent(clickAction))
                }

                val c = display.config
                val loc = c.location

                sender.sendMessage(
                    Component.text()
                        .appendNewline()
                        .append(Component.text("Display ").append(Component.text(display.name).displayInfo(display)))
                        .appendInfo("text", Component.text("[Click to copy]"), copyValue = c.text)
                        .appendInfo(
                            "position",
                            Component
                                .text()
                                .append(
                                    Component.text("${dec.format(loc.x)} ${dec.format(loc.y)} ${dec.format(loc.z)}")
                                        .color(NamedTextColor.GOLD)
                                )
                                .append(Component.text(" on "))
                                .append(Component.text(location.world.name).color(NamedTextColor.AQUA))
                                .build(),
                            hoverEvent = HoverEvent.showText(Component.text("Click to teleport")),
                            clickAction = ClickEvent.callback {
                                player.teleport(loc)
                                player.sendMessage(
                                    Component.text("Teleported to the location of display ")
                                        .append(Component.text(display.name).displayInfo(display))
                                        .append(Component.text("."))
                                )
                            }
                        )
                        .appendInfo("rotation", Component.text("yaw: ${loc.yaw} pitch: ${loc.pitch}"), copyValue = "${loc.yaw} ${loc.pitch}")
                        .appendInfo("updateInterval", Component.text("${c.updateInterval} ticks"), copyValue = "${c.updateInterval}")
                        .run {
                            var result = this

                            displayAttributes.forEach { attr ->
                                val formatted = attr.format(c.attributes[attr.name])
                                result = result.appendInfo(attr.name, formatted, copyValue = formatted.content())
                            }

                            result
                        }
                        .appendNewline().appendNewline()
                        .append(Component.text("[ Transformation ]").color(NamedTextColor.GOLD)
                            .hoverEvent(HoverEvent.showText(Component.text("The transformation of display entities"))))
                        .appendInfo("scale", Component.text(c.scale.run {
                            "$x $y $z"
                        }), copyValue = c.scale.run { "$x $y $z" })
                        .appendInfo("translation", Component.text(c.translation.run {
                            "$x $y $z"
                        }), copyValue = c.translation.run { "$x $y $z" })
                        .appendInfo("leftRotation", Component.text(c.leftRotation.run {
                            "$x $y $z $angle"
                        }), copyValue = c.leftRotation.run { "$x $y $z $angle" })
                        .appendInfo("rightRotation", Component.text(c.rightRotation.run {
                            "$x $y $z $angle"
                        }), copyValue = c.rightRotation.run { "$x $y $z $angle" })
                )
            }
        }
    }
}
