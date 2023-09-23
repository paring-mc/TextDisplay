package moe.paring.textdisplay.command

import io.github.monun.kommand.node.RootNode
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import net.kyori.adventure.text.Component


fun RootNode.reload(plugin: TextDisplayPlugin) {
    then("reload") {
        requires { sender.hasPermission("textdisplay.command.reload") }
        executes {
            plugin.reload()
            broadcast(
                Component.text("reloaded all texts!")
            ) { isOp }
        }
    }
}
