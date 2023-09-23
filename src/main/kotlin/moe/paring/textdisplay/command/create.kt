package moe.paring.textdisplay.command

import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.RootNode
import io.github.monun.kommand.wrapper.Position3D
import io.github.monun.kommand.wrapper.Rotation
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import moe.paring.textdisplay.util.executesCatching
import org.bukkit.Location
import org.bukkit.entity.Player

fun RootNode.create(plugin: TextDisplayPlugin) {
    then("create") {
        requires { sender.hasPermission("textdisplay.command.create") && sender is Player }

        then("name" to string()) {
            then("position" to position()) {
                then("rotation" to rotation()) {
                    then("text" to string(StringType.QUOTABLE_PHRASE)) {
                        executesCatching { context ->
                            val name: String by context
                            val position: Position3D by context
                            val rotation: Rotation by context
                            val text: String by context

                            plugin.textManager.add(name, position.toLocation(player.world, rotation), text)
                        }
                    }
                }
            }
        }
    }
}
