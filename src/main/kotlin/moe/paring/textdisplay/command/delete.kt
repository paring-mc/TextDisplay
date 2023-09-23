package moe.paring.textdisplay.command

import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.RootNode
import moe.paring.textdisplay.entities.CustomTextDisplay
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import moe.paring.textdisplay.util.display
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor


fun RootNode.delete(plugin: TextDisplayPlugin) {
    then("delete") {
        requires { sender.hasPermission("textdisplay.command.delete") }
        then("display" to display) {
            executes { context ->
                val display: CustomTextDisplay by context
                val name = display.name
                plugin.textManager.delete(display)
                plugin.reload()

                broadcast(
                    Component.text("Deleted text display ")
                        .append(Component.text(name).color(NamedTextColor.GREEN))
                        .append(Component.text("."))
                ) { isOp }
            }
        }
    }
}
