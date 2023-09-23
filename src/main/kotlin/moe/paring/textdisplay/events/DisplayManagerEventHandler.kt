package moe.paring.textdisplay.events

import moe.paring.textdisplay.manager.TextDisplayManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Suppress("unused")
class DisplayManagerEventHandler(private val textDisplayManager: TextDisplayManager) : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        textDisplayManager.addPlayer(e.player)
    }


    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        textDisplayManager.removePlayer(e.player)
    }
}