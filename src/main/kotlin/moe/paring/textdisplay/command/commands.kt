package moe.paring.textdisplay.command

import io.github.monun.kommand.kommand
import moe.paring.textdisplay.plugin.TextDisplayPlugin

fun TextDisplayPlugin.registerCommands() {
    kommand {
        register("textdisplay") {
            requires { sender.hasPermission("textdisplay.command") }

            help()
            create(this@registerCommands)
            reload(this@registerCommands)
            delete(this@registerCommands)
            set()
        }
    }
}
