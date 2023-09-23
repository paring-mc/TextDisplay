package moe.paring.textdisplay.util

import me.clip.placeholderapi.PlaceholderAPI
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import org.bukkit.entity.Player

fun String.setPlaceholders(player: Player) : String {
    return if (TextDisplayPlugin.hasPlaceholderAPI) {
        PlaceholderAPI.setPlaceholders(player, this)
    } else {
        this
    }
}
