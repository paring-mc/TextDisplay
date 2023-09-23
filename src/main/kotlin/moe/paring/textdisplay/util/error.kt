package moe.paring.textdisplay.util

import io.github.monun.kommand.KommandContext
import io.github.monun.kommand.KommandSource
import io.github.monun.kommand.node.KommandNode
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class CommandError(val component: Component) : Throwable()

fun cmdRequire(value: Boolean, lazyError: () -> Component) {
    if (!value) throw CommandError(lazyError())
}

fun cmdRequireStr(value: Boolean, lazyError: () -> String) =
    cmdRequire(value) { Component.text(lazyError()) }

fun KommandNode.executesCatching(executes: KommandSource.(KommandContext) -> Unit) {
    executes {
        runCatching { executes(it) }.onFailure {
                if (it is CommandError) {
                    sender.sendMessage(it.component.color(NamedTextColor.RED))
                    return@executes
                }
                throw it
            }
    }
}
