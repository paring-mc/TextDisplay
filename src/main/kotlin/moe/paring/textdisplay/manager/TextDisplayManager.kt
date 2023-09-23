package moe.paring.textdisplay.manager

import io.github.monun.tap.protocol.PacketContainer
import io.github.monun.tap.protocol.sendPacket
import moe.paring.textdisplay.entities.CustomTextDisplay
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import moe.paring.textdisplay.util.cmdRequire
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.entity.Player

class TextDisplayManager(private val plugin: TextDisplayPlugin) {
    private val displays = mutableMapOf<String, CustomTextDisplay>()

    fun add(name: String, location: Location, text: String) {
        cmdRequire(displays[name] == null) {
            Component.text("Text {name} already exists!")
                .replaceText { it.match("\\{name}").replacement(Component.text(name).color(NamedTextColor.YELLOW)) }
        }

        val display = CustomTextDisplay(plugin, name, location, text)
        displays[name] = display
        display.load()
    }
}
