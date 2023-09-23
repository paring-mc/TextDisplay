package moe.paring.textdisplay.util

import io.github.monun.kommand.KommandArgument
import io.github.monun.kommand.StringType
import moe.paring.textdisplay.plugin.TextDisplayPlugin

val display = KommandArgument.dynamic(StringType.SINGLE_WORD) { _, input ->
    TextDisplayPlugin.instance.textManager.displays[input]
}.apply {
    suggests {
        suggest(TextDisplayPlugin.instance.textManager.displays.values, { it.name })
    }
}
