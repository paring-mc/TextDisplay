package moe.paring.textdisplay.manager

import moe.paring.textdisplay.entities.CustomTextDisplay
import moe.paring.textdisplay.handler.PlayerHandler
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import org.bukkit.Location
import org.bukkit.entity.Player

class TextDisplayManager(private val plugin: TextDisplayPlugin) {
    val displays = mutableMapOf<String, CustomTextDisplay>()
    private val players = mutableMapOf<Player, PlayerHandler>()

    fun add(name: String, location: Location, text: String) {
        // TODO enable this
//        cmdRequire(displays[name] == null) {
//            Component.text("Text {name} already exists!")
//                .replaceText { it.match("\\{name}").replacement(Component.text(name).color(NamedTextColor.YELLOW)) }
//        }

        val display = CustomTextDisplay(plugin, name, location, text)
        displays[name] = display
        display.load()
    }

    fun update() {
        players.forEach { (_, handler) -> handler.update() }
    }

    fun addPlayer(player: Player) {
        players[player] = PlayerHandler(player, this, plugin)
    }

    fun removePlayer(player: Player) {
        displays.forEach { (_, display) ->
            display.despawnTo(player)
            display.players.remove(player)
        }
        players.remove(player)
    }
}
