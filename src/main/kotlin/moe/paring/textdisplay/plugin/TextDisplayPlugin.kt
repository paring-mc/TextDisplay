package moe.paring.textdisplay.plugin

import moe.paring.textdisplay.command.registerCommands
import moe.paring.textdisplay.events.DisplayManagerEventHandler
import moe.paring.textdisplay.manager.TextDisplayManager
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer

class TextDisplayPlugin : JavaPlugin() {
    val textManager = TextDisplayManager(this)

    val spawnDistance: Double = 160.0
    val despawnDistance: Double = 180.0

    override fun onEnable() {
        registerCommands()

        server.scheduler.runTaskTimer(this, Consumer { textManager.update() }, 0, 1)

        server.pluginManager.registerEvents(DisplayManagerEventHandler(textManager), this)

        server.onlinePlayers.forEach { player ->
            textManager.addPlayer(player)
        }
    }
}
