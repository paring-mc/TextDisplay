package moe.paring.textdisplay.command

import io.github.monun.kommand.node.RootNode

fun RootNode.list() {
    then("list") {
        requires { sender.hasPermission("textdisplay.command.list") }

        executes {
            sender.sendMessage("test")
        }
    }
}