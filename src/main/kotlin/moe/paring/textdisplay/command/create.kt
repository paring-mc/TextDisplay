package moe.paring.textdisplay.command

import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.RootNode
import io.github.monun.kommand.wrapper.Position3D
import moe.paring.textdisplay.persistence.DisplayConfig
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import moe.paring.textdisplay.util.executesCatching
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

fun RootNode.create(plugin: TextDisplayPlugin) {
    then("create") {
        requires { sender.hasPermission("textdisplay.command.create") && sender is Player }

        then("name" to string()) {
            then("position" to position()) {
                then("text" to string(StringType.GREEDY_PHRASE)) {
                    executesCatching { context ->
                        val name: String by context
                        val position: Position3D by context
                        val text: String by context

                        plugin.textManager.add(name, DisplayConfig().apply {
                            this.text = text
                            this.worldName = player.world.name
                            this.x = position.x
                            this.y = position.y
                            this.z = position.z
                            this.yaw = 0f
                            this.pitch = 0f
                            this.updateInterval = 0
                        }).run { saveConfig() }

                        broadcast(
                            Component.text("Created text display ")
                                .append(Component.text(name).color(NamedTextColor.GREEN))
                                .append(Component.text("."))
                        )
                    }
                }
            }
        }
    }
}
