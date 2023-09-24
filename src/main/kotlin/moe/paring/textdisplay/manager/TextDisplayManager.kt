package moe.paring.textdisplay.manager

import moe.paring.textdisplay.entities.CustomTextDisplay
import moe.paring.textdisplay.handler.PlayerHandler
import moe.paring.textdisplay.persistence.DisplayConfig
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import moe.paring.textdisplay.util.cmdRequire
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

class TextDisplayManager(private val plugin: TextDisplayPlugin) {
    val displays = mutableMapOf<String, CustomTextDisplay>()
    private val players = mutableMapOf<Player, PlayerHandler>()

    fun add(name: String, config: DisplayConfig): CustomTextDisplay {
        cmdRequire(displays[name] == null) {
            Component.text("Text {name} already exists!")
                .replaceText { it.match("\\{name}").replacement(Component.text(name).color(NamedTextColor.YELLOW)) }
        }

        val display = CustomTextDisplay(plugin, name, config)
        displays[name] = display
        display.load()

        return display
    }

    fun update() {
        displays.values.forEach { it.load() }
        players.forEach { (_, handler) -> handler.update() }
    }

    fun addPlayer(player: Player) {
        players[player] = PlayerHandler(player, this, plugin)
    }

    fun removePlayer(player: Player, removeFromList: Boolean = true) {
        displays.forEach { (_, display) ->
            display.despawnTo(player)
            display.players.remove(player)
        }

        if (removeFromList) players.remove(player)
    }

    fun unloadAll() {
        players.keys.forEach {removePlayer(it, false) }
        players.clear()
        displays.values.forEach { it.unload() }
        displays.clear()
    }

    fun delete(display: CustomTextDisplay) {
        plugin.textsDir.resolve("${display.name}.yml").delete()
    }
}
