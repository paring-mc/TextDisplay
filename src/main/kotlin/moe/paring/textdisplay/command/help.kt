package moe.paring.textdisplay.command

import io.github.monun.kommand.node.RootNode
import io.github.monun.tap.protocol.PacketSupport
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor

fun RootNode.help() {
    then("help") {
        executes {
            fun TextComponent.Builder.appendHelp(
                name: String, detail: String, command: String = name
            ): TextComponent.Builder {
                return append(
                    Component.text("\n$name").color(NamedTextColor.GOLD).clickEvent(ClickEvent.suggestCommand(command))
                        .hoverEvent(
                            HoverEvent.showText(Component.text("Click to complete"))
                        )
                ).append(Component.text("\n    $detail"))
            }

            val command = "/textdisplay"

            sender.sendMessage(
                Component.text().append(Component.text("----- TextDisplay Usage -----").color(NamedTextColor.YELLOW))
                    .appendHelp("$command help", "Shows this message").appendHelp("$command list", "List displays")
                    .appendHelp(
                        "$command create <name> <x> <y> <z> <yaw> <pitch> <text>", "Create a display", "$command create "
                    ).appendHelp("$command delete <name>", "Delete a display", "$command delete ").appendHelp(
                        "$command set <name> <property> <value>", "Set a property for display", "$command set "
                    ).appendHelp("$command reload", "Reload all text displays").build()
            )
        }
    }
}