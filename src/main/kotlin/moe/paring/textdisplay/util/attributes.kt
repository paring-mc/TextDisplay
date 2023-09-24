package moe.paring.textdisplay.util

import io.github.monun.kommand.KommandArgument
import me.clip.placeholderapi.libs.kyori.adventure.text.format.NamedTextColor
import me.clip.placeholderapi.libs.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Color
import org.bukkit.entity.Display.Billboard
import org.bukkit.entity.TextDisplay
import org.bukkit.entity.TextDisplay.TextAlignment
import org.bukkit.util.Transformation
import java.util.EnumSet

data class DisplayAttribute<T>(
    val name: String,
    val defaultValue: T,
    val argType: KommandArgument<T>,
    val decode: (Any) -> T,
    val encode: (T) -> Any,
    val getter: TextDisplay.() -> T,
    val setter: TextDisplay.(T) -> Unit,
    val formatter: (T) -> TextComponent = { Component.text(it.toString()) }
) {
    @Suppress("UNCHECKED_CAST")
    fun encodeAny(value: Any?): Any {
        return encode(value as T)
    }

    @Suppress("UNCHECKED_CAST")
    fun set(display: TextDisplay, value: Any?) {
        setter(display, value as T)
    }

    @Suppress("UNCHECKED_CAST")
    fun format(value: Any?): TextComponent {
        return formatter(value as T)
    }
}

val displayAttributes = arrayOf<DisplayAttribute<*>>(
    DisplayAttribute(
        "billboard",
        Billboard.FIXED,
        KommandArgument.dynamicByEnum(
            EnumSet.allOf(Billboard::class.java)
        ),
        { Billboard.valueOf(it as String) },
        { it.toString() },
        { billboard },
        { billboard = it }
    ),
    DisplayAttribute(
        "alignment",
        TextAlignment.CENTER,
        KommandArgument.dynamicByEnum(
            EnumSet.allOf(TextAlignment::class.java)
        ),
        { TextAlignment.valueOf(it as String) },
        { it.toString() },
        { alignment },
        { alignment = it }
    ),
    DisplayAttribute(
        "defaultBackground",
        true,
        KommandArgument.bool(),
        { it as Boolean },
        { it },
        { isDefaultBackground },
        { isDefaultBackground = it }
    ),
    DisplayAttribute(
        "seeThrough",
        false,
        KommandArgument.bool(),
        { it as Boolean },
        { it },
        { isSeeThrough },
        { isSeeThrough = it }
    ),
    DisplayAttribute(
        "shadow",
        false,
        KommandArgument.bool(),
        { it as Boolean },
        { it },
        { isShadowed },
        { isShadowed = it }
    ),
    DisplayAttribute(
        "lineWidth",
        140,
        KommandArgument.int(),
        { it as Int },
        { it },
        { lineWidth },
        { lineWidth = it }
    )
)
