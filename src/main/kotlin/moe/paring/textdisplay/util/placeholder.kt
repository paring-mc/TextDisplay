package moe.paring.textdisplay.util

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun String.setPlaceholders(player: Player) : String {
    return if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
        PlaceholderAPI.setPlaceholders(player, this)
    } else {
        this
    }
}
